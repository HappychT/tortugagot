package com.tortugagot.togcore.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class KnightModel extends ModelBase {
	private final ModelRenderer torso;
	private final ModelRenderer torso_top;
	private final ModelRenderer h_head;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer cape_1;
	private final ModelRenderer cape_2;
	private final ModelRenderer cape_3;
	private final ModelRenderer right_pauldron;
	private final ModelRenderer cube_r5;
	private final ModelRenderer left_pauldron;
	private final ModelRenderer cube_r6;
	private final ModelRenderer right_arm;
	private final ModelRenderer right_forearm;
	private final ModelRenderer cube_r7;
	private final ModelRenderer right_hand;
	private final ModelRenderer sword;
	private final ModelRenderer cube_r8;
	private final ModelRenderer cube_r9;
	private final ModelRenderer left_arm;
	private final ModelRenderer left_forearm;
	private final ModelRenderer cube_r10;
	private final ModelRenderer shield_arm;
	private final ModelRenderer cube_r11;
	private final ModelRenderer cube_r12;
	private final ModelRenderer left_hand;
	private final ModelRenderer tabard;
	private final ModelRenderer right_leg;
	private final ModelRenderer right_shin;
	private final ModelRenderer cube_r13;
	private final ModelRenderer left_leg;
	private final ModelRenderer left_shin;
	private final ModelRenderer cube_r14;
	private final ModelRenderer sheath;
	private final ModelRenderer cube_r15;
	private final ModelRenderer cube_r16;

	private static float prevLimbSwing = 0F;

	public KnightModel() {
		textureWidth = 128;
		textureHeight = 128;

		torso = new ModelRenderer(this);
		torso.setRotationPoint(0.0F, 10.0F, 1.0F);
		torso.cubeList.add(new ModelBox(torso, 30, 1, -4.0F, -4.0F, -3.0F, 8, 5, 5, 0.0F));

		torso_top = new ModelRenderer(this);
		torso_top.setRotationPoint(0.0F, -4.0F, 2.0F);
		torso.addChild(torso_top);
		torso_top.cubeList.add(new ModelBox(torso_top, 0, 0, -5.0F, -6.0F, -5.0F, 10, 6, 5, 0.0F));

		h_head = new ModelRenderer(this);
		h_head.setRotationPoint(-0.214F, -6.125F, -2.9277F);
		torso_top.addChild(h_head);
		h_head.cubeList.add(new ModelBox(h_head, 56, 0, -3.786F, -7.875F, -4.0723F, 8, 8, 8, 0.0F));

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(5.0392F, -1.375F, 0.1512F);
		h_head.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.3927F, 0.0F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 51, -5, 0.0F, -2.5F, -2.5F, 0, 5, 5, 0.0F));

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(-4.6112F, -1.375F, 0.1512F);
		h_head.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, -0.3927F, 0.0F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 25, -5, 0.0F, -2.5F, -2.5F, 0, 5, 5, 0.0F));

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(-3.3056F, -3.875F, -3.9274F);
		h_head.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0F, 0.3927F, 0.0F);
		cube_r3.cubeList.add(new ModelBox(cube_r3, 14, 11, -1.0F, 0.0F, -0.5F, 5, 5, 2, 0.0F));

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(3.7028F, -3.875F, -3.9274F);
		h_head.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0F, -0.3927F, 0.0F);
		cube_r4.cubeList.add(new ModelBox(cube_r4, 0, 11, -4.0F, 0.0F, -0.5F, 5, 5, 2, 0.0F));

		cape_1 = new ModelRenderer(this);
		cape_1.setRotationPoint(0.0F, -6.0F, 0.0F);
		torso_top.addChild(cape_1);
		setRotationAngle(cape_1, 0.3927F, 0.0F, 0.0F);
		cape_1.cubeList.add(new ModelBox(cape_1, 112, 0, -4.0F, 0.0F, 0.0F, 8, 5, 0, 0.0F));

		cape_2 = new ModelRenderer(this);
		cape_2.setRotationPoint(0.0F, 5.0F, 0.0F);
		cape_1.addChild(cape_2);
		cape_2.cubeList.add(new ModelBox(cape_2, 80, 0, -4.0F, 0.0F, 0.0F, 8, 5, 0, 0.0F));

		cape_3 = new ModelRenderer(this);
		cape_3.setRotationPoint(0.0F, 5.0F, 0.0F);
		cape_2.addChild(cape_3);
		cape_3.cubeList.add(new ModelBox(cape_3, 96, 0, -4.0F, 0.0F, 0.0F, 8, 5, 0, 0.0F));

		right_pauldron = new ModelRenderer(this);
		right_pauldron.setRotationPoint(-4.5F, -5.0F, -2.5F);
		torso_top.addChild(right_pauldron);
		setRotationAngle(right_pauldron, 0.1776F, 0.0283F, 0.086F);
		right_pauldron.cubeList.add(new ModelBox(right_pauldron, 0, 18, -4.0F, -2.0F, -3.0F, 5, 5, 6, 0.0F));

		cube_r5 = new ModelRenderer(this);
		cube_r5.setRotationPoint(2.3858F, 1.0F, -3.574F);
		right_pauldron.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.0F, 0.3927F, 0.0F);
		cube_r5.cubeList.add(new ModelBox(cube_r5, 42, 22, -1.5F, -2.0F, 0.0F, 3, 4, 0, 0.0F));

		left_pauldron = new ModelRenderer(this);
		left_pauldron.setRotationPoint(4.5F, -5.0F, -2.5F);
		torso_top.addChild(left_pauldron);
		setRotationAngle(left_pauldron, 0.1776F, -0.0283F, -0.086F);
		left_pauldron.cubeList.add(new ModelBox(left_pauldron, 0, 29, -1.0F, -2.0F, -3.0F, 5, 5, 6, 0.0F));

		cube_r6 = new ModelRenderer(this);
		cube_r6.setRotationPoint(-2.3858F, 1.0F, -3.574F);
		left_pauldron.addChild(cube_r6);
		setRotationAngle(cube_r6, 0.0F, -0.3927F, 0.0F);
		cube_r6.cubeList.add(new ModelBox(cube_r6, 28, 13, -1.5F, -2.0F, 0.0F, 3, 4, 0, 0.0F));

		right_arm = new ModelRenderer(this);
		right_arm.setRotationPoint(-4.5F, -4.75F, -2.5F);
		torso_top.addChild(right_arm);
		setRotationAngle(right_arm, 0.0873F, 0.0F, 0.2182F);
		right_arm.cubeList.add(new ModelBox(right_arm, 44, 11, -3.0F, -1.25F, -2.0F, 4, 7, 4, 0.0F));

		right_forearm = new ModelRenderer(this);
		right_forearm.setRotationPoint(-1.2641F, 5.6667F, 1.9021F);
		right_arm.addChild(right_forearm);
		setRotationAngle(right_forearm, -0.1745F, 0.0F, 0.0F);
		right_forearm.cubeList
				.add(new ModelBox(right_forearm, 0, 40, -1.2359F, 0.0833F, -3.9021F, 3, 5, 4, 0.0F));

		cube_r7 = new ModelRenderer(this);
		cube_r7.setRotationPoint(-0.5281F, 1.5833F, 0.8043F);
		right_forearm.addChild(cube_r7);
		setRotationAngle(cube_r7, 0.0F, 0.7854F, 0.0F);
		cube_r7.cubeList.add(new ModelBox(cube_r7, 0, -2, 0.0F, -1.5F, -1.0F, 0, 3, 2, 0.0F));

		right_hand = new ModelRenderer(this);
		right_hand.setRotationPoint(-1.2359F, 5.0833F, -1.9021F);
		right_forearm.addChild(right_hand);
		setRotationAngle(right_hand, 0.0F, -0.0436F, -0.2182F);
		right_hand.cubeList.add(new ModelBox(right_hand, 114, 5, 0.0F, 0.0F, -2.0F, 3, 4, 4, 0.0F));

		sword = new ModelRenderer(this);
		sword.setRotationPoint(1.5F, 2.0F, 0.0F);
		right_hand.addChild(sword);
		sword.cubeList.add(new ModelBox(sword, 97, 30, 0.0F, -1.0F, -18.5F, 0, 2, 15, 0.0F));
		sword.cubeList.add(new ModelBox(sword, 16, 18, -0.5F, -2.5F, -3.5F, 1, 5, 1, 0.0F));
		sword.cubeList.add(new ModelBox(sword, 8, 43, -0.5F, -0.5F, -2.5F, 1, 1, 6, 0.0F));

		cube_r8 = new ModelRenderer(this);
		cube_r8.setRotationPoint(1.0F, 0.8358F, -1.7929F);
		sword.addChild(cube_r8);
		setRotationAngle(cube_r8, 0.7854F, 0.0F, 0.0F);
		cube_r8.cubeList.add(new ModelBox(cube_r8, 1, 18, -1.5F, -0.5F, -2.75F, 1, 1, 1, 0.0F));

		cube_r9 = new ModelRenderer(this);
		cube_r9.setRotationPoint(1.0F, -3.9142F, -1.7929F);
		sword.addChild(cube_r9);
		setRotationAngle(cube_r9, 0.7854F, 0.0F, 0.0F);
		cube_r9.cubeList.add(new ModelBox(cube_r9, 1, 21, -1.5F, -0.5F, -2.75F, 1, 1, 1, 0.0F));

		left_arm = new ModelRenderer(this);
		left_arm.setRotationPoint(4.5F, -4.75F, -2.5F);
		torso_top.addChild(left_arm);
		setRotationAngle(left_arm, 0.0873F, 0.0F, -0.2182F);
		left_arm.cubeList.add(new ModelBox(left_arm, 112, 13, -1.0F, -1.25F, -2.0F, 4, 7, 4, 0.0F));

		left_forearm = new ModelRenderer(this);
		left_forearm.setRotationPoint(1.2641F, 5.6667F, 1.9021F);
		left_arm.addChild(left_forearm);
		setRotationAngle(left_forearm, -0.1745F, 0.0F, 0.0F);
		left_forearm.cubeList
				.add(new ModelBox(left_forearm, 114, 28, -1.7641F, 0.0833F, -3.9021F, 3, 5, 4, 0.0F));

		cube_r10 = new ModelRenderer(this);
		cube_r10.setRotationPoint(0.5281F, 1.5833F, 0.8043F);
		left_forearm.addChild(cube_r10);
		setRotationAngle(cube_r10, 0.0F, -0.7854F, 0.0F);
		cube_r10.cubeList.add(new ModelBox(cube_r10, 108, 7, 0.0F, -1.5F, -1.0F, 0, 3, 2, 0.0F));

		shield_arm = new ModelRenderer(this);
		shield_arm.setRotationPoint(2.4437F, 2.3333F, -1.5194F);
		left_forearm.addChild(shield_arm);
		setRotationAngle(shield_arm, 1.5708F, 0.0F, 1.5708F);

		cube_r11 = new ModelRenderer(this);
		cube_r11.setRotationPoint(-3.0422F, 3.0F, -0.3827F);
		shield_arm.addChild(cube_r11);
		setRotationAngle(cube_r11, 0.0F, -0.3927F, 0.0F);
		cube_r11.cubeList.add(new ModelBox(cube_r11, 94, 17, -2.5F, -9.0F, -0.5F, 6, 11, 1, 0.0F));
		cube_r11.cubeList.add(new ModelBox(cube_r11, 108, 5, -0.5F, 2.0F, -0.5F, 4, 3, 1, 0.0F));

		cube_r12 = new ModelRenderer(this);
		cube_r12.setRotationPoint(3.0422F, 3.0F, -0.3827F);
		shield_arm.addChild(cube_r12);
		setRotationAngle(cube_r12, 0.0F, 0.3927F, 0.0F);
		cube_r12.cubeList.add(new ModelBox(cube_r12, 118, 24, -3.5F, 2.0F, -0.5F, 4, 3, 1, 0.0F));
		cube_r12.cubeList.add(new ModelBox(cube_r12, 94, 5, -3.5F, -9.0F, -0.5F, 6, 11, 1, 0.0F));

		left_hand = new ModelRenderer(this);
		left_hand.setRotationPoint(1.2359F, 5.0833F, -1.9021F);
		left_forearm.addChild(left_hand);
		setRotationAngle(left_hand, 0.0F, 0.0436F, 0.2182F);
		left_hand.cubeList.add(new ModelBox(left_hand, 114, 37, -3.0F, 0.0F, -2.0F, 3, 4, 4, 0.0F));

		tabard = new ModelRenderer(this);
		tabard.setRotationPoint(0.0F, 1.0F, -3.0F);
		torso.addChild(tabard);
		tabard.cubeList.add(new ModelBox(tabard, 68, 20, -3.0F, 0.0F, 0.0F, 6, 7, 0, 0.0F));

		right_leg = new ModelRenderer(this);
		right_leg.setRotationPoint(-2.3333F, 1.0254F, -1.4609F);
		torso.addChild(right_leg);
		setRotationAngle(right_leg, 0.1035F, 0.1655F, 0.1035F);
		right_leg.cubeList.add(new ModelBox(right_leg, 44, 22, -1.6667F, -0.0254F, -1.0391F, 4, 7, 4, 0.0F));

		right_shin = new ModelRenderer(this);
		right_shin.setRotationPoint(0.3333F, 6.9746F, -1.0391F);
		right_leg.addChild(right_shin);
		right_shin.cubeList.add(new ModelBox(right_shin, 95, 29, -1.5F, 0.0F, 0.0F, 3, 6, 3, 0.0F));

		cube_r13 = new ModelRenderer(this);
		cube_r13.setRotationPoint(0.0F, -0.9239F, -0.3827F);
		right_shin.addChild(cube_r13);
		setRotationAngle(cube_r13, 0.3927F, 0.0F, 0.0F);
		cube_r13.cubeList.add(new ModelBox(cube_r13, 36, 14, -1.5F, -1.0F, 0.0F, 3, 2, 0, 0.0F));

		left_leg = new ModelRenderer(this);
		left_leg.setRotationPoint(2.3333F, 1.0254F, -1.4609F);
		torso.addChild(left_leg);
		setRotationAngle(left_leg, 0.1035F, -0.1655F, -0.1035F);
		left_leg.cubeList.add(new ModelBox(left_leg, 44, 33, -2.3333F, -0.0254F, -1.0391F, 4, 7, 4, 0.0F));

		left_shin = new ModelRenderer(this);
		left_shin.setRotationPoint(-0.3333F, 6.9746F, -1.0391F);
		left_leg.addChild(left_shin);
		left_shin.cubeList.add(new ModelBox(left_shin, 83, 29, -1.5F, 0.0F, 0.0F, 3, 6, 3, 0.0F));

		cube_r14 = new ModelRenderer(this);
		cube_r14.setRotationPoint(0.0F, -0.9239F, -0.3827F);
		left_shin.addChild(cube_r14);
		setRotationAngle(cube_r14, 0.3927F, 0.0F, 0.0F);
		cube_r14.cubeList.add(new ModelBox(cube_r14, 36, 12, -1.5F, -1.0F, 0.0F, 3, 2, 0, 0.0F));

		sheath = new ModelRenderer(this);
		sheath.setRotationPoint(4.5F, 2.0F, -0.5F);
		torso.addChild(sheath);
		setRotationAngle(sheath, -0.6619F, 0.1382F, -0.1069F);
		sheath.cubeList.add(new ModelBox(sheath, 27, 29, -0.5F, -1.0F, -2.5F, 1, 2, 15, 0.0F));
		sheath.cubeList.add(new ModelBox(sheath, 64, 20, -0.5F, -2.5F, -3.5F, 1, 5, 1, 0.0F));
		sheath.cubeList.add(new ModelBox(sheath, 69, 31, -0.5F, -0.5F, -9.5F, 1, 1, 6, 0.0F));

		cube_r15 = new ModelRenderer(this);
		cube_r15.setRotationPoint(1.0F, 0.8358F, -4.2071F);
		sheath.addChild(cube_r15);
		setRotationAngle(cube_r15, -0.7854F, 0.0F, 0.0F);
		cube_r15.cubeList.add(new ModelBox(cube_r15, 56, 23, -1.5F, -0.5F, 1.25F, 1, 1, 1, 0.0F));

		cube_r16 = new ModelRenderer(this);
		cube_r16.setRotationPoint(1.0F, -3.9142F, -4.2071F);
		sheath.addChild(cube_r16);
		setRotationAngle(cube_r16, -0.7854F, 0.0F, 0.0F);
		cube_r16.cubeList.add(new ModelBox(cube_r16, 63, 26, -1.5F, -0.5F, 1.25F, 1, 1, 1, 0.0F));
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scaleFactor) {
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
		torso.render(scaleFactor);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scaleFactor, Entity entity) {
		// mob idle, if that values is equals
		if (limbSwing == prevLimbSwing) {
			torso_top.rotateAngleX = MathHelper.sin(ageInTicks * 0.1f) * 0.05f;
			right_leg.rotateAngleZ = (float) Math.toRadians(3);
			left_leg.rotateAngleX = -right_leg.rotateAngleX;
		} else {
			torso_top.rotateAngleY = MathHelper.sin(limbSwing * 0.9f) * (limbSwingAmount * 0.05f);
			torso_top.rotateAngleZ = MathHelper.sin(limbSwing * 0.9f) * (limbSwingAmount * 0.05f);
			right_arm.rotateAngleX = MathHelper.sin(limbSwing * 0.9f) * (limbSwingAmount * 0.7f);
			right_forearm.rotateAngleX = -MathHelper.sin(limbSwing * 0.9f) * (limbSwingAmount * 0.2f);
			right_leg.rotateAngleX = MathHelper.sin(limbSwing * 0.9f) * (limbSwingAmount * 0.6f);
			right_leg.rotateAngleZ = (float) Math.toRadians(-4);
			left_arm.rotateAngleX = -right_arm.rotateAngleX;
			right_forearm.rotateAngleX = -right_forearm.rotateAngleX;
			left_leg.rotateAngleX = -right_leg.rotateAngleX;
		}
		prevLimbSwing = limbSwing;
	}
}
