package got.client.model.customarmor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class king_robberts_helmet extends ModelBiped {
	private final ModelRenderer helmet_out_layer_r1;
	private final ModelRenderer helmet_out_layer_r2;

	public king_robberts_helmet() {
		super(0.0F);
		textureWidth = 64;
		textureHeight = 64;

		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 0, -4.5F, -8.5F, -4.5F, 9, 9, 9, 0.25F));
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 0, -4.5F, -9.5F, -4.5F, 9, 1, 9, 0.0F));
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 18, -4.5F, -8.5F, -4.5F, 9, 9, 9, 0.4F));

		helmet_out_layer_r1 = new ModelRenderer(this);
		helmet_out_layer_r1.setRotationPoint(7.0F, -2.0F, -1.0F);
		bipedHead.addChild(helmet_out_layer_r1);
		setRotationAngle(helmet_out_layer_r1, 0.0F, -0.4363F, 0.0F);
		helmet_out_layer_r1.mirror = true;
		helmet_out_layer_r1.cubeList.add(new ModelBox(helmet_out_layer_r1, 47, 33, -4.5F, -11.5F, 0.0F, 7, 12, 1, 0.0F));
		helmet_out_layer_r1.mirror = false;

		helmet_out_layer_r2 = new ModelRenderer(this);
		helmet_out_layer_r2.setRotationPoint(-7.0F, -2.0F, -1.0F);
		bipedHead.addChild(helmet_out_layer_r2);
		setRotationAngle(helmet_out_layer_r2, 0.0F, 0.4363F, 0.0F);
		helmet_out_layer_r2.cubeList.add(new ModelBox(helmet_out_layer_r2, 47, 33, -2.5F, -11.5F, 0.0F, 7, 12, 1, 0.0F));

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