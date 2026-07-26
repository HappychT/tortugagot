package brain.factions.network;

import brain.factions.Annot;
import brain.factions.Faction;
import brain.factions.servers.CollectionGoal;
import brain.factions.servers.CoreFaction;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import got.rome.ExtendedPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

public class PacketFactionCreateCollection implements IMessage {
    private String name;
    private long amount;

    public PacketFactionCreateCollection() {}

    public PacketFactionCreateCollection(String name, long amount) {
        this.name = name;
        this.amount = amount;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.name = ByteBufUtils.readUTF8String(buf);
        this.amount = buf.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.name);
        buf.writeLong(this.amount);
    }

    public static class Handler implements IMessageHandler<PacketFactionCreateCollection, IMessage> {
        @Override
        public IMessage onMessage(PacketFactionCreateCollection message, MessageContext ctx) {
            if (ctx.getServerHandler() != null && ctx.getServerHandler().playerEntity != null) {
                EntityPlayerMP player = ctx.getServerHandler().playerEntity;
                Faction faction = PacketMessage.getCurrentFaction(player.getCommandSenderName());

                if (faction != null) {
                    boolean hasPermission = faction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_MANAGE_TREASURY);
                    
                    ExtendedPlayer ext = ExtendedPlayer.get(player);
                    if (ext != null && ext.isTutorialActive() && ext.getTutorialStage() == 3) {
                        hasPermission = true;
                    }

                    if (hasPermission) {
                        CollectionGoal newGoal = new CollectionGoal(message.name, message.amount);
                        newGoal.logTransaction(player.getCommandSenderName(), 0);
                        faction.getCollectionGoals().add(newGoal);
                        CoreFaction.saveFactions();
                        CoreFaction.sendAllGui();
                        player.addChatMessage(new ChatComponentText("§aНовый сбор '" + message.name + "' успешно организован!"));
                    } else {
                        player.addChatMessage(new ChatComponentText("§cУ вас недостаточно прав для организации сбора."));
                    }
                }
                return null;
            }
            return null;
        }
    }
}