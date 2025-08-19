package brain.factions.servers;

import java.util.Collections;
import java.util.List;

import brain.factions.Annot;
import brain.factions.network.PacketMessage;
import cpw.mods.fml.relauncher.SideOnly;
import got.GOT;
import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import got.common.faction.GOTFaction;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;


@Annot(Annot.Side.SERVER)
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
		CoreFaction.checkPlayers();
		sender.addChatMessage(new ChatComponentText("§aУспешно!"));
		return;
	}
	
	
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender commandSender) {
      
        //Только опам или если в мире активны читы.
        return commandSender instanceof EntityPlayer ? MinecraftServer.getServer().getConfigurationManager().func_152596_g(((EntityPlayer) commandSender).getGameProfile()) : false;      
    }
}
