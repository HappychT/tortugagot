package noname.weapons.render;

import noname.weapons.config.WeaponsConfig;
import noname.weapons.entity.EntityBalista;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;

import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBalista extends Render {
    private IModelCustom model;
    private IModelCustom modelArm1;
    private static final ResourceLocation texture = new ResourceLocation("got", "textures/entity/noname/ballista.png");
    private static final float Y_OFFSET = -0.1F;

    public RenderBalista() {
        this.shadowSize = 1.0F;
        this.model = AdvancedModelLoader.loadModel(new ResourceLocation("got", "models/ballista_based.obj"));
        this.modelArm1 = AdvancedModelLoader.loadModel(new ResourceLocation("got", "models/ballistaArm1.obj"));
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        EntityBalista balista = (EntityBalista) entity;
        float renderYaw = balista.prevRotationYaw + (balista.rotationYaw - balista.prevRotationYaw) * partialTicks;
        float pitch = balista.prevRotationPitch + (balista.rotationPitch - balista.prevRotationPitch) * partialTicks;
        GL11.glPushMatrix();
        GL11.glTranslated(x, y + Y_OFFSET, z);
        GL11.glRotatef(180.0F - renderYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-pitch, 1.0F, 0.0F, 0.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.bindEntityTexture(entity);
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        model.renderAll();
        if (modelArm1 != null) {
            float backwardOffset = calculateArmOffset(balista, partialTicks);
            GL11.glPushMatrix();
            GL11.glTranslatef(0, 0, backwardOffset);
            modelArm1.renderAll();
            GL11.glPopMatrix();
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }


    private float calculateArmOffset(EntityBalista balista, float partialTicks) {
        float maxOffset = 0.5f;
        int maxReloadTime = (int) WeaponsConfig.reloadTimeBalista;
        boolean isReloading = (balista.getDataWatcher().getWatchableObjectByte(17) & 1) != 0;
        boolean isLoaded = (balista.getDataWatcher().getWatchableObjectByte(21) & 1) != 0;
        int reloadTimer = balista.getDataWatcher().getWatchableObjectInt(18);

        if (isReloading) {
            float progress = Math.min((float)reloadTimer / (float)maxReloadTime, 1.0f);
            return maxOffset * progress;
        } else if (isLoaded) {
            return maxOffset; 
        } else {
            return 0.0f; 
        }
    }



    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }
}

