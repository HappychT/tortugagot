package brain.factions.network;

import brain.factions.servers.BarracksManager;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import got.client.gui.faction.GuiStructureBlock;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class PacketBarracksPlayers implements IMessage {
    private List<BarracksManager.PlayerProfile> players;

    public PacketBarracksPlayers() {
        this.players = new ArrayList<>();
    }

    public PacketBarracksPlayers(List<BarracksManager.PlayerProfile> players) {
        this.players = players;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();
        this.players = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String uuid = ByteBufUtils.readUTF8String(buf);
            String name = ByteBufUtils.readUTF8String(buf);
            this.players.add(new BarracksManager.PlayerProfile(uuid, name));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.players.size());
        for (BarracksManager.PlayerProfile player : this.players) {
            ByteBufUtils.writeUTF8String(buf, player.getUuid());
            ByteBufUtils.writeUTF8String(buf, player.getName());
        }
    }

    public static class Handler implements IMessageHandler<PacketBarracksPlayers, IMessage> {
        @Override
        public IMessage onMessage(PacketBarracksPlayers message, MessageContext ctx) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiStructureBlock) {
                GuiStructureBlock gui = (GuiStructureBlock) Minecraft.getMinecraft().currentScreen;
                gui.receiveBarracksPlayers(message.players);
            }
            return null;
        }
    }
}