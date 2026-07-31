package com.tortugagot.togcore.technology;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.util.HashMap;
import java.util.Map;

public final class TOGTechnologyNotifier {
    private static final int DEFAULT_COOLDOWN_TICKS = 20;
    private static final Map<String, Long> LAST_NOTIFICATIONS = new HashMap<String, Long>();

    private TOGTechnologyNotifier() {
    }

    public static void notifyBlocked(EntityPlayer player, String key, String action, String reason, String technologyId) {
        notify(player, key, buildMessage(action, reason, technologyId), DEFAULT_COOLDOWN_TICKS);
    }

    public static void notifyBlockedNoTechnology(EntityPlayer player, String key, String action, String reason) {
        notify(player, key, buildNoTechnologyMessage(action, reason), DEFAULT_COOLDOWN_TICKS);
    }

    public static void notifyMessage(EntityPlayer player, String key, String message) {
        notify(player, key, message, DEFAULT_COOLDOWN_TICKS);
    }

    public static void notifyComponent(EntityPlayer player, String key, IChatComponent component) {
        notify(player, key, component, DEFAULT_COOLDOWN_TICKS);
    }

    public static String buildMessage(String action, String reason, String technologyId) {
        return "Не удалось: " + action + ". Причина: " + reason + ". Требуется технология: \"" + technologyName(technologyId) + "\".";
    }

    public static String buildNoTechnologyMessage(String action, String reason) {
        return "Не удалось: " + action + ". Причина: " + reason + ". Требуемая технология: не требуется.";
    }

    public static String technologyName(String technologyId) {
        TOGTechnology technology = TOGTechnologyRegistry.get(technologyId);
        return technology != null ? technology.getName() : technologyId;
    }

    private static void notify(EntityPlayer player, String key, String message, int cooldownTicks) {
        if (message == null || message.length() == 0) {
            return;
        }
        notify(player, key, new ChatComponentText(message), cooldownTicks);
    }

    private static void notify(EntityPlayer player, String key, IChatComponent component, int cooldownTicks) {
        if (player == null || player.worldObj == null || player.worldObj.isRemote || component == null) {
            return;
        }
        String notificationKey = player.getUniqueID().toString() + ":" + key;
        long now = player.worldObj.getTotalWorldTime();
        Long last = LAST_NOTIFICATIONS.get(notificationKey);
        if (last != null && now - last < cooldownTicks) {
            return;
        }
        if (LAST_NOTIFICATIONS.size() > 2048) {
            LAST_NOTIFICATIONS.clear();
        }
        LAST_NOTIFICATIONS.put(notificationKey, now);
        player.addChatMessage(component);
    }
}
