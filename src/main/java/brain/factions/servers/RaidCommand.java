package brain.factions.servers;

import brain.factions.network.PacketFactionStructures;
import brain.factions.structures.FactionStructureManager;
import brain.factions.structures.FactionStructureSlot;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.List;

public class RaidCommand extends CommandBase {

    private static final int SIEGE_RAID_RADIUS = 250;

    @Override
    public String getCommandName() {
        return "raid";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/raid <start|stop|\"название точки\">";
    }

    private int getSenderDimension(ICommandSender sender) {
        try {
            World w = sender.getEntityWorld();
            return w != null ? w.provider.dimensionId : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            return;
        }

        if (args[0].equalsIgnoreCase("start")) {
            StructureManager.isRaidTimeFortress = true;
            sender.addChatMessage(new ChatComponentText("§cРейд-тайм для крепостей начался!"));
            updateClients();
        } else if (args[0].equalsIgnoreCase("stop")) {
            StructureManager.isRaidTimeFortress = false;
            SiegeActivationManager.getInstance().clearRaidZones();
            sender.addChatMessage(new ChatComponentText("§aРейд-тайм для крепостей окончен."));
            updateClients();
        } else {
            StringBuilder sb = new StringBuilder(args[0]);
            for (int i = 1; i < args.length; i++) sb.append(" ").append(args[i]);
            String pointName = sb.toString().trim();
            FactionStructureSlot slot = FactionStructureManager.getStructureByName(pointName);
            if (slot != null) {
                int dim = getSenderDimension(sender);
                SiegeActivationManager.getInstance().addRaidZone(dim, slot.xCoord, slot.yCoord, slot.zCoord);
                sender.addChatMessage(new ChatComponentText("§aОсадные машины включены в радиусе " + SIEGE_RAID_RADIUS + " блоков от точки \"" + slot.name + "\" (активны во время рейд-тайма)."));
            } else {
                sender.addChatMessage(new ChatComponentText("§cТочка не найдена: " + pointName));
            }
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