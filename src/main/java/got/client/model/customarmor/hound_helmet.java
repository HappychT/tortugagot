package got.client.model.customarmor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class hound_helmet extends ModelBiped {
	private final ModelRenderer addon;
	private final ModelRenderer inner_helmet_r1;
	private final ModelRenderer inner_helmet_r2;

	public hound_helmet() {
		super(0.0F);
		textureWidth = 64;
		textureHeight = 64;

		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 0, -4.5F, -8.75F, -4.5F, 9, 9, 9, 0.36F));

		addon = new ModelRenderer(this);
		addon.setRotationPoint(-1.0F, 24.0F, -10.0F);
		bipedHead.addChild(addon);
		addon.cubeList.add(new ModelBox(addon, 36, 12, -2.45F, -30.0F, 4.5F, 1, 3, 2, 0.0F));
		addon.cubeList.add(new ModelBox(addon, 37, 28, -0.5F, -32.0F, 1.0F, 3, 2, 2, 0.0F));
		addon.cubeList.add(new ModelBox(addon, 34, 28, -1.5F, -32.75F, 4.5F, 5, 2, 5, 0.0F));
		addon.cubeList.add(new ModelBox(addon, 0, 6, 2.25F, -32.75F, 4.4F, 2, 1, 2, 0.0F));
		addon.mirror = true;
		addon.cubeList.add(new ModelBox(addon, 0, 6, -2.25F, -32.75F, 4.4F, 2, 1, 2, 0.0F));
		addon.mirror = false;
		addon.cubeList.add(new ModelBox(addon, 32, 17, -3.0F, -34.0F, 5.9F, 8, 2, 8, 0.0F));
		addon.cubeList.add(new ModelBox(addon, 36, 9, 1.75F, -33.6F, 4.0F, 3, 1, 2, 0.0F));
		addon.mirror = true;
		addon.cubeList.add(new ModelBox(addon, 36, 9, -2.75F, -33.6F, 4.0F, 3, 1, 2, 0.0F));
		addon.mirror = false;
		addon.cubeList.add(new ModelBox(addon, 33, 1, -2.5F, -31.75F, 1.5F, 7, 3, 5, 0.0F));
		addon.cubeList.add(new ModelBox(addon, 36, 12, 3.45F, -30.0F, 4.5F, 1, 3, 2, 0.0F));
		addon.cubeList.add(new ModelBox(addon, 0, 3, 3.15F, -29.5F, 1.6F, 1, 2, 1, 0.0F));
		addon.cubeList.add(new ModelBox(addon, 0, 3, -2.15F, -29.5F, 1.6F, 1, 2, 1, 0.0F));

		inner_helmet_r1 = new ModelRenderer(this);
		inner_helmet_r1.setRotationPoint(1.0F, 3.0F, 7.0F);
		addon.addChild(inner_helmet_r1);
		setRotationAngle(inner_helmet_r1, 0.1745F, 0.0F, 0.0F);
		inner_helmet_r1.cubeList.add(new ModelBox(inner_helmet_r1, 0, 0, -2.4F, -30.0F, 0.5F, 1, 2, 1, 0.0F));
		inner_helmet_r1.cubeList.add(new ModelBox(inner_helmet_r1, 0, 0, 1.4F, -30.0F, 0.5F, 1, 2, 1, 0.0F));

		inner_helmet_r2 = new ModelRenderer(this);
		inner_helmet_r2.setRotationPoint(6.0F, 4.0F, 7.0F);
		addon.addChild(inner_helmet_r2);
		setRotationAngle(inner_helmet_r2, 0.1745F, 0.0F, 0.0F);
		inner_helmet_r2.cubeList.add(new ModelBox(inner_helmet_r2, 46, 9, -7.5F, -30.0F, 0.5F, 5, 2, 4, 0.0F));

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