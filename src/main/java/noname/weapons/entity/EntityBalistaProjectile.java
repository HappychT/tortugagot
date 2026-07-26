package noname.weapons.entity;

import noname.weapons.config.WeaponsConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

public class EntityBalistaProjectile extends EntityThrowable {

    private double startX;
    private double startY;
    private double startZ;
    private boolean startInitialized = false;
    private float maxRange = WeaponsConfig.maxRangeBalsita;
    private boolean inGround = false;
    private int groundTicks = 0;
    private static final int GROUND_LIFE = 600;
    private float stuckYaw = 0;
    private float stuckPitch = 0;

    private EntityLivingBase thrower;

    public EntityBalistaProjectile(World world) {
        super(world);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    public void setThrower(EntityLivingBase entity) {
        this.thrower = entity;
    }

    @Override
    public EntityLivingBase getThrower() {
        return this.thrower;
    }

    @Override
    public void onUpdate() {
        if (inGround) {
            this.motionX = 0;
            this.motionY = 0;
            this.motionZ = 0;
            this.noClip = true;
            if (!this.worldObj.isRemote) {
                groundTicks++;
                if (groundTicks >= GROUND_LIFE) {
                    this.setDead();
                }
            }
            return;
        }

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

            if (distanceTraveled >= this.maxRange) {
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

    public void setMaxRange(float range) {
        this.maxRange = range;
    }

    @Override
    protected void onImpact(MovingObjectPosition mop) {
        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            double hDist = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.stuckYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0 / Math.PI);
            this.stuckPitch = (float) (-Math.atan2(this.motionY, hDist) * 180.0 / Math.PI);
            this.motionX = 0;
            this.motionY = 0;
            this.motionZ = 0;
            this.inGround = true;
            this.noClip = true;
        }

        if (!this.worldObj.isRemote) {
            double hitX = mop.hitVec.xCoord;
            double hitY = mop.hitVec.yCoord;
            double hitZ = mop.hitVec.zCoord;

            Entity hitEntity = null;
            if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                hitEntity = mop.entityHit;
            }

            dealSplashDamageToEntities(hitX, hitY, hitZ, hitEntity);

            if (mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
                this.setDead();
            }
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

        List<Entity> entities = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, aabb);

        for (Entity entity : entities) {
            if (entity == this.getThrower()) continue;
            if (!(entity instanceof EntityLivingBase) && !(entity instanceof EntityBatteringRam)) continue;

            boolean isDirectHit = (hitEntity != null && entity == hitEntity);
            double distX = entity.posX - hitX;
            double distY = entity.posY - hitY;
            double distZ = entity.posZ - hitZ;
            double distance = Math.sqrt(distX * distX + distY * distY + distZ * distZ);

            if (isDirectHit || distance <= splashRadius) {
                float damage = WeaponsConfig.damageBalista;
                if (damage > 0) {
                    entity.attackEntityFrom(
                            DamageSource.causeThrownDamage(this, getThrower()),
                            damage
                    );
                }
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("InGround", this.inGround);
        nbt.setInteger("GroundTicks", this.groundTicks);
        nbt.setFloat("StuckYaw", this.stuckYaw);
        nbt.setFloat("StuckPitch", this.stuckPitch);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.inGround = nbt.getBoolean("InGround");
        this.groundTicks = nbt.getInteger("GroundTicks");
        this.stuckYaw = nbt.getFloat("StuckYaw");
        this.stuckPitch = nbt.getFloat("StuckPitch");
        if (this.inGround) {
            this.noClip = true;
        }
    }

    public boolean isInGround() {
        return this.inGround;
    }

    public float getStuckYaw() {
        return this.stuckYaw;
    }

    public float getStuckPitch() {
        return this.stuckPitch;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public void applyEntityCollision(Entity entity) {
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entity) {
        return null;
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return null;
    }

    @Override
    protected float getGravityVelocity() {
        return 0.02F;
    }

    protected float getVelocity() {
        return 2.5F;
    }
}

