package noname.weapons.entity;

import noname.weapons.config.WeaponsConfig;
import noname.weapons.world.BlockDamageStorage;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

public class EntityStoneProjectile extends EntityThrowable {

    public EntityStoneProjectile(World world) {
        super(world);
    }

    public EntityStoneProjectile(World world, Entity shooter, double motionX, double motionY, double motionZ) {
        super(world, (EntityLivingBase) shooter);
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
    }

    @Override
    protected void onImpact(MovingObjectPosition mop) {
        if (!this.worldObj.isRemote) {
            double hitX = mop.hitVec.xCoord;
            double hitY = mop.hitVec.yCoord;
            double hitZ = mop.hitVec.zCoord;

            
            if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                Entity entityHit = mop.entityHit;
                if (entityHit instanceof EntityLivingBase) {
                    
                    float directDamage = WeaponsConfig.entityDirectDamage;
                    ((EntityLivingBase) entityHit).attackEntityFrom(
                            net.minecraft.util.DamageSource.generic,
                            directDamage
                    );
                }
            }

            
            if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int blockX = mop.blockX;
                int blockY = mop.blockY;
                int blockZ = mop.blockZ;

                
                float radius = WeaponsConfig.explosionRadius;
                int radiusInt = MathHelper.ceiling_double_int(radius);

                
                for (int x = -radiusInt; x <= radiusInt; x++) {
                    for (int y = -radiusInt; y <= radiusInt; y++) {
                        for (int z = -radiusInt; z <= radiusInt; z++) {
                            int currentX = blockX + x;
                            int currentY = blockY + y;
                            int currentZ = blockZ + z;

                            
                            double distance = Math.sqrt(x * x + y * y + z * z);
                            if (distance > radius) {
                                continue;
                            }

                            Block block = this.worldObj.getBlock(currentX, currentY, currentZ);
                            if (block == null || block == Blocks.air) {
                                continue;
                            }

                            
                            float damage = calculateDamage(distance, block);

                            
                            if (isWeakBlock(block)) {
                                
                                this.worldObj.setBlockToAir(currentX, currentY, currentZ);
                                this.worldObj.playAuxSFX(2001, currentX, currentY, currentZ,
                                        Block.getIdFromBlock(block) + (this.worldObj.getBlockMetadata(currentX, currentY, currentZ) << 12));
                                BlockDamageStorage.removeBlockDamage(this.worldObj, currentX, currentY, currentZ);
                            } else {
                                
                                BlockDamageStorage.addBlockDamage(this.worldObj, currentX, currentY, currentZ, damage);

                                
                                float totalDamage = BlockDamageStorage.getBlockDamage(this.worldObj, currentX, currentY, currentZ);
                                int metadata = this.worldObj.getBlockMetadata(currentX, currentY, currentZ);

                                
                                int harvestLevel = 0;
                                try {
                                    harvestLevel = block.getHarvestLevel(metadata);
                                } catch (Exception e) {
                                    
                                    harvestLevel = 0;
                                }

                                float maxDamage = getMaxDamageForBlock(harvestLevel);
                                if (maxDamage > 0 && totalDamage > 0) {
                                    applyCracksToBlock(this.worldObj, currentX, currentY, currentZ, totalDamage, maxDamage);
                                }

                                
                                if (totalDamage >= maxDamage) {
                                    this.worldObj.setBlockToAir(currentX, currentY, currentZ);
                                    this.worldObj.playAuxSFX(2001, currentX, currentY, currentZ,
                                            Block.getIdFromBlock(block) + (this.worldObj.getBlockMetadata(currentX, currentY, currentZ) << 12));
                                    BlockDamageStorage.removeBlockDamage(this.worldObj, currentX, currentY, currentZ);
                                }
                            }
                        }
                    }
                }

                
                dealSplashDamageToEntities(hitX, hitY, hitZ);
            }

