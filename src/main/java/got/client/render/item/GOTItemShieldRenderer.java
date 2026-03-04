package got.client.render.item;

import got.client.render.other.GOTRenderShield;
import got.common.GOTLevelData;
import got.common.database.GOTShields;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;

public class GOTItemShieldRenderer implements IItemRenderer {

    private IItemRenderer baseRenderer;
    private static Field timerField = null;
    private static float getPartialTicks() {
        try {
            if (timerField == null) {
                try {
                    timerField = Minecraft.class.getDeclaredField("timer");
                } catch (NoSuchFieldException e) {
                    timerField = Minecraft.class.getDeclaredField("field_71428_T");
                }
                timerField.setAccessible(true);
            }
            net.minecraft.util.Timer t = (net.minecraft.util.Timer) timerField.get(Minecraft.getMinecraft());
            return t.renderPartialTicks;
        } catch (Exception e) {
            return 0.0F;
        }
    }
    public GOTItemShieldRenderer(IItemRenderer baseRenderer) {
        this.baseRenderer = baseRenderer;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return (baseRenderer != null && baseRenderer.handleRenderType(item, type))
                || type == ItemRenderType.EQUIPPED
                || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        if (baseRenderer != null) {
            return baseRenderer.shouldUseRenderHelper(type, item, helper);
        }
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        try {
            GL11.glPushMatrix();
            if (baseRenderer != null) {
                baseRenderer.renderItem(type, item, data);
            } else {
                renderDefaultWeapon(item, type);
            }
            GL11.glPopMatrix();

            if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
                GOTShields shieldToRender = getShieldForPlayer(item, data, type);

                if (shieldToRender != null) {
                    if (type == ItemRenderType.EQUIPPED) {
                        if (data.length >= 2 && data[1] instanceof EntityLivingBase) {
                            GOTRenderShield.renderShield(shieldToRender, (EntityLivingBase) data[1], (ModelBiped) data[0]);
                        }
                    }
                    else {
                        renderShieldFirstPerson(item, shieldToRender);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            GL11.glPopMatrix();
            GL11.glPopAttrib();

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
        }
    }
    private void renderShieldFirstPerson(ItemStack item, GOTShields shield) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        boolean isBlocking = (player != null && player.isUsingItem() && player.getItemInUse() == item);

        GL11.glPushMatrix();

        GL11.glLoadIdentity();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        float partialTicks = getPartialTicks();

        float rawDist = player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * partialTicks;

        float bobFrequency = rawDist * 0.6F;

        float bobX = (float) Math.sin(bobFrequency) * 0.015F;
        float bobY = (float) -Math.abs(Math.cos(bobFrequency * 2.0F)) * 0.015F;

        float breathArg = (player.ticksExisted + partialTicks) / 25.0F;
        float breath = (float) Math.sin(breathArg) * 0.002F;


        GL11.glTranslatef(bobX, bobY + breath, 0.0F);

        GL11.glRotatef(bobX * 15.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(bobY * 10.0F, 1.0F, 0.0F, 0.0F);


        if (isBlocking) {
            GL11.glTranslatef(-0.6F, -0.45F, -0.45F);
        } else {
            GL11.glTranslatef(-0.9F, -0.55F, -0.45F);
        }

        GL11.glScalef(0.65F, 0.65F, 0.65F);

        GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(5.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(-5.0F, 1.0F, 0.0F, 0.0F);

        GL11.glDisable(GL11.GL_CULL_FACE);
        GOTRenderShield.renderShieldFirstPerson(shield, false);
        GL11.glEnable(GL11.GL_CULL_FACE);

        GL11.glPopMatrix();
    }
    private void renderDefaultWeapon(ItemStack item, ItemRenderType type) {
        // Стандартный рендер, если нет спец-модели оружия
        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON || type == ItemRenderType.EQUIPPED) {
            GL11.glPushMatrix();
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
            if (type == ItemRenderType.EQUIPPED) GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            IIcon icon = item.getIconIndex();
            if (icon != null) {
                ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
            }
            GL11.glPopMatrix();
        }
    }

    private GOTShields getShieldForPlayer(ItemStack item, Object[] data, ItemRenderType type) {
        EntityPlayer player = null;
        if (data.length >= 2 && data[1] instanceof EntityLivingBase) {
            if (data[1] instanceof EntityPlayer) player = (EntityPlayer) data[1];
        }
        if (player == null && type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            player = Minecraft.getMinecraft().thePlayer;
        }
        GOTShields shield = (player != null) ? GOTLevelData.getData(player).getShield() : GOTShields.SHIELD_WEAPON;
        return (shield == null) ? GOTShields.SHIELD_WEAPON : shield;
    }
}