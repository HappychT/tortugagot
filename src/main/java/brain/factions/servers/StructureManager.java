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
import got.common.block.factionblocks.BlockStructureHeart;
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
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

public class StructureManager {

    public static ServerConfig config;
    private static Timer resourceTimer;
    public static boolean isRaidTime = false;
    public static boolean isRaidTimeFortress = false;

    public static int serverTimeOffsetMinutes = 0;

    public static void init(File configFolder) {
        config = new ServerConfig(new File(configFolder, "server_config.cfg"));
        CustomResourceConfig.init(configFolder);
        startResourceGeneration();
    }

    public static boolean isRaidTimeActive() {
        return isRaidTime;
    }

    public static boolean isRaidTimeNow(FactionStructureSlot slot) {
        if (slot == null) return false;

        String timeString = null;

        if (slot.raidTime != null && !slot.raidTime.isEmpty() && !slot.raidTime.equals("00:00-00:00")) {
            timeString = slot.raidTime;
        } else {
            if (slot.type == FactionStructureSlot.StructureType.FORTRESS) {
                timeString = config.specificRaidTimes.get("FORTRESS");
            } else if (slot.category != null) {
                timeString = config.specificRaidTimes.get(slot.category.name());
            }

            if (timeString == null) {
                timeString = config.raidTimeResourcePoints;
            }
        }

        return isTimeInInterval(timeString);
    }

    public static boolean isTimeInInterval(String interval) {
        try {
            if(interval == null || !interval.contains("-")) return false;
            String[] parts = interval.split("-");
            LocalTime start = LocalTime.parse(parts[0]);
            LocalTime end = LocalTime.parse(parts[1]);

            LocalTime now = LocalTime.now().plusMinutes(serverTimeOffsetMinutes);

            if (start.isAfter(end)) {
                return !now.isBefore(start) || now.isBefore(end);
            } else {
                return !now.isBefore(start) && now.isBefore(end);
            }
        } catch (Exception e) {
            return false;
        }
    }

