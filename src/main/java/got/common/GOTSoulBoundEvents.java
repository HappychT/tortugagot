package got.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import brain.factions.arenas.ArenaManager;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import got.common.database.GOTEffects;
import got.common.database.GOTRegistry;
import got.common.enchant.GOTEnchantment;
import got.common.enchant.GOTEnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class GOTSoulBoundEvents {

    private Map<String, ItemStack[]> itemsToRestore = new HashMap<>();
    private Map<String, ItemStack[]> fullInventoryCache = new HashMap<>();
    private Set<String> skipPenalty = new HashSet<>();

    public static GOTSoulBoundEvents instance;

    private static final double killBoostForSeal = 0.0001;
    private static final String SAVE_DIR = "GOT_SoulBound";
    private Set<String> processingLogout = new HashSet<>();
    public GOTSoulBoundEvents() {
        instance = this;
    }

    @SubscribeEvent
    public void killOther(LivingDeathEvent event) {
        if(!event.entityLiving.worldObj.isRemote) {
            if (event.entityLiving instanceof EntityPlayer) {
                Entity attacker = event.source.getEntity();
                Entity sourceEntity = event.source.getSourceOfDamage();

                if (attacker instanceof EntityPlayer) {
                    EntityPlayer attackerPlayer = (EntityPlayer) attacker;
                    if (attackerPlayer == sourceEntity) {
                        ItemStack[] main = attackerPlayer.inventory.mainInventory;
                        for(ItemStack mainItem : main) {
                            if (mainItem != null && GOTEnchantmentHelper.hasEnchant(mainItem, GOTEnchantment.valyrianSeal)) {
                                if(mainItem.hasTagCompound()) {
                                    double chance = mainItem.getTagCompound().getDouble("sealChance");
                                    if (chance < 1.0) {
                                        chance += killBoostForSeal;
                                        if (chance > 1.0) {
                                            chance = 1.0;
                                        }
                                    }
                                    mainItem.getTagCompound().setDouble("sealChance", chance);
                                }
                            }
                        }
                        ItemStack[] armor = attackerPlayer.inventory.armorInventory;
                        for(ItemStack armorItem : armor) {
                            if (armorItem != null && GOTEnchantmentHelper.hasEnchant(armorItem, GOTEnchantment.valyrianSeal)) {
                                if(armorItem.hasTagCompound()) {
                                    double chance = armorItem.getTagCompound().getDouble("sealChance");
                                    if (chance < 1.0) {
                                        chance += killBoostForSeal;
                                        if (chance > 1.0) {
                                            chance = 1.0;
                                        }
                                    }
                                    armorItem.getTagCompound().setDouble("sealChance", chance);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private File getSaveDir(EntityPlayer player) {
        File worldDir = player.worldObj.getSaveHandler().getWorldDirectory();
        File saveDir = new File(worldDir, SAVE_DIR);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        return saveDir;
    }

    private File getPlayerSaveFile(EntityPlayer player) {
        return new File(getSaveDir(player), player.getUniqueID().toString() + ".dat");
    }

    private void saveItemsToFile(EntityPlayer player, ItemStack[] items) {
        File saveFile = getPlayerSaveFile(player);
        try (FileOutputStream fos = new FileOutputStream(saveFile)) {
            NBTTagCompound root = new NBTTagCompound();
            NBTTagList itemList = new NBTTagList();

            for (int i = 0; i < items.length; i++) {
                if (items[i] != null) {
                    NBTTagCompound itemTag = new NBTTagCompound();
                    itemTag.setInteger("Slot", i);
                    items[i].writeToNBT(itemTag);
                    itemList.appendTag(itemTag);
                }
            }
            root.setTag("Inventory", itemList);
            root.setInteger("InvSize", items.length);
            CompressedStreamTools.writeCompressed(root, fos);
        } catch (Exception e) {
            System.err.println("Ошибка сохранения SoulBound инвентаря для " + player.getDisplayName());
            e.printStackTrace();
        }
    }

    private ItemStack[] loadItemsFromFile(EntityPlayer player) {
        File saveFile = getPlayerSaveFile(player);
        if (!saveFile.exists()) {
            return null;
        }

        try (FileInputStream fis = new FileInputStream(saveFile)) {
            NBTTagCompound root = CompressedStreamTools.readCompressed(fis);
            NBTTagList itemList = root.getTagList("Inventory", 10);
            int invSize = root.getInteger("InvSize");

            if (invSize == 0) invSize = player.inventory.mainInventory.length + player.inventory.armorInventory.length;

            ItemStack[] items = new ItemStack[invSize];

            for (int i = 0; i < itemList.tagCount(); i++) {
                NBTTagCompound itemTag = itemList.getCompoundTagAt(i);
                int slot = itemTag.getInteger("Slot");
                if (slot >= 0 && slot < items.length) {
                    items[slot] = ItemStack.loadItemStackFromNBT(itemTag);
                }
            }
            return items;
        } catch (Exception e) {
            System.err.println("Ошибка загрузки SoulBound инвентаря для " + player.getDisplayName());
            e.printStackTrace();
            return null;
        }
    }

    private void deleteSaveFile(EntityPlayer player) {
        File saveFile = getPlayerSaveFile(player);
        if (saveFile.exists()) {
            saveFile.delete();
        }
    }

    private ItemStack[] captureFullInventory(EntityPlayer player) {
        ItemStack[] mainCopy = player.inventory.mainInventory;
        ItemStack[] armorCopy = player.inventory.armorInventory;
        ItemStack[] fullInventory = new ItemStack[mainCopy.length + armorCopy.length];
        for (int i = 0; i < mainCopy.length; i++) {
            if (mainCopy[i] != null) {
                fullInventory[i + armorCopy.length] = mainCopy[i].copy();
            }
        }
        for (int i = 0; i < armorCopy.length; i++) {
            if (armorCopy[i] != null) {
                fullInventory[i] = armorCopy[i].copy();
            }
        }
        return fullInventory;
    }

    private ItemStack[] getOrCreateFullInventory(String playerID, EntityPlayer player) {
        ItemStack[] fullInventory = this.fullInventoryCache.get(playerID);
        if (fullInventory == null) {
            fullInventory = captureFullInventory(player);
            this.fullInventoryCache.put(playerID, fullInventory);
        }
        return fullInventory;
    }

    private void queueFullInventoryRestore(EntityPlayer player, String playerID) {
        ItemStack[] fullInventory = getOrCreateFullInventory(playerID, player);
        this.itemsToRestore.put(playerID, fullInventory);
        this.skipPenalty.add(playerID);
        saveItemsToFile(player, fullInventory);
    }

    public void manuallyTriggerSave(EntityPlayer player) {
        String playerID = player.getUniqueID().toString();
        ItemStack[] fullInventory = captureFullInventory(player);
        this.fullInventoryCache.put(playerID, fullInventory);
        saveItemsToFile(player, fullInventory);
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void death(LivingDeathEvent event) {
        if (!event.entityLiving.worldObj.isRemote) {
            if (event.entityLiving instanceof EntityPlayer) {

                EntityPlayer player = (EntityPlayer) event.entityLiving;
                String playerID = player.getUniqueID().toString();
                boolean isInSafeZone = ArenaManager.instance.isPlayerInAnyRegion(player);

                if (isInSafeZone) {
                    queueFullInventoryRestore(player, playerID);
                    return;
                }
                getOrCreateFullInventory(playerID, player);

                boolean restore = false;
                ItemStack[] main = player.inventory.mainInventory;
                ItemStack[] armor = player.inventory.armorInventory;
                ItemStack[] itemsPerPlayer = new ItemStack[main.length + armor.length];

                for (int mainIndex = 0; mainIndex < main.length; mainIndex++) {
                    ItemStack mainItem = main[mainIndex];
                    if (mainItem == null) continue;
                    if (mainItem.getItem() == GOTRegistry.wargCloak || GOTEnchantmentHelper.hasEnchant(mainItem, GOTEnchantment.soulbound)) {
                        itemsPerPlayer[mainIndex + armor.length] = mainItem;
                        main[mainIndex] = null;
                        restore = true;
                    } else if (GOTEnchantmentHelper.getEnchantList(mainItem).contains(GOTEnchantment.valyrianSeal)) {
                        if (mainItem.hasTagCompound() && player.worldObj.rand.nextDouble() <= mainItem.getTagCompound().getDouble("sealChance")) {
                            itemsPerPlayer[mainIndex + armor.length] = mainItem;
                            main[mainIndex] = null;
                            restore = true;
                        }
                    }
                }
                for (int armorIndex = 0; armorIndex < armor.length; armorIndex++) {
                    ItemStack armorItem = armor[armorIndex];
                    if (armorItem == null) continue;
                    if (armorItem.getItem() == GOTRegistry.wargCloak || GOTEnchantmentHelper.hasEnchant(armorItem, GOTEnchantment.soulbound)) {
                        itemsPerPlayer[armorIndex] = armorItem;
                        armor[armorIndex] = null;
                        restore = true;
                    } else if (GOTEnchantmentHelper.getEnchantList(armorItem).contains(GOTEnchantment.valyrianSeal)) {
                        if (armorItem.hasTagCompound() && player.worldObj.rand.nextDouble() <= armorItem.getTagCompound().getDouble("sealChance")) {
                            itemsPerPlayer[armorIndex] = armorItem;
                            armor[armorIndex] = null;
                            restore = true;
                        }
                    }
                }

                if (restore) {
                    this.itemsToRestore.put(playerID, itemsPerPlayer);
                    saveItemsToFile(player, itemsPerPlayer);
                } else {
                    this.fullInventoryCache.remove(playerID);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void drop(PlayerDropsEvent event) {
        if (!event.entityPlayer.worldObj.isRemote) {
            EntityPlayer player = event.entityPlayer;
            String playerID = player.getUniqueID().toString();
            boolean isInSafeZone = ArenaManager.instance.isPlayerInAnyRegion(player);

            if (isInSafeZone) {
                queueFullInventoryRestore(player, playerID);
                event.drops.clear();
                this.fullInventoryCache.remove(playerID);
                return;
            }
            if (this.fullInventoryCache.containsKey(playerID)) {
                if (event.drops.isEmpty()) {
                    ItemStack[] fullInventory = this.fullInventoryCache.get(playerID);
                    this.itemsToRestore.put(playerID, fullInventory);
                    this.skipPenalty.add(playerID);
                    saveItemsToFile(player, fullInventory);
                }
                this.fullInventoryCache.remove(playerID);
            }
        }
    }

    @SubscribeEvent
    public void respawn(PlayerEvent.Clone event) {
        if (!event.entityPlayer.worldObj.isRemote) {
            EntityPlayer player = event.entityPlayer;
            String playerID = player.getUniqueID().toString();
            if (this.fullInventoryCache.containsKey(playerID)) {
                this.fullInventoryCache.remove(playerID);
            }

            boolean shouldSkipPenalty = this.skipPenalty.remove(playerID);

            if (event.wasDeath) {
                ItemStack[] itemsPerPlayer = null;

                if (this.itemsToRestore.containsKey(playerID)) {
                    itemsPerPlayer = this.itemsToRestore.remove(playerID);
                }
                else {
                    itemsPerPlayer = loadItemsFromFile(player);
                }

                if (itemsPerPlayer != null) {

                    if (!shouldSkipPenalty) {
                        for (ItemStack item : itemsPerPlayer) {
                            if (item != null && GOTEnchantmentHelper.hasEnchant(item, GOTEnchantment.valyrianSeal)) {
                                if (item.hasTagCompound() && item.getTagCompound().hasKey("sealChance")) {
                                    double chance = item.getTagCompound().getDouble("sealChance");
                                    chance = Math.max(0.0, chance - 0.1);
                                    item.getTagCompound().setDouble("sealChance", chance);
                                }
                            }
                        }
                    }

                    System.arraycopy(itemsPerPlayer, player.inventory.armorInventory.length, player.inventory.mainInventory, 0, player.inventory.mainInventory.length);
                    System.arraycopy(itemsPerPlayer, 0, player.inventory.armorInventory, 0, player.inventory.armorInventory.length);

                    deleteSaveFile(player);
                }
            }

            this.skipPenalty.remove(playerID);
        }
    }
    public void setProcessingLogout(String playerID) {
        this.processingLogout.add(playerID);
    }

    public boolean isProcessingLogout(String playerID) {
        return this.processingLogout.contains(playerID);
    }

    public void clearProcessingLogout(String playerID) {
        this.processingLogout.remove(playerID);
    }
}
