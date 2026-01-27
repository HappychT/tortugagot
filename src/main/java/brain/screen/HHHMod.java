package brain.screen;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = HHHMod.MODID, version = HHHMod.VERSION, name = "SecureScreenMod")
public class HHHMod {
    public static final String MODID = "screenmod";
    public static final String VERSION = "2.0";

    public static SimpleNetworkWrapper nw;

    @Mod.EventHandler
    public void a(FMLPreInitializationEvent e) {
        nw = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        nw.registerMessage(PacketSDS.Handler.class, PacketSDS.class, 0, Side.CLIENT);
        nw.registerMessage(PacketDSD.Handler.class, PacketDSD.class, 1, Side.SERVER);
    }

    @Mod.EventHandler
    public void b(FMLServerStartingEvent e) { // serverLoad -> b
        e.registerServerCommand(new CommandDDD());
    }
}