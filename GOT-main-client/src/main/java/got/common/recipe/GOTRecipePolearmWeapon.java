package got.common.recipe;

import got.common.database.GOTRegistry;
import got.common.item.weapon.GOTItemPolearm;
import got.common.item.weapon.GOTItemShieldPike;
import got.common.item.weapon.GOTItemShieldReachPike;
import got.common.item.weapon.GOTItemShieldSpear;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class GOTRecipePolearmWeapon implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World p_77569_2_) {
        ItemStack shield = null;
        ItemStack weapon = null;

        for (int slotId = 0, size = inventoryCrafting.getSizeInventory(); slotId < size; slotId++) {
            ItemStack stackInSlot = inventoryCrafting.getStackInSlot(slotId);

            if (stackInSlot == null)
                continue;

            if (stackInSlot.getItem() == GOTRegistry.shield) {
                if (shield != null) {
                    return false;
                }
                shield = stackInSlot;
                continue;
            }

            if (stackInSlot.getItem() instanceof GOTItemPolearm) {
                if (weapon != null) {
                    return false;
                }
                weapon = stackInSlot;
            }
        }
        return weapon != null && shield != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        ItemStack shield = null;
        ItemStack weapon = null;

        for (int slotId = 0, size = inventoryCrafting.getSizeInventory(); slotId < size; slotId++) {
            ItemStack stackInSlot = inventoryCrafting.getStackInSlot(slotId);

            if (stackInSlot == null)
                continue;

            if (stackInSlot.getItem() == GOTRegistry.shield) {
                if (shield != null) {
                    return null;
                }
                shield = stackInSlot;
                continue;
            }

            if (stackInSlot.getItem() instanceof GOTItemPolearm) {
                if (weapon != null) {
                    return null;
                }
                weapon = stackInSlot;
            }
        }
        if (weapon != null && shield != null) {
            ItemStack weaponCopy = weapon.copy();
            return getNBTCompound(weaponCopy);
        }
        return null;
    }

    private static ItemStack getNBTCompound(ItemStack weaponCopy) {
        NBTTagCompound nbt = weaponCopy.getTagCompound();
        ItemStack result = null;

        if (weaponCopy.getItem() == GOTRegistry.westerosPike) {
            result = new ItemStack(GOTRegistry.shieldWestorPike);
            result.setTagCompound(nbt);
        }
        if (weaponCopy.getItem() == GOTRegistry.reachPike) {
            result = new ItemStack(GOTRegistry.shieldReachPike);
            result.setTagCompound(nbt);
        }
        if (weaponCopy.getItem() == GOTRegistry.essosPike) {
            result = new ItemStack(GOTRegistry.shieldEssosPike);
            result.setTagCompound(nbt);
        }
        if (weaponCopy.getItem() == GOTRegistry.westerosSpear) {
            result = new ItemStack(GOTRegistry.shieldWestorSpear);
            result.setTagCompound(nbt);
        }
        if (weaponCopy.getItem() == GOTRegistry.essosSpear) {
            result = new ItemStack(GOTRegistry.shieldEssosSpear);
            result.setTagCompound(nbt);
        }
        if (weaponCopy.getItem() == GOTRegistry.riverlandsTrident) {
            result = new ItemStack(GOTRegistry.shieldRiverlandsTrident);
            result.setTagCompound(nbt);
        }
        return result;
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }
}
