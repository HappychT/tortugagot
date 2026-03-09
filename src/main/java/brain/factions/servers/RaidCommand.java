package brain.factions.servers;

import brain.factions.structures.FactionStructureManager;
import brain.factions.structures.FactionStructureSlot;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class RaidCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "raid";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/raid <start|stop|point_id>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            return;
        }

        if (args[0].equalsIgnoreCase("start")) {
            StructureManager.isRaidTimeFortress = true;
            StructureManager.isRaidTime = true;
            sender.addChatMessage(new ChatComponentText("§cГлобальный рейд-тайм начался!"));
        } else if (args[0].equalsIgnoreCase("stop")) {
            StructureManager.isRaidTimeFortress = false;
            StructureManager.isRaidTime = false;
            StructureManager.activeRaidPoints.clear();
            sender.addChatMessage(new ChatComponentText("§aГлобальный рейд-тайм окончен."));
        } else {
            String targetId = args[0];
            FactionStructureSlot slot = FactionStructureManager.getStructureById(targetId);

            if (slot == null || slot.category == FactionStructureSlot.StructureCategory.FORTRESS) {
                sender.addChatMessage(new ChatComponentText("§cРесурсная точка с таким ID не найдена."));
                return;
            }

            if (StructureManager.activeRaidPoints.contains(targetId)) {
                StructureManager.activeRaidPoints.remove(targetId);
                sender.addChatMessage(new ChatComponentText("§aРейд-тайм для точки " + slot.name + " отключен."));
            } else {
                StructureManager.activeRaidPoints.add(targetId);
                sender.addChatMessage(new ChatComponentText("§cРейд-тайм для точки " + slot.name + " включен! Уязвимость в радиусе 250 блоков."));
            }
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }
}