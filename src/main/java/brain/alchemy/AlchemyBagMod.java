package brain.alchemy;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy; // Не забудьте импорт!
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = AlchemyBagMod.MODID, name = AlchemyBagMod.NAME, version = AlchemyBagMod.VERSION)
public class AlchemyBagMod {

    public static final String MODID = "alchemybag";
    public static final String NAME = "Alchemy Bag Mod";
    public static final String VERSION = "1.0";

    @Mod.Instance(MODID)
    public static AlchemyBagMod instance;

    @SidedProxy(clientSide = "brain.alchemy.ClientProxy", serverSide = "brain.alchemy.CommonProxy")
    public static CommonProxy proxy;

    public static SimpleNetworkWrapper network;

    public static Item alchemy_bag;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        network.registerMessage(PacketCyclePotion.Handler.class, PacketCyclePotion.class, 0, Side.SERVER);

        alchemy_bag = new ItemAlchemyBag().setUnlocalizedName("alchemy_bag")
                .setTextureName(MODID + ":alchemy_bag");
        GameRegistry.registerItem(alchemy_bag, "alchemy_bag");

        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        AlchemyBagEventHandler handler = new AlchemyBagEventHandler();
        FMLCommonHandler.instance().bus().register(handler);
        MinecraftForge.EVENT_BUS.register(handler);

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        proxy.init(event);
    }
}