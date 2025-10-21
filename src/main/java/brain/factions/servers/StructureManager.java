package brain.factions.servers;

import brain.factions.Faction;
import brain.factions.network.PacketMessage;
import brain.factions.structures.BlockData;
import brain.factions.structures.FactionStructureManager;
import brain.factions.structures.FactionStructureSlot;
import brain.factions.structures.StructureData;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import got.common.block.factionblocks.TileEntityStructureHeart;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class StructureManager {

    public static ServerConfig config;
    private static Timer resourceTimer;
    public static boolean isRaidTime = false;
    public static boolean isRaidTimeFortress = false;

    public static void init(File configFolder) {
        config = new ServerConfig(new File(configFolder, "server_config.cfg"));
        startResourceGeneration();
    }

    public static boolean isRaidTimeActive() {
        return isRaidTime;
    }

    private static void startResourceGeneration() {
        if (resourceTimer != null) resourceTimer.cancel();
        resourceTimer = new Timer("StructureResourceTimer", true);
        resourceTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ServerTaskExecutor.addScheduledTask(() -> {
                    final MinecraftServer server = MinecraftServer.getServer();
                    if (server == null) return;
                    final WorldServer mainWorld = server.worldServerForDimension(0);
                    if (mainWorld == null) return;

                    for (FactionStructureSlot slot : FactionStructureManager.structureSlots) {
                        if (slot.ownerFactionID != null && (slot.category == FactionStructureSlot.StructureCategory.FARMS || slot.category == FactionStructureSlot.StructureCategory.INDUSTRY || slot.type == FactionStructureSlot.StructureType.FORTRESS)) {
                            Faction ownerFaction = CoreFaction.factions.get(slot.ownerFactionID);
                            if(ownerFaction != null && slot.id.equals(ownerFaction.getMainFortressId())) {
                                continue;
                            }

                            TileEntity te = mainWorld.getTileEntity(slot.xCoord, slot.yCoord, slot.zCoord);
                            if (te instanceof TileEntityStructureHeart) {
                                String[] items = config.resourceItems.getOrDefault(slot.level, new String[0]);
                                if (items.length > 0) {
                                    String itemString = items[mainWorld.rand.nextInt(items.length)];
                                    Item item = (Item) Item.itemRegistry.getObject(itemString);
                                    if (item != null) {
                                        ((TileEntityStructureHeart) te).addItems(new ItemStack(item, config.resourcesPerCollection.getOrDefault(slot.level, 1)));
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }, config.resourceGenerationInterval * 1000L, config.resourceGenerationInterval * 1000L);
    }

    public static void handlePurchase(EntityPlayerMP player, String structureId, String purchaseType, String subType) {
        Faction playerFaction = PacketMessage.getCurrentFaction(player.getCommandSenderName());
        FactionStructureSlot slot = FactionStructureManager.getStructureById(structureId);

        if (playerFaction == null || slot == null || slot.ownerFactionID != null) return;

        if (!playerFaction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_MANAGE_STRUCTURES)) {
            player.addChatMessage(new ChatComponentText("§cУ вас нет прав на захват точек."));
            return;
        }

        boolean isFortressPurchase = slot.type == FactionStructureSlot.StructureType.FORTRESS;

        long fortressCount = FactionStructureManager.structureSlots.stream().filter(s -> playerFaction.getID().equals(s.ownerFactionID) && s.type == FactionStructureSlot.StructureType.FORTRESS).count();
        long resourcePointCount = FactionStructureManager.structureSlots.stream().filter(s -> playerFaction.getID().equals(s.ownerFactionID) && s.type != FactionStructureSlot.StructureType.FORTRESS).count();

        if (isFortressPurchase) {
            if (fortressCount >= config.maxFortressesPerFaction) {
                player.addChatMessage(new ChatComponentText("§cВаша фракция достигла лимита крепостей (" + config.maxFortressesPerFaction + ")!"));
                return;
            }
        } else {
            if (resourcePointCount >= config.maxStructuresPerFaction) {
                player.addChatMessage(new ChatComponentText("§cВаша фракция достигла лимита ресурсных точек (" + config.maxStructuresPerFaction + ")!"));
                return;
            }
        }


        if (playerFaction.getTreasury() >= slot.price) {
            World world = player.worldObj;
            try {
                String fileName = FactionStructureManager.structureTypes.get(purchaseType).get(subType);
                if (fileName == null) {
                    player.addChatMessage(new ChatComponentText("§cОшибка: не найден файл для структуры '" + subType + "' в категории '" + purchaseType + "'."));
                    return;
                }

                File structuresFolder = new File(CoreFaction.configFolder, "structures");
                File structureFile = new File(structuresFolder, fileName);
                if (!structureFile.exists()) {
                    player.addChatMessage(new ChatComponentText("§cОшибка: файл структуры " + fileName + " не найден на сервере."));
                    return;
                }

                String jsonContent = Files.toString(structureFile, Charsets.UTF_8);
                StructureData structureData = new Gson().fromJson(jsonContent, StructureData.class);

                int xOffset = structureData.getWidth() / 2;
                int zOffset = structureData.getDepth() / 2;

                for (BlockData blockData : structureData.getBlocks()) {
                    Block block = (Block) Block.blockRegistry.getObject(blockData.getId());
                    if (block != null) {
                        int blockX = slot.xCoord + blockData.getX() - xOffset;
                        int blockY = slot.yCoord + blockData.getY() - 3;
                        int blockZ = slot.zCoord + blockData.getZ() - zOffset;

                        if (blockX == slot.xCoord && blockY == slot.yCoord && blockZ == slot.zCoord) {
                            continue;
                        }

                        world.setBlock(blockX, blockY, blockZ, block, blockData.getMetadata(), 3);

                        NBTTagCompound nbt = blockData.getNbt();
                        if (nbt != null) {
                            TileEntity te = world.getTileEntity(blockX, blockY, blockZ);
                            if (te != null) {
                                te.readFromNBT(nbt);
                                te.markDirty();
                            }
                        }
                    }
                }

                playerFaction.setTreasury(playerFaction.getTreasury() - slot.price);

                slot.ownerFactionID = playerFaction.getID();
                slot.name = subType;
                slot.category = FactionStructureSlot.StructureCategory.valueOf(purchaseType);
                slot.resource = subType;
                slot.structureFile = fileName;
                slot.level = 1;
                slot.health = config.structureBaseHealth;
                slot.destructionCount = config.structureBreakCounts.getOrDefault(1, 1);

                if (isFortressPurchase) {
                    slot.barracksCapacity = config.fortressBarracksCapacity.getOrDefault(1, 10);
                    slot.provisions = 0;
                }

                TileEntity te = world.getTileEntity(slot.xCoord, slot.yCoord, slot.zCoord);
                if(te instanceof TileEntityStructureHeart){
                    ((TileEntityStructureHeart)te).setStructureId(structureId);
                }

                FactionStructureManager.setStructureOwner(slot.id, playerFaction.getID());
                CoreFaction.saveFactions();
                CoreFaction.sendAllGui();
                player.addChatMessage(new ChatComponentText("§aВы успешно захватили и построили: " + slot.name));
                player.closeScreen();
            } catch (IOException e) {
                player.addChatMessage(new ChatComponentText("§cОшибка при чтении файла структуры."));
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                player.addChatMessage(new ChatComponentText("§cОшибка: неверная категория структуры '" + purchaseType + "'."));
            }
        } else {
            player.addChatMessage(new ChatComponentText("§cВ казне недостаточно средств."));
        }
    }

    public static void handleCollect(EntityPlayerMP player, String structureId) {
        Faction playerFaction = PacketMessage.getCurrentFaction(player.getCommandSenderName());
        FactionStructureSlot slot = FactionStructureManager.getStructureById(structureId);

        if (playerFaction != null && slot != null && playerFaction.getID().equals(slot.ownerFactionID)) {
            World world = player.worldObj;
            TileEntity te = world.getTileEntity(slot.xCoord, slot.yCoord, slot.zCoord);
            if (te instanceof TileEntityStructureHeart) {
                ((TileEntityStructureHeart) te).collectItems(player);
                player.addChatMessage(new ChatComponentText("§aВы собрали ресурсы."));
            }
        }
    }

    public static void handleUpgrade(EntityPlayerMP player, String structureId) {
        Faction playerFaction = PacketMessage.getCurrentFaction(player.getCommandSenderName());
        FactionStructureSlot slot = FactionStructureManager.getStructureById(structureId);

        if (playerFaction != null && slot != null && playerFaction.getID().equals(slot.ownerFactionID)) {
            if (!playerFaction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_MANAGE_STRUCTURES)) {
                player.addChatMessage(new ChatComponentText("§cУ вас нет прав на улучшение точек."));
                return;
            }

            if (playerFaction.isInWarState()) {
                player.addChatMessage(new ChatComponentText("§cНельзя улучшать постройки во время войны!"));
                return;
            }

            if (slot.level < config.maxStructureLevel) {
                int cost = config.upgradeCost.getOrDefault(slot.level + 1, Integer.MAX_VALUE);
                if (playerFaction.getTreasury() >= cost) {
                    playerFaction.setTreasury(playerFaction.getTreasury() - cost);
                    slot.level++;
                    slot.destructionCount = config.structureBreakCounts.getOrDefault(slot.level, slot.level);

                    if (slot.type == FactionStructureSlot.StructureType.FORTRESS) {
                        slot.barracksCapacity = config.fortressBarracksCapacity.getOrDefault(slot.level, slot.barracksCapacity);
                    }

                    FactionStructureManager.saveStructureOwnership();
                    CoreFaction.saveFactions();
                    CoreFaction.sendAllGui();
                    player.addChatMessage(new ChatComponentText("§aСтруктура улучшена до уровня " + slot.level));
                } else {
                    player.addChatMessage(new ChatComponentText("§cНедостаточно средств для улучшения. Цена: " + cost));
                }
            } else {
                player.addChatMessage(new ChatComponentText("§cДостигнут максимальный уровень."));
            }
        }
    }
}