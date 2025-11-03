package got.client.model.somearmor;

import got.client.model.GOTModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class WestlandBootsModel extends GOTModelBiped {
	private final ModelRenderer right_leg;
	private final ModelRenderer left_leg;

	public WestlandBootsModel() {
		textureWidth = 64;
		textureHeight = 64;

		right_leg = new ModelRenderer(this);
		right_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
		right_leg.cubeList.add(new ModelBox(right_leg, 40, 0, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.5F));

		left_leg = new ModelRenderer(this);
		left_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
		left_leg.cubeList.add(new ModelBox(left_leg, 40, 0, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.5F));


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