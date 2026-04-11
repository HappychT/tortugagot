package noname.weapons.war;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import got.common.database.GOTRegistry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WarTicketHandler {

    private int tickCounter = 0;
    private static final int CHECK_INTERVAL = 100;
    private final Map<UUID, ItemStack> ticketsToRestore = new HashMap<>();

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (!WarModeManager.getInstance().isWarModeActive()) {
            return;
        }

        tickCounter++;
        if (tickCounter < CHECK_INTERVAL) {
            return;
        }
        tickCounter = 0;

        MinecraftServer server = MinecraftServer.getServer();
        if (server == null) {
            return;
        }

        List<EntityPlayerMP> playersToBan = new ArrayList<>();

        for (Object obj : server.getConfigurationManager().playerEntityList) {
            if (!(obj instanceof EntityPlayerMP)) {
                continue;
            }

            EntityPlayerMP player = (EntityPlayerMP) obj;

            if (isPlayerOP(player)) {
                continue;
            }

            if (!hasWarTicket(player)) {
                playersToBan.add(player);
            }
        }

        for (EntityPlayerMP player : playersToBan) {
            banPlayer(player);
        }
    }

    private boolean isPlayerOP(EntityPlayerMP player) {
        MinecraftServer server = MinecraftServer.getServer();
        if (server == null) {
            return false;
        }
        return server.getConfigurationManager().func_152596_g(player.getGameProfile());
    }

    private boolean hasWarTicket(EntityPlayerMP player) {
        if (ticketsToRestore.containsKey(player.getUniqueID())) {
            return true;
        }
        return findWarTicket(player.inventory) != null;
    }

    private ItemStack findWarTicket(InventoryPlayer inventory) {
        if (inventory == null) {
            return null;
        }
        for (int i = 0; i < inventory.mainInventory.length; i++) {
            ItemStack stack = inventory.mainInventory[i];
            if (isWarTicket(stack)) {
                return stack;
            }
        }

        for (int i = 0; i < inventory.armorInventory.length; i++) {
            ItemStack stack = inventory.armorInventory[i];
            if (isWarTicket(stack)) {
                return stack;
            }
        }

        return null;
    }

    private boolean isWarTicket(ItemStack stack) {
        return stack != null && stack.getItem() == GOTRegistry.millitaryCard;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerDrops(PlayerDropsEvent event) {
        if (event.entityPlayer == null || event.entityPlayer.worldObj.isRemote) {
            return;
        }

        if (!(event.entityPlayer instanceof EntityPlayerMP)) {
            return;
        }
        EntityPlayerMP player = (EntityPlayerMP) event.entityPlayer;
        ItemStack ticket = findWarTicket(player.inventory);
        EntityItem ticketDrop = null;
        if (ticket == null) {
            for (EntityItem drop : event.drops) {
                if (isWarTicket(drop.getEntityItem())) {
                    ticket = drop.getEntityItem();
                    ticketDrop = drop;
                    break;
                }
            }
            if (ticket == null) {
                return;
            }
        }

        UUID playerId = player.getUniqueID();
        int newDamage = Math.min(ticket.getMaxDamage(), ticket.getItemDamage() + 1);
        ItemStack updatedTicket = ticket.copy();
        updatedTicket.stackSize = 1;
        updatedTicket.setItemDamage(newDamage);
        ticket.setItemDamage(newDamage);

        if (updatedTicket.getItemDamage() < updatedTicket.getMaxDamage()) {
            ticketsToRestore.put(playerId, updatedTicket);
        } else {
            ticketsToRestore.remove(playerId);
        }

        Iterator<EntityItem> it = event.drops.iterator();
        while (it.hasNext()) {
            EntityItem drop = it.next();
            if (drop == ticketDrop || isWarTicket(drop.getEntityItem())) {
                drop.setDead();
                it.remove();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.wasDeath || event.entityPlayer.worldObj.isRemote) {
            return;
        }

        UUID playerId = event.entityPlayer.getUniqueID();
        ItemStack ticket = ticketsToRestore.remove(playerId);
        if (ticket == null) {
            return;
        }

        if (findWarTicket(event.entityPlayer.inventory) != null) {
            return;
        }

        if (!event.entityPlayer.inventory.addItemStackToInventory(ticket)) {
            event.entityPlayer.dropPlayerItemWithRandomChoice(ticket, false);
        }
    }

    private void banPlayer(EntityPlayerMP player) {
        String reason = "Вы потратили последнюю жизнь, ожидайте окончания ивента.";

        player.playerNetServerHandler.kickPlayerFromServer(reason);

        System.out.println("[WarMode] Игрок " + player.getCommandSenderName() +
                " исключен: нет военного билета");

        MinecraftServer server = MinecraftServer.getServer();
        if (server != null) {
            server.getConfigurationManager().sendChatMsg(new ChatComponentText(
                    EnumChatFormatting.RED + "☠ " + player.getCommandSenderName() +
                            " потерял последнюю жизнь и выбыл из ивента!"));
        }
    }
}
