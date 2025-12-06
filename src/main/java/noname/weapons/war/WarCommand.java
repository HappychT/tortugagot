package noname.weapons.war;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.List;

/**
 * Команда /war для управления военным режимом.
 * Доступна только для операторов сервера.
 */
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
    public int getRequiredPermissionLevel() {
        return 2; // Требуется OP
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "on", "off");
        }
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 1) {
            // Показать текущее состояние
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
                broadcastMessage(EnumChatFormatting.GREEN + "☮ Военное положение снято. ☮");
                sender.addChatMessage(new ChatComponentText(
                        EnumChatFormatting.GREEN + "Военное положение выключено."));
                break;

            default:
                sender.addChatMessage(new ChatComponentText(
                        EnumChatFormatting.RED + "Использование: " + getCommandUsage(sender)));
        }
    }

    private void broadcastMessage(String message) {
        MinecraftServer server = MinecraftServer.getServer();
        if (server != null) {
            server.getConfigurationManager().sendChatMsg(new ChatComponentText(message));
        }
    }
}
