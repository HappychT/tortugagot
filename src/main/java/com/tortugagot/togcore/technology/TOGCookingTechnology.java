package com.tortugagot.togcore.technology;

import java.util.UUID;

import got.common.database.GOTRegistry;
import got.common.inventory.GOTContainerOven;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public final class TOGCookingTechnology {
    private TOGCookingTechnology() {
    }

    public static boolean hasCookingI(EntityPlayer player) {
        return TOGTechnologyLocks.has(player, TOGTechnologyLocks.COOKING_I);
    }

    public static boolean hasCookingII(EntityPlayer player) {
        return TOGTechnologyLocks.has(player, TOGTechnologyLocks.COOKING_II);
    }

    public static boolean hasCookingIII(EntityPlayer player) {
        return TOGTechnologyLocks.has(player, TOGTechnologyLocks.COOKING_III);
    }

    public static boolean canReceiveCookingResult(EntityPlayer player, ItemStack result) {
        String requiredTechnologyId = getRequiredTechnologyForResult(result);
        return requiredTechnologyId == null || TOGTechnologyLocks.has(player, requiredTechnologyId);
    }

    public static boolean isCookingUnlockedFor(World world, UUID playerUuid, int x, int y, int z, ItemStack result) {
        String requiredTechnologyId = getRequiredTechnologyForResult(result);
        if (requiredTechnologyId == null || world == null || world.isRemote) {
            return true;
        }
        if (playerUuid != null) {
            TOGTechnologySavedData data = TOGTechnologySavedData.get(world);
            return data != null && data.hasTechnology(playerUuid, requiredTechnologyId);
        }
        return TOGTechnologyLocks.isUnlockedNearby(world, x, y, z, requiredTechnologyId);
    }

    public static String getRequiredTechnologyForResult(ItemStack result) {
        if (result == null) {
            return null;
        }
        Item item = result.getItem();
        if (item == GOTRegistry.rhinoCooked || item == GOTRegistry.melonSoup || item == GOTRegistry.shishKebab) {
            return TOGTechnologyLocks.COOKING_I;
        }
        if (item == GOTRegistry.elephantCooked || item == GOTRegistry.marzipan || item == GOTRegistry.pancakeMapleSyrup) {
            return TOGTechnologyLocks.COOKING_II;
        }
        if (item == GOTRegistry.marzipanChocolate || item == GOTRegistry.gingerbread || item == GOTRegistry.walrusLardCooked) {
            return TOGTechnologyLocks.COOKING_III;
        }
        return null;
    }

    public static void discardLockedCookingOutputs(EntityPlayer player) {
        if (player == null || player.worldObj == null || player.worldObj.isRemote || player.openContainer == null) {
            return;
        }
        Container container = player.openContainer;
        if (container instanceof ContainerFurnace) {
            discardLockedOutputSlot(player, container, 2);
        } else if (container instanceof GOTContainerOven) {
            for (int slot = 9; slot < 18; slot++) {
                discardLockedOutputSlot(player, container, slot);
            }
        }
    }

    public static void discardSmeltedItem(EntityPlayer player, ItemStack smelting) {
        String requiredTechnologyId = getRequiredTechnologyForResult(smelting);
        if (requiredTechnologyId == null || TOGTechnologyLocks.has(player, requiredTechnologyId)) {
            return;
        }
        int amount = smelting != null ? smelting.stackSize : 0;
        removeFromInventory(player, smelting, amount);
        if (smelting != null) {
            smelting.stackSize = 0;
        }
        notifyCookingBlocked(player, smelting, requiredTechnologyId);
    }

    private static void discardLockedOutputSlot(EntityPlayer player, Container container, int slotIndex) {
        if (slotIndex < 0 || slotIndex >= container.inventorySlots.size()) {
            return;
        }
        Slot slot = (Slot) container.inventorySlots.get(slotIndex);
        if (slot == null || !slot.getHasStack()) {
            return;
        }
        ItemStack stack = slot.getStack();
        String requiredTechnologyId = getRequiredTechnologyForResult(stack);
        if (requiredTechnologyId == null || TOGTechnologyLocks.has(player, requiredTechnologyId)) {
            return;
        }
        ItemStack blocked = stack.copy();
        slot.putStack(null);
        slot.onSlotChanged();
        container.detectAndSendChanges();
        notifyCookingBlocked(player, blocked, requiredTechnologyId);
    }

    public static void notifyNearbyCookingBlocked(World world, int x, int y, int z, ItemStack result, String requiredTechnologyId) {
        if (world == null || world.isRemote || requiredTechnologyId == null) {
            return;
        }
        for (Object obj : world.playerEntities) {
            if (!(obj instanceof EntityPlayer)) {
                continue;
            }
            EntityPlayer player = (EntityPlayer) obj;
            if (player.getDistanceSq(x + 0.5D, y + 0.5D, z + 0.5D) <= 64.0D) {
                notifyCookingBlocked(player, result, requiredTechnologyId);
            }
        }
    }

    private static void removeFromInventory(EntityPlayer player, ItemStack target, int amount) {
        if (player == null || target == null || amount <= 0) {
            return;
        }
        int remaining = amount;
        for (int slot = 0; slot < player.inventory.mainInventory.length && remaining > 0; slot++) {
            ItemStack stack = player.inventory.mainInventory[slot];
            if (stack == null || !stack.isItemEqual(target)) {
                continue;
            }
            int removed = Math.min(stack.stackSize, remaining);
            stack.stackSize -= removed;
            remaining -= removed;
            if (stack.stackSize <= 0) {
                player.inventory.mainInventory[slot] = null;
            }
        }
        player.inventory.markDirty();
        player.inventoryContainer.detectAndSendChanges();
    }

    private static void notifyCookingBlocked(EntityPlayer player, ItemStack result, String requiredTechnologyId) {
        String action = "получить приготовленное блюдо";
        if (result != null) {
            action = "получить " + result.getDisplayName();
        }
        TOGTechnologyNotifier.notifyBlocked(player, "cooking:" + requiredTechnologyId, action, "не открыта технология для приготовления этого блюда", requiredTechnologyId);
    }
}
