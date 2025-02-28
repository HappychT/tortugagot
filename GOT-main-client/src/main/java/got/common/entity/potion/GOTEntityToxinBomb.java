package got.common.entity.potion;

import got.GOT;
import got.common.item.potions.GOTItemToxinBomb;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class GOTEntityToxinBomb extends GOTEntityLingeringPotion {

    public GOTEntityToxinBomb(World world) {
        super(world);
    }

    public GOTEntityToxinBomb(World world, double x, double y, double z, ItemStack stack) {
        super(world, x, y, z, stack);
        this.stack = stack;
    }

    public GOTEntityToxinBomb(World world, EntityLivingBase thrower, ItemStack stack) {
        super(world, thrower, stack);
        this.stack = stack;
    }

    @Override
    public void onImpact(MovingObjectPosition mop) {
        if (this.worldObj.isRemote)
            return;
        this.worldObj.spawnEntityInWorld(new GOTEntityLingeringBombEffect(this.worldObj, this));
        if(this.stack.getItem() instanceof GOTItemToxinBomb) {
            GOT.proxy.addFXPotion(this.worldObj, this.stack, 0, (int)Math.round(this.posX), (int)Math.round(this.posY), (int)Math.round(this.posZ),
                    Potion.potionTypes[((GOTItemToxinBomb)this.stack.getItem()).getEffects(this.stack).get(0).getPotionID()].getLiquidColor());
        }
        setDead();
    }

    @Override
    protected float getGravityVelocity() {
        return 0.025f;
    }

    @Override
    protected float func_70182_d() {
        return 0.65F;
    }

}