            this.worldObj.createExplosion(this, hitX, hitY, hitZ, WeaponsConfig.explosionRadius, false);
            this.setDead();
        }
    }

    


    private void dealSplashDamageToEntities(double hitX, double hitY, double hitZ) {
        float splashRadius = WeaponsConfig.entitySplashRadius;
        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(
                hitX - splashRadius,
                hitY - splashRadius,
                hitZ - splashRadius,
                hitX + splashRadius,
                hitY + splashRadius,
                hitZ + splashRadius
        );

        
        List<Entity> entities = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, aabb);

        for (Entity entity : entities) {
            if (entity instanceof EntityLivingBase && entity != this.getThrower()) {
                EntityLivingBase livingEntity = (EntityLivingBase) entity;
                double distX = livingEntity.posX - hitX;
                double distY = livingEntity.posY - hitY;
                double distZ = livingEntity.posZ - hitZ;
                double distance = Math.sqrt(distX * distX + distY * distY + distZ * distZ);
                if (distance <= splashRadius) {
                    float baseSplashDamage = WeaponsConfig.entityDirectDamage;
                    float falloff = WeaponsConfig.entitySplashDamageFalloff;
                    float splashDamage = baseSplashDamage - (float) distance * falloff;
                    if (splashDamage > 0) {
                        livingEntity.attackEntityFrom(
                                net.minecraft.util.DamageSource.generic,
                                splashDamage
                        );
                    }
                }
            }
        }
    }

    public static void applyCracksToBlock(World world, int x, int y, int z, float totalDamage, float maxDamage) {
        if (maxDamage <= 0 || totalDamage <= 0) {
            return;
        }

        float progress = totalDamage / maxDamage;
        int crackProgress = (int) (progress * 10.0F);
        if (crackProgress > 10) crackProgress = 10;
        if (crackProgress < 1 && totalDamage > 0) crackProgress = 1;

        long hash = ((long)x << 20) ^ ((long)y << 10) ^ (long)z;
        int entityId = -(int)(hash & 0x7FFFFFFF);

        if (entityId == 0 || entityId == -1) {
            entityId = -(Math.abs(x) % 10000 * 10000 + Math.abs(y) % 256 * 256 + Math.abs(z) % 10000) - 2;
        }

        world.destroyBlockInWorldPartially(entityId, x, y, z, crackProgress);
    }

    


    private float calculateDamage(double distance, Block block) {
        float baseDamage = WeaponsConfig.baseDamage;
        float falloff = WeaponsConfig.damageFalloff;
        float distanceFloat = (float) distance;
        float damage = baseDamage - (distanceFloat * falloff);
        if (damage < 0) damage = 0;
        return damage;
    }

    


    private boolean isWeakBlock(Block block) {
        
        if (block == Blocks.dirt || block == Blocks.grass || block == Blocks.farmland) {
            return true;
        }

        
        if (block == Blocks.sand || block == Blocks.gravel) {
            return true;
        }

        
        if (block == Blocks.wool) {
            return true;
        }

        
        if (block == Blocks.wooden_slab || block == Blocks.wooden_button ||
                block == Blocks.wooden_pressure_plate || block == Blocks.fence) {
            return true;
        }

        
        if (block == Blocks.glass || block == Blocks.glass_pane) {
            return true;
        }

        
        if (block == Blocks.leaves || block == Blocks.leaves2) {
            return true;
        }

        
        if (block == Blocks.red_flower || block == Blocks.yellow_flower ||
                block == Blocks.tallgrass || block == Blocks.deadbush ||
                block == Blocks.brown_mushroom || block == Blocks.red_mushroom) {
            return true;
        }

        return false;
    }

    




    private float getMaxDamageForBlock(int harvestLevel) {
        return getMaxDamageForBlockStatic(harvestLevel);
    }

    


    public static float getMaxDamageForBlockStatic(int harvestLevel) {
        if (harvestLevel <= 0) {
            return 3.0F * WeaponsConfig.baseDamage;
        }
        return (harvestLevel + 1) * WeaponsConfig.damagePerHarvestLevel;
    }

    @Override
    protected float getGravityVelocity() {
        return 0.05F;
    }

    protected float getVelocity() {
        return 1.5F;
    }
}