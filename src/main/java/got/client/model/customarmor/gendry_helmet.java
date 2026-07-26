package got.client.model.customarmor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class gendry_helmet extends ModelBiped {
	private final ModelRenderer addon;
	private final ModelRenderer addon2;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer addon3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer cube_r5;
	private final ModelRenderer cube_r6;

	public gendry_helmet() {
		super(0.0F);
		textureWidth = 64;
		textureHeight = 64;

		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 0, -4.5F, -8.75F, -4.5F, 9, 9, 9, 0.36F));

		addon = new ModelRenderer(this);
		addon.setRotationPoint(-1.0F, 24.0F, -10.0F);
		bipedHead.addChild(addon);
		addon.cubeList.add(new ModelBox(addon, 50, 60, -1.5F, -28.0F, 4.0F, 5, 2, 2, 0.0F));
		addon.cubeList.add(new ModelBox(addon, 3, 49, 0.0F, -33.75F, 4.6F, 2, 6, 2, 0.0F));
		addon.cubeList.add(new ModelBox(addon, 42, 60, 2.25F, -29.75F, 4.8F, 2, 2, 2, 0.0F));
		addon.mirror = true;
		addon.cubeList.add(new ModelBox(addon, 42, 60, -2.25F, -29.75F, 4.8F, 2, 2, 2, 0.0F));
		addon.mirror = false;
		addon.cubeList.add(new ModelBox(addon, 32, 17, -3.0F, -34.0F, 5.9F, 8, 2, 8, 0.0F));
		addon.cubeList.add(new ModelBox(addon, 54, 57, 1.75F, -30.6F, 4.5F, 3, 1, 2, 0.0F));
		addon.mirror = true;
		addon.cubeList.add(new ModelBox(addon, 54, 57, -2.75F, -30.6F, 4.5F, 3, 1, 2, 0.0F));
		addon.mirror = false;
		addon.cubeList.add(new ModelBox(addon, 0, 57, -2.5F, -27.75F, 4.5F, 7, 4, 3, 0.0F));
		addon.cubeList.add(new ModelBox(addon, 1, 60, -1.5F, -24.0F, 5.0F, 5, 1, 3, 0.0F));

		addon2 = new ModelRenderer(this);
		addon2.setRotationPoint(1.0F, 24.0F, -10.0F);
		bipedHead.addChild(addon2);
		addon2.mirror = true;
		addon2.cubeList.add(new ModelBox(addon2, 22, 54, 4.7F, -34.0F, 7.5F, 2, 2, 2, 0.0F));
		addon2.mirror = false;

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(4.0F, -29.0F, 9.0F);
		addon2.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.0F, -0.6981F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 20, 58, -2.0F, -1.7F, -2.0F, 4, 3, 3, 0.0F));

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(6.0F, -33.0F, 8.0F);
		addon2.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.0F, -0.1745F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 31, 58, -0.8F, -3.0F, 0.0F, 1, 2, 1, 0.2F));

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(5.0F, -29.0F, 17.0F);
		addon2.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0F, 0.0F, -1.3963F);
		cube_r3.mirror = true;
		cube_r3.cubeList.add(new ModelBox(cube_r3, 22, 54, 0.5F, -0.9F, -9.5F, 3, 2, 2, 0.1F));
		cube_r3.mirror = false;

		addon3 = new ModelRenderer(this);
		addon3.setRotationPoint(-1.0F, 24.0F, -10.0F);
		bipedHead.addChild(addon3);
		addon3.cubeList.add(new ModelBox(addon3, 22, 54, -6.7F, -34.0F, 7.5F, 2, 2, 2, 0.0F));

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(-6.0F, -33.0F, 8.0F);
		addon3.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0F, 0.0F, 0.1745F);
		cube_r4.mirror = true;
		cube_r4.cubeList.add(new ModelBox(cube_r4, 31, 58, -0.2F, -3.0F, 0.0F, 1, 2, 1, 0.2F));
		cube_r4.mirror = false;

		cube_r5 = new ModelRenderer(this);
		cube_r5.setRotationPoint(-5.0F, -29.0F, 17.0F);
		addon3.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.0F, 0.0F, 1.3963F);
		cube_r5.cubeList.add(new ModelBox(cube_r5, 22, 54, -3.5F, -0.9F, -9.5F, 3, 2, 2, 0.1F));

		cube_r6 = new ModelRenderer(this);
		cube_r6.setRotationPoint(-4.0F, -29.0F, 9.0F);
		addon3.addChild(cube_r6);
		setRotationAngle(cube_r6, 0.0F, 0.0F, 0.6981F);
		cube_r6.mirror = true;
		cube_r6.cubeList.add(new ModelBox(cube_r6, 20, 58, -2.0F, -1.7F, -2.0F, 4, 3, 3, 0.0F));
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