package got.client.model.somearmor;

import got.client.model.GOTModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class WestlandLeggingsModel extends GOTModelBiped {
	private final ModelRenderer right_leg;
	private final ModelRenderer right_skirt_r1;
	private final ModelRenderer left_leg;
	private final ModelRenderer left_skirt_r1;

	public WestlandLeggingsModel() {
		textureWidth = 64;
		textureHeight = 64;

		right_leg = new ModelRenderer(this);
		right_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
		right_leg.cubeList.add(new ModelBox(right_leg, 24, 0, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.3F));

		right_skirt_r1 = new ModelRenderer(this);
		right_skirt_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		right_leg.addChild(right_skirt_r1);
		setRotationAngle(right_skirt_r1, 0.0F, 0.0F, 0.0873F);
		right_skirt_r1.cubeList.add(new ModelBox(right_skirt_r1, 0, 16, -3.9F, 0.0F, -2.0F, 3, 7, 4, 0.8F));

		left_leg = new ModelRenderer(this);
		left_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
		left_leg.cubeList.add(new ModelBox(left_leg, 24, 0, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.3F));

		left_skirt_r1 = new ModelRenderer(this);
		left_skirt_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		left_leg.addChild(left_skirt_r1);
		setRotationAngle(left_skirt_r1, 0.0F, 0.0F, -0.0873F);
		left_skirt_r1.cubeList.add(new ModelBox(left_skirt_r1, 0, 16, 0.9F, 0.0F, -2.0F, 3, 7, 4, 0.8F));


		this.bipedRightLeg.addChild(right_leg);
		this.bipedLeftLeg.addChild(left_leg);

		this.bipedHead.cubeList.clear();
		this.bipedBody.cubeList.clear();
		this.bipedRightArm.cubeList.clear();
		this.bipedLeftArm.cubeList.clear();
	}


	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}