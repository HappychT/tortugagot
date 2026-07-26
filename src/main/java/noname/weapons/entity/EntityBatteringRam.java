package noname.weapons.entity;

import brain.factions.servers.SiegeActivationManager;
import net.minecraft.util.ChatComponentText;
import noname.weapons.config.WeaponsConfig;
import noname.weapons.RegItem;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
    private static final long REMOUNT_COOLDOWN_TICKS = 1200;

    private static final SeatDefinition[] SEAT_DEFINITIONS = new SeatDefinition[]{
            new SeatDefinition(0.0F, 0.6F, 1.2F),
            new SeatDefinition(-0.6F, 0.6F, -0.4F),
            new SeatDefinition(0.6F, 0.6F, -0.4F),
            new SeatDefinition(-0.6F, 0.6F, 0.4F),
            new SeatDefinition(0.6F, 0.6F, 0.4F),
            new SeatDefinition(0.0F, 0.6F, 1.0F)
    };

    private static final Map<UUID, Long> dismountCooldowns = new HashMap<>();

    private float currentSpeed = 0.0F;
    private float targetSpeed = 0.0F;
    private int ramPosRotationIncrements;
    private double ramX, ramY, ramZ, ramYaw, ramPitch;
    private double velocityX, velocityY, velocityZ;

    private final EntityRamSeat[] seats = new EntityRamSeat[MAX_PASSENGERS];
    private final List<UUID> passengerOrder = new ArrayList<>();
    private final Map<UUID, Integer> seatIndexByPassenger = new HashMap<>();

    private FakePlayer fakePlayer;
    private final ItemStack woodenPickaxe = new ItemStack(Items.wooden_pickaxe);
    private final ItemStack woodenAxe = new ItemStack(Items.wooden_axe);
    private final ItemStack woodenShovel = new ItemStack(Items.wooden_shovel);

    private final Map<BlockTarget, Float> breakingBlocks = new HashMap<>();
    private boolean breakingActive;
    private boolean wasSwinging;
    private int lastSwingTick;
    private Entity previousDriver;

    public EntityBatteringRam(World world) {
        super(world);
        this.preventEntitySpawning = true;
        this.setSize(2.5F, 2.3F);
        this.stepHeight = 1.0F;
        this.isImmuneToFire = true;
    }

    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(17, Float.valueOf(0.0F));
        this.dataWatcher.addObject(18, Float.valueOf(0.0F));
        this.dataWatcher.addObject(19, Float.valueOf(0.0F));
        this.dataWatcher.addObject(20, Float.valueOf(0.0F));
        this.dataWatcher.addObject(21, Float.valueOf(0.0F));
        this.dataWatcher.addObject(22, Float.valueOf(WeaponsConfig.maxHealthBatteringRam));
        this.dataWatcher.addObject(23, Integer.valueOf(0));
        this.dataWatcher.addObject(25, Integer.valueOf(0));
    }

    public float getHealth() {
        return this.dataWatcher.getWatchableObjectFloat(22);
    }

    public void setHealth(float health) {
        this.dataWatcher.updateObject(22, Float.valueOf(MathHelper.clamp_float(health, 0.0F, WeaponsConfig.maxHealthBatteringRam)));
    }

    public float getMaxHealth() {
        return WeaponsConfig.maxHealthBatteringRam;
    }

    public int getPassengerCount() {
        return this.dataWatcher.getWatchableObjectInt(23);
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return AxisAlignedBB.getBoundingBox(
                this.posX - 0.7D, this.posY, this.posZ - 0.7D,
                this.posX + 0.7D, this.posY + 1.8D, this.posZ + 0.7D
        );
    }

    public void setStatus(boolean status){
        this.breakingActive = status;
        if (!this.worldObj.isRemote) {
            int current = this.dataWatcher.getWatchableObjectInt(25);
            if (status) {
                if (current == 0) {
                    long currentTime = this.worldObj.getTotalWorldTime();
                    this.dataWatcher.updateObject(25, Integer.valueOf((int) (currentTime % 999999) + 1));
                }
            } else if (current != 0) {
                this.dataWatcher.updateObject(25, Integer.valueOf(0));
            }
        }
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entity) {
        return null;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isDead || this.worldObj.isRemote) return false;

        if (source.getSourceOfDamage() instanceof EntityArrow) return false;

        if (source == DamageSource.inFire || source == DamageSource.onFire || source.isFireDamage()) return false;

        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;

        if (source.getSourceOfDamage() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) source.getSourceOfDamage();
            if (getAllCrew().contains(player)) return false;
            if (player.capabilities.isCreativeMode) {
                destroyRam();
                return true;
            }
        }

        float armor = WeaponsConfig.armorBatteringRam;
        float reducedDamage;
        if (source.getSourceOfDamage() instanceof EntityStoneProjectile || source.getSourceOfDamage() instanceof EntityBalistaProjectile) {
            reducedDamage = amount;
        } else {
            reducedDamage = amount * (1.0F - Math.min(armor / 25.0F, 0.8F));
        }

        List<EntityPlayer> crew = getAllCrew();
        if (!crew.isEmpty()) {
            float damagePerPlayer = reducedDamage / crew.size();
            for (EntityPlayer crewMember : crew) {
                crewMember.attackEntityFrom(DamageSource.generic, damagePerPlayer);
            }
        }

        float health = getHealth() - reducedDamage;
        setHealth(health);

        if (health <= 0.0F) {
            destroyRam();
        }

        return true;
    }

    private List<EntityPlayer> getAllCrew() {
        List<EntityPlayer> crew = new ArrayList<>();
        if (this.riddenByEntity instanceof EntityPlayer) {
            crew.add((EntityPlayer) this.riddenByEntity);
        }
        for (EntityRamSeat seat : seats) {
            if (seat != null && !seat.isDead && seat.riddenByEntity instanceof EntityPlayer) {
                crew.add((EntityPlayer) seat.riddenByEntity);
            }
        }
        return crew;
    }

    @Override
    public void onUpdate() {
        stabilizeIfUnsupported();
        super.onUpdate();

        if (!this.worldObj.isRemote) {
            trackDriverDismount();
            promoteDriverIfNeeded();

            int count = getAllCrew().size();
            this.dataWatcher.updateObject(23, Integer.valueOf(count));

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
            applyGravityIfNeeded();
            this.moveEntityWithHeading(0.0F, 0.0F);

            if (this.ticksExisted % 6000 == 0) {
                long now = this.worldObj.getTotalWorldTime();
                Iterator<Map.Entry<UUID, Long>> it = dismountCooldowns.entrySet().iterator();
                while (it.hasNext()) {
                    if (now - it.next().getValue() >= REMOUNT_COOLDOWN_TICKS) it.remove();
                }
            }
        } else {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.currentSpeed = this.dataWatcher.getWatchableObjectFloat(19);

            if (ramPosRotationIncrements > 0) {
                double nextX = this.posX + (this.ramX - this.posX) / this.ramPosRotationIncrements;
                double nextY = this.posY + (this.ramY - this.posY) / this.ramPosRotationIncrements;
                double nextZ = this.posZ + (this.ramZ - this.posZ) / this.ramPosRotationIncrements;
                double yawDiff = MathHelper.wrapAngleTo180_double(this.ramYaw - this.rotationYaw);
                this.rotationYaw = (float) (this.rotationYaw + yawDiff / this.ramPosRotationIncrements);
                this.rotationPitch = (float) (this.rotationPitch + (this.ramPitch - this.rotationPitch) / this.ramPosRotationIncrements);
                --this.ramPosRotationIncrements;
                this.setPosition(nextX, nextY, nextZ);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            } else {
                this.prevRotationYaw = this.rotationYaw;
                this.rotationYaw = this.dataWatcher.getWatchableObjectFloat(17);
                double watchedMotionX = this.dataWatcher.getWatchableObjectFloat(20);
                double watchedMotionZ = this.dataWatcher.getWatchableObjectFloat(21);
                this.setPosition(this.posX + watchedMotionX, this.posY, this.posZ + watchedMotionZ);
                this.motionX = watchedMotionX;
                this.motionY = 0.0D;
                this.motionZ = watchedMotionZ;
            }

            this.motionX = this.posX - this.prevPosX;
            this.motionY = this.posY - this.prevPosY;
            this.motionZ = this.posZ - this.prevPosZ;
        }

        updateSeatPositions();
        stabilizeIfUnsupported();
    }

    private void trackDriverDismount() {
        if (this.isDead) return;
        Entity currentDriver = this.riddenByEntity;
        if (previousDriver instanceof EntityPlayer && currentDriver != previousDriver) {
            EntityPlayer player = (EntityPlayer) previousDriver;
            if (!player.isDead) {
                dismountCooldowns.put(player.getUniqueID(), this.worldObj.getTotalWorldTime());
            }
        }
        previousDriver = currentDriver;
    }

    private void promoteDriverIfNeeded() {
        if (this.riddenByEntity != null && !this.riddenByEntity.isDead) return;

        for (int i = 0; i < seats.length; i++) {
            EntityRamSeat seat = seats[i];
            if (seat != null && !seat.isDead && seat.riddenByEntity instanceof EntityPlayer) {
                EntityPlayer newDriver = (EntityPlayer) seat.riddenByEntity;
                UUID id = newDriver.getUniqueID();
                dismountCooldowns.remove(id);
                newDriver.mountEntity(null);
                newDriver.mountEntity(this);
                passengerOrder.remove(id);
                seatIndexByPassenger.remove(id);
                return;
            }
        }
    }

    private void updateSeatPositions() {
        for (int i = 0; i < seats.length; i++) {
            if (seats[i] != null && seats[i].isDead && !this.worldObj.isRemote) {
                seats[i] = null;
            }
        }
    }

    private void stabilizeIfUnsupported() {
        if (!isSupported()) {
            this.onGround = false;
        }
    }

    private boolean isSupported() {
        int blockX = MathHelper.floor_double(this.posX);
        int blockY = MathHelper.floor_double(this.posY - 0.1D);
        int blockZ = MathHelper.floor_double(this.posZ);
        Block block = this.worldObj.getBlock(blockX, blockY, blockZ);
        return block != null && !block.isAir(this.worldObj, blockX, blockY, blockZ);
    }

    private void applyGravityIfNeeded() {
        if (isSupported()) {
            if (this.motionY < 0.0D) {
                this.motionY = 0.0D;
            }
            this.fallDistance = 0.0F;
            return;
        }
        this.onGround = false;
        this.motionY -= 0.08D;
        this.motionY *= 0.98D;
    }

    private void updateRotation(EntityPlayer driver) {
        float targetYaw = driver.rotationYaw;
        float difference = MathHelper.wrapAngleTo180_float(targetYaw - this.rotationYaw);
        float maxRotation = WeaponsConfig.rotationSpeedBatteringRam;

        if (difference > maxRotation) difference = maxRotation;
        else if (difference < -maxRotation) difference = -maxRotation;

        this.prevRotationYaw = this.rotationYaw;
        this.rotationYaw += difference;
        this.dataWatcher.updateObject(17, Float.valueOf(this.rotationYaw));
        this.dataWatcher.updateObject(18, Float.valueOf(this.prevRotationYaw));
    }

    public void moveEntityWithHeading(float strafe, float forward) {
        if (this.riddenByEntity instanceof EntityPlayer) {
            EntityPlayer driver = (EntityPlayer) this.riddenByEntity;
            float moveForward = driver.moveForward;
            float crewFactor = getCrewSpeedFactor(getAllCrew().size());

            if (moveForward > 0.0F) {
                targetSpeed = WeaponsConfig.movementSpeedBatteringRam * crewFactor;
            } else if (moveForward < 0.0F) {
                targetSpeed = -WeaponsConfig.movementSpeedBatteringRam * 0.5F * crewFactor;
            } else {
                targetSpeed = 0.0F;
            }

            float acceleration = 0.02F;
            if (Math.abs(targetSpeed - currentSpeed) < acceleration) {
                currentSpeed = targetSpeed;
            } else if (currentSpeed < targetSpeed) {
                currentSpeed += acceleration;
            } else {
                currentSpeed -= acceleration;
            }

            double horizontalMotionX = 0.0D;
            double horizontalMotionZ = 0.0D;
            if (currentSpeed != 0.0F) {
                double yawRad = Math.toRadians(this.rotationYaw);
                horizontalMotionX = -Math.sin(yawRad) * currentSpeed;
                horizontalMotionZ = Math.cos(yawRad) * currentSpeed;
            }
            this.motionX = horizontalMotionX;
            this.motionZ = horizontalMotionZ;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
        } else {
            targetSpeed = 0.0F;
            currentSpeed *= 0.9F;
            if (Math.abs(currentSpeed) < 0.001F) currentSpeed = 0.0F;

            this.motionX *= 0.9D;
            this.motionZ *= 0.9D;
            if (Math.abs(this.motionX) < 0.001D) this.motionX = 0.0D;
            if (Math.abs(this.motionZ) < 0.001D) this.motionZ = 0.0D;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
        }

        if (!this.worldObj.isRemote) {
            this.dataWatcher.updateObject(19, Float.valueOf(currentSpeed));
            this.dataWatcher.updateObject(20, Float.valueOf((float) this.motionX));
            this.dataWatcher.updateObject(21, Float.valueOf((float) this.motionZ));
        }
    }

    private float getCrewSpeedFactor(int occupants) {
        if (occupants <= 0) {
            return 0.0F;
        }
        float factor = occupants / (float) MAX_PASSENGERS;
        return MathHelper.clamp_float(factor, 1.0F / MAX_PASSENGERS, 1.0F);
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
            double yawRad = Math.toRadians(this.rotationYaw);
            double leftX = Math.cos(yawRad) * 0.5D;
            double leftZ = Math.sin(yawRad) * 0.5D;
            double riderY = this.posY + this.getMountedYOffset() - 0.5D + this.riddenByEntity.getYOffset();
            this.riddenByEntity.setPosition(this.posX + leftX, riderY, this.posZ + leftZ);
        }
    }

    private void applySlowness(EntityPlayer driver) {
        int occupants = getAllCrew().size();
        if (occupants == 0) return;
        int amplifier = Math.max(0, 6 - occupants);
        driver.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 20, amplifier, true));
    }

    private boolean hasCooldown(EntityPlayer player) {
        UUID id = player.getUniqueID();
        Long dismountTime = dismountCooldowns.get(id);
        if (dismountTime == null) return false;
        long elapsed = this.worldObj.getTotalWorldTime() - dismountTime;
        if (elapsed >= REMOUNT_COOLDOWN_TICKS) {
            dismountCooldowns.remove(id);
            return false;
        }
        return true;
    }

    public long getRemainingCooldown(EntityPlayer player) {
        UUID id = player.getUniqueID();
        Long dismountTime = dismountCooldowns.get(id);
        if (dismountTime == null) return 0;
        long elapsed = this.worldObj.getTotalWorldTime() - dismountTime;
        if (elapsed >= REMOUNT_COOLDOWN_TICKS) return 0;
        return (REMOUNT_COOLDOWN_TICKS - elapsed) / 20;
    }

    private void handleBlockBreaking(EntityPlayer driver) {
        if (!breakingActive) {
            if (!breakingBlocks.isEmpty()) resetBlockBreaking();
            return;
        }

        List<BlockTarget> allTargets = getFrontBlockTargets();
        if (allTargets == null || allTargets.isEmpty()) {
            if (!breakingBlocks.isEmpty()) {
                resetBlockBreaking();
            }
            return;
        }

        List<BlockTarget> targets = new ArrayList<>(allTargets);

        if (!(this.worldObj instanceof WorldServer)) {
            return;
        }

        FakePlayer fake = getFakePlayer();
        if (fake == null) {
            resetBlockBreaking();
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

            if (EntityStoneProjectile.isProtectedSiegeBlock(worldObj, target.x, target.y, target.z)) {
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

            int partial = (int) (currentProgress * 10.0F);
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
            // Keep breakingActive controlled by input (setStatus),
            // so animation doesn't stop mid-hold.
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
            this.dataWatcher.updateObject(25, 0);
        }
    }

    private void updateBreakingState(EntityPlayer driver) {



        if (breakingActive) {
            if (!this.worldObj.isRemote) {
                int current = this.dataWatcher.getWatchableObjectInt(25);
                if (current == 0) {
                    long currentTime = this.worldObj.getTotalWorldTime();
                    this.dataWatcher.updateObject(25, Integer.valueOf((int) (currentTime % 999999) + 1));
                }
            }
        } else{
            if (!this.worldObj.isRemote && this.dataWatcher.getWatchableObjectInt(25) != 0) {
                this.dataWatcher.updateObject(25, Integer.valueOf(0));
            }
        }
    }

    private List<BlockTarget> getFrontBlockTargets() {
        List<BlockTarget> targets = new ArrayList<>();
        Set<BlockTarget> uniqueTargets = new HashSet<>();
        double yawRad = Math.toRadians(this.rotationYaw);
        double cos = Math.cos(yawRad);
        double sin = Math.sin(yawRad);
        AxisAlignedBB boundingBox = this.boundingBox;
        double minY = boundingBox != null ? boundingBox.minY : this.posY;
        int baseY = MathHelper.floor_double(minY);
        double reach = 1.6D;
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
                if (EntityStoneProjectile.isProtectedSiegeBlock(worldObj, blockX, blockY, blockZ)) {
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
            return woodenAxe;
        }
        if (material == Material.ground || material == Material.grass || material == Material.clay || material == Material.sand || material == Material.snow || material == Material.craftedSnow) {
            return woodenShovel;
        }
        return woodenPickaxe;
    }

    private FakePlayer getFakePlayer() {
        if (this.fakePlayer == null && !this.worldObj.isRemote && this.worldObj instanceof WorldServer) {
            FakePlayer player = FakePlayerFactory.get((WorldServer) this.worldObj, FAKE_PROFILE);
            player.inventory.currentItem = 0;
            player.inventory.setInventorySlotContents(0, woodenPickaxe);
            this.fakePlayer = player;
        }
        return this.fakePlayer;
    }

    public EntityPlayer getDriver() {
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
    public void applyEntityCollision(Entity entity) {
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    private void destroyRam() {
        if (this.riddenByEntity != null) {
            this.riddenByEntity.mountEntity(null);
        }
        for (EntityRamSeat seat : seats) {
            if (seat != null && seat.riddenByEntity != null) {
                seat.riddenByEntity.mountEntity(null);
            }
        }
        this.setDead();
    }

    @Override
    public boolean interactFirst(EntityPlayer player) {
        if (player == null || player.isSneaking()) return false;

        if (!this.worldObj.isRemote) {
            if (hasCooldown(player)) {
                long sec = getRemainingCooldown(player);
                player.addChatMessage(new net.minecraft.util.ChatComponentText("\u00a7c\u0412\u044b \u0441\u043c\u043e\u0436\u0435\u0442\u0435 \u0441\u0435\u0441\u0442\u044c \u0432 \u0442\u0430\u0440\u0430\u043d \u0447\u0435\u0440\u0435\u0437 " + sec + " \u0441\u0435\u043a."));
                return true;
            }

            if (this.riddenByEntity == null || this.riddenByEntity.isDead) {
                player.mountEntity(this);
                return true;
            }

            if (this.riddenByEntity == player) return true;

            UUID id = player.getUniqueID();
            if (seatIndexByPassenger.containsKey(id)) return true;

            int seatIndex = getFirstAvailableSeat();
            if (seatIndex == -1) return true;

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

    public void onSeatOccupantChanged(int seatIndex, Entity oldPassenger, Entity newPassenger) {
        if (this.worldObj.isRemote || seatIndex < 0 || seatIndex >= MAX_PASSENGERS) return;

        if (oldPassenger instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) oldPassenger;
            UUID id = player.getUniqueID();
            passengerOrder.remove(id);
            seatIndexByPassenger.remove(id);
            if (!player.isDead && player != this.riddenByEntity && !this.isDead) {
                dismountCooldowns.put(id, this.worldObj.getTotalWorldTime());
            }
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
        if (nbt.hasKey("Health")) {
            setHealth(nbt.getFloat("Health"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements) {
        double dx = x - this.posX;
        double dy = y - this.posY;
        double dz = z - this.posZ;
        double distSq = dx * dx + dy * dy + dz * dz;
        if (distSq <= 1.0D) {
            this.ramPosRotationIncrements = 3;
        } else {
            this.ramPosRotationIncrements = posRotationIncrements + 5;
        }
        this.ramX = x;
        this.ramY = y;
        this.ramZ = z;
        this.ramYaw = yaw;
        this.ramPitch = pitch;
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setVelocity(double x, double y, double z) {
        this.velocityX = x;
        this.velocityY = 0.0D;
        this.velocityZ = z;
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setFloat("Rotation", this.rotationYaw);
        nbt.setFloat("Health", getHealth());
    }

    static class SeatDefinition {
        final float offsetX;
        final float offsetY;
        final float offsetZ;

        SeatDefinition(float offsetX, float offsetY, float offsetZ) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
        }
    }

    static class BlockTarget {
        final int x;
        final int y;
        final int z;

        BlockTarget(int x, int y, int z) {
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
