package got.client.model.customarmor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class arryn_helmet extends ModelBiped {
	private final ModelRenderer helmet_out_layer_r1;
	private final ModelRenderer helmet_out_layer_r2;
	private final ModelRenderer helmet_out_layer;

	public arryn_helmet() {
		super(0.0F);
		textureWidth = 64;
		textureHeight = 64;

		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 18, -4.5F, -9.0F, -4.5F, 9, 10, 9, 0.5F));
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 0, -4.5F, -8.75F, -4.5F, 9, 9, 9, 0.2F));

		helmet_out_layer_r1 = new ModelRenderer(this);
		helmet_out_layer_r1.setRotationPoint(-11.0F, 20.0F, -1.0F);
		bipedHead.addChild(helmet_out_layer_r1);
		setRotationAngle(helmet_out_layer_r1, 0.0F, -0.3927F, 0.0F);
		helmet_out_layer_r1.mirror = true;
		helmet_out_layer_r1.cubeList.add(new ModelBox(helmet_out_layer_r1, 40, 17, 5.1F, -28.0F, -4.5F, 0, 7, 8, 0.0F));
		helmet_out_layer_r1.mirror = false;

		helmet_out_layer_r2 = new ModelRenderer(this);
		helmet_out_layer_r2.setRotationPoint(11.0F, 20.0F, -1.0F);
		bipedHead.addChild(helmet_out_layer_r2);
		setRotationAngle(helmet_out_layer_r2, 0.0F, 0.3927F, 0.0F);
		helmet_out_layer_r2.cubeList.add(new ModelBox(helmet_out_layer_r2, 40, 17, -5.1F, -28.0F, -4.5F, 0, 7, 8, 0.0F));

		helmet_out_layer = new ModelRenderer(this);
		helmet_out_layer.setRotationPoint(4.0F, 4.0F, 1.0F);
		bipedHead.addChild(helmet_out_layer);
		helmet_out_layer.cubeList.add(new ModelBox(helmet_out_layer, 21, 31, -5.0F, -16.0F, -3.0F, 2, 3, 2, 0.0F));
		helmet_out_layer.cubeList.add(new ModelBox(helmet_out_layer, 19, 30, -5.0F, -14.0F, -1.0F, 2, 1, 2, 0.0F));
		helmet_out_layer.cubeList.add(new ModelBox(helmet_out_layer, 20, 31, -5.0F, -15.0F, -1.0F, 2, 1, 1, 0.0F));
		helmet_out_layer.cubeList.add(new ModelBox(helmet_out_layer, 19, 30, -5.0F, -16.0F, -1.0F, 2, 1, 2, 0.0F));
		helmet_out_layer.cubeList.add(new ModelBox(helmet_out_layer, 44, 37, -5.0F, -13.0F, 6.0F, 2, 3, 2, 0.0F));
		helmet_out_layer.cubeList.add(new ModelBox(helmet_out_layer, 44, 37, -5.0F, -11.0F, 7.0F, 2, 3, 2, 0.0F));
		helmet_out_layer.cubeList.add(new ModelBox(helmet_out_layer, 42, 35, -5.0F, -8.0F, 6.0F, 2, 1, 4, 0.0F));
		helmet_out_layer.cubeList.add(new ModelBox(helmet_out_layer, 43, 36, -5.0F, -9.0F, 5.0F, 2, 1, 3, 0.0F));
		helmet_out_layer.cubeList.add(new ModelBox(helmet_out_layer, 43, 36, -5.0F, -10.0F, 4.0F, 2, 1, 3, 0.0F));
		helmet_out_layer.cubeList.add(new ModelBox(helmet_out_layer, 44, 37, -5.0F, -19.0F, 5.0F, 2, 7, 2, 0.0F));
		helmet_out_layer.cubeList.add(new ModelBox(helmet_out_layer, 44, 37, -5.0F, -19.0F, 3.0F, 2, 7, 2, 0.0F));
		helmet_out_layer.cubeList.add(new ModelBox(helmet_out_layer, 45, 38, -5.0F, -18.0F, 1.0F, 2, 3, 1, 0.0F));
		helmet_out_layer.cubeList.add(new ModelBox(helmet_out_layer, 41, 34, -5.0F, -19.0F, -2.0F, 2, 3, 5, 0.0F));
		helmet_out_layer.cubeList.add(new ModelBox(helmet_out_layer, 39, 33, -5.0F, -20.0F, -1.0F, 2, 1, 7, 0.0F));
		helmet_out_layer.cubeList.add(new ModelBox(helmet_out_layer, 41, 34, -5.0F, -21.0F, 0.0F, 2, 1, 5, 0.0F));
		helmet_out_layer.cubeList.add(new ModelBox(helmet_out_layer, 3, 27, -2.99F, -21.0F, -3.0F, 0, 17, 13, 0.0F));
		helmet_out_layer.cubeList.add(new ModelBox(helmet_out_layer, 3, 27, -5.01F, -21.0F, -3.0F, 0, 17, 13, 0.0F));

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