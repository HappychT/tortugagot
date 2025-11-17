package noname.weapons.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import noname.weapons.entity.EntityTribushet;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderTribushet extends Render {
    private IModelCustom model;
    private IModelCustom modelArm1;
    private IModelCustom modelArm2;
    private static final ResourceLocation texture = new ResourceLocation("got", "textures/entity/noname/trebuchet.png");

    public RenderTribushet() {
        this.shadowSize = 1.0F;
        try {
            this.model = AdvancedModelLoader.loadModel(new ResourceLocation("got", "models/trebuchet_based.obj"));
        } catch (Exception ignored) {
            this.model = null;
        }

        try {
            this.modelArm1 = AdvancedModelLoader.loadModel(new ResourceLocation("got", "models/trebuchetArm1.obj"));
        } catch (Exception ignored) {
            this.modelArm1 = null;
        }
        try {
            this.modelArm2 = AdvancedModelLoader.loadModel(new ResourceLocation("got", "models/trebuchetArm2.obj"));
        } catch (Exception ignored) {
            this.modelArm2 = null;
        }
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        if (model == null) {
            return;
        }

        EntityTribushet tribushet = (EntityTribushet) entity;
        GL11.glPushMatrix();
        GL11.glTranslated(x, y + 0.5, z);
        GL11.glRotatef(180.0F - yaw, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.bindEntityTexture(entity);
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        model.renderAll();
        if (modelArm1 != null) {
            float rotationAngle = calculateArmRotation(tribushet, partialTicks);
            GL11.glPushMatrix();
            GL11.glTranslatef(0, 8.0f, -0.7f);
            GL11.glRotatef(rotationAngle, 1, 0, 0);
            GL11.glTranslatef(0, -8.0f, 0.7f);
            modelArm1.renderAll();
            GL11.glPopMatrix();
        }
        if (modelArm2 != null) {
            float spinAngle = calculateArm2Spin(tribushet, partialTicks);
            GL11.glPushMatrix();
            GL11.glTranslatef(0, 1.55f, 5.52f);
            GL11.glRotatef(spinAngle, 1, 0, 0);
            GL11.glTranslatef(0, -1.55f, -5.52f);
            modelArm2.renderAll();
            GL11.glPopMatrix();
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    private float calculateArmRotation(EntityTribushet tribushet, float partialTicks) {
        float maxRotation = 80.0f;
        float time = (tribushet.ticksExisted + partialTicks) / 20.0f;
        float progress = (float) Math.sin(time * 0.5f) * 0.5f + 0.5f;
        return maxRotation * progress;
    }

    private float calculateArm2Spin(EntityTribushet tribushet, float partialTicks) {
        float rotationSpeed = 2.0f;
        float angle = (tribushet.ticksExisted + partialTicks) * rotationSpeed;
        return angle % 360.0f;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }
}
