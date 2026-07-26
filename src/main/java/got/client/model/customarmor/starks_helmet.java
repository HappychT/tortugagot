package got.client.model.customarmor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class starks_helmet extends ModelBiped {
	private final ModelRenderer addon;
	private final ModelRenderer inner_helmet;
	private final ModelRenderer inner_helmet3;
	private final ModelRenderer addon2;

	public starks_helmet() {
		super(0.0F);
		textureWidth = 64;
		textureHeight = 64;

		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 46, -4.5F, -6.75F, -4.5F, 9, 7, 9, 0.36F));

		addon = new ModelRenderer(this);
		addon.setRotationPoint(-1.0F, 24.0F, -10.0F);
		bipedHead.addChild(addon);
		addon.cubeList.add(new ModelBox(addon, 39, 1, -0.5F, -32.5F, 1.0F, 3, 2, 2, 0.0F));
		addon.cubeList.add(new ModelBox(addon, 8, 39, -3.5F, -33.5F, 13.75F, 9, 4, 1, 0.02F));
		addon.cubeList.add(new ModelBox(addon, 9, 39, -2.5F, -34.5F, 5.0F, 3, 1, 1, 0.0F));
		addon.mirror = true;
		addon.cubeList.add(new ModelBox(addon, 9, 39, 1.5F, -34.5F, 5.0F, 3, 1, 1, 0.0F));
		addon.mirror = false;
		addon.cubeList.add(new ModelBox(addon, 44, 0, -1.5F, -32.25F, 1.5F, 5, 2, 5, 0.1F));
		addon.cubeList.add(new ModelBox(addon, 44, 7, -1.5F, -29.5F, 1.5F, 5, 1, 5, 0.0F));
		addon.mirror = true;
		addon.cubeList.add(new ModelBox(addon, 0, 31, -4.0F, -34.75F, 5.0F, 10, 6, 9, -0.2F));
		addon.mirror = false;
		addon.cubeList.add(new ModelBox(addon, 25, 1, -1.5F, -31.0F, 1.6F, 5, 2, 4, -0.1F));

		inner_helmet = new ModelRenderer(this);
		inner_helmet.setRotationPoint(5.0F, -36.0F, 9.0F);
		addon.addChild(inner_helmet);
		setRotationAngle(inner_helmet, 0.0F, 0.0F, 0.1309F);
		inner_helmet.mirror = true;
		inner_helmet.cubeList.add(new ModelBox(inner_helmet, 25, 10, -0.2344F, -0.8546F, -1.0F, 1, 3, 3, 0.0F));
		inner_helmet.mirror = false;
		inner_helmet.cubeList.add(new ModelBox(inner_helmet, 39, 14, -0.2344F, -2.8546F, 1.0F, 1, 1, 1, 0.0F));
		inner_helmet.cubeList.add(new ModelBox(inner_helmet, 33, 13, -0.2344F, -1.8546F, 0.0F, 1, 1, 2, 0.0F));

		inner_helmet3 = new ModelRenderer(this);
		inner_helmet3.setRotationPoint(-3.0F, -36.0F, 9.0F);
		addon.addChild(inner_helmet3);
		setRotationAngle(inner_helmet3, 0.0F, 0.0F, -0.1309F);
		inner_helmet3.cubeList.add(new ModelBox(inner_helmet3, 25, 10, -0.7656F, -0.8546F, -1.0F, 1, 3, 3, 0.0F));
		inner_helmet3.mirror = true;
		inner_helmet3.cubeList.add(new ModelBox(inner_helmet3, 39, 14, -0.7656F, -2.8546F, 1.0F, 1, 1, 1, 0.0F));
		inner_helmet3.mirror = false;
		inner_helmet3.mirror = true;
		inner_helmet3.cubeList.add(new ModelBox(inner_helmet3, 33, 13, -0.7656F, -1.8546F, 0.0F, 1, 1, 2, 0.0F));
		inner_helmet3.mirror = false;

		addon2 = new ModelRenderer(this);
		addon2.setRotationPoint(1.0F, 24.0F, -10.0F);
		bipedHead.addChild(addon2);
		addon2.mirror = true;
		addon2.cubeList.add(new ModelBox(addon2, 2, 32, -5.0F, -35.5F, 5.5F, 8, 6, 8, 0.0F));
		addon2.mirror = false;

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