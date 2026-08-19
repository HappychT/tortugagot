package brain.tutorial;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import got.rome.ExtendedPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;

public class TutorialManager {

    public static final TutorialManager INSTANCE = new TutorialManager();

    public static TutorialManager getInstance() {
        return INSTANCE;
    }

    private final Map<UUID, Integer> tutorialTimers = new HashMap<UUID, Integer>();
    private final Map<UUID, Integer> blockTicks = new HashMap<UUID, Integer>();
    private Map<UUID, Integer> tutorialSubSteps = new HashMap<>();
    private Map<UUID, String> currentSubtitles = new HashMap<>();
    private Map<UUID, java.util.List<Integer>> tutorialEntitiesMap = new HashMap<>();
    private Map<UUID, Integer> activeArenas = new HashMap<>();

    public int allocateSlot(UUID playerUUID) {
        if (activeArenas.containsKey(playerUUID)) {
            return activeArenas.get(playerUUID);
        }
        int slot = 0;
        while (activeArenas.containsValue(slot)) {
            slot++;
        }
        activeArenas.put(playerUUID, slot);
        return slot;
    }

    public int getSlot(UUID playerUUID) {
        return activeArenas.containsKey(playerUUID) ? activeArenas.get(playerUUID) : 0;
    }

    public void freeArena(UUID playerUUID, net.minecraft.world.World world) {
        if (!activeArenas.containsKey(playerUUID)) return;
        int slot = activeArenas.get(playerUUID);
        activeArenas.remove(playerUUID);

        int baseX = brain.tutorial.TutorialConfig.banner_start_x + (slot * brain.tutorial.TutorialConfig.banner_interval);
        int y = brain.tutorial.TutorialConfig.banner_start_y;
        int z = brain.tutorial.TutorialConfig.banner_start_z;

        // Clear the box (5x5 interior, meaning 7x7 outer bounds)
        for (int i = -3; i <= 3; i++) {
            for (int k = -3; k <= 3; k++) {
                for (int j = -1; j <= 4; j++) {
                    world.setBlockToAir(baseX + i, y + j, z + k);
                }
            }
        }

        // Kill entities in this area
        net.minecraft.util.AxisAlignedBB aabb = net.minecraft.util.AxisAlignedBB.getBoundingBox(baseX - 4, y - 2, z - 4, baseX + 4, y + 5, z + 4);
        java.util.List<net.minecraft.entity.Entity> entities = world.getEntitiesWithinAABB(net.minecraft.entity.Entity.class, aabb);
        for (net.minecraft.entity.Entity e : entities) {
            if (!(e instanceof net.minecraft.entity.player.EntityPlayer)) {
                e.setDead();
            }
        }
    }

    private void registerTutorialEntity(EntityPlayer player, net.minecraft.entity.Entity entity) {
        entity.getEntityData().setString("TutorialOwner", player.getCommandSenderName());
        java.util.List<Integer> list = tutorialEntitiesMap.get(player.getUniqueID());
        if (list == null) list = new java.util.ArrayList<>();
        list.add(entity.getEntityId());
        tutorialEntitiesMap.put(player.getUniqueID(), list);
        syncState(player);
    }

    private int[] getTutorialEntities(EntityPlayer player) {
        java.util.List<Integer> list = tutorialEntitiesMap.get(player.getUniqueID());
        if (list == null) return new int[0];
        int[] arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
        return arr;
    }

    private void clearTutorialEntities(EntityPlayer player) {
        java.util.List<Integer> list = tutorialEntitiesMap.get(player.getUniqueID());
        if (list != null) {
            for (int id : list) {
                net.minecraft.entity.Entity e = player.worldObj.getEntityByID(id);
                if (e != null) {
                    e.setDead();
                }
            }
            list.clear();
        }
    }

    private TutorialManager() {}

    public static void init() {
        MinecraftForge.EVENT_BUS.register(INSTANCE);
        FMLCommonHandler.instance().bus().register(INSTANCE);
    }

    /**
     * Increments the dodge counter for step 18 and returns the new count.
     */
    public int incrementDodgeCount(EntityPlayer player) {
        UUID uuid = player.getUniqueID();
        int count = tutorialSubSteps.getOrDefault(uuid, 0) + 1;
        tutorialSubSteps.put(uuid, count);
        return count;
    }

    public void startTutorial(EntityPlayer player) {
        ExtendedPlayer ext = ExtendedPlayer.get(player);
        if (ext != null && ext.getTutorialStage() == 0) {
            // Do NOT set stage to 1 yet. Just TP to limbo and open welcome GUI.
            if (!player.worldObj.isRemote && player instanceof net.minecraft.entity.player.EntityPlayerMP) {
                net.minecraft.entity.player.EntityPlayerMP mp = (net.minecraft.entity.player.EntityPlayerMP) player;
                
                if (mp.dimension != brain.tutorial.TutorialConfig.limbo_dimension) {
                    net.minecraft.world.WorldServer targetWorld = mp.mcServer.worldServerForDimension(brain.tutorial.TutorialConfig.limbo_dimension);
                    if (targetWorld != null) {
                        mp.mcServer.getConfigurationManager().transferPlayerToDimension(mp, brain.tutorial.TutorialConfig.limbo_dimension, new brain.tutorial.world.TutorialTeleporter(targetWorld));
                    }
                }
                
                mp.playerNetServerHandler.setPlayerLocation(brain.tutorial.TutorialConfig.limbo_x + 0.5, brain.tutorial.TutorialConfig.limbo_y, brain.tutorial.TutorialConfig.limbo_z + 0.5, mp.rotationYaw, mp.rotationPitch);
                
                got.common.network.GOTPacketHandler.networkWrapper.sendTo(new brain.tutorial.network.GOTPacketTutorialWelcomeOpen(), mp);
            }
        }
    }
    
