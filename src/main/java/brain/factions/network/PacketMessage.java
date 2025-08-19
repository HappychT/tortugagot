package brain.factions.network;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import brain.factions.Annot;
import brain.factions.servers.CoreFaction;
import brain.factions.servers.Faction;
import brain.factions.servers.FreeTeleporter;
import brain.factions.servers.Location;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import got.GOT;
import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import got.common.faction.GOTFaction;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.UsernameCache;

import static brain.factions.Annot.Side.SERVER;

public class PacketMessage implements IMessage {
	private static String message;

	public PacketMessage() {

	}

	public PacketMessage(String message) {
		this.message = message;
	}

	// CLIENT
	@Override
	public void fromBytes(ByteBuf buf) {
		message = ByteBufUtils.readUTF8String(buf);

	}

	// SERVER
	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, message);
	}

	@Annot(SERVER)
	public static EntityPlayerMP getPlayer(String name) {
		for(EntityPlayerMP player : (List<EntityPlayerMP>) MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
			if(player.getDisplayName().equals(name)) {
				return player;
			}
		}
		return null;
	}
	@Annot(SERVER)
	public static Faction getCurrentFaction(String name) {
		for(Faction fac : CoreFaction.factions.values()) {
			if(fac.getPlayers().containsKey(name)) {
				return fac;
			}
		}
		return null;
	}
	@Annot(SERVER)
	public static <K, V> Map<V, K> invertMap(Map<K, V> map) {
		return map.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (existing, replacement) -> existing));
	}
	@Annot(SERVER)
	public static boolean checkAplic(String name) {
		for(Faction fac : CoreFaction.factions.values()) {
			if(fac.getApplications().containsKey(name)) {
				return true;
			}
		}
		return false;
	}
	@Annot(SERVER)
	public static Faction getFaction(String id) {
		if (CoreFaction.factions.containsKey(id)) {
			return CoreFaction.factions.get(id);
		}
		return null;
	}


	public static class Handler implements IMessageHandler<PacketMessage, IMessage> {
		@Annot(SERVER)
		public IMessage onMessage(PacketMessage packet, MessageContext ctx) {
			String[] args = message.split("#");
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			CoreFaction.initFactions();

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
				int typeRove = Integer.parseInt(args[2]); // 1 повысить   -1 понизить 0 кикнуть
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
						if(faction.getAssistantName().equals(upprovePlayer)) {
							faction.setAssistantName("");
						}
						GOTPlayerData pd = GOTLevelData.getData(invertMap(UsernameCache.getMap()).get(upprovePlayer)); // get offline player
						pd.revokePledgeFaction(player, true);

						break;
					case 1:
						if(faction.getLeaderName().equals((upprovePlayer))) {
							player.addChatMessage(new ChatComponentText("§eВы не можете себя повысить!"));
							return null;
						}
						faction.setAssistantName(upprovePlayer);
						break;
					case -1:
						if (faction.getAssistantName().equals(upprovePlayer)) {
							faction.setAssistantName("");
						}

						break;
					default:
						break;
				}

				CoreFaction.saveFactions();
				CoreFaction.initFactions();
				CoreFaction.sendAllGui();
				return null;
			} else if(args[0].equalsIgnoreCase("applicat")) {
				if (faction.getLeaderName().equals(player.getDisplayName()) || faction.getAssistantName().equals(player.getDisplayName())) {

					String upprovePlayer = args[1];
					int typeRove = Integer.parseInt(args[2]); // 1 принять -1 отклонить
					if (!faction.getApplications().containsKey(upprovePlayer)) {
						return null;
					}

					switch (typeRove) {
						case 1:
							faction.getApplications().remove(upprovePlayer);
							faction.getPlayers().put(upprovePlayer, "");

							GOTPlayerData pd = GOTLevelData.getData(invertMap(UsernameCache.getMap()).get(upprovePlayer)); // get offline player																				// player
							GOTFaction fac = GOTFaction.forName(faction.getID());
							pd.setPledgeFaction(fac);
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
				FreeTeleporter.sendToDimensionWithoutPortal(player, faction.getHome().getWorldID(), faction.getHome().getX(), faction.getHome().getY(), faction.getHome().getZ());
				return null;
			} else if(args[0].equalsIgnoreCase("setPrefix")) {
				if (faction.getLeaderName().equals(player.getDisplayName()) || faction.getAssistantName().equals(player.getDisplayName())) {
					String name = args[1];
					if(faction.getPlayers().containsKey(name)) {
						faction.getPlayers().put(name, args[2]);
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