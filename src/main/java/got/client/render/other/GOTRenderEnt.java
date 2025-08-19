package got.client.render.other;

import java.util.HashMap;

import got.client.GOTTextures;
import got.client.model.GOTModelEnt;
import got.common.entity.other.GOTEntityEnt;
import got.common.entity.other.GOTEntityTree;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class GOTRenderEnt extends RenderLiving {
    private static HashMap<Integer, ResourceLocation> entTextures = new HashMap<Integer, ResourceLocation>();
    private GOTModelEnt eyesModel = new GOTModelEnt(0.05f);

    public GOTRenderEnt() {
        super(new GOTModelEnt(), 0.5f);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        int treeType = ((GOTEntityEnt)entity).getTreeType();
        String s = "got:textures/entity/ent/" + GOTEntityTree.TYPES[treeType] + ".png";
        ResourceLocation r = (ResourceLocation)entTextures.get(treeType);
        if (r == null) {
            r = new ResourceLocation(s);
            entTextures.put(treeType, r);
        }
        return r;
    }

    @Override
    protected void renderModel(EntityLivingBase entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.renderModel(entity, f, f1, f2, f3, f4, f5);
        ResourceLocation eyes = GOTTextures.getEyesTexture(getEntityTexture(entity), new int[][]{{15, 23}, {22, 23}}, 3, 2);
        GOTGlowingEyes.renderGlowingEyes(entity, eyes, this.eyesModel, f, f1, f2, f3, f4, f5);
    }
}