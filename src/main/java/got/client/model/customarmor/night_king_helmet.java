package got.client.model.customarmor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class night_king_helmet extends ModelBiped {
	public night_king_helmet() {
		super(0.0F);
		textureWidth = 64;
		textureHeight = 64;

		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 0, -4.5F, -6.5F, -4.5F, 9, 1, 9, 0.1F));
		bipedHead.cubeList.add(new ModelBox(bipedHead, 58, 0, -0.5F, -10.5F, -4.8F, 1, 5, 2, 0.1F));
		bipedHead.cubeList.add(new ModelBox(bipedHead, 58, 8, 1.5F, -9.5F, -4.6F, 1, 4, 2, 0.1F));
		bipedHead.cubeList.add(new ModelBox(bipedHead, 58, 8, -2.5F, -9.5F, -4.6F, 1, 4, 2, 0.1F));

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