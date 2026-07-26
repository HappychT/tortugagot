package got.client.model.customarmor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class first_humans_body extends ModelBiped {
	private final ModelRenderer savewithbody;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer Right_Leg_Layer_r1;
	private final ModelRenderer savewithbody2;
	private final ModelRenderer cube_r5;
	private final ModelRenderer cube_r6;
	private final ModelRenderer cube_r7;
	private final ModelRenderer cube_r8;
	private final ModelRenderer Left_Leg_Layer_r1;
	private final ModelRenderer savewithLegs;
	private final ModelRenderer left_pauldron;
	private final ModelRenderer group;
	private final ModelRenderer cube_r9;
	private final ModelRenderer cube_r10;
	private final ModelRenderer LeftArm_r1;
	private final ModelRenderer LeftArm_r2;
	private final ModelRenderer LeftArm_r3;
	private final ModelRenderer right_pauldron;
	private final ModelRenderer group2;
	private final ModelRenderer cube_r11;
	private final ModelRenderer cube_r12;
	private final ModelRenderer RightArm_r1;
	private final ModelRenderer RightArm_r2;
	private final ModelRenderer RightArm_r3;

	public first_humans_body() {
		super(0.0F);
		textureWidth = 64;
		textureHeight = 64;

		bipedRightLeg = new ModelRenderer(this);
		bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);

		savewithbody = new ModelRenderer(this);
		savewithbody.setRotationPoint(-0.1F, 12.0F, 0.0F);
		bipedRightLeg.addChild(savewithbody);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(-5.0F, -5.0F, 0.0F);
		savewithbody.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.0F, -0.2182F);
		cube_r1.mirror = true;
		cube_r1.cubeList.add(new ModelBox(cube_r1, 58, 2, 1.5F, -1.0F, -2.0F, 2, 1, 1, 0.1F));
		cube_r1.mirror = false;
		cube_r1.mirror = true;
		cube_r1.cubeList.add(new ModelBox(cube_r1, 58, 2, 1.5F, -1.0F, 1.0F, 2, 1, 1, 0.1F));
		cube_r1.mirror = false;

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(-6.0F, -5.0F, 0.0F);
		savewithbody.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.0F, -0.2182F);
		cube_r2.mirror = true;
		cube_r2.cubeList.add(new ModelBox(cube_r2, 56, 0, 2.5F, -3.0F, -2.0F, 1, 2, 1, 0.075F));
		cube_r2.mirror = false;
		cube_r2.mirror = true;
		cube_r2.cubeList.add(new ModelBox(cube_r2, 56, 0, 2.5F, -3.0F, 1.0F, 1, 2, 1, 0.075F));
		cube_r2.mirror = false;

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(-6.0F, -6.0F, -1.0F);
		savewithbody.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0F, 0.0F, -0.2182F);
		cube_r3.mirror = true;
		cube_r3.cubeList.add(new ModelBox(cube_r3, 56, 0, 2.5F, -3.0F, 0.5F, 1, 2, 1, 0.075F));
		cube_r3.mirror = false;

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(-5.0F, -6.0F, -1.0F);
		savewithbody.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0F, 0.0F, -0.2182F);
		cube_r4.mirror = true;
		cube_r4.cubeList.add(new ModelBox(cube_r4, 58, 2, 1.5F, -1.0F, 0.5F, 2, 1, 1, 0.1F));
		cube_r4.mirror = false;

		Right_Leg_Layer_r1 = new ModelRenderer(this);
		Right_Leg_Layer_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		savewithbody.addChild(Right_Leg_Layer_r1);
		setRotationAngle(Right_Leg_Layer_r1, 0.0F, 0.0F, 0.0873F);
		Right_Leg_Layer_r1.mirror = true;
		Right_Leg_Layer_r1.cubeList.add(new ModelBox(Right_Leg_Layer_r1, 40, 16, -3.45F, -12.0F, -2.5F, 5, 9, 5, 0.24F));
		Right_Leg_Layer_r1.mirror = false;
		Right_Leg_Layer_r1.mirror = true;
		Right_Leg_Layer_r1.cubeList.add(new ModelBox(Right_Leg_Layer_r1, 40, 0, -3.45F, -12.0F, -2.5F, 5, 9, 5, 0.11F));
		Right_Leg_Layer_r1.mirror = false;

		bipedLeftLeg = new ModelRenderer(this);
		bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);

		savewithbody2 = new ModelRenderer(this);
		savewithbody2.setRotationPoint(0.1F, 12.0F, 0.0F);
		bipedLeftLeg.addChild(savewithbody2);

		cube_r5 = new ModelRenderer(this);
		cube_r5.setRotationPoint(6.0F, -5.0F, 3.0F);
		savewithbody2.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.0F, 0.0F, 0.2182F);
		cube_r5.cubeList.add(new ModelBox(cube_r5, 56, 0, -3.5F, -3.0F, -2.0F, 1, 2, 1, 0.075F));
		cube_r5.cubeList.add(new ModelBox(cube_r5, 56, 0, -3.5F, -3.0F, -5.0F, 1, 2, 1, 0.075F));

		cube_r6 = new ModelRenderer(this);
		cube_r6.setRotationPoint(5.0F, -5.0F, 3.0F);
		savewithbody2.addChild(cube_r6);
		setRotationAngle(cube_r6, 0.0F, 0.0F, 0.2182F);
		cube_r6.cubeList.add(new ModelBox(cube_r6, 58, 2, -3.5F, -1.0F, -2.0F, 2, 1, 1, 0.1F));
		cube_r6.cubeList.add(new ModelBox(cube_r6, 58, 2, -3.5F, -1.0F, -5.0F, 2, 1, 1, 0.1F));

		cube_r7 = new ModelRenderer(this);
		cube_r7.setRotationPoint(5.0F, -6.0F, -1.0F);
		savewithbody2.addChild(cube_r7);
		setRotationAngle(cube_r7, 0.0F, 0.0F, 0.2182F);
		cube_r7.cubeList.add(new ModelBox(cube_r7, 58, 2, -3.5F, -1.0F, 0.5F, 2, 1, 1, 0.1F));

		cube_r8 = new ModelRenderer(this);
		cube_r8.setRotationPoint(6.0F, -6.0F, -1.0F);
		savewithbody2.addChild(cube_r8);
		setRotationAngle(cube_r8, 0.0F, 0.0F, 0.2182F);
		cube_r8.cubeList.add(new ModelBox(cube_r8, 56, 0, -3.5F, -3.0F, 0.5F, 1, 2, 1, 0.075F));

		Left_Leg_Layer_r1 = new ModelRenderer(this);
		Left_Leg_Layer_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		savewithbody2.addChild(Left_Leg_Layer_r1);
		setRotationAngle(Left_Leg_Layer_r1, 0.0F, 0.0F, -0.0873F);
		Left_Leg_Layer_r1.cubeList.add(new ModelBox(Left_Leg_Layer_r1, 40, 16, -1.55F, -12.0F, -2.5F, 5, 9, 5, 0.25F));
		Left_Leg_Layer_r1.cubeList.add(new ModelBox(Left_Leg_Layer_r1, 40, 0, -1.55F, -12.0F, -2.5F, 5, 9, 5, 0.11F));

		bipedBody = new ModelRenderer(this);
		bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedBody.cubeList.add(new ModelBox(bipedBody, 0, 16, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.8F));
		bipedBody.cubeList.add(new ModelBox(bipedBody, 0, 0, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.615F));

		savewithLegs = new ModelRenderer(this);
		savewithLegs.setRotationPoint(0.0F, 24.0F, 0.0F);
		bipedBody.addChild(savewithLegs);
		savewithLegs.cubeList.add(new ModelBox(savewithLegs, 0, 0, -4.0F, -24.0F, -2.0F, 8, 12, 4, 0.56F));

		bipedLeftArm = new ModelRenderer(this);
		bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		bipedLeftArm.mirror = true;
		bipedLeftArm.cubeList.add(new ModelBox(bipedLeftArm, 24, 0, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.55F));
		bipedLeftArm.mirror = false;
		bipedLeftArm.mirror = true;
		bipedLeftArm.cubeList.add(new ModelBox(bipedLeftArm, 25, 16, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.75F));
		bipedLeftArm.mirror = false;

		LeftArm_r1 = new ModelRenderer(this);
		LeftArm_r1.setRotationPoint(-5.0F, -2.4F, 0.25F);
		bipedLeftArm.addChild(LeftArm_r1);
		setRotationAngle(LeftArm_r1, -0.1745F, 0.0F, 0.0873F);
		LeftArm_r1.cubeList.add(new ModelBox(LeftArm_r1, 0, 60, 5.8303F, -3.0799F, 2.0F, 4, 4, 0, 0.0F));

		LeftArm_r2 = new ModelRenderer(this);
		LeftArm_r2.setRotationPoint(-5.0F, 18.8F, 2.85F);
		bipedLeftArm.addChild(LeftArm_r2);
		setRotationAngle(LeftArm_r2, 0.0F, 0.0F, 0.1309F);
		LeftArm_r2.cubeList.add(new ModelBox(LeftArm_r2, 0, 60, 4.0F, -24.0F, -2.0F, 4, 4, 0, 0.0F));
		LeftArm_r2.cubeList.add(new ModelBox(LeftArm_r2, 0, 60, 4.0F, -24.0F, -3.7F, 4, 4, 0, 0.0F));

		LeftArm_r3 = new ModelRenderer(this);
		LeftArm_r3.setRotationPoint(-4.4F, -2.3F, -0.05F);
		bipedLeftArm.addChild(LeftArm_r3);
		setRotationAngle(LeftArm_r3, 0.1745F, 0.0F, 0.0873F);
		LeftArm_r3.cubeList.add(new ModelBox(LeftArm_r3, 0, 60, 5.8303F, -3.0799F, -2.0F, 4, 4, 0, 0.0F));

		left_pauldron = new ModelRenderer(this);
		left_pauldron.setRotationPoint(-6.0F, 12.0F, -3.0F);
		bipedLeftArm.addChild(left_pauldron);

		group = new ModelRenderer(this);
		group.setRotationPoint(13.0F, -8.0F, 3.0F);
		left_pauldron.addChild(group);
		setRotationAngle(group, 0.0F, 0.0F, 0.0873F);
		group.cubeList.add(new ModelBox(group, 56, 0, -3.25F, -4.0F, -0.5F, 1, 2, 1, 0.075F));
		group.cubeList.add(new ModelBox(group, 58, 2, -4.25F, -1.0F, 1.0F, 2, 1, 1, 0.1F));
		group.cubeList.add(new ModelBox(group, 56, 0, -3.25F, -3.0F, 1.0F, 1, 2, 1, 0.075F));
		group.cubeList.add(new ModelBox(group, 58, 2, -4.25F, -2.0F, -0.5F, 2, 1, 1, 0.1F));
		group.cubeList.add(new ModelBox(group, 60, 0, -4.25F, -1.0F, -0.5F, 1, 1, 1, 0.15F));
		group.cubeList.add(new ModelBox(group, 60, 0, -4.25F, 0.0F, 1.0F, 1, 1, 1, 0.15F));
		group.cubeList.add(new ModelBox(group, 60, 0, -4.25F, 0.0F, -2.0F, 1, 1, 1, 0.15F));
		group.cubeList.add(new ModelBox(group, 58, 2, -4.25F, -1.0F, -2.0F, 2, 1, 1, 0.1F));
		group.cubeList.add(new ModelBox(group, 56, 0, -3.25F, -3.0F, -2.0F, 1, 2, 1, 0.075F));

		cube_r9 = new ModelRenderer(this);
		cube_r9.setRotationPoint(0.0F, 0.0F, 0.0F);
		group.addChild(cube_r9);
		setRotationAngle(cube_r9, 0.0F, 0.0F, -0.0873F);
		cube_r9.cubeList.add(new ModelBox(cube_r9, 60, 4, -4.0F, 3.9657F, -2.0F, 1, 3, 1, 0.075F));
		cube_r9.cubeList.add(new ModelBox(cube_r9, 60, 4, -4.0F, 4.9657F, 1.0F, 1, 2, 1, 0.075F));

		cube_r10 = new ModelRenderer(this);
		cube_r10.setRotationPoint(0.0F, -1.0F, -1.0F);
		group.addChild(cube_r10);
		setRotationAngle(cube_r10, 0.0F, 0.0F, -0.0873F);
		cube_r10.cubeList.add(new ModelBox(cube_r10, 60, 4, -4.0F, 4.9657F, 0.5F, 1, 2, 1, 0.075F));

		bipedRightArm = new ModelRenderer(this);
		bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		bipedRightArm.cubeList.add(new ModelBox(bipedRightArm, 24, 0, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.55F));
		bipedRightArm.cubeList.add(new ModelBox(bipedRightArm, 25, 16, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.75F));

		RightArm_r1 = new ModelRenderer(this);
		RightArm_r1.setRotationPoint(4.4F, -2.3F, -0.05F);
		bipedRightArm.addChild(RightArm_r1);
		setRotationAngle(RightArm_r1, 0.1745F, 0.0F, -0.0873F);
		RightArm_r1.mirror = true;
		RightArm_r1.cubeList.add(new ModelBox(RightArm_r1, 0, 60, -9.8303F, -3.0799F, -2.0F, 4, 4, 0, 0.0F));
		RightArm_r1.mirror = false;

		RightArm_r2 = new ModelRenderer(this);
		RightArm_r2.setRotationPoint(5.0F, 18.8F, 1.15F);
		bipedRightArm.addChild(RightArm_r2);
		setRotationAngle(RightArm_r2, 0.0F, 0.0F, -0.1309F);
		RightArm_r2.mirror = true;
		RightArm_r2.cubeList.add(new ModelBox(RightArm_r2, 0, 60, -8.0F, -24.0F, -2.0F, 4, 4, 0, 0.0F));
		RightArm_r2.mirror = false;
		RightArm_r2.mirror = true;
		RightArm_r2.cubeList.add(new ModelBox(RightArm_r2, 0, 60, -8.0F, -24.0F, -0.3F, 4, 4, 0, 0.0F));
		RightArm_r2.mirror = false;

		RightArm_r3 = new ModelRenderer(this);
		RightArm_r3.setRotationPoint(5.0F, -2.4F, 0.25F);
		bipedRightArm.addChild(RightArm_r3);
		setRotationAngle(RightArm_r3, -0.1745F, 0.0F, -0.0873F);
		RightArm_r3.mirror = true;
		RightArm_r3.cubeList.add(new ModelBox(RightArm_r3, 0, 60, -9.8303F, -3.0799F, 2.0F, 4, 4, 0, 0.0F));
		RightArm_r3.mirror = false;

		right_pauldron = new ModelRenderer(this);
		right_pauldron.setRotationPoint(6.0F, 12.0F, -3.0F);
		bipedRightArm.addChild(right_pauldron);

		group2 = new ModelRenderer(this);
		group2.setRotationPoint(-13.0F, -8.0F, 3.0F);
		right_pauldron.addChild(group2);
		setRotationAngle(group2, 0.0F, 0.0F, -0.0873F);
		group2.mirror = true;
		group2.cubeList.add(new ModelBox(group2, 56, 0, 2.25F, -4.0F, -0.5F, 1, 2, 1, 0.075F));
		group2.mirror = false;
		group2.mirror = true;
		group2.cubeList.add(new ModelBox(group2, 58, 2, 2.25F, -1.0F, 1.0F, 2, 1, 1, 0.1F));
		group2.mirror = false;
		group2.mirror = true;
		group2.cubeList.add(new ModelBox(group2, 56, 0, 2.25F, -3.0F, 1.0F, 1, 2, 1, 0.075F));
		group2.mirror = false;
		group2.mirror = true;
		group2.cubeList.add(new ModelBox(group2, 58, 2, 2.25F, -2.0F, -0.5F, 2, 1, 1, 0.1F));
		group2.mirror = false;
		group2.mirror = true;
		group2.cubeList.add(new ModelBox(group2, 60, 0, 3.25F, -1.0F, -0.5F, 1, 1, 1, 0.15F));
		group2.mirror = false;
		group2.mirror = true;
		group2.cubeList.add(new ModelBox(group2, 60, 0, 3.25F, 0.0F, 1.0F, 1, 1, 1, 0.15F));
		group2.mirror = false;
		group2.mirror = true;
		group2.cubeList.add(new ModelBox(group2, 60, 0, 3.25F, 0.0F, -2.0F, 1, 1, 1, 0.15F));
		group2.mirror = false;
		group2.mirror = true;
		group2.cubeList.add(new ModelBox(group2, 58, 2, 2.25F, -1.0F, -2.0F, 2, 1, 1, 0.1F));
		group2.mirror = false;
		group2.mirror = true;
		group2.cubeList.add(new ModelBox(group2, 56, 0, 2.25F, -3.0F, -2.0F, 1, 2, 1, 0.075F));
		group2.mirror = false;

		cube_r11 = new ModelRenderer(this);
		cube_r11.setRotationPoint(0.0F, 0.0F, 3.0F);
		group2.addChild(cube_r11);
		setRotationAngle(cube_r11, 0.0F, 0.0F, 0.0873F);
		cube_r11.mirror = true;
		cube_r11.cubeList.add(new ModelBox(cube_r11, 60, 4, 3.0F, 4.9657F, -2.0F, 1, 2, 1, 0.075F));
		cube_r11.mirror = false;
		cube_r11.mirror = true;
		cube_r11.cubeList.add(new ModelBox(cube_r11, 60, 4, 3.0F, 3.9657F, -5.0F, 1, 3, 1, 0.075F));
		cube_r11.mirror = false;

		cube_r12 = new ModelRenderer(this);
		cube_r12.setRotationPoint(0.0F, -1.0F, -1.0F);
		group2.addChild(cube_r12);
		setRotationAngle(cube_r12, 0.0F, 0.0F, 0.0873F);
		cube_r12.mirror = true;
		cube_r12.cubeList.add(new ModelBox(cube_r12, 60, 4, 3.0F, 4.9657F, 0.5F, 1, 2, 1, 0.075F));
		cube_r12.mirror = false;

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
