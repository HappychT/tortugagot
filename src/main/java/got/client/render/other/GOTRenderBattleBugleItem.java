package got.client.render.other;

import org.lwjgl.opengl.GL11;

import got.client.model.GOTModelBattleBugle;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

/**
 * Рендер горна в руке: своя ModelBase + текстура (как GOTRenderSkullStaff).
 * GeoItemRenderer в бэкпорте GeckoLib под 1.7.10 даёт NPE — не используем.
 */
public class GOTRenderBattleBugleItem implements IItemRenderer {

    private static final ResourceLocation TEXTURE = new ResourceLocation("got", "textures/items/battle_bugle.png");
    private static final GOTModelBattleBugle MODEL = new GOTModelBattleBugle();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        GL11.glPushMatrix();
        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glTranslatef(0.5f, 0.4f, 0f);
            GL11.glRotatef(-30f, 0f, 0f, 1f);
            GL11.glRotatef(100f, 0f, 1f, 0f);
            GL11.glScalef(0.08f, 0.08f, 0.08f);
        } else {
            GL11.glScalef(-1f, 1f, 1f);
            GL11.glTranslatef(-1.2f, 0.15f, 0.1f);
            GL11.glRotatef(-45f, 0f, 0f, 1f);
            GL11.glRotatef(90f, 0f, 1f, 0f);
            GL11.glScalef(0.07f, 0.07f, 0.07f);
        }
        MODEL.renderAsItem(0.0625f);
        GL11.glPopMatrix();
    }
}
