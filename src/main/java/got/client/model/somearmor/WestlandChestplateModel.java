package got.client.model.somearmor;

import got.client.model.GOTModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class WestlandChestplateModel extends GOTModelBiped {
	private final ModelRenderer cuirass;
	private final ModelRenderer right_arm;
	private final ModelRenderer left_arm;

	public WestlandChestplateModel() {
		textureWidth = 64;
		textureHeight = 64;

		cuirass = new ModelRenderer(this);
		cuirass.setRotationPoint(0.0F, 0.0F, 0.0F);
		cuirass.cubeList.add(new ModelBox(cuirass, 0, 0, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.5F));
		cuirass.cubeList.add(new ModelBox(cuirass, 0, 16, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.7F));
		cuirass.cubeList.add(new ModelBox(cuirass, 0, 0, -4.0F, 0.0F, -2.068F, 8, 12, 4, 0.2F));

		right_arm = new ModelRenderer(this);
		right_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
		right_arm.cubeList.add(new ModelBox(right_arm, 24, 0, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.6F));
		right_arm.cubeList.add(new ModelBox(right_arm, 40, 0, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.3F));

		left_arm = new ModelRenderer(this);
		left_arm.setRotationPoint(0.0F, 0.0F, 0.0F);
		left_arm.cubeList.add(new ModelBox(left_arm, 24, 0, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.6F));
		left_arm.cubeList.add(new ModelBox(left_arm, 40, 0, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.3F));


		this.bipedBody.addChild(cuirass);
		this.bipedRightArm.addChild(right_arm);
		this.bipedLeftArm.addChild(left_arm);

		this.bipedHead.cubeList.clear();
		this.bipedRightLeg.cubeList.clear();
		this.bipedLeftLeg.cubeList.clear();
	}


	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}