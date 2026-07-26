package got.common.entity.other;

import got.common.database.GOTRegistry;
import got.common.enchant.*;
import got.common.entity.GOTEnchaldBlacksmith;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;

import java.util.HashSet;

public class GOTBlacksmithOffer {
    private ItemStack[] items;
    private float[] costs;
    public static GOTEnchantment[] meleeWeaponEnchantments = new GOTEnchantment[10];
    public static GOTEnchantment[] rangedWeaponEnchantments = new GOTEnchantment[7];
    public static GOTEnchantment[] armorEnchantments = new GOTEnchantment[23];
    public static GOTEnchantment[] toolEnchantments = new GOTEnchantment[10];
    public static final int[] meleeWeaponEnchantmentsLevels = {1, 2, 3, 1, 2, 3, 1, 2, 3, 4};
    public static final int[] rangedWeaponEnchantmentsLevels = {1, 2, 3, 1, 2, 3, 4};
    public static final int[] armorEnchantmentsLevels = {1, 2, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3};
    public static final int[] toolEnchantmentsLevels = {1, 2, 3, 1, 2, 3, 1, 2, 3, 4};
    private static final GOTBlacksmithOffer[] meleeWeaponUnlockPrices = new GOTBlacksmithOffer[10];
    private static final GOTBlacksmithOffer[] rangedWeaponUnlockPrices = new GOTBlacksmithOffer[7];
    private static final GOTBlacksmithOffer[] armorUnlockPrices = new GOTBlacksmithOffer[23];
    private static final GOTBlacksmithOffer[] toolUnlockPrices = new GOTBlacksmithOffer[10];
    private static final GOTBlacksmithOffer[] enchantmentPrices = new GOTBlacksmithOffer[4];

    public GOTBlacksmithOffer(ItemStack[] itemstack, float[] cost) {
        this.items = itemstack;
        this.costs = cost;
    }

