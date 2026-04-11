package got.client;

import java.util.*;
import java.util.Map.Entry;

import got.client.handlers.ClientEventHandler;
import got.client.render.item.GOTItemShieldRenderer;
import got.common.database.GOTRegistry;
import got.common.entity.other.*;
import net.minecraft.client.renderer.entity.RenderSnowball;
import noname.weapons.entity.*;
import noname.weapons.events.OverlayEventHandler;
import noname.weapons.render.*;
import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import got.client.ROMEMusic.MusicEventHandler;
import got.client.effect.GOTEffectRenderer;
import got.client.effect.GOTEntityAlignmentBonus;
import got.client.effect.GOTEntityAngryFX;
import got.client.effect.GOTEntityAsshaiTorchFX;
import got.client.effect.GOTEntityChillFX;
import got.client.effect.GOTEntityLargeBlockFX;
import got.client.effect.GOTEntityLeafFX;
import got.client.effect.GOTEntityMarshFlameFX;
import got.client.effect.GOTEntityMarshLightFX;
import got.client.effect.GOTEntityPickpocketFX;
import got.client.effect.GOTEntityPickpocketFailFX;
import got.client.effect.GOTEntityRiverWaterFX;
import got.client.effect.GOTEntityWaveFX;
import got.client.effect.GOTEntityWhiteSmokeFX;
import got.client.gui.GOTGuiBanner;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.GOTGuiFastTravel;
import got.client.gui.GOTGuiFellowships;
import got.client.gui.GOTGuiHiredFarmer;
import got.client.gui.GOTGuiHiredWarrior;
import got.client.gui.GOTGuiMap;
import got.client.gui.GOTGuiMessage;
import got.client.gui.GOTGuiMiniquestOffer;
import got.client.handlers.GOTBlockClientHandler;
import got.client.handlers.GOTClientStaminaHandler;
import got.client.handlers.GOTInterfaceHandler;
import got.client.render.DecorationRenderer;
import got.client.render.GOTRender;
import got.client.render.other.GOTRenderAnimalJar;
import got.client.render.other.GOTRenderArmorStand;
import got.client.render.other.GOTRenderBeacon;
import got.client.render.other.GOTRenderBlocks;
import got.client.render.other.GOTRenderChest;
import got.client.render.other.GOTRenderCommandTable;
import got.client.render.other.GOTRenderDartTrap;
import got.client.render.other.GOTRenderKebabStand;
import got.client.render.other.GOTRenderMug;
import got.client.render.other.GOTRenderPlateFood;
import got.client.render.other.GOTRenderPlayer;
import got.client.render.other.GOTRenderSignCarved;
import got.client.render.other.GOTRenderSignCarvedValyrian;
import got.client.render.other.GOTRenderSpawnerChest;
import got.client.render.other.GOTRenderUnsmeltery;
import got.client.render.other.GOTRenderWeaponRack;
import got.client.render.other.GOTSwingHandler;
import got.client.sound.GOTMusic;
import got.common.GOTCommonProxy;
import got.common.GOTDimension;
import got.common.GOTGuiMessageTypes;
import got.common.GOTTickHandlerServer;
import got.common.block.base.DecorationTileEntity;
import got.common.database.GOTAchievement;
import got.common.database.GOTArmorModels;
import got.common.decorations.DecorationsRegister;
import got.common.decorations.base.Decoration;
import got.common.entity.animal.GOTEntityElephant3DViewer;
import got.common.entity.animal.GOTEntityMammoth3DViewer;
import got.common.entity.dragon.GOTEntityDragon3DViewer;
import noname.weapons.entity.EntityBatteringRam3DViewer;
import got.common.faction.GOTAlignmentBonusMap;
import got.common.faction.GOTFaction;
import got.common.network.GOTPacketClientInfo;
import got.common.network.GOTPacketFellowshipAcceptInviteResult;
import got.common.network.GOTPacketHandler;
import got.common.network.GOTPacketMenuPrompt;
import got.common.quest.GOTMiniQuest;
import got.common.tileentity.GOTTileEntityAnimalJar;
import got.common.tileentity.GOTTileEntityArmorStand;
import got.common.tileentity.GOTTileEntityBeacon;
import got.common.tileentity.GOTTileEntityChest;
import got.common.tileentity.GOTTileEntityCommandTable;
import got.common.tileentity.GOTTileEntityKebabStand;
import got.common.tileentity.GOTTileEntityMug;
import got.common.tileentity.GOTTileEntityPlate;
import got.common.tileentity.GOTTileEntitySarbacaneTrap;
import got.common.tileentity.GOTTileEntitySignCarved;
import got.common.tileentity.GOTTileEntitySignCarvedValyrian;
import got.common.tileentity.GOTTileEntitySpawnerChest;
import got.common.tileentity.GOTTileEntityUnsmeltery;
import got.common.tileentity.GOTTileEntityWeaponRack;
import got.common.util.GOTFunctions;
import got.common.world.biome.GOTBiome;
import got.common.world.map.GOTAbstractWaypoint;
import got.common.world.map.GOTConquestZone;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import software.bernie.geckolib3.renderers.geo.RenderBlockItem;

