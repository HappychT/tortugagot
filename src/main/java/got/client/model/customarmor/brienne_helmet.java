package got.client.model.customarmor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class brienne_helmet extends ModelBiped {
	public brienne_helmet() {
		super(0.0F);
		textureWidth = 64;
		textureHeight = 64;

		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 0, -4.5F, -9.0F, -4.5F, 9, 9, 9, 0.2F));
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 18, -4.5F, -8.75F, -4.5F, 9, 9, 9, 0.3F));
		bipedHead.cubeList.add(new ModelBox(bipedHead, 27, 27, -4.5F, -9.0F, -4.5F, 9, 9, 9, -1.0F));
		bipedHead.cubeList.add(new ModelBox(bipedHead, 18, 51, -5.0F, -5.2F, -6.0F, 10, 1, 12, 0.0F));
		bipedHead.cubeList.add(new ModelBox(bipedHead, 40, 35, -0.5F, -9.25F, -5.5F, 1, 5, 11, 0.0F));

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

}