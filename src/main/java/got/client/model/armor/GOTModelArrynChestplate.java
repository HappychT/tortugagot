package got.client.model.armor;

import got.client.model.GOTModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class GOTModelArrynChestplate extends GOTModelBiped {
    private final ModelRenderer cuirass;
    private final ModelRenderer right_arm;
    private final ModelRenderer left_arm;

    public GOTModelArrynChestplate() {
        textureWidth = 64;
        textureHeight = 64;

        cuirass = new ModelRenderer(this);
        cuirass.setRotationPoint(0.0F, 0.0F, 0.0F);
        // Координаты коробок скорректированы для работы с biped-моделью
        cuirass.cubeList.add(new ModelBox(cuirass, 24, 48, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.5F));
        cuirass.cubeList.add(new ModelBox(cuirass, 0, 48, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.7F));
        cuirass.cubeList.add(new ModelBox(cuirass, 0, 32, -4.0F, 0.0F, -2.068F, 8, 12, 4, 0.2F));

        right_arm = new ModelRenderer(this);
        right_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        right_arm.cubeList.add(new ModelBox(right_arm, 48, 48, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.6F));
        right_arm.cubeList.add(new ModelBox(right_arm, 48, 20, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.3F));

        left_arm = new ModelRenderer(this);
        left_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
        left_arm.cubeList.add(new ModelBox(left_arm, 48, 48, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.6F));
        left_arm.cubeList.add(new ModelBox(left_arm, 48, 20, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.3F));

        // Присоединяем части брони к соответствующим частям тела
        this.bipedBody.addChild(cuirass);
        this.bipedRightArm.addChild(right_arm);
        this.bipedLeftArm.addChild(left_arm);

        // Скрываем ненужные части
        this.bipedHead.cubeList.clear();
        this.bipedRightLeg.cubeList.clear();
        this.bipedLeftLeg.cubeList.clear();
    }
}
