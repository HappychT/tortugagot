package noname.weapons.entity;

import noname.weapons.RegItem;
import noname.weapons.config.WeaponsConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityTribushet extends Entity {

    private int reloadTimer = 0;
    private boolean isReloading = false;
    private boolean isLoaded = false;
    private EntityPlayer rider;
    private boolean lastSwingState = false;
    private float health;

    public EntityTribushet(World world) {
        super(world);
        this.setSize(2.0F, 1.5F);
        this.preventEntitySpawning = true;
        this.health = WeaponsConfig.maxHealthTribushet;
    }

    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(17, Byte.valueOf((byte)0)); 
        this.dataWatcher.addObject(18, Integer.valueOf(0)); 
        this.dataWatcher.addObject(19, Float.valueOf(0.0F)); 
        this.dataWatcher.addObject(20, Float.valueOf(0.0F)); 
        this.dataWatcher.addObject(21, Byte.valueOf((byte)0)); 
        this.dataWatcher.addObject(22, Float.valueOf(WeaponsConfig.maxHealthTribushet));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!this.worldObj.isRemote) {
            if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) this.riddenByEntity;
                this.rider = player;

                float targetYaw = player.rotationYaw;
                float difference = targetYaw - this.rotationYaw;

                while (difference > 180.0F) difference -= 360.0F;
                while (difference < -180.0F) difference += 360.0F;

                float maxRotation = WeaponsConfig.rotationSpeedTribushet;

                if (difference > maxRotation) {
                    difference = maxRotation;
                } else if (difference < -maxRotation) {
                    difference = -maxRotation;
                }

                this.prevRotationYaw = this.rotationYaw;
                this.rotationYaw += difference;
                this.dataWatcher.updateObject(19, Float.valueOf(this.rotationYaw));
                this.dataWatcher.updateObject(20, Float.valueOf(this.prevRotationYaw));

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

                        if (reloadTimer >= WeaponsConfig.reloadTimeTribushet) {
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
            this.health = this.dataWatcher.getWatchableObjectFloat(22);
        }
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
            EntityPlayer player = (EntityPlayer) this.riddenByEntity;

            if (!isReadyToFire()) {
                return;
            }

            float yaw = this.rotationYaw;
            float pitch = player.rotationPitch;

            double motionX = -MathHelper.sin(yaw / 180.0F * (float)Math.PI) *
                    MathHelper.cos(pitch / 180.0F * (float)Math.PI);
            double motionY = -MathHelper.sin(pitch / 180.0F * (float)Math.PI);
            double motionZ = MathHelper.cos(yaw / 180.0F * (float)Math.PI) *
                    MathHelper.cos(pitch / 180.0F * (float)Math.PI);

			double speed = WeaponsConfig.tribushetProjectileSpeed;
            motionX *= speed;
            motionY *= speed;
            motionZ *= speed;

            EntityStoneProjectile projectile = new EntityStoneProjectile(
                    this.worldObj,
                    player,
                    motionX,
                    motionY,
                    motionZ
            );

            double shootX = this.posX + motionX * 2.0D;
            double shootY = this.posY + 1.0D + motionY * 2.0D;
            double shootZ = this.posZ + motionZ * 2.0D;

            projectile.setPosition(shootX, shootY, shootZ);
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

        if (damageSource.getSourceOfDamage() instanceof EntityStoneProjectile) {
            this.health -= damage;
        } else {
            float reducedDamage = damage * (1.0F - Math.min(WeaponsConfig.armor / 25.0F, 0.8F));
            this.health -= reducedDamage;
        }
        this.dataWatcher.updateObject(22, Float.valueOf(this.health));
        if (this.health <= 0) {
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
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        this.reloadTimer = nbt.getInteger("ReloadTimer");
        this.isReloading = nbt.getBoolean("IsReloading");
        this.isLoaded = nbt.getBoolean("IsLoaded");
        this.health = nbt.getFloat("Health");

        if (this.health <= 0 || this.health > WeaponsConfig.maxHealthTribushet) {
            this.health = WeaponsConfig.maxHealthTribushet;
        }

        this.dataWatcher.updateObject(21, Byte.valueOf(this.isLoaded ? (byte)1 : (byte)0));
        this.dataWatcher.updateObject(22, Float.valueOf(this.health));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setInteger("ReloadTimer", this.reloadTimer);
        nbt.setBoolean("IsReloading", this.isReloading);
        nbt.setBoolean("IsLoaded", this.isLoaded);
        nbt.setFloat("Health", this.health);
    }

    public String getEntityName() {
        return "Tribushet";
    }
}
