package got.common.entity.other;

import java.util.List;

import com.google.common.collect.Lists;

import got.common.database.GOTEffects;
import got.common.network.GOTPacketHandler;
import got.common.network.GOTPacketWeaponFX;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class GOTEntityThrowingBomb extends EntityThrowable {
    private List<PotionEffect> list;
    private double[] radius = new double[3];
    private float extraDamage = 0.0f;

    public GOTEntityThrowingBomb(World world) {
        super(world);
    }

    public GOTEntityThrowingBomb(World world, EntityLivingBase entity, List<PotionEffect> list, double[] radius) {
        super(world, entity);
        this.list = list;
        this.radius = radius;
    }

    public GOTEntityThrowingBomb(World world, double x, double y, double z, List<PotionEffect> list, double[] radius) {
        super(world, x, y, z);
        this.list = list;
        this.radius = radius;
    }

    @Override
    protected float getGravityVelocity() {
        return 0.025f;
    }

    @Override
    protected float func_70182_d() {
        return 0.65F;
    }

    @Override
    protected float func_70183_g() {
        return -20.0F;
    }

    public void setPotionList(List<PotionEffect> list) {
        this.list = list;
    }

    public GOTEntityThrowingBomb setDamage(float damage) {
        this.extraDamage = damage;
        return this;
    }

    public List<PotionEffect> getPotionDamage() {
        if(this.list == null) return Lists.newArrayList();
        return this.list;
    }

    @Override
    protected void onImpact(MovingObjectPosition position) {
        AxisAlignedBB aabb = this.boundingBox.expand(this.radius[0], this.radius[1], this.radius[2]);
        List<EntityLivingBase> list1 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
        if (!this.worldObj.isRemote) {
            if (this.list != null && !this.list.isEmpty()) {
                if (list1 != null && !list1.isEmpty()) {
                    for (EntityLivingBase entitylivingbase : list1) {
                        double d0 = getDistanceSqToEntity(entitylivingbase);

                        if (d0 < 16.0D) {
                            double d1 = 1.0D - Math.sqrt(d0) / 4.0D;

                            if (entitylivingbase == position.entityHit) {
                                d1 = 1.0D;
                            }

                            for (PotionEffect effect : this.list) {
                                int i = effect.getPotionID();

                                if (Potion.potionTypes[i].isInstant()) {
                                    Potion.potionTypes[i].affectEntity(getThrower(), entitylivingbase, effect.getAmplifier(), d1);
                                } else {
                                    int j = (int)(d1 * (double)effect.getDuration() + 0.5D);

                                    if (j > 20) {
                                        entitylivingbase.addPotionEffect(new PotionEffect(i, effect.getDuration(), effect.getAmplifier()));
                                    }
                                }
                                if(effect.getPotionID() == GOTEffects.freeze.id) {
                                    GOTPacketWeaponFX packet = new GOTPacketWeaponFX(GOTPacketWeaponFX.Type.CHILLING, this);
                                    GOTPacketHandler.networkWrapper.sendToAllAround(packet, GOTPacketHandler.nearEntity(this, 64.0D));
                                }
                            }
                        }
                    }
                }
            }
        }
        if(this.extraDamage > 0.0f) {
            System.out.println("HUUUUUUUUUUUUUUUUUUUUUUUUI");
            if (list1 != null && !list1.isEmpty()) {
                for (EntityLivingBase entitylivingbase : list1) {
                    entitylivingbase.attackEntityFrom(DamageSource.causeThrownDamage(this, entitylivingbase), this.extraDamage);
                }
            }
        }
        setDead();
        for(int i = 0; i <= 6; i++) {
            this.worldObj.spawnParticle("smoke", this.posX + 0.5, this.posY + 0.2, this.posZ, 0.0, 0.0, 0.0);
            this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.2, this.posZ + 0.5, 0.0, 0.0, 0.0);
            this.worldObj.spawnParticle("smoke", this.posX - 0.5, this.posY + 0.2, this.posZ, 0.0, 0.0, 0.0);
            this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.2, this.posZ - 0.5, 0.0, 0.0, 0.0);
            this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.2, this.posZ, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);

        if (nbt.hasKey("Potions", 10)) {
            List<PotionEffect> newList = Lists.newArrayList();
            NBTTagCompound potions = nbt.getCompoundTag("Potions");
            for(Object obj : potions.func_150296_c()) {
                if(obj instanceof NBTTagCompound) {
                    newList.add(PotionEffect.readCustomPotionEffectFromNBT((NBTTagCompound)obj));
                }
            }
            setPotionList(newList);
        }

        if (this.list == null) {
            setDead();
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);

        if (this.list != null) {
            NBTTagCompound potions = new NBTTagCompound();
            for(PotionEffect eff : this.list) {
                potions.setTag(eff.getEffectName(), eff.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }
            nbt.setTag("Potions", potions);
        }
    }
}
