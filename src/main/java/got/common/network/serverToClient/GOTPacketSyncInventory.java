package got.common.network.serverToClient;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import got.GOT;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

public class GOTPacketSyncInventory implements IMessage {
    private ItemStack[] mainInventory;
    private ItemStack itemstack;

    public GOTPacketSyncInventory() {}

    public GOTPacketSyncInventory(InventoryPlayer inventory) {
        this.mainInventory = Arrays.copyOf(inventory.mainInventory, inventory.mainInventory.length);
        this.itemstack = inventory.getItemStack();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();
        this.mainInventory = new ItemStack[size];
        for (int i = 0; i < size; i++)
            this.mainInventory[i] = ByteBufUtils.readItemStack(buf);
        itemstack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(mainInventory.length);
        for (ItemStack itemstack : mainInventory)
            ByteBufUtils.writeItemStack(buf, itemstack);
        ByteBufUtils.writeItemStack(buf, itemstack);
    }

    public static class Handler implements IMessageHandler<GOTPacketSyncInventory, IMessage> {
        @Override
        public IMessage onMessage(GOTPacketSyncInventory message, MessageContext ctx) {
            GOT.proxy.handleSyncInventory(message.mainInventory, message.itemstack);
            return null;
        }
    }
}