    private static void startResourceGeneration() {
        if (resourceTimer != null) resourceTimer.cancel();
        resourceTimer = new Timer("StructureResourceTimer", true);

        long interval = config.resourceGenerationInterval * 1000L;

        resourceTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ServerTaskExecutor.addScheduledTask(() -> {
                    final MinecraftServer server = MinecraftServer.getServer();
                    if (server == null) return;
                    for (WorldServer world : server.worldServers) {
                        for (FactionStructureSlot slot : FactionStructureManager.structureSlots) {
                            if (slot.ownerFactionID == null) continue;
                            Faction ownerFaction = CoreFaction.factions.get(slot.ownerFactionID);
                            if (ownerFaction != null && slot.id.equals(ownerFaction.getMainFortressId())) continue;

                            if (slot.category == FactionStructureSlot.StructureCategory.FARMS ||
                                    slot.category == FactionStructureSlot.StructureCategory.INDUSTRY ||
                                    slot.type == FactionStructureSlot.StructureType.FORTRESS) {

                                TileEntityStructureHeart te = findStructureHeartNearby(world, slot.xCoord, slot.yCoord, slot.zCoord, 10, 5);

                                if (te != null) {
                                    if (!te.getStructureId().equals(slot.id)) {
                                        te.setStructureId(slot.id);
                                        te.markDirty();
                                    }

                                    Map<String, Map<String, List<CustomResourceConfig.ResourceEntry>>> factionCats = CustomResourceConfig.factionResources.get(slot.ownerFactionID);
                                    if (factionCats != null) {
                                        Map<String, List<CustomResourceConfig.ResourceEntry>> variants = factionCats.get(slot.category.name());
                                        if (variants != null) {
                                            String variantKey = slot.resource;
                                            if (slot.type == FactionStructureSlot.StructureType.FORTRESS && (variantKey == null || variantKey.isEmpty())) {
                                                variantKey = "FORTRESS";
                                            }

                                            List<CustomResourceConfig.ResourceEntry> resources = variants.get(variantKey);
                                            if (resources != null) {
                                                double productivityMultiplier = 1.0 + ((slot.level - 1) * 0.20);

                                                for (CustomResourceConfig.ResourceEntry res : resources) {
                                                    double totalAmount = res.amount * productivityMultiplier;
                                                    te.addFractionalItem(res.item, totalAmount);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }, interval, interval);
    }

    private static TileEntityStructureHeart findStructureHeartNearby(World world, int centerX, int centerY, int centerZ, int radiusXZ, int radiusY) {
        TileEntity centerTe = world.getTileEntity(centerX, centerY, centerZ);
        if (centerTe instanceof TileEntityStructureHeart) {
            return (TileEntityStructureHeart) centerTe;
        }

        for (int dx = -radiusXZ; dx <= radiusXZ; dx++) {
            for (int dy = -radiusY; dy <= radiusY; dy++) {
                for (int dz = -radiusXZ; dz <= radiusXZ; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;

                    int x = centerX + dx;
                    int y = centerY + dy;
                    int z = centerZ + dz;

                    if (world.blockExists(x, y, z)) {
                        TileEntity te = world.getTileEntity(x, y, z);
                        if (te instanceof TileEntityStructureHeart) {
                            return (TileEntityStructureHeart) te;
                        }
                    }
                }
            }
        }

        return null;
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

        long existingCount = FactionStructureManager.structureSlots.stream()
                .filter(s -> playerFaction.getID().equals(s.ownerFactionID))
                .filter(s -> isFortressPurchase ? s.type == FactionStructureSlot.StructureType.FORTRESS : s.type != FactionStructureSlot.StructureType.FORTRESS)
                .count();

        long finalPrice = (long) (slot.price * Math.pow(2, existingCount));

        if (playerFaction.getTreasury() >= finalPrice) {
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

                int oldHeartX = slot.xCoord;
                int oldHeartY = slot.yCoord;
                int oldHeartZ = slot.zCoord;
                int minX = 0, minY = 0, minZ = 0;
                int maxX = 0, maxY = 0, maxZ = 0;

                for (BlockData b : structureData.getBlocks()) {
                    if (b.getX() < minX) minX = b.getX();
                    if (b.getX() > maxX) maxX = b.getX();
                    if (b.getY() < minY) minY = b.getY();
                    if (b.getY() > maxY) maxY = b.getY();
                    if (b.getZ() < minZ) minZ = b.getZ();
                    if (b.getZ() > maxZ) maxZ = b.getZ();
                }

                for (int x = minX; x <= maxX; x++) {
                    for (int y = minY; y <= maxY; y++) {
                        for (int z = minZ; z <= maxZ; z++) {
                            int clearX = oldHeartX + x - xOffset;
                            int clearY = oldHeartY + y - 3;
                            int clearZ = oldHeartZ + z - zOffset;

                            if (clearX == oldHeartX && clearY == oldHeartY && clearZ == oldHeartZ) continue;

                            world.setBlockToAir(clearX, clearY, clearZ);
                        }
                    }
                }
                boolean newHeartPlaced = false;

                for (BlockData blockData : structureData.getBlocks()) {
                    Block block = (Block) Block.blockRegistry.getObject(blockData.getId());
                    if (block == null) continue;

                    int blockX = oldHeartX + blockData.getX() - xOffset;
                    int blockY = oldHeartY + blockData.getY() - 3;
                    int blockZ = oldHeartZ + blockData.getZ() - zOffset;

                    if (blockX == oldHeartX && blockY == oldHeartY && blockZ == oldHeartZ) {
                        continue;
                    }

                    if (block instanceof BlockStructureHeart) {
                        if (newHeartPlaced) {
                            CoreFaction.logger().log(Level.WARNING, "Структура " + fileName + " содержит более одного сердца! Лишний блок заменен на воздух.");
                            world.setBlockToAir(blockX, blockY, blockZ);
                            continue;
                        }

                        world.setBlock(blockX, blockY, blockZ, block, blockData.getMetadata(), 3);

                        slot.xCoord = blockX;
                        slot.yCoord = blockY;
                        slot.zCoord = blockZ;
                        newHeartPlaced = true;

                        TileEntity te = world.getTileEntity(blockX, blockY, blockZ);
                        if (te instanceof TileEntityStructureHeart) {
                            ((TileEntityStructureHeart) te).setStructureId(structureId);
                        }

                        NBTTagCompound nbt = blockData.getNbt();
                        if (nbt != null && te != null) {
                            te.readFromNBT(nbt);
                            te.markDirty();
                        }
                    } else {
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

                if (newHeartPlaced) {
                    world.setBlockToAir(oldHeartX, oldHeartY, oldHeartZ);
                } else {
                    TileEntity te = world.getTileEntity(slot.xCoord, slot.yCoord, slot.zCoord);
                    if(te instanceof TileEntityStructureHeart){
                        ((TileEntityStructureHeart)te).setStructureId(structureId);
                    } else {
                        CoreFaction.logger().log(Level.SEVERE, "Structure slot " + slot.id + " at " + slot.xCoord + "," + slot.yCoord + "," + slot.zCoord + " is NOT a TileEntityStructureHeart and no new heart was provided!");
                    }
                }

                playerFaction.setTreasury(playerFaction.getTreasury() - finalPrice);

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

                FactionStructureManager.setStructureOwner(slot.id, playerFaction.getID());
                CoreFaction.saveFactions();
                CoreFaction.sendAllGui();
                player.addChatMessage(new ChatComponentText("§aВы успешно захватили и построили: " + slot.name + " за " + finalPrice + " монет (Множитель x" + Math.pow(2, existingCount) + ")"));
                player.closeScreen();
            } catch (IOException e) {
                player.addChatMessage(new ChatComponentText("§cОшибка при чтении файла структуры."));
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                player.addChatMessage(new ChatComponentText("§cОшибка: неверная категория структуры '" + purchaseType + "'."));
            }
        } else {
            player.addChatMessage(new ChatComponentText("§cВ казне недостаточно средств. Требуется: " + finalPrice));
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
                int baseCost = config.upgradeCost.getOrDefault(slot.level + 1, 5000);
                long finalCost = (long) (baseCost * Math.pow(1.5, slot.level - 1));

                if (playerFaction.getTreasury() >= finalCost) {
                    playerFaction.setTreasury(playerFaction.getTreasury() - finalCost);
                    slot.level++;
                    slot.destructionCount = config.structureBreakCounts.getOrDefault(slot.level, slot.level);

                    if (slot.type == FactionStructureSlot.StructureType.FORTRESS) {
                        slot.barracksCapacity = config.fortressBarracksCapacity.getOrDefault(slot.level, slot.barracksCapacity);
                    }

                    FactionStructureManager.saveStructureOwnership();
                    CoreFaction.saveFactions();
                    CoreFaction.sendAllGui();
                    player.addChatMessage(new ChatComponentText("§aСтруктура улучшена до уровня " + slot.level + " за " + finalCost));
                } else {
                    player.addChatMessage(new ChatComponentText("§cНедостаточно средств для улучшения. Цена: " + finalCost));
                }
            } else {
                player.addChatMessage(new ChatComponentText("§cДостигнут максимальный уровень."));
            }
        }
    }
}