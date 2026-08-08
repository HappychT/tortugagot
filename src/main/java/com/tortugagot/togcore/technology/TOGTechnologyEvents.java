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
