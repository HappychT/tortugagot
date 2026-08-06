package com.tortugagot.togcore.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TOGMasteryItemReturnData extends WorldSavedData {
    private static final String DATA_NAME = "togcore_mastery_item_returns";

    private final Map<String, List<ItemStack>> pendingItems = new HashMap<String, List<ItemStack>>();

    public TOGMasteryItemReturnData(String name) {
        super(name);
    }

    public static TOGMasteryItemReturnData get(World world) {
        World storageWorld = DimensionManager.getWorld(0);
        if (storageWorld == null) {
            storageWorld = world;
        }
        MapStorage storage = storageWorld.perWorldStorage;
        TOGMasteryItemReturnData data = (TOGMasteryItemReturnData) storage.loadData(TOGMasteryItemReturnData.class, DATA_NAME);
        if (data == null) {
            data = new TOGMasteryItemReturnData(DATA_NAME);
            storage.setData(DATA_NAME, data);
        }
        return data;
    }

    public void queue(UUID ownerUuid, ItemStack stack) {
        if (ownerUuid == null || stack == null || stack.stackSize <= 0) {
            return;
        }
        String ownerKey = ownerUuid.toString();
        List<ItemStack> stacks = pendingItems.get(ownerKey);
        if (stacks == null) {
            stacks = new ArrayList<ItemStack>();
            pendingItems.put(ownerKey, stacks);
        }
        stacks.add(stack.copy());
        markDirty();
    }

    public boolean deliver(EntityPlayer player) {
        if (player == null) {
            return false;
        }
        String ownerKey = player.getUniqueID().toString();
        List<ItemStack> stacks = pendingItems.get(ownerKey);
        if (stacks == null || stacks.isEmpty()) {
            return false;
        }

        boolean changed = false;
        Iterator<ItemStack> iterator = stacks.iterator();
        while (iterator.hasNext()) {
            ItemStack pendingStack = iterator.next();
            TOGMasteryItemOwnership.bindToPlayer(pendingStack, player);
            ItemStack toAdd = pendingStack.copy();
            int originalSize = toAdd.stackSize;
            boolean added = player.inventory.addItemStackToInventory(toAdd);
            if (added || toAdd.stackSize <= 0) {
                iterator.remove();
                changed = true;
            } else if (toAdd.stackSize != originalSize) {
                pendingStack.stackSize = toAdd.stackSize;
                changed = true;
                break;
            } else {
                break;
            }
        }

        if (stacks.isEmpty()) {
            pendingItems.remove(ownerKey);
        }
        if (changed) {
            markDirty();
            player.inventory.markDirty();
            player.inventoryContainer.detectAndSendChanges();
        }
        return changed;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        pendingItems.clear();
        NBTTagList owners = compound.getTagList("Owners", 10);
        for (int ownerIndex = 0; ownerIndex < owners.tagCount(); ownerIndex++) {
            NBTTagCompound ownerTag = owners.getCompoundTagAt(ownerIndex);
            String ownerKey = ownerTag.getString("Owner");
            NBTTagList stacksTag = ownerTag.getTagList("Items", 10);
            List<ItemStack> stacks = new ArrayList<ItemStack>();
            for (int stackIndex = 0; stackIndex < stacksTag.tagCount(); stackIndex++) {
                ItemStack stack = ItemStack.loadItemStackFromNBT(stacksTag.getCompoundTagAt(stackIndex));
                if (TOGMasteryItemOwnership.isMasteryItem(stack)) {
                    stacks.add(stack);
                }
            }
            if (ownerKey.length() > 0 && !stacks.isEmpty()) {
                pendingItems.put(ownerKey, stacks);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        NBTTagList owners = new NBTTagList();
        for (Map.Entry<String, List<ItemStack>> entry : pendingItems.entrySet()) {
            NBTTagCompound ownerTag = new NBTTagCompound();
            ownerTag.setString("Owner", entry.getKey());
            NBTTagList stacksTag = new NBTTagList();
            for (ItemStack stack : entry.getValue()) {
                if (TOGMasteryItemOwnership.isMasteryItem(stack)) {
                    NBTTagCompound stackTag = new NBTTagCompound();
                    stack.writeToNBT(stackTag);
                    stacksTag.appendTag(stackTag);
                }
            }
            if (stacksTag.tagCount() > 0) {
                ownerTag.setTag("Items", stacksTag);
                owners.appendTag(ownerTag);
            }
        }
        compound.setTag("Owners", owners);
    }
}
