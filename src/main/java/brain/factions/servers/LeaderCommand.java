package brain.factions.servers;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import brain.factions.Faction;
import brain.factions.network.PacketMessage;
import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import got.common.faction.GOTFaction;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class LeaderCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "setleader";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "/setleader - GOT mod";
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		switch (args.length) {
			case 1:
				return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
			case 2:
				List<String> list = GOTFaction.getPlayableAlignmentFactionNames();
				return CommandBase.getListOfStringsMatchingLastWord(args, list.toArray(new String[0]));
		}
		return Collections.emptyList();
	}


	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(args.length >= 2) {
			CoreFaction.initFactions();
			String playerName = args[0];
			String factionID = args[1];
			brain.factions.Faction faction = PacketMessage.getFaction(factionID);

			if(faction == null) {
				sender.addChatMessage(new ChatComponentText("§eДанной фракции не существует!"));
				return;
			}
			if(checkLeader(playerName).length > 0) {
				sender.addChatMessage(new ChatComponentText("§e" + playerName + " уже является лидером в фракции " + checkLeader(playerName)[1]));
				return;
			}
			if(!faction.getApplications().containsKey(playerName) && !faction.getPlayers().containsKey(playerName)) {
				sender.addChatMessage(new ChatComponentText("§e" + playerName + " должен подать заявку чтобы вы назначили его лидером"));
				return;
			}

			faction.setLeaderName(playerName);
			if(faction.getApplications().containsKey(playerName)) {
				faction.getApplications().remove(playerName);
			}
			faction.getPlayers().put(playerName, new brain.factions.Faction.PlayerData("", System.currentTimeMillis(), "Лидер"));
			GOTPlayerData pd = GOTLevelData.getData((EntityPlayer) sender);
			GOTFaction fac = GOTFaction.forName(factionID);
			pd.setPledgeFaction(fac);

			CoreFaction.saveFactions();
			CoreFaction.initFactions();
			CoreFaction.sendAllGui();
			sender.addChatMessage(new ChatComponentText("§aУспешно!"));
			return;
		}
	}

	public Object[] checkLeader(String name) {
		for(brain.factions.Faction fac : CoreFaction.factions.values()) {
			if(fac.getLeaderName().equals(name)) {
				return new Object[] { true, fac.getID()};
			}
		}
		return new Object[] {};
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender commandSender) {
		return commandSender instanceof EntityPlayer ? MinecraftServer.getServer().getConfigurationManager().func_152596_g(((EntityPlayer) commandSender).getGameProfile()) : false;
	}
}