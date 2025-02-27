package brain.factions;

import java.util.Collections;
import java.util.List;

import brain.factions.network.PacketMessage;
import got.GOT;
import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import got.common.faction.GOTFaction;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class CheckPlayerCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "checkplayers";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "/checkplayers - GOT mod";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		GOT.coreFaction.checkPlayers();
		sender.addChatMessage(new ChatComponentText("§aУспешно!"));
		return;
	}
	
	
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender commandSender) {
      
        //Только опам или если в мире активны читы.
        return commandSender instanceof EntityPlayer ? MinecraftServer.getServer().getConfigurationManager().func_152596_g(((EntityPlayer) commandSender).getGameProfile()) : false;      
    }
}
