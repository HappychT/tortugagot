package got.common.entity.potion;

import java.util.List;

import got.GOT;
import got.common.item.other.GOTItemCustomPotion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class GOTEntityCustomPotion extends EntityThrowable {
    private ItemStack potionDamage;

    public GOTEntityCustomPotion(World p_i1788_1_) {
        super(p_i1788_1_);
    }

    public GOTEntityCustomPotion(World p_i1790_1_, EntityLivingBase p_i1790_2_, ItemStack p_i1790_3_) {
        super(p_i1790_1_, p_i1790_2_);
        this.potionDamage = p_i1790_3_;
    }

    public GOTEntityCustomPotion(World p_i1792_1_, double p_i1792_2_, double p_i1792_4_, double p_i1792_6_, ItemStack p_i1792_8_) {
        super(p_i1792_1_, p_i1792_2_, p_i1792_4_, p_i1792_6_);
        this.potionDamage = p_i1792_8_;
    }

    /**
     * Gets the amount of gravity to apply to the thrown entity with each tick.
     */
    @Override
    protected float getGravityVelocity() {
        return 0.05F;
    }

    @Override
    protected float func_70182_d() {
        return 0.5F;
    }

    @Override
    protected float func_70183_g() {
        return -20.0F;
    }

    public void setPotionDamage(int p_82340_1_) {
        if (this.potionDamage == null) {
            this.potionDamage = new ItemStack(Items.potionitem, 1, 0);
        }

        this.potionDamage.setItemDamage(p_82340_1_);
    }

    public int getPotionDamage() {
        if (this.potionDamage == null) {
            this.potionDamage = new ItemStack(Items.potionitem, 1, 0);
        }

        return this.potionDamage.getItemDamage();
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    @Override
    protected void onImpact(MovingObjectPosition p_70184_1_) {
        if (!this.worldObj.isRemote && this.potionDamage.getItem() instanceof GOTItemCustomPotion) {
            List<PotionEffect> list = ((GOTItemCustomPotion)this.potionDamage.getItem()).getEffects(this.potionDamage);

            if (list != null && !list.isEmpty()) {
                AxisAlignedBB axisalignedbb = this.boundingBox.expand(4.0D, 2.0D, 4.0D);
                List<EntityLivingBase> list1 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

                if (list1 != null && !list1.isEmpty()) {
                    for (EntityLivingBase entitylivingbase : list1) {
                        double d0 = getDistanceSqToEntity(entitylivingbase);

                        if (d0 < 16.0D) {
                            double d1 = 1.0D - Math.sqrt(d0) / 4.0D;

                            if (entitylivingbase == p_70184_1_.entityHit) {
                                d1 = 1.0D;
                            }

                            for (PotionEffect potioneffect : list) {
                                int i = potioneffect.getPotionID();

                                if (Potion.potionTypes[i].isInstant()) {
                                    Potion.potionTypes[i].affectEntity(getThrower(), entitylivingbase, potioneffect.getAmplifier(), d1);
                                } else {
                                    int j = (int)(d1 * (double)potioneffect.getDuration() + 0.5D);

                                    if (j > 20) {
                                        entitylivingbase.addPotionEffect(new PotionEffect(i, j, potioneffect.getAmplifier()));
                                    }
                                }
                            }
                        }
                    }
                }
                GOT.proxy.addFXPotion(this.worldObj, this.potionDamage, 0, (int)Math.round(this.posX), (int)Math.round(this.posY), (int)Math.round(this.posZ), Potion.potionTypes[list.get(0).getPotionID()].getLiquidColor());
            }

            setDead();
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
        super.readEntityFromNBT(p_70037_1_);

        if (p_70037_1_.hasKey("Potion", 10)) {
            this.potionDamage = ItemStack.loadItemStackFromNBT(p_70037_1_.getCompoundTag("Potion"));
        } else {
            setPotionDamage(p_70037_1_.getInteger("potionValue"));
        }

        if (this.potionDamage == null) {
            setDead();
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
        super.writeEntityToNBT(p_70014_1_);

        if (this.potionDamage != null) {
            p_70014_1_.setTag("Potion", this.potionDamage.writeToNBT(new NBTTagCompound()));
        }
    }
}