package noname.weapons.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import noname.weapons.entity.EntityBalistaProjectile;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBalistaProjectile extends Render {
    private final IModelCustom model;

    public RenderBalistaProjectile() {
        this.model = AdvancedModelLoader.loadModel(new ResourceLocation("got", "models/arrow.obj"));
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        EntityBalistaProjectile projectile = (EntityBalistaProjectile) entity;

        float calcYaw;
        float calcPitch;

        if (projectile.isInGround()) {
            calcYaw = projectile.getStuckYaw();
            calcPitch = projectile.getStuckPitch();
        } else {
            double dx = entity.posX - entity.prevPosX;
            double dy = entity.posY - entity.prevPosY;
            double dz = entity.posZ - entity.prevPosZ;
            double horizontalDist = Math.sqrt(dx * dx + dz * dz);

            if (horizontalDist > 0.001 || Math.abs(dy) > 0.001) {
                calcYaw = (float) (Math.atan2(dx, dz) * 180.0 / Math.PI);
                calcPitch = (float) (-Math.atan2(dy, horizontalDist) * 180.0 / Math.PI);
            } else {
                calcYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
                calcPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
            }
        }

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(180.0F + calcYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-calcPitch, 1.0F, 0.0F, 0.0F);
        bindTexture(new ResourceLocation("got", "textures/entity/noname/ballista.png"));
        model.renderAll();
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return new ResourceLocation("got", "textures/entity/noname/ballista.png");
    }
}