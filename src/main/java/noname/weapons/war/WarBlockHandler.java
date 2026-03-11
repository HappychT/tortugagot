package noname.weapons.war;

import brain.factions.servers.SiegeActivationManager;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import got.common.GOTBannerProtection;
import got.common.database.GOTRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Обработчик блоков для военного режима.
 * Управляет размещением и ломанием земли в приватах во время войны.
 */
public class WarBlockHandler {

    private static final float WAR_DIG_SPEED = 1.0F;
    private static final List<Object[]> PENDING_DIRT_FALL = new ArrayList<>();

    public static void scheduleDirtFallCheck(World world, int x, int y, int z) {
        synchronized (PENDING_DIRT_FALL) {
            PENDING_DIRT_FALL.add(new Object[]{world, x, y, z});
        }
    }

    public static void scheduleDirtColumnFallChecks(World world, int x, int y, int z) {
        for (int checkY = y; checkY < y + 6; checkY++) {
            if (!isDirtBlock(world.getBlock(x, checkY, z))) {
                break;
            }
            scheduleDirtFallCheck(world, x, checkY, z);
        }
    }

    public static boolean isWaterBlock(Block block) {
        return block == Blocks.water || block == Blocks.flowing_water;
    }

    public static boolean isWarActiveAt(World world, int x, int y, int z) {
        return world != null && SiegeActivationManager.getInstance().isSiegeActive(world.provider.dimensionId, x, y, z);
    }

    public static boolean canPlaceProtectedDirt(World world, int x, int y, int z, int side) {
        int targetX = x;
        int targetY = y;
        int targetZ = z;

        switch (side) {
            case 0:
                targetY--;
                break;
            case 1:
                targetY++;
                break;
            case 2:
                targetZ--;
                break;
            case 3:
                targetZ++;
                break;
            case 4:
                targetX--;
                break;
            case 5:
                targetX++;
                break;
            default:
                return false;
        }

        if (isWaterBlock(world.getBlock(targetX, targetY, targetZ))) {
            return true;
        }

        if (side != 1) {
            return false;
        }

        if (!isDirtBlock(world.getBlock(x, y, z))) {
            return false;
        }

        return countRaisedDirtStack(world, x, y, z) < 4;
    }

