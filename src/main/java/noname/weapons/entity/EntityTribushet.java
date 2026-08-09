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
import net.minecraft.world.World;

public class EntityTribushet extends EntityLivingBase {
    private double anchorX;
    private double anchorY;
    private double anchorZ;
    private float anchorYaw;
    private boolean anchorSet = false;

    private int reloadTimer = 0;
    private boolean isReloading = false;
    private boolean isLoaded = false;
    private EntityPlayer rider;
    private boolean lastSwingState = false;

    public static final int FIRE_ANIM_DURATION = 30;
    private int fireAnimTimer = 0;

    public EntityTribushet(World world) {
        super(world);
        this.setSize(2.4F, 1.5F);
        this.preventEntitySpawning = true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(WeaponsConfig.maxHealthTribushet);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(17, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(18, Integer.valueOf(0));
        this.dataWatcher.addObject(19, Float.valueOf(0.0F));
        this.dataWatcher.addObject(20, Float.valueOf(0.0F));
        this.dataWatcher.addObject(21, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(28, Float.valueOf(0.6F));
        this.dataWatcher.addObject(22, Integer.valueOf(0));
    }

    private static final float RANGE_MIN = 0.2F;
    private static final float RANGE_MAX = 1.0F;
    private static final float RANGE_STEP = 0.015F;

    @Override
    public void onUpdate() {
        stabilizeIfUnsupported();
        super.onUpdate();
        updateAnchor();
        lockToAnchor();
        stabilizeIfUnsupported();

        if (!this.worldObj.isRemote) {
            if (fireAnimTimer > 0) {
                fireAnimTimer--;
                this.dataWatcher.updateObject(22, Integer.valueOf(fireAnimTimer));
            }

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

                updateControlledYaw(player.rotationYaw, WeaponsConfig.rotationSpeedTribushet);
                this.prevRotationYaw = this.rotationYaw;
                this.rotationYaw = this.anchorYaw;
                this.dataWatcher.updateObject(19, Float.valueOf(this.rotationYaw));
                this.dataWatcher.updateObject(20, Float.valueOf(this.prevRotationYaw));

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

                        if (reloadTimer >= TOGEngineeringTechnology.getReloadTime(WeaponsConfig.reloadTimeTribushet, player)) {
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
                this.rider = null;
                lastSwingState = false;

                if (isReloading) {
                    stopReloading();
                }
            }
        } else {

            this.prevRotationYaw = this.dataWatcher.getWatchableObjectFloat(20);
            this.rotationYaw = this.dataWatcher.getWatchableObjectFloat(19);
            isReloading = (this.dataWatcher.getWatchableObjectByte(17) & 1) != 0;
            reloadTimer = this.dataWatcher.getWatchableObjectInt(18);
            fireAnimTimer = this.dataWatcher.getWatchableObjectInt(22);
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

    private boolean hasAmmo(EntityPlayer player) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() == RegItem.stoneProjectile) {
                return true;
            }
        }
        return false;
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

    private boolean consumeAmmo(EntityPlayer player) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() == RegItem.stoneProjectile) {
                stack.stackSize--;
                if (stack.stackSize <= 0) {
                    player.inventory.setInventorySlotContents(i, null);
                }
                return true;
            }
        }
        return false;
    }

    public boolean isReadyToFire() {
        return isLoaded && !isReloading;
    }

    public void fire() {
        if (!this.worldObj.isRemote && this.riddenByEntity != null) {
            if (!SiegeActivationManager.getInstance().isSiegeActive(this.worldObj.provider.dimensionId, this.posX, this.posY, this.posZ)) return;
            EntityPlayer player = (EntityPlayer) this.riddenByEntity;

            if (!TOGEngineeringTechnology.canUseSiegeWeapon(player, this)) {
                TOGEngineeringTechnology.notifySiegeWeaponBlocked(player, this);
                return;
            }

            if (!isReadyToFire()) {
                return;
            }

            float yaw = this.rotationYaw;
            float yawRad = yaw / 180.0F * (float) Math.PI;

            float launchAngle = -55.0F;
            float pitchRad = launchAngle / 180.0F * (float) Math.PI;
            double dirX = -MathHelper.sin(yawRad) * MathHelper.cos(pitchRad);
            double dirY = -MathHelper.sin(pitchRad);
            double dirZ = MathHelper.cos(yawRad) * MathHelper.cos(pitchRad);

            float rangePower = this.dataWatcher.getWatchableObjectFloat(28);
            double speed = WeaponsConfig.tribushetProjectileSpeed * (double) rangePower;

            EntityStoneProjectile projectile = new EntityStoneProjectile(
                    this.worldObj,
                    player,
                    dirX * speed,
                    dirY * speed,
                    dirZ * speed
            );

            double backOffset = 3.0D;
            double shootX = this.posX + MathHelper.sin(yawRad) * backOffset;
            double shootY = this.posY + 5.0D;
            double shootZ = this.posZ - MathHelper.cos(yawRad) * backOffset;

            projectile.setPosition(shootX, shootY, shootZ);
            this.worldObj.spawnEntityInWorld(projectile);

            isLoaded = false;
            this.dataWatcher.updateObject(21, Byte.valueOf((byte)0));

            fireAnimTimer = FIRE_ANIM_DURATION;
            this.dataWatcher.updateObject(22, Integer.valueOf(fireAnimTimer));
        }
    }

    public int getFireAnimTimer() {
        return fireAnimTimer;
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
        if (damageSource.getSourceOfDamage() instanceof EntityStoneProjectile) {
            reducedDamage = damage;
        } else {
            reducedDamage = damage * (1.0F - Math.min(WeaponsConfig.armor / 25.0F, 0.8F));
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
            double offsetBackward = -9.0D;
            double offsetSide = 1.8D;
            double offsetX = -Math.sin(Math.toRadians(this.rotationYaw)) * offsetBackward
                    + Math.cos(Math.toRadians(this.rotationYaw)) * offsetSide;
            double offsetZ = Math.cos(Math.toRadians(this.rotationYaw)) * offsetBackward
                    + Math.sin(Math.toRadians(this.rotationYaw)) * offsetSide;
            this.riddenByEntity.setPosition(
                    this.posX + offsetX,
                    this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(),
                    this.posZ + offsetZ
            );
        }
    }


    @Override
    public double getMountedYOffset() {
        return 0.5D;
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
        nbt.setFloat("RangePower", this.dataWatcher.getWatchableObjectFloat(28));
    }

    public String getEntityName() {
        return "Tribushet";
    }

    @Override
    public ItemStack getHeldItem() {
        return null;
    }

    @Override
    public ItemStack getEquipmentInSlot(int p_71124_1_) {
        return null;
    }

    @Override
    public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_) {
    }

    @Override
    public ItemStack[] getLastActiveItems() {
        return new ItemStack[5];
    }
}
