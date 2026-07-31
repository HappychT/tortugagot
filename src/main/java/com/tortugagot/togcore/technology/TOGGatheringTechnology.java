package com.tortugagot.togcore.technology;

import got.common.database.GOTRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.init.Blocks;

public final class TOGGatheringTechnology {
    private TOGGatheringTechnology() {
    }

    public static GatheringLock getLock(Block block, int meta) {
        if (block == null || block == Blocks.air) {
            return null;
        }
        if (isBerryBush(block) || isSeedGrass(block)) {
            return lock(TOGTechnologyLocks.GATHERER_BERRIES, "собрать ягоды или семена");
        }
        if (isTobacco(block)) {
            return lock(TOGTechnologyLocks.FARMER_TOBACCO, "собрать табак");
        }
        if (isWheat(block)) {
            return lock(TOGTechnologyLocks.FARMER_WHEAT, "собрать пшеницу");
        }
        if (isBasicCrop(block)) {
            return lock(TOGTechnologyLocks.FARMER_BASIC_CROPS, "собрать этот урожай");
        }
        if (isAdvancedCrop(block)) {
            return lock(TOGTechnologyLocks.FARMER_ADVANCED_CROPS, "собрать этот урожай");
        }
        if (isHerbOrPlant(block)) {
            return lock(TOGTechnologyLocks.GATHERER_HERBS, "собрать это растение");
        }
        if (isFlower(block, meta)) {
            return lock(TOGTechnologyLocks.GATHERER_FLOWERS, "собрать этот цветок");
        }
        return null;
    }

    public static boolean isBerryBush(Block block) {
        return block == GOTRegistry.berryBush;
    }

    public static boolean isCorn(Block block) {
        return block == GOTRegistry.cornStalk;
    }

    public static boolean isGrapes(Block block) {
        return block == GOTRegistry.grapevineRed || block == GOTRegistry.grapevineWhite;
    }

    private static GatheringLock lock(String technologyId, String actionText) {
        return new GatheringLock(technologyId, TOGTechnologyNotifier.buildMessage(actionText, "не открыта необходимая технология", technologyId));
    }

    private static boolean isSeedGrass(Block block) {
        return block == Blocks.tallgrass || block == GOTRegistry.tallGrass || block == GOTRegistry.aridGrass || block == GOTRegistry.asshaiGrass;
    }

    private static boolean isHerbOrPlant(Block block) {
        return block == Blocks.deadbush || block == GOTRegistry.plantain || block == GOTRegistry.blackroot || block == GOTRegistry.clover || block == GOTRegistry.deadMarshPlant || block == GOTRegistry.flaxPlant || block == GOTRegistry.cucumberPlant;
    }

    private static boolean isFlower(Block block, int meta) {
        if (block == Blocks.red_flower || block == Blocks.yellow_flower || block == GOTRegistry.asshaiFlower || block == GOTRegistry.bluebell || block == GOTRegistry.marigold || block == GOTRegistry.essosFlower || block == GOTRegistry.yitiFlower || block == GOTRegistry.doubleFlower) {
            return true;
        }
        if (block == Blocks.double_plant) {
            int flowerMeta = meta & 7;
            return flowerMeta == 0 || flowerMeta == 1 || flowerMeta == 4 || flowerMeta == 5;
        }
        return false;
    }

    private static boolean isTobacco(Block block) {
        return block == GOTRegistry.pipeweedCrop || block == GOTRegistry.pipeweedPlant;
    }

    private static boolean isWheat(Block block) {
        return block == Blocks.wheat;
    }

    private static boolean isBasicCrop(Block block) {
        return block == Blocks.carrots || block == Blocks.potatoes || block == GOTRegistry.turnipCrop;
    }

    private static boolean isAdvancedCrop(Block block) {
        return block == GOTRegistry.flaxCrop || block == GOTRegistry.cucumberCrop || block == GOTRegistry.leekCrop || block == GOTRegistry.lettuceCrop || block == GOTRegistry.yamCrop || block == GOTRegistry.ricePlant || block == GOTRegistry.cornStalk || block == GOTRegistry.grapevineRed || block == GOTRegistry.grapevineWhite || block == GOTRegistry.bananaBlock || block == GOTRegistry.dateBlock || block == GOTRegistry.reeds || block == GOTRegistry.driedReeds || block == Blocks.reeds || block == Blocks.melon_block || block == Blocks.pumpkin || block == Blocks.cocoa || block instanceof BlockCrops;
    }

    public static class GatheringLock {
        private final String technologyId;
        private final String message;

        private GatheringLock(String technologyId, String message) {
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