    public void startCombat(EntityPlayer player) {
        ExtendedPlayer ext = ExtendedPlayer.get(player);
        if (ext != null && ext.getTutorialStage() <= 1) { // Accept 0 or 1
            ext.setTutorialActive(true);
            ext.setTutorialStage(1);
            ext.setTutorialProgress(0);
            syncToClient(player, true, 1, 0, "");
            
            if (!player.worldObj.isRemote && player instanceof net.minecraft.entity.player.EntityPlayerMP) {
                net.minecraft.entity.player.EntityPlayerMP mp = (net.minecraft.entity.player.EntityPlayerMP) player;
                
                if (mp.dimension != 100) {
                    net.minecraft.world.WorldServer targetWorld = mp.mcServer.worldServerForDimension(100);
                    if (targetWorld != null) {
                        mp.mcServer.getConfigurationManager().transferPlayerToDimension(mp, 100, new brain.tutorial.world.TutorialTeleporter(targetWorld));
                    }
                }
                
                net.minecraft.world.World world = mp.worldObj;
                int startX = brain.tutorial.TutorialConfig.stage1_x;
                int y = brain.tutorial.TutorialConfig.stage1_y - 1;
                int startZ = brain.tutorial.TutorialConfig.stage1_z;
                clearTutorialEntities(player);
                
                if (brain.tutorial.TutorialConfig.generatePlatforms) {
                    for (int i = -7; i <= 7; i++) {
                        for (int j = -7; j <= 7; j++) {
                            world.setBlock(startX + i, y, startZ + j, net.minecraft.init.Blocks.glass);
                            world.setBlockToAir(startX + i, y + 1, startZ + j);
                            world.setBlockToAir(startX + i, y + 2, startZ + j);
                        }
                    }
                }
                
                mp.playerNetServerHandler.setPlayerLocation(startX + 0.5, y + 1, startZ + 0.5, player.rotationYaw, player.rotationPitch);
                
                // Spawn 2 drunkards
                spawnDrunkard(player, world, brain.tutorial.TutorialConfig.drunkard1_stage1_x, brain.tutorial.TutorialConfig.drunkard1_stage1_y, brain.tutorial.TutorialConfig.drunkard1_stage1_z);
                spawnDrunkard(player, world, brain.tutorial.TutorialConfig.drunkard2_stage1_x, brain.tutorial.TutorialConfig.drunkard2_stage1_y, brain.tutorial.TutorialConfig.drunkard2_stage1_z);
                
                // Spawn Captain in the corner
                got.common.entity.tutorial.GOTEntityTutorialCaptain captain = new got.common.entity.tutorial.GOTEntityTutorialCaptain(world);
                captain.setPosition(brain.tutorial.TutorialConfig.captain_stage1_x, brain.tutorial.TutorialConfig.captain_stage1_y, brain.tutorial.TutorialConfig.captain_stage1_z);
                captain.setCustomNameTag(brain.tutorial.TutorialTexts.get("entity.captain")); // Капитан
                captain.getEntityData().setBoolean("TutorialCaptain", true);
                captain.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.movementSpeed).setBaseValue(0.0);
                captain.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0);
                captain.tasks.taskEntries.clear();
                captain.targetTasks.taskEntries.clear();
                captain.tasks.addTask(1, new net.minecraft.entity.ai.EntityAIWatchClosest(captain, net.minecraft.entity.player.EntityPlayer.class, 8.0F));
                captain.tasks.addTask(2, new net.minecraft.entity.ai.EntityAILookIdle(captain));
                world.spawnEntityInWorld(captain);
                registerTutorialEntity(player, captain);
                
                tutorialTimers.put(player.getUniqueID(), 0);
                tutorialSubSteps.put(player.getUniqueID(), 0);
            }
        }
    }

    /**
     * Удаляет заголовки и сборы, созданные этим игроком во время этапа 3 (фракции).
     * Вызывается при переходе к этапу 4, чтобы очистить фракцию Тортуга
     * от туториального мусора, который мог бы сбить с толку других игроков.
     */
    private void cleanupStage3FactionData(EntityPlayer player) {
        try {
            brain.factions.Faction faction = brain.factions.network.PacketMessage.getCurrentFaction(player.getCommandSenderName());
            if (faction == null) return;

            String playerName = player.getCommandSenderName();

            // Удаляем все заголовки, созданные этим игроком (ищем по имени игрока как префиксу)
            // Туториальные заголовки игрок создаёт с произвольным именем — удаляем все кроме дефолтных
            // Более безопасно: удалять все заголовки (дефолтные будут пересозданы при initFactions)
            java.util.Map<String, brain.factions.Faction.Title> titles = faction.getTitles();
            java.util.List<String> titlesToRemove = new java.util.ArrayList<>();
            for (java.util.Map.Entry<String, brain.factions.Faction.Title> entry : titles.entrySet()) {
                // Оставляем только базовые системные заголовки фракции
                String titleName = entry.getKey();
                if (!titleName.equals("Участник") && !titleName.equals("Офицер") && !titleName.equals("Лидер")) {
                    titlesToRemove.add(titleName);
                }
            }
            for (String t : titlesToRemove) {
                titles.remove(t);
            }

            // Удаляем все сборы (collection goals)
            faction.getCollectionGoals().clear();

            brain.factions.servers.CoreFaction.saveFactions();
            brain.factions.servers.CoreFaction.sendAllGui();
        } catch (Exception e) {
            // Не прерываем туториал при ошибке очистки
            e.printStackTrace();
        }
    }

    /**
     * Явно активирует атаку всех пьяниц этого игрока.
     * Вызывается по таймеру спустя 5 секунд после начала диалога.
     */
    private void activateDrunkardAttack(EntityPlayer player) {
        java.util.List<Integer> entityIds = tutorialEntitiesMap.get(player.getUniqueID());
        if (entityIds == null) return;
        for (int id : entityIds) {
            net.minecraft.entity.Entity e = player.worldObj.getEntityByID(id);
            if (e instanceof got.common.entity.tutorial.GOTEntityTutorialDrunkard) {
                got.common.entity.tutorial.GOTEntityTutorialDrunkard drunkard =
                        (got.common.entity.tutorial.GOTEntityTutorialDrunkard) e;
                // Снимаем стан если ещё висит
                drunkard.removePotionEffect(net.minecraft.potion.Potion.moveSlowdown.id);
                drunkard.removePotionEffect(net.minecraft.potion.Potion.weakness.id);
                // Принудительно ставим цель — базовый класс проверит что это владелец
                drunkard.setAttackTarget(player);
            }
        }
    }

    private void spawnDrunkard(EntityPlayer player, net.minecraft.world.World world, double x, double y, double z) {
        got.common.entity.tutorial.GOTEntityTutorialDrunkard drunkard = new got.common.entity.tutorial.GOTEntityTutorialDrunkard(world);
        drunkard.setPosition(x, y, z);
        drunkard.setCustomNameTag(brain.tutorial.TutorialTexts.get("entity.drunkard"));
        drunkard.getEntityData().setBoolean("TutorialDrunkard", true);
        drunkard.getEntityData().setInteger("TutorialStun", 80); // 4 seconds of stun
        // Remove weapons to fight with fists
        drunkard.setCurrentItemOrArmor(0, null);
        drunkard.setCurrentItemOrArmor(1, null);
        drunkard.setCurrentItemOrArmor(2, null);
        drunkard.setCurrentItemOrArmor(3, null);
        drunkard.setCurrentItemOrArmor(4, null);
        
        // Stun them for 4 seconds during dialogue
        drunkard.addPotionEffect(new net.minecraft.potion.PotionEffect(net.minecraft.potion.Potion.moveSlowdown.id, 80, 10, true));
        drunkard.addPotionEffect(new net.minecraft.potion.PotionEffect(net.minecraft.potion.Potion.weakness.id, 80, 10, true));
        
        world.spawnEntityInWorld(drunkard);
        registerTutorialEntity(player, drunkard);
    }

    public void advanceStage3(EntityPlayer player) {
        ExtendedPlayer ext = ExtendedPlayer.get(player);
        if (ext != null && ext.isTutorialActive() && ext.getTutorialStage() == 3) {
            int currentStep = ext.getTutorialProgress();

            if (currentStep == 21) {
                got.common.item.other.GOTItemCoin.giveCoins(100, player);
            }

            // Removed forced pledge to tortuga because the client now handles the pledge through the normal network packet
            if (currentStep >= 45) {
                // CLEANUP TORTUGA JUNK
                brain.factions.Faction faction = brain.factions.network.PacketMessage.getCurrentFaction(player.getCommandSenderName());
                if (faction != null) {
                    brain.factions.Faction.PlayerData pData = faction.getPlayers().get(player.getCommandSenderName());
                    if (pData != null) {
                        String playerTitleName = pData.getTitle();
                        if (playerTitleName != null && !playerTitleName.equals("Участник") && !playerTitleName.equals("Лидер")) {
                            faction.getTitles().remove(playerTitleName);
                            pData.setTitle("Участник");
                        }
                    }
                    if (faction.getCollectionGoals() != null) {
                        faction.getCollectionGoals().removeIf(goal -> {
                            if (goal.getHistory() != null && !goal.getHistory().isEmpty()) {
                                return goal.getHistory().get(0).getPlayerName().equals(player.getCommandSenderName());
                            }
                            return false;
                        });
                    }
                    brain.factions.servers.CoreFaction.saveFactions();
                    brain.factions.servers.CoreFaction.sendAllGui();
                }

                if (ext.isTutorialReplay()) {
                    ext.setTutorialReplay(false);
                    completeTutorial(player);
                } else {
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage3.captain_done"));
                    startStage4(player, ext);
                }
            } else {
                ext.setTutorialProgress(currentStep + 1);
                syncState(player);
            }
        }
    }

    public void completeTutorial(EntityPlayer player) {
        clearTutorialEntities(player);
        tutorialEntitiesMap.remove(player.getUniqueID());
        freeArena(player.getUniqueID(), player.worldObj);
        tutorialTimers.remove(player.getUniqueID());
        tutorialSubSteps.remove(player.getUniqueID());
        ExtendedPlayer ext = ExtendedPlayer.get(player);
        if (ext != null) {
            ext.setTutorialActive(false);
            ext.setTutorialStage(-1); // -1 means completed
            ext.setTutorialProgress(0);
            syncToClient(player, false, -1, 0, "");
            
            // Clear inventory
            player.inventory.clearInventory(null, -1);
            player.inventoryContainer.detectAndSendChanges();
            
            // Kick from faction
            brain.factions.Faction faction = brain.factions.network.PacketMessage.getCurrentFaction(player.getCommandSenderName());
            if (faction != null) {
                faction.getPlayers().remove(player.getCommandSenderName());
                got.common.GOTPlayerData pd = got.common.GOTLevelData.getData(player);
                if (pd != null) {
                    pd.revokePledgeFaction(player, true);
                }
                brain.factions.servers.CoreFaction.saveFactions();
                brain.factions.servers.CoreFaction.initFactions();
                brain.factions.servers.CoreFaction.sendAllGui();
                brain.factions.servers.CoreFaction.updatePrefix(player.getCommandSenderName());
            }
            
            if (player instanceof net.minecraft.entity.player.EntityPlayerMP) {
                net.minecraft.entity.player.EntityPlayerMP mp = (net.minecraft.entity.player.EntityPlayerMP) player;
                net.minecraft.world.WorldServer targetWorld = mp.mcServer.worldServerForDimension(got.common.GOTDimension.GAME_OF_THRONES.dimensionID);
                if (targetWorld != null) {
                    if (mp.dimension == 100) {
                        mp.mcServer.getConfigurationManager().transferPlayerToDimension(mp, got.common.GOTDimension.GAME_OF_THRONES.dimensionID, new got.common.world.GOTTeleporter(targetWorld, false));
                    }
                    int x = brain.tutorial.TutorialConfig.end_x;
                    int y = brain.tutorial.TutorialConfig.end_y;
                    int z = brain.tutorial.TutorialConfig.end_z;
                    if (y <= 0) {
                        y = got.GOT.getTrueTopBlock(targetWorld, x, z);
                    }
                    mp.playerNetServerHandler.setPlayerLocation(x + 0.5, y + 1.0, z + 0.5, mp.rotationYaw, mp.rotationPitch);
                }
                
                // Give reward items
                if (brain.tutorial.TutorialConfig.reward_items != null) {
                    for (String itemStr : brain.tutorial.TutorialConfig.reward_items) {
                        try {
                            String[] parts = itemStr.split(":");
                            if (parts.length >= 2) {
                                net.minecraft.item.Item item = (net.minecraft.item.Item) net.minecraft.item.Item.itemRegistry.getObject(parts[0] + ":" + parts[1]);
                                if (item != null) {
                                    int count = parts.length >= 3 ? Integer.parseInt(parts[2]) : 1;
                                    int meta = parts.length >= 4 ? Integer.parseInt(parts[3]) : 0;
                                    mp.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(item, count, meta));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mp.inventoryContainer.detectAndSendChanges();
                }
            }
            
            player.addChatMessage(new net.minecraft.util.ChatComponentText(brain.tutorial.TutorialTexts.get("chat.tutorial_complete")));
        }
    }

    public void syncToClient(EntityPlayer player, boolean isActive, int stage, int progress, String subtitle) {
        if (player instanceof net.minecraft.entity.player.EntityPlayerMP) {
            got.common.network.GOTPacketHandler.networkWrapper.sendTo(
                new brain.tutorial.network.GOTPacketTutorialState(isActive, stage, progress, subtitle, getTutorialEntities(player)),
                (net.minecraft.entity.player.EntityPlayerMP) player
            );
        }
    }

    public void syncState(EntityPlayer player) {
        ExtendedPlayer ext = ExtendedPlayer.get(player);
        if (ext != null) {
            String sub = currentSubtitles.getOrDefault(player.getUniqueID(), "");
            syncToClient(player, ext.isTutorialActive(), ext.getTutorialStage(), ext.getTutorialProgress(), sub);
        }
    }

    public void setSubtitle(EntityPlayer player, String subtitle) {
        currentSubtitles.put(player.getUniqueID(), subtitle);
        syncState(player);
    }

    @SubscribeEvent
    public void onPlayerLogin(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.player.worldObj.isRemote && event.player instanceof net.minecraft.entity.player.EntityPlayerMP) {
            got.common.network.GOTPacketHandler.networkWrapper.sendTo(new brain.tutorial.network.GOTPacketTutorialTexts(brain.tutorial.TutorialTexts.getTexts()), (net.minecraft.entity.player.EntityPlayerMP) event.player);
        }
        
        got.rome.ExtendedPlayer ext = got.rome.ExtendedPlayer.get(event.player);
        if (ext != null) {
            net.minecraft.nbt.NBTTagCompound persisted = event.player.getEntityData().getCompoundTag(net.minecraft.entity.player.EntityPlayer.PERSISTED_NBT_TAG);
            if (ext.getTutorialStage() == 0 && !ext.isTutorialActive() && !persisted.getBoolean("TutorialCompleted")) {
                tutorialTimers.put(event.player.getUniqueID(), -20);
            } else {
                syncState(event.player);
            }
        }
    }
    
    @SubscribeEvent
    public void onPlayerLogout(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event) {
        TutorialQueue.getInstance().removeFromQueue(event.player.getUniqueID());
    }

    @SubscribeEvent
    public void onEntityDeath(net.minecraftforge.event.entity.living.LivingDeathEvent event) {
        if (!event.entity.worldObj.isRemote) {
            if (event.entity instanceof got.common.entity.tutorial.GOTEntityTutorialDrunkard && 
                event.source.getEntity() instanceof EntityPlayer) {
                
                EntityPlayer player = (EntityPlayer) event.source.getEntity();
                ExtendedPlayer ext = ExtendedPlayer.get(player);
                
                if (ext != null && ext.isTutorialActive() && ext.getTutorialStage() == 1) {
                    ext.setTutorialProgress(ext.getTutorialProgress() + 1);
                    syncState(player);
                    
                    if (ext.getTutorialProgress() >= 2) {
                        setSubtitle(player, "");
                        startStage2(player, ext);
                    }
                }
            }
        }
    }
    
    private void startStage2(EntityPlayer player, ExtendedPlayer ext) {
        ext.setTutorialStage(2);
        ext.setTutorialProgress(0);
        syncState(player);
        
        player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(got.common.database.GOTRegistry.bronzeSword, 1, 0));
        
        tutorialTimers.put(player.getUniqueID(), 0);
        tutorialSubSteps.put(player.getUniqueID(), 0);
    }
    
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            TutorialQueue.getInstance().tick();
            
            for (Object obj : net.minecraft.server.MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
                EntityPlayer player = (EntityPlayer) obj;
                ExtendedPlayer ext = ExtendedPlayer.get(player);
                if (ext != null) {
                    if (ext.isTutorialActive() && ext.getTutorialStage() == 7 && ext.getTutorialProgress() == 8) {
                        // Removed blockTicks tracking, using actual server block mechanics now
                    }
                    UUID uuid = player.getUniqueID();
                    if (tutorialTimers.containsKey(uuid)) {
                        int timer = tutorialTimers.get(uuid);
                        timer++;
                        tutorialTimers.put(uuid, timer);

                        if (ext.getTutorialStage() == 0 && !ext.isTutorialActive()) {
                            net.minecraft.nbt.NBTTagCompound persisted = player.getEntityData().getCompoundTag(net.minecraft.entity.player.EntityPlayer.PERSISTED_NBT_TAG);
                            if (timer >= 0 && !persisted.getBoolean("TutorialCompleted")) {
                                startTutorial(player);
                                tutorialTimers.remove(uuid);
                            } else if (persisted.getBoolean("TutorialCompleted")) {
                                tutorialTimers.remove(uuid);
                            }
                            continue;
                        }

                        if (ext.isTutorialActive()) {
                            if (ext.getTutorialStage() == 1) {
                            int subStep = tutorialSubSteps.getOrDefault(uuid, 0);
                            if (subStep == 0 && timer == 10) {
                                setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage1.drunkard1"));
                                tutorialSubSteps.put(uuid, 1);
                            } else if (subStep == 1 && timer == 80) {
                                setSubtitle(player, "");
                                tutorialSubSteps.put(uuid, 2);
                            } else if (subStep == 2 && timer == 100) {
                                // 5 секунд прошло — пьяницы начинают драку
                                activateDrunkardAttack(player);
                            }
                        } else if (ext.getTutorialStage() == 2) {
                            int subStep = tutorialSubSteps.getOrDefault(uuid, 0);
                            if (subStep == 0 && timer == 40) { // 2 seconds
                                setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage2.captain1"));
                                tutorialSubSteps.put(uuid, 1);
                            } else if (subStep == 1 && timer == 100) { // 5 seconds
                                setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage2.captain2"));
                                tutorialSubSteps.put(uuid, 2);
                            } else if (subStep == 2 && timer == 160) { // 8 seconds
                                setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage2.captain3"));
                                startStage3(player, ext);
                                tutorialTimers.remove(uuid);
                                tutorialSubSteps.remove(uuid);
                            }
                        } else if (ext.getTutorialStage() == 4) {
                            if (ext.getTutorialProgress() == 4 && timer == 80) { // 4 seconds
                                advanceStage4(player, 5);
                                tutorialTimers.remove(uuid);
                            }
                        } else if (ext.getTutorialStage() == 6) {
                            if (ext.getTutorialProgress() == 8 && timer == 100) { // 5 seconds after sword forged
                                startStage7(player, ext);
                            }
                        } else if (ext.getTutorialStage() == 7) {
                            int p = ext.getTutorialProgress();
                            if (p == 0 && timer == 100) {
                                advanceStage7(player, 1);
                            } else if (p == 1 && timer == 200) {
                                advanceStage7(player, 2);
                            } else if (p == 2) {
                                // Boosted stamina drain during run phase every tick
                                got.common.handlers.StaminaServerHandler.drainStaminaByPercent(0.18, player);
                                if (timer >= 160) { // 8 seconds
                                    advanceStage7(player, 3);
                                }
                            } else if (p == 3 && timer == 80) {
                                advanceStage7(player, 4);
                            } else if (p == 13) {
                                // Shield craft check: need shieldSpear AND shieldPike
                                boolean hasShieldSpear = false;
                                boolean hasShieldPike = false;
                                for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                                    net.minecraft.item.ItemStack stack = player.inventory.getStackInSlot(i);
                                    if (stack != null) {
                                        if (stack.getItem() instanceof got.common.item.weapon.GOTItemShieldSpear) hasShieldSpear = true;
                                        if (stack.getItem() instanceof got.common.item.weapon.GOTItemShieldPike) hasShieldPike = true;
                                    }
                                }
                                if (hasShieldSpear && hasShieldPike) {
                                    advanceStage7(player, 14);
                                }
                            } else if (p == 14 && timer == 80) { // Delay before block fight 1
                                advanceStage7(player, 15);
                            } else if (p == 19 && timer == 60) { // 3 seconds after done
                                startStage8(player, ext);
                                tutorialTimers.remove(uuid);
                            }
                        } else if (ext.getTutorialStage() == 8) {
                            if (ext.getTutorialProgress() == 2 && timer == 40) { // 2 seconds delay
                                startStage9(player, ext);
                                tutorialTimers.remove(uuid);
                            }
                        } else if (ext.getTutorialStage() == 9) {
                            if (ext.getTutorialProgress() == 1 && timer == 40) { // 2 seconds delay after getting resources
                                ext.setTutorialProgress(2);
                                syncState(player);
                            } else if (ext.getTutorialProgress() == 2 && timer == 70) { // Teleport while blind
                                startStage10(player, ext);
                                tutorialTimers.remove(uuid);
                            }
                        }
                        }
                    }
                }
            }
        }
    }

    public void startStage3(EntityPlayer player, ExtendedPlayer ext) {
        clearTutorialEntities(player);
        ext.setTutorialStage(3);
        ext.setTutorialProgress(0); // 0 = not opened yet
        setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage3.start"));
        syncState(player);

        // Ре-спавним капитана чтобы он не пропадал при clearTutorialEntities
        if (player instanceof net.minecraft.entity.player.EntityPlayerMP) {
            net.minecraft.world.World world = player.worldObj;
            got.common.entity.tutorial.GOTEntityTutorialCaptain captain = new got.common.entity.tutorial.GOTEntityTutorialCaptain(world);
            captain.setPosition(brain.tutorial.TutorialConfig.captain_stage1_x, brain.tutorial.TutorialConfig.captain_stage1_y, brain.tutorial.TutorialConfig.captain_stage1_z);
            captain.setCustomNameTag(brain.tutorial.TutorialTexts.get("entity.captain"));
            captain.getEntityData().setBoolean("TutorialCaptain", true);
            captain.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.movementSpeed).setBaseValue(0.0);
            captain.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0);
            captain.tasks.taskEntries.clear();
            captain.targetTasks.taskEntries.clear();
            captain.tasks.addTask(1, new net.minecraft.entity.ai.EntityAIWatchClosest(captain, net.minecraft.entity.player.EntityPlayer.class, 8.0F));
            captain.tasks.addTask(2, new net.minecraft.entity.ai.EntityAILookIdle(captain));
            world.spawnEntityInWorld(captain);
            registerTutorialEntity(player, captain);
        }
    }

    @SubscribeEvent
    public void onChat(ServerChatEvent event) {
        ExtendedPlayer ext = ExtendedPlayer.get(event.player);
        if (ext != null && ext.isTutorialActive()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onCommand(net.minecraftforge.event.CommandEvent event) {
        if (event.sender instanceof net.minecraft.entity.player.EntityPlayerMP) {
            net.minecraft.entity.player.EntityPlayerMP player = (net.minecraft.entity.player.EntityPlayerMP) event.sender;
            got.rome.ExtendedPlayer ext = got.rome.ExtendedPlayer.get(player);
            if (ext != null && ext.isTutorialActive()) {
                boolean isAdmin = net.minecraft.server.MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile());
                if (!isAdmin) {
                    event.setCanceled(true);
                    player.addChatMessage(new net.minecraft.util.ChatComponentText(brain.tutorial.TutorialTexts.get("chat.commands_blocked")));
                }
            }
        }
    }



    @SubscribeEvent
    public void onEntityAttack(net.minecraftforge.event.entity.player.AttackEntityEvent event) {
        if (event.target.getEntityData().hasKey("TutorialOwner")) {
            String owner = event.target.getEntityData().getString("TutorialOwner");
            if (!owner.equals(event.entityPlayer.getCommandSenderName())) {
                event.setCanceled(true); // Can't hit others' NPCs
                return;
            }
        }
        got.rome.ExtendedPlayer ext = got.rome.ExtendedPlayer.get(event.entityPlayer);
        if (ext != null && ext.isTutorialActive()) {
            if (!event.target.getEntityData().hasKey("TutorialOwner") && !(event.target instanceof got.common.entity.other.GOTEntityBanner)) {
                event.setCanceled(true); // Can't hit normal NPCs in tutorial
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreak(net.minecraftforge.event.world.BlockEvent.BreakEvent event) {
        got.rome.ExtendedPlayer ext = got.rome.ExtendedPlayer.get(event.getPlayer());
        if (ext != null && ext.isTutorialActive()) {
            event.setCanceled(true); // Can't break blocks in tutorial
        }
    }

    @SubscribeEvent
    public void onEntityInteract(net.minecraftforge.event.entity.player.EntityInteractEvent event) {
        if (event.target.getEntityData().hasKey("TutorialOwner")) {
            String owner = event.target.getEntityData().getString("TutorialOwner");
            if (!owner.equals(event.entityPlayer.getCommandSenderName())) {
                event.setCanceled(true); // Can't interact with others' NPCs
                return;
            }
        }
        got.rome.ExtendedPlayer ext = got.rome.ExtendedPlayer.get(event.entityPlayer);
        if (ext != null && ext.isTutorialActive()) {
             if (!event.target.getEntityData().hasKey("TutorialOwner") && !(event.target instanceof got.common.entity.other.GOTEntityBanner) && !(event.target instanceof got.common.entity.tutorial.GOTEntityTutorialNPC)) {
                 event.setCanceled(true); // Can't interact with normal NPCs in tutorial
             }
             
             if (ext.getTutorialStage() == 9 && ext.getTutorialProgress() == 0) {
                 if (event.target instanceof got.common.entity.tutorial.GOTEntityTutorialCaptain) {
                     setSubtitle(event.entityPlayer, brain.tutorial.TutorialTexts.get("subtitle.stage9.captain1"));
                 }
             }
             
             if (ext.getTutorialStage() == 8 && ext.getTutorialProgress() == 1) {
                 if (event.target instanceof got.common.entity.tutorial.GOTEntityTutorialCaptain && event.target.getEntityData().getBoolean("TutorialCaptainEnd")) {
                     setSubtitle(event.entityPlayer, brain.tutorial.TutorialTexts.get("subtitle.stage8.done"));
                     ext.setTutorialProgress(2);
                     tutorialTimers.put(event.entityPlayer.getUniqueID(), 0);
                     syncState(event.entityPlayer);
                     event.setCanceled(true);
                 }
             }
             

        }
    }

    @SubscribeEvent
    public void onLivingAttack(net.minecraftforge.event.entity.living.LivingAttackEvent event) {
        if (event.source.getEntity() instanceof EntityPlayer && !event.entityLiving.worldObj.isRemote) {
            EntityPlayer player = (EntityPlayer) event.source.getEntity();
            ExtendedPlayer ext = ExtendedPlayer.get(player);
            
            // Prevent players from attacking tutorial NPCs that belong to someone else
            if (event.entityLiving instanceof got.common.entity.tutorial.GOTEntityTutorialNPC) {
                String owner = event.entityLiving.getEntityData().getString("TutorialOwner");
                if (owner != null && !owner.isEmpty() && !owner.equals(player.getCommandSenderName())) {
                    event.setCanceled(true);
                    return;
                }
            }
            
            if (ext != null && ext.isTutorialActive()) {
                if (ext.getTutorialStage() == 7 && event.entityLiving instanceof got.common.entity.tutorial.GOTEntityTutorialDummy) {
                    net.minecraft.item.ItemStack held = player.getHeldItem();
                    boolean correct = false;
                    int p = ext.getTutorialProgress();
                    // Step 4 = dagger
                    if (p == 4 && held != null && held.getItem() instanceof got.common.item.weapon.GOTItemDagger) {
                        correct = true;
                    // Step 5 = sword (vanilla iron sword)
                    } else if (p == 5 && held != null && (held.getItem() instanceof got.common.item.weapon.GOTItemSword || held.getItem() == net.minecraft.init.Items.iron_sword)) {
                        correct = true;
                    // Step 6 = axe (one-handed, not battleaxe)
                    } else if (p == 6 && held != null && (held.getItem() instanceof got.common.item.tool.GOTItemAxe || held.getItem() instanceof got.common.item.weapon.GOTItemIronBornAxe || held.getItem() instanceof got.common.item.weapon.GOTItemBattleaxe)) {
                        correct = true;
                    // Step 7 = hammer
                    } else if (p == 7 && held != null && held.getItem() instanceof got.common.item.weapon.GOTItemHammer) {
                        correct = true;
                    // Step 8 = battleaxe (two-handed)
                    } else if (p == 8 && held != null && (held.getItem() instanceof got.common.item.weapon.GOTItemBattleaxe || held.getItem() instanceof got.common.item.weapon.GOTItemPoleaxe)) {
                        correct = true;
                    // Step 9 = spear (melee polearm)
                    } else if (p == 9 && (event.source.getSourceOfDamage() instanceof got.common.entity.other.GOTEntitySpear || (held != null && held.getItem() instanceof got.common.item.weapon.GOTItemSpear))) {
                        correct = true;
                    // Step 10 = pike
                    } else if (p == 10 && held != null && held.getItem() instanceof got.common.item.weapon.GOTItemPike) {
                        correct = true;
                    // Step 11 = bow (arrow)
                    } else if (p == 11 && event.source.isProjectile()) {
                        correct = true;
                    // Step 12 = crossbow (bolt, also EntityArrow subclass)
                    } else if (p == 12 && event.source.isProjectile()) {
                        correct = true;
                    }
                    
                    if (correct) {
                        int hits = tutorialSubSteps.getOrDefault(player.getUniqueID(), 0);
                        if (hits < 1) { // Requires 2 hits (hits=0, hits=1->advance)
                            tutorialSubSteps.put(player.getUniqueID(), hits + 1);
                            event.entityLiving.setHealth(event.entityLiving.getMaxHealth()); // heal it so it doesn't die
                        } else {
                            tutorialSubSteps.put(player.getUniqueID(), 0);
                            event.entityLiving.setDead();
                            brain.tutorial.TutorialManager.getInstance().advanceStage7(player, p + 1);
                        }
                    } else {
                        event.setCanceled(true);
                    }
                    return;
                }
            }
        }
        
        // Entity attacking Player
        if (event.entityLiving instanceof EntityPlayer && !event.entityLiving.worldObj.isRemote) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            ExtendedPlayer ext = ExtendedPlayer.get(player);
            if (ext != null && ext.isTutorialActive()) {
                // Block fights at steps 15, 16, 17
                if (ext.getTutorialStage() == 7 && (ext.getTutorialProgress() == 15 || ext.getTutorialProgress() == 16 || ext.getTutorialProgress() == 17)) {
                    if (event.source.getEntity() instanceof got.common.entity.tutorial.GOTEntityTutorialSailor) {
                        
                        // Check if player holds the CORRECT weapon to block!
                        int p = ext.getTutorialProgress();
                        net.minecraft.item.ItemStack held = player.getHeldItem();
                        boolean correctWeapon = false;
                        if (held != null) {
                            if (p == 15 && (held.getItem() instanceof got.common.item.weapon.GOTItemSword || held.getItem() == net.minecraft.init.Items.iron_sword)) {
                                correctWeapon = true;
                            } else if (p == 16 && held.getItem() instanceof got.common.item.weapon.GOTItemHammer) {
                                correctWeapon = true;
                            } else if (p == 17 && (held.getItem() instanceof got.common.item.weapon.GOTItemShieldSpear || held.getItem() instanceof got.common.item.weapon.GOTItemShieldPike)) {
                                correctWeapon = true;
                            }
                        }
                        
                        if (!correctWeapon) {
                            brain.tutorial.TutorialManager.getInstance().setSubtitle(player, "\u00a7c[\u0411\u043e\u0446\u043c\u0430\u043d] \u0412\u043e\u0437\u044c\u043c\u0438 \u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u043e\u0435 \u043e\u0440\u0443\u0436\u0438\u0435 \u0434\u043b\u044f \u044d\u0442\u043e\u0433\u043e \u0442\u0435\u0441\u0442\u0430!");
                            event.setCanceled(true);
                            return;
                        }

                        boolean isBlocking = player.isBlocking();
                        if (!isBlocking) {
                            try {
                                if (got.common.handlers.BlockServerHandler.INSTANCE.isBlocking(player)) isBlocking = true;
                            } catch (Exception e) {}
                        }
                        if (isBlocking) {
                            boolean blocked = false;
                            try {
                                float[] blockAngles = {
                                        got.common.systems.GOTCoreBlockingSystem.getBlockData(player.getHeldItem().getItem().getClass(), player).getLeftBlockAngle(),
                                        got.common.systems.GOTCoreBlockingSystem.getBlockData(player.getHeldItem().getItem().getClass(), player).getRightBlockAngle()
                                };
                                float rawYaw = player.rotationYaw;
                                float realYaw = (rawYaw + 90) % 360;
                                if (realYaw < 0) realYaw += 360;

                                double dx = event.source.getEntity().posX - player.posX;
                                double dz = event.source.getEntity().posZ - player.posZ;
                                float attackYaw = (float) Math.toDegrees(Math.atan2(dz, dx)) % 360;
                                if (attackYaw < 0) attackYaw += 360;

                                float diff = attackYaw - realYaw;
                                diff = (diff + 360) % 360;

                                blocked = (diff <= blockAngles[1]) || (diff >= 360 - blockAngles[0]);
                            } catch (Exception e) {
                                // Fallback if no item
                            }
                            
                            if (blocked) {
                                event.setCanceled(true);
                                int nextStep = ext.getTutorialProgress() + 1;
                                brain.tutorial.TutorialManager.getInstance().advanceStage7(player, nextStep);
                                event.source.getEntity().setDead(); // kill sailor
                            } else {
                                brain.tutorial.TutorialManager.getInstance().setSubtitle(player, "\u00a7c[\u0411\u043e\u0446\u043c\u0430\u043d] \u0423\u0434\u0430\u0440 \u043f\u0440\u043e\u0448\u0435\u043b! \u041f\u043e\u0432\u0435\u0440\u043d\u0438\u0441\u044c \u043b\u0438\u0446\u043e\u043c \u043a \u043f\u0440\u043e\u0442\u0438\u0432\u043d\u0438\u043a\u0443!");
                            }
                        } else {
                            brain.tutorial.TutorialManager.getInstance().setSubtitle(player, "\u00a7c[\u0411\u043e\u0446\u043c\u0430\u043d] \u041d\u0430\u0436\u043c\u0438 \u0438 \u0443\u0434\u0435\u0440\u0436\u0438\u0432\u0430\u0439 \u041f\u041a\u041c \u0434\u043b\u044f \u0431\u043b\u043e\u043a\u0430!");
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingHurt(net.minecraftforge.event.entity.living.LivingHurtEvent event) {
        if (event.entityLiving instanceof EntityPlayer && !event.entityLiving.worldObj.isRemote) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            ExtendedPlayer ext = ExtendedPlayer.get(player);
            if (ext != null && ext.isTutorialActive()) {
                
                if (event.source.getEntity() instanceof got.common.entity.tutorial.GOTEntityTutorialMutineer || 
                    event.source.getEntity() instanceof got.common.entity.tutorial.GOTEntityTutorialSailor) {
                    event.ammount = 0;
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent event) {
        if (event.phase == cpw.mods.fml.common.gameevent.TickEvent.Phase.END && !event.player.worldObj.isRemote) {
            ExtendedPlayer ext = ExtendedPlayer.get(event.player);
            if (ext != null && ext.isTutorialActive()) {
                if (ext.getTutorialStage() == 5) {
                    if (event.player.openContainer != null && event.player.openContainer != event.player.inventoryContainer) {
                        if (event.player.openContainer instanceof got.common.inventory.GOTContainerTrade || event.player.openContainer instanceof net.minecraft.inventory.ContainerMerchant) {
                            event.player.getEntityData().setBoolean("TutContOpen", true);
                        }
                    } else if (event.player.getEntityData().getBoolean("TutContOpen")) {
                        event.player.getEntityData().setBoolean("TutContOpen", false);
                        if (ext.getTutorialProgress() >= 1) {
                            brain.tutorial.TutorialManager.getInstance().startStage6(event.player, ext);
                        }
                    }
                } else if (ext.getTutorialStage() == 6) {
                    // Stage 6 advancement is handled when the player picks up the item from the anvil output
                }
            }
        }
    }

    public void startStage4(EntityPlayer player, ExtendedPlayer ext) {
        clearTutorialEntities(player);

        // Очистка туториальных данных фракции: удаляем заголовки и сборы,
        // созданные этим игроком во время этапа 3, чтобы они не мешали другим.
        cleanupStage3FactionData(player);

        ext.setTutorialStage(4);
        ext.setTutorialProgress(0);
        syncState(player);

        
        int slot = allocateSlot(player.getUniqueID());
        int baseX = brain.tutorial.TutorialConfig.banner_start_x + (slot * brain.tutorial.TutorialConfig.banner_interval);
        int y = brain.tutorial.TutorialConfig.banner_start_y;
        int z = brain.tutorial.TutorialConfig.banner_start_z;

        net.minecraft.world.World world = player.worldObj;
        if (brain.tutorial.TutorialConfig.generateBannerPlatforms) {
            for (int i = -3; i <= 3; i++) {
                for (int k = -3; k <= 3; k++) {
                    // Floor
                    world.setBlock(baseX + i, y - 1, z + k, net.minecraft.init.Blocks.grass);
                    // Ceiling
                    world.setBlock(baseX + i, y + 4, z + k, net.minecraft.init.Blocks.glowstone);
                    
                    // Walls
                    if (i == -3 || i == 3 || k == -3 || k == 3) {
                        for (int j = 0; j <= 3; j++) {
                            world.setBlock(baseX + i, y + j, z + k, net.minecraft.init.Blocks.stonebrick);
                        }
                    } else {
                        // Interior Air
                        for (int j = 0; j <= 3; j++) {
                            world.setBlockToAir(baseX + i, y + j, z + k);
                        }
                    }
                }
            }
        }
        
        if (player instanceof net.minecraft.entity.player.EntityPlayerMP) {
            player.closeScreen();
            ((net.minecraft.entity.player.EntityPlayerMP) player).playerNetServerHandler.setPlayerLocation(baseX, y, z, player.rotationYaw, player.rotationPitch);
        }
        
        player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(got.common.database.GOTRegistry.blockMetal1, 1, 2));
        player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(got.common.database.GOTRegistry.blockMetal1, 1, 3));
        player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(net.minecraft.init.Blocks.gold_block, 1, 0));
        
        got.common.GOTPlayerData pd = got.common.GOTLevelData.getData(player);
        int bannerMeta = 0;
        if (pd.getPledgeFaction() != null && !pd.getPledgeFaction().factionBanners.isEmpty()) {
            bannerMeta = pd.getPledgeFaction().factionBanners.get(0).bannerID;
        }
        player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(got.common.database.GOTRegistry.banner, 1, bannerMeta));
        
        setSubtitle(player, "");
        
        syncState(player);
    }
    
    public void advanceStage4(EntityPlayer player, int step) {
        ExtendedPlayer ext = ExtendedPlayer.get(player);
        if (ext != null && ext.isTutorialActive() && ext.getTutorialStage() == 4) {
            if (ext.getTutorialProgress() == step - 1) {
                ext.setTutorialProgress(step);
                syncState(player);
                
                if (step == 1) {
                    setSubtitle(player, "");
                } else if (step == 2) {
                    setSubtitle(player, "");
                } else if (step == 3) {
                    setSubtitle(player, "");
                } else if (step == 4) {
                    setSubtitle(player, "");
                    tutorialTimers.put(player.getUniqueID(), 0);
                } else if (step == 5) {
                    setSubtitle(player, "");
                    startStage5(player, ext);
                }
            }
        }
    }
    
    public void startStage5(EntityPlayer player, ExtendedPlayer ext) {
        clearTutorialEntities(player);
        freeArena(player.getUniqueID(), player.worldObj);
        ext.setTutorialStage(5);
        ext.setTutorialProgress(0);
        syncState(player);
        
        if (player instanceof net.minecraft.entity.player.EntityPlayerMP) {
            player.closeScreen();
            ((net.minecraft.entity.player.EntityPlayerMP) player).playerNetServerHandler.setPlayerLocation(brain.tutorial.TutorialConfig.stage6_x, brain.tutorial.TutorialConfig.stage6_y, brain.tutorial.TutorialConfig.stage6_z, player.rotationYaw, player.rotationPitch);
        }
        
        player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(got.common.database.GOTRegistry.bronzeChestplate, 1, 0));
        got.common.item.other.GOTItemCoin.giveCoins(200, player);
        
        net.minecraft.world.World world = player.worldObj;
        got.common.entity.tutorial.GOTEntityTutorialBartender bartender = new got.common.entity.tutorial.GOTEntityTutorialBartender(world);
        bartender.setPosition(brain.tutorial.TutorialConfig.stage6_x + 3, brain.tutorial.TutorialConfig.stage6_y, brain.tutorial.TutorialConfig.stage6_z);
        bartender.setCustomNameTag(brain.tutorial.TutorialTexts.get("entity.bartender"));
        world.spawnEntityInWorld(bartender);
        registerTutorialEntity(player, bartender);
        
        setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage5.bartender"));
    }
    
    public void advanceStage5(EntityPlayer player, int step) {
        ExtendedPlayer ext = ExtendedPlayer.get(player);
        if (ext != null && ext.isTutorialActive() && ext.getTutorialStage() == 5) {
            if (ext.getTutorialProgress() == step - 1) {
                ext.setTutorialProgress(step);
                syncState(player);
                
                if (step == 1) {
                    setSubtitle(player, "");
                }
            }
        }
    }
    
    public void startStage6(EntityPlayer player, ExtendedPlayer ext) {
        clearTutorialEntities(player);
        ext.setTutorialStage(6);
        ext.setTutorialProgress(0); // 0 = ждём открытия бронника
        syncState(player);
        
        if (player instanceof net.minecraft.entity.player.EntityPlayerMP) {
            player.closeScreen();
            ((net.minecraft.entity.player.EntityPlayerMP) player).playerNetServerHandler.setPlayerLocation(brain.tutorial.TutorialConfig.stage6_x, brain.tutorial.TutorialConfig.stage6_y, brain.tutorial.TutorialConfig.stage6_z, player.rotationYaw, player.rotationPitch);
        }
        
        // Выдаём: бронзовый нагрудник и меч для перековки, ресурсы для разблокировки слота
        player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(got.common.database.GOTRegistry.bronzeChestplate, 1, 0));
        player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(got.common.database.GOTRegistry.bronzeIngot, 5, 0));
        player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(net.minecraft.init.Blocks.iron_block, 20, 0));
        player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(net.minecraft.init.Blocks.coal_block, 20, 0));
        player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(got.common.database.GOTRegistry.alloySteelIngot, 5, 0));
        net.minecraft.item.ItemStack scroll = new net.minecraft.item.ItemStack(got.common.database.GOTRegistry.smithScroll, 1, 0);
        got.common.item.other.GOTItemModifierTemplate.setModifier(scroll, got.common.enchant.GOTEnchantment.strong1);
        player.inventory.addItemStackToInventory(scroll);
        got.common.item.other.GOTItemCoin.giveCoins(15000, player);
        
        net.minecraft.world.World world = player.worldObj;
        
        // Убрали декоративную наковальню, чтобы игрок не кликал по ней случайно
        if (brain.tutorial.TutorialConfig.generatePlatforms) {
            world.setBlockToAir(brain.tutorial.TutorialConfig.mwsmith_stage6_x, brain.tutorial.TutorialConfig.mwsmith_stage6_y, brain.tutorial.TutorialConfig.mwsmith_stage6_z + 1);
        }
        
        // Спавним бронника-туториального NPC
        got.common.entity.tutorial.GOTEntityTutorialArmorsmith armorsmith = new got.common.entity.tutorial.GOTEntityTutorialArmorsmith(world);
        armorsmith.setPosition(
            brain.tutorial.TutorialConfig.armorsmith_stage6_x,
            brain.tutorial.TutorialConfig.armorsmith_stage6_y,
            brain.tutorial.TutorialConfig.armorsmith_stage6_z
        );
        armorsmith.setCustomNameTag(brain.tutorial.TutorialTexts.get("entity.armorsmith"));
        armorsmith.getEntityData().setString("TutorialOwner", player.getCommandSenderName());
        world.spawnEntityInWorld(armorsmith);
        registerTutorialEntity(player, armorsmith);
        
        
        setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage6.start"));
        syncState(player);

    }
    
    public void advanceStage6(EntityPlayer player, int step) {
        ExtendedPlayer ext = ExtendedPlayer.get(player);
        if (ext != null && ext.isTutorialActive() && ext.getTutorialStage() == 6) {
            if (ext.getTutorialProgress() < step) {
                ext.setTutorialProgress(step);
                syncState(player);
                if (step == 1) {
                    // Бронник открыт: подсказка — разблокировать чар, положить нагрудник
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage6.armorsmith_open"));
                } else if (step == 2) {
                    // Чар выбран: подсказка — положить нагрудник в центральный слот
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage6.armorsmith_enchant"));
                } else if (step == 3) {
                    // Нагрудник вложен: подсказка — нажать перековка
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage6.armorsmith_forge"));
                } else if (step == 4) {
                    // Следующий подпункт: ставим наковальню
                    net.minecraft.world.World world = player.worldObj;
                    if (!world.isRemote) {
                        world.setBlock(
                            brain.tutorial.TutorialConfig.mwsmith_stage6_x,
                            brain.tutorial.TutorialConfig.mwsmith_stage6_y,
                            brain.tutorial.TutorialConfig.mwsmith_stage6_z,
                            net.minecraft.init.Blocks.anvil, 1, 3
                        );
                        
                        net.minecraft.item.ItemStack scroll = new net.minecraft.item.ItemStack(got.common.database.GOTRegistry.smithScroll);
                        got.common.item.other.GOTItemModifierTemplate.setModifier(scroll, got.common.enchant.GOTEnchantment.strong1);
                        player.inventory.addItemStackToInventory(scroll);
                        player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(got.common.database.GOTRegistry.alloySteelIngot, 25));
                        if (player instanceof net.minecraft.entity.player.EntityPlayerMP) {
                            ((net.minecraft.entity.player.EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
                        }
                    }
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage6.anvil_start"));
                } else if (step == 5) {
                    // Меч видно: подсказка — выбрать чар для меча
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage6.anvil_open"));
                } else if (step == 6) {
                    // Чар выбран: положить меч
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage6.anvil_enchant"));
                } else if (step == 7) {
                    // Меч вложен: нажать перековка
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage6.anvil_forge"));
                } else if (step == 8) {
                    // Готово!
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage6.done"));
                    tutorialTimers.put(player.getUniqueID(), 0);
                }
            }
        }
    }
    
    public void startStage7(EntityPlayer player, ExtendedPlayer ext) {
        clearTutorialEntities(player);
        ext.setTutorialStage(7);
        // Progress map:
        // 0=health/armor/food, 1=stamina_info, 2=run(8s forced drain), 3=regen_info,
        // 4=dagger_dummy, 5=sword_dummy, 6=axe_dummy, 7=hammer_dummy, 8=battleaxe_dummy,
        // 9=spear_dummy, 10=pike_dummy, 11=bow_dummy, 12=crossbow_dummy,
        // 13=shield_craft, 14=block_info, 15=block_fight1(sword), 16=block_fight2(hammer), 17=block_fight3(shieldspear),
        // 18=dodge_info(x3), 19=done
        ext.setTutorialProgress(0);
        syncState(player);
        tutorialTimers.put(player.getUniqueID(), 0);
        tutorialSubSteps.put(player.getUniqueID(), 0);
        
        net.minecraft.world.World world = player.worldObj;
        if (brain.tutorial.TutorialConfig.generatePlatforms) {
            for (int i = -8; i <= 8; i++) {
                for (int j = -8; j <= 8; j++) {
                    world.setBlock(brain.tutorial.TutorialConfig.stage7_x + i, brain.tutorial.TutorialConfig.stage7_y - 1, brain.tutorial.TutorialConfig.stage7_z + j, net.minecraft.init.Blocks.planks);
                    world.setBlockToAir(brain.tutorial.TutorialConfig.stage7_x + i, brain.tutorial.TutorialConfig.stage7_y, brain.tutorial.TutorialConfig.stage7_z + j);
                    world.setBlockToAir(brain.tutorial.TutorialConfig.stage7_x + i, brain.tutorial.TutorialConfig.stage7_y + 1, brain.tutorial.TutorialConfig.stage7_z + j);
                }
            }
        }
        
        if (player instanceof net.minecraft.entity.player.EntityPlayerMP) {
            player.closeScreen();
            ((net.minecraft.entity.player.EntityPlayerMP) player).playerNetServerHandler.setPlayerLocation(brain.tutorial.TutorialConfig.stage7_x, brain.tutorial.TutorialConfig.stage7_y, brain.tutorial.TutorialConfig.stage7_z, player.rotationYaw, player.rotationPitch);
        }
        
        setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage7.start"));
        
        syncState(player);
    }
    
    /**
     * Spawns a dummy for a given weapon step. Steps 4-12 = weapon dummies.
     * Steps 11 (bow) and 12 (crossbow) get a moving dummy.
     * HP is set to 15 so damage is visible.
     */
    public void spawnDummyForStage7(EntityPlayer player, int step) {
        net.minecraft.world.World world = player.worldObj;
        got.common.entity.tutorial.GOTEntityTutorialDummy dummy = new got.common.entity.tutorial.GOTEntityTutorialDummy(world);
        
        // Use dummy1 pos for all weapon dummies — cleared between each step
        dummy.setPosition(brain.tutorial.TutorialConfig.dummy1_stage7_x, brain.tutorial.TutorialConfig.dummy1_stage7_y, brain.tutorial.TutorialConfig.dummy1_stage7_z);
        
        boolean moving = (step == 11 || step == 12); // bow and crossbow get moving dummies
        if (moving) {
            dummy.setPosition(brain.tutorial.TutorialConfig.dummy3_stage7_x, brain.tutorial.TutorialConfig.dummy3_stage7_y, brain.tutorial.TutorialConfig.dummy3_stage7_z);
        }
        
        dummy.setCustomNameTag(brain.tutorial.TutorialTexts.get("entity.dummy"));
        dummy.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.movementSpeed).setBaseValue(moving ? 0.15D : 0.0D);
        dummy.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.maxHealth).setBaseValue(15.0D);
        dummy.setHealth(15.0F);
        dummy.setCurrentItemOrArmor(4, new net.minecraft.item.ItemStack(net.minecraft.init.Blocks.pumpkin));
        dummy.setCurrentItemOrArmor(3, new net.minecraft.item.ItemStack(net.minecraft.init.Items.leather_chestplate));
        dummy.setCurrentItemOrArmor(2, new net.minecraft.item.ItemStack(net.minecraft.init.Items.leather_leggings));
        dummy.setCurrentItemOrArmor(1, new net.minecraft.item.ItemStack(net.minecraft.init.Items.leather_boots));
        if (moving) {
            dummy.getEntityData().setBoolean("TutorialDummyMoving", true);
        }
        world.spawnEntityInWorld(dummy);
        registerTutorialEntity(player, dummy);
    }

    public void advanceStage7(EntityPlayer player, int step) {
        ExtendedPlayer ext = ExtendedPlayer.get(player);
        if (ext != null && ext.isTutorialActive() && ext.getTutorialStage() == 7) {
            if (ext.getTutorialProgress() < step) {
                ext.setTutorialProgress(step);
                syncState(player);
                
                // Clear tutorial entities between weapon dummies (steps 4-12)
                if (step >= 4 && step <= 12) {
                    // Kill previous dummy before spawning next
                    java.util.List<Integer> entities = tutorialEntitiesMap.getOrDefault(player.getUniqueID(), new java.util.ArrayList<>());
                    for (int id : entities) {
                        net.minecraft.entity.Entity e = player.worldObj.getEntityByID(id);
                        if (e instanceof got.common.entity.tutorial.GOTEntityTutorialDummy) {
                            e.setDead();
                        }
                    }
                    entities.removeIf(id -> {
                        net.minecraft.entity.Entity e = player.worldObj.getEntityByID(id);
                        return e == null || e.isDead;
                    });
                    tutorialEntitiesMap.put(player.getUniqueID(), entities);
                }
                // Kill sailors between block fights (steps 15-17)
                if (step >= 15 && step <= 17) {
                    java.util.List<Integer> entities = tutorialEntitiesMap.getOrDefault(player.getUniqueID(), new java.util.ArrayList<>());
                    for (int id : entities) {
                        net.minecraft.entity.Entity e = player.worldObj.getEntityByID(id);
                        if (e instanceof got.common.entity.tutorial.GOTEntityTutorialSailor) {
                            e.setDead();
                        }
                    }
                    entities.removeIf(id -> {
                        net.minecraft.entity.Entity e = player.worldObj.getEntityByID(id);
                        return e == null || e.isDead;
                    });
                    tutorialEntitiesMap.put(player.getUniqueID(), entities);
                }
                
                if (step == 1) {
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage7.stamina_info"));
                } else if (step == 2) {
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage7.run_info"));
                    tutorialTimers.put(player.getUniqueID(), 0);
                    // Restore stamina first so drain is visible
                    ExtendedPlayer.get(player).setStamina(got.common.handlers.StaminaServerHandler.MAX_STAMINA);
                    got.common.network.base.PacketDispatcher.sendTo(new got.common.network.serverToClient.PacketSendStamina(got.common.handlers.StaminaServerHandler.MAX_STAMINA), (net.minecraft.entity.player.EntityPlayerMP) player);
                } else if (step == 3) {
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage7.stamina_regen"));
                    tutorialTimers.put(player.getUniqueID(), 0);
                } else if (step == 4) {
                    for (int i = 0; i < player.inventory.mainInventory.length; i++) {
                        player.inventory.mainInventory[i] = null;
                    }
                    if (player instanceof net.minecraft.entity.player.EntityPlayerMP) {
                        ((net.minecraft.entity.player.EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
                    }
                    // Give all 9 weapon types + ammo
                    player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(got.common.database.GOTRegistry.ironDagger, 1, 0));
                    player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(net.minecraft.init.Items.iron_sword, 1, 0));
                    player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(got.common.database.GOTRegistry.ironBattleaxe, 1, 0));
                    player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(got.common.database.GOTRegistry.westerosHammer, 1, 0));
                    player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(got.common.database.GOTRegistry.wildlingPolearm, 1, 0));
                    player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(got.common.database.GOTRegistry.ironSpear, 1, 0));
                    player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(got.common.database.GOTRegistry.ironPike, 1, 0));
                    player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(net.minecraft.init.Items.bow, 1, 0));
                    player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(got.common.database.GOTRegistry.bronzeCrossbow, 1, 0));
                    player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(net.minecraft.init.Items.arrow, 64, 0));
                    player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(got.common.database.GOTRegistry.crossbowBolt, 64, 0));
                    
                    net.minecraft.world.World world = player.worldObj;
                    got.common.entity.tutorial.GOTEntityTutorialBoatswain boatswain = new got.common.entity.tutorial.GOTEntityTutorialBoatswain(world);
                    boatswain.setPosition(brain.tutorial.TutorialConfig.boatswain_stage7_x, brain.tutorial.TutorialConfig.boatswain_stage7_y, brain.tutorial.TutorialConfig.boatswain_stage7_z);
                    boatswain.setCustomNameTag(brain.tutorial.TutorialTexts.get("entity.boatswain"));
                    world.spawnEntityInWorld(boatswain);
                    registerTutorialEntity(player, boatswain);
                    
                    spawnDummyForStage7(player, 4);
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage7.dagger_hit"));
                } else if (step == 5) {
                    spawnDummyForStage7(player, 5);
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage7.sword_hit"));
                } else if (step == 6) {
                    spawnDummyForStage7(player, 6);
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage7.axe_hit"));
                } else if (step == 7) {
                    spawnDummyForStage7(player, 7);
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage7.hammer_hit"));
                } else if (step == 8) {
                    spawnDummyForStage7(player, 8);
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage7.battleaxe_hit"));
                } else if (step == 9) {
                    spawnDummyForStage7(player, 9);
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage7.spear_hit"));
                } else if (step == 10) {
                    spawnDummyForStage7(player, 10);
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage7.pike_hit"));
                } else if (step == 11) {
                    spawnDummyForStage7(player, 11);
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage7.bow_hit"));
                } else if (step == 12) {
                    spawnDummyForStage7(player, 12);
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage7.crossbow_hit"));
                } else if (step == 13) {
                    // Shield craft: give shields
                    player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(got.common.database.GOTRegistry.shield, 2, 0));
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage7.shield_craft"));
                } else if (step == 14) {
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage7.block_info"));
                    tutorialTimers.put(player.getUniqueID(), 0);
                } else if (step == 15) {
                    // Block fight 1: sailor with sword
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage7.block_hold"));
                    net.minecraft.world.World world = player.worldObj;
                    got.common.entity.tutorial.GOTEntityTutorialSailor sailor = new got.common.entity.tutorial.GOTEntityTutorialSailor(world);
                    sailor.setPosition(brain.tutorial.TutorialConfig.sailor_stage7_x, brain.tutorial.TutorialConfig.sailor_stage7_y, brain.tutorial.TutorialConfig.sailor_stage7_z);
                    sailor.setCustomNameTag(brain.tutorial.TutorialTexts.get("entity.sailor"));
                    sailor.setCurrentItemOrArmor(0, new net.minecraft.item.ItemStack(net.minecraft.init.Items.iron_sword));
                    sailor.setAttackTarget(player);
                    world.spawnEntityInWorld(sailor);
                    registerTutorialEntity(player, sailor);
                } else if (step == 16) {
                    // Block fight 2: sailor with sword
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage7.block_hold2"));
                    net.minecraft.world.World world = player.worldObj;
                    got.common.entity.tutorial.GOTEntityTutorialSailor sailor = new got.common.entity.tutorial.GOTEntityTutorialSailor(world);
                    sailor.setPosition(brain.tutorial.TutorialConfig.sailor_stage7_x, brain.tutorial.TutorialConfig.sailor_stage7_y, brain.tutorial.TutorialConfig.sailor_stage7_z);
                    sailor.setCustomNameTag(brain.tutorial.TutorialTexts.get("entity.sailor"));
                    sailor.setCurrentItemOrArmor(0, new net.minecraft.item.ItemStack(net.minecraft.init.Items.iron_sword));
                    sailor.setAttackTarget(player);
                    world.spawnEntityInWorld(sailor);
                    registerTutorialEntity(player, sailor);
                } else if (step == 17) {
                    // Block fight 3: sailor with sword
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage7.block_hold3"));
                    net.minecraft.world.World world = player.worldObj;
                    got.common.entity.tutorial.GOTEntityTutorialSailor sailor = new got.common.entity.tutorial.GOTEntityTutorialSailor(world);
                    sailor.setPosition(brain.tutorial.TutorialConfig.sailor_stage7_x, brain.tutorial.TutorialConfig.sailor_stage7_y, brain.tutorial.TutorialConfig.sailor_stage7_z);
                    sailor.setCustomNameTag(brain.tutorial.TutorialTexts.get("entity.sailor"));
                    sailor.setCurrentItemOrArmor(0, new net.minecraft.item.ItemStack(net.minecraft.init.Items.iron_sword));
                    sailor.setAttackTarget(player);
                    world.spawnEntityInWorld(sailor);
                    registerTutorialEntity(player, sailor);
                } else if (step == 18) {
                    // Dodge: require 3 dodges
                    tutorialSubSteps.put(player.getUniqueID(), 0);
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage7.dodge_info"));
                } else if (step == 19) {
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage7.done"));
                    tutorialTimers.put(player.getUniqueID(), 0);
                }
            }
        }
    }

    
    @SubscribeEvent
    public void onLivingDeath(net.minecraftforge.event.entity.living.LivingDeathEvent event) {
        if (!event.entityLiving.worldObj.isRemote) {
            if (event.source.getEntity() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.source.getEntity();
                ExtendedPlayer ext = ExtendedPlayer.get(player);
                if (ext != null && ext.isTutorialActive()) {
                    if (ext.getTutorialStage() == 8) {
                        if (event.entityLiving instanceof got.common.entity.tutorial.GOTEntityTutorialMutineer) {
                            brain.tutorial.TutorialManager.getInstance().advanceStage8(player, ext.getTutorialProgress() + 1);
                        }
                    }
                }
            }
            if (event.entityLiving instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.entityLiving;
                ExtendedPlayer ext = ExtendedPlayer.get(player);
                if (ext != null && ext.isTutorialActive() && ext.getTutorialStage() == 10) {
                    got.common.GOTPlayerData pd = got.common.GOTLevelData.getData(player);
                    pd.setPledgeFaction(null);
                    
                    ext.setTutorialActive(false);
                    ext.setTutorialStage(0);
                    ext.setTutorialProgress(0);
                    brain.tutorial.TutorialManager.getInstance().syncState(player);
                    
                    net.minecraft.nbt.NBTTagCompound persisted = player.getEntityData().getCompoundTag(net.minecraft.entity.player.EntityPlayer.PERSISTED_NBT_TAG);
                    if (!player.getEntityData().hasKey(net.minecraft.entity.player.EntityPlayer.PERSISTED_NBT_TAG)) {
                        player.getEntityData().setTag(net.minecraft.entity.player.EntityPlayer.PERSISTED_NBT_TAG, persisted);
                    }
                    persisted.setBoolean("TutorialRewardsPending", true);
                }
            }
        }
    }

    public void startStage8(EntityPlayer player, ExtendedPlayer ext) {
        clearTutorialEntities(player);
        ext.setTutorialStage(8);
        ext.setTutorialProgress(0); // number of killed mutineers
        syncState(player);
        
        net.minecraft.world.World world = player.worldObj;
        if (brain.tutorial.TutorialConfig.generatePlatforms) {
            for (int i = -10; i <= 10; i++) {
                for (int j = -10; j <= 10; j++) {
                    world.setBlock(brain.tutorial.TutorialConfig.stage8_x + i, brain.tutorial.TutorialConfig.stage8_y - 1, brain.tutorial.TutorialConfig.stage8_z + j, net.minecraft.init.Blocks.stonebrick);
                    world.setBlockToAir(brain.tutorial.TutorialConfig.stage8_x + i, brain.tutorial.TutorialConfig.stage8_y, brain.tutorial.TutorialConfig.stage8_z + j);
                    world.setBlockToAir(brain.tutorial.TutorialConfig.stage8_x + i, brain.tutorial.TutorialConfig.stage8_y + 1, brain.tutorial.TutorialConfig.stage8_z + j);
                }
            }
        }
        
        if (player instanceof net.minecraft.entity.player.EntityPlayerMP) {
            player.closeScreen();
            ((net.minecraft.entity.player.EntityPlayerMP) player).playerNetServerHandler.setPlayerLocation(brain.tutorial.TutorialConfig.stage8_x, brain.tutorial.TutorialConfig.stage8_y, brain.tutorial.TutorialConfig.stage8_z, player.rotationYaw, player.rotationPitch);
        }
        
        got.common.entity.tutorial.GOTEntityTutorialMutineer m1 = new got.common.entity.tutorial.GOTEntityTutorialMutineer(world);
        m1.setPosition(brain.tutorial.TutorialConfig.mutineer1_stage8_x, brain.tutorial.TutorialConfig.mutineer1_stage8_y, brain.tutorial.TutorialConfig.mutineer1_stage8_z);
        m1.setCustomNameTag(brain.tutorial.TutorialTexts.get("entity.mutineer"));
        m1.setCurrentItemOrArmor(0, new net.minecraft.item.ItemStack(net.minecraft.init.Items.wooden_sword));
        m1.setAttackTarget(player);
        world.spawnEntityInWorld(m1);
        registerTutorialEntity(player, m1);

        got.common.entity.tutorial.GOTEntityTutorialMutineer m2 = new got.common.entity.tutorial.GOTEntityTutorialMutineer(world);
        m2.setPosition(brain.tutorial.TutorialConfig.mutineer2_stage8_x, brain.tutorial.TutorialConfig.mutineer2_stage8_y, brain.tutorial.TutorialConfig.mutineer2_stage8_z);
        m2.setCustomNameTag(brain.tutorial.TutorialTexts.get("entity.mutineer"));
        m2.setCurrentItemOrArmor(0, new net.minecraft.item.ItemStack(net.minecraft.init.Items.wooden_sword));
        m2.setAttackTarget(player);
        world.spawnEntityInWorld(m2);
        registerTutorialEntity(player, m2);

        got.common.entity.tutorial.GOTEntityTutorialMutineer m3 = new got.common.entity.tutorial.GOTEntityTutorialMutineer(world);
        m3.setPosition(brain.tutorial.TutorialConfig.mutineer3_stage8_x, brain.tutorial.TutorialConfig.mutineer3_stage8_y, brain.tutorial.TutorialConfig.mutineer3_stage8_z);
        m3.setCustomNameTag(brain.tutorial.TutorialTexts.get("entity.mutineer"));
        m3.setCurrentItemOrArmor(0, new net.minecraft.item.ItemStack(net.minecraft.init.Items.wooden_sword));
        m3.setAttackTarget(player);
        world.spawnEntityInWorld(m3);
        registerTutorialEntity(player, m3);

        // m4 — лучник с луком и стрелами
        got.common.entity.tutorial.GOTEntityTutorialMutineer m4 = new got.common.entity.tutorial.GOTEntityTutorialMutineer(world);
        m4.setPosition(brain.tutorial.TutorialConfig.mutineer4_stage8_x, brain.tutorial.TutorialConfig.mutineer4_stage8_y, brain.tutorial.TutorialConfig.mutineer4_stage8_z);
        m4.setCustomNameTag(brain.tutorial.TutorialTexts.get("entity.mutineer"));
        m4.setCurrentItemOrArmor(0, new net.minecraft.item.ItemStack(net.minecraft.init.Items.bow));
        m4.setCurrentItemOrArmor(1, new net.minecraft.item.ItemStack(net.minecraft.init.Items.arrow, 32));
        m4.setAttackTarget(player);
        world.spawnEntityInWorld(m4);
        registerTutorialEntity(player, m4);

        got.common.entity.tutorial.GOTEntityTutorialMutineer m5 = new got.common.entity.tutorial.GOTEntityTutorialMutineer(world);
        m5.setPosition(brain.tutorial.TutorialConfig.mutineer5_stage8_x, brain.tutorial.TutorialConfig.mutineer5_stage8_y, brain.tutorial.TutorialConfig.mutineer5_stage8_z);
        m5.setCustomNameTag(brain.tutorial.TutorialTexts.get("entity.mutineer"));
        m5.setCurrentItemOrArmor(0, new net.minecraft.item.ItemStack(net.minecraft.init.Items.wooden_sword));
        m5.setAttackTarget(player);
        world.spawnEntityInWorld(m5);
        registerTutorialEntity(player, m5);

        setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage8.start"));
        syncState(player);
    }
    
    public void advanceStage8(EntityPlayer player, int count) {
        ExtendedPlayer ext = ExtendedPlayer.get(player);
        if (ext != null && ext.isTutorialActive() && ext.getTutorialStage() == 8) {
            ext.setTutorialProgress(count);
            syncState(player);
            setSubtitle(player, String.format(brain.tutorial.TutorialTexts.get("subtitle.stage8.progress"), count));
            if (count >= 5) {
                clearTutorialEntities(player);
                ext.setTutorialProgress(1); // Wait for Captain interaction
                syncState(player);
                
                net.minecraft.world.World world = player.worldObj;
                got.common.entity.tutorial.GOTEntityTutorialCaptain captain = new got.common.entity.tutorial.GOTEntityTutorialCaptain(world);
                captain.setPosition(brain.tutorial.TutorialConfig.captain_stage8_x, brain.tutorial.TutorialConfig.captain_stage8_y, brain.tutorial.TutorialConfig.captain_stage8_z);
                captain.setCustomNameTag(brain.tutorial.TutorialTexts.get("entity.captain")); // Капитан
                captain.getEntityData().setBoolean("TutorialCaptainEnd", true);
                world.spawnEntityInWorld(captain);
                registerTutorialEntity(player, captain);
                
                setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage8.end"));
            }
        }
    }
    
    @SubscribeEvent
    public void onPlayerInteract(net.minecraftforge.event.entity.player.PlayerInteractEvent event) {
        ExtendedPlayer ext = ExtendedPlayer.get(event.entityPlayer);
        if (ext != null && ext.isTutorialActive() && ext.getTutorialStage() == 9) {
            // Stage 9 interaction is handled in BlockStructureHeart via GUI
        }
    }

    public void startStage9(EntityPlayer player, ExtendedPlayer ext) {
        clearTutorialEntities(player);
        ext.setTutorialStage(9);
        ext.setTutorialProgress(0); // 0 = wait for resources
        syncState(player);
        
        net.minecraft.world.World world = player.worldObj;
        if (brain.tutorial.TutorialConfig.generatePlatforms) {
            for (int i = -3; i <= 3; i++) {
                for (int j = -3; j <= 3; j++) {
                    world.setBlock(brain.tutorial.TutorialConfig.stage9_x + i, brain.tutorial.TutorialConfig.stage9_y - 1, brain.tutorial.TutorialConfig.stage9_z + j, net.minecraft.init.Blocks.grass);
                    world.setBlockToAir(brain.tutorial.TutorialConfig.stage9_x + i, brain.tutorial.TutorialConfig.stage9_y, brain.tutorial.TutorialConfig.stage9_z + j);
                    world.setBlockToAir(brain.tutorial.TutorialConfig.stage9_x + i, brain.tutorial.TutorialConfig.stage9_y + 1, brain.tutorial.TutorialConfig.stage9_z + j);
                }
            }
        }
        
        if (player instanceof net.minecraft.entity.player.EntityPlayerMP) {
            player.closeScreen();
            ((net.minecraft.entity.player.EntityPlayerMP) player).playerNetServerHandler.setPlayerLocation(brain.tutorial.TutorialConfig.stage9_x, brain.tutorial.TutorialConfig.stage9_y, brain.tutorial.TutorialConfig.stage9_z, player.rotationYaw, player.rotationPitch);
        }
        
        if (brain.tutorial.TutorialConfig.generatePlatforms) {
            world.setBlock(brain.tutorial.TutorialConfig.stage9_x + 3, brain.tutorial.TutorialConfig.stage9_y, brain.tutorial.TutorialConfig.stage9_z + 3, got.GOT.blockStructureHeart);
        }
        
        got.common.entity.tutorial.GOTEntityTutorialCaptain captain = new got.common.entity.tutorial.GOTEntityTutorialCaptain(world);
        captain.setLocationAndAngles(brain.tutorial.TutorialConfig.captain_stage9_x, brain.tutorial.TutorialConfig.captain_stage9_y, brain.tutorial.TutorialConfig.captain_stage9_z, 90.0F, 0.0F);
        captain.tasks.addTask(1, new net.minecraft.entity.ai.EntityAIWatchClosest(captain, net.minecraft.entity.player.EntityPlayer.class, 8.0F));
        captain.tasks.addTask(2, new net.minecraft.entity.ai.EntityAILookIdle(captain));
        world.spawnEntityInWorld(captain);
        registerTutorialEntity(player, captain);
        
        setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage9.captain2"));
        
        syncState(player);
    }
    
    public void advanceStage9(EntityPlayer player, int step) {
        ExtendedPlayer ext = ExtendedPlayer.get(player);
        if (ext != null && ext.isTutorialActive() && ext.getTutorialStage() == 9) {
            if (ext.getTutorialProgress() < step) {
                ext.setTutorialProgress(step);
                syncState(player);
                if (step == 1) {
                    setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage9.captain3"));
                    tutorialTimers.put(player.getUniqueID(), 0);
                    player.addPotionEffect(new net.minecraft.potion.PotionEffect(net.minecraft.potion.Potion.blindness.id, 140, 1, true));
                }
            }
        }
    }
    
    public void startStage10(EntityPlayer player, ExtendedPlayer ext) {
        clearTutorialEntities(player);
        ext.setTutorialStage(10);
        ext.setTutorialProgress(1); // Start automatically
        syncState(player);
        
        net.minecraft.world.World world = player.worldObj;
        if (brain.tutorial.TutorialConfig.generatePlatforms) {
            for (int i = -4; i <= 4; i++) {
                for (int j = -4; j <= 4; j++) {
                    world.setBlock(brain.tutorial.TutorialConfig.stage10_x + i, brain.tutorial.TutorialConfig.stage10_y - 1, brain.tutorial.TutorialConfig.stage10_z + j, net.minecraft.init.Blocks.lava);
                    world.setBlockToAir(brain.tutorial.TutorialConfig.stage10_x + i, brain.tutorial.TutorialConfig.stage10_y, brain.tutorial.TutorialConfig.stage10_z + j);
                    world.setBlockToAir(brain.tutorial.TutorialConfig.stage10_x + i, brain.tutorial.TutorialConfig.stage10_y + 1, brain.tutorial.TutorialConfig.stage10_z + j);
                }
            }
            world.setBlock(brain.tutorial.TutorialConfig.stage10_x, brain.tutorial.TutorialConfig.stage10_y - 1, brain.tutorial.TutorialConfig.stage10_z, net.minecraft.init.Blocks.obsidian);
            world.setBlock(brain.tutorial.TutorialConfig.stage10_x, brain.tutorial.TutorialConfig.stage10_y - 1, brain.tutorial.TutorialConfig.stage10_z + 2, net.minecraft.init.Blocks.obsidian);
        }
        
        if (player instanceof net.minecraft.entity.player.EntityPlayerMP) {
            player.closeScreen();
            ((net.minecraft.entity.player.EntityPlayerMP) player).playerNetServerHandler.setPlayerLocation(brain.tutorial.TutorialConfig.stage10_x, brain.tutorial.TutorialConfig.stage10_y, brain.tutorial.TutorialConfig.stage10_z, player.rotationYaw, player.rotationPitch);
        }
        
        got.common.entity.tutorial.GOTEntityTutorialJaqen jaqen = new got.common.entity.tutorial.GOTEntityTutorialJaqen(world);
        jaqen.setPosition(brain.tutorial.TutorialConfig.jaqen_stage10_x, brain.tutorial.TutorialConfig.jaqen_stage10_y, brain.tutorial.TutorialConfig.jaqen_stage10_z);
        jaqen.setCustomNameTag(brain.tutorial.TutorialTexts.get("entity.jaqen"));
        jaqen.tasks.addTask(1, new net.minecraft.entity.ai.EntityAIWatchClosest(jaqen, net.minecraft.entity.player.EntityPlayer.class, 8.0F));
        jaqen.tasks.addTask(2, new net.minecraft.entity.ai.EntityAILookIdle(jaqen));
        world.spawnEntityInWorld(jaqen);
        registerTutorialEntity(player, jaqen);
        
        setSubtitle(player, brain.tutorial.TutorialTexts.get("subtitle.stage10.jaqen1"));
    }

    @SubscribeEvent
    public void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        ExtendedPlayer oldExt = ExtendedPlayer.get(event.original);
        ExtendedPlayer newExt = ExtendedPlayer.get(event.entityPlayer);
        if (oldExt != null && newExt != null) {
            newExt.setTutorialActive(oldExt.isTutorialActive());
            newExt.setTutorialStage(oldExt.getTutorialStage());
            newExt.setTutorialProgress(oldExt.getTutorialProgress());
            newExt.setTutorialReplay(oldExt.isTutorialReplay());
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent event) {
        net.minecraft.nbt.NBTTagCompound persisted = event.player.getEntityData().getCompoundTag(net.minecraft.entity.player.EntityPlayer.PERSISTED_NBT_TAG);
        if (persisted.getBoolean("TutorialRewardsPending")) {
            persisted.setBoolean("TutorialRewardsPending", false);
            completeTutorial(event.player);
        }
    }
    
    @SubscribeEvent
    public void onCheckSpawn(net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn event) {
        if (event.world != null && event.world.provider != null && event.world.provider.dimensionId == 100) {
            if (event.entityLiving instanceof net.minecraft.entity.monster.IMob || event.entityLiving instanceof net.minecraft.entity.passive.IAnimals) {
                event.setResult(cpw.mods.fml.common.eventhandler.Event.Result.DENY);
            }
        }
    }
    
    @SubscribeEvent
    public void onItemToss(net.minecraftforge.event.entity.item.ItemTossEvent event) {
        ExtendedPlayer ext = ExtendedPlayer.get(event.player);
        if (ext != null && ext.isTutorialActive()) {
            event.setCanceled(true);
            event.player.inventory.addItemStackToInventory(event.entityItem.getEntityItem());
            if (event.player instanceof net.minecraft.entity.player.EntityPlayerMP) {
                ((net.minecraft.entity.player.EntityPlayerMP) event.player).sendContainerToPlayer(event.player.inventoryContainer);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDrops(net.minecraftforge.event.entity.player.PlayerDropsEvent event) {
        ExtendedPlayer ext = ExtendedPlayer.get(event.entityPlayer);
        if (ext != null && ext.isTutorialActive()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(net.minecraftforge.event.entity.EntityJoinWorldEvent event) {
        if (event.entity instanceof net.minecraft.entity.item.EntityItem) {
            if (event.world.provider.dimensionId == 100) {
                event.setCanceled(true);
            }
        }
    }
}


