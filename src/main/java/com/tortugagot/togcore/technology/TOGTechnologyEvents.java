package com.tortugagot.togcore.technology;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import com.tortugagot.togcore.recipe.TOGRecipeBallistaBolt;
import cpw.mods.fml.common.gameevent.TickEvent;
import got.common.block.other.GOTBlockBerryBush;
import got.common.block.other.GOTBlockCorn;
import got.common.block.other.GOTBlockGrapevine;
import got.common.database.GOTRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;

public class TOGTechnologyEvents {
    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer && TOGTechnologyPlayerData.get((EntityPlayer) event.entity) == null) {
            TOGTechnologyPlayerData.register((EntityPlayer) event.entity);
        }
    }

    @SubscribeEvent
    public void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        TOGTechnologyPlayerData oldData = TOGTechnologyPlayerData.get(event.original);
        TOGTechnologyPlayerData newData = TOGTechnologyPlayerData.get(event.entityPlayer);
        if (oldData != null && newData != null) {
            newData.copyFrom(oldData);
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event) {
        sync(event.player);
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        sync(event.player);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.worldObj.isRemote) {
            return;
        }
        if (TOGWarriorTechnology.isStunned(event.player)) {
            event.player.setSprinting(false);
            event.player.clearItemInUse();
            if (event.player.motionY > 0.0D) {
                event.player.motionY = 0.0D;
            }
            event.player.jumpMovementFactor = 0.0F;
        }
        TOGWarriorTechnology.updateShieldWall(event.player);
        TOGCookingTechnology.discardLockedCookingOutputs(event.player);
        if (TOGTechnologyLocks.has(event.player, TOGTechnologyLocks.FACTION_ARMOR)) {
            return;
        }
        boolean changed = false;
        for (int armorSlot = 0; armorSlot < event.player.inventory.armorInventory.length; armorSlot++) {
            ItemStack stack = event.player.inventory.armorInventory[armorSlot];
            if (!isLockedFactionArmor(stack)) {
                continue;
            }
            ItemStack toReturn = stack.copy();
            event.player.inventory.armorInventory[armorSlot] = null;
            if (event.player.inventory.addItemStackToInventory(toReturn)) {
                changed = true;
            } else {
                event.player.inventory.armorInventory[armorSlot] = stack;
                TOGTechnologyNotifier.notifyBlocked(event.player, "armor:faction:no_space", "снять заблокированную фракционную броню", "в инвентаре нет места для возврата предмета", TOGTechnologyLocks.FACTION_ARMOR);
                continue;
            }
            TOGTechnologyNotifier.notifyBlocked(event.player, "armor:faction", "надеть фракционную броню", "не открыта технология для использования этой брони", TOGTechnologyLocks.FACTION_ARMOR);
        }
        if (changed) {
            event.player.inventory.markDirty();
            event.player.inventoryContainer.detectAndSendChanges();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onAttackWhileStunned(AttackEntityEvent event) {
        if (event.entityPlayer != null && !event.entityPlayer.worldObj.isRemote && TOGWarriorTechnology.isStunned(event.entityPlayer)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        TOGWarriorTechnology.updateSuppressedKnockback(event.entityLiving);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onUseHoe(UseHoeEvent event) {
        EntityPlayer player = event.entityPlayer;
        TOGFarmingTechnology.FarmingLock lock = TOGFarmingTechnology.getHoeLock();
        if (player == null || event.world.isRemote || TOGTechnologyLocks.has(player, lock.getTechnologyId()) || !TOGFarmingTechnology.isHoeTarget(event.world, event.x, event.y, event.z)) {
            return;
        }
        if (event.current != null && !player.capabilities.isCreativeMode) {
            event.current.damageItem(1, player);
        }
        event.setCanceled(true);
        TOGFarmingTechnology.notifyBlocked(player, lock);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlantingAttempt(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || event.entityPlayer == null || event.world.isRemote) {
            return;
        }
        ItemStack stack = event.entityPlayer.getHeldItem();
        TOGFarmingTechnology.FarmingLock lock = TOGFarmingTechnology.getPlantingLock(stack, event.world, event.x, event.y, event.z);
        if (lock == null || TOGTechnologyLocks.has(event.entityPlayer, lock.getTechnologyId()) || !TOGFarmingTechnology.isPlantingAttempt(event.world, event.x, event.y, event.z, event.face, stack)) {
            return;
        }
        event.setCanceled(true);
        TOGFarmingTechnology.notifyBlocked(event.entityPlayer, lock);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
        EntityPlayer player = event.harvester;
        if (event.world.isRemote) {
            return;
        }
        TOGGatheringTechnology.GatheringLock lock = TOGGatheringTechnology.getLock(event.block, event.blockMetadata);
        if (lock != null && (player == null || !TOGTechnologyLocks.has(player, lock.getTechnologyId()))) {
            event.drops.clear();
            if (player != null) {
                notifyBlocked(player, lock);
            }
            return;
        }
        TOGMiningTechnology.applyHarvestDrops(event);
    }

    @SubscribeEvent
    public void onItemCrafted(ItemCraftedEvent event) {
        TOGRecipeBallistaBolt.consumeExtraSteel(event.crafting, event.craftMatrix);
    }

    @SubscribeEvent
    public void onItemSmelted(ItemSmeltedEvent event) {
        TOGCookingTechnology.discardSmeltedItem(event.player, event.smelting);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || event.entityPlayer == null || event.world.isRemote) {
            return;
        }
        Block block = event.world.getBlock(event.x, event.y, event.z);
        int meta = event.world.getBlockMetadata(event.x, event.y, event.z);
        TOGGatheringTechnology.GatheringLock lock = TOGGatheringTechnology.getLock(block, meta);
        if (lock == null || TOGTechnologyLocks.has(event.entityPlayer, lock.getTechnologyId()) || !isManualHarvestReady(block, meta, event)) {
            return;
        }
        consumeManualHarvest(block, meta, event);
        event.setCanceled(true);
        notifyBlocked(event.entityPlayer, lock);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDrops(LivingDropsEvent event) {
        EntityPlayer player = getPlayer(event.source.getEntity());
        if (event.entityLiving.worldObj.isRemote || !TOGHuntingTechnology.isAnimal(event.entityLiving)) {
            return;
        }
        boolean hunterBasic = player != null && TOGTechnologyLocks.has(player, TOGTechnologyLocks.HUNTER_BASIC);
        boolean hunterMoreMeat = player != null && TOGTechnologyLocks.has(player, TOGTechnologyLocks.HUNTER_MORE_MEAT);
        boolean hunterAdvanced = player != null && TOGTechnologyLocks.has(player, TOGTechnologyLocks.HUNTER_ADVANCED);
        int keptMeat = 0;
        Map<String, Integer> keptLimitedResources = new HashMap<String, Integer>();
        boolean removedBasic = false;
        boolean removedAdvanced = false;
        boolean removedExtraMeat = false;
        boolean removedExtraResource = false;
        Iterator iterator = event.drops.iterator();
        while (iterator.hasNext()) {
            EntityItem entityItem = (EntityItem) iterator.next();
            ItemStack stack = entityItem.getEntityItem();
            TOGHuntingTechnology.DropType type = TOGHuntingTechnology.getDropType(stack);
            if (type == TOGHuntingTechnology.DropType.ALLOWED) {
                continue;
            }
            if (type == TOGHuntingTechnology.DropType.MEAT) {
                if (!hunterMoreMeat) {
                    int allowed = Math.max(0, 1 - keptMeat);
                    if (trimOrRemove(iterator, stack, allowed)) {
                        removedExtraMeat = true;
                    }
                    keptMeat += stack != null ? stack.stackSize : 0;
                }
                continue;
            }
            if (type == TOGHuntingTechnology.DropType.HIDE_OR_TROPHY) {
                if (!hunterAdvanced) {
                    iterator.remove();
                    removedAdvanced = true;
                }
                continue;
            }
            if (!hunterBasic) {
                iterator.remove();
                removedBasic = true;
                continue;
            }
            if (!hunterAdvanced && TOGHuntingTechnology.isLimitedByAdvancedHunter(stack)) {
                String key = TOGHuntingTechnology.stackKey(stack);
                int kept = keptLimitedResources.containsKey(key) ? keptLimitedResources.get(key) : 0;
                int allowed = Math.max(0, 1 - kept);
                if (trimOrRemove(iterator, stack, allowed)) {
                    removedExtraResource = true;
                }
                keptLimitedResources.put(key, kept + (stack != null ? stack.stackSize : 0));
            }
        }
        if (player != null) {
            notifyHuntingBlocked(player, removedBasic, removedAdvanced, removedExtraMeat, removedExtraResource);
        }
    }

    private void sync(EntityPlayer player) {
        TOGTechnologyPlayerData data = TOGTechnologyPlayerData.get(player);
        if (data != null && !player.worldObj.isRemote) {
            data.sync();
        }
    }

    private boolean isManualHarvestReady(Block block, int meta, PlayerInteractEvent event) {
        if (TOGGatheringTechnology.isBerryBush(block)) {
            return GOTBlockBerryBush.hasBerries(meta);
        }
        if (TOGGatheringTechnology.isCorn(block)) {
            return GOTBlockCorn.hasCorn(event.world, event.x, event.y, event.z);
        }
        return TOGGatheringTechnology.isGrapes(block) && GOTBlockGrapevine.isFullGrownGrapes(block, meta);
    }

    private void consumeManualHarvest(Block block, int meta, PlayerInteractEvent event) {
        if (TOGGatheringTechnology.isBerryBush(block)) {
            event.world.setBlockMetadataWithNotify(event.x, event.y, event.z, GOTBlockBerryBush.setHasBerries(meta, false), 3);
        } else if (TOGGatheringTechnology.isCorn(block)) {
            GOTBlockCorn.setHasCorn(event.world, event.x, event.y, event.z, false);
        } else if (TOGGatheringTechnology.isGrapes(block)) {
            event.world.setBlock(event.x, event.y, event.z, GOTRegistry.grapevine, 0, 3);
        }
    }

    private void notifyBlocked(EntityPlayer player, TOGGatheringTechnology.GatheringLock lock) {
        player.addChatMessage(new ChatComponentText(lock.getMessage()));
    }

    private boolean isLockedFactionArmor(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        Item item = stack.getItem();
        return item == GOTRegistry.arrynHelmet || item == GOTRegistry.arrynChestplate || item == GOTRegistry.arrynLeggings || item == GOTRegistry.arrynBoots
                || item == GOTRegistry.crownlandsHelmet || item == GOTRegistry.crownlandsChestplate || item == GOTRegistry.crownlandsLeggings || item == GOTRegistry.crownlandsBoots
                || item == GOTRegistry.dorneHelmet || item == GOTRegistry.dorneChestplate || item == GOTRegistry.dorneLeggings || item == GOTRegistry.dorneBoots
                || item == GOTRegistry.dragonstoneHelmet || item == GOTRegistry.dragonstoneChestplate || item == GOTRegistry.dragonstoneLeggings || item == GOTRegistry.dragonstoneBoots
                || item == GOTRegistry.giftHelmet || item == GOTRegistry.giftChestplate || item == GOTRegistry.giftLeggings || item == GOTRegistry.giftBoots
                || item == GOTRegistry.ironbornHelmet || item == GOTRegistry.ironbornChestplate || item == GOTRegistry.ironbornLeggings || item == GOTRegistry.ironbornBoots
                || item == GOTRegistry.northHelmet || item == GOTRegistry.northChestplate || item == GOTRegistry.northLeggings || item == GOTRegistry.northBoots
                || item == GOTRegistry.reachHelmet || item == GOTRegistry.reachChestplate || item == GOTRegistry.reachLeggings || item == GOTRegistry.reachBoots
                || item == GOTRegistry.riverlandsHelmet || item == GOTRegistry.riverlandsChestplate || item == GOTRegistry.riverlandsLeggings || item == GOTRegistry.riverlandsBoots
                || item == GOTRegistry.stormlandsHelmet || item == GOTRegistry.stormlandsChestplate || item == GOTRegistry.stormlandsLeggings || item == GOTRegistry.stormlandsBoots
                || item == GOTRegistry.westerlandsHelmet || item == GOTRegistry.westerlandsChestplate || item == GOTRegistry.westerlandsLeggings || item == GOTRegistry.westerlandsBoots;
    }

    private EntityPlayer getPlayer(Entity entity) {
        return entity instanceof EntityPlayer ? (EntityPlayer) entity : null;
    }

    private boolean trimOrRemove(Iterator iterator, ItemStack stack, int allowed) {
        if (stack == null || allowed <= 0) {
            iterator.remove();
            return true;
        }
        if (stack.stackSize > allowed) {
            stack.stackSize = allowed;
            return true;
        }
        return false;
    }

    private void notifyHuntingBlocked(EntityPlayer player, boolean removedBasic, boolean removedAdvanced, boolean removedExtraMeat, boolean removedExtraResource) {
        if (removedBasic) {
            TOGTechnologyNotifier.notifyBlocked(player, "hunt:basic", "получить материалы с животного", "не открыта технология для кожи, меха и рогов", TOGTechnologyLocks.HUNTER_BASIC);
        }
        if (removedAdvanced) {
            TOGTechnologyNotifier.notifyBlocked(player, "hunt:advanced", "получить шкуру с животного", "не открыта технология снятия шкур", TOGTechnologyLocks.HUNTER_ADVANCED);
        }
        if (removedExtraResource) {
            TOGTechnologyNotifier.notifyBlocked(player, "hunt:extra_resource", "получить дополнительные кожу или мех с животного", "не открыта технология увеличенного выхода кожи и меха", TOGTechnologyLocks.HUNTER_ADVANCED);
        }
        if (removedExtraMeat) {
            TOGTechnologyNotifier.notifyBlocked(player, "hunt:extra_meat", "получить больше мяса с животного", "не открыта технология увеличенного выхода мяса", TOGTechnologyLocks.HUNTER_MORE_MEAT);
        }
    }
}
