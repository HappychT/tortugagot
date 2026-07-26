package got.common.item.other;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import got.common.database.GOTCreativeTabs;
import got.common.database.GOTEffects;
import got.common.entity.animal.GOTEntityBridleHorse;
import got.common.entity.animal.GOTEntityDirewolf;
import got.common.entity.animal.GOTEntityHorse;
import got.common.util.GOTReflection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class GOTItemBridle extends Item {

    public static final int SUMMON_TICKS = 20 * 8;

    private final Class<? extends EntityLiving> entityClass;
    private final GOTBridleMountStats summonStats;
    private final String mountNameKey;

    public GOTItemBridle(Class<? extends EntityLiving> entityClass) {
        this(entityClass, null, "got.bridle_mount");
    }

    public GOTItemBridle(Class<? extends EntityLiving> entityClass, GOTBridleMountStats summonStats, String mountNameKey) {
        super();
        setMaxStackSize(1);
        setCreativeTab(GOTCreativeTabs.tabMisc);
        this.entityClass = entityClass;
        this.summonStats = summonStats;
        this.mountNameKey = mountNameKey;
    }

    public static final String OWNER_UUID_KEY = "BridleOwnerUUID";
    public static final String OWNER_NAME_KEY = "BridleOwnerName";

    public static void setOwner(ItemStack stack, EntityPlayer player) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound nbt = stack.getTagCompound();
        nbt.setString(OWNER_UUID_KEY, getPlayerUUID(player));
        nbt.setString(OWNER_NAME_KEY, player.getCommandSenderName());
    }

    public static String getOwnerUUID(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(OWNER_UUID_KEY, 8)) {
            return stack.getTagCompound().getString(OWNER_UUID_KEY);
        }
        return null;
    }

    public static String getOwnerName(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(OWNER_NAME_KEY, 8)) {
            return stack.getTagCompound().getString(OWNER_NAME_KEY);
        }
        return null;
    }

    public static boolean isOwner(ItemStack stack, EntityPlayer player) {
        String ownerUUID = getOwnerUUID(stack);
        if (ownerUUID != null && ownerUUID.equals(getPlayerUUID(player))) {
            return true;
        }
        String ownerName = getOwnerName(stack);
        if (ownerName != null && ownerName.equalsIgnoreCase(player.getCommandSenderName())) {
            return true;
        }
        return ownerUUID == null && ownerName == null;
    }

    public static boolean ensureOwner(ItemStack stack, EntityPlayer player) {
        String ownerUUID = getOwnerUUID(stack);
        String ownerName = getOwnerName(stack);
        if (ownerUUID == null && ownerName == null) {
            setOwner(stack, player);
            return true;
        }
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound nbt = stack.getTagCompound();
        boolean changed = false;
        if (ownerUUID == null && ownerName != null && ownerName.equalsIgnoreCase(player.getCommandSenderName())) {
            nbt.setString(OWNER_UUID_KEY, getPlayerUUID(player));
            changed = true;
        }
        if (ownerName == null && ownerUUID != null && ownerUUID.equals(getPlayerUUID(player))) {
            nbt.setString(OWNER_NAME_KEY, player.getCommandSenderName());
            changed = true;
        }
        return changed;
    }

    private static String getPlayerUUID(EntityPlayer player) {
        return player.getUniqueID().toString();
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
        String ownerName = getOwnerName(stack);
        if (ownerName != null) {
            list.add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("got.bridle_owner") + ": " + ownerName);
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return SUMMON_TICKS;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.bow;
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            ensureOwner(stack, player);
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isHeld) {
        if (world.isRemote || !(entity instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) entity;
        if (getOwnerUUID(stack) != null && getOwnerName(stack) != null) {
            return;
        }

        // In 1.7.10, mutating the held stack NBT during active use can reset itemInUse
        // and kill the summon bow animation plus the HUD countdown.
        if (player.isUsingItem() && player.getItemInUse() == stack) {
            return;
        }

        ensureOwner(stack, player);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!isOwner(stack, player)) {
            return stack;
        }
        if (!canStartSummon(player, world)) {
            return stack;
        }
        player.setItemInUse(stack, getMaxItemUseDuration(stack));
        return stack;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        World world = player.worldObj;
        if (world.isRemote) {
            return;
        }
        if (player.isPotionActive(GOTEffects.combatLog.id)) {
            player.clearItemInUse();
            return;
        }
        if (count != 1) {
            return;
        }
        if (!canCompleteSummon(player)) {
            return;
        }
        if (trySummonMount(stack, world, player)) {
            player.clearItemInUse();
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int timeLeft) {
        if (world.isRemote || timeLeft > 0) {
            return;
        }
        if (!canCompleteSummon(player)) {
            return;
        }
        trySummonMount(stack, world, player);
    }

    private boolean canStartSummon(EntityPlayer player, World world) {
        if (player.isPotionActive(GOTEffects.combatLog.id)) {
            return false;
        }
        if (!world.isRemote) {
            GOTPlayerData data = GOTLevelData.getData(player);
            if (player.isRiding() || data.getBridleMount() != -1) {
                return false;
            }
        }
        return true;
    }

    private boolean canCompleteSummon(EntityPlayer player) {
        if (player.isPotionActive(GOTEffects.combatLog.id)) {
            return false;
        }
        GOTPlayerData data = GOTLevelData.getData(player);
        if (player.isRiding() || data.getBridleMount() != -1) {
            return false;
        }
        return true;
    }

    private boolean trySummonMount(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote) {
            return false;
        }
        GOTPlayerData data = GOTLevelData.getData(player);
        if (player.isRiding() || data.getBridleMount() != -1) {
            return false;
        }
        EntityLiving mount = createMount(world);
        if (mount == null) {
            return false;
        }
        mount.setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        mount.setCustomNameTag(StatCollector.translateToLocal(mountNameKey));
        prepareMountForPlayer(mount, player);
        world.spawnEntityInWorld(mount);
        (summonStats != null ? summonStats : GOTBridleMountStats.getStats(mount.getClass())).applyTo(mount);
        applyPostSpawnMountSetup(mount);
        player.mountEntity(mount);
        data.setBridleMount(mount.getEntityId());
        return true;
    }

    private void prepareMountForPlayer(EntityLiving mount, EntityPlayer player) {
        if (mount instanceof GOTEntityHorse) {
            GOTEntityHorse horse = (GOTEntityHorse) mount;
            horse.setTamedBy(player);
            if (horse instanceof GOTEntityBridleHorse) {
                ((GOTEntityBridleHorse) horse).applyBridleAppearance();
            }
            GOTReflection.getHorseInv(horse).setInventorySlotContents(0, new ItemStack(Items.saddle));
            GOTReflection.setupHorseInv(horse);
            horse.setHorseSaddled(true);
            return;
        }
        if (mount instanceof GOTEntityDirewolf) {
            GOTEntityDirewolf direwolf = (GOTEntityDirewolf) mount;
            direwolf.setBridleTamedBy(player);
            direwolf.getMountInventory().setInventorySlotContents(0, new ItemStack(Items.saddle));
            direwolf.setMountSaddled(true);
            direwolf.setHostile(false);
            direwolf.setAttackTarget(null);
        }
    }

    private void applyPostSpawnMountSetup(EntityLiving mount) {
    }

    private EntityLiving createMount(World world) {
        try {
            return entityClass.getConstructor(World.class).newInstance(world);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }
}
