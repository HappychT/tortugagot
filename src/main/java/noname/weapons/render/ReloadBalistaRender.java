package noname.weapons.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import noname.weapons.config.WeaponsConfig;
import noname.weapons.entity.EntityBalista;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ReloadBalistaRender extends Gui {

    private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation("got", "textures/gui/reload_bar.png");

    public void render() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityClientPlayerMP player = mc.thePlayer;
        if (player == null) return;

        if (player.ridingEntity == null || !(player.ridingEntity instanceof EntityBalista)) {
            return;
        }

        EntityBalista balista = (EntityBalista) player.ridingEntity;
        ScaledResolution scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int screenWidth = scaledResolution.getScaledWidth();
        int screenHeight = scaledResolution.getScaledHeight();

        float rangePower = balista.getDataWatcher().getWatchableObjectFloat(28);
        int rangePct = (int)(rangePower * 100F);
        String rangeText = "Дальность: " + rangePct + "% (W/S)";
        int rangeBarWidth = 120;
        int rangeBarX = (screenWidth - rangeBarWidth) / 2;
        int rangeBarY = screenHeight - 72;
        mc.fontRenderer.drawStringWithShadow(rangeText, (screenWidth - mc.fontRenderer.getStringWidth(rangeText)) / 2, rangeBarY - 12, 0xFFFFFF);
        drawRect(rangeBarX, rangeBarY, rangeBarX + rangeBarWidth, rangeBarY + 6, 0x80000000);
        drawRect(rangeBarX, rangeBarY, rangeBarX + (int)(rangeBarWidth * rangePower), rangeBarY + 6, 0xFF4080FF);

        boolean isReloading = (balista.getDataWatcher().getWatchableObjectByte(17) & 1) != 0;
        int reloadTimer = balista.getDataWatcher().getWatchableObjectInt(18);

        if (!isReloading) return;

        int maxReloadTime = (int) WeaponsConfig.reloadTimeBalista;
        float percentage = Math.max(0f, Math.min(1f, (float) reloadTimer / (float) maxReloadTime));

        int computedWidth = 182;
        int posX = (screenWidth / 2) - (computedWidth / 2); 
        int posY = screenHeight - 51; 

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.getTextureManager().bindTexture(OVERLAY_TEXTURE);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        renderReloadBar(posX, posY, percentage, computedWidth);

        int remainingTicks = maxReloadTime - reloadTimer;
        float remainingSeconds = remainingTicks / 20.0f;
        String timeText = String.format("Перезарядка: %.1f сек.", remainingSeconds);

        mc.fontRenderer.drawStringWithShadow(
                timeText,
                posX + (computedWidth - mc.fontRenderer.getStringWidth(timeText)) / 2,
                posY - 10, 
                0xFFFFFF
        );

        GL11.glDisable(GL11.GL_BLEND);
        mc.getTextureManager().bindTexture(Gui.icons);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    private void renderReloadBar(int posX, int posY, float percentage, int computedWidth) {
        drawTexturedModalRect(posX, posY, 0, 0, 2, 12);
        for (int i = 2; i <= computedWidth - 2; i++) {
            drawTexturedModalRect(posX + i, posY, 2, 0, 1, 12);
        }
        drawTexturedModalRect(posX + computedWidth - 1, posY, 3, 0, 2, 12);
        int pixelPercentage = (int) ((computedWidth * percentage) + 2);
        drawTexturedModalRect(posX, posY, 0, percentage == 0 ? 0 : 26, 2, 12);
        for (int i = 2; i <= computedWidth - 2; i++) {
            drawTexturedModalRect(posX + i, posY, 2, i < pixelPercentage ? 26 : 0, 1, 12);
        }
        drawTexturedModalRect(posX + computedWidth - 1, posY, 3, percentage >= 1 ? 26 : 0, 2, 12);
    }
}
