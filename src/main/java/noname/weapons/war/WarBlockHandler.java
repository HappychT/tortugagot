package noname.weapons.war;

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

    // Фиксированная скорость ломания земли во время войны
    private static final float WAR_DIG_SPEED = 1.0F;

    /**
     * Обрабатывает размещение блоков
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onBlockPlace(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (!WarModeManager.getInstance().isWarModeActive()) {
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

        if (!isProtected) {
            // Не в привате - обычное поведение
            return;
        }

        Block targetBlock = world.getBlock(targetX, targetY, targetZ);

        // Условие 5: Замена воды разрешена без ограничений
        if (targetBlock == Blocks.water || targetBlock == Blocks.flowing_water) {
            // Разрешаем замену воды
            event.setCanceled(false);

            // Размещаем блок
            if (!world.isRemote) {
                world.setBlock(targetX, targetY, targetZ, blockToPlace);
                if (!player.capabilities.isCreativeMode) {
                    heldItem.stackSize--;
                }
                // Планируем падение земли
                scheduleFalling(world, targetX, targetY, targetZ);
            }
            event.setCanceled(true);
            return;
        }

        // Условие 1: Земля ставится только СВЕРХУ другой земли
        if (side != 1) {
            // Не сверху - запрещаем
            event.setCanceled(true);
            return;
        }

        // Проверяем, что под местом размещения есть земля
        Block blockBelow = world.getBlock(x, y, z);
        if (!isDirtBlock(blockBelow)) {
            // Под ногами не земля - запрещаем
            event.setCanceled(true);
            return;
        }

        // Условие 2: Максимум 4 блока земли в столбе
        int dirtCount = countDirtBelow(world, x, y, z);
        if (dirtCount >= 4) {
            // Уже 4 блока земли - запрещаем
            event.setCanceled(true);
            return;
        }

        // Разрешаем размещение
        event.setCanceled(false);

        // После размещения - земля должна упасть как гравий
        if (!world.isRemote) {
            // Используем отложенное обновление для проверки падения
            world.scheduleBlockUpdate(targetX, targetY, targetZ, blockToPlace, 2);
        }
    }

    /**
     * Обрабатывает ломание блоков в приватах
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!WarModeManager.getInstance().isWarModeActive()) {
            return;
        }

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
                GOTBannerProtection.forPlayer(player, GOTBannerProtection.Permission.FULL), false);

        if (isProtected) {
            // В привате - разрешаем ломать землю
            event.setCanceled(false);
        }
    }

    /**
     * Условие 3: Фиксированная скорость ломания земли в приватах во время войны
     */
    @SubscribeEvent
    public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (!WarModeManager.getInstance().isWarModeActive()) {
            return;
        }

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
                GOTBannerProtection.forPlayer(player, GOTBannerProtection.Permission.FULL), false);

        if (isProtected) {
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
                block.getMaterial() == Material.grass) {
            return true;
        }

        return false;
    }

    /**
     * Считает количество блоков земли под указанной позицией
     */
    private int countDirtBelow(World world, int x, int y, int z) {
        int count = 0;
        for (int checkY = y; checkY >= 0 && count < 5; checkY--) {
            Block block = world.getBlock(x, checkY, z);
            if (isDirtBlock(block)) {
                count++;
            } else {
                break; // Прерываем при первом не-земляном блоке
            }
        }
        return count;
    }

    /**
     * Планирует падение земли
     */
    private void scheduleFalling(World world, int x, int y, int z) {
        // Проверяем, может ли блок упасть
        if (canFall(world, x, y - 1, z)) {
            // Создаём падающий блок
            Block block = world.getBlock(x, y, z);
            int meta = world.getBlockMetadata(x, y, z);

            world.setBlockToAir(x, y, z);

            net.minecraft.entity.item.EntityFallingBlock fallingBlock = new net.minecraft.entity.item.EntityFallingBlock(
                    world, x + 0.5, y + 0.5, z + 0.5, block, meta);

            world.spawnEntityInWorld(fallingBlock);
        }
    }

    /**
     * Проверяет, может ли блок упасть в указанную позицию
     */
    private boolean canFall(World world, int x, int y, int z) {
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
