package com.tortugagot.togcore.technology;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TOGWeaponSkillEvents {
    private final Map<UUID, String> activeWarnings = new HashMap<UUID, String>();

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (!(event.source.getEntity() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.source.getEntity();
        ItemStack heldItem = player.getHeldItem();
        if (TOGWeaponTechnology.hasUnskilledWarrior(player, heldItem)) {
            event.ammount *= TOGWeaponTechnology.UNSKILLED_WARRIOR_DAMAGE_FACTOR;
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.worldObj.isRemote) {
            return;
        }
        EntityPlayer player = event.player;
        UUID playerId = player.getUniqueID();
        ItemStack heldItem = player.getHeldItem();
        String effectKey = getServerEffectKey(player, heldItem);
        if (effectKey == null) {
            activeWarnings.remove(playerId);
            return;
        }
        if (effectKey.equals(activeWarnings.get(playerId))) {
            return;
        }
        activeWarnings.put(playerId, effectKey);
        player.addChatMessage(new ChatComponentText(getMessage(effectKey)));
    }

    private String getServerEffectKey(EntityPlayer player, ItemStack heldItem) {
        if (TOGWeaponTechnology.hasUnskilledWarrior(player, heldItem)) {
            String technologyId = TOGWeaponTechnology.getRequiredTechnology(heldItem);
            return technologyId != null ? "unskilled_warrior_" + technologyId : null;
        }
        if ((TOGWeaponTechnology.isBow(heldItem) || TOGWeaponTechnology.isCrossbow(heldItem))
                && !TOGWeaponTechnology.hasRequiredTechnology(player, heldItem)) {
            return TOGWeaponTechnology.isCrossbow(heldItem) ? "unskilled_archer_crossbow" : "unskilled_archer_bow";
        }
        return null;
    }

    private String getMessage(String effectKey) {
        if (effectKey != null && effectKey.startsWith("unskilled_archer")) {
            return "Неумелый стрелок: нет нужной технологии владения оружием.";
        }
        return "Неумелый воин: нет нужной технологии владения оружием.";
    }
}
