package got.common.inventory;

import got.common.entity.animal.GOTEntityDirewolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

public class GOTContainerDirewolfInventory extends Container {
    private final IInventory mountInv;
    private final GOTEntityDirewolf mount;

    public GOTContainerDirewolfInventory(InventoryPlayer playerInv, IInventory mountInv, GOTEntityDirewolf mount) {
        int chestRows = 3;
        int yOffset = (chestRows - 4) * 18;
        this.mountInv = mountInv;
        this.mount = mount;
        mountInv.openInventory();
        addSlotToContainer(new Slot(mountInv, 0, 8, 18) {
            @Override
            public boolean isItemValid(ItemStack itemstack) {
                return itemstack != null && itemstack.getItem() == Items.saddle && !getHasStack();
            }
        });
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                addSlotToContainer(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 102 + row * 18 + yOffset));
            }
        }
        for (int col = 0; col < 9; ++col) {
            addSlotToContainer(new Slot(playerInv, col, 8 + col * 18, 160 + yOffset));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return mountInv.isUseableByPlayer(player) && mount.isEntityAlive() && mount.getDistanceToEntity(player) < 8.0f;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        mountInv.closeInventory();
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(slotIndex);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (slotIndex < mountInv.getSizeInventory()) {
                if (!mergeItemStack(itemstack1, mountInv.getSizeInventory(), inventorySlots.size(), true)) {
                    return null;
                }
            } else if (getSlot(0).isItemValid(itemstack1)) {
                if (!mergeItemStack(itemstack1, 0, 1, false)) {
                    return null;
                }
            } else {
                return null;
            }
            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }
}