    /**
     * Обрабатывает размещение блоков
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onBlockPlace(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        EntityPlayer player = event.entityPlayer;
        World world = event.world;

        if (world.isRemote) {
            return;
        }

        ItemStack heldItem = player.getHeldItem();
        if (heldItem == null || !(heldItem.getItem() instanceof ItemBlock)) {
            return;
        }

        Block blockToPlace = ((ItemBlock) heldItem.getItem()).field_150939_a;

        // Проверяем, является ли это землёй
        if (!isDirtBlock(blockToPlace)) {
            return;
        }

        int x = event.x;
        int y = event.y;
        int z = event.z;
        int side = event.face;

        // Получаем координаты целевого блока
        int targetX = x;
        int targetY = y;
        int targetZ = z;

        switch (side) {
            case 0:
                targetY--;
                break; // Снизу
            case 1:
                targetY++;
                break; // Сверху
            case 2:
                targetZ--;
                break;
            case 3:
                targetZ++;
                break;
            case 4:
                targetX--;
                break;
            case 5:
                targetX++;
                break;
        }

        // Проверяем, защищена ли позиция
        boolean isProtected = GOTBannerProtection.isProtected(world, targetX, targetY, targetZ,
                GOTBannerProtection.forPlayer(player, GOTBannerProtection.Permission.FULL), false);

        if (!isProtected || !isWarActiveAt(world, targetX, targetY, targetZ)) {
            // Не в привате - обычное поведение
            return;
        }

        if (!canPlaceProtectedDirt(world, x, y, z, side)) {
            event.setCanceled(true);
            return;
        }

        scheduleDirtFallCheck(world, targetX, targetY, targetZ);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        synchronized (PENDING_DIRT_FALL) {
            Iterator<Object[]> it = PENDING_DIRT_FALL.iterator();
            while (it.hasNext()) {
                Object[] entry = it.next();
                World w = (World) entry[0];
                int px = (Integer) entry[1];
                int py = (Integer) entry[2];
                int pz = (Integer) entry[3];
                it.remove();
                if (w != null && w.blockExists(px, py, pz)) {
                    tryFall(w, px, py, pz);
                }
            }
        }
    }

    /**
     * Обрабатывает ломание блоков в приватах
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        World world = event.world;
        if (world.isRemote) {
            return;
        }

        Block block = event.block;

        // Только для земли
        if (!isDirtBlock(block)) {
            return;
        }

        int x = event.x;
        int y = event.y;
        int z = event.z;
        EntityPlayer player = event.getPlayer();

        // Проверяем, защищена ли позиция
        boolean isProtected = GOTBannerProtection.isProtected(world, x, y, z,
                GOTBannerProtection.anyBanner(), false);

        if (isProtected && isWarActiveAt(world, x, y, z)) {
            // В привате - разрешаем ломать землю
            event.setCanceled(false);
            scheduleDirtColumnFallChecks(world, x, y + 1, z);
        }
    }

    /**
     * Условие 3: Фиксированная скорость ломания земли в приватах во время войны
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Block block = event.block;

        // Только для земли
        if (!isDirtBlock(block)) {
            return;
        }

        EntityPlayer player = event.entityPlayer;
        World world = player.worldObj;

        // Проверяем, защищена ли позиция
        boolean isProtected = GOTBannerProtection.isProtected(world,
                (int) event.x, (int) event.y, (int) event.z,
                GOTBannerProtection.anyBanner(), false);

        if (isProtected && isWarActiveAt(world, (int) event.x, (int) event.y, (int) event.z)) {
            // В привате - фиксированная скорость
            event.newSpeed = WAR_DIG_SPEED;
        }
    }

    /**
     * Проверяет, является ли блок землёй
     */
    public static boolean isDirtBlock(Block block) {
        if (block == null) {
            return false;
        }

        // Основные типы земли
        if (block == Blocks.dirt ||
                block == Blocks.grass ||
                block == Blocks.farmland ||
                block == Blocks.mycelium) {
            return true;
        }

        // Проверяем блоки из мода GOT
        try {
            if (block == GOTRegistry.mud ||
                    block == GOTRegistry.mudGrass ||
                    block == GOTRegistry.mudFarmland ||
                    block == GOTRegistry.asshaiDirt) {
                return true;
            }
        } catch (Exception e) {
            // Блоки могут не существовать
        }

        // Проверяем материал
        if (block.getMaterial() == Material.ground ||
                block.getMaterial() == Material.grass ||
                block.getMaterial() == Material.clay) {
            return true;
        }

        return false;
    }

    private static boolean isRaisedDirtSegment(World world, int x, int y, int z) {
        if (!isDirtBlock(world.getBlock(x, y, z))) {
            return false;
        }

        return !isDirtBlock(world.getBlock(x + 1, y, z)) ||
                !isDirtBlock(world.getBlock(x - 1, y, z)) ||
                !isDirtBlock(world.getBlock(x, y, z + 1)) ||
                !isDirtBlock(world.getBlock(x, y, z - 1));
    }

    private static void tryFall(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (!isDirtBlock(block) || !canFall(world, x, y - 1, z)) {
            return;
        }
        int meta = world.getBlockMetadata(x, y, z);
        world.setBlockToAir(x, y, z);
        net.minecraft.entity.item.EntityFallingBlock falling = new net.minecraft.entity.item.EntityFallingBlock(
                world, x + 0.5, y + 0.5, z + 0.5, block, meta);
        world.spawnEntityInWorld(falling);
    }

    public static int countDirtBelow(World world, int x, int y, int z) {
        int count = 0;
        for (int checkY = y; checkY >= 0 && count < 5; checkY--) {
            Block block = world.getBlock(x, checkY, z);
            if (isDirtBlock(block)) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    public static int countRaisedDirtStack(World world, int x, int y, int z) {
        int count = 0;
        for (int checkY = y; checkY >= 0 && count < 5; checkY--) {
            if (!isRaisedDirtSegment(world, x, checkY, z)) {
                break;
            }
            count++;
        }
        return count;
    }

    private static boolean canFall(World world, int x, int y, int z) {
        if (y < 0) {
            return false;
        }
        Block block = world.getBlock(x, y, z);
        return block.isAir(world, x, y, z) ||
                block == Blocks.water ||
                block == Blocks.flowing_water ||
                block == Blocks.fire ||
                block.getMaterial().isLiquid();
    }
}
