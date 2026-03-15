package noname.weapons.entity;

import noname.weapons.config.WeaponsConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

public class EntityBalistaProjectile extends EntityThrowable {

    private double startX;
    private double startY;
    private double startZ;
    private boolean startInitialized = false;

    public EntityBalistaProjectile(World world) {
        super(world);
    }

    public EntityBalistaProjectile(World world, Entity shooter, double motionX, double motionY, double motionZ) {
        super(world, (EntityLivingBase) shooter);
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!this.worldObj.isRemote) {
            
            if (!startInitialized) {
                startX = this.posX;
                startY = this.posY;
                startZ = this.posZ;
                startInitialized = true;
            }

            
            double distanceTraveled = Math.sqrt(
                    Math.pow(this.posX - startX, 2) +
                    Math.pow(this.posY - startY, 2) +
                    Math.pow(this.posZ - startZ, 2)
            );

            if (distanceTraveled >= WeaponsConfig.maxRangeBalsita) {
                
                this.setDead();
                return;
            }
        }
    }

    public void setStartPosition(double x, double y, double z) {
        this.startX = x;
        this.startY = y;
        this.startZ = z;
        this.startInitialized = true;
    }

    @Override
    protected void onImpact(MovingObjectPosition mop) {
        if (!this.worldObj.isRemote) {
            double hitX = mop.hitVec.xCoord;
            double hitY = mop.hitVec.yCoord;
            double hitZ = mop.hitVec.zCoord;

            Entity hitEntity = null;
            
            if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                hitEntity = mop.entityHit;
            }

            
            
            dealSplashDamageToEntities(hitX, hitY, hitZ, hitEntity);

            
            this.setDead();
        }
    }

    



    private void dealSplashDamageToEntities(double hitX, double hitY, double hitZ, Entity hitEntity) {
        float splashRadius = WeaponsConfig.entitySplashRadiusBalista;
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
                
                
                boolean isDirectHit = (hitEntity != null && entity == hitEntity);
                double distX = livingEntity.posX - hitX;
                double distY = livingEntity.posY - hitY;
                double distZ = livingEntity.posZ - hitZ;
                double distance = Math.sqrt(distX * distX + distY * distY + distZ * distZ);
                
                if (isDirectHit || distance <= splashRadius) {
                    
                    float damage = WeaponsConfig.damageBalista;
                    if (damage > 0) {
                        livingEntity.attackEntityFrom(
                                net.minecraft.util.DamageSource.generic,
                                damage
                        );
                    }
                }
            }
        }
    }

    @Override
    protected float getGravityVelocity() {
        return 0.02F; 
    }

    protected float getVelocity() {
        return 2.5F;
    }
}

