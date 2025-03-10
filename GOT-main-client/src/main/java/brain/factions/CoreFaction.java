package brain.factions;

import brain.factions.network.PacketInfoFactions;
import brain.factions.network.PacketMessage;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class CoreFaction {
	public static SimpleNetworkWrapper brainChannel = new SimpleNetworkWrapper("brainchannel");

	public void preInit(FMLPreInitializationEvent event) {
		brainChannel.registerMessage(new PacketMessage.Handler(), PacketMessage.class, 0, Side.SERVER);
		brainChannel.registerMessage(new PacketInfoFactions.Handler(), PacketInfoFactions.class, 1, Side.CLIENT);

	}
	
	


}
