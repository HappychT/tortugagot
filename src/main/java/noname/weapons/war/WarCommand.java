package noname.weapons.war;

import brain.factions.servers.SiegeActivationManager;
import brain.factions.structures.FactionStructureManager;
import brain.factions.structures.FactionStructureSlot;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

public class WarCommand extends CommandBase {

    private static final int SIEGE_WAR_RADIUS = 500;

    @Override
    public String getCommandName() {
        return "war";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/war <on|off|\"название крепости\">";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "on", "off");
        }
        return null;
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
            boolean isActive = WarModeManager.getInstance().isWarModeActive();
            String status = isActive ? EnumChatFormatting.RED + "АКТИВНО" : EnumChatFormatting.GREEN + "ВЫКЛЮЧЕНО";
            sender.addChatMessage(new ChatComponentText(
                    EnumChatFormatting.GOLD + "Военное положение: " + status));
            return;
        }

        String action = args[0].toLowerCase();

        switch (action) {
            case "on":
                WarModeManager.getInstance().setWarMode(true);
                broadcastMessage(EnumChatFormatting.RED + "⚔ ВОЕННОЕ ПОЛОЖЕНИЕ ОБЪЯВЛЕНО! ⚔");
                broadcastMessage(EnumChatFormatting.YELLOW
                        + "Земля теперь может быть размещена в приватах при особых условиях.");
                broadcastMessage(EnumChatFormatting.YELLOW + "Игроки без военного билета будут исключены!");
                sender.addChatMessage(new ChatComponentText(
                        EnumChatFormatting.GREEN + "Военное положение включено."));
                break;

            case "off":
                WarModeManager.getInstance().setWarMode(false);
                SiegeActivationManager.getInstance().clearWarZones();
                broadcastMessage(EnumChatFormatting.GREEN + "☮ Военное положение снято. ☮");
                sender.addChatMessage(new ChatComponentText(
                        EnumChatFormatting.GREEN + "Военное положение выключено."));
                break;

            default:
                StringBuilder sb = new StringBuilder(args[0]);
                for (int i = 1; i < args.length; i++) sb.append(" ").append(args[i]);
                String fortressName = sb.toString().trim();
                FactionStructureSlot slot = FactionStructureManager.getFortressByName(fortressName);
                if (slot != null) {
                    int dim = getSenderDimension(sender);
                    SiegeActivationManager.getInstance().addWarZone(dim, slot.xCoord, slot.yCoord, slot.zCoord);
                    sender.addChatMessage(new ChatComponentText(
                            EnumChatFormatting.GREEN + "Осадные машины включены в радиусе " + SIEGE_WAR_RADIUS + " блоков от крепости \"" + slot.name + "\"."));
                } else {
                    sender.addChatMessage(new ChatComponentText(
                            EnumChatFormatting.RED + "Крепость не найдена: " + fortressName));
                }
                break;
        }
    }

    private void broadcastMessage(String message) {
        MinecraftServer server = MinecraftServer.getServer();
        if (server != null) {
            server.getConfigurationManager().sendChatMsg(new ChatComponentText(message));
        }
    }
}