    static {
        int i = 0, j = 0, k = 0, l = 0;
        meleeWeaponEnchantments[i++] = GOTEnchantment.strong2;
        meleeWeaponEnchantments[i++] = GOTEnchantment.strong3;
        meleeWeaponEnchantments[i++] = GOTEnchantment.strong4;
        meleeWeaponEnchantments[i++] = GOTEnchantment.meleeSpeed1;
        meleeWeaponEnchantments[i++] = GOTEnchantment.meleeSpeed2;
        meleeWeaponEnchantments[i++] = GOTEnchantment.meleeSpeed3;
        meleeWeaponEnchantments[i++] = GOTEnchantment.meleeReach1;
        meleeWeaponEnchantments[i++] = GOTEnchantment.meleeReach2;
        meleeWeaponEnchantments[i++] = GOTEnchantment.meleeReach3;
        meleeWeaponEnchantments[i++] = GOTEnchantment.knockback1;
        rangedWeaponEnchantments[j++] = GOTEnchantment.rangedStrong1;
        rangedWeaponEnchantments[j++] = GOTEnchantment.rangedStrong2;
        rangedWeaponEnchantments[j++] = GOTEnchantment.rangedStrong3;
        rangedWeaponEnchantments[j++] = GOTEnchantment.rangedSpeed1;
        rangedWeaponEnchantments[j++] = GOTEnchantment.rangedSpeed2;
        rangedWeaponEnchantments[j++] = GOTEnchantment.rangedSpeed3;
        rangedWeaponEnchantments[j++] = GOTEnchantment.knockback1;
        armorEnchantments[k++] = GOTEnchantment.protect1;
        armorEnchantments[k++] = GOTEnchantment.protect2;
        armorEnchantments[k++] = GOTEnchantment.protectRanged1;
        armorEnchantments[k++] = GOTEnchantment.protectRanged2;
        armorEnchantments[k++] = GOTEnchantment.protectRanged3;
        armorEnchantments[k++] = GOTEnchantment.protectSword1;
        armorEnchantments[k++] = GOTEnchantment.protectSword2;
        armorEnchantments[k++] = GOTEnchantment.protectSword3;
        armorEnchantments[k++] = GOTEnchantment.protectPolearm1;
        armorEnchantments[k++] = GOTEnchantment.protectPolearm2;
        armorEnchantments[k++] = GOTEnchantment.protectPolearm3;
        armorEnchantments[k++] = GOTEnchantment.protectBattleaxe1;
        armorEnchantments[k++] = GOTEnchantment.protectBattleaxe2;
        armorEnchantments[k++] = GOTEnchantment.protectBattleaxe3;
        armorEnchantments[k++] = GOTEnchantment.protectHammer1;
        armorEnchantments[k++] = GOTEnchantment.ProtectHammer2;
        armorEnchantments[k++] = GOTEnchantment.ProtectHammer3;
        armorEnchantments[k++] = GOTEnchantment.protectFall1;
        armorEnchantments[k++] = GOTEnchantment.protectFall2;
        armorEnchantments[k++] = GOTEnchantment.protectFall3;
        armorEnchantments[k++] = GOTEnchantment.durable1;
        armorEnchantments[k++] = GOTEnchantment.durable2;
        armorEnchantments[k++] = GOTEnchantment.durable3;
        toolEnchantments[l++] = GOTEnchantment.toolSpeed2;
        toolEnchantments[l++] = GOTEnchantment.toolSpeed3;
        toolEnchantments[l++] = GOTEnchantment.toolSpeed4;
        toolEnchantments[l++] = GOTEnchantment.looting1;
        toolEnchantments[l++] = GOTEnchantment.looting2;
        toolEnchantments[l++] = GOTEnchantment.looting3;
        toolEnchantments[l++] = GOTEnchantment.durable1;
        toolEnchantments[l++] = GOTEnchantment.durable2;
        toolEnchantments[l++] = GOTEnchantment.durable3;
        toolEnchantments[l++] = GOTEnchantment.toolSilk;
        i = 0;
        j = 0;
        k = 0;
        l = 0;
        meleeWeaponUnlockPrices[i++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{5000.0f, 5.0f, 5.0f});
        meleeWeaponUnlockPrices[i++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{15000.0f, 5.0f, 5.0f});
        meleeWeaponUnlockPrices[i++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{30000.0f, 5.0f, 5.0f});
        meleeWeaponUnlockPrices[i++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{5000.0f, 5.0f, 5.0f});
        meleeWeaponUnlockPrices[i++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{15000.0f, 5.0f, 5.0f});
        meleeWeaponUnlockPrices[i++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{30000.0f, 5.0f, 5.0f});
        meleeWeaponUnlockPrices[i++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{5000.0f, 5.0f, 5.0f});
        meleeWeaponUnlockPrices[i++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{15000.0f, 5.0f, 5.0f});
        meleeWeaponUnlockPrices[i++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{30000.0f, 5.0f, 5.0f});
        meleeWeaponUnlockPrices[i++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{5000.0f, 5.0f, 5.0f});
        rangedWeaponUnlockPrices[j++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Items.string), new ItemStack(Items.stick)}, new float[]{10000, 64.0f, 64.0f});
        rangedWeaponUnlockPrices[j++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Items.string), new ItemStack(Items.stick)}, new float[]{20000, 64.0f, 64.0f});
        rangedWeaponUnlockPrices[j++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Items.string), new ItemStack(Items.stick)}, new float[]{30000, 64.0f, 64.0f});
        rangedWeaponUnlockPrices[j++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Items.string), new ItemStack(Items.stick)}, new float[]{10000, 64.0f, 64.0f});
        rangedWeaponUnlockPrices[j++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Items.string), new ItemStack(Items.stick)}, new float[]{20000, 64.0f, 64.0f});
        rangedWeaponUnlockPrices[j++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Items.string), new ItemStack(Items.stick)}, new float[]{30000, 64.0f, 64.0f});
        rangedWeaponUnlockPrices[j++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Items.string), new ItemStack(Items.stick)}, new float[]{50000, 64.0f, 64.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{2000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{5000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{2000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{5000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{10000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{2000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{5000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{10000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{2000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{5000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{10000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{2000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{5000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{10000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{2000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{5000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{10000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{2000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{5000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{10000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{2000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{5000.0f, 5.0f, 5.0f});
        armorUnlockPrices[k++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{10000.0f, 5.0f, 5.0f});
        toolUnlockPrices[l++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{5000.0f, 5.0f, 5.0f});
        toolUnlockPrices[l++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{10000.0f, 5.0f, 5.0f});
        toolUnlockPrices[l++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{20000.0f, 5.0f, 5.0f});
        toolUnlockPrices[l++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{5000.0f, 5.0f, 5.0f});
        toolUnlockPrices[l++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{10000.0f, 5.0f, 5.0f});
        toolUnlockPrices[l++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{20000.0f, 5.0f, 5.0f});
        toolUnlockPrices[l++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{5000.0f, 5.0f, 5.0f});
        toolUnlockPrices[l++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{10000.0f, 5.0f, 5.0f});
        toolUnlockPrices[l++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{20000.0f, 5.0f, 5.0f});
        toolUnlockPrices[l++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block)}, new float[]{2000.0f, 5.0f, 5.0f});
        i = 0;
        enchantmentPrices[i++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(GOTRegistry.alloySteelIngot)}, new float[]{250.0f, 1.0f});
        enchantmentPrices[i++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(GOTRegistry.alloySteelIngot)}, new float[]{500.0f, 5.0f});
        enchantmentPrices[i++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(GOTRegistry.alloySteelIngot)}, new float[]{1000.0f, 10.0f});
        enchantmentPrices[i++] = new GOTBlacksmithOffer(new ItemStack[]{new ItemStack(GOTRegistry.coin), new ItemStack(GOTRegistry.alloySteelIngot)}, new float[]{100.0f, 10.0f});
    }

    public static GOTEnchantment[] getEnchantments(GOTEnchaldBlacksmith.BlacksmithType type) {
        switch (type) {
            case MW:
                return meleeWeaponEnchantments;
            case RW:
                return rangedWeaponEnchantments;
            case ARM:
                return armorEnchantments;
            case TOOL:
                return toolEnchantments;
            default:
                return null;
        }
    }

    public static GOTBlacksmithOffer getUnlockOffer(int slot, GOTEnchaldBlacksmith.BlacksmithType type) {
        switch (type) {
            case MW:
                return meleeWeaponUnlockPrices[slot];
            case RW:
                return rangedWeaponUnlockPrices[slot];
            case ARM:
                return armorUnlockPrices[slot];
            case TOOL:
                return toolUnlockPrices[slot];
            default:
                return null;
        }
    }

    public static HashSet<Class<?>> getEnchantmentClasses(GOTEnchaldBlacksmith.BlacksmithType type) {
        switch (type) {
            case MW:
                return new HashSet<Class<?>>(Arrays.asList(new Class[]{GOTEnchantmentDamage.class, GOTEnchantmentMeleeSpeed.class, GOTEnchantmentMeleeReach.class, GOTEnchantmentKnockback.class}));
            case RW:
                return new HashSet<Class<?>>(Arrays.asList(new Class[]{GOTEnchantmentRangedDamage.class, GOTEnchantmentRangedSpeed.class, GOTEnchantmentDurability.class, GOTEnchantmentRangedKnockback.class}));
            case ARM:
                return new HashSet<Class<?>>(Arrays.asList(new Class[]{GOTEnchantmentProtection.class, GOTEnchantmentProtectionSword.class, GOTEnchantmentProtectionPolearm.class, GOTEnchantmentProtectionBattlaxe.class, GOTEnchantmentProtectionHammer.class, GOTEnchantmentProtectionFall.class, GOTEnchantmentProtectionFire.class, GOTEnchantmentDurability.class}));
            case TOOL:
                return new HashSet<Class<?>>(Arrays.asList(new Class[]{GOTEnchantmentToolSpeed.class, GOTEnchantmentLooting.class, GOTEnchantmentDurability.class}));
            default:
                return null;
        }
    }

    public static int[] getEnchantmentLevels(GOTEnchaldBlacksmith.BlacksmithType type) {
        switch (type) {
            case MW:
                return GOTBlacksmithOffer.meleeWeaponEnchantmentsLevels;
            case RW:
                return GOTBlacksmithOffer.rangedWeaponEnchantmentsLevels;
            case ARM:
                return GOTBlacksmithOffer.armorEnchantmentsLevels;
            case TOOL:
                return GOTBlacksmithOffer.toolEnchantmentsLevels;
            default:
                return null;
        }
    }

    public GOTBlacksmithOffer getUnlockCost(float multiplier, float counter) {
        float[] newCosts = new float[costs.length];
        for (int i = 0; i < costs.length; i++) {
            if (i == 0)
                newCosts[i] = costs[i] + costs[i] * multiplier;
            else
                newCosts[i] = costs[i] + counter;
        }
        return new GOTBlacksmithOffer(items, newCosts);
    }

    public static GOTBlacksmithOffer getEnchantmentPrice(int slot) {
        return enchantmentPrices[slot];
    }

    public ItemStack[] getItems() {
        return items;
    }

    public float[] getCosts() {
        return costs;
    }

    public int getEnchantmentLevel(int slot, GOTEnchaldBlacksmith.BlacksmithType type) {
        switch (type) {
            case MW:
                return meleeWeaponEnchantmentsLevels[slot];
            case RW:
                return rangedWeaponEnchantmentsLevels[slot];
            case ARM:
                return armorEnchantmentsLevels[slot];
            case TOOL:
                return toolEnchantmentsLevels[slot];
            default:
                return 0;
        }
    }
}
