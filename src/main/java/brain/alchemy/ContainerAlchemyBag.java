package brain.alchemy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

public class ContainerAlchemyBag extends Container {

    private IInventory bagInventory;
    private ItemStack bagStack;

    public ContainerAlchemyBag(InventoryPlayer playerInv, ItemStack bag) {
        this.bagStack = bag;
        this.bagInventory = new InventoryBagAdapter(bag);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.addSlotToContainer(new SlotPotion(bagInventory, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack result = null;
        Slot slot = (Slot) this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            result = slotStack.copy();

            if (slotIndex < 9) {

                if (!this.mergeItemStack(slotStack, 9, this.inventorySlots.size(), true)) {
                    return null;
                }
            } else {

                if (slotStack.getItem() == Items.potionitem && ItemPotion.isSplash(slotStack.getItemDamage())) {
                    if (!this.mergeItemStack(slotStack, 0, 9, false)) {
                        return null;
                    }
                } else {
                    return null;
                }
            }

            if (slotStack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }

        return result;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        if (!player.worldObj.isRemote) {
            bagInventory.closeInventory();
        }
    }

    class SlotPotion extends Slot {

        public SlotPotion(IInventory inv, int index, int x, int y) {
            super(inv, index, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return stack != null && stack.getItem() == Items.potionitem && ItemPotion.isSplash(stack.getItemDamage());
        }
    }
}
