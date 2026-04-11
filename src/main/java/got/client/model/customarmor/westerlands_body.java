package got.client.model.customarmor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class westerlands_body extends ModelBiped {
	private final ModelRenderer cloth;
	private final ModelRenderer Right_Leg_Layer_r1;
	private final ModelRenderer cloth2;
	private final ModelRenderer Left_Leg_Layer_r1;
	private final ModelRenderer right_pauldron;
	private final ModelRenderer right_arm_head_extension_r1;
	private final ModelRenderer left_pauldron;
	private final ModelRenderer left_arm_head_extension_r1;
	private final ModelRenderer capetemplate;
	private final ModelRenderer armorCape9;
	private final ModelRenderer capeMid9;
	private final ModelRenderer capeLow;

	public westerlands_body() {
		super(0.0F);
		textureWidth = 64;
		textureHeight = 64;

		bipedRightLeg = new ModelRenderer(this);
		bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);

		cloth = new ModelRenderer(this);
		cloth.setRotationPoint(-0.1F, 12.0F, 0.0F);
		bipedRightLeg.addChild(cloth);

		Right_Leg_Layer_r1 = new ModelRenderer(this);
		Right_Leg_Layer_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		cloth.addChild(Right_Leg_Layer_r1);
		setRotationAngle(Right_Leg_Layer_r1, 0.0F, 0.0F, 0.0873F);
		Right_Leg_Layer_r1.mirror = true;
		Right_Leg_Layer_r1.cubeList.add(new ModelBox(Right_Leg_Layer_r1, 44, 2, -3.4F, -12.0F, -2.5F, 5, 9, 5, 0.2F));
		Right_Leg_Layer_r1.mirror = false;

		bipedLeftLeg = new ModelRenderer(this);
		bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);

		cloth2 = new ModelRenderer(this);
		cloth2.setRotationPoint(0.1F, 12.0F, 0.0F);
		bipedLeftLeg.addChild(cloth2);

		Left_Leg_Layer_r1 = new ModelRenderer(this);
		Left_Leg_Layer_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		cloth2.addChild(Left_Leg_Layer_r1);
		setRotationAngle(Left_Leg_Layer_r1, 0.0F, 0.0F, -0.0873F);
		Left_Leg_Layer_r1.cubeList.add(new ModelBox(Left_Leg_Layer_r1, 44, 2, -1.6F, -12.0F, -2.5F, 5, 9, 5, 0.2F));

		bipedRightArm = new ModelRenderer(this);
		bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		bipedRightArm.cubeList.add(new ModelBox(bipedRightArm, 24, 0, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.6F));
		bipedRightArm.cubeList.add(new ModelBox(bipedRightArm, 24, 16, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.75F));

		right_pauldron = new ModelRenderer(this);
		right_pauldron.setRotationPoint(4.0F, 12.0F, -3.0F);
		bipedRightArm.addChild(right_pauldron);

		right_arm_head_extension_r1 = new ModelRenderer(this);
		right_arm_head_extension_r1.setRotationPoint(3.0F, 0.0F, 2.0F);
		right_pauldron.addChild(right_arm_head_extension_r1);
		setRotationAngle(right_arm_head_extension_r1, 0.0F, 0.0F, -0.0436F);
		right_arm_head_extension_r1.mirror = true;
		right_arm_head_extension_r1.cubeList.add(new ModelBox(right_arm_head_extension_r1, 42, 22, -10.5F, -15.5F, -1.5F, 6, 5, 5, 0.5F));
		right_arm_head_extension_r1.mirror = false;

		bipedLeftArm = new ModelRenderer(this);
		bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		bipedLeftArm.mirror = true;
		bipedLeftArm.cubeList.add(new ModelBox(bipedLeftArm, 24, 0, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.6F));
		bipedLeftArm.mirror = false;
		bipedLeftArm.mirror = true;
		bipedLeftArm.cubeList.add(new ModelBox(bipedLeftArm, 24, 16, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.75F));
		bipedLeftArm.mirror = false;

		left_pauldron = new ModelRenderer(this);
		left_pauldron.setRotationPoint(-6.0F, 12.0F, -3.0F);
		bipedLeftArm.addChild(left_pauldron);

		left_arm_head_extension_r1 = new ModelRenderer(this);
		left_arm_head_extension_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		left_pauldron.addChild(left_arm_head_extension_r1);
		setRotationAngle(left_arm_head_extension_r1, 0.0F, 0.0F, 0.0436F);
		left_arm_head_extension_r1.cubeList.add(new ModelBox(left_arm_head_extension_r1, 42, 22, 3.5F, -15.5F, 0.5F, 6, 5, 5, 0.5F));

		bipedBody = new ModelRenderer(this);
		bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedBody.cubeList.add(new ModelBox(bipedBody, 0, 16, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.8F));
		bipedBody.cubeList.add(new ModelBox(bipedBody, 0, 0, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.615F));

		capetemplate = new ModelRenderer(this);
		capetemplate.setRotationPoint(0.0F, 1.0F, 2.75F);
		bipedBody.addChild(capetemplate);

		armorCape9 = new ModelRenderer(this);
		armorCape9.setRotationPoint(0.0F, -2.0F, 0.0F);
		capetemplate.addChild(armorCape9);
		setRotationAngle(armorCape9, 0.0873F, 0.0F, 0.0F);
		armorCape9.cubeList.add(new ModelBox(armorCape9, 16, 37, -6.0F, 1.5F, -0.65F, 12, 12, 1, 0.0F));

		capeMid9 = new ModelRenderer(this);
		capeMid9.setRotationPoint(21.5F, 13.5F, 0.35F);
		armorCape9.addChild(capeMid9);
		setRotationAngle(capeMid9, 0.0873F, 0.0F, 0.0F);
		capeMid9.cubeList.add(new ModelBox(capeMid9, 16, 51, -27.5F, -0.1305F, -0.9914F, 12, 6, 1, -0.01F));

		capeLow = new ModelRenderer(this);
		capeLow.setRotationPoint(-21.0F, 6.0F, -1.0F);
		capeMid9.addChild(capeLow);
		setRotationAngle(capeLow, 0.1309F, 0.0F, 0.0F);
		capeLow.cubeList.add(new ModelBox(capeLow, 16, 59, -6.5F, -0.1271F, 0.0311F, 12, 4, 1, 0.0F));

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