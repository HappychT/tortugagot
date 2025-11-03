package got.client.model.arrynArmor;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class arryn_boots extends ModelBase {
	private final ModelRenderer right_leg;
	private final ModelRenderer left_leg;

	public arryn_boots() {
		textureWidth = 16;
		textureHeight = 16;

		right_leg = new ModelRenderer(this);
		right_leg.setRotationPoint(6.1F, 13.0F, -8.0F);
		right_leg.cubeList.add(new ModelBox(right_leg, 0, 0, -10.0F, -1.0F, 6.0F, 4, 12, 4, 0.5F));

		left_leg = new ModelRenderer(this);
		left_leg.setRotationPoint(9.9F, 13.0F, -8.0F);
		left_leg.cubeList.add(new ModelBox(left_leg, 0, 0, -10.0F, -1.0F, 6.0F, 4, 12, 4, 0.5F));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		right_leg.render(f5);
		left_leg.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}