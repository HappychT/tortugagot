package noname.weapons.entity;

import noname.weapons.config.WeaponsConfig;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class EntityBatteringRam extends Entity {

    private static final int MAX_PASSENGERS = 6;
    private static final GameProfile FAKE_PROFILE = new GameProfile(UUID.fromString("f2346e2b-9b7c-4b24-9d6d-f57fbe7c3a9c"), "siege_ram");
    private static final SeatDefinition[] SEAT_DEFINITIONS = new SeatDefinition[] {
            new SeatDefinition(0.0F, 0.6F, 1.2F),
            new SeatDefinition(-0.6F, 0.6F, -0.4F),
            new SeatDefinition(0.6F, 0.6F, -0.4F),
            new SeatDefinition(-0.6F, 0.6F, 0.4F),
            new SeatDefinition(0.6F, 0.6F, 0.4F),
            new SeatDefinition(0.0F, 0.6F, 1.0F)
    };

    private boolean animationTriggered = false;
    private long lastSwingTime = 0;
    private float currentSpeed = 0.0F;
    private float targetSpeed = 0.0F;
    private final EntityRamSeat[] seats = new EntityRamSeat[MAX_PASSENGERS];
    private final List<UUID> passengerOrder = new ArrayList<>();
    private final Map<UUID, Integer> seatIndexByPassenger = new HashMap<>();

    private FakePlayer fakePlayer;
    private final ItemStack ironPickaxe = new ItemStack(Items.iron_pickaxe);
    private final ItemStack ironAxe = new ItemStack(Items.iron_axe);
    private final ItemStack ironShovel = new ItemStack(Items.iron_shovel);

    private final Map<BlockTarget, Float> breakingBlocks = new HashMap<>();
    private boolean breakingActive;
    private boolean wasSwinging;

    public EntityBatteringRam(World world) {
        super(world);
        this.preventEntitySpawning = true;
        this.setSize(2.5F, 2.3F);
        this.stepHeight = 1.0F;
        this.yOffset = this.height * 0.0F;
    }

    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(17, Float.valueOf(0.0F));
        this.dataWatcher.addObject(18, Float.valueOf(0.0F));
        this.dataWatcher.addObject(19, Float.valueOf(0.0F));
        this.dataWatcher.addObject(20, Float.valueOf(0.0F));
        this.dataWatcher.addObject(21, Float.valueOf(0.0F));
        this.dataWatcher.addObject(22, Float.valueOf(0.0F));
        this.dataWatcher.addObject(23, Float.valueOf(0.0F));
        this.dataWatcher.addObject(24, Float.valueOf(0.0F));
        this.dataWatcher.addObject(25, Integer.valueOf(0));
    }



    @Override
    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!this.worldObj.isRemote) {
            
            EntityPlayer driver = getDriver();
            if (driver != null) {
                updateRotation(driver);
                applySlowness(driver);
                updateBreakingState(driver);
                handleBlockBreaking(driver);
            } else {
                this.prevRotationYaw = this.rotationYaw;
                this.dataWatcher.updateObject(17, Float.valueOf(this.rotationYaw));
                this.dataWatcher.updateObject(18, Float.valueOf(this.prevRotationYaw));
                clearBreakingState();
            }

            this.motionY -= 0.08D;
            this.moveEntityWithHeading(0.0F, 0.0F);

            if (this.onGround) {
                this.motionY = 0.0D;
            }

            this.motionY *= 0.98D;

            
            this.dataWatcher.updateObject(22, Float.valueOf((float)this.posX));
            this.dataWatcher.updateObject(23, Float.valueOf((float)this.posY));
            this.dataWatcher.updateObject(24, Float.valueOf((float)this.posZ));

        } else {
            
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;

            this.prevRotationYaw = this.dataWatcher.getWatchableObjectFloat(18);
            this.rotationYaw = this.dataWatcher.getWatchableObjectFloat(17);
            this.currentSpeed = this.dataWatcher.getWatchableObjectFloat(19);

            
            float targetX = this.dataWatcher.getWatchableObjectFloat(22);
            float targetY = this.dataWatcher.getWatchableObjectFloat(23);
            float targetZ = this.dataWatcher.getWatchableObjectFloat(24);

            
            double lerpFactor = 0.25D;
            this.posX += (targetX - this.posX) * lerpFactor;
            this.posY += (targetY - this.posY) * lerpFactor;
            this.posZ += (targetZ - this.posZ) * lerpFactor;

            
            this.motionX = this.posX - this.prevPosX;
            this.motionY = this.posY - this.prevPosY;
            this.motionZ = this.posZ - this.prevPosZ;
        }

        updateSeatPositions();
    }



    private void updateSeatPositions() {
        for (EntityRamSeat seat : seats) {
            if (seat != null && seat.isDead && !this.worldObj.isRemote) {
                removeSeatInstance(seat);
            }
        }
    }

    private void updateRotation(EntityPlayer driver) {
        float targetYaw = driver.rotationYaw;
        float difference = targetYaw - this.rotationYaw;

        while (difference > 180.0F) difference -= 360.0F;
        while (difference < -180.0F) difference += 360.0F;

        float maxRotation = WeaponsConfig.rotationSpeedBatteringRam;

        if (difference > maxRotation) {
            difference = maxRotation;
        } else if (difference < -maxRotation) {
            difference = -maxRotation;
        }

        this.prevRotationYaw = this.rotationYaw;
        this.rotationYaw += difference;
        this.dataWatcher.updateObject(17, Float.valueOf(this.rotationYaw));
        this.dataWatcher.updateObject(18, Float.valueOf(this.prevRotationYaw));
    }

    public void moveEntityWithHeading(float strafe, float forward) {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer) {
            EntityPlayer driver = (EntityPlayer) this.riddenByEntity;

            float moveForward = driver.moveForward;
            float moveStrafe = driver.moveStrafing * 0.5F;

            
            if (moveForward > 0.0F) {
                targetSpeed = WeaponsConfig.movementSpeedBatteringRam;
            } else if (moveForward < 0.0F) {
                targetSpeed = -WeaponsConfig.movementSpeedBatteringRam * 0.5F;
            } else {
                targetSpeed = 0.0F;
            }

            
            float acceleration = 0.02F;
            if (Math.abs(targetSpeed - currentSpeed) < acceleration) {
                currentSpeed = targetSpeed;
            } else if (currentSpeed < targetSpeed) {
                currentSpeed += acceleration;
            } else if (currentSpeed > targetSpeed) {
                currentSpeed -= acceleration;
            }

            
            if (currentSpeed != 0.0F || moveStrafe != 0.0F) {
                double yawRad = Math.toRadians(this.rotationYaw);
                double cos = Math.cos(yawRad);
                double sin = Math.sin(yawRad);

                this.motionX = -sin * currentSpeed;
                this.motionZ = cos * currentSpeed;

                if (moveStrafe != 0.0F) {
                    this.motionX += cos * moveStrafe * 0.3;
                    this.motionZ += sin * moveStrafe * 0.3;
                }

                
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
            } else {
                this.motionX *= 0.8D;
                this.motionZ *= 0.8D;
            }

            
            if (!this.worldObj.isRemote) {
                this.dataWatcher.updateObject(19, Float.valueOf(currentSpeed));
                this.dataWatcher.updateObject(20, Float.valueOf((float)this.motionX));
                this.dataWatcher.updateObject(21, Float.valueOf((float)this.motionZ));
            }

        } else {
            targetSpeed = 0.0F;
            currentSpeed *= 0.9F;

            if (Math.abs(currentSpeed) < 0.001F) {
                currentSpeed = 0.0F;
            }

            this.motionX *= 0.9D;
            this.motionZ *= 0.9D;

            
            if (!this.worldObj.isRemote) {
                this.dataWatcher.updateObject(19, Float.valueOf(currentSpeed));
                this.dataWatcher.updateObject(20, Float.valueOf((float)this.motionX));
                this.dataWatcher.updateObject(21, Float.valueOf((float)this.motionZ));
            }

            
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
        }
    }

    public boolean isBreakingActive() {
        return breakingActive;
    }

    @Override
    public double getMountedYOffset() {
        return 1.2D;
    }

    @Override
    public void updateRiderPosition() {
        if (this.riddenByEntity != null) {
            double riderY = this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset();
            this.riddenByEntity.setPosition(this.posX, riderY, this.posZ);
        }
    }

    private void applySlowness(EntityPlayer driver) {
        int occupants = passengerOrder.size() + 1; 
        if (occupants <= 0) {
            return;
        }

        int amplifier = Math.max(0, 6 - occupants);
        driver.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 20, amplifier, true));
    }

    private void handleBlockBreaking(EntityPlayer driver) {
        if (!breakingActive) {
            if (!breakingBlocks.isEmpty()) {
                resetBlockBreaking();
            }
            return;
        }

        List<BlockTarget> targets = getFrontBlockTargets();
        if (targets == null || targets.isEmpty()) {
            if (!breakingBlocks.isEmpty()) {
                resetBlockBreaking();
            }
            breakingActive = false;
            return;
        }

        if (!(this.worldObj instanceof WorldServer)) {
            return;
        }

        FakePlayer fake = getFakePlayer();
        if (fake == null) {
            resetBlockBreaking();
            breakingActive = false;
            return;
        }

        Set<BlockTarget> targetSet = new HashSet<BlockTarget>(targets);

        Iterator<Map.Entry<BlockTarget, Float>> maintenanceIterator = breakingBlocks.entrySet().iterator();
        while (maintenanceIterator.hasNext()) {
            Map.Entry<BlockTarget, Float> entry = maintenanceIterator.next();
            BlockTarget breakingTarget = entry.getKey();
            if (!targetSet.contains(breakingTarget)) {
                worldObj.destroyBlockInWorldPartially(this.getEntityId(), breakingTarget.x, breakingTarget.y, breakingTarget.z, -1);
                maintenanceIterator.remove();
            }
        }

        for (BlockTarget target : targets) {
            if (!breakingBlocks.containsKey(target)) {
                breakingBlocks.put(target, 0.0F);
            }
        }

        Iterator<Map.Entry<BlockTarget, Float>> progressIterator = breakingBlocks.entrySet().iterator();
        while (progressIterator.hasNext()) {
            Map.Entry<BlockTarget, Float> entry = progressIterator.next();
            BlockTarget target = entry.getKey();
            Block block = worldObj.getBlock(target.x, target.y, target.z);
            if (block == null || block.isAir(worldObj, target.x, target.y, target.z)) {
                worldObj.destroyBlockInWorldPartially(this.getEntityId(), target.x, target.y, target.z, -1);
                progressIterator.remove();
                continue;
            }

            ItemStack toolStack = getToolForBlock(block, worldObj.getBlockMetadata(target.x, target.y, target.z));
            fake.inventory.mainInventory[fake.inventory.currentItem] = toolStack;
            float strength = ForgeHooks.blockStrength(block, fake, worldObj, target.x, target.y, target.z);

            if (strength <= 0.0F) {
                worldObj.destroyBlockInWorldPartially(this.getEntityId(), target.x, target.y, target.z, -1);
                progressIterator.remove();
                continue;
            }

            float currentProgress = entry.getValue() + strength;
            if (currentProgress > 1.0F) {
                currentProgress = 1.0F;
            }
            entry.setValue(currentProgress);

            int partial = (int)(currentProgress * 10.0F);
            if (partial > 10) {
                partial = 10;
            }
            worldObj.destroyBlockInWorldPartially(this.getEntityId(), target.x, target.y, target.z, partial);

            if (currentProgress >= 1.0F) {
                worldObj.func_147480_a(target.x, target.y, target.z, true);
                progressIterator.remove();
            }
        }

        if (breakingBlocks.isEmpty()) {
            breakingActive = false;
        }
    }

    private void resetBlockBreaking() {
        for (BlockTarget target : breakingBlocks.keySet()) {
            worldObj.destroyBlockInWorldPartially(this.getEntityId(), target.x, target.y, target.z, -1);
        }
        breakingBlocks.clear();
    }

    private void clearBreakingState() {
        if (!breakingBlocks.isEmpty()) {
            resetBlockBreaking();
        }
        breakingActive = false;
        wasSwinging = false;
        if (!this.worldObj.isRemote) {
            this.dataWatcher.updateObject(25, Integer.valueOf(0));
        }
    }


    private void updateBreakingState(EntityPlayer driver) {
        boolean swinging = driver.isSwingInProgress || driver.swingProgressInt > 0 || driver.swingProgress > 0.0F;
        if (swinging && !wasSwinging) {
            breakingActive = true;
            if (!this.worldObj.isRemote) {
                long currentTime = this.worldObj.getTotalWorldTime();
                this.dataWatcher.updateObject(25, Integer.valueOf((int)(currentTime % 999999) + 1));
            }
        }
        if (!swinging && wasSwinging) {
            if (!this.worldObj.isRemote) {
                this.dataWatcher.updateObject(25, Integer.valueOf(0));
            }
        }

        wasSwinging = swinging;
    }


    private List<BlockTarget> getFrontBlockTargets() {
        List<BlockTarget> targets = new ArrayList<BlockTarget>();
        Set<BlockTarget> uniqueTargets = new HashSet<BlockTarget>();
        double yawRad = Math.toRadians(this.rotationYaw);
        double cos = Math.cos(yawRad);
        double sin = Math.sin(yawRad);
        double reach = 3.5D;
        AxisAlignedBB boundingBox = this.boundingBox;
        double minY = boundingBox != null ? boundingBox.minY : this.posY;
        int baseY = MathHelper.floor_double(minY);

        for (int widthOffset = -1; widthOffset <= 1; widthOffset++) {
            double worldX = this.posX + widthOffset * cos - reach * sin;
            double worldZ = this.posZ + widthOffset * sin + reach * cos;
            int blockX = MathHelper.floor_double(worldX);
            int blockZ = MathHelper.floor_double(worldZ);

            for (int heightOffset = 0; heightOffset < 3; heightOffset++) {
                int blockY = baseY + heightOffset;
                Block block = worldObj.getBlock(blockX, blockY, blockZ);
                if (block == null || block.isAir(worldObj, blockX, blockY, blockZ)) {
                    continue;
                }
                float hardness = block.getBlockHardness(worldObj, blockX, blockY, blockZ);
                if (hardness < 0.0F) {
                    continue;
                }
                BlockTarget target = new BlockTarget(blockX, blockY, blockZ);
                if (uniqueTargets.add(target)) {
                    targets.add(target);
                }
            }
        }

        return targets.isEmpty() ? null : targets;
    }

    private ItemStack getToolForBlock(Block block, int meta) {
        Material material = block.getMaterial();
        if (material == Material.wood || material == Material.gourd || material == Material.plants || material == Material.vine) {
            return ironAxe;
        }
        if (material == Material.ground || material == Material.grass || material == Material.clay || material == Material.sand || material == Material.snow || material == Material.craftedSnow) {
            return ironShovel;
        }
        return ironPickaxe;
    }

    private FakePlayer getFakePlayer() {
        if (this.fakePlayer == null && !this.worldObj.isRemote && this.worldObj instanceof WorldServer) {
            FakePlayer player = FakePlayerFactory.get((WorldServer) this.worldObj, FAKE_PROFILE);
            player.inventory.currentItem = 0;
            player.inventory.setInventorySlotContents(0, ironPickaxe);
            this.fakePlayer = player;
        }
        return this.fakePlayer;
    }

    private EntityPlayer getDriver() {
        if (this.riddenByEntity instanceof EntityPlayer) {
            return (EntityPlayer) this.riddenByEntity;
        }
        return null;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @Override
    public boolean interactFirst(EntityPlayer player) {
        if (player == null || player.isSneaking()) {
            return false;
        }

        if (!this.worldObj.isRemote) {
            if (this.riddenByEntity == null || this.riddenByEntity.isDead) {
                player.mountEntity(this);
                return true;
            }

            if (this.riddenByEntity == player) {
                return true;
            }

            UUID id = player.getUniqueID();
            if (seatIndexByPassenger.containsKey(id)) {
                Integer seatIndex = seatIndexByPassenger.get(id);
                if (seatIndex != null) {
                    EntityRamSeat seat = getOrCreateSeat(seatIndex);
                    if (seat != null && seat.riddenByEntity != player) {
                        player.mountEntity(seat);
                    }
                }
                return true;
            }

            int seatIndex = getFirstAvailableSeat();
            if (seatIndex == -1) {
                return true;
            }

            EntityRamSeat seat = getOrCreateSeat(seatIndex);
            player.mountEntity(seat);
        }
        return true;
    }

    private int getFirstAvailableSeat() {
        for (int i = 0; i < MAX_PASSENGERS; i++) {
            EntityRamSeat seat = getOrCreateSeat(i);
            if (seat.riddenByEntity == null) {
                return i;
            }
        }
        return -1;
    }

    private EntityRamSeat getOrCreateSeat(int index) {
        EntityRamSeat seat = seats[index];
        if (seat == null || seat.isDead) {
            SeatDefinition definition = SEAT_DEFINITIONS[index];
            seat = new EntityRamSeat(this, index, definition.offsetX, definition.offsetY, definition.offsetZ);
            seat.setSeatIndex(index);
            seat.setParent(this);
            if (!this.worldObj.isRemote) {
                this.worldObj.spawnEntityInWorld(seat);
            }
            seats[index] = seat;
        }
        return seat;
    }

    private void removeSeatInstance(EntityRamSeat seat) {
        for (int i = 0; i < seats.length; i++) {
            if (seats[i] == seat) {
                seats[i] = null;
            }
        }
    }

    public void onSeatOccupantChanged(int seatIndex, Entity oldPassenger, Entity newPassenger) {
        if (this.worldObj.isRemote || seatIndex < 0 || seatIndex >= MAX_PASSENGERS) {
            return;
        }

        if (oldPassenger instanceof EntityLivingBase) {
            UUID id = oldPassenger.getUniqueID();
            passengerOrder.remove(id);
            seatIndexByPassenger.remove(id);
        }

        if (newPassenger instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) newPassenger;
            UUID id = player.getUniqueID();
            passengerOrder.remove(id);
            passengerOrder.add(id);
            seatIndexByPassenger.put(id, seatIndex);
        }
    }

    @Override
    public void setDead() {
        super.setDead();
        clearBreakingState();
        for (EntityRamSeat seat : seats) {
            if (seat != null) {
                seat.setDead();
            }
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        this.rotationYaw = nbt.getFloat("Rotation");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setFloat("Rotation", this.rotationYaw);
    }

    private static class SeatDefinition {
        final float offsetX;
        final float offsetY;
        final float offsetZ;

        private SeatDefinition(float offsetX, float offsetY, float offsetZ) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
        }
    }

    private static class BlockTarget {
        final int x;
        final int y;
        final int z;

        private BlockTarget(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            BlockTarget that = (BlockTarget) obj;
            return x == that.x && y == that.y && z == that.z;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + z;
            return result;
        }
    }
}


