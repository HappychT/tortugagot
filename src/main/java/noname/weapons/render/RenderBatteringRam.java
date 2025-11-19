package noname.weapons.render;

import noname.weapons.entity.EntityBatteringRam;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBatteringRam extends Render {

    private IModelCustom model;
    private IModelCustom modelArm1;
    private long lastAnimationStartTime = 0;
    private boolean wasBreaking = false;
    private float currentAnimationTime = 0.0f;
    private int lastProcessedAnimationState = 0;

    private static final ResourceLocation texture = new ResourceLocation("got", "textures/entity/noname/taran.png");

    public RenderBatteringRam() {
        this.shadowSize = 1.8F;
        this.model = AdvancedModelLoader.loadModel(new ResourceLocation("got", "models/taran_based.obj"));
        this.modelArm1 = AdvancedModelLoader.loadModel(new ResourceLocation("got", "models/taranArm1.obj"));
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        EntityBatteringRam batteringRam = (EntityBatteringRam) entity;
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
            float rotationAngle = calculateArmRotation(batteringRam, partialTicks);
            GL11.glPushMatrix();
            GL11.glTranslatef(0, 1.2f, 0.0f);
            GL11.glRotatef(rotationAngle, 1, 0, 0);
            GL11.glTranslatef(0, -1.2f, 0.0f);
            modelArm1.renderAll();
            GL11.glPopMatrix();
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    private float calculateArmRotation(EntityBatteringRam batteringRam, float partialTicks) {
        int animationState = batteringRam.getDataWatcher().getWatchableObjectInt(25);

        boolean newTrigger = animationState != 0 && animationState != lastProcessedAnimationState;

        if (newTrigger) {
            lastAnimationStartTime = System.currentTimeMillis();
            wasBreaking = true;
            lastProcessedAnimationState = animationState;
        }

        if (!wasBreaking) {
            return 0.0f;
        }

        float maxAngle = 10.0f;
        float cycleDuration = 2.0f;
        long currentTime = System.currentTimeMillis();
        float elapsedSeconds = (currentTime - lastAnimationStartTime) / 1000.0f;

        if (elapsedSeconds >= cycleDuration) {
            wasBreaking = false;
            currentAnimationTime = 0.0f;
            return 0.0f;
        }

        float normalizedTime = elapsedSeconds / cycleDuration;
        float angle;
        if (normalizedTime < 0.5f) {
            float phaseProgress = normalizedTime / 0.5f;
            angle = -maxAngle * smoothStep(phaseProgress);
        }
        else {
            float phaseProgress = (normalizedTime - 0.5f) / 0.5f;
            angle = -maxAngle * (1.0f - smoothStep(phaseProgress));
        }

        currentAnimationTime = angle;
        return angle;
    }

    private float smoothStep(float t) {
        return t * t * (3.0f - 2.0f * t);
    }


    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }
}

