package got.client.model.arrynArmor;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class arryn_leggins extends ModelBase {
	private final ModelRenderer template;
	private final ModelRenderer right_leg;
	private final ModelRenderer right_skirt_r1;
	private final ModelRenderer left_leg;
	private final ModelRenderer left_skirt_r1;

	public arryn_leggins() {
		textureWidth = 32;
		textureHeight = 32;

		template = new ModelRenderer(this);
		template.setRotationPoint(8.0F, 24.0F, -8.0F);
		

		right_leg = new ModelRenderer(this);
		right_leg.setRotationPoint(-1.9F, -11.0F, 0.0F);
		template.addChild(right_leg);
		right_leg.cubeList.add(new ModelBox(right_leg, 0, 0, -10.1F, -1.0F, 6.0F, 4, 12, 4, 0.3F));

		right_skirt_r1 = new ModelRenderer(this);
		right_skirt_r1.setRotationPoint(-6.1F, -1.0F, 8.0F);
		right_leg.addChild(right_skirt_r1);
		setRotationAngle(right_skirt_r1, 0.0F, 0.0F, 0.0873F);
		right_skirt_r1.cubeList.add(new ModelBox(right_skirt_r1, 0, 16, -3.9F, 0.0F, -2.0F, 3, 7, 4, 0.8F));

		left_leg = new ModelRenderer(this);
		left_leg.setRotationPoint(1.9F, -11.0F, 0.0F);
		template.addChild(left_leg);
		left_leg.cubeList.add(new ModelBox(left_leg, 0, 0, -9.968F, -1.0F, 6.0F, 4, 12, 4, 0.3F));

		left_skirt_r1 = new ModelRenderer(this);
		left_skirt_r1.setRotationPoint(-9.9F, -1.0F, 8.0F);
		left_leg.addChild(left_skirt_r1);
		setRotationAngle(left_skirt_r1, 0.0F, 0.0F, -0.0873F);
		left_skirt_r1.cubeList.add(new ModelBox(left_skirt_r1, 0, 16, 0.9F, 0.0F, -2.0F, 3, 7, 4, 0.8F));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		template.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}