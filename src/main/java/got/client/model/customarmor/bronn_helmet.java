package got.client.model.customarmor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class bronn_helmet extends ModelBiped {
	private final ModelRenderer cape_template;
	private final ModelRenderer armorCape9;
	private final ModelRenderer capeMid9;
	private final ModelRenderer capeLow;

	public bronn_helmet() {
		super(0.0F);
		textureWidth = 64;
		textureHeight = 64;

		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.55F));
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 0, -4.0F, -9.0F, -4.0F, 8, 2, 8, 0.0F));
		bipedHead.cubeList.add(new ModelBox(bipedHead, 32, 0, -4.0F, -7.5F, -4.0F, 8, 8, 8, 0.7F));

		bipedBody = new ModelRenderer(this);
		bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);

		cape_template = new ModelRenderer(this);
		cape_template.setRotationPoint(0.0F, 0.0F, 2.75F);
		bipedBody.addChild(cape_template);

		armorCape9 = new ModelRenderer(this);
		armorCape9.setRotationPoint(0.0F, -2.0F, 0.0F);
		cape_template.addChild(armorCape9);
		setRotationAngle(armorCape9, 0.0873F, 0.0F, 0.0F);
		armorCape9.cubeList.add(new ModelBox(armorCape9, 35, 37, -6.0F, 1.5F, -0.65F, 12, 12, 1, 0.0F));

		capeMid9 = new ModelRenderer(this);
		capeMid9.setRotationPoint(21.5F, 13.5F, 0.35F);
		armorCape9.addChild(capeMid9);
		setRotationAngle(capeMid9, 0.0873F, 0.0F, 0.0F);
		capeMid9.cubeList.add(new ModelBox(capeMid9, 35, 51, -27.5F, -0.1305F, -0.9914F, 12, 6, 1, -0.01F));

		capeLow = new ModelRenderer(this);
		capeLow.setRotationPoint(-21.0F, 6.0F, -1.0F);
		capeMid9.addChild(capeLow);
		setRotationAngle(capeLow, 0.1309F, 0.0F, 0.0F);
		capeLow.cubeList.add(new ModelBox(capeLow, 35, 59, -6.5F, -0.1271F, 0.0311F, 12, 4, 1, 0.0F));

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