public class GOTClientProxy extends GOTCommonProxy {
    public static ResourceLocation enchantmentTexture = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    public static ResourceLocation alignmentTexture = new ResourceLocation("got:textures/gui/alignment.png");
    public static ResourceLocation particlesTexture = new ResourceLocation("got:textures/misc/particles.png");
    public static ResourceLocation particles2Texture = new ResourceLocation("got:textures/misc/particles2.png");
    public static ResourceLocation customPotionsTexture = new ResourceLocation("got:textures/gui/effects.png");
    public static GOTEffectRenderer customEffectRenderer;
    public static GOTRenderPlayer specialPlayerRenderer = new GOTRenderPlayer();
    public static GOTSwingHandler swingHandler = new GOTSwingHandler();
    public static GOTAutoRespawnHandler autoRespawnHandler = new GOTAutoRespawnHandler();
    public static GOTTickHandlerClient tickHandler = new GOTTickHandlerClient();
    public static GOTGuiHandler guiHandler = new GOTGuiHandler();
    public static GOTMusic musicHandler;
    public static int TESSELLATOR_MAX_BRIGHTNESS = 15728880;
    public static int FONTRENDERER_ALPHA_MIN = 4;
    public int beaconRenderID;
    public int barrelRenderID;
    public int bombRenderID;
    public int doubleTorchRenderID;
    public int plateRenderID;
    public int stalactiteRenderID;
    public int flowerPotRenderID;
    public int cloverRenderID;
    public int plantainRenderID;
    public int fenceRenderID;
    public int grassRenderID;
    public int fallenLeavesRenderID;
    public int leavesRenderID;
    public int commandTableRenderID;
    public int butterflyJarRenderID;
    public int unsmelteryRenderID;
    public int chestRenderID;
    public int reedsRenderID;
    public int wasteRenderID;
    public int beamRenderID;
    public int vCauldronRenderID;
    public int grapevineRenderID;
    public int thatchFloorRenderID;
    public int treasureRenderID;
    public int flowerRenderID;
    public int doublePlantRenderID;
    public int birdCageRenderID;
    public int wildFireJarRenderID;
    public int coralRenderID;
    public int doorRenderID;
    public int ropeRenderID;
    public int chainRenderID;
    public int trapdoorRenderID;

    public static final Map<Item, Integer> weaponHitCounts = new HashMap<>();

    @Override
    public void addMapPlayerLocation(GameProfile player, double posX, double posZ) {
        GOTGuiMap.addPlayerLocationInfo(player, posX, posZ);
    }

    @Override
    public void cancelItemHighlight() {
        tickHandler.cancelItemHighlight = true;
    }

    @Override
    public void clearMapPlayerLocations() {
        GOTGuiMap.clearPlayerLocations();
    }

    @Override
    public void clientReceiveSpeech(GOTEntityNPC npc, String speech) {
        GOTSpeechClient.receiveSpeech(npc, speech);
    }

    @Override
    public void displayAlignmentSee(String username, String factionName, Map<GOTFaction, Float> alignments) {
        GOTFaction faction = GOTFaction.forName(factionName);

        if (faction != null) {
            GOTGuiFactions gui = new GOTGuiFactions();

            gui.setOtherPlayer(faction);

            Minecraft mc = Minecraft.getMinecraft();
            mc.displayGuiScreen(gui);
        }
    }

    @Override
    public void displayBannerGui(GOTEntityBanner banner) {
        Minecraft mc = Minecraft.getMinecraft();
        GOTGuiBanner gui = new GOTGuiBanner(banner);
        mc.displayGuiScreen(gui);
    }

