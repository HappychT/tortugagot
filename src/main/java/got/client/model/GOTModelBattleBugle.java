package got.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * 3D-модель боевого горна. Без addChild — рендер всех частей вручную (совместимость с 1.7.10).
 */
public class GOTModelBattleBugle extends ModelBase {

    private final ModelRenderer bell;
    private final ModelRenderer body;
    private final ModelRenderer tube;

    public GOTModelBattleBugle() {
        textureWidth = 32;
        textureHeight = 32;

        bell = new ModelRenderer(this, 0, 0);
        bell.setRotationPoint(0.0f, 0.0f, 0.0f);
        bell.addBox(-2.0f, -2.0f, -2.0f, 4, 4, 4);

        body = new ModelRenderer(this, 0, 12);
        body.setRotationPoint(0.0f, 0.0f, 0.0f);
        body.addBox(-1.5f, -1.5f, 2.0f, 3, 3, 6);

        tube = new ModelRenderer(this, 0, 22);
        tube.setRotationPoint(0.0f, 0.0f, 0.0f);
        tube.addBox(-0.5f, -0.5f, 8.0f, 1, 1, 4);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        bell.render(scale);
        body.render(scale);
        tube.render(scale);
    }

    /** Рендер в руке: общий поворот горна. */
    public void renderAsItem(float scale) {
        float rx = (float) Math.toRadians(-45.0);
        float rz = (float) Math.toRadians(90.0);
        bell.rotateAngleX = rx;
        bell.rotateAngleZ = rz;
        body.rotateAngleX = rx;
        body.rotateAngleZ = rz;
        tube.rotateAngleX = rx;
        tube.rotateAngleZ = rz;
        bell.render(scale);
        body.render(scale);
        tube.render(scale);
        bell.rotateAngleX = 0;
        bell.rotateAngleZ = 0;
        body.rotateAngleX = 0;
        body.rotateAngleZ = 0;
        tube.rotateAngleX = 0;
        tube.rotateAngleZ = 0;
    }
}
