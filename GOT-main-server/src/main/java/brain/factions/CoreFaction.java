package brain.factions;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import brain.factions.network.PacketInfoFactions;
import brain.factions.network.PacketMessage;
import cpw.mods.fml.common.FMLCommonHandler;
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
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.event.ServerChatEvent;

public class CoreFaction {
	public static Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
	public static File configFolder;

	public static HashMap<String, Faction> factions;
	public static SimpleNetworkWrapper brainChannel = new SimpleNetworkWrapper("brainchannel");

	public void preInit(FMLPreInitializationEvent event) {
		configFolder = new File(event.getModConfigurationDirectory(), "BrainFaction");
		if (!configFolder.exists()) {
			configFolder.mkdir();
		}
		brainChannel.registerMessage(new PacketMessage.Handler(), PacketMessage.class, 0, Side.SERVER);
		brainChannel.registerMessage(new PacketInfoFactions.Handler(), PacketInfoFactions.class, 1, Side.CLIENT);

	}

	public void init(FMLInitializationEvent event) {
		initFactions();
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}
	
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new LeaderCommand());
		event.registerServerCommand(new CheckPlayerCommand());
		checkPlayers();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					checkApplications();
					try {
						Thread.sleep(TimeUnit.HOURS.toMillis(1));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();
	}


	
	public static void checkPlayers() {
	    initFactions();

	    for (UUID player : UsernameCache.getMap().keySet()) {
	        GOTPlayerData data = GOTLevelData.getData(player);
	        if (data != null) {
	            String factionCodeName = data.getPledgeFaction() != null ? data.getPledgeFaction().codeName() : null;
	            if (factionCodeName != null && factions.containsKey(factionCodeName)) {
	                factions.get(factionCodeName).getPlayers().put(UsernameCache.getMap().get(player), getPrefix(factions.get(factionCodeName), UsernameCache.getMap().get(player)));
	            }
	        }
	    }

	    saveFactions();
	    initFactions();
	}
	
	public static String getPrefix(Faction faction, String player) {
		for(Map.Entry<String, String> fac: faction.getPlayers().entrySet()) {
			if(fac.getKey().equals(player)) {
				return fac.getValue();
			}
		}
		return "";
	}


	public static void checkApplications() {
		initFactions();
		for(Faction faction : factions.values()) {
			for(Iterator<Map.Entry<String, Long>> p = faction.getApplications().entrySet().iterator(); p.hasNext();) {
				 Map.Entry<String, Long> entry = p.next();
				 if(System.currentTimeMillis() >= entry.getValue() + TimeUnit.DAYS.toMillis(7)) {
					 p.remove();
				 }
			}
		}
		saveFactions();
		initFactions();
	}
	
	@SubscribeEvent
	public void join(PlayerLoggedInEvent e) {
		initFactions();
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

	public void sendAllGui() {
		List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for(EntityPlayerMP player : players) {
			brainChannel.sendTo(new PacketInfoFactions(), player);
		}
	}
	
	public boolean getMute(String playerName) {
		for(Faction f : factions.values()) {
			if(f.getMutePlayer().containsKey(playerName)) {
				return true;
			}
		}
		return false;
	}
	
	
	public static void initFactions() {
		File caseFile = new File(configFolder, "factions.json");
		if (!caseFile.exists()) {
			try {
				caseFile.createNewFile();
				factions = new HashMap<>();
				for (int i = 0; i < GOTFaction.values().length; i++) {
					String factionName = GOTFaction.values()[i].codeName();
					String[] colorTag = {"§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f"};
					factions.put(factionName, new Faction(factionName, "", "", new HashMap<>(), new HashMap<>(), new HashMap<>(), colorTag[new Random().nextInt(colorTag.length)], null));
				}
				saveFactions();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			factions = GSON.fromJson(Files.toString(caseFile, Charsets.UTF_8), new TypeToken<HashMap<String, Faction>>() {}.getType());
		} catch (JsonParseException ex) {
			logger().log(Level.WARNING, "Error while parsing factions.json, please check syntax", ex);
		} catch (IOException e) {
			logger().log(Level.WARNING, "Error while reading factions.json", e);
		}
	}
	
	public static void saveFactions() {
		try {
			Files.write(GSON.toJson(factions).getBytes(Charsets.UTF_8), new File(configFolder, "factions.json"));
		} catch (IOException e) {
			logger().log(Level.WARNING, "Error while saving factions.json", e);
		}
	}
	
    public static Logger logger() {
        return Logger.getLogger(CoreFaction.class.getName());
    }
	
		

}
