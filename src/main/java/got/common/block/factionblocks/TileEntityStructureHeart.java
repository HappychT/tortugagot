package got.common.block.factionblocks;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityStructureHeart extends TileEntity {

    private String structureId = "";
    private ItemStack[] inventory = new ItemStack[9];

    public String getStructureId() { return structureId; }
    public void setStructureId(String id) { this.structureId = id; }
    public ItemStack[] getInventory() { return inventory; }

    public int addItems(ItemStack stack) {
        for (int i = 0; i < inventory.length; i++) {
            if (stack == null || stack.stackSize <= 0) return 0;
            if (inventory[i] == null) {
                inventory[i] = stack.copy();
                return 0;
            } else if (inventory[i].isItemEqual(stack) && ItemStack.areItemStackTagsEqual(inventory[i], stack)) {
                int space = inventory[i].getMaxStackSize() - inventory[i].stackSize;
                int transfer = Math.min(stack.stackSize, space);
                if (transfer > 0) {
                    inventory[i].stackSize += transfer;
                    stack.stackSize -= transfer;
                }
            }
        }
        return stack.stackSize;
    }

    public void collectItems(EntityPlayer player) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) {
                ItemStack stackToGive = inventory[i].copy();
                inventory[i] = null;

                if (!player.inventory.addItemStackToInventory(stackToGive)) {
                    player.worldObj.spawnEntityInWorld(new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, stackToGive));
                }
            }
        }
        this.markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }


    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setString("StructureID", this.structureId);
        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < this.inventory.length; i++) {
            if (this.inventory[i] != null) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(itemTag);
                itemList.appendTag(itemTag);
            }
        }
        nbt.setTag("Inventory", itemList);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.structureId = nbt.getString("StructureID");
        NBTTagList itemList = nbt.getTagList("Inventory", 10);
        this.inventory = new ItemStack[9];
        for (int i = 0; i < itemList.tagCount(); i++) {
            NBTTagCompound itemTag = itemList.getCompoundTagAt(i);
            byte slot = itemTag.getByte("Slot");
            if (slot >= 0 && slot < this.inventory.length) {
                this.inventory[slot] = ItemStack.loadItemStackFromNBT(itemTag);
            }
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }
}