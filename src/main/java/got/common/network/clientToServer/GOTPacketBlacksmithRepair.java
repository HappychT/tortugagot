package got.common.network.clientToServer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import got.common.faction.GOTContainerFactionBlacksmith;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class GOTPacketBlacksmithRepair implements IMessage {

    public GOTPacketBlacksmithRepair() {}

    @Override
    public void fromBytes(ByteBuf data) {}

    @Override
    public void toBytes(ByteBuf data) {}

    public static class Handler implements IMessageHandler<GOTPacketBlacksmithRepair, IMessage> {
        @Override
        public IMessage onMessage(GOTPacketBlacksmithRepair packet, MessageContext context) {
            EntityPlayer entityplayer = context.getServerHandler().playerEntity;
            Container container = entityplayer.openContainer;
            if (container instanceof GOTContainerFactionBlacksmith) {
                GOTContainerFactionBlacksmith containerBlacksmith = (GOTContainerFactionBlacksmith) container;
                containerBlacksmith.takeRepairItems();
                ItemStack itemCopy = containerBlacksmith.invInput.getStackInSlot(0).copy();
                itemCopy.setItemDamage(0);
                containerBlacksmith.invInput.setInventorySlotContents(0, itemCopy);
            }
            return null;
        }
    }
}

