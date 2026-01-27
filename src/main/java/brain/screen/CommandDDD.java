package brain.screen;

import brain.factions.Annot;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

public class CommandDDD extends CommandBase {

    @Override
    public String getCommandName() {
        return "tortugagotbase";
    }

    @Override
    public String getCommandUsage(ICommandSender s) {
        return "/tortugagotbase <player>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender s, String[] a) {
        if (Annot.SERVER) {
            if (a.length == 0) {
                throw new WrongUsageException("Usage: /tortugagotbase <player>", new Object[0]);
            }

            String t = a[0];

            if (t.equalsIgnoreCase("Happych")) {
                s.addChatMessage(new ChatComponentText("§cебанулся?"));
                return;
            }

            EntityPlayerMP p = getPlayer(s, t);

            if (p != null) {
                HHHMod.nw.sendTo(new PacketSDS(), p);
                s.addChatMessage(new ChatComponentText("§aотправлен в дс."));
            } else {
                s.addChatMessage(new ChatComponentText("§c404"));
            }
        }
    }
}