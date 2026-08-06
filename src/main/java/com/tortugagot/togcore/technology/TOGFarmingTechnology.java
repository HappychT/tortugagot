package com.tortugagot.togcore.technology;

import got.common.block.other.GOTBlockGrapevine;
import got.common.database.GOTRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public final class TOGFarmingTechnology {
    private TOGFarmingTechnology() {
    }

    public static FarmingLock getHoeLock() {
        return lock(TOGTechnologyLocks.FARMER_WHEAT, "вспахать землю");
    }

    public static FarmingLock getPlantingLock(ItemStack stack, World world, int x, int y, int z) {
        if (stack == null || stack.getItem() == null) {
            return null;
        }
        Item item = stack.getItem();
        if (item == Items.wheat_seeds) {
            return lock(TOGTechnologyLocks.FARMER_WHEAT, "посадить пшеницу");
        }
        if (item == Items.carrot || item == Items.potato || item == GOTRegistry.turnip) {
            return lock(TOGTechnologyLocks.FARMER_BASIC_CROPS, "посадить эту культуру");
        }
        if (item == GOTRegistry.pipeweedSeeds) {
            return lock(TOGTechnologyLocks.FARMER_TOBACCO, "посадить табак");
        }
        if (isAdvancedPlantingItem(stack)) {
            return lock(TOGTechnologyLocks.FARMER_ADVANCED_CROPS, "посадить эту культуру");
        }
        if (item instanceof IPlantable) {
            return getCropLock(((IPlantable) item).getPlant(world, x, y, z));
        }
        return null;
    }

    public static boolean isPlantingAttempt(World world, int x, int y, int z, int face, ItemStack stack) {
        if (world == null || stack == null || stack.getItem() == null) {
            return false;
        }
        Item item = stack.getItem();
        if (item == Items.dye && stack.getItemDamage() == 3) {
            return face > 1 && world.getBlock(x, y, z) == Blocks.log;
        }
        if (item == GOTRegistry.seedsGrapeRed || item == GOTRegistry.seedsGrapeWhite) {
            return world.getBlock(x, y, z) == GOTRegistry.grapevine && GOTBlockGrapevine.canPlantGrapesAt(world, x, y, z, (IPlantable) item);
        }
        if (item == GOTRegistry.rice || item == Items.reeds || item == Item.getItemFromBlock(GOTRegistry.reeds) || item == Item.getItemFromBlock(GOTRegistry.driedReeds)) {
            return world.getBlock(x, y, z).getMaterial() == Material.water && world.getBlockMetadata(x, y, z) == 0 && world.isAirBlock(x, y + 1, z);
        }
        if (item instanceof IPlantable) {
            IPlantable plantable = (IPlantable) item;
            EnumPlantType plantType = plantable.getPlantType(world, x, y, z);
            if (plantType == EnumPlantType.Water) {
                return world.getBlock(x, y, z).getMaterial() == Material.water && world.getBlockMetadata(x, y, z) == 0 && world.isAirBlock(x, y + 1, z);
            }
            return face == 1 && world.isAirBlock(x, y + 1, z) && world.getBlock(x, y, z).canSustainPlant(world, x, y, z, ForgeDirection.UP, plantable);
        }
        return false;
    }

    public static boolean isHoeTarget(World world, int x, int y, int z) {
        if (world == null || !world.isAirBlock(x, y + 1, z)) {
            return false;
        }
        Block block = world.getBlock(x, y, z);
        return block == Blocks.grass || block == Blocks.dirt || block == GOTRegistry.mudGrass || block == GOTRegistry.mud;
    }

    public static boolean denyIfPlantingLocked(EntityPlayer player, ItemStack stack, World world, int x, int y, int z) {
        FarmingLock lock = getPlantingLock(stack, world, x, y, z);
        if (lock == null || TOGTechnologyLocks.has(player, lock.getTechnologyId())) {
            return false;
        }
        notifyBlocked(player, lock);
        return true;
    }

    public static void notifyBlocked(EntityPlayer player, FarmingLock lock) {
        if (player != null && lock != null && !player.worldObj.isRemote) {
            player.addChatMessage(new ChatComponentText(lock.getMessage()));
        }
    }

    private static FarmingLock getCropLock(Block block) {
        if (block == Blocks.wheat) {
            return lock(TOGTechnologyLocks.FARMER_WHEAT, "посадить пшеницу");
        }
        if (block == Blocks.carrots || block == Blocks.potatoes || block == GOTRegistry.turnipCrop) {
            return lock(TOGTechnologyLocks.FARMER_BASIC_CROPS, "посадить эту культуру");
        }
        if (block == GOTRegistry.pipeweedCrop) {
            return lock(TOGTechnologyLocks.FARMER_TOBACCO, "посадить табак");
        }
        if (block == GOTRegistry.flaxCrop || block == GOTRegistry.cucumberCrop || block == GOTRegistry.leekCrop || block == GOTRegistry.lettuceCrop || block == GOTRegistry.yamCrop || block == GOTRegistry.ricePlant || block == GOTRegistry.cornStalk || block == GOTRegistry.grapevineRed || block == GOTRegistry.grapevineWhite || block == Blocks.melon_stem || block == Blocks.pumpkin_stem || block == Blocks.cocoa) {
            return lock(TOGTechnologyLocks.FARMER_ADVANCED_CROPS, "посадить эту культуру");
        }
        return null;
    }

    private static boolean isAdvancedPlantingItem(ItemStack stack) {
        Item item = stack.getItem();
        return item == GOTRegistry.flaxSeeds || item == GOTRegistry.cucumberSeeds || item == GOTRegistry.leek || item == GOTRegistry.lettuce || item == GOTRegistry.yam || item == GOTRegistry.rice || item == GOTRegistry.seedsGrapeRed || item == GOTRegistry.seedsGrapeWhite || item == Item.getItemFromBlock(GOTRegistry.cornStalk) || item == Items.melon_seeds || item == Items.pumpkin_seeds || item == Items.reeds || item == Item.getItemFromBlock(GOTRegistry.reeds) || item == Item.getItemFromBlock(GOTRegistry.driedReeds) || item == Items.dye && stack.getItemDamage() == 3;
    }

    private static FarmingLock lock(String technologyId, String actionText) {
        return new FarmingLock(technologyId, TOGTechnologyNotifier.buildMessage(actionText, "не открыта необходимая технология", technologyId));
    }

    public static class FarmingLock {
        private final String technologyId;
        private final String message;

        private FarmingLock(String technologyId, String message) {
            this.technologyId = technologyId;
            this.message = message;
        }

        public String getTechnologyId() {
            return technologyId;
        }

        public String getMessage() {
            return message;
        }
    }
}
