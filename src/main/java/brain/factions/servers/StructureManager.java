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
import got.common.entity.other.GOTEntityNPC;
import got.common.entity.westeros.arryn.GOTEntityArrynSoldier;
import got.common.entity.westeros.arryn.GOTEntityArrynSoldierArcher;
import got.common.entity.westeros.crownlands.GOTEntityCrownlandsLevyman;
import got.common.entity.westeros.crownlands.GOTEntityCrownlandsLevymanArcher;
import got.common.entity.westeros.crownlands.GOTEntityKingsguard;
import got.common.entity.westeros.dorne.GOTEntityDorneSoldier;
import got.common.entity.westeros.dorne.GOTEntityDorneSoldierArcher;
import got.common.entity.westeros.dragonstone.GOTEntityDragonstoneSoldier;
import got.common.entity.westeros.dragonstone.GOTEntityDragonstoneSoldierArcher;
import got.common.entity.westeros.ironborn.GOTEntityIronbornSoldier;
import got.common.entity.westeros.ironborn.GOTEntityIronbornSoldierArcher;
import got.common.entity.westeros.north.GOTEntityNorthSoldier;
import got.common.entity.westeros.north.GOTEntityNorthSoldierArcher;
import got.common.entity.westeros.reach.GOTEntityReachSoldier;
import got.common.entity.westeros.reach.GOTEntityReachSoldierArcher;
import got.common.entity.westeros.riverlands.GOTEntityRiverlandsSoldier;
import got.common.entity.westeros.riverlands.GOTEntityRiverlandsSoldierArcher;
import got.common.entity.westeros.stormlands.GOTEntityStormlandsSoldier;
import got.common.entity.westeros.stormlands.GOTEntityStormlandsSoldierArcher;
import got.common.entity.westeros.westerlands.GOTEntityWesterlandsSoldier;
import got.common.entity.westeros.westerlands.GOTEntityWesterlandsSoldierArcher;
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
import noname.weapons.RegItem;

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
    public static final int MAX_GRANARY_LEVEL = 50;
    public static int serverTimeOffsetMinutes = 0;
    public static java.util.Set<String> activeWarFortresses = new java.util.HashSet<>();
    public static java.util.Set<String> activeRaidPoints = new java.util.HashSet<>();
    public static void init(File configFolder) {
        config = new ServerConfig(new File(configFolder, "server_config.cfg"));
        CustomResourceConfig.init(configFolder);
        startResourceGeneration();
    }

    public static int getGranaryCapacity(int currentLevel) {
        if (currentLevel < 1) currentLevel = 1;
        if (currentLevel > MAX_GRANARY_LEVEL) currentLevel = MAX_GRANARY_LEVEL;

        return currentLevel * 100;
    }
    public static GOTEntityNPC getFactionSoldier(World world, String factionId, boolean isArcher) {
        if (factionId == null) return null;

        boolean isArryn = factionId.equals("ARRYN");
        boolean isCrownlands = factionId.equals("CROWNLANDS");
        boolean isCrownlandsRed = factionId.equals("KINGSGUARD");
        boolean isDorne = factionId.equals("DORNE");
        boolean isDragonstone = factionId.equals("DRAGONSTONE");
        boolean isIronborn = factionId.equals("IRON_ISLANDS");
        boolean isNorth = factionId.equals("NORTH");
        boolean isReach = factionId.equals("REACH");
        boolean isRiverlands = factionId.equals("RIVERLANDS");
        boolean isStormlands = factionId.equals("STORMLANDS");
        boolean isWesterlands = factionId.equals("WESTERLANDS");

        if (isArcher) {
            if (isArryn) return new GOTEntityArrynSoldierArcher(world);
            if (isCrownlands) return new GOTEntityCrownlandsLevymanArcher(world);
            if (isCrownlandsRed) return new GOTEntityKingsguard(world);
            if (isDorne) return new GOTEntityDorneSoldierArcher(world);
            if (isDragonstone) return new GOTEntityDragonstoneSoldierArcher(world);
            if (isIronborn) return new GOTEntityIronbornSoldierArcher(world);
            if (isNorth) return new GOTEntityNorthSoldierArcher(world);
            if (isReach) return new GOTEntityReachSoldierArcher(world);
            if (isRiverlands) return new GOTEntityRiverlandsSoldierArcher(world);
            if (isStormlands) return new GOTEntityStormlandsSoldierArcher(world);
            if (isWesterlands) return new GOTEntityWesterlandsSoldierArcher(world);
        } else {
            if (isArryn) return new GOTEntityArrynSoldier(world);
            if (isCrownlands) return new GOTEntityCrownlandsLevyman(world);
            if (isCrownlandsRed) return new GOTEntityKingsguard(world);
            if (isDorne) return new GOTEntityDorneSoldier(world);
            if (isDragonstone) return new GOTEntityDragonstoneSoldier(world);
            if (isIronborn) return new GOTEntityIronbornSoldier(world);
            if (isNorth) return new GOTEntityNorthSoldier(world);
            if (isReach) return new GOTEntityReachSoldier(world);
            if (isRiverlands) return new GOTEntityRiverlandsSoldier(world);
            if (isStormlands) return new GOTEntityStormlandsSoldier(world);
            if (isWesterlands) return new GOTEntityWesterlandsSoldier(world);
        }
        return null;
    }

    public static int getGranaryUpgradeCost(int currentLevel) {
        if (currentLevel >= MAX_GRANARY_LEVEL) return -1;

        int levelDifference = currentLevel - 1;
        return (int) (2000 * Math.pow(1.5, levelDifference));
    }
    public static int getMaxFortressLevel(String factionId) {
        if (factionId == null) return 1;
        int max = 1;
        for (FactionStructureSlot s : FactionStructureManager.structureSlots) {
            if (factionId.equals(s.ownerFactionID) && s.category == FactionStructureSlot.StructureCategory.FORTRESS) {
                if (s.level > max) max = s.level;
            }
        }
        return max;
    }
    public static boolean isRaidTimeActive() {
        return isRaidTime;
    }

    public static boolean isRaidTimeNow(FactionStructureSlot slot) {
        if (slot == null) return false;
        // Проверяем временной интервал
        if (!isTimeInInterval(slot.raidTime)) return false;
        // Проверяем частоту дней (raidDays): 0 или 1 = каждый день
        if (slot.raidDays > 1) {
            long epochDay = java.time.LocalDate.now().toEpochDay();
            if (epochDay % slot.raidDays != 0) return false;
        }
        return true;
    }

    public static boolean isTimeInInterval(String interval) {
        try {
            if (interval == null || interval.isEmpty() || !interval.contains("-")) return false;
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
                                    slot.category == FactionStructureSlot.StructureCategory.FORTRESS) {

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
                                            if (slot.category == FactionStructureSlot.StructureCategory.FORTRESS && (variantKey == null || variantKey.isEmpty())) {
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

        boolean isFortressPurchase = slot.category == FactionStructureSlot.StructureCategory.FORTRESS;

        long fortressCount = FactionStructureManager.structureSlots.stream().filter(s -> playerFaction.getID().equals(s.ownerFactionID) && s.category == FactionStructureSlot.StructureCategory.FORTRESS).count();
        long resourcePointCount = FactionStructureManager.structureSlots.stream().filter(s -> playerFaction.getID().equals(s.ownerFactionID) && s.category != FactionStructureSlot.StructureCategory.FORTRESS).count();

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
                .filter(s -> isFortressPurchase ? s.category == FactionStructureSlot.StructureCategory.FORTRESS : s.category != FactionStructureSlot.StructureCategory.FORTRESS)
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

                boolean newHeartPlaced = false;
                int oldHeartX = slot.xCoord;
                int oldHeartY = slot.yCoord;
                int oldHeartZ = slot.zCoord;

                if (slot.canBuild) {
                    String jsonContent = Files.toString(structureFile, Charsets.UTF_8);
                    StructureData structureData = new Gson().fromJson(jsonContent, StructureData.class);

                    int xOffset = structureData.getWidth() / 2;
                    int zOffset = structureData.getDepth() / 2;
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
                    }
                } // End of if (slot.canBuild)

                if (!newHeartPlaced) {
                    TileEntity te = world.getTileEntity(slot.xCoord, slot.yCoord, slot.zCoord);
                    if (te instanceof TileEntityStructureHeart) {
                        ((TileEntityStructureHeart) te).setStructureId(structureId);
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
                slot.destructionCount = config.structureBreakCounts.getOrDefault(1, 1);

                if (isFortressPurchase) {
                    slot.destructionCount = 100;
                    slot.barnLevel = 1;
                    slot.workshopLevel = 1;
                    slot.stableLevel = 1;
                    slot.barracksLevel = 1;
                    slot.barracksCapacity = 10;
                    slot.provisions = 0;
                } else {
                    slot.destructionCount = 100;
                    slot.securityLevel = 0;
                    if (slot.category == FactionStructureSlot.StructureCategory.BARN) {
                        slot.provisions = 0;
                        slot.storedFoodItems = 0;
                    }
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
    public static void handleSubUpgrade(EntityPlayerMP player, String structureId, String subType) {
        Faction playerFaction = PacketMessage.getCurrentFaction(player.getCommandSenderName());
        FactionStructureSlot slot = FactionStructureManager.getStructureById(structureId);

        if (playerFaction == null || slot == null || !playerFaction.getID().equals(slot.ownerFactionID) || slot.category != FactionStructureSlot.StructureCategory.FORTRESS) return;

        if (!playerFaction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_UPGRADE_STRUCTURES)) {
            player.addChatMessage(new ChatComponentText("§cУ вас нет прав на улучшение зданий."));
            return;
        }

        int maxFortress = slot.level;
        long cost = 0;

        if (subType.equals("barn")) {
            int maxLevel = Math.min(MAX_GRANARY_LEVEL, maxFortress * 5);
            if (slot.barnLevel >= maxLevel) { player.addChatMessage(new ChatComponentText("§cМаксимальный уровень амбара для текущей крепости!")); return; }
            cost = getGranaryUpgradeCost(slot.barnLevel);
            if (playerFaction.getTreasury() >= cost) { playerFaction.setTreasury(playerFaction.getTreasury() - cost); slot.barnLevel++; } else { player.addChatMessage(new ChatComponentText("§cНужно " + cost + " монет.")); return; }
        } else if (subType.equals("workshop")) {
            if (slot.workshopLevel >= maxFortress) { player.addChatMessage(new ChatComponentText("§cМаксимальный уровень мастерской для текущей крепости!")); return; }
            cost = (long)(20000 * Math.pow(1.5, slot.workshopLevel - 1));
            if (playerFaction.getTreasury() >= cost) { playerFaction.setTreasury(playerFaction.getTreasury() - cost); slot.workshopLevel++; } else { player.addChatMessage(new ChatComponentText("§cНужно " + cost + " монет.")); return; }
        } else if (subType.equals("stable")) {
            if (slot.stableLevel >= maxFortress) { player.addChatMessage(new ChatComponentText("§cМаксимальный уровень конюшни для текущей крепости!")); return; }
            cost = (long)(20000 * Math.pow(1.5, slot.stableLevel - 1));
            if (playerFaction.getTreasury() >= cost) { playerFaction.setTreasury(playerFaction.getTreasury() - cost); slot.stableLevel++; } else { player.addChatMessage(new ChatComponentText("§cНужно " + cost + " монет.")); return; }
        } else if (subType.equals("barracks")) {
            if (slot.barracksLevel >= maxFortress) { player.addChatMessage(new ChatComponentText("§cМаксимальный уровень казармы для текущей крепости!")); return; }
            cost = (long)(15000 * Math.pow(1.5, slot.barracksLevel - 1));
            if (playerFaction.getTreasury() >= cost) { playerFaction.setTreasury(playerFaction.getTreasury() - cost); slot.barracksLevel++; slot.barracksCapacity = slot.barracksLevel * 10; } else { player.addChatMessage(new ChatComponentText("§cНужно " + cost + " монет.")); return; }
        }

        FactionStructureManager.saveStructureOwnership();
        CoreFaction.saveFactions();
        CoreFaction.brainChannel.sendTo(new brain.factions.network.PacketStructureGUISync(slot.xCoord, slot.yCoord, slot.zCoord, slot), player);
        player.addChatMessage(new ChatComponentText("§aУлучшение " + subType + " прошло успешно!"));
    }
    public static void handleCollect(EntityPlayerMP player, String structureId) {
        Faction playerFaction = PacketMessage.getCurrentFaction(player.getCommandSenderName());
        FactionStructureSlot slot = FactionStructureManager.getStructureById(structureId);

        if (playerFaction != null && slot != null && playerFaction.getID().equals(slot.ownerFactionID)) {
            if (!playerFaction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_INTERACT_RESOURCE_POINTS)) {
                player.addChatMessage(new ChatComponentText("§cУ вас нет прав на взаимодействие с ресурсными точками."));
                return;
            }
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
            if (!playerFaction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_UPGRADE_STRUCTURES)) {
                player.addChatMessage(new ChatComponentText("§cУ вас нет прав на улучшение зданий."));
                return;
            }

            if (playerFaction.isInWarState()) {
                player.addChatMessage(new ChatComponentText("§cНельзя улучшать постройки во время войны!"));
                return;
            }

            boolean isBarn = slot.category == FactionStructureSlot.StructureCategory.BARN;
            boolean isWorkshop = slot.category == FactionStructureSlot.StructureCategory.ENGINEERING_WORKSHOP;
            boolean isStable = slot.category == FactionStructureSlot.StructureCategory.STABLE;
            boolean isFortress = slot.category == FactionStructureSlot.StructureCategory.FORTRESS;

            int maxFortressLevel = getMaxFortressLevel(playerFaction.getID());
            int maxLevel;

            if (isBarn) {
                maxLevel = Math.min(MAX_GRANARY_LEVEL, maxFortressLevel * 5);
            } else if (isWorkshop || isStable) {
                maxLevel = Math.min(10, maxFortressLevel);
            } else if (isFortress) {
                maxLevel = 10;
            } else {
                maxLevel = config.maxStructureLevel;
            }

            if (slot.level < maxLevel) {
                long finalCost;
                if (isBarn) {
                    finalCost = getGranaryUpgradeCost(slot.level);
                } else if (isWorkshop || isStable) {
                    finalCost = (long) (20000 * Math.pow(1.5, slot.level - 1));
                } else if (isFortress) {
                    finalCost = (long) (15000 * Math.pow(1.5, slot.level - 1));
                } else {
                    int baseCost = config.upgradeCost.getOrDefault(slot.level + 1, 5000);
                    finalCost = (long) (baseCost * Math.pow(1.5, slot.level - 1));
                }

                if (playerFaction.getTreasury() >= finalCost) {
                    playerFaction.setTreasury(playerFaction.getTreasury() - finalCost);
                    slot.level++;

                    if (isFortress) {
                        slot.destructionCount = 100 + ((slot.level - 1) * 10);
                        slot.barracksCapacity = slot.level * 10;
                    } else {
                        slot.destructionCount = config.structureBreakCounts.getOrDefault(slot.level, slot.level);
                    }

                    FactionStructureManager.saveStructureOwnership();
                    CoreFaction.saveFactions();
                    CoreFaction.sendAllGui();

                    CoreFaction.brainChannel.sendTo(new brain.factions.network.PacketStructureGUISync(slot.xCoord, slot.yCoord, slot.zCoord, slot), player);

                    player.addChatMessage(new ChatComponentText("§aСтруктура улучшена до уровня " + slot.level + " за " + finalCost));
                } else {
                    player.addChatMessage(new ChatComponentText("§cНедостаточно средств для улучшения. Цена: " + finalCost));
                }
            } else {
                if (isBarn || isWorkshop || isStable) {
                    player.addChatMessage(new ChatComponentText("§cДля дальнейшего улучшения повысьте уровень вашей Крепости! (Макс. доступно: " + maxLevel + ")"));
                } else {
                    player.addChatMessage(new ChatComponentText("§cДостигнут максимальный уровень."));
                }
            }
        }
    }
    public static void handleSiegePurchase(EntityPlayerMP player, String structureId, String weaponType) {
        Faction playerFaction = PacketMessage.getCurrentFaction(player.getCommandSenderName());
        FactionStructureSlot slot = FactionStructureManager.getStructureById(structureId);

        if (playerFaction == null || slot == null || !playerFaction.getID().equals(slot.ownerFactionID)) {
            return;
        }

        boolean isFortress = slot.category == FactionStructureSlot.StructureCategory.FORTRESS;
        if (!isFortress && slot.category != FactionStructureSlot.StructureCategory.ENGINEERING_WORKSHOP) {
            return;
        }

        if (!playerFaction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_BUY_SIEGE_WEAPONS)) {
            player.addChatMessage(new ChatComponentText("§cУ вас нет прав на приобретение осадных орудий."));
            return;
        }

        int activeLevel = isFortress ? slot.workshopLevel : slot.level;
        long cooldownDays = 11 - activeLevel;
        if (cooldownDays < 1) cooldownDays = 1;
        long cooldownMs = cooldownDays * 24 * 60 * 60 * 1000L;

        if (System.currentTimeMillis() - slot.lastSiegePurchaseTime < cooldownMs) {
            long timeLeft = cooldownMs - (System.currentTimeMillis() - slot.lastSiegePurchaseTime);
            long daysLeft = timeLeft / (24 * 60 * 60 * 1000L);
            long hoursLeft = (timeLeft / (60 * 60 * 1000L)) % 24;
            player.addChatMessage(new ChatComponentText(String.format("§cМастерская еще подготавливает материалы. Осталось: %d д. %d ч.", daysLeft, hoursLeft)));
            return;
        }

        long cost = 0;
        net.minecraft.item.Item itemObj = null;

        switch (weaponType) {
            case "ballista":
                cost = 18000;
                itemObj = noname.weapons.RegItem.balistaSpawner;
                break;
            case "ram":
                cost = 40000;
                itemObj = noname.weapons.RegItem.batteringRamSpawner;
                break;
            case "catapult":
                cost = 25000;
                itemObj = noname.weapons.RegItem.catapultSpawner;
                break;
            case "trebuchet":
                cost = 30000;
                itemObj = noname.weapons.RegItem.tribushetSpawner;
                break;
            default:
                return;
        }

        if (itemObj == null) {
            player.addChatMessage(new ChatComponentText("§cПредмет не найден! Проверьте инициализацию RegItem."));
            return;
        }

        if (playerFaction.getTreasury() >= cost) {
            playerFaction.setTreasury(playerFaction.getTreasury() - cost);
            slot.lastSiegePurchaseTime = System.currentTimeMillis();

            ItemStack itemToGive = new ItemStack(itemObj);

            if (!player.inventory.addItemStackToInventory(itemToGive)) {
                player.worldObj.spawnEntityInWorld(new net.minecraft.entity.item.EntityItem(player.worldObj, player.posX, player.posY, player.posZ, itemToGive));
            }
            player.addChatMessage(new ChatComponentText("§aВы успешно приобрели осадное орудие!"));

            FactionStructureManager.saveStructureOwnership();
            CoreFaction.saveFactions();
            CoreFaction.sendAllGui();

            CoreFaction.brainChannel.sendTo(new brain.factions.network.PacketStructureGUISync(slot.xCoord, slot.yCoord, slot.zCoord, slot), player);
        } else {
            player.addChatMessage(new ChatComponentText("§cВ казне недостаточно средств! Требуется: " + cost));
        }
    }

    public static void handleHorsePurchase(EntityPlayerMP player, String structureId) {
        Faction playerFaction = PacketMessage.getCurrentFaction(player.getCommandSenderName());
        FactionStructureSlot slot = FactionStructureManager.getStructureById(structureId);

        if (playerFaction == null || slot == null || !playerFaction.getID().equals(slot.ownerFactionID)) {
            return;
        }

        boolean isFortress = slot.category == FactionStructureSlot.StructureCategory.FORTRESS;
        if (!isFortress && slot.category != FactionStructureSlot.StructureCategory.STABLE) {
            return;
        }

        if (!playerFaction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_USE_TREASURY)) {
            player.addChatMessage(new ChatComponentText("§cУ вас нет прав на покупку из казны."));
            return;
        }

        long cost = 5000;

        if (playerFaction.getTreasury() >= cost) {
            playerFaction.setTreasury(playerFaction.getTreasury() - cost);

            int activeLevel = isFortress ? slot.stableLevel : slot.level;

            double[] healthLevels = {18, 21, 24, 27, 30, 33, 36, 39, 42, 45};
            double[] speedLevels = {0.22, 0.23, 0.25, 0.26, 0.28, 0.29, 0.31, 0.32, 0.33, 0.34};

            int levelIndex = Math.max(0, Math.min(9, activeLevel - 1));
            double targetHealth = healthLevels[levelIndex];
            double targetSpeed = speedLevels[levelIndex];

            net.minecraft.entity.passive.EntityHorse horse = new net.minecraft.entity.passive.EntityHorse(player.worldObj);
            horse.setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);

            net.minecraft.nbt.NBTTagCompound nbt = new net.minecraft.nbt.NBTTagCompound();
            horse.writeEntityToNBT(nbt);

            nbt.setBoolean("Tame", true);

            net.minecraft.item.ItemStack saddleStack = new net.minecraft.item.ItemStack(net.minecraft.init.Items.saddle);
            net.minecraft.nbt.NBTTagCompound saddleTag = new net.minecraft.nbt.NBTTagCompound();
            saddleStack.writeToNBT(saddleTag);
            nbt.setTag("SaddleItem", saddleTag);

            horse.readEntityFromNBT(nbt);

            try { horse.func_152120_b(player.getUniqueID().toString()); } catch(Exception ignored) {}

            horse.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.maxHealth).setBaseValue(targetHealth);
            horse.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.movementSpeed).setBaseValue(targetSpeed);
            horse.setHealth((float)targetHealth);

            player.worldObj.spawnEntityInWorld(horse);
            player.addChatMessage(new ChatComponentText("§aВы успешно приобрели коня (Уровень " + activeLevel + ")!"));

            FactionStructureManager.saveStructureOwnership();
            CoreFaction.saveFactions();
            CoreFaction.sendAllGui();

            CoreFaction.brainChannel.sendTo(new brain.factions.network.PacketStructureGUISync(slot.xCoord, slot.yCoord, slot.zCoord, slot), player);
        } else {
            player.addChatMessage(new ChatComponentText("§cВ казне недостаточно средств! Требуется: " + cost));
        }
    }


    public static void handleSecurityUpgrade(EntityPlayerMP player, String structureId, String currency) {
        Faction playerFaction = PacketMessage.getCurrentFaction(player.getCommandSenderName());
        FactionStructureSlot slot = FactionStructureManager.getStructureById(structureId);

        if (playerFaction == null || slot == null || !playerFaction.getID().equals(slot.ownerFactionID)) return;
        if (slot.category == FactionStructureSlot.StructureCategory.FORTRESS) {
            player.addChatMessage(new net.minecraft.util.ChatComponentText("§cЗащищенность качается только у ресурсных точек."));
            return;
        }

        if (!playerFaction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_UPGRADE_STRUCTURES)) {
            player.addChatMessage(new net.minecraft.util.ChatComponentText("§cУ вас нет прав на улучшение зданий."));
            return;
        }

        if (slot.securityLevel >= 10) {
            player.addChatMessage(new net.minecraft.util.ChatComponentText("§cДостигнут максимальный уровень защищенности (10)."));
            return;
        }

        if (currency.equals("dubloons")) {
            int cost = 5 + (5 * slot.securityLevel);
            net.minecraft.item.Item dublon = (net.minecraft.item.Item) net.minecraft.item.Item.itemRegistry.getObject("got:dublon");
            if (!hasAndRemoveItem(player, dublon, cost)) {
                player.addChatMessage(new net.minecraft.util.ChatComponentText("§cНедостаточно дублонов! Требуется: " + cost));
                return;
            }
        } else if (currency.equals("coins")) {
            long cost = (long) (25000 * Math.pow(2, slot.securityLevel));
            if (playerFaction.getTreasury() < cost) {
                player.addChatMessage(new net.minecraft.util.ChatComponentText("§cНедостаточно средств в казне! Требуется: " + cost));
                return;
            }
            playerFaction.setTreasury(playerFaction.getTreasury() - cost);
        }

        slot.securityLevel++;
        slot.destructionCount += 10;

        FactionStructureManager.saveStructureOwnership();
        CoreFaction.saveFactions();
        CoreFaction.sendAllGui();
        CoreFaction.brainChannel.sendTo(new brain.factions.network.PacketStructureGUISync(slot.xCoord, slot.yCoord, slot.zCoord, slot), player);
        player.addChatMessage(new net.minecraft.util.ChatComponentText("§aУровень защищенности повышен до " + slot.securityLevel + "!"));
    }
    private static boolean hasAndRemoveItem(net.minecraft.entity.player.EntityPlayer player, net.minecraft.item.Item item, int amount) {
        int count = 0;
        for (net.minecraft.item.ItemStack stack : player.inventory.mainInventory) {
            if (stack != null && stack.getItem() == item) count += stack.stackSize;
        }
        if (count < amount) return false;

        int remaining = amount;
        for (int i = 0; i < player.inventory.mainInventory.length; i++) {
            net.minecraft.item.ItemStack stack = player.inventory.mainInventory[i];
            if (stack != null && stack.getItem() == item) {
                if (stack.stackSize > remaining) {
                    stack.stackSize -= remaining;
                    remaining = 0;
                    break;
                } else {
                    remaining -= stack.stackSize;
                    player.inventory.mainInventory[i] = null;
                }
            }
        }
        player.inventory.markDirty();
        return true;
    }
    public static void spawnSecurityNPCs(net.minecraft.world.World world, int x, int y, int z, String factionId, int securityLevel) {
        if (securityLevel <= 0 || world.isRemote) return;

        int totalNPCs = 5 + (securityLevel - 1) * 3;
        boolean isIronborn = factionId != null && factionId.equals("IRON_ISLANDS");

        double cavChance = isIronborn ? 0.0 : 0.10 + (securityLevel - 1) * 0.02;
        double rangedChance = isIronborn ? 0.30 : 0.20;

        for (int i = 0; i < totalNPCs; i++) {
            double rand = Math.random();
            double sX = x + (world.rand.nextDouble() - 0.5) * 12;
            double sZ = z + (world.rand.nextDouble() - 0.5) * 12;
            double sY = world.getTopSolidOrLiquidBlock((int)sX, (int)sZ);

            if (!isIronborn && rand < cavChance) {
                got.common.entity.animal.GOTEntityHorse horse = new got.common.entity.animal.GOTEntityHorse(world);
                horse.setLocationAndAngles(sX, sY, sZ, world.rand.nextFloat() * 360.0F, 0.0F);
                horse.saddleMountForWorldGen();
                horse.setBelongsToNPC(true);

                got.common.entity.other.GOTEntityNPC rider = getFactionSoldier(world, factionId, false);
                if (rider != null) {
                    rider.setLocationAndAngles(sX, sY, sZ, horse.rotationYaw, 0.0F);
                    rider.onSpawnWithEgg(null);
                    rider.createMountToRide();
                    world.spawnEntityInWorld(horse);
                    world.spawnEntityInWorld(rider);
                    rider.mountEntity(horse);
                }
            } else {
                boolean spawnArcher = rand < cavChance + rangedChance;
                got.common.entity.other.GOTEntityNPC unit = getFactionSoldier(world, factionId, spawnArcher);
                if (unit != null) {
                    unit.setLocationAndAngles(sX, sY, sZ, world.rand.nextFloat() * 360.0F, 0.0F);
                    unit.onSpawnWithEgg(null);
                    world.spawnEntityInWorld(unit);
                }
            }
        }
    }
}
