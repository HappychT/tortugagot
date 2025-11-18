package brain.factions.servers;

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
        } else if (args[0].equalsIgnoreCase("stop")) {
            StructureManager.isRaidTimeFortress = false;
            sender.addChatMessage(new ChatComponentText("§aРейд-тайм для крепостей окончен."));
        } else {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }
}