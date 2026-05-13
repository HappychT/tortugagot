package brain.factions.servers;

import brain.factions.Faction;
import brain.factions.network.PacketMessage;
import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class FactionKickCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "factionkick";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/factionkick <player>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 1) {
            throw new WrongUsageException(getCommandUsage(sender));
        }

        String targetName = args[0];

        Faction faction = PacketMessage.getCurrentFaction(targetName);

        if (faction == null) {
            sender.addChatMessage(new ChatComponentText("§cИгрок '" + targetName + "' не состоит ни в одной фракции."));
            return;
        }

        if (faction.getLeaderName().equals(targetName)) {
            if (faction.getAssistantName() != null && !faction.getAssistantName().isEmpty()) {
                faction.setLeaderName(faction.getAssistantName());
                faction.setAssistantName("");
                sender.addChatMessage(new ChatComponentText("§eВы кикнули лидера, и новым лидером назначен: " + faction.getLeaderName()));
            } else {
                faction.setLeaderName("");
                sender.addChatMessage(new ChatComponentText("§cВы кикнули лидера, и у фракции нет заместителя."));
            }
        } else if (faction.getAssistantName().equals(targetName)) {
            faction.setAssistantName("");
        }

        faction.getPlayers().remove(targetName);

        EntityPlayerMP targetPlayer = MinecraftServer.getServer().getConfigurationManager().func_152612_a(targetName);
        
        if (targetPlayer != null) {
            GOTPlayerData pd = GOTLevelData.getData(targetPlayer);
            if (pd != null) {
                pd.revokePledgeFaction(targetPlayer, true);
            }
            targetPlayer.addChatMessage(new ChatComponentText("§cВы были исключены из фракции."));
            CoreFaction.updatePrefix(targetPlayer.getCommandSenderName());
        } else {
            CoreFaction.updatePrefix(targetName);

            java.util.UUID targetUUID = brain.factions.network.PacketMessage.invertMap(net.minecraftforge.common.UsernameCache.getMap()).get(targetName);
            if (targetUUID != null) {
                GOTPlayerData pd = GOTLevelData.getData(targetUUID);
                if (pd != null) pd.revokePledgeFaction(null, false);
            }

            sender.addChatMessage(new ChatComponentText("§eИгрок оффлайн, но он удален из фракции."));
        }

        CoreFaction.saveFactions();
        CoreFaction.sendAllGui();

        sender.addChatMessage(new ChatComponentText("§aИгрок " + targetName + " успешно изгнан из фракции " + faction.getID()));
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}