package got.client.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import got.common.handlers.StaminaServerHandler;
import got.rome.ExtendedPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

public class GOTInterfaceHandler {

    public static final GOTInterfaceHandler INSTANCE = new GOTInterfaceHandler();
    private float interpolatedStamina = 0;
    private float interpolatedBounce = 0;
    DecimalFormat df = new DecimalFormat("#.#");

    @SubscribeEvent
    public void onOverlayPostMy(RenderGameOverlayEvent.Post e) {
        if (e.type != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        int w = e.resolution.getScaledWidth();
        int h = e.resolution.getScaledHeight();
        EntityClientPlayerMP player = mc.thePlayer;

        int aX = w / 2 - 10;
        int aY = h + 40;

        GL11.glPushMatrix();

        float disposX = 140;
        float disposY = 40;
        float scaleFactor = 2f;

        float newPosX = aX - 89.5f;
        float newPosY = aY - 114 + disposY;
        float staminaPosX = newPosX + 10;
        float staminaPosY = newPosY + 5.5f;
        newPosX /= scaleFactor;
        newPosY /= scaleFactor;
        staminaPosX /= scaleFactor;
        staminaPosY /= scaleFactor;

        GL11.glScalef(scaleFactor, scaleFactor, 1.0f);

        // Credits for the art goes to the original author (bdragon1727)
        // https://bdragon1727.itch.io/basic-pixel-health-bar-and-scroll-bar
        // IF is being used in commercial matters, should be replaced with a custom art or any payment(even 1$) should be made to the original author

        mc.getTextureManager().bindTexture(new ResourceLocation("got:textures/hud/stamina_bar.png"));
        drawTextureCustomSize(newPosX, newPosY, 0, 0, 100, 7, 100, 7);
        
        float currentStamina = ExtendedPlayer.get(player).getStamina();
//      interpolatedStamina += (currentStamina - interpolatedStamina) * 0.1f; // Adjust the 0.1f value for smoother or faster interpolation
        mc.getTextureManager().bindTexture(new ResourceLocation("got:textures/hud/stamina.png"));
        drawTextureCustomSize(staminaPosX, staminaPosY, 0, 0, ((currentStamina) * 89.5d) / StaminaServerHandler.MAX_STAMINA, 2.4f, 89.5f, 2.4f); // put interpolatedStamina instead of currentStamina to return lerp(smooth transition animation)

        float cooldownPosX = (aX - 260 + disposX) / scaleFactor;
        float cooldownPosY = (aY - 99.2F + disposY) / scaleFactor;
        int secondBreathCooldown = ExtendedPlayer.get(player).getSecondBreathCooldown();
        if (secondBreathCooldown > 0) {
            String cooldownText = String.valueOf(secondBreathCooldown / 20  + 1);
            mc.fontRenderer.drawString(cooldownText, (int) cooldownPosX, (int) cooldownPosY, 0xFFFFFF);
        }

        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glScalef(scaleFactor, scaleFactor, 1.0f);

//        float newbounceX = aX + 103;
//        float newbounceY = aY - 63;
        float newbounceX = aX + 103;
        float newbounceY = aY - 85;
        newbounceX /= scaleFactor;
        newbounceY /= scaleFactor;

        mc.getTextureManager().bindTexture(new ResourceLocation("got:textures/hud/bounce.png"));
        drawTextureCustomSize(newbounceX, newbounceY, 0, 0, 14, 12, 14, 12);

        float currentBounce = ExtendedPlayer.get(player).getBounceCooldown();
        float maxBounceCooldown = 20 * 3; // Maximum cooldown value
        interpolatedBounce += (currentBounce - interpolatedBounce) * 0.1f; // Adjust the 0.1f value for smoother or faster interpolation
        mc.getTextureManager().bindTexture(new ResourceLocation("got:textures/hud/bounce_bar.png"));
        drawTextureCustomSize(newbounceX, newbounceY, 0, 0, 14, ((interpolatedBounce  * 12) / maxBounceCooldown), 14, 12);
        int cooldownBouncePosX = (aX + 135) / 2;
        int cooldownBouncePosY = (aY - 81) / 2;
        if (currentBounce> 0) {
            String cooldownText = String.valueOf(Math.round((currentBounce / 20) * 10.0f) / 10.0f);
            mc.fontRenderer.drawString(cooldownText, cooldownBouncePosX, cooldownBouncePosY, 0xFFFFFF);
        }

        GL11.glPopMatrix();

//        GL11.glPushMatrix();
//        GL11.glScalef(scaleFactor, scaleFactor, 1.0f);
//        mc.getTextureManager().bindTexture(new ResourceLocation("got:textures/hud/stamina.png"));
//        float staminaPosX = (aX - 33 + disposX) / scaleFactor;
//        float staminaPosY = (aY - 100 + disposY) / scaleFactor;
//        int fix = 0;
//        float currentStamina = ExtendedPlayer.get(player).getStamina();
//        if (currentStamina == StaminaServerHandler.MAX_STAMINA) {
//            fix = 0;
//        }
//        interpolatedStamina += (currentStamina - interpolatedStamina) * 0.1f; // Adjust the 0.1f value for smoother or faster interpolation
//        drawTextureCustomSize(staminaPosX, staminaPosY, 0, 0, (double) ((currentStamina + fix) * 100) / StaminaServerHandler.MAX_STAMINA, 8, 100, 8); // put interpolatedStamina instead of currentStamina to return lerp(smooth transition animation)
//
//        float cooldownPosX = (aX - 260 + disposX) / scaleFactor;
//        float cooldownPosY = (aY - 99.2F + disposY) / scaleFactor;
//        int secondBreathCooldown = ExtendedPlayer.get(player).getSecondBreathCooldown();
//        if (secondBreathCooldown > 0) {
//            String cooldownText = String.valueOf(secondBreathCooldown / 20  + 1);
//            mc.fontRenderer.drawString(cooldownText, (int) cooldownPosX, (int) cooldownPosY, 0xFFFFFF);
//        }
//
//        GL11.glPopMatrix();

    }

    public static void drawTextureCustomSize(double posX, double posY, double startPixX, double startPixY, double pieceSizeX, double pieceSizeY, float sizeTextureX, float sizeTextureY) {
        float f4 = 1.0F / sizeTextureX;
        float f5 = 1.0F / sizeTextureY;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(posX, posY + pieceSizeY, 0.0D, startPixX * f4, (startPixY + pieceSizeY) * f5);
        tessellator.addVertexWithUV(posX + pieceSizeX, posY + pieceSizeY, 0.0D, (startPixX + pieceSizeX) * f4, (startPixY + pieceSizeY) * f5);
        tessellator.addVertexWithUV(posX + pieceSizeX, posY, 0.0D, (startPixX + pieceSizeX) * f4, startPixY * f5);
        tessellator.addVertexWithUV(posX, posY, 0.0D, startPixX * f4, startPixY * f5);
        tessellator.draw();
    }
}
