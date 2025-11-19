package noname.weapons.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBalistaProjectile extends Render {
    private final IModelCustom model;

    public RenderBalistaProjectile() {
        this.model = AdvancedModelLoader.loadModel(new ResourceLocation("got", "models/arrow.obj"));
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(-yaw, 0, 1, 0);
        bindTexture(new ResourceLocation("got", "textures/entity/noname/ballista.png"));
        model.renderAll();
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return new ResourceLocation("got", "textures/entity/noname/ballista.png");
    }
}

