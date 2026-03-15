package brain.factions.servers;

import brain.factions.Faction;
import brain.factions.structures.FactionStructureManager;
import brain.factions.structures.FactionStructureSlot;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.Collections;
import java.util.List;

public class FactionAdminCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "factionadmin";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/factionadmin setmainfortress <factionID> <structureID>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            throw new WrongUsageException(getCommandUsage(sender));
        }

        if (args[0].equalsIgnoreCase("setmainfortress")) {
            if (args.length != 3) {
                throw new WrongUsageException("/factionadmin setmainfortress <factionID> <structureID>");
            }
            String factionID = args[1];
            String structureID = args[2];

            Faction faction = CoreFaction.factions.get(factionID);
            if (faction == null) {
                sender.addChatMessage(new ChatComponentText("§cфракция с айди '" + factionID + "' не найдена."));
                return;
            }

            FactionStructureSlot slot = FactionStructureManager.getStructureById(structureID);
            if (slot == null) {
                sender.addChatMessage(new ChatComponentText("§cструктура с ID '" + structureID + "' не найдена."));
                return;
            }

            if (slot.category != FactionStructureSlot.StructureCategory.FORTRESS) {
                sender.addChatMessage(new ChatComponentText("§cэта структура не крепость."));
                return;
            }

            if (!factionID.equals(slot.ownerFactionID)) {
                sender.addChatMessage(new ChatComponentText("§cона не пренадлежит фракции '" + factionID + "'."));
                return;
            }

            faction.setMainFortressId(structureID);
            CoreFaction.saveFactions();
            CoreFaction.sendAllGui();
            sender.addChatMessage(new ChatComponentText("§aуспех '" + structureID + "' терь основная для фракции '" + factionID + "'."));
        } else {
            throw new WrongUsageException(getCommandUsage(sender));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender commandSender) {
        return commandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }
}