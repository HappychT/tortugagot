package got.client.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import got.common.GOTConfig;
import got.common.handlers.BlockServerHandler;
import got.common.systems.GOTCoreBlockingSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.Timer;

public class GOTBlockClientHandler {

    public static final GOTBlockClientHandler INSTANCE = new GOTBlockClientHandler();


    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;

        if (BlockServerHandler.INSTANCE.isBlocking(player) && true) { // Put true instead of false to enable the block angle overlay debug feature
            float[] blockAngles = {GOTCoreBlockingSystem.getBlockData(player.getHeldItem().getItem().getClass(), player).getLeftBlockAngle(), GOTCoreBlockingSystem.getBlockData(player.getHeldItem().getItem().getClass(), player).getRightBlockAngle()};
            // if(GOTConfig.enableBlockArc) {
            //    renderBlockAngleOverlay(blockAngles);
            // }
        }
    }

    private void renderBlockAngleOverlay(float[] blockAngles) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;

        float partialTicks = 0.0f;
        try {
            Field timerField;
            try {
                timerField = Minecraft.class.getDeclaredField("timer");
            } catch (NoSuchFieldException e) {
                // Handle obfuscated field name
                timerField = Minecraft.class.getDeclaredField("field_71428_T"); // Example obfuscated name
            }
            timerField.setAccessible(true);
            Object timer = timerField.get(mc);
            Field renderPartialTicksField;
            try {
                renderPartialTicksField = timer.getClass().getDeclaredField("renderPartialTicks");
            } catch (NoSuchFieldException e) {
                // Handle obfuscated field name
                renderPartialTicksField = timer.getClass().getDeclaredField("field_74285_i"); // Example obfuscated name
            }
            renderPartialTicksField.setAccessible(true);
            partialTicks = renderPartialTicksField.getFloat(timer);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        double playerX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
        double playerY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
        double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;

        double radius = 2.0; // Radius of the arc
        double centerX = playerX;
        double centerY = playerY + player.getEyeHeight();
        double centerZ = playerZ;
        double startAngle = -blockAngles[0]; // -35
        double endAngle   =  blockAngles[1]; // +35
        float  playerYaw  = (player.rotationYaw % 360 + 360) % 360;

        double realForward = playerYaw + 90.0;

        GL11.glPushMatrix();
        for (double angle = startAngle; angle <= endAngle; angle += 5) {
            double actualAngle = realForward + angle;
            double rad = Math.toRadians(actualAngle);
            double x = player.posX + Math.cos(rad) * radius;
            double z = player.posZ + Math.sin(rad) * radius;
            double y = player.posY + player.getEyeHeight();

            mc.theWorld.spawnParticle("reddust", x, y, z, 0.0, 0.0, 0.0);
        }
        GL11.glPopMatrix();
    }

}