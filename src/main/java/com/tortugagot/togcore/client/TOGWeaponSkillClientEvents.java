package com.tortugagot.togcore.client;

import com.tortugagot.togcore.technology.TOGWeaponTechnology;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class TOGWeaponSkillClientEvents {
    private boolean shakeApplied;
    private float appliedPitch;
    private float appliedYaw;

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            removeShake();
            return;
        }

        Minecraft minecraft = Minecraft.getMinecraft();
        EntityClientPlayerMP player = minecraft.thePlayer;
        EntityLivingBase camera = minecraft.renderViewEntity;
        if (player == null || camera == null) {
            return;
        }

        ItemStack heldItem = player.getHeldItem();
        if (!shouldShake(player, heldItem)) {
            return;
        }

        float ticks = player.ticksExisted + event.renderTickTime;
        appliedPitch = MathHelper.sin(ticks * 1.35F) * 1.25F + MathHelper.sin(ticks * 2.7F) * 0.35F;
        appliedYaw = MathHelper.cos(ticks * 1.15F) * 0.9F;
        camera.rotationPitch += appliedPitch;
        camera.rotationYaw += appliedYaw;
        shakeApplied = true;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            TOGClientPassiveCounterData.tick();
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.HOTBAR || !TOGClientPassiveCounterData.isVisible()) {
            return;
        }
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.thePlayer == null || minecraft.fontRenderer == null) {
            return;
        }
        String text = TOGClientPassiveCounterData.getDisplayText();
        int x = event.resolution.getScaledWidth() / 2 - minecraft.fontRenderer.getStringWidth(text) / 2;
        int y = event.resolution.getScaledHeight() - 68;
        minecraft.fontRenderer.drawStringWithShadow(text, x, y, TOGClientPassiveCounterData.getColor());
    }

    private void removeShake() {
        if (!shakeApplied) {
            return;
        }
        EntityLivingBase camera = Minecraft.getMinecraft().renderViewEntity;
        if (camera != null) {
            camera.rotationPitch -= appliedPitch;
            camera.rotationYaw -= appliedYaw;
        }
        shakeApplied = false;
        appliedPitch = 0.0F;
        appliedYaw = 0.0F;
    }

    private boolean shouldShake(EntityClientPlayerMP player, ItemStack heldItem) {
        if (!TOGWeaponTechnology.hasUnskilledArcherClient(heldItem)) {
            return false;
        }
        if (TOGWeaponTechnology.isCrossbow(heldItem)) {
            return true;
        }
        return TOGWeaponTechnology.isBow(heldItem) && player.isUsingItem() && player.getItemInUse() != null;
    }
}
