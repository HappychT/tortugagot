package brain.factions.servers;

import brain.factions.structures.FactionStructureManager;
import brain.factions.structures.FactionStructureSlot;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class WarCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "war-structure";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/war-structure <structure_id>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            return;
        }

        String targetId = args[0];
        FactionStructureSlot slot = FactionStructureManager.getStructureById(targetId);

        if (slot == null || slot.category != FactionStructureSlot.StructureCategory.FORTRESS) {
            sender.addChatMessage(new ChatComponentText("§cКрепость с таким ID не найдена."));
            return;
        }

        if (StructureManager.activeWarFortresses.contains(targetId)) {
            StructureManager.activeWarFortresses.remove(targetId);
            sender.addChatMessage(new ChatComponentText("§aВоенный режим для крепости " + slot.name + " отключен."));
        } else {
            StructureManager.activeWarFortresses.add(targetId);
            sender.addChatMessage(new ChatComponentText("§cВоенный режим для крепости " + slot.name + " включен! Сердце уязвимо в радиусе 500 блоков."));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }
}