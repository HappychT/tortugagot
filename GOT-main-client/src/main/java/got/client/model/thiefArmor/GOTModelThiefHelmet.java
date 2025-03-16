package got.client.model.thiefArmor;

import got.client.model.GOTModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class GOTModelThiefHelmet extends GOTModelBiped {
    private final ModelRenderer Head;

    public GOTModelThiefHelmet() {
        this.textureWidth = 128;
        this.textureHeight = 128;

        this.Head = new ModelRenderer(this);
        this.Head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Head.cubeList.add(new ModelBox(this.Head, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F));

        this.bipedHead.addChild(this.Head);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}