package noname.weapons.war;

import brain.factions.servers.SiegeActivationManager;
import brain.factions.servers.StructureManager;
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
        return "/war <название крепости|id>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
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
            WarModeManager warModeManager = WarModeManager.getInstance();
            if (warModeManager.isWarModeActive()) {
                warModeManager.setWarMode(false, null, -1);
                StructureManager.activeWarFortresses.clear();
                SiegeActivationManager.getInstance().clearWarZones();
                broadcastMessage(EnumChatFormatting.GREEN + "☮ Военное положение снято. ☮");
                sender.addChatMessage(new ChatComponentText(
                        EnumChatFormatting.GREEN + "Военное положение выключено."));
                return;
            }

            World w = sender.getEntityWorld();
            int sx = sender.getPlayerCoordinates().posX;
            int sy = sender.getPlayerCoordinates().posY;
            int sz = sender.getPlayerCoordinates().posZ;
            FactionStructureSlot nearby = FactionStructureManager.getStructureNearby(sx, sy, sz);
            if (nearby == null || nearby.category != FactionStructureSlot.StructureCategory.FORTRESS) {
                sender.addChatMessage(new ChatComponentText(
                        EnumChatFormatting.RED + "Поблизости нет крепости для включения военного режима."));
                return;
            }

            String slotKey = nearby.id != null ? nearby.id : nearby.name;
            StructureManager.activeWarFortresses.clear();
            if (nearby.id != null) {
                StructureManager.activeWarFortresses.add(nearby.id);
            }
            SiegeActivationManager.getInstance().clearWarZones();
            int dim = getSenderDimension(sender);
            SiegeActivationManager.getInstance().addWarZone(dim, nearby.xCoord, nearby.yCoord, nearby.zCoord);
            warModeManager.setWarMode(true, slotKey, dim);
            broadcastMessage(EnumChatFormatting.RED + "⚔ ВОЕННОЕ ПОЛОЖЕНИЕ ОБЪЯВЛЕНО! ⚔");
            broadcastMessage(EnumChatFormatting.YELLOW
                    + "Земля теперь может быть размещена в приватах при особых условиях.");
            broadcastMessage(EnumChatFormatting.YELLOW + "Игроки без военного билета будут исключены!");
            sender.addChatMessage(new ChatComponentText(
                    EnumChatFormatting.GREEN + "Осадные машины включены в радиусе " + SIEGE_WAR_RADIUS + " блоков от крепости \"" + nearby.name + "\"."));
            return;
        }

        StringBuilder sb = new StringBuilder(args[0]);
        for (int i = 1; i < args.length; i++) sb.append(" ").append(args[i]);
        String fortressName = sb.toString().trim();

        FactionStructureSlot slot = FactionStructureManager.getFortressByName(fortressName);
        if (slot == null) {
            sender.addChatMessage(new ChatComponentText(
                    EnumChatFormatting.RED + "Крепость не найдена: " + fortressName));
            return;
        }

        String slotKey = slot.id != null ? slot.id : slot.name;
        WarModeManager warModeManager = WarModeManager.getInstance();
        if (warModeManager.isWarModeActive() && slotKey != null && slotKey.equals(warModeManager.getActiveStructureId())) {
            warModeManager.setWarMode(false, null, -1);
            StructureManager.activeWarFortresses.remove(slot.id);
            SiegeActivationManager.getInstance().clearWarZones();
            broadcastMessage(EnumChatFormatting.GREEN + "☮ Военное положение снято. ☮");
            sender.addChatMessage(new ChatComponentText(
                    EnumChatFormatting.GREEN + "Военное положение выключено."));
            return;
        }

        int dim = getSenderDimension(sender);
        StructureManager.activeWarFortresses.clear();
        if (slot.id != null) {
            StructureManager.activeWarFortresses.add(slot.id);
        }
        SiegeActivationManager.getInstance().clearWarZones();
        SiegeActivationManager.getInstance().addWarZone(dim, slot.xCoord, slot.yCoord, slot.zCoord);
        warModeManager.setWarMode(true, slotKey, dim);
        broadcastMessage(EnumChatFormatting.RED + "⚔ ВОЕННОЕ ПОЛОЖЕНИЕ ОБЪЯВЛЕНО! ⚔");
        broadcastMessage(EnumChatFormatting.YELLOW
                + "Земля теперь может быть размещена в приватах при особых условиях.");
        broadcastMessage(EnumChatFormatting.YELLOW + "Игроки без военного билета будут исключены!");
        sender.addChatMessage(new ChatComponentText(
                EnumChatFormatting.GREEN + "Осадные машины включены в радиусе " + SIEGE_WAR_RADIUS + " блоков от крепости \"" + slot.name + "\"."));
    }

    private void broadcastMessage(String message) {
        MinecraftServer server = MinecraftServer.getServer();
        if (server != null) {
            server.getConfigurationManager().sendChatMsg(new ChatComponentText(message));
        }
    }
}
