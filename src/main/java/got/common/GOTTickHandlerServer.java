package got.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import got.GOT;
import got.common.database.GOTEffects;
import got.common.database.GOTRegistry;
import got.common.entity.other.GOTEntityPortal;
import got.common.faction.GOTFactionBounties;
import got.common.faction.GOTFactionRelations;
import got.common.fellowship.GOTFellowshipData;
import got.common.item.other.GOTItemBanner;
import got.common.item.other.GOTItemBanner.BannerType;
import got.common.item.other.GOTItemStructureSpawner;
import got.common.item.weapon.GOTItemCrossbow;
import got.common.util.GOTReflection;
import got.common.world.GOTTeleporter;
import got.common.world.GOTWorldInfo;
import got.common.world.GOTWorldProvider;
import got.common.world.biome.GOTClimateType;
import got.common.world.biome.variant.GOTBiomeVariantStorage;
import got.common.world.map.GOTConquestGrid;
import got.common.world.spawning.GOTEventSpawner;
import got.common.world.spawning.GOTSpawnerNPCs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class GOTTickHandlerServer {
    public static Map<EntityPlayer, Integer> playersInPortals = new HashMap<>();

    public GOTTickHandlerServer() {
        FMLCommonHandler.instance().bus().register(this);
    }


    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        World world = player.worldObj;
        if (world == null || world.isRemote)
            return;
        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) player;
            if (event.phase == TickEvent.Phase.START && entityplayer.playerNetServerHandler != null && !(entityplayer.playerNetServerHandler instanceof GOTNetHandlerPlayServer)) {
                entityplayer.playerNetServerHandler = new GOTNetHandlerPlayServer(MinecraftServer.getServer(), entityplayer.playerNetServerHandler.netManager, entityplayer);
            }

            if (event.phase == TickEvent.Phase.END) {
                GOTLevelData.getData(entityplayer).onUpdate(entityplayer, (WorldServer) world);
                GOTPlayerData data = GOTLevelData.getData(entityplayer);
                int bridleMountId = data.getBridleMount();
                if (bridleMountId != -1) {
                    if (entityplayer.isRiding() && entityplayer.ridingEntity != null
                            && entityplayer.ridingEntity.getEntityId() == bridleMountId && entityplayer.isSneaking()) {
                        Entity mount = entityplayer.ridingEntity;
                        entityplayer.dismountEntity(mount);
                        mount.setDead();
                        entityplayer.ridingEntity = null;
                        data.setBridleMount(-1);
                    } else if (!entityplayer.isRiding()) {
                        Entity mount = world.getEntityByID(bridleMountId);
                        if (mount != null) {
                            mount.setDead();
                        }
                        data.setBridleMount(-1);
                    }
                }
                NetHandlerPlayServer netHandler = entityplayer.playerNetServerHandler;
                if (netHandler instanceof GOTNetHandlerPlayServer) {
                    ((GOTNetHandlerPlayServer) netHandler).update();
                }
                ItemStack heldItem = entityplayer.getHeldItem();
                if (heldItem != null && (heldItem.getItem() instanceof ItemWritableBook || heldItem.getItem() instanceof ItemEditableBook)) {
                    entityplayer.func_143004_u();
                }
                if (entityplayer.dimension == 0 && GOTLevelData.madePortal == 0) {
                    List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, entityplayer.boundingBox.expand(16.0D, 16.0D, 16.0D));
                    for (EntityItem item : items) {
                        if (GOTLevelData.madePortal == 0 && item.getEntityItem() != null && item.getEntityItem().getItem() == Items.iron_sword && item.isBurning()) {
                            GOTLevelData.setMadePortal(1);
                            GOTLevelData.markOverworldPortalLocation(MathHelper.floor_double(item.posX), MathHelper.floor_double(item.posY), MathHelper.floor_double(item.posZ));
                            item.setDead();
                            world.createExplosion(entityplayer, item.posX, item.posY + 3.0D, item.posZ, 3.0F, true);
                            GOTEntityPortal portal = new GOTEntityPortal(world);
                            portal.setLocationAndAngles(item.posX, item.posY + 3.0D, item.posZ, 0.0F, 0.0F);
                            world.spawnEntityInWorld(portal);
                        }
                    }
                }
                if ((entityplayer.dimension == 0 || entityplayer.dimension == GOTDimension.GAME_OF_THRONES.dimensionID) && playersInPortals.containsKey(entityplayer)) {
                    List<GOTEntityPortal> portals = world.getEntitiesWithinAABB(GOTEntityPortal.class, entityplayer.boundingBox.expand(8.0D, 8.0D, 8.0D));
                    boolean inPortal = false;
                    int i;
                    for (i = 0; i < portals.size(); i++) {
                        GOTEntityPortal portal = portals.get(i);
                        if (portal.boundingBox.intersectsWith(entityplayer.boundingBox)) {
                            inPortal = true;
                            break;
                        }
                    }
                    if (inPortal) {
                        i = playersInPortals.get(entityplayer);
                        i++;
                        playersInPortals.put(entityplayer, i);
                        if (i >= 100) {
                            int dimension = 0;
                            if (entityplayer.dimension == 0) {
                                dimension = GOTDimension.GAME_OF_THRONES.dimensionID;
                            } else if (entityplayer.dimension == GOTDimension.GAME_OF_THRONES.dimensionID) {
                                dimension = 0;
                            }
                            if (world instanceof WorldServer) {
                                MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(entityplayer, dimension, new GOTTeleporter(DimensionManager.getWorld(dimension), true));
                            }
                            playersInPortals.remove(entityplayer);
                        }
                    } else {
                        playersInPortals.remove(entityplayer);
                    }
                }
            }

            if(entityplayer.isPotionActive(GOTEffects.nauseaResistance) && entityplayer.isPotionActive(Potion.confusion)) {
                entityplayer.removePotionEffect(Potion.confusion.id);
            }

            if(entityplayer.isPotionActive(GOTEffects.frostResistance) && entityplayer.isPotionActive(GOTEffects.freeze)) {
                entityplayer.removePotionEffect(GOTEffects.freeze.id);
            }

            if(entityplayer.isPotionActive(GOTEffects.antidote) && entityplayer.isPotionActive(GOTEffects.killingPoison)) {
                entityplayer.removePotionEffect(GOTEffects.killingPoison.id);
            }

            if(entityplayer.isPotionActive(GOTEffects.antidote) && entityplayer.isPotionActive(Potion.poison)) {
                entityplayer.removePotionEffect(Potion.poison.id);
            }

            if(entityplayer.inventory.hasItem(GOTRegistry.rabbitPaw)) {
                entityplayer.addPotionEffect(new PotionEffect(Potion.jump.id, 40));
            }

            if(entityplayer.inventory.hasItem(GOTRegistry.minerRing)) {
                entityplayer.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 40));
            }

            if(!world.isRemote && entityplayer.getHeldItem() != null) {
                Item item = entityplayer.getHeldItem().getItem();
                if(item == GOTRegistry.siegeCrossbow && GOTItemCrossbow.isLoaded(entityplayer.getHeldItem())) {
                    entityplayer.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.01);
                }

                if(item == GOTRegistry.banner) {
                    GOTItemBanner.BannerType type = GOTItemBanner.getBannerType(entityplayer.getHeldItem().getItemDamage());
                    if(type == BannerType.GREYJOY  || type == BannerType.LANNISTER || type == BannerType.EDDARD
                            || type == BannerType.HIGHTOWER || type == BannerType.ROBERT || type == BannerType.MARTELL
                            || type == BannerType.TULLY || type == BannerType.ARRYN || type == BannerType.STANNIS) {
                        List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, entityplayer.boundingBox.copy().expand(8.0f, 8.0f, 8.0f));
                        if(list != null) {
                            for(Entity entity : list) {
                                if(!(entity instanceof EntityLivingBase) || entity.equals(entityplayer)) {
                                    continue;
                                }
                                ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(GOTEffects.combatSpirit.id, 20));
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        World world = event.world;
        if (world.isRemote)
            return;
        if (event.phase == TickEvent.Phase.START && world == DimensionManager.getWorld(0)) {
            World overworld = world;
            if (GOTLevelData.needsLoad) {
                GOTLevelData.load();
            }
            if (GOTTime.needsLoad) {
                GOTTime.load();
            }
            if (GOTFellowshipData.needsLoad) {
                GOTFellowshipData.loadAll();
            }
            if (GOTFactionBounties.needsLoad) {
                GOTFactionBounties.loadAll();
            }
            if (GOTFactionRelations.needsLoad) {
                GOTFactionRelations.load();
            }
            if (GOTConquestGrid.needsLoad) {
                GOTConquestGrid.loadAllZones();
            }
            for (WorldServer dimWorld : MinecraftServer.getServer().worldServers) {
                if (dimWorld.provider instanceof GOTWorldProvider) {
                    WorldInfo prevWorldInfo = dimWorld.getWorldInfo();
                    if (prevWorldInfo.getClass() != GOTWorldInfo.class) {
                        GOTWorldInfo lOTRWorldInfo = new GOTWorldInfo(overworld.getWorldInfo());
                        lOTRWorldInfo.setWorldName(prevWorldInfo.getWorldName());
                        GOTReflection.setWorldInfo(dimWorld, lOTRWorldInfo);
                        FMLLog.info("Hummel009: Successfully replaced world info in %s", GOTDimension.getCurrentDimensionWithFallback(dimWorld).dimensionName);
                    }
                }
            }
            GOTBannerProtection.updateWarningCooldowns();
            GOTInterModComms.update();
        }
        if (event.phase == TickEvent.Phase.END) {
            if (world == DimensionManager.getWorld(0)) {
                if (GOTLevelData.anyDataNeedsSave()) {
                    GOTLevelData.save();
                }
                if (GOTFellowshipData.anyDataNeedsSave()) {
                    GOTFellowshipData.saveAll();
                }
                GOTFactionBounties.updateAll();
                if (GOTFactionBounties.anyDataNeedsSave()) {
                    GOTFactionBounties.saveAll();
                }
                if (GOTFactionRelations.needsSave()) {
                    GOTFactionRelations.save();
                }
                if (GOTConquestGrid.anyChangedZones()) {
                    GOTConquestGrid.saveChangedZones();
                }
                if (world.getTotalWorldTime() % 600L == 0L) {
                    GOTLevelData.save();
                    GOTFellowshipData.saveAll();
                    GOTFellowshipData.saveAndClearUnusedFellowships();
                    GOTFactionBounties.saveAll();
                    GOTFactionRelations.save();
                }
                int playerDataClearingInterval = GOTConfig.playerDataClearingInterval;
                playerDataClearingInterval = Math.max(playerDataClearingInterval, 600);
                if (world.getTotalWorldTime() % playerDataClearingInterval == 0L) {
                    GOTLevelData.saveAndClearUnusedPlayerData();
                }
                if (GOTItemStructureSpawner.lastStructureSpawnTick > 0) {
                    GOTItemStructureSpawner.lastStructureSpawnTick--;
                }
                GOTTime.update();
                GOTJaqenHgharTracker.updateCooldowns();
            }
            if (world == DimensionManager.getWorld(GOTDimension.GAME_OF_THRONES.dimensionID)) {
                GOTDate.update(world);
                if (GOT.canSpawnMobs(world)) {
                    GOTSpawnerNPCs.performSpawning(world);
                    GOTEventSpawner.performSpawning(world);
                }
                GOTConquestGrid.updateZones(world);
                if (!world.playerEntities.isEmpty() && world.getTotalWorldTime() % 20L == 0L) {
                    for (Object element : world.playerEntities) {
                        EntityPlayer entityplayer = (EntityPlayer) element;
                        GOTLevelData.sendPlayerLocationsToPlayer(entityplayer, world);
                    }
                }
            }
            if (world.provider instanceof GOTWorldProvider && world.getTotalWorldTime() % 100L == 0L) {
                GOTBiomeVariantStorage.performCleanup((WorldServer) world);
            }
            if (world.getTotalWorldTime() % 20L == 0L) {
                GOTClimateType.performSeasonalChanges();
            }
        }
    }
}
