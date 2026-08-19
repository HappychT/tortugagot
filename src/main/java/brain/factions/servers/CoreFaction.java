package brain.factions.servers;

import brain.factions.Faction;
import brain.factions.arenas.ArenaManager;
import brain.factions.arenas.CommandSaveArena;
import brain.factions.client.PacketStructureGUISyncHandler;
import brain.factions.network.*;
import brain.factions.structures.FactionStructureManager;
import brain.factions.Annot;
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
import net.minecraft.entity.player.EntityPlayer;
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
	private static final Set<String> clearedPrefixCache = Collections.synchronizedSet(new HashSet<>());
	public static SimpleNetworkWrapper brainChannel = new SimpleNetworkWrapper("brainchannel");
	public static Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
	public static File configFolder;
	public static HashMap<String, Faction> factions;
	private static volatile boolean factionsInitialized = false;

	private static final Map<String, String> FACTION_NAMES_RU = new HashMap<>();
	static {
		FACTION_NAMES_RU.put("NORTH",         "Север");
		FACTION_NAMES_RU.put("WESTERLANDS",   "Западные земли");
		FACTION_NAMES_RU.put("RIVERLANDS",    "Речные земли");
		FACTION_NAMES_RU.put("STORMLANDS",    "Штормовые земли");
		FACTION_NAMES_RU.put("REACH",         "Простор");
		FACTION_NAMES_RU.put("DORNE",         "Дорн");
		FACTION_NAMES_RU.put("IRONBORN",      "Железные острова");
		FACTION_NAMES_RU.put("ARRYN",         "Долина Аррен");
		FACTION_NAMES_RU.put("DRAGONSTONE",   "Драконий Камень");
		FACTION_NAMES_RU.put("CROWNLANDS",    "Королевские земли");
		FACTION_NAMES_RU.put("NIGHT_WATCH",   "Ночной Дозор");
		FACTION_NAMES_RU.put("WILDLING",      "Одичалые");
		FACTION_NAMES_RU.put("HILL_TRIBES",   "Горные кланы");
		FACTION_NAMES_RU.put("WHITE_WALKER",  "Белые ходоки");
		FACTION_NAMES_RU.put("HIGH_POWER",    "Тортуга");
		FACTION_NAMES_RU.put("DOTHRAKI",      "Дотракийцы");
		FACTION_NAMES_RU.put("BRAAVOS",       "Браавос");
		FACTION_NAMES_RU.put("LYS",           "Лис");
		FACTION_NAMES_RU.put("MYR",           "Мир");
		FACTION_NAMES_RU.put("TYROSH",        "Тирош");
		FACTION_NAMES_RU.put("PENTOS",        "Пентос");
		FACTION_NAMES_RU.put("VOLANTIS",      "Волантис");
		FACTION_NAMES_RU.put("QOHOR",         "Квохор");
		FACTION_NAMES_RU.put("NORVOS",        "Норвос");
		FACTION_NAMES_RU.put("LORATH",        "Лорат");
		FACTION_NAMES_RU.put("ASSHAI",        "Асшай");
		FACTION_NAMES_RU.put("GHISCAR",       "Гискар");
		FACTION_NAMES_RU.put("QARTH",         "Кварт");
		FACTION_NAMES_RU.put("MOSSOVY",       "Моссовия");
		FACTION_NAMES_RU.put("IBBEN",         "Иббен");
		FACTION_NAMES_RU.put("LHAZAR",        "Лхазар");
		FACTION_NAMES_RU.put("SOTHORYOS",     "Соториос");
		FACTION_NAMES_RU.put("JOGOS",         "Джогос Нхай");
		FACTION_NAMES_RU.put("YI_TI",         "Йи Ти");
		FACTION_NAMES_RU.put("SUMMER_ISLANDS","Летние острова");
		FACTION_NAMES_RU.put("ULTHOS",        "Ультос");
		FACTION_NAMES_RU.put("BANDITS",       "Бандиты");
		FACTION_NAMES_RU.put("HOSTILE",       "Враждебная фракция");
		FACTION_NAMES_RU.put("UNALIGNED",     "Нейтральная фракция");
	}

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

		ArenaManager.instance.initConfig(event.getModConfigurationDirectory());
	}
	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);

		FMLCommonHandler.instance().bus().register(new TeleportHandler());
		MinecraftForge.EVENT_BUS.register(new TeleportHandler());

		FMLCommonHandler.instance().bus().register(new ServerTickHandler());
		FMLCommonHandler.instance().bus().register(new TimedEventsHandler());
		MinecraftForge.EVENT_BUS.register(new TimedEventsHandler());
		ItemToProvisionMap.init();
		if (event.getSide().isClient()) {
			MinecraftForge.EVENT_BUS.register(new brain.factions.client.HUDHandler());
		}
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new LeaderCommand());
		event.registerServerCommand(new CheckPlayerCommand());
		event.registerServerCommand(new SaveStructureCommand());
		event.registerServerCommand(new FactionAdminCommand());
		event.registerServerCommand(new RaidCommand());
		event.registerServerCommand(new CommandSaveArena());
		event.registerServerCommand(new FactionKickCommand());
		event.registerServerCommand(new WarCommand());

		CompletableFuture.runAsync(() -> {
			LOGGER.info("Loading Faction data asynchronously...");
			initFactions();
			checkPlayers();
			factionsInitialized = true;
			ServerTaskExecutor.addScheduledTask(() -> {
				if(MinecraftServer.getServer() != null) {
					for(Object obj : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
						if(obj instanceof EntityPlayer) {
							updatePrefix(((EntityPlayer)obj).getCommandSenderName());
						}
					}
				}
			});
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
	public static void updatePrefix(String playerName) {
		if (MinecraftServer.getServer() != null && MinecraftServer.getServer().getConfigurationManager() != null) {
			net.minecraft.entity.player.EntityPlayerMP targetPlayer = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playerName);
			if (targetPlayer != null) {
				got.rome.ExtendedPlayer ext = got.rome.ExtendedPlayer.get(targetPlayer);
				if (ext != null && ext.isTutorialActive()) {
					return;
				}
			}
		}

		Faction faction = PacketMessage.getCurrentFaction(playerName);
		String newPrefix = "";
		Faction.PlayerData pData = null;

		if (faction != null) {
			String color = faction.getColorTag() != null ? faction.getColorTag() : "";
			String titleText = "";

			if (faction.getLeaderName().equals(playerName)) {
				titleText = "Лидер";
				if (faction.getPlayers().containsKey(playerName)) {
					pData = faction.getPlayers().get(playerName);
				}
			} else {
				pData = faction.getPlayers().get(playerName);
				if (pData != null && pData.getTitle() != null && !pData.getTitle().isEmpty()
						&& !pData.getTitle().equals("Игрок")) {
					titleText = pData.getTitle();
				}
			}

			String factionName = FACTION_NAMES_RU.getOrDefault(faction.getID(), faction.getID());

			if (!titleText.isEmpty()) {
				newPrefix = color + "[" + factionName + "][" + titleText + "] ";
			} else {
				newPrefix = color + "[" + factionName + "] ";
			}
		}

		newPrefix = newPrefix.replace('§', '&');


		if (newPrefix.isEmpty()) {
			if (clearedPrefixCache.contains(playerName)) {
				return;
			}
			clearedPrefixCache.add(playerName);
		} else {
			clearedPrefixCache.remove(playerName);

			if (pData != null) {
				String currentStoredPrefix = pData.getPrefix();
				if (currentStoredPrefix == null) currentStoredPrefix = "";

				if (newPrefix.equals(currentStoredPrefix)) {
					return;
				}
				pData.setPrefix(newPrefix);
				saveFactions();
			}
		}

		String command;
		if (newPrefix.isEmpty()) {
			command = "lp user " + playerName + " meta clear prefix";
		} else {
			command = "lp user " + playerName + " meta setprefix \"" + newPrefix + "\"";
		}

		boolean executedViaBukkit = false;

		try {
			Class<?> bukkitClass = Class.forName("org.bukkit.Bukkit");
			Class<?> commandSenderClass = Class.forName("org.bukkit.command.CommandSender");

			Object server = bukkitClass.getMethod("getServer").invoke(null);
			Object consoleSender = server.getClass().getMethod("getConsoleSender").invoke(server);

			server.getClass()
					.getMethod("dispatchCommand", commandSenderClass, String.class)
					.invoke(server, consoleSender, command);

			executedViaBukkit = true;
		} catch (ClassNotFoundException ignored) {
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Failed to execute LuckPerms command via Bukkit", e);
		}

		if (!executedViaBukkit && MinecraftServer.getServer() != null) {
			MinecraftServer.getServer()
					.getCommandManager()
					.executeCommand(MinecraftServer.getServer(), command);
		}
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
		brainChannel.sendTo(new PacketFactionStructures(FactionStructureManager.structureSlots), player);
		updatePrefix(player.getCommandSenderName());
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
		PacketFactionStructures structPacket = new PacketFactionStructures(FactionStructureManager.structureSlots);

		for(EntityPlayerMP player : players) {
			brainChannel.sendTo(new PacketInfoFactions(), player);
			brainChannel.sendTo(structPacket, player);
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