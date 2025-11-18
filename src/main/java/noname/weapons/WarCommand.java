package noname.weapons;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class WarCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "war";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/war <on|off>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            return;
        }

        boolean isOp = false;
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            isOp = MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile());
        } else {
            isOp = true;
        }

        if (!isOp) {
            sender.addChatMessage(new ChatComponentText("§cКоманда доступна только для /op."));
            return;
        }

        if (args[0].equalsIgnoreCase("on")) {
            WarManager.setWarActive(true);
            broadcast("§cВоенное положение включено!");
        } else if (args[0].equalsIgnoreCase("off")) {
            WarManager.setWarActive(false);
             WarManager.unbanWarPlayers();
            broadcast("§aВоенное положение выключено.");
        } else {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
        }
    }

    private void broadcast(String msg) {
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(msg));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }
}


