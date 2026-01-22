package brain.factions.servers;

import brain.factions.network.PacketFactionStructures;
import brain.factions.structures.FactionStructureManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class RaidCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "raid";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/raid <start|stop>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            return;
        }

        if (args[0].equalsIgnoreCase("start")) {
            StructureManager.isRaidTimeFortress = true;
            sender.addChatMessage(new ChatComponentText("§cРейд-тайм для крепостей начался!"));
            updateClients();
        } else if (args[0].equalsIgnoreCase("stop")) {
            StructureManager.isRaidTimeFortress = false;
            sender.addChatMessage(new ChatComponentText("§aРейд-тайм для крепостей окончен."));
            updateClients();
        } else {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
        }
    }

    private void updateClients() {
        List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
        PacketFactionStructures packet = new PacketFactionStructures(FactionStructureManager.structureSlots);
        for (EntityPlayerMP player : players) {
            CoreFaction.brainChannel.sendTo(packet, player);
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }
}