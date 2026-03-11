package noname.weapons.render;

import noname.weapons.config.WeaponsConfig;
import noname.weapons.entity.EntityCatapult;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;

import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderCatapult extends Render {
    private IModelCustom model;
    private IModelCustom modelArm1;
    private IModelCustom modelArm2;
    private static final ResourceLocation texture = new ResourceLocation("got", "textures/entity/noname/catapult.png");
    private static final float Y_OFFSET = -0.1F;

    public RenderCatapult() {
        this.shadowSize = 1.0F;
        this.model = AdvancedModelLoader.loadModel(new ResourceLocation("got", "models/catapult_based.obj"));
        this.modelArm1 = AdvancedModelLoader.loadModel(new ResourceLocation("got", "models/catapultArm1.obj"));
        this.modelArm2 = AdvancedModelLoader.loadModel(new ResourceLocation("got", "models/catapultArm2.obj"));
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        EntityCatapult catapult = (EntityCatapult) entity;
        float renderYaw = catapult.prevRotationYaw + (catapult.rotationYaw - catapult.prevRotationYaw) * partialTicks;
        GL11.glPushMatrix();
        GL11.glTranslated(x, y + Y_OFFSET, z);
        GL11.glRotatef(180.0F - renderYaw, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.bindEntityTexture(entity);
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        model.renderAll();
        if (modelArm1 != null) {
            float rotationAngle = calculateArmRotation(catapult, partialTicks);
            GL11.glPushMatrix();
            GL11.glTranslatef(0, 0.2f, 0.0f);
            GL11.glRotatef(rotationAngle, 1, 0, 0);
            GL11.glTranslatef(0, -0.2f, 0.0f);
            modelArm1.renderAll();
            GL11.glPopMatrix();
        }
        if (modelArm2 != null) {
            float spinAngle = calculateArm2Spin(catapult, partialTicks);
            GL11.glPushMatrix();
            GL11.glTranslatef(0, 0.3f, 2.3f);
            GL11.glRotatef(spinAngle, 1, 0, 0);
            GL11.glTranslatef(0, -0.3f, -2.3f);
            modelArm2.renderAll();
            GL11.glPopMatrix();
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    private float calculateArmRotation(EntityCatapult catapult, float partialTicks) {
        float maxRotation = 60.0f;
        int maxReloadTime = WeaponsConfig.reloadTimeCatapult;

        boolean isReloading = (catapult.getDataWatcher().getWatchableObjectByte(17) & 1) != 0;
        boolean isLoaded = (catapult.getDataWatcher().getWatchableObjectByte(21) & 1) != 0;
        int reloadTimer = catapult.getDataWatcher().getWatchableObjectInt(18);

        if (isReloading) {
            float progress = Math.min((float)reloadTimer / (float)maxReloadTime, 1.0f);
            return maxRotation * progress;
        } else if (isLoaded) {
            return maxRotation;
        } else {
            return 0.0f;
        }
    }

    private float calculateArm2Spin(EntityCatapult catapult, float partialTicks) {
        boolean isReloading = (catapult.getDataWatcher().getWatchableObjectByte(17) & 1) != 0;

        if (!isReloading) {
            return 0.0f;
        }

        int reloadTimer = catapult.getDataWatcher().getWatchableObjectInt(18);
        float rotationSpeed = 2.0f;
        float angle = (reloadTimer + partialTicks) * rotationSpeed;
        return angle % 360.0f;
    }


    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }
}
