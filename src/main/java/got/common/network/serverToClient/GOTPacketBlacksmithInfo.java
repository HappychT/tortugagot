package got.common.network.serverToClient;

import java.io.IOException;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.simpleimpl.*;
import got.GOT;
import got.client.gui.GOTGuiFactionBlacksmith;
import got.common.faction.GOTContainerFactionBlacksmith;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public class GOTPacketBlacksmithInfo implements IMessage {
    public NBTTagCompound blacksmithData;

    public GOTPacketBlacksmithInfo() {
    }

    public GOTPacketBlacksmithInfo(NBTTagCompound nbt) {
        blacksmithData = nbt;
    }

    @Override
    public void fromBytes(ByteBuf data) {
        try {
            blacksmithData = new PacketBuffer(data).readNBTTagCompoundFromBuffer();
        } catch (IOException e) {
            FMLLog.severe("Error reading blacksmith data");
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf data) {
        try {
            new PacketBuffer(data).writeNBTTagCompoundToBuffer(blacksmithData);
        } catch (IOException e) {
            FMLLog.severe("Error writing blacksmith data");
            e.printStackTrace();
        }
    }

    public static class Handler implements IMessageHandler<GOTPacketBlacksmithInfo, IMessage> {
        @Override
        public IMessage onMessage(GOTPacketBlacksmithInfo packet, MessageContext context) {
            EntityPlayer entityplayer = GOT.proxy.getClientPlayer();
            Container container = entityplayer.openContainer;
            if (container instanceof GOTContainerFactionBlacksmith) {
                GOTContainerFactionBlacksmith containerBlacksmith = (GOTContainerFactionBlacksmith) container;
                containerBlacksmith.theBlacksmithNPC.receiveClientPacket(packet);
//                ((GOTGuiFactionBlacksmith) Minecraft.getMinecraft().currentScreen).updateState();
            }
            return null;
        }
    }

}
