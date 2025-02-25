package got.common.potions;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class GOTCustomPotion extends Potion {
    private ResourceLocation texture;

    public GOTCustomPotion(int id, boolean isBad, int fluidColor, String namePot) {
        this(id, isBad, fluidColor, new ResourceLocation("got", "textures/potion/" + namePot + ".png"), "got.potion." + namePot);
    }

    public GOTCustomPotion(int id, boolean isBad, int fluidColor, ResourceLocation tex, String namePot) {
        super(id, isBad, fluidColor);
        setPotionName(namePot);
        this.texture = tex;
    }

    @Override
    public boolean hasStatusIcon()
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(this.texture);
        return true;
    }

    public ResourceLocation getIcon() {

        return this.texture;
    }

    @Override
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        mc.renderEngine.bindTexture(this.texture);
        drawTexturedRect(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
    }

    public static void drawTexturedRect(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight)
    {
        float f = 1F / (float)textureWidth;
        float f1 = 1F / (float)textureHeight;
        net.minecraft.client.renderer.Tessellator tessellator = net.minecraft.client.renderer.Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, 0, (float)(u) * f, (float)(v + height) * f1);
        tessellator.addVertexWithUV(x + width, y + height, 0, (float)(u + width) * f, (float)(v + height) * f1);
        tessellator.addVertexWithUV(x + width, y, 0, (float)(u + width) * f, (float)(v) * f1);
        tessellator.addVertexWithUV(x, y, 0, (float)(u) * f, (float)(v) * f1);
        tessellator.draw();
    }

    @Override
    public Potion setIconIndex(int par1, int par2)
    {
        super.setIconIndex(par1, par2);
        return this;
    }

    @Override
    @SideOnly(value= Side.CLIENT)
    public int getStatusIconIndex() {
        Minecraft.getMinecraft().renderEngine.bindTexture(this.texture);
        return super.getStatusIconIndex();
    }
}
