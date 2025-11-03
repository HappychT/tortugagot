package got.common.entity.other;

import got.common.database.GOTEffects;
import got.common.item.weapon.GOTItemCrossbowBolt;
import got.common.item.weapon.GOTItemSword;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class GOTEntityCrossbowBolt extends GOTEntityProjectileBase {
    public static float BOLT_RELATIVE_TO_ARROW = 2.0f;
    public double boltDamageFactor = 2.0;
    public boolean isBleeding = false;
    public boolean isHarpoonBolt = false;

    public GOTEntityCrossbowBolt(World world) {
        super(world);
    }

    public GOTEntityCrossbowBolt(World world, EntityLivingBase entityliving, EntityLivingBase target, ItemStack item, float charge, float inaccuracy) {
        super(world, entityliving, target, item, charge, inaccuracy);
    }

    public GOTEntityCrossbowBolt(World world, EntityLivingBase entityliving, ItemStack item, float charge) {
        super(world, entityliving, item, charge);
    }

    public GOTEntityCrossbowBolt(World world, ItemStack item, double d, double d1, double d2) {
        super(world, item, d, d1, d2);
    }

    @Override
    public float getBaseImpactDamage(Entity entity, ItemStack itemstack) {
        float speed = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        return speed * 2.0f * (float) this.boltDamageFactor;
    }

    @Override
    public int maxTicksInGround() {
        return 1200;
    }

    @Override
    public void onCollideWithTarget(Entity entity) {
        Item item;
        ItemStack itemstack;
        if (!this.worldObj.isRemote && entity instanceof EntityLivingBase && (itemstack = getProjectileItem()) != null && (item = itemstack.getItem()) instanceof GOTItemCrossbowBolt) {

            if (this.isHarpoonBolt && entity instanceof EntityPlayer && this.getThrower() != null && this.getThrower() instanceof EntityPlayer) {
                EntityPlayer target = (EntityPlayer) entity;
                EntityPlayer shooter = (EntityPlayer) this.getThrower();

                if (target != shooter) {
                    double vecX = shooter.posX - target.posX;
                    double vecY = shooter.posY - target.posY;
                    double vecZ = shooter.posZ - target.posZ;

                    double distance = (double)MathHelper.sqrt_double(vecX * vecX + vecY * vecY + vecZ * vecZ);

                    if (distance > 0) {
                        double normX = vecX / distance;
                        double normY = vecY / distance;
                        double normZ = vecZ / distance;

                        double pullDistance = Math.min(distance, 6.0);
                        double pullVelocity = pullDistance * 0.35;
                        double pullMotionY = normY * pullVelocity + 0.2;

                        target.motionX = normX * pullVelocity;
                        target.motionY = pullMotionY;
                        target.motionZ = normZ * pullVelocity;

                        target.velocityChanged = true;
                    }
                }
            }

            if(this.isBleeding && this.worldObj.rand.nextInt(100) <= 5) {
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(GOTEffects.bleeding.id, 80));
            }
            if(((GOTItemCrossbowBolt) item).isPoisoned) {
                GOTItemSword.applyStandardPoison((EntityLivingBase) entity);
            }
        }
        super.onCollideWithTarget(entity);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("boltDamageFactor")) {
            this.boltDamageFactor = nbt.getDouble("boltDamageFactor");
        }
        if (nbt.hasKey("IsHarpoon")) {
            this.isHarpoonBolt = nbt.getBoolean("IsHarpoon");
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setDouble("boltDamageFactor", this.boltDamageFactor);
        if (this.isHarpoonBolt) {
            nbt.setBoolean("IsHarpoon", true);
        }
    }
}