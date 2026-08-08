package com.tortugagot.togcore.item;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class TOGMasteryItemEvents {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onItemToss(ItemTossEvent event) {
        ItemStack stack = event.entityItem.getEntityItem();
        if (!TOGMasteryItemOwnership.isMasteryItem(stack)) {
            return;
        }
        if (TOGMasteryItemOwnership.canOperatorKeepUnbound(stack, event.player)) {
            return;
        }
        boolean hadOwner = TOGMasteryItemOwnership.hasOwner(stack);
        TOGMasteryItemOwnership.bindToPlayer(stack, event.player);
        event.entityItem.setDead();
        if (hadOwner) {
            returnToOwnerOrQueue(event.player.worldObj, stack);
            sendMessage(event.player, "togcore.chat.masteryItemNoDrop");
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onItemPickup(EntityItemPickupEvent event) {
        ItemStack stack = event.item.getEntityItem();
        if (!TOGMasteryItemOwnership.isMasteryItem(stack)) {
            return;
        }
        if (!TOGMasteryItemOwnership.hasOwner(stack)) {
            TOGMasteryItemOwnership.bindToPlayer(stack, event.entityPlayer);
            return;
        }
        if (!TOGMasteryItemOwnership.isOwnedBy(stack, event.entityPlayer)) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
            event.item.setDead();
            returnToOwnerOrQueue(event.entityPlayer.worldObj, stack);
            sendWrongOwnerMessage(event.entityPlayer, stack);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerDrops(PlayerDropsEvent event) {
        if (event.entityPlayer.worldObj.isRemote) {
            return;
        }
        Iterator<EntityItem> iterator = event.drops.iterator();
        while (iterator.hasNext()) {
            EntityItem droppedEntity = iterator.next();
            ItemStack stack = droppedEntity.getEntityItem();
            if (TOGMasteryItemOwnership.isMasteryItem(stack)) {
                if (TOGMasteryItemOwnership.canOperatorKeepUnbound(stack, event.entityPlayer)) {
                    continue;
                }
                TOGMasteryItemOwnership.bindToPlayer(stack, event.entityPlayer);
                droppedEntity.setDead();
                iterator.remove();
                returnToOwnerOrQueue(event.entityPlayer.worldObj, stack);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.entityPlayer.worldObj.isRemote && event.wasDeath) {
            deliverPending(event.entityPlayer);
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event) {
        if (!event.player.worldObj.isRemote) {
            sanitizePlayerInventory(event.player);
            deliverPending(event.player);
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!event.player.worldObj.isRemote) {
            sanitizePlayerInventory(event.player);
            deliverPending(event.player);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.worldObj.isRemote) {
            return;
        }
        sanitizePlayerInventory(event.player);
        sanitizeOpenContainer(event.player);
        deliverPending(event.player);
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.world.isRemote || event.world.getTotalWorldTime() % 20L != 0L) {
            return;
        }
        scanTileInventories(event.world);
        scanEntityInventories(event.world);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityInteract(EntityInteractEvent event) {
        ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
        if (TOGMasteryItemOwnership.isMasteryItem(stack)) {
            if (TOGMasteryItemOwnership.canOperatorKeepUnbound(stack, event.entityPlayer)) {
                return;
            }
            TOGMasteryItemOwnership.bindToPlayer(stack, event.entityPlayer);
            event.setCanceled(true);
            sendMessage(event.entityPlayer, "togcore.chat.masteryItemNoTransfer");
        }
    }

    private void sanitizePlayerInventory(EntityPlayer player) {
        boolean changed = false;
        for (int slot = 0; slot < player.inventory.getSizeInventory(); slot++) {
            ItemStack stack = player.inventory.getStackInSlot(slot);
            if (!TOGMasteryItemOwnership.isMasteryItem(stack)) {
                continue;
            }
            boolean hadOwner = TOGMasteryItemOwnership.hasOwner(stack);
            if (TOGMasteryItemOwnership.canOperatorKeepUnbound(stack, player)) {
                changed |= hadOwner;
                continue;
            }
            if (!TOGMasteryItemOwnership.hasOwner(stack)) {
                TOGMasteryItemOwnership.bindToPlayer(stack, player);
                changed = true;
            } else if (!TOGMasteryItemOwnership.isOwnedBy(stack, player)) {
                player.inventory.setInventorySlotContents(slot, null);
                returnToOwnerOrQueue(player.worldObj, stack);
                sendWrongOwnerMessage(player, stack);
                changed = true;
            }
        }
        if (changed) {
            player.inventory.markDirty();
            player.inventoryContainer.detectAndSendChanges();
        }
    }

    private void sanitizeOpenContainer(EntityPlayer player) {
        Container container = player.openContainer;
        if (container == null || container == player.inventoryContainer) {
            return;
        }
        boolean changed = false;
        for (Object slotObject : container.inventorySlots) {
            Slot slot = (Slot) slotObject;
            ItemStack stack = slot.getStack();
            if (!TOGMasteryItemOwnership.isMasteryItem(stack)) {
                continue;
            }
            if (slot.inventory == player.inventory) {
                boolean hadOwner = TOGMasteryItemOwnership.hasOwner(stack);
                if (TOGMasteryItemOwnership.canOperatorKeepUnbound(stack, player)) {
                    changed |= hadOwner;
                    continue;
                }
                if (!TOGMasteryItemOwnership.hasOwner(stack)) {
                    TOGMasteryItemOwnership.bindToPlayer(stack, player);
                    changed = true;
                } else if (!TOGMasteryItemOwnership.isOwnedBy(stack, player)) {
                    slot.putStack(null);
                    returnToOwnerOrQueue(player.worldObj, stack);
                    sendWrongOwnerMessage(player, stack);
                    changed = true;
                }
                continue;
            }
            if (TOGMasteryItemOwnership.hasOwner(stack)) {
                slot.putStack(null);
                returnToOwnerOrQueue(player.worldObj, stack);
                if (TOGMasteryItemOwnership.isOwnedBy(stack, player)) {
                    sendMessage(player, "togcore.chat.masteryItemNoTransfer");
                } else {
                    sendWrongOwnerMessage(player, stack);
                }
                changed = true;
            }
        }
        if (changed) {
            container.detectAndSendChanges();
            player.inventoryContainer.detectAndSendChanges();
        }
    }

    private void scanTileInventories(World world) {
        List tileEntities = new ArrayList(world.loadedTileEntityList);
        for (Object tileEntity : tileEntities) {
            if (tileEntity instanceof IInventory) {
                scanInventory(world, (IInventory) tileEntity);
            }
        }
    }

    private void scanEntityInventories(World world) {
        List entities = new ArrayList(world.loadedEntityList);
        for (Object entityObject : entities) {
            Entity entity = (Entity) entityObject;
            if (entity instanceof EntityPlayer) {
                continue;
            }
            if (entity instanceof EntityItem) {
                scanEntityItem(world, (EntityItem) entity);
            }
            if (entity instanceof EntityItemFrame) {
                scanItemFrame(world, (EntityItemFrame) entity);
            }
            if (entity instanceof IInventory) {
                scanInventory(world, (IInventory) entity);
            }
            if (entity instanceof EntityLivingBase) {
                scanLivingEquipment(world, (EntityLivingBase) entity);
            }
        }
    }

    private void scanEntityItem(World world, EntityItem entityItem) {
        ItemStack stack = entityItem.getEntityItem();
        if (TOGMasteryItemOwnership.isMasteryItem(stack) && TOGMasteryItemOwnership.hasOwner(stack)) {
            entityItem.setDead();
            returnToOwnerOrQueue(world, stack);
        }
    }

    private void scanItemFrame(World world, EntityItemFrame itemFrame) {
        ItemStack stack = itemFrame.getDisplayedItem();
        if (TOGMasteryItemOwnership.isMasteryItem(stack) && TOGMasteryItemOwnership.hasOwner(stack)) {
            itemFrame.setDisplayedItem(null);
            returnToOwnerOrQueue(world, stack);
        }
    }

    private void scanLivingEquipment(World world, EntityLivingBase entity) {
        for (int slot = 0; slot <= 4; slot++) {
            ItemStack stack = entity.getEquipmentInSlot(slot);
            if (TOGMasteryItemOwnership.isMasteryItem(stack) && TOGMasteryItemOwnership.hasOwner(stack)) {
                entity.setCurrentItemOrArmor(slot, null);
                returnToOwnerOrQueue(world, stack);
            }
        }
    }

    private void scanInventory(World world, IInventory inventory) {
        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (TOGMasteryItemOwnership.isMasteryItem(stack) && TOGMasteryItemOwnership.hasOwner(stack)) {
                inventory.setInventorySlotContents(slot, null);
                inventory.markDirty();
                returnToOwnerOrQueue(world, stack);
            }
        }
    }

    private void returnToOwnerOrQueue(World world, ItemStack stack) {
        UUID ownerUuid = TOGMasteryItemOwnership.getOwnerUuid(stack);
        if (ownerUuid == null || stack == null || stack.stackSize <= 0) {
            return;
        }
        EntityPlayerMP owner = findOnlineOwner(ownerUuid);
        if (owner != null) {
            ItemStack toAdd = stack.copy();
            if (!owner.inventory.addItemStackToInventory(toAdd) && toAdd.stackSize > 0) {
                TOGMasteryItemReturnData.get(world).queue(ownerUuid, toAdd);
            }
            owner.inventory.markDirty();
            owner.inventoryContainer.detectAndSendChanges();
        } else {
            TOGMasteryItemReturnData.get(world).queue(ownerUuid, stack);
        }
    }

    private void deliverPending(EntityPlayer player) {
        TOGMasteryItemReturnData.get(player.worldObj).deliver(player);
    }

    private EntityPlayerMP findOnlineOwner(UUID ownerUuid) {
        if (MinecraftServer.getServer() == null || MinecraftServer.getServer().getConfigurationManager() == null) {
            return null;
        }
        for (Object playerObject : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            EntityPlayerMP player = (EntityPlayerMP) playerObject;
            if (player.getUniqueID().equals(ownerUuid)) {
                return player;
            }
        }
        return null;
    }

    private void sendWrongOwnerMessage(EntityPlayer player, ItemStack stack) {
        String ownerName = TOGMasteryItemOwnership.getOwnerName(stack);
        ChatComponentTranslation message = new ChatComponentTranslation("togcore.chat.masteryItemWrongOwner", ownerName != null ? ownerName : "?");
        message.getChatStyle().setColor(EnumChatFormatting.RED);
        player.addChatMessage(message);
    }

    private void sendMessage(EntityPlayer player, String key) {
        ChatComponentTranslation message = new ChatComponentTranslation(key);
        message.getChatStyle().setColor(EnumChatFormatting.RED);
        player.addChatMessage(message);
    }
}
