package got.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import got.common.database.GOTRegistry;
import got.common.enchant.GOTEnchantment;
import got.common.enchant.GOTEnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class GOTSoulBoundEvents {
    private Map<String, ItemStack[]> itemsToRestore = new HashMap<String, ItemStack[]>();
    private static final double killBoostForSeal = 0.0001;

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
                                    mainItem.getTagCompound().setDouble("sealChance", chance == 1.0 ? 1.0 : (chance += killBoostForSeal));
                                }
                            }
                        }
                        ItemStack[] armor = attackerPlayer.inventory.armorInventory;
                        for(ItemStack armorItem : armor) {
                            if (armorItem != null && GOTEnchantmentHelper.hasEnchant(armorItem, GOTEnchantment.valyrianSeal)) {
                                if(armorItem.hasTagCompound()) {
                                    double chance = armorItem.getTagCompound().getDouble("sealChance");
                                    armorItem.getTagCompound().setDouble("sealChance", chance == 1.0 ? 1.0 : (chance += killBoostForSeal));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void death(LivingDeathEvent event) {
        if(!event.entityLiving.worldObj.isRemote) {
            if (event.entityLiving instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)event.entityLiving;
                boolean restore = false;
                ItemStack[] main = player.inventory.mainInventory;
                ItemStack[] armor = player.inventory.armorInventory;
                ItemStack[] itemsPerPlayer = new ItemStack[main.length + armor.length];
                for (int mainIndex = 0; mainIndex < main.length; mainIndex++) {
                    ItemStack mainItem = main[mainIndex];
                    if (mainItem != null && mainItem.getItem() == GOTRegistry.wargCloak) {
                        itemsPerPlayer[mainIndex + armor.length] = mainItem;
                        restore = true;
                    }
                    if (mainItem != null && GOTEnchantmentHelper.getEnchantList(mainItem).contains(GOTEnchantment.valyrianSeal)) {
                        if(player.worldObj.rand.nextDouble() <= mainItem.getTagCompound().getDouble("sealChance")) {
                            itemsPerPlayer[mainIndex + armor.length] = mainItem;
                            restore = true;
                            double chance = mainItem.getTagCompound().getDouble("sealChance");
                            mainItem.getTagCompound().setDouble("sealChance", chance -= 0.1);
                        }
                    }
                }
                for (int armorIndex = 0; armorIndex < armor.length; armorIndex++) {
                    ItemStack armorItem = armor[armorIndex];
                    if (armorItem != null && armorItem.getItem() == GOTRegistry.wargCloak) {
                        itemsPerPlayer[armorIndex] = armorItem;
                        restore = true;
                    }
                    if (armorItem != null && GOTEnchantmentHelper.getEnchantList(armorItem).contains(GOTEnchantment.valyrianSeal)) {
                        if(player.worldObj.rand.nextDouble() <= armorItem.getTagCompound().getDouble("sealChance")) {
                            itemsPerPlayer[armorIndex] = armorItem;
                            restore = true;
                            double chance = armorItem.getTagCompound().getDouble("sealChance");
                            armorItem.getTagCompound().setDouble("sealChance", chance -= 0.1);
                        }
                    }
                }
                if (restore) {
                    this.itemsToRestore.put(player.getUniqueID().toString(), itemsPerPlayer);
                }
            }
        }
    }

    @SubscribeEvent
    public void respawn(PlayerEvent.Clone event) {
        if(!event.entityPlayer.worldObj.isRemote) {
            EntityPlayer player = event.entityPlayer;
            if (event.wasDeath && this.itemsToRestore.containsKey(player.getUniqueID().toString())) {
                ItemStack[] itemsPerPlayer = this.itemsToRestore.get(player.getUniqueID().toString());
                System.arraycopy(itemsPerPlayer, player.inventory.armorInventory.length, player.inventory.mainInventory, 0, player.inventory.mainInventory.length);
                System.arraycopy(itemsPerPlayer, 0, player.inventory.armorInventory, 0, player.inventory.armorInventory.length);
                this.itemsToRestore.remove(player.getUniqueID().toString());
            }
        }
    }

    @SubscribeEvent
    public void drop(PlayerDropsEvent event) {
        if(!event.entityPlayer.worldObj.isRemote) {
            EntityPlayer player = event.entityPlayer;
            if (this.itemsToRestore.containsKey(player.getUniqueID().toString())) {
                List<ItemStack> listPerPlayer = Arrays.asList(this.itemsToRestore.get(player.getUniqueID().toString()));
                Stream<EntityItem> stream = StreamSupport.stream(event.drops.spliterator(), false);
                Set<EntityItem> itemsToRemove = stream.filter(itemToFilter -> listPerPlayer.contains(itemToFilter.getEntityItem())).collect(Collectors.toSet());
                event.drops.removeAll(itemsToRemove);
            }
        }
    }
}
