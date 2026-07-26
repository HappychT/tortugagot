package got.client.model.customarmor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class starks_body extends ModelBiped {
	private final ModelRenderer savewithbody2;
	private final ModelRenderer Left_Leg_Layer_r1;
	private final ModelRenderer savewithbody;
	private final ModelRenderer Right_Leg_Layer_r1;
	private final ModelRenderer right_arm_armor_r1;
	private final ModelRenderer right_pauldron;
	private final ModelRenderer left_arm_armor_r1;
	private final ModelRenderer left_pauldron;
	private final ModelRenderer cape_template9;
	private final ModelRenderer armorCape9;
	private final ModelRenderer capeMid9;
	private final ModelRenderer capeLow;

	public starks_body() {
		super(0.0F);
		textureWidth = 64;
		textureHeight = 64;

		bipedLeftLeg = new ModelRenderer(this);
		bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);

		savewithbody2 = new ModelRenderer(this);
		savewithbody2.setRotationPoint(0.1F, 12.0F, 0.0F);
		bipedLeftLeg.addChild(savewithbody2);

		Left_Leg_Layer_r1 = new ModelRenderer(this);
		Left_Leg_Layer_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		savewithbody2.addChild(Left_Leg_Layer_r1);
		setRotationAngle(Left_Leg_Layer_r1, 0.0F, 0.0F, -0.0873F);
		Left_Leg_Layer_r1.cubeList.add(new ModelBox(Left_Leg_Layer_r1, 44, 2, -1.6F, -12.0F, -2.5F, 5, 9, 5, 0.22F));

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
		Right_Leg_Layer_r1.cubeList.add(new ModelBox(Right_Leg_Layer_r1, 44, 2, -3.4F, -12.0F, -2.5F, 5, 9, 5, 0.22F));
		Right_Leg_Layer_r1.mirror = false;

		bipedRightArm = new ModelRenderer(this);
		bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		bipedRightArm.cubeList.add(new ModelBox(bipedRightArm, 24, 0, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.6F));
		bipedRightArm.cubeList.add(new ModelBox(bipedRightArm, 24, 16, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.75F));

		right_arm_armor_r1 = new ModelRenderer(this);
		right_arm_armor_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedRightArm.addChild(right_arm_armor_r1);
		setRotationAngle(right_arm_armor_r1, 0.0F, 0.0F, 0.0436F);
		right_arm_armor_r1.cubeList.add(new ModelBox(right_arm_armor_r1, 0, 52, -3.5F, -3.0F, -2.5F, 5, 7, 5, 0.6F));
		right_arm_armor_r1.cubeList.add(new ModelBox(right_arm_armor_r1, 0, 40, -3.25F, -3.0F, -2.5F, 5, 7, 5, 0.5F));
		right_arm_armor_r1.cubeList.add(new ModelBox(right_arm_armor_r1, 16, 33, -3.5F, -3.0F, -2.5F, 5, 7, 5, 0.7F));

		right_pauldron = new ModelRenderer(this);
		right_pauldron.setRotationPoint(4.0F, 12.0F, -3.0F);
		bipedRightArm.addChild(right_pauldron);

		bipedLeftArm = new ModelRenderer(this);
		bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		bipedLeftArm.mirror = true;
		bipedLeftArm.cubeList.add(new ModelBox(bipedLeftArm, 24, 0, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.6F));
		bipedLeftArm.mirror = false;
		bipedLeftArm.mirror = true;
		bipedLeftArm.cubeList.add(new ModelBox(bipedLeftArm, 24, 16, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.75F));
		bipedLeftArm.mirror = false;

		left_arm_armor_r1 = new ModelRenderer(this);
		left_arm_armor_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedLeftArm.addChild(left_arm_armor_r1);
		setRotationAngle(left_arm_armor_r1, 0.0F, 0.0F, -0.0436F);
		left_arm_armor_r1.mirror = true;
		left_arm_armor_r1.cubeList.add(new ModelBox(left_arm_armor_r1, 16, 33, -1.5F, -3.0F, -2.5F, 5, 7, 5, 0.7F));
		left_arm_armor_r1.mirror = false;
		left_arm_armor_r1.mirror = true;
		left_arm_armor_r1.cubeList.add(new ModelBox(left_arm_armor_r1, 0, 52, -1.5F, -3.0F, -2.5F, 5, 7, 5, 0.6F));
		left_arm_armor_r1.mirror = false;
		left_arm_armor_r1.mirror = true;
		left_arm_armor_r1.cubeList.add(new ModelBox(left_arm_armor_r1, 0, 40, -1.75F, -3.0F, -2.5F, 5, 7, 5, 0.5F));
		left_arm_armor_r1.mirror = false;

		left_pauldron = new ModelRenderer(this);
		left_pauldron.setRotationPoint(-6.0F, 12.0F, -3.0F);
		bipedLeftArm.addChild(left_pauldron);

		bipedBody = new ModelRenderer(this);
		bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedBody.cubeList.add(new ModelBox(bipedBody, 0, 16, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.8F));
		bipedBody.cubeList.add(new ModelBox(bipedBody, 0, 0, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.615F));

		cape_template9 = new ModelRenderer(this);
		cape_template9.setRotationPoint(0.0F, 0.0F, 2.75F);
		bipedBody.addChild(cape_template9);

		armorCape9 = new ModelRenderer(this);
		armorCape9.setRotationPoint(0.0F, -2.0F, 0.0F);
		cape_template9.addChild(armorCape9);
		setRotationAngle(armorCape9, 0.0873F, 0.0F, 0.0F);
		armorCape9.cubeList.add(new ModelBox(armorCape9, 38, 37, -6.0F, 1.5F, -0.65F, 12, 12, 1, 0.0F));

		capeMid9 = new ModelRenderer(this);
		capeMid9.setRotationPoint(21.5F, 13.5F, 0.35F);
		armorCape9.addChild(capeMid9);
		setRotationAngle(capeMid9, 0.0873F, 0.0F, 0.0F);
		capeMid9.cubeList.add(new ModelBox(capeMid9, 38, 51, -27.5F, -0.1305F, -0.9914F, 12, 6, 1, -0.01F));

		capeLow = new ModelRenderer(this);
		capeLow.setRotationPoint(-21.0F, 6.0F, -1.0F);
		capeMid9.addChild(capeLow);
		setRotationAngle(capeLow, 0.1309F, 0.0F, 0.0F);
		capeLow.cubeList.add(new ModelBox(capeLow, 38, 59, -6.5F, -0.1271F, 0.0311F, 12, 4, 1, 0.0F));

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