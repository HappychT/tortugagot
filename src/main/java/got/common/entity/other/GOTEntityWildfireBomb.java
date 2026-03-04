package got.common.entity.other;

import got.common.database.GOTRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class GOTEntityWildfireBomb extends EntityThrowable {

    public GOTEntityWildfireBomb(World world) {
        super(world);
    }

    public GOTEntityWildfireBomb(World world, EntityLivingBase thrower) {
        super(world, thrower);
    }

    @Override
    protected float getGravityVelocity() {
        return 0.05F;
    }

    @Override
    protected float func_70182_d() {
        return 0.8F;
    }


    @Override
    protected void onImpact(MovingObjectPosition mop) {
        if (mop.entityHit != null) {
            mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 3.0F);
        }

        if (!this.worldObj.isRemote) {
            int centerX = (int)this.posX;
            int centerY = (int)this.posY;
            int centerZ = (int)this.posZ;

            int radius = 2;

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    
                    if (this.rand.nextInt(100) < 48) {
                        
                        int targetX = centerX + x;
                        int targetY = centerY;
                        int targetZ = centerZ + z;

                        if (canPlaceFire(targetX, targetY, targetZ)) {
                            setFire(targetX, targetY, targetZ);
                        } 
                        else if (canPlaceFire(targetX, targetY + 1, targetZ)) {
                            setFire(targetX, targetY + 1, targetZ);
                        }
                        else if (canPlaceFire(targetX, targetY - 1, targetZ)) {
                            setFire(targetX, targetY - 1, targetZ);
                        }
                    }
                }
            }
            
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.glass", 1.0F, 1.0F);
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "fire.ignite", 1.0F, 1.0F);
            
            this.setDead();
        }
    }

    private boolean canPlaceFire(int x, int y, int z) {
        return this.worldObj.isAirBlock(x, y, z) && World.doesBlockHaveSolidTopSurface(this.worldObj, x, y - 1, z);
    }

    private void setFire(int x, int y, int z) {
        this.worldObj.setBlock(x, y, z, GOTRegistry.wildFire);
    }
}