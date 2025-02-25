package got.common.entity.other;

import got.common.item.weapon.GOTItemSpear;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class GOTEntitySpear extends GOTEntityProjectileBase {
    public GOTEntitySpear(World world) {
        super(world);
    }

    public GOTEntitySpear(World world, EntityLivingBase entityliving, EntityLivingBase target, ItemStack item, float charge, float inaccuracy) {
        super(world, entityliving, target, item, charge, inaccuracy);
    }

    public GOTEntitySpear(World world, EntityLivingBase entityliving, ItemStack item, float charge) {
        super(world, entityliving, item, charge);
    }

    public GOTEntitySpear(World world, ItemStack item, double d, double d1, double d2) {
        super(world, item, d, d1, d2);
    }

    @Override
    public float getBaseImpactDamage(Entity entity, ItemStack itemstack) {
        float speed = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        float damage = ((GOTItemSpear) itemstack.getItem()).getRangedDamageMultiplier(itemstack, this.shootingEntity, entity);
        return speed * damage;
    }

    public static class GOTEntityThiefKnife extends GOTEntitySpear {
        public GOTEntityThiefKnife(World world) {
            super(world);
        }

        public GOTEntityThiefKnife(World world, EntityLivingBase entityliving, EntityLivingBase target, ItemStack item, float charge, float inaccuracy) {
            super(world, entityliving, target, item, charge, inaccuracy);
        }

        public GOTEntityThiefKnife(World world, EntityLivingBase entityliving, ItemStack item, float charge) {
            super(world, entityliving, item, charge);
        }

        public GOTEntityThiefKnife(World world, ItemStack item, double d, double d1, double d2) {
            super(world, item, d, d1, d2);
        }
    }
}
