package brain.factions.network;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import brain.factions.Faction;
import brain.factions.servers.*;
import brain.factions.structures.FactionStructureManager;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import got.common.faction.GOTFaction;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.UsernameCache;

public class PacketMessage implements IMessage {
	private String message;

	public PacketMessage() {

	}

	public PacketMessage(String message) {
		this.message = message;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		message = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, message);
	}

	public static EntityPlayerMP getPlayer(String name) {
		MinecraftServer server = MinecraftServer.getServer();
		if (server == null || server.getConfigurationManager() == null) return null;

		for(EntityPlayerMP player : (List<EntityPlayerMP>) server.getConfigurationManager().playerEntityList) {
			if(player.getDisplayName().equals(name)) {
				return player;
			}
		}
		return null;
	}

	public static Faction getCurrentFaction(String name) {
		Map<String, Faction> factionsMap = CoreFaction.factions;
		if (factionsMap == null) {
			factionsMap = PacketInfoFactions.getFactions();
		}

		if (factionsMap == null) return null;

		for(Faction fac : factionsMap.values()) {
			if(fac.getPlayers().containsKey(name)) {
				return fac;
			}
		}
		return null;
	}

	public static <K, V> Map<V, K> invertMap(Map<K, V> map) {
		return map.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (existing, replacement) -> existing));
	}

	public static boolean checkAplic(String name) {
		Map<String, Faction> factionsMap = CoreFaction.factions;
		if (factionsMap == null) factionsMap = PacketInfoFactions.getFactions();
		if (factionsMap == null) return false;

		for(Faction fac : factionsMap.values()) {
			if(fac.getApplications().containsKey(name)) {
				return true;
			}
		}
		return false;
	}

	public static Faction getFaction(String id) {
		Map<String, Faction> factionsMap = CoreFaction.factions;
		if (factionsMap == null) factionsMap = PacketInfoFactions.getFactions();

		if (factionsMap != null && factionsMap.containsKey(id)) {
			return factionsMap.get(id);
		}
		return null;
	}

	public static class Handler implements IMessageHandler<PacketMessage, IMessage> {
		public IMessage onMessage(PacketMessage packet, MessageContext ctx) {
			String[] args = packet.message.split("#");
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;

			if (brain.factions.Annot.SERVER) {
				CoreFaction.initFactions();
			}

			if (args[0].equalsIgnoreCase("requestStructures")) {
				CoreFaction.brainChannel.sendTo(new PacketFactionStructures(FactionStructureManager.structureSlots), player);
				return null;
			}
			else if (args[0].equalsIgnoreCase("requestBarracksPlayers")) {
				String factionID = args[1];
				List<BarracksManager.PlayerProfile> barracksPlayers = BarracksManager.getBarracksPlayers(factionID);
				brain.factions.servers.CoreFaction.brainChannel.sendTo(new PacketBarracksPlayers(barracksPlayers), player);
				return null;
			}
			if(args[0].equalsIgnoreCase("sendApplication")) {
				Faction faction = getFaction(args[1]);
				if(faction == null ) {
					return null;
				}
				if(faction.getPlayers().containsKey(player.getDisplayName()) || faction.getApplications().containsKey(player.getDisplayName())) {
					player.addChatMessage(new ChatComponentText("§eВы не можете еще раз кинуть заявку!"));
					return null;
				}

				if(checkAplic(player.getDisplayName())) {
					player.addChatMessage(new ChatComponentText("§eВы не можете еще раз кинуть заявку!"));
					return null;
				}
				if(faction.getMutePlayer().containsKey(player.getDisplayName())) {
					if(System.currentTimeMillis() >= faction.getMutePlayer().get(player.getDisplayName()) + TimeUnit.DAYS.toMillis(1) ) {
						faction.getMutePlayer().remove(player.getDisplayName());
					} else {
						player.addChatMessage(new ChatComponentText("§eВы не можете еще раз кинуть заявку! Кулдаун - 1 день"));
						return null;
					}
				}

				faction.getApplications().put(player.getDisplayName(), System.currentTimeMillis());
				CoreFaction.saveFactions();
				CoreFaction.initFactions();
				CoreFaction.sendAllGui();
				player.addChatMessage(new ChatComponentText("§aЗаявка успешно отправлена!"));
				if (getPlayer(faction.getLeaderName()) != null) {
					getPlayer(faction.getLeaderName()).addChatMessage(new ChatComponentText("§aУ ВАС НОВАЯ ЗАЯВКА НА ВСТУПЛЕНИЕ В ФРАКЦИЮ!"));
				} else if (getPlayer(faction.getAssistantName()) != null) {
					getPlayer(faction.getAssistantName()).addChatMessage(new ChatComponentText("§aУ ВАС НОВАЯ ЗАЯВКА НА ВСТУПЛЕНИЕ В ФРАКЦИЮ!"));
				}

				return null;
			} else if(args[0].equalsIgnoreCase("quet")) {
				Faction faction = getCurrentFaction(player.getDisplayName());
				if(faction == null ) {
					return null;
				}
				if(faction.getLeaderName().equals(player.getDisplayName())) {
					faction.setLeaderName(faction.getAssistantName());
				}
				faction.getPlayers().remove(player.getDisplayName());

				GOTPlayerData pd = GOTLevelData.getData(player);

				pd.revokePledgeFaction(player, true);
				CoreFaction.saveFactions();
				CoreFaction.initFactions();
				CoreFaction.sendAllGui();

				CoreFaction.updatePrefix(player.getCommandSenderName());

				return null;
			}

			Faction faction = getCurrentFaction(player.getDisplayName());
			if(faction == null ) {
				return null;
			}
			if(args[0].equalsIgnoreCase("editPlayer")) {
				if(!faction.getLeaderName().equals(player.getDisplayName())) {
					return null;
				}
				String upprovePlayer = args[1];
				int typeRove = Integer.parseInt(args[2]);
				if(!faction.getPlayers().containsKey(upprovePlayer)) {
					return null;
				}
				switch (typeRove) {
					case 0:
						if(faction.getLeaderName().equals((upprovePlayer))) {
							player.addChatMessage(new ChatComponentText("§eВы не можете себя кикнуть!"));
							return null;
						}
						faction.getPlayers().remove(upprovePlayer);
						CoreFaction.updatePrefix(upprovePlayer);
						if(faction.getAssistantName().equals(upprovePlayer)) {
							faction.setAssistantName("");
						}

						EntityPlayerMP targetPlayer = getPlayer(upprovePlayer);
						if (targetPlayer == null) targetPlayer = MinecraftServer.getServer().getConfigurationManager().func_152612_a(upprovePlayer);

						if (targetPlayer != null) {
							GOTPlayerData pd = GOTLevelData.getData(targetPlayer);
							if (pd != null) pd.revokePledgeFaction(targetPlayer, true);
						} else {
							java.util.UUID targetUUID = null;
							for (Map.Entry<java.util.UUID, String> entry : net.minecraftforge.common.UsernameCache.getMap().entrySet()) {
								if (upprovePlayer.contains(entry.getValue())) {
									targetUUID = entry.getKey();
									break;
								}
							}
							if (targetUUID != null) {
								GOTPlayerData pd = GOTLevelData.getData(targetUUID);
								if (pd != null) pd.revokePledgeFaction(null, false);
							}
						}
						break;
					case 1:
						if(faction.getLeaderName().equals((upprovePlayer))) {
							player.addChatMessage(new ChatComponentText("§eВы не можете себя повысить!"));
							return null;
						}
						faction.setAssistantName(upprovePlayer);
						CoreFaction.updatePrefix(upprovePlayer);
						break;
					case -1:
						if (faction.getAssistantName().equals(upprovePlayer)) {
							faction.setAssistantName("");
						}
						CoreFaction.updatePrefix(upprovePlayer);
						break;
					default:
						break;
				}

				CoreFaction.saveFactions();
				CoreFaction.initFactions();
				CoreFaction.sendAllGui();
				return null;
			} else if(args[0].equalsIgnoreCase("applicat")) {
				if (faction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_ACCEPT_APPLICATIONS)) {

					String upprovePlayer = args[1];
					int typeRove = Integer.parseInt(args[2]);
					if (!faction.getApplications().containsKey(upprovePlayer)) {
						return null;
					}

					switch (typeRove) {
						case 1:
							if (faction.isInWarState()) {
								long cost = StructureManager.config.warStateRecruitmentCost;
								if (faction.getTreasury() < cost) {
									player.addChatMessage(new ChatComponentText("§cНедостаточно средств в казне для принятия новобранца во время войны! Требуется: " + cost));
									return null;
								}
								faction.setTreasury(faction.getTreasury() - cost);
								player.addChatMessage(new ChatComponentText("§eСписан налог за вербовку: " + cost));
							}

							faction.getApplications().remove(upprovePlayer);
							faction.getPlayers().put(upprovePlayer, new Faction.PlayerData("", System.currentTimeMillis(), "Игрок"));
							CoreFaction.updatePrefix(upprovePlayer);
							GOTFaction fac = GOTFaction.forName(faction.getID());

							EntityPlayerMP acceptedPlayer = getPlayer(upprovePlayer);
							if (acceptedPlayer == null) acceptedPlayer = MinecraftServer.getServer().getConfigurationManager().func_152612_a(upprovePlayer);

							if (acceptedPlayer != null) {
								GOTPlayerData pd = GOTLevelData.getData(acceptedPlayer);
								if (pd != null) pd.setPledgeFaction(fac);
							} else {
								java.util.UUID targetUUID = null;
								for (Map.Entry<java.util.UUID, String> entry : net.minecraftforge.common.UsernameCache.getMap().entrySet()) {
									if (upprovePlayer.contains(entry.getValue())) {
										targetUUID = entry.getKey();
										break;
									}
								}
								if (targetUUID != null) {
									GOTPlayerData pd = GOTLevelData.getData(targetUUID);
									if (pd != null) pd.setPledgeFaction(fac);
								}
							}
							break;
						case -1:
							faction.getApplications().remove(upprovePlayer);
							faction.getMutePlayer().put(upprovePlayer, System.currentTimeMillis());
							break;
						default:
							break;
					}
					CoreFaction.saveFactions();
					CoreFaction.initFactions();
					CoreFaction.sendAllGui();
					return null;
				}
			} else if(args[0].equalsIgnoreCase("setHome")) {
				if (faction.getLeaderName().equals(player.getDisplayName()) || faction.getAssistantName().equals(player.getDisplayName())) {
					long cooldownRemaining = faction.getLastSetHomeTime() + TimeUnit.DAYS.toMillis(1) - System.currentTimeMillis();
					if (cooldownRemaining > 0) {
						long hours = TimeUnit.MILLISECONDS.toHours(cooldownRemaining);
						long minutes = TimeUnit.MILLISECONDS.toMinutes(cooldownRemaining) % 60;
						player.addChatMessage(new ChatComponentText("§aВы можете поставить точку дома только через " + hours + " ч. " + minutes + " мин.!"));
						return null;
					}

					faction.setHome(new Location(player.dimension, player.posX, player.posY, player.posZ));
					faction.setLastSetHomeTime(System.currentTimeMillis());
					CoreFaction.saveFactions();
					CoreFaction.initFactions();
					CoreFaction.sendAllGui();
					player.addChatMessage(new ChatComponentText("§aТочка дома для всей фракции установлена!"));
					return null;
				}
			} else if(args[0].equalsIgnoreCase("home")) {
				if(faction.getHome() == null) {
					player.addChatMessage(new ChatComponentText("§eРуководители пока не поставили точку дома!"));
					return null;
				}
				TeleportHandler.startTeleport(player, faction.getHome());
				return null;
			} else if(args[0].equalsIgnoreCase("setPrefix")) {
				if (faction.getLeaderName().equals(player.getDisplayName()) || faction.getAssistantName().equals(player.getDisplayName())) {
					String name = args[1];
					if(faction.getPlayers().containsKey(name)) {
						Faction.PlayerData pData = faction.getPlayers().get(name);
						pData.setPrefix(args[2]);
						faction.getPlayers().put(name, pData);
						CoreFaction.saveFactions();
						CoreFaction.initFactions();
						CoreFaction.sendAllGui();
					}
				}
			}

			return null;
		}

	}
}