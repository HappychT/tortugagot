package com.tortugagot.togcore.technology;

import got.common.block.other.GOTBlockOre;
import got.common.block.other.GOTBlockOreGem;
import got.common.database.GOTRegistry;
import got.common.enchant.GOTEnchantmentHelper;
import got.common.item.other.GOTItemGem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class TOGMiningTechnology {
    private TOGMiningTechnology() {
    }

    public static boolean applyHarvestDrops(BlockEvent.HarvestDropsEvent event) {
        if (event == null || event.block == null || event.block == Blocks.air || event.isSilkTouching) {
            return false;
        }
        Block block = event.block;
        int meta = event.blockMetadata;
        ArrayList<ItemStack> baseDrops = getDrops(event.world, block, event.x, event.y, event.z, meta, 0);
        if (baseDrops.isEmpty()) {
            return false;
        }

        boolean gemOre = isGemOre(block, baseDrops);
        boolean oreBlock = gemOre || isOreBlock(block, meta);
        if (!oreBlock) {
            return false;
        }

        EntityPlayer player = event.harvester;
        boolean experiencedMiner = player != null && TOGTechnologyLocks.has(player, TOGTechnologyLocks.EXPERIENCED_MINER);
        boolean masterMiner = player != null && TOGTechnologyLocks.has(player, TOGTechnologyLocks.MASTER_MINER);
        boolean legendaryMiner = player != null && TOGTechnologyLocks.has(player, TOGTechnologyLocks.LEGENDARY_MINER);
        int luck = getEffectiveMiningLuck(legendaryMiner || (gemOre && experiencedMiner), legendaryMiner, player != null ? player.getCurrentEquippedItem() : null);

        ArrayList<ItemStack> recalculated = gemOre ? getGemDrops(event.world, block, event.x, event.y, event.z, meta, baseDrops, experiencedMiner, luck, event.world.rand) : getOreDrops(event.world, block, event.x, event.y, event.z, meta, baseDrops, legendaryMiner ? luck : 0, event.world.rand);
        if (!gemOre && masterMiner) {
            recalculated = getAutoSmeltedDrops(recalculated);
        }

        event.drops.clear();
        event.drops.addAll(recalculated);
        return true;
    }

    static int getEffectiveMiningLuck(boolean modifiersEnabled, boolean legendaryMiner, ItemStack tool) {
        if (!modifiersEnabled && !legendaryMiner) {
            return 0;
        }
        int luck = modifiersEnabled && tool != null ? GOTEnchantmentHelper.calcLootingLevel(tool) : 0;
        if (legendaryMiner) {
            luck++;
        }
        return Math.max(0, luck);
    }

    static int applyModLuckAmount(int baseAmount, int luck, Random random) {
        if (baseAmount <= 0) {
            return 0;
        }
        if (luck <= 0) {
            return baseAmount;
        }
        int factor = random.nextInt(luck + 2) - 1;
        return baseAmount * (Math.max(factor, 0) + 1);
    }

    static boolean isGemDrop(ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return false;
        }
        Item item = stack.getItem();
        if (item instanceof GOTItemGem || item == Items.diamond || item == Items.emerald || item == Items.dye && stack.getItemDamage() == 4) {
            return true;
        }
        return hasOreDictionaryPrefix(stack, "gem");
    }

    static boolean isCoalDrop(ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return false;
        }
        return stack.getItem() == Items.coal || hasOreDictionaryName(stack, "coal") || hasOreDictionaryName(stack, "gemCoal");
    }

    static boolean canAutoSmeltDrop(ItemStack input, ItemStack result) {
        return input != null && result != null && input.getItem() != null && result.getItem() != null && !isGemDrop(input) && !isGemDrop(result) && !isCoalDrop(input) && !isCoalDrop(result) && !areSameItem(input, result);
    }

    static ItemStack getSmeltingResult(ItemStack input) {
        if (input == null || input.getItem() == null) {
            return null;
        }
        ItemStack lookup = input.copy();
        lookup.stackSize = 1;
        ItemStack result = FurnaceRecipes.smelting().getSmeltingResult(lookup);
        if (result == null && lookup.getItemDamage() != 0) {
            lookup.setItemDamage(0);
            result = FurnaceRecipes.smelting().getSmeltingResult(lookup);
        }
        return result != null ? result.copy() : null;
    }

    private static ArrayList<ItemStack> getDrops(World world, Block block, int x, int y, int z, int meta, int luck) {
        ArrayList<ItemStack> drops = block.getDrops(world, x, y, z, meta, luck);
        return drops != null ? drops : new ArrayList<ItemStack>();
    }

    private static ArrayList<ItemStack> getGemDrops(World world, Block block, int x, int y, int z, int meta, List<ItemStack> baseDrops, boolean experiencedMiner, int luck, Random random) {
        if (experiencedMiner) {
            return getDrops(world, block, x, y, z, meta, luck);
        }
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        for (ItemStack stack : baseDrops) {
            if (stack == null || stack.stackSize <= 0) {
                continue;
            }
            ItemStack copy = stack.copy();
            copy.stackSize = getHalfKeptAmount(copy.stackSize, random);
            if (copy.stackSize > 0) {
                drops.add(copy);
            }
        }
        return drops;
    }

    private static ArrayList<ItemStack> getOreDrops(World world, Block block, int x, int y, int z, int meta, List<ItemStack> baseDrops, int luck, Random random) {
        if (luck > 0 && !hasRawOreDrop(baseDrops)) {
            return getDrops(world, block, x, y, z, meta, luck);
        }
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        for (ItemStack stack : baseDrops) {
            if (stack == null || stack.stackSize <= 0) {
                continue;
            }
            ItemStack copy = stack.copy();
            if (isRawOreDrop(copy)) {
                copy.stackSize = applyModLuckAmount(copy.stackSize, luck, random);
            }
            if (copy.stackSize > 0) {
                drops.add(copy);
            }
        }
        return drops;
    }

    static int getHalfKeptAmount(int amount, Random random) {
        int kept = 0;
        for (int i = 0; i < amount; i++) {
            if (random.nextBoolean()) {
                kept++;
            }
        }
        return kept;
    }

    private static ArrayList<ItemStack> getAutoSmeltedDrops(List<ItemStack> drops) {
        ArrayList<ItemStack> resultDrops = new ArrayList<ItemStack>();
        for (ItemStack stack : drops) {
            resultDrops.add(getAutoSmeltedDrop(stack, getSmeltingResult(stack)));
        }
        return resultDrops;
    }

    static ItemStack getAutoSmeltedDrop(ItemStack stack, ItemStack result) {
        if (canAutoSmeltDrop(stack, result)) {
            ItemStack smelted = result.copy();
            smelted.stackSize *= stack.stackSize;
            return smelted;
        }
        return stack;
    }

    private static boolean hasRawOreDrop(List<ItemStack> drops) {
        for (ItemStack stack : drops) {
            if (isRawOreDrop(stack)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isRawOreDrop(ItemStack stack) {
        return canAutoSmeltDrop(stack, getSmeltingResult(stack));
    }

    private static boolean isGemOre(Block block, List<ItemStack> baseDrops) {
        if (block instanceof GOTBlockOreGem || block == GOTRegistry.oreGem || block == Blocks.diamond_ore || block == Blocks.emerald_ore || block == Blocks.lapis_ore) {
            return true;
        }
        if (baseDrops.isEmpty()) {
            return false;
        }
        for (ItemStack stack : baseDrops) {
            if (!isGemDrop(stack)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isOreBlock(Block block, int meta) {
        if (block instanceof GOTBlockOre || block instanceof BlockOre || block == Blocks.coal_ore || block == Blocks.iron_ore || block == Blocks.gold_ore || block == Blocks.redstone_ore || block == Blocks.lit_redstone_ore || block == Blocks.quartz_ore) {
            return true;
        }
        Item blockItem = Item.getItemFromBlock(block);
        if (blockItem == null) {
            return false;
        }
        ItemStack stack = new ItemStack(blockItem, 1, blockItem.getHasSubtypes() ? meta : 0);
        return hasOreDictionaryPrefix(stack, "ore");
    }

    private static boolean hasOreDictionaryPrefix(ItemStack stack, String prefix) {
        try {
            for (int id : OreDictionary.getOreIDs(stack)) {
                String name = OreDictionary.getOreName(id);
                if (name != null && name.startsWith(prefix)) {
                    return true;
                }
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    private static boolean hasOreDictionaryName(ItemStack stack, String expected) {
        try {
            for (int id : OreDictionary.getOreIDs(stack)) {
                String name = OreDictionary.getOreName(id);
                if (expected.equals(name)) {
                    return true;
                }
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    private static boolean areSameItem(ItemStack left, ItemStack right) {
        return left.getItem() == right.getItem() && left.getItemDamage() == right.getItemDamage();
    }
}
