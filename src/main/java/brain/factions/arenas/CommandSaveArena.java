package brain.factions.arenas;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

public class CommandSaveArena extends CommandBase {

    @Override
    public String getCommandName() {
        return "savearena";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/savearena <name> <x1> <y1> <z1> <x2> <y2> <z2>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender instanceof EntityPlayerMP ?
                super.canCommandSenderUseCommand(sender) :
                true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 7) {
            throw new WrongUsageException(getCommandUsage(sender));
        }

        EntityPlayerMP player = null;
        if (sender instanceof EntityPlayerMP) {
            player = getCommandSenderAsPlayer(sender);
        }

        String name = args[0];

        int x1 = (int) CommandBase.func_110666_a(sender, player != null ? player.posX : 0, args[1]);
        int y1 = (int) CommandBase.func_110666_a(sender, player != null ? player.posY : 0, args[2]);
        int z1 = (int) CommandBase.func_110666_a(sender, player != null ? player.posZ : 0, args[3]);
        int x2 = (int) CommandBase.func_110666_a(sender, player != null ? player.posX : 0, args[4]);
        int y2 = (int) CommandBase.func_110666_a(sender, player != null ? player.posY : 0, args[5]);
        int z2 = (int) CommandBase.func_110666_a(sender, player != null ? player.posZ : 0, args[6]);

        int dimensionId;
        if (player != null) {
            dimensionId = player.dimension;
        } else {
            dimensionId = 0;
        }

        ArenaRegion region = new ArenaRegion(name, dimensionId, x1, y1, z1, x2, y2, z2);
        ArenaManager.instance.addAndSaveRegion(region);

        sender.addChatMessage(new ChatComponentText(name + "успешно сохранен"));
    }
}