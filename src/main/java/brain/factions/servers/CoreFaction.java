package brain.factions.servers;

import brain.factions.Faction;
import brain.factions.client.PacketStructureGUISyncHandler;
import brain.factions.network.*;
import brain.factions.structures.FactionStructureManager;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import got.common.faction.GOTFaction;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.UsernameCache;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Mod(modid = "faction", name = "faction", acceptableRemoteVersions = "*", dependencies = "required-after:got")
public class CoreFaction {
	public static final String MODID = "faction";
	public static final Logger LOGGER = Logger.getLogger(MODID);

	public static SimpleNetworkWrapper brainChannel = new SimpleNetworkWrapper("brainchannel");
	public static Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
	public static File configFolder;
	public static HashMap<String, Faction> factions;
	private static volatile boolean factionsInitialized = false;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		configFolder = new File(event.getModConfigurationDirectory(), "BrainFaction");
		if (!configFolder.exists()) {
			configFolder.mkdir();
		}

		FactionStructureManager.init(configFolder);
		BarracksManager.init(configFolder);
		StructureManager.init(configFolder);

		brainChannel.registerMessage(PacketMessage.Handler.class, PacketMessage.class, 0, Side.SERVER);
		brainChannel.registerMessage(PacketInfoFactions.Handler.class, PacketInfoFactions.class, 1, Side.CLIENT);
		brainChannel.registerMessage(PacketFactionCreateCollection.Handler.class, PacketFactionCreateCollection.class, 2, Side.SERVER);
		brainChannel.registerMessage(PacketFactionManage.Handler.class, PacketFactionManage.class, 3, Side.SERVER);
		brainChannel.registerMessage(PacketFactionStructures.Handler.class, PacketFactionStructures.class, 4, Side.CLIENT);
		brainChannel.registerMessage(PacketBarracksPlayers.Handler.class, PacketBarracksPlayers.class, 5, Side.CLIENT);
		brainChannel.registerMessage(PacketStructureGUISyncHandler.class, PacketStructureGUISync.class, 6, Side.CLIENT);
		brainChannel.registerMessage(PacketStructureAction.Handler.class, PacketStructureAction.class, 7, Side.SERVER);
	}
	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		FMLCommonHandler.instance().bus().register(new TeleportHandler());
		FMLCommonHandler.instance().bus().register(new ServerTickHandler());
		FMLCommonHandler.instance().bus().register(new TimedEventsHandler());
		ItemToProvisionMap.init();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new LeaderCommand());
		event.registerServerCommand(new CheckPlayerCommand());
		event.registerServerCommand(new SaveStructureCommand());
		event.registerServerCommand(new FactionAdminCommand());
		event.registerServerCommand(new RaidCommand());

		CompletableFuture.runAsync(() -> {
			LOGGER.info("Loading Faction data asynchronously...");
			initFactions();
			checkPlayers();
			factionsInitialized = true;
			LOGGER.info("Faction data loaded.");
		}).exceptionally(e -> {
			LOGGER.log(Level.SEVERE, "Failed to load faction data asynchronously!", e);
			return null;
		});

		new Thread(() -> {
			while (true) {
				try {
					TimeUnit.MINUTES.sleep(30);
					if (factionsInitialized) {
						checkApplications();
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		}, "Faction Application Checker").start();
	}

	public static void checkPlayers() {
		if (!factionsInitialized) return;
		initFactions();
		for (UUID player : UsernameCache.getMap().keySet()) {
			GOTPlayerData data = GOTLevelData.getData(player);
			if (data != null) {
				String factionCodeName = data.getPledgeFaction() != null ? data.getPledgeFaction().codeName() : null;
				if (factionCodeName != null && factions.containsKey(factionCodeName)) {
					String playerName = UsernameCache.getMap().get(player);
					Faction.PlayerData pData = getPlayerData(factions.get(factionCodeName), playerName);
					factions.get(factionCodeName).getPlayers().put(playerName, pData);
				}
			}
		}
		saveFactions();
		initFactions();
	}

	public static Faction.PlayerData getPlayerData(Faction faction, String player) {
		for(Map.Entry<String, Faction.PlayerData> fac: faction.getPlayers().entrySet()) {
			if(fac.getKey().equals(player)) {
				return fac.getValue();
			}
		}
		return new Faction.PlayerData("", System.currentTimeMillis(), "Игрок");
	}

	public static void checkApplications() {
		if (!factionsInitialized) return;
		initFactions();
		for(Faction faction : factions.values()) {
			for(Iterator<Map.Entry<String, Long>> p = faction.getApplications().entrySet().iterator(); p.hasNext();) {
				Map.Entry<String, Long> entry = p.next();
				if(System.currentTimeMillis() >= entry.getValue() + TimeUnit.DAYS.toMillis(3)) {
					p.remove();
				}
			}
		}
		saveFactions();
		initFactions();
	}

	@SubscribeEvent
	public void join(PlayerLoggedInEvent e) {
		if (!factionsInitialized) {
			LOGGER.warning("Player " + e.player.getDisplayName() + " logged in before faction data was ready. They may need to relog to see faction info.");
			return;
		}

		EntityPlayerMP player = (EntityPlayerMP) e.player;
		brainChannel.sendTo(new PacketInfoFactions(), player);

		for(Faction faction : factions.values()) {
			if(faction.getLeaderName().equals(player.getDisplayName()) || faction.getAssistantName().equals(player.getDisplayName())) {
				if(faction.getApplications().size() > 0) {
					player.addChatMessage(new ChatComponentText("§aУ ВАС НОВЫЕ ЗАЯВКИ НА ВСТУПЛЕНИЕ В ФРАКЦИЮ!"));
					break;
				}
			}
		}
	}

	public static void sendAllGui() {
		if (!factionsInitialized) return;
		List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for(EntityPlayerMP player : players) {
			brainChannel.sendTo(new PacketInfoFactions(), player);
		}
	}

	public static synchronized void initFactions() {
		File caseFile = new File(configFolder, "factions.json");
		if (!caseFile.exists()) {
			try {
				caseFile.createNewFile();
				factions = new HashMap<>();
				for (int i = 0; i < GOTFaction.values().length; i++) {
					String factionName = GOTFaction.values()[i].codeName();
					String[] colorTag = {"§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f"};
					factions.put(factionName, new Faction(factionName, "", "", new HashMap<>(), new HashMap<>(), new HashMap<>(), colorTag[new Random().nextInt(colorTag.length)], null, 0, "Не указана", 0L, new HashMap<>()));
				}
				saveFactions();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			factions = GSON.fromJson(Files.toString(caseFile, Charsets.UTF_8), new TypeToken<HashMap<String, Faction>>() {}.getType());
		} catch (JsonParseException ex) {
			LOGGER.log(Level.WARNING, "Error while parsing factions.json, please check syntax", ex);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Error while reading factions.json", e);
		}

		if (factions == null) {
			LOGGER.log(Level.WARNING, "Factions map was null after loading, creating a new one.");
			factions = new HashMap<>();
		}
	}

	public static synchronized void saveFactions() {
		try {
			if (factions != null) {
				Files.write(GSON.toJson(factions).getBytes(Charsets.UTF_8), new File(configFolder, "factions.json"));
				BarracksManager.saveBarracksPlayers();
			}
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Error while saving factions.json", e);
		}
	}

	public static Logger logger() {
		return LOGGER;
	}
}