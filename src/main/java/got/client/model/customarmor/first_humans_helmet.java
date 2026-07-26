package got.client.model.customarmor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class first_humans_helmet extends ModelBiped {
	private final ModelRenderer horn;
	private final ModelRenderer cube_r1;
	private final ModelRenderer group3;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer horn2;
	private final ModelRenderer cube_r4;
	private final ModelRenderer group4;
	private final ModelRenderer cube_r5;
	private final ModelRenderer cube_r6;

	public first_humans_helmet() {
		super(0.0F);
		textureWidth = 64;
		textureHeight = 64;

		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 36, -4.5F, -9.0F, -4.5F, 9, 9, 9, 0.1F));
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 18, -4.5F, -8.25F, -4.5F, 9, 9, 9, 0.3F));

		horn = new ModelRenderer(this);
		horn.setRotationPoint(-1.0F, -10.0F, 7.0F);
		bipedHead.addChild(horn);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(6.0F, 6.0F, -8.0F);
		horn.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.0F, -0.6981F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 58, -1.0F, -1.8F, -2.0F, 3, 3, 3, 0.0F));

		group3 = new ModelRenderer(this);
		group3.setRotationPoint(-1.0F, 6.0F, -8.5F);
		horn.addChild(group3);
		setRotationAngle(group3, 0.0F, 3.1416F, 0.3054F);

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(1.0F, -6.0F, 8.5F);
		group3.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.0F, -0.48F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 29, 57, -6.6569F, -7.6568F, -9.5F, 4, 1, 2, 0.0F));
		cube_r2.cubeList.add(new ModelBox(cube_r2, 26, 60, -7.6569F, -6.6568F, -9.5F, 6, 2, 2, 0.0F));
		cube_r2.cubeList.add(new ModelBox(cube_r2, 42, 60, -2.6568F, -4.6568F, -9.5F, 2, 2, 2, 0.0F));

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(0.0F, 0.0F, 8.5F);
		group3.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0F, 0.0F, -1.3963F);
		cube_r3.cubeList.add(new ModelBox(cube_r3, 11, 60, 1.5442F, -8.9316F, -9.5F, 5, 2, 2, -0.02F));

		horn2 = new ModelRenderer(this);
		horn2.setRotationPoint(-7.0F, -10.0F, 7.0F);
		bipedHead.addChild(horn2);

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(2.0F, 6.0F, -8.0F);
		horn2.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0F, 0.0F, 0.6981F);
		cube_r4.mirror = true;
		cube_r4.cubeList.add(new ModelBox(cube_r4, 0, 58, -2.0F, -1.8F, -2.0F, 3, 3, 3, 0.0F));
		cube_r4.mirror = false;

		group4 = new ModelRenderer(this);
		group4.setRotationPoint(1.0F, 6.0F, -8.5F);
		horn2.addChild(group4);
		setRotationAngle(group4, 0.0F, -3.1416F, -0.3054F);

		cube_r5 = new ModelRenderer(this);
		cube_r5.setRotationPoint(-1.0F, -6.0F, 8.5F);
		group4.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.0F, 0.0F, 0.48F);
		cube_r5.mirror = true;
		cube_r5.cubeList.add(new ModelBox(cube_r5, 29, 57, -3.0F, -2.0F, -9.5F, 4, 1, 2, 0.0F));
		cube_r5.mirror = false;
		cube_r5.mirror = true;
		cube_r5.cubeList.add(new ModelBox(cube_r5, 26, 60, -4.0F, -1.0F, -9.5F, 6, 2, 2, 0.0F));
		cube_r5.mirror = false;
		cube_r5.mirror = true;
		cube_r5.cubeList.add(new ModelBox(cube_r5, 42, 60, -5.0F, 1.0F, -9.5F, 2, 2, 2, 0.0F));
		cube_r5.mirror = false;

		cube_r6 = new ModelRenderer(this);
		cube_r6.setRotationPoint(0.0F, 0.0F, 8.5F);
		group4.addChild(cube_r6);
		setRotationAngle(cube_r6, 0.0F, 0.0F, 1.3963F);
		cube_r6.mirror = true;
		cube_r6.cubeList.add(new ModelBox(cube_r6, 11, 60, -5.5F, -1.0F, -9.5F, 5, 2, 2, -0.02F));
		cube_r6.mirror = false;

		bipedBody = new ModelRenderer(this);
		bipedHeadwear = new ModelRenderer(this);
		bipedLeftArm = new ModelRenderer(this);
		bipedLeftLeg = new ModelRenderer(this);
		bipedRightArm = new ModelRenderer(this);
		bipedRightLeg = new ModelRenderer(this);
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
