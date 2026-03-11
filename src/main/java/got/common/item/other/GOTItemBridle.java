package got.common.item.other;

import java.lang.reflect.InvocationTargetException;

import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import got.common.database.GOTCreativeTabs;
import got.common.database.GOTEffects;
import got.common.entity.animal.GOTEntityBridleHorse;
import got.common.entity.animal.GOTEntityDirewolf;
import got.common.entity.animal.GOTEntityHorse;
import got.common.util.GOTReflection;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

/**
 * Поводья: призыв маунта по удержанию ПКМ 8 сек. Снятие — Shift (GOTTickHandlerServer).
 * При эффекте «Присутствие в бою» призыв недоступен и отменяется при появлении эффекта во время удержания.
 */
public class GOTItemBridle extends Item {

    /** Длительность удержания ПКМ для призыва (тики). Для клиентского прогресс-бара. */
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

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return SUMMON_TICKS;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.bow;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
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

    /** Можно ли начать удержание (нет эффекта боя; на сервере — не в седле и нет призванного маунта). */
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

    /** Можно ли завершить призыв (те же проверки, только на сервере). */
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

    /** Создаёт маунта, спавнит, сажает игрока, записывает id в данные. Вызывать только на сервере. */
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
