package noname.weapons;

import got.common.GOTBannerProtection;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

import java.util.HashSet;
import java.util.Set;

public class WarManager {

    private static boolean warActive = false;
private static final Set<String> warBannedPlayers = new HashSet<String>();
    public static final String WAR_BAN_MESSAGE = "Вы потратили последнюю жизнь, ожидайте окончания ивента.";

    public static boolean isWarActive() {
        return warActive;
    }

    public static void setWarActive(boolean active) {
        warActive = active;
    }

    public static void banPlayerForWar(EntityPlayerMP player) {
        MinecraftServer server = MinecraftServer.getServer();
        if (server == null) {
            return;
        }
        String playerName = player.getCommandSenderName();
        if (warBannedPlayers.contains(playerName)) {
            return;
        }
        player.addChatMessage(new ChatComponentText("§c" + WAR_BAN_MESSAGE));
        String command = "ban " + playerName + " \"" + WAR_BAN_MESSAGE + "\"";
        server.getCommandManager().executeCommand(server, command);
        warBannedPlayers.add(playerName);
    }

    public static void unbanWarPlayers() {
        MinecraftServer server = MinecraftServer.getServer();
        if (server == null) {
            return;
        }
        for (String playerName : new HashSet<>(warBannedPlayers)) {
            String command = "pardon " + playerName;
            server.getCommandManager().executeCommand(server, command);
        }
        warBannedPlayers.clear();
    }

    public static boolean isWarSoilBlock(Block block) {
        if (block == null) {
            return false;
        }
        if (block == Blocks.dirt || block == Blocks.grass || block == Blocks.farmland || block == Blocks.mycelium) {
            return true;
        }
        Material m = block.getMaterial();
        return m == Material.ground || m == Material.grass;
    }

    public static boolean isWarSoilItem(ItemStack stack) {
        if (stack == null) return false;
        Item item = stack.getItem();
        if (!(item instanceof ItemBlock)) return false;
        Block block = ((ItemBlock) item).field_150939_a;
        return isWarSoilBlock(block);
    }

    public static boolean shouldCancelProtectedBreak(BlockEvent.BreakEvent event) {
        World world = event.world;
        EntityPlayer player = event.getPlayer();
        Block block = event.block;

        if (world.isRemote || player == null) {
            return false;
        }

        if (!GOTBannerProtection.isProtected(world, event.x, event.y, event.z,
                GOTBannerProtection.forPlayer(player, GOTBannerProtection.Permission.FULL), false)) {
            return false;
        }

        if (isWarActive() && isWarSoilBlock(block)) {
            return false;
        }

        GOTBannerProtection.isProtected(world, event.x, event.y, event.z,
                GOTBannerProtection.forPlayer(player, GOTBannerProtection.Permission.FULL), true);
        return true;
    }

    public static boolean handleProtectedInteract(PlayerInteractEvent event,
                                                  GOTBannerProtection.Permission perm,
                                                  Block clickedBlock) {
        World world = event.entityPlayer.worldObj;
        EntityPlayer player = event.entityPlayer;

        if (world.isRemote) {
            return false;
        }

        boolean protectedLand = GOTBannerProtection.isProtected(world, event.x, event.y, event.z,
                GOTBannerProtection.forPlayer(player, perm), false);

        if (!protectedLand) {
            return false;
        }

        ItemStack held = player.getCurrentEquippedItem();
        if (!isWarActive() || held == null || !isWarSoilItem(held) || event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            GOTBannerProtection.isProtected(world, event.x, event.y, event.z,
                    GOTBannerProtection.forPlayer(player, perm), true);
            event.setCanceled(true);
            return true;
        }

        if (!canPlaceWarSoilHere(world, event.x, event.y, event.z, event.face, clickedBlock)) {
            player.addChatMessage(new ChatComponentText("§cЗемлю можно ставить только сверху и не более 4 блоков столбом."));
            event.setCanceled(true);
            return true;
        }

        return false;
    }

    private static boolean canPlaceWarSoilHere(World world, int x, int y, int z, int side, Block clickedBlock) {
        int tx = x;
        int ty = y;
        int tz = z;

        switch (side) {
            case 0:
                ty = y - 1;
                break;
            case 1:
                ty = y + 1;
                break;
            case 2:
                tz = z - 1;
                break;
            case 3:
                tz = z + 1;
                break;
            case 4:
                tx = x - 1;
                break;
            case 5:
                tx = x + 1;
                break;
        }

        Block target = world.getBlock(tx, ty, tz);

        if (target == Blocks.water || target == Blocks.flowing_water) {
            return true;
        }

        if (side != 1) {
            return false;
        }

        Block below = world.getBlock(tx, ty - 1, tz);
        if (!isWarSoilBlock(below)) {
            return false;
        }

        int count = 0;
        int cy = ty - 1;
        while (cy >= 0 && count < 4) {
            Block b = world.getBlock(tx, cy, tz);
            if (!isWarSoilBlock(b)) {
                break;
            }
            count++;
            cy--;
        }
        return count < 4;
    }
}