    @Override
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
    }

    @Override
    public void displayFellowshipAcceptInvitationResult(UUID fellowshipID, String name, GOTPacketFellowshipAcceptInviteResult.AcceptInviteResult result) {
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen gui = mc.currentScreen;
        if (gui instanceof GOTGuiFellowships) {
            ((GOTGuiFellowships) gui).displayAcceptInvitationResult(fellowshipID, name, result);
        }
    }

    @Override
    public void displayFTScreen(GOTAbstractWaypoint waypoint, int startX, int startZ) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.displayGuiScreen(new GOTGuiFastTravel(waypoint, startX, startZ));
    }

    @Override
    public void displayMenuPrompt(GOTPacketMenuPrompt.Type type) {
        if (type == GOTPacketMenuPrompt.Type.MENU) {
            GOTTickHandlerClient.renderMenuPrompt = true;
        }
    }

    @Override
    public void displayMessage(GOTGuiMessageTypes message) {
        Minecraft.getMinecraft().displayGuiScreen(new GOTGuiMessage(message));
    }

    @Override
    public void displayMiniquestOffer(GOTMiniQuest quest, GOTEntityNPC npc) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.displayGuiScreen(new GOTGuiMiniquestOffer(quest, npc));
    }

    @Override
    public void displayNewDate() {
        tickHandler.updateDate();
    }

    @Override
    public void fillMugFromCauldron(World world, int i, int j, int k, int side, ItemStack itemstack) {
        if (!world.isRemote) {
            super.fillMugFromCauldron(world, i, j, k, side, itemstack);
        } else {
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(i, j, k, side, itemstack, 0.0F, 0.0F, 0.0F));
        }
    }

    @Override
    public int getBarrelRenderID() {
        return this.barrelRenderID;
    }

    @Override
    public int getBeaconRenderID() {
        return this.beaconRenderID;
    }

    @Override
    public int getBeamRenderID() {
        return this.beamRenderID;
    }

    @Override
    public int getBirdCageRenderID() {
        return this.birdCageRenderID;
    }

    @Override
    public int getBombRenderID() {
        return this.bombRenderID;
    }

    @Override
    public int getButterflyJarRenderID() {
        return this.butterflyJarRenderID;
    }

    @Override
    public int getChainRenderID() {
        return this.chainRenderID;
    }

    @Override
    public int getChestRenderID() {
        return this.chestRenderID;
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getMinecraft().theWorld;
    }

    @Override
    public int getCloverRenderID() {
        return this.cloverRenderID;
    }

    @Override
    public int getCommandTableRenderID() {
        return this.commandTableRenderID;
    }

    @Override
    public int getCoralRenderID() {
        return this.coralRenderID;
    }

    @Override
    public int getDoorRenderID() {
        return this.doorRenderID;
    }

    @Override
    public int getDoublePlantRenderID() {
        return this.doublePlantRenderID;
    }

    @Override
    public int getDoubleTorchRenderID() {
        return this.doubleTorchRenderID;
    }

    @Override
    public int getFallenLeavesRenderID() {
        return this.fallenLeavesRenderID;
    }

    @Override
    public int getFenceRenderID() {
        return this.fenceRenderID;
    }

    @Override
    public int getFlowerPotRenderID() {
        return this.flowerPotRenderID;
    }

    @Override
    public int getFlowerRenderID() {
        return this.flowerRenderID;
    }

    @Override
    public int getGrapevineRenderID() {
        return this.grapevineRenderID;
    }

    @Override
    public int getGrassRenderID() {
        return this.grassRenderID;
    }

    @Override
    public int getLeavesRenderID() {
        return this.leavesRenderID;
    }

    @Override
    public int getPlantainRenderID() {
        return this.plantainRenderID;
    }

    @Override
    public int getPlateRenderID() {
        return this.plateRenderID;
    }

    @Override
    public int getReedsRenderID() {
        return this.reedsRenderID;
    }

    @Override
    public int getRopeRenderID() {
        return this.ropeRenderID;
    }

    @Override
    public int getStalactiteRenderID() {
        return this.stalactiteRenderID;
    }

    @Override
    public int getThatchFloorRenderID() {
        return this.thatchFloorRenderID;
    }

    @Override
    public int getTrapdoorRenderID() {
        return this.trapdoorRenderID;
    }

    @Override
    public int getTreasureRenderID() {
        return this.treasureRenderID;
    }

    @Override
    public int getUnsmelteryRenderID() {
        return this.unsmelteryRenderID;
    }

    @Override
    public int getVCauldronRenderID() {
        return this.vCauldronRenderID;
    }

    @Override
    public int getWasteRenderID() {
        return this.wasteRenderID;
    }

    @Override
    public int getWildFireJarRenderID() {
        return this.wildFireJarRenderID;
    }

    @Override
    public void handleInvasionWatch(int invasionEntityID, boolean overrideAlreadyWatched) {
        GOTInvasionStatus status = GOTTickHandlerClient.watchedInvasion;
        if (overrideAlreadyWatched || !status.isActive()) {
            World world = getClientWorld();
            Entity e = world.getEntityByID(invasionEntityID);
            if (e instanceof GOTEntityInvasionSpawner) {
                status.setWatchedInvasion((GOTEntityInvasionSpawner) e);
            }
        }
    }

    @Override
    public boolean isClient() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
    }

    @Override
    public boolean isPaused() {
        return Minecraft.getMinecraft().isGamePaused();
    }

    @Override
    public boolean isSingleplayer() {
        return Minecraft.getMinecraft().isSingleplayer();
    }

    @Override
    public void onLoad() {
        customEffectRenderer = new GOTEffectRenderer(Minecraft.getMinecraft());
        GOTTextures.onInit();
        GOTRender.onInit();
        for (Entry<Class<? extends Entity>, Render> cl : GOTRender.renders.entrySet()) {
            RenderingRegistry.registerEntityRenderingHandler(cl.getKey(), cl.getValue());
        }
        this.beaconRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.barrelRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.bombRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.doubleTorchRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.plateRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.stalactiteRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.flowerPotRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.cloverRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.fenceRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.grassRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.fallenLeavesRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.commandTableRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.butterflyJarRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.unsmelteryRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.chestRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.reedsRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.wasteRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.beamRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.vCauldronRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.grapevineRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.thatchFloorRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.treasureRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.flowerRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.doublePlantRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.birdCageRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.wildFireJarRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.coralRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.doorRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.ropeRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.chainRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.trapdoorRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.plantainRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.leavesRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(this.plantainRenderID, new GOTRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(this.leavesRenderID, new GOTRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(this.beaconRenderID, new GOTRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(this.barrelRenderID, new GOTRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(this.bombRenderID, new GOTRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(this.doubleTorchRenderID, new GOTRenderBlocks(false));
        RenderingRegistry.registerBlockHandler(this.plateRenderID, new GOTRenderBlocks(false));
        RenderingRegistry.registerBlockHandler(this.stalactiteRenderID, new GOTRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(this.flowerPotRenderID, new GOTRenderBlocks(false));
        RenderingRegistry.registerBlockHandler(this.cloverRenderID, new GOTRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(this.fenceRenderID, new GOTRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(this.grassRenderID, new GOTRenderBlocks(false));
        RenderingRegistry.registerBlockHandler(this.fallenLeavesRenderID, new GOTRenderBlocks(false));
        RenderingRegistry.registerBlockHandler(this.commandTableRenderID, new GOTRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(this.butterflyJarRenderID, new GOTRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(this.unsmelteryRenderID, new GOTRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(this.chestRenderID, new GOTRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(this.reedsRenderID, new GOTRenderBlocks(false));
        RenderingRegistry.registerBlockHandler(this.wasteRenderID, new GOTRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(this.beamRenderID, new GOTRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(this.vCauldronRenderID, new GOTRenderBlocks(false));
        RenderingRegistry.registerBlockHandler(this.grapevineRenderID, new GOTRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(this.thatchFloorRenderID, new GOTRenderBlocks(false));
        RenderingRegistry.registerBlockHandler(this.treasureRenderID, new GOTRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(this.flowerRenderID, new GOTRenderBlocks(false));
        RenderingRegistry.registerBlockHandler(this.doublePlantRenderID, new GOTRenderBlocks(false));
        RenderingRegistry.registerBlockHandler(this.birdCageRenderID, new GOTRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(this.wildFireJarRenderID, new GOTRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(this.coralRenderID, new GOTRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(this.doorRenderID, new GOTRenderBlocks(false));
        RenderingRegistry.registerBlockHandler(this.ropeRenderID, new GOTRenderBlocks(false));
        RenderingRegistry.registerBlockHandler(this.chainRenderID, new GOTRenderBlocks(false));
        RenderingRegistry.registerBlockHandler(this.trapdoorRenderID, new GOTRenderBlocks(true));
        ClientRegistry.bindTileEntitySpecialRenderer(GOTTileEntityBeacon.class, new GOTRenderBeacon());
        ClientRegistry.bindTileEntitySpecialRenderer(GOTTileEntityPlate.class, new GOTRenderPlateFood());
        ClientRegistry.bindTileEntitySpecialRenderer(GOTTileEntitySpawnerChest.class, new GOTRenderSpawnerChest());
        ClientRegistry.bindTileEntitySpecialRenderer(GOTTileEntityArmorStand.class, new GOTRenderArmorStand());
        ClientRegistry.bindTileEntitySpecialRenderer(GOTTileEntityMug.class, new GOTRenderMug());
        ClientRegistry.bindTileEntitySpecialRenderer(GOTTileEntityCommandTable.class, new GOTRenderCommandTable());
        ClientRegistry.bindTileEntitySpecialRenderer(GOTTileEntityAnimalJar.class, new GOTRenderAnimalJar());
        ClientRegistry.bindTileEntitySpecialRenderer(GOTTileEntityUnsmeltery.class, new GOTRenderUnsmeltery());
        ClientRegistry.bindTileEntitySpecialRenderer(GOTTileEntitySarbacaneTrap.class, new GOTRenderDartTrap());
        ClientRegistry.bindTileEntitySpecialRenderer(GOTTileEntityChest.class, new GOTRenderChest());
        ClientRegistry.bindTileEntitySpecialRenderer(GOTTileEntityWeaponRack.class, new GOTRenderWeaponRack());
        ClientRegistry.bindTileEntitySpecialRenderer(GOTTileEntityKebabStand.class, new GOTRenderKebabStand());
        ClientRegistry.bindTileEntitySpecialRenderer(GOTTileEntitySignCarved.class, new GOTRenderSignCarved());
        ClientRegistry.bindTileEntitySpecialRenderer(GOTTileEntitySignCarvedValyrian.class, new GOTRenderSignCarvedValyrian());
         ClientRegistry.bindTileEntitySpecialRenderer(DecorationTileEntity.class, new DecorationRenderer());
        RenderingRegistry.registerEntityRenderingHandler(EntityCatapult.class, new RenderCatapult());
        RenderingRegistry.registerEntityRenderingHandler(EntityStoneProjectile.class, new RenderStoneProjectile());
        RenderingRegistry.registerEntityRenderingHandler(EntityTribushet.class, new RenderTribushet());
        RenderingRegistry.registerEntityRenderingHandler(EntityBatteringRam.class, new RenderBatteringRam());
        RenderingRegistry.registerEntityRenderingHandler(EntityBalista.class, new RenderBalista());
        RenderingRegistry.registerEntityRenderingHandler(EntityBalistaProjectile.class, new RenderBalistaProjectile());
        RenderingRegistry.registerEntityRenderingHandler(
                GOTEntityWildfireBomb.class,
                new RenderSnowball(GOTRegistry.wildfireBomb)
        );
        for(Decoration d : DecorationsRegister.getDecorations()) {
            bindItemRender(d.getItem(), new DecorationTileEntity(), new DecorationRenderer(d.getItem()));
        }

        FMLCommonHandler.instance().bus().register(new GOTEntityDragon3DViewer());
        FMLCommonHandler.instance().bus().register(new GOTEntityMammoth3DViewer());
        FMLCommonHandler.instance().bus().register(GOTInterfaceHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(GOTInterfaceHandler.INSTANCE);
        FMLCommonHandler.instance().bus().register(GOTBlockClientHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(GOTBlockClientHandler.INSTANCE);
        FMLCommonHandler.instance().bus().register(GOTClientStaminaHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(GOTClientStaminaHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new OverlayEventHandler());
        FMLCommonHandler.instance().bus().register(new GOTEntityElephant3DViewer());
        FMLCommonHandler.instance().bus().register(new EntityBatteringRam3DViewer());
        FMLCommonHandler.instance().bus().register(new GOTKeyHandler(GOTPacketHandler.networkWrapper));

        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }
    public static void bindItemRender(Block block, TileEntity tile, DecorationRenderer tesr){
        Item blockItem = ItemBlock.getItemFromBlock(block);
        MinecraftForgeClient.registerItemRenderer(blockItem,new RenderBlockItem(tesr, tile));
    }

    @Override
    public void onPostload() {
        musicHandler = new GOTMusic();
    }

    @Override
    public void onPreload() {
        DecorationsRegister.registerDecorations();
        System.setProperty("fml.skipFirstTextureLoad", "false");
        GOTItemRendererManager.preInit();
        GOTArmorModels.preInit();
        MinecraftForge.EVENT_BUS.register(MusicEventHandler.INSTANCE);
        FMLCommonHandler.instance().bus().register(MusicEventHandler.INSTANCE);

    }

    @Override
    public void openHiredNPCGui(GOTEntityNPC npc) {
        Minecraft mc = Minecraft.getMinecraft();
        if (npc.hiredNPCInfo.getTask() == GOTHiredNPCInfo.Task.WARRIOR) {
            mc.displayGuiScreen(new GOTGuiHiredWarrior(npc));
        } else if (npc.hiredNPCInfo.getTask() == GOTHiredNPCInfo.Task.FARMER) {
            mc.displayGuiScreen(new GOTGuiHiredFarmer(npc));
        }
    }

    @Override
    public void placeFlowerInPot(World world, int i, int j, int k, int side, ItemStack itemstack) {
        if (!world.isRemote) {
            super.placeFlowerInPot(world, i, j, k, side, itemstack);
        } else {
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(i, j, k, side, itemstack, 0.0F, 0.0F, 0.0F));
        }
    }

    @Override
    public void queueAchievement(GOTAchievement achievement) {
        GOTTickHandlerClient.notificationDisplay.queueAchievement(achievement);
    }

    @Override
    public void queueConquestNotification(GOTFaction fac, float conq, boolean isCleansing) {
        GOTTickHandlerClient.notificationDisplay.queueConquest(fac, conq, isCleansing);
    }

    @Override
    public void queueFellowshipNotification(IChatComponent message) {
        GOTTickHandlerClient.notificationDisplay.queueFellowshipNotification(message);
    }

    @Override
    public void receiveConquestGrid(GOTFaction conqFac, List<GOTConquestZone> allZones) {
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen gui = mc.currentScreen;
        if (gui instanceof GOTGuiMap) {
            ((GOTGuiMap) gui).receiveConquestGrid(conqFac, allZones);
        }
    }

    @Override
    public void renderCustomPotionEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        Potion potion = Potion.potionTypes[effect.getPotionID()];
        mc.getTextureManager().bindTexture(customPotionsTexture);
        int l = potion.getStatusIconIndex();
        GuiScreen screen = mc.currentScreen;
        if (screen != null) {
            screen.drawTexturedModalRect(x + 6, y + 7, 0 + l % 8 * 18, 0 + l / 8 * 18, 18, 18);
        }
    }

    @Override
    public void setClientDifficulty(EnumDifficulty difficulty) {
        Minecraft.getMinecraft().gameSettings.difficulty = difficulty;
    }

    @Override
    public void setInPortal(EntityPlayer entityplayer) {
        if (!GOTTickHandlerClient.playersInPortals.containsKey(entityplayer)) {
            GOTTickHandlerClient.playersInPortals.put(entityplayer, 0);
        }
        if (Minecraft.getMinecraft().isSingleplayer() && !GOTTickHandlerServer.playersInPortals.containsKey(entityplayer)) {
            GOTTickHandlerServer.playersInPortals.put(entityplayer, 0);
        }
    }

    @Override
    public void setMapCWPProtectionMessage(IChatComponent message) {
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen gui = mc.currentScreen;
        if (gui instanceof GOTGuiMap) {
            ((GOTGuiMap) gui).setCWPProtectionMessage(message);
        }
    }

    @Override
    public void setMapIsOp(boolean isOp) {
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen gui = mc.currentScreen;
        if (gui instanceof GOTGuiMap) {
            GOTGuiMap map = (GOTGuiMap) gui;
            map.isPlayerOp = isOp;
        }
    }

    @Override
    public void setTrackedQuest(GOTMiniQuest quest) {
        GOTTickHandlerClient.miniquestTracker.setTrackedQuest(quest);
    }

    @Override
    public void setWaypointModes(boolean showWP, boolean showCWP, boolean showHiddenSWP) {
        GOTGuiMap.showWP = showWP;
        GOTGuiMap.showCWP = showCWP;
        GOTGuiMap.showHiddenSWP = showHiddenSWP;
    }

    @Override
    public void showBurnOverlay() {
        tickHandler.onBurnDamage();
    }

    @Override
    public void showFrostOverlay() {
        tickHandler.onFrostDamage();
    }

    @Override
    public void spawnAlignmentBonus(GOTFaction faction, float prevMainAlignment, GOTAlignmentBonusMap factionBonusMap, String name, boolean isKill, boolean isHiredKill, float conquestBonus, double posX, double posY, double posZ) {
        World world = getClientWorld();
        if (world != null) {
            GOTEntityAlignmentBonus entity = new GOTEntityAlignmentBonus(world, posX, posY, posZ, name, faction, prevMainAlignment, factionBonusMap, isKill, isHiredKill, conquestBonus);
            world.spawnEntityInWorld(entity);
        }
    }

    @Override
    public void spawnParticle(String type, double d, double d1, double d2, double d3, double d4, double d5) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.renderViewEntity != null && mc.theWorld != null) {
            WorldClient worldClient = mc.theWorld;
            Random rand = ((World) worldClient).rand;
            int i = mc.gameSettings.particleSetting;
            if (i == 1 && rand.nextInt(3) == 0) {
                i = 2;
            }
            if (i > 1)
                return;
            if ("angry".equals(type)) {
                customEffectRenderer.addEffect(new GOTEntityAngryFX(worldClient, d, d1, d2, d3, d4, d5));
            } else if ("chill".equals(type)) {
                mc.effectRenderer.addEffect(new GOTEntityChillFX(worldClient, d, d1, d2, d3, d4, d5));
            } else if ("largeStone".equals(type)) {
                mc.effectRenderer.addEffect(new GOTEntityLargeBlockFX(worldClient, d, d1, d2, d3, d4, d5, Blocks.stone, 0));
            } else if (type.startsWith("leaf")) {
                String s = type.substring("leaf".length());
                int[] texIndices = null;
                if (s.startsWith("Gold")) {
                    if (rand.nextBoolean()) {
                        texIndices = GOTFunctions.intRange(0, 5);
                    } else {
                        texIndices = GOTFunctions.intRange(8, 13);
                    }
                } else if (s.startsWith("Red")) {
                    if (rand.nextBoolean()) {
                        texIndices = GOTFunctions.intRange(16, 21);
                    } else {
                        texIndices = GOTFunctions.intRange(24, 29);
                    }
                } else if (s.startsWith("Mirk")) {
                    if (rand.nextBoolean()) {
                        texIndices = GOTFunctions.intRange(32, 37);
                    } else {
                        texIndices = GOTFunctions.intRange(40, 45);
                    }
                } else if (s.startsWith("Green")) {
                    if (rand.nextBoolean()) {
                        texIndices = GOTFunctions.intRange(48, 53);
                    } else {
                        texIndices = GOTFunctions.intRange(56, 61);
                    }
                }
                if (texIndices != null) {
                    if (type.indexOf("_") > -1) {
                        int age = Integer.parseInt(type.substring(type.indexOf("_") + 1));
                        customEffectRenderer.addEffect(new GOTEntityLeafFX(worldClient, d, d1, d2, d3, d4, d5, texIndices, age));
                    } else {
                        customEffectRenderer.addEffect(new GOTEntityLeafFX(worldClient, d, d1, d2, d3, d4, d5, texIndices));
                    }
                }
            } else if ("marshFlame".equals(type)) {
                mc.effectRenderer.addEffect(new GOTEntityMarshFlameFX(worldClient, d, d1, d2, d3, d4, d5));
            } else if ("marshLight".equals(type)) {
                customEffectRenderer.addEffect(new GOTEntityMarshLightFX(worldClient, d, d1, d2, d3, d4, d5));
            } else if ("ulthosWater".equals(type)) {
                mc.effectRenderer.addEffect(new GOTEntityRiverWaterFX(worldClient, d, d1, d2, d3, d4, d5, GOTBiome.ulthosForest.getWaterColorMultiplier()));
            } else if ("asshaiTorch".equals(type)) {
                mc.effectRenderer.addEffect(new GOTEntityAsshaiTorchFX(worldClient, d, d1, d2, d3, d4, d5));
            } else if ("asshaiWater".equals(type)) {
                mc.effectRenderer.addEffect(new GOTEntityRiverWaterFX(worldClient, d, d1, d2, d3, d4, d5, GOTBiome.shadowLand.getWaterColorMultiplier()));
            } else if ("pickpocket".equals(type)) {
                customEffectRenderer.addEffect(new GOTEntityPickpocketFX(worldClient, d, d1, d2, d3, d4, d5));
            } else if ("pickpocketFail".equals(type)) {
                customEffectRenderer.addEffect(new GOTEntityPickpocketFailFX(worldClient, d, d1, d2, d3, d4, d5));
            } else if ("wave".equals(type)) {
                mc.effectRenderer.addEffect(new GOTEntityWaveFX(worldClient, d, d1, d2, d3, d4, d5));
            } else if ("whiteSmoke".equals(type)) {
                mc.effectRenderer.addEffect(new GOTEntityWhiteSmokeFX(worldClient, d, d1, d2, d3, d4, d5));
            }
        }
    }

    @Override
    public void addFXPotion(World p_72706_1_, ItemStack stack, int p_72706_2_, int x, int y, int z, int color) {
        Random random = p_72706_1_.rand;
        double d4;
        double d5;
        double d6;
        double d7;
        int l2;
        double d13;
        double d0 = (double)x;
        double d1 = (double)y;
        double d2 = (double)z;
        String s = "iconcrack_" + Item.getIdFromItem(Items.potionitem) + "_" + 0;

        for (int k1 = 0; k1 < 8; ++k1) {
            spawnParticle(s, d0, d1, d2, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D);
        }

        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color >> 0 & 255) / 255.0F;
        String s1 = "spell";

        //        if (Items.potionitem.isEffectInstant(p_72706_6_)) {
        //            s1 = "instantSpell";
        //        }

        for (l2 = 0; l2 < 100; ++l2) {
            d4 = random.nextDouble() * 4.0D;
            d13 = random.nextDouble() * Math.PI * 2.0D;
            d5 = Math.cos(d13) * d4;
            d6 = 0.01D + random.nextDouble() * 0.5D;
            d7 = Math.sin(d13) * d4;
            EntityFX entityfx = Minecraft.getMinecraft().renderGlobal.doSpawnParticle(s1, d0 + d5 * 0.1D, d1 + 0.3D, d2 + d7 * 0.1D, d5, d6, d7);

            if (entityfx != null) {
                float f4 = 0.75F + random.nextFloat() * 0.25F;
                entityfx.setRBGColorF(f * f4, f1 * f4, f2 * f4);
                entityfx.multiplyVelocity((float)d4);
            }
        }

        p_72706_1_.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "game.potion.smash", 1.0F, random.nextFloat() * 0.1F + 0.9F, false);

    }

    @Override
    public void testReflection(World world) {
        super.testReflection(world);
        GOTReflectionClient.testAll(world, Minecraft.getMinecraft());
    }

    @Override
    public void usePouchOnChest(EntityPlayer entityplayer, World world, int i, int j, int k, int side, ItemStack itemstack, int pouchSlot) {
        if (!world.isRemote) {
            super.usePouchOnChest(entityplayer, world, i, j, k, side, itemstack, pouchSlot);
        } else {
            ((EntityClientPlayerMP) entityplayer).sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(i, j, k, side, itemstack, 0.0F, 0.0F, 0.0F));
        }
    }

    @Override
    public void validateBannerUsername(GOTEntityBanner banner, int slot, String prevText, boolean valid) {
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen gui = mc.currentScreen;
        if (gui instanceof GOTGuiBanner) {
            GOTGuiBanner guiBanner = (GOTGuiBanner) gui;
            if (guiBanner.theBanner == banner) {
                guiBanner.validateUsername(slot, prevText, valid);
            }
        }
    }

    public static boolean doesClientChunkExist(World world, int i, int k) {
        int chunkX = i >> 4;
        int chunkZ = k >> 4;
        Chunk chunk = world.getChunkProvider().provideChunk(chunkX, chunkZ);
        return !(chunk instanceof net.minecraft.world.chunk.EmptyChunk);
    }

    public static int getAlphaInt(float alphaF) {
        int alphaI = (int) (alphaF * 255.0F);
        return MathHelper.clamp_int(alphaI, 4, 255);
    }

    public static void renderEnchantmentEffect() {
        Tessellator tessellator = Tessellator.instance;
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        GL11.glDepthFunc(514);
        GL11.glDisable(2896);
        texturemanager.bindTexture(enchantmentTexture);
        GL11.glEnable(3042);
        GL11.glBlendFunc(768, 1);
        float shade = 0.76F;
        GL11.glColor4f(0.5F * shade, 0.25F * shade, 0.8F * shade, 1.0F);
        GL11.glMatrixMode(5890);
        GL11.glPushMatrix();
        float scale = 0.125F;
        GL11.glScalef(scale, scale, scale);
        float randomShift = Minecraft.getSystemTime() % 3000L / 3000.0F * 8.0F;
        GL11.glTranslatef(randomShift, 0.0F, 0.0F);
        GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
        ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        randomShift = Minecraft.getSystemTime() % 4873L / 4873.0F * 8.0F;
        GL11.glTranslatef(-randomShift, 0.0F, 0.0F);
        GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
        ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
        GL11.glPopMatrix();
        GL11.glMatrixMode(5888);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glDepthFunc(515);
    }

    public static void sendClientInfoPacket(GOTFaction viewingFaction, Map<GOTDimension.DimensionRegion, GOTFaction> changedRegionMap) {
        boolean showWP = GOTGuiMap.showWP;
        boolean showCWP = GOTGuiMap.showCWP;
        boolean showHiddenSWP = GOTGuiMap.showHiddenSWP;
        GOTPacketClientInfo packet = new GOTPacketClientInfo(viewingFaction, changedRegionMap, showWP, showCWP, showHiddenSWP);
        GOTPacketHandler.networkWrapper.sendToServer(packet);
    }

    @Override
    public void handleSyncInventory(net.minecraft.item.ItemStack[] mainInventory, net.minecraft.item.ItemStack itemstack) {
        net.minecraft.entity.player.EntityPlayer player = net.minecraft.client.Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            for (int i = 0; i < mainInventory.length; i++) {
                player.inventory.mainInventory[i] = mainInventory[i];
            }
            player.inventory.setItemStack(itemstack);
        }
    }

    @Override
    public void handleSyncWeaponHitCount(net.minecraft.item.Item item, int hitCount) {
        if (item != null) {
            if (hitCount <= 0) {
                weaponHitCounts.remove(item);
            } else {
                weaponHitCounts.put(item, hitCount);
            }
        }
    }
}
