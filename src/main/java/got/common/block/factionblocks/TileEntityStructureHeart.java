package got.common.block.factionblocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import java.util.HashMap;
import java.util.Map;

public class TileEntityStructureHeart extends TileEntity {

    private String structureId = "";
    private ItemStack[] inventory = new ItemStack[9];

    private Map<String, Double> resourceBuffer = new HashMap<>();

    public String getStructureId() { return structureId; }
    public void setStructureId(String id) { this.structureId = id; }
    public ItemStack[] getInventory() { return inventory; }

    public void addFractionalItem(String itemId, double amount) {
        double current = resourceBuffer.getOrDefault(itemId, 0.0);
        double total = current + amount;

        int itemsToGive = (int) total;

        resourceBuffer.put(itemId, total - itemsToGive);

        if (itemsToGive > 0) {
            net.minecraft.item.Item item = (net.minecraft.item.Item) net.minecraft.item.Item.itemRegistry.getObject(itemId);
            if (item != null) {
                addItems(new ItemStack(item, itemsToGive));
            }
        }
        this.markDirty();
    }

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

        NBTTagList bufferList = new NBTTagList();
        for (Map.Entry<String, Double> entry : resourceBuffer.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("Item", entry.getKey());
            tag.setDouble("Amount", entry.getValue());
            bufferList.appendTag(tag);
        }
        nbt.setTag("Buffer", bufferList);
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

        this.resourceBuffer.clear();
        if (nbt.hasKey("Buffer")) {
            NBTTagList bufferList = nbt.getTagList("Buffer", 10);
            for (int i = 0; i < bufferList.tagCount(); i++) {
                NBTTagCompound tag = bufferList.getCompoundTagAt(i);
                this.resourceBuffer.put(tag.getString("Item"), tag.getDouble("Amount"));
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
    public void onDataPacket(net.minecraft.network.NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }
}