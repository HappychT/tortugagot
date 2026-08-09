package noname.weapons.war;

import brain.factions.servers.StructureManager;
import brain.factions.structures.FactionStructureManager;
import brain.factions.structures.FactionStructureSlot;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
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

/**
 * Обработчик блоков для военного режима.
 * Управляет размещением и ломанием земли в приватах во время войны.
 */
public class WarBlockHandler {

    private static final float WAR_DIG_SPEED = 0.4f;
    private static final int WAR_RADIUS = 500;

    public static boolean isWarActiveAt(World world, int x, int y, int z) {
        if (world == null) {
            return false;
        }

        if (StructureManager.activeWarFortresses.isEmpty()) {
            return false;
        }

        for (String id : StructureManager.activeWarFortresses) {
            if (id == null) {
                continue;
            }
            FactionStructureSlot slot = FactionStructureManager.getStructureById(id);
            if (slot == null) {
                continue;
            }
            int dx = x - slot.xCoord;
            int dy = y - slot.yCoord;
            int dz = z - slot.zCoord;
            if ((dx * dx + dy * dy + dz * dz) <= (WAR_RADIUS * WAR_RADIUS)) {
                return true;
            }
        }

        return false;
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

        if (side != 1) {
            return false;
        }

        if (!isDirtBlock(world.getBlock(x, y, z))) {
            return false;
        }

        return countDirtBelow(world, x, y, z) < 4;
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

        // Явные исключения (материал слишком общий и включает несоответствующие блоки)
        if (block == Blocks.hay_block) {
            return false;
        }

        // Проверяем материал (как fallback для модовых "земляных" блоков)
        if (block.getMaterial() == Material.grass ||
                block.getMaterial() == Material.clay) {
            return true;
        }

        return false;
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
}