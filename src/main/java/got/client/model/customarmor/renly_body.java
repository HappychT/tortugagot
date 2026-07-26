package got.client.model.customarmor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class renly_body extends ModelBiped {
	private final ModelRenderer savewithbody2;
	private final ModelRenderer Left_Leg_Layer_r1;
	private final ModelRenderer savewithbody;
	private final ModelRenderer Right_Leg_Layer_r1;

	public renly_body() {
		super(0.0F);
		textureWidth = 64;
		textureHeight = 64;

		bipedLeftLeg = new ModelRenderer(this);
		bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);

		savewithbody2 = new ModelRenderer(this);
		savewithbody2.setRotationPoint(0.1F, 12.0F, 0.0F);
		bipedLeftLeg.addChild(savewithbody2);

		Left_Leg_Layer_r1 = new ModelRenderer(this);
		Left_Leg_Layer_r1.setRotationPoint(-1.0F, 0.0F, 0.0F);
		savewithbody2.addChild(Left_Leg_Layer_r1);
		setRotationAngle(Left_Leg_Layer_r1, 0.0F, 0.0F, -0.0873F);
		Left_Leg_Layer_r1.cubeList.add(new ModelBox(Left_Leg_Layer_r1, 44, 2, -0.2F, -12.0F, -2.5F, 5, 9, 5, 0.11F));

		bipedRightLeg = new ModelRenderer(this);
		bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);

		savewithbody = new ModelRenderer(this);
		savewithbody.setRotationPoint(-0.1F, 12.0F, 0.0F);
		bipedRightLeg.addChild(savewithbody);

		Right_Leg_Layer_r1 = new ModelRenderer(this);
		Right_Leg_Layer_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		savewithbody.addChild(Right_Leg_Layer_r1);
		setRotationAngle(Right_Leg_Layer_r1, 0.0F, 0.0F, 0.0873F);
		Right_Leg_Layer_r1.mirror = true;
		Right_Leg_Layer_r1.cubeList.add(new ModelBox(Right_Leg_Layer_r1, 44, 2, -3.8F, -12.0F, -2.5F, 5, 9, 5, 0.11F));
		Right_Leg_Layer_r1.mirror = false;

		bipedRightArm = new ModelRenderer(this);
		bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		bipedRightArm.cubeList.add(new ModelBox(bipedRightArm, 26, 0, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.35F));
		bipedRightArm.cubeList.add(new ModelBox(bipedRightArm, 26, 16, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.575F));

		bipedLeftArm = new ModelRenderer(this);
		bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		bipedLeftArm.mirror = true;
		bipedLeftArm.cubeList.add(new ModelBox(bipedLeftArm, 26, 0, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.35F));
		bipedLeftArm.mirror = false;
		bipedLeftArm.mirror = true;
		bipedLeftArm.cubeList.add(new ModelBox(bipedLeftArm, 26, 16, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.575F));
		bipedLeftArm.mirror = false;

		bipedBody = new ModelRenderer(this);
		bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedBody.cubeList.add(new ModelBox(bipedBody, 0, 16, -4.5F, 0.0F, -2.0F, 9, 12, 4, 0.7F));
		bipedBody.cubeList.add(new ModelBox(bipedBody, 0, 32, -4.5F, 0.0F, -2.0F, 9, 12, 4, 0.75F));
		bipedBody.cubeList.add(new ModelBox(bipedBody, 0, 0, -4.5F, 0.0F, -2.0F, 9, 12, 4, 0.55F));

		bipedHead = new ModelRenderer(this);
		bipedHeadwear = new ModelRenderer(this);
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}