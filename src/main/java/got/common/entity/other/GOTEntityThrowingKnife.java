package got.common.entity.other;

import got.common.item.weapon.GOTItemThrowingKnife;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class GOTEntityThrowingKnife extends GOTEntityProjectileBase {
    public int KnifeRotation;
    public int effectType;

    public GOTEntityThrowingKnife(World world) {
        super(world);
    }

    public GOTEntityThrowingKnife(World world, EntityLivingBase entityliving, EntityLivingBase target, ItemStack item, float charge, float inaccuracy) {
        super(world, entityliving, target, item, charge, inaccuracy);
    }

    public GOTEntityThrowingKnife(World world, EntityLivingBase entityliving, ItemStack item, float charge) {
        super(world, entityliving, item, charge);
    }

    public GOTEntityThrowingKnife(World world, ItemStack item, double d, double d1, double d2) {
        super(world, item, d, d1, d2);
    }

    @Override
    public float getBaseImpactDamage(Entity entity, ItemStack itemstack) {
        if (!isThrowingKnife())
            return 0.0f;
        float speed = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        float damage = ((GOTItemThrowingKnife) itemstack.getItem()).getRangedDamageMultiplier(itemstack, this.shootingEntity, entity);
        return speed * damage;
    }

    public boolean isThrowingKnife() {
        Item item = getProjectileItem().getItem();
        return item instanceof GOTItemThrowingKnife;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.inGround) {
            ++this.KnifeRotation;
            if (this.KnifeRotation > 9) {
                this.KnifeRotation = 0;
            }
            this.rotationPitch = this.KnifeRotation / 9.0f * 360.0f;
        }
        if (!isThrowingKnife()) {
            setDead();
        }

    }
}
