package brain.alchemy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryBagAdapter implements IInventory {

    private ItemStack[] inventory = new ItemStack[9];
    private ItemStack bagStack;
    private EntityPlayer player;

    public InventoryBagAdapter(ItemStack bag, EntityPlayer player) {
        this.bagStack = bag;
        this.player = player;
        loadFromNBT();
    }

    private void loadFromNBT() {
        if (!bagStack.hasTagCompound()) {
            bagStack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound nbt = bagStack.getTagCompound();
        NBTTagList potions = nbt.getTagList("Potions", 10);

        inventory = new ItemStack[9];

        for (int i = 0; i < potions.tagCount(); i++) {
            NBTTagCompound potionTag = potions.getCompoundTagAt(i);
            int slot = potionTag.getInteger("Slot");
            if (slot >= 0 && slot < 9) {
                ItemStack stack = ItemStack.loadItemStackFromNBT(potionTag);
                if (stack != null) {
                    stack.stackSize = potionTag.getInteger("Count");
                    inventory[slot] = stack;
                }
            }
        }
    }

    private void saveToNBT() {
        NBTTagCompound nbt = bagStack.hasTagCompound() ? bagStack.getTagCompound() : new NBTTagCompound();
        NBTTagList potions = new NBTTagList();

        boolean hasActiveSlotItem = false;
        int activeSlot = nbt.getInteger("ActiveSlot");

        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) {
                NBTTagCompound potionTag = new NBTTagCompound();
                inventory[i].writeToNBT(potionTag);
                potionTag.setInteger("Count", inventory[i].stackSize);
                potionTag.setInteger("Slot", i);
                potions.appendTag(potionTag);

                if (i == activeSlot) hasActiveSlotItem = true;
            }
        }

        nbt.setTag("Potions", potions);

        if (!hasActiveSlotItem && potions.tagCount() > 0) {
            nbt.setInteger("ActiveSlot", potions.getCompoundTagAt(0).getInteger("Slot"));
        } else if (potions.tagCount() == 0) {
            nbt.setInteger("ActiveSlot", 0);
        }

        bagStack.setTagCompound(nbt);
    }

    @Override
    public int getSizeInventory() {
        return 9;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if (inventory[slot] != null) {
            ItemStack stack;
            if (inventory[slot].stackSize <= amount) {
                stack = inventory[slot];
                inventory[slot] = null;
                markDirty();
                return stack;
            } else {
                stack = inventory[slot].splitStack(amount);
                if (inventory[slot].stackSize == 0) {
                    inventory[slot] = null;
                }
                markDirty();
                return stack;
            }
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (inventory[slot] != null) {
            ItemStack stack = inventory[slot];
            inventory[slot] = null;
            return stack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory[slot] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public String getInventoryName() {
        return "container.alchemy_bag";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void markDirty() {
        saveToNBT();
        if (player != null) {
            player.inventory.markDirty();
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {
        saveToNBT();
        if (player != null) {
            player.inventory.markDirty();
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return true;
    }
}