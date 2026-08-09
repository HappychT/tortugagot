package noname.weapons.entity;

import brain.factions.servers.SiegeActivationManager;
import com.tortugagot.togcore.technology.TOGEngineeringTechnology;
import noname.weapons.RegItem;
import noname.weapons.config.WeaponsConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityBalista extends EntityLivingBase {
    private double anchorX;
    private double anchorY;
    private double anchorZ;
    private float anchorYaw;
    private boolean anchorSet = false;

    private static final float MIN_PITCH = 0.0F;
    private static final float MAX_PITCH = 30.0F;
    private static final float PITCH_SPEED = 3.0F;

    private int reloadTimer = 0;
    private boolean isReloading = false;
    private boolean isLoaded = false;
    private EntityPlayer rider;
    private boolean lastSwingState = false;

    public EntityBalista(World world) {
        super(world);
        this.setSize(2.2F, 1.2F);
        this.preventEntitySpawning = true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(WeaponsConfig.maxHealthBalista);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(17, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(18, Integer.valueOf(0));
        this.dataWatcher.addObject(19, Float.valueOf(0.0F));
        this.dataWatcher.addObject(20, Float.valueOf(0.0F));
        this.dataWatcher.addObject(21, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(26, Float.valueOf(0.0F));
        this.dataWatcher.addObject(27, Float.valueOf(0.0F));
        this.dataWatcher.addObject(28, Float.valueOf(0.6F));
    }

    private static final float RANGE_MIN = 0.0F;
    private static final float RANGE_MAX = 1.0F;
    private static final float RANGE_STEP = 0.015F;
    private static final double SPEED_MIN_FACTOR = 0.3D;
    private static final float RANGE_MIN_FACTOR = 0.05F;

    @Override
    public void onUpdate() {
        stabilizeIfUnsupported();
        super.onUpdate();
        updateAnchor();
        lockToAnchor();
        stabilizeIfUnsupported();

        if (!this.worldObj.isRemote) {
            if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) this.riddenByEntity;
                this.rider = player;
                if (!TOGEngineeringTechnology.canUseSiegeWeapon(player, this)) {
                    player.mountEntity(null);
                    this.rider = null;
                    lastSwingState = false;
                    if (isReloading) {
                        stopReloading();
                    }
                    TOGEngineeringTechnology.notifySiegeWeaponBlocked(player, this);
                    return;
                }

                updateControlledYaw(player.rotationYaw, WeaponsConfig.rotationSpeedBalista);
                this.prevRotationYaw = this.rotationYaw;
                this.rotationYaw = this.anchorYaw;
                this.dataWatcher.updateObject(19, Float.valueOf(this.rotationYaw));
                this.dataWatcher.updateObject(20, Float.valueOf(this.prevRotationYaw));
                this.prevRotationPitch = this.rotationPitch;
                float targetPitch = MathHelper.clamp_float(player.rotationPitch, MIN_PITCH, MAX_PITCH);
                float pitchDiff = targetPitch - this.rotationPitch;
                if (pitchDiff > PITCH_SPEED) pitchDiff = PITCH_SPEED;
                else if (pitchDiff < -PITCH_SPEED) pitchDiff = -PITCH_SPEED;
                this.rotationPitch += pitchDiff;
                this.dataWatcher.updateObject(26, Float.valueOf(this.rotationPitch));
                this.dataWatcher.updateObject(27, Float.valueOf(this.prevRotationPitch));

                float range = this.dataWatcher.getWatchableObjectFloat(28);
                if (player.moveForward > 0.1F) range = Math.min(RANGE_MAX, range + RANGE_STEP);
                else if (player.moveForward < -0.1F) range = Math.max(RANGE_MIN, range - RANGE_STEP);
                this.dataWatcher.updateObject(28, Float.valueOf(range));

                if (player.isSwingInProgress && !lastSwingState && isReadyToFire()) {
                    fire();
                }
                lastSwingState = player.isSwingInProgress;

                if (!isLoaded && hasAmmo(player)) {
                    if (!isReloading) {
                        if (consumeAmmo(player)) {
                            startReloading();
                        }
                    }

                    if (isReloading) {
                        reloadTimer++;
                        this.dataWatcher.updateObject(18, Integer.valueOf(reloadTimer));

                        if (reloadTimer >= TOGEngineeringTechnology.getReloadTime(WeaponsConfig.reloadTimeBalista, player)) {
                            completeReload();
                        }
                    }
                } else if (!hasAmmo(player) && isReloading) {
                    stopReloading();
                }
            } else {
                this.prevRotationYaw = this.rotationYaw;
                this.dataWatcher.updateObject(19, Float.valueOf(this.rotationYaw));
                this.dataWatcher.updateObject(20, Float.valueOf(this.prevRotationYaw));
                this.prevRotationPitch = this.rotationPitch;
                this.dataWatcher.updateObject(26, Float.valueOf(this.rotationPitch));
                this.dataWatcher.updateObject(27, Float.valueOf(this.prevRotationPitch));
                this.rider = null;
                lastSwingState = false;

                if (isReloading) {
                    stopReloading();
                }
            }
        } else {

            this.prevRotationYaw = this.dataWatcher.getWatchableObjectFloat(20);
            this.rotationYaw = this.dataWatcher.getWatchableObjectFloat(19);
            this.prevRotationPitch = this.dataWatcher.getWatchableObjectFloat(27);
            this.rotationPitch = this.dataWatcher.getWatchableObjectFloat(26);
            isReloading = (this.dataWatcher.getWatchableObjectByte(17) & 1) != 0;
            reloadTimer = this.dataWatcher.getWatchableObjectInt(18);
        }
    }

    private void updateAnchor() {
        if (!anchorSet) {
            anchorX = posX;
            anchorY = posY;
            anchorZ = posZ;
            anchorYaw = rotationYaw;
            anchorSet = true;
        }
    }

    private void lockToAnchor() {
        if (!anchorSet) {
            return;
        }
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        prevPosX = posX = anchorX;
        prevPosY = posY = anchorY;
        prevPosZ = posZ = anchorZ;
        prevRotationYaw = rotationYaw = anchorYaw;
        fallDistance = 0.0F;
        setPosition(anchorX, anchorY, anchorZ);
    }

    private void stabilizeIfUnsupported() {
        int blockX = MathHelper.floor_double(this.posX);
        int blockY = MathHelper.floor_double(this.posY - 0.1D);
        int blockZ = MathHelper.floor_double(this.posZ);
        if (this.worldObj.isAirBlock(blockX, blockY, blockZ)) {
            this.motionY = 0.0D;
            this.fallDistance = 0.0F;
            this.onGround = true;
        }
    }

    private void updateControlledYaw(float targetYaw, float maxStep) {
        float yawDiff = MathHelper.wrapAngleTo180_float(targetYaw - this.anchorYaw);
        if (yawDiff > maxStep) {
            yawDiff = maxStep;
        } else if (yawDiff < -maxStep) {
            yawDiff = -maxStep;
        }
        this.anchorYaw += yawDiff;
    }

    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
    }

    private void startReloading() {
        isReloading = true;
        isLoaded = false;
        reloadTimer = 0;
        this.dataWatcher.updateObject(17, Byte.valueOf((byte)1));
        this.dataWatcher.updateObject(18, Integer.valueOf(0));
        this.dataWatcher.updateObject(21, Byte.valueOf((byte)0));
    }

    private void stopReloading() {
        isReloading = false;
        reloadTimer = 0;
        this.dataWatcher.updateObject(17, Byte.valueOf((byte)0));
        this.dataWatcher.updateObject(18, Integer.valueOf(0));
    }

    private void completeReload() {
        stopReloading();
        isLoaded = true;
        this.dataWatcher.updateObject(21, Byte.valueOf((byte)1));
    }

    public boolean isReadyToFire() {
        return isLoaded && !isReloading;
    }

    public void tryFire() {
        if (isReadyToFire()) {
            fire();
        }
    }

    public void fire() {
        if (!this.worldObj.isRemote && this.riddenByEntity != null) {
            //if (!SiegeActivationManager.getInstance().isSiegeActive(this.worldObj.provider.dimensionId, this.posX, this.posY, this.posZ)) return;
            EntityPlayer player = (EntityPlayer) this.riddenByEntity;
            if (!TOGEngineeringTechnology.canUseSiegeWeapon(player, this)) {
                TOGEngineeringTechnology.notifySiegeWeaponBlocked(player, this);
                return;
            }
            if (!isReadyToFire()) return;

            float yaw = this.rotationYaw;
            float pitch = this.rotationPitch;

            float yawRad = yaw / 180.0F * (float) Math.PI;
            float pitchRad = pitch / 180.0F * (float) Math.PI;
            double dirX = -MathHelper.sin(yawRad) * MathHelper.cos(pitchRad);
            double dirY = -MathHelper.sin(pitchRad);
            double dirZ = MathHelper.cos(yawRad) * MathHelper.cos(pitchRad);

            float rangePower = this.dataWatcher.getWatchableObjectFloat(28);
            double speedFactor = SPEED_MIN_FACTOR + (1.0D - SPEED_MIN_FACTOR) * rangePower;
            double speed = WeaponsConfig.BalistaProjectileSpeed * speedFactor;

            EntityBalistaProjectile projectile = new EntityBalistaProjectile(this.worldObj);
            projectile.setThrower(player);
            projectile.setMaxRange(WeaponsConfig.maxRangeBalsita * (RANGE_MIN_FACTOR + (1.0F - RANGE_MIN_FACTOR) * rangePower));

            double muzzleOffset = 3.0D;
            double originX = this.posX;
            double originY = this.posY + 1.0D;
            double originZ = this.posZ;
            double shootX = originX + dirX * muzzleOffset;
            double shootY = originY + dirY * muzzleOffset;
            double shootZ = originZ + dirZ * muzzleOffset;

            Vec3 start = Vec3.createVectorHelper(originX, originY, originZ);
            Vec3 end = Vec3.createVectorHelper(shootX, shootY, shootZ);
            MovingObjectPosition rayTrace = this.worldObj.rayTraceBlocks(start, end);
            if (rayTrace != null && rayTrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                return;
            }

            projectile.setPosition(shootX, shootY, shootZ);
            projectile.setStartPosition(shootX, shootY, shootZ);
            projectile.motionX = dirX * speed;
            projectile.motionY = dirY * speed;
            projectile.motionZ = dirZ * speed;
            this.worldObj.spawnEntityInWorld(projectile);

            isLoaded = false;
            this.dataWatcher.updateObject(21, Byte.valueOf((byte)0));
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if (this.isDead) {
            return false;
        }
        if (damageSource.getSourceOfDamage() instanceof EntityArrow) {
            return false;
        }
        if (damageSource == DamageSource.inFire || damageSource == DamageSource.onFire ||
                damageSource.isFireDamage()) {
            return false;
        }

        float reducedDamage;
        if (damageSource.getSourceOfDamage() instanceof EntityBalistaProjectile) {
            reducedDamage = damage;
        } else {
            reducedDamage = damage * (1.0F - Math.min(10.0F / 25.0F, 0.8F));
        }

        this.setHealth(this.getHealth() - reducedDamage);

        if (this.getHealth() <= 0) {
            this.setDead();
            return true;
        }

        return true;
    }

    @Override
    public boolean isEntityInvulnerable() {
        return false;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean interactFirst(EntityPlayer player) {
        if (!this.worldObj.isRemote && !TOGEngineeringTechnology.canUseSiegeWeapon(player, this)) {
            TOGEngineeringTechnology.notifySiegeWeaponBlocked(player, this);
            return true;
        }
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer &&
                this.riddenByEntity != player) {
            return true;
        } else {
            if (!this.worldObj.isRemote) {
                player.mountEntity(this);
            }
            return true;
        }
    }

    @Override
    public void updateRiderPosition() {
        if (this.riddenByEntity != null) {
            double offsetBackward = -1.8D;
            double pitchRad = Math.toRadians(this.rotationPitch);
            double horizontalOffset = offsetBackward * Math.cos(pitchRad);
            double offsetX = -Math.sin(Math.toRadians(this.rotationYaw)) * horizontalOffset;
            double offsetZ = Math.cos(Math.toRadians(this.rotationYaw)) * horizontalOffset;
            double offsetY = -offsetBackward * Math.sin(pitchRad);
            this.riddenByEntity.setPosition(
                    this.posX + offsetX,
                    this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset() + offsetY,
                    this.posZ + offsetZ
            );
        }
    }

    @Override
    public double getMountedYOffset() {
        return 0.3D;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.reloadTimer = nbt.getInteger("ReloadTimer");
        this.isReloading = nbt.getBoolean("IsReloading");
        this.isLoaded = nbt.getBoolean("IsLoaded");
        if (nbt.hasKey("AnchorX")) {
            this.anchorX = nbt.getDouble("AnchorX");
            this.anchorY = nbt.getDouble("AnchorY");
            this.anchorZ = nbt.getDouble("AnchorZ");
            this.anchorYaw = nbt.getFloat("AnchorYaw");
            this.anchorSet = true;
        }
        if (nbt.hasKey("RotationPitch")) {
            this.rotationPitch = nbt.getFloat("RotationPitch");
            this.dataWatcher.updateObject(26, Float.valueOf(this.rotationPitch));
            this.dataWatcher.updateObject(27, Float.valueOf(this.rotationPitch));
        }
        if (nbt.hasKey("RangePower")) {
            float r = MathHelper.clamp_float(nbt.getFloat("RangePower"), RANGE_MIN, RANGE_MAX);
            this.dataWatcher.updateObject(28, Float.valueOf(r));
        }
        this.dataWatcher.updateObject(21, Byte.valueOf(this.isLoaded ? (byte)1 : (byte)0));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("ReloadTimer", this.reloadTimer);
        nbt.setBoolean("IsReloading", this.isReloading);
        nbt.setBoolean("IsLoaded", this.isLoaded);
        nbt.setDouble("AnchorX", this.anchorX);
        nbt.setDouble("AnchorY", this.anchorY);
        nbt.setDouble("AnchorZ", this.anchorZ);
        nbt.setFloat("AnchorYaw", this.anchorYaw);
        nbt.setFloat("RotationPitch", this.rotationPitch);
        nbt.setFloat("RangePower", this.dataWatcher.getWatchableObjectFloat(28));
    }

    public String getEntityName() {
        return "Balista";
    }

    private boolean hasAmmo(EntityPlayer player) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() == RegItem.balistaBolt) {
                return true;
            }
        }
        return false;
    }

    private boolean consumeAmmo(EntityPlayer player) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() == RegItem.balistaBolt) {
                stack.stackSize--;
                if (stack.stackSize <= 0) {
                    player.inventory.setInventorySlotContents(i, null);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack getHeldItem() {
        return null;
    }

    @Override
    public ItemStack getEquipmentInSlot(int slot) {
        return null;
    }

    @Override
    public void setCurrentItemOrArmor(int slot, ItemStack stack) {

    }

    @Override
    public ItemStack[] getLastActiveItems() {
        return new ItemStack[5];
    }
}
