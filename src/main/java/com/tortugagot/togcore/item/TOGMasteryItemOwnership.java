package com.tortugagot.togcore.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public final class TOGMasteryItemOwnership {
    private static final String OWNER_UUID_MOST = "TOGOwnerUUIDMost";
    private static final String OWNER_UUID_LEAST = "TOGOwnerUUIDLeast";
    private static final String OWNER_NAME = "TOGOwnerName";

    private TOGMasteryItemOwnership() {
    }

    public static boolean isMasteryItem(ItemStack stack) {
        return stack != null && stack.getItem() instanceof TOGItemMasteryPoints;
    }

    public static boolean hasOwner(ItemStack stack) {
        return getOwnerUuid(stack) != null;
    }

    public static UUID getOwnerUuid(ItemStack stack) {
        if (!isMasteryItem(stack) || !stack.hasTagCompound()) {
            return null;
        }
        NBTTagCompound tag = stack.getTagCompound();
        if (!tag.hasKey(OWNER_UUID_MOST) || !tag.hasKey(OWNER_UUID_LEAST)) {
            return null;
        }
        return new UUID(tag.getLong(OWNER_UUID_MOST), tag.getLong(OWNER_UUID_LEAST));
    }

    public static String getOwnerName(ItemStack stack) {
        if (!isMasteryItem(stack) || !stack.hasTagCompound()) {
            return null;
        }
        String ownerName = stack.getTagCompound().getString(OWNER_NAME);
        if (ownerName != null && ownerName.length() > 0) {
            return ownerName;
        }
        UUID ownerUuid = getOwnerUuid(stack);
        return ownerUuid != null ? ownerUuid.toString() : null;
    }

    public static void bindToPlayer(ItemStack stack, EntityPlayer player) {
        if (!isMasteryItem(stack) || player == null || hasOwner(stack)) {
            return;
        }
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        UUID ownerUuid = player.getUniqueID();
        NBTTagCompound tag = stack.getTagCompound();
        tag.setLong(OWNER_UUID_MOST, ownerUuid.getMostSignificantBits());
        tag.setLong(OWNER_UUID_LEAST, ownerUuid.getLeastSignificantBits());
        tag.setString(OWNER_NAME, player.getCommandSenderName());
    }

    public static boolean isOwnedBy(ItemStack stack, EntityPlayer player) {
        UUID ownerUuid = getOwnerUuid(stack);
        return ownerUuid != null && player != null && ownerUuid.equals(player.getUniqueID());
    }

    public static boolean ensureOwnedBy(ItemStack stack, EntityPlayer player) {
        bindToPlayer(stack, player);
        return isOwnedBy(stack, player);
    }
}
