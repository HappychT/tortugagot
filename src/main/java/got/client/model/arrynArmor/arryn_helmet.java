package got.client.model.arrynArmor;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class arryn_helmet extends ModelBase {
	private final ModelRenderer helmet;

	public arryn_helmet() {
		textureWidth = 64;
		textureHeight = 64;

		helmet = new ModelRenderer(this);
		helmet.setRotationPoint(0.0F, 1.0F, 0.0F);
		helmet.cubeList.add(new ModelBox(helmet, 0, 0, -4.0F, -9.0F, -4.0F, 8, 8, 8, 0.3F));
		helmet.cubeList.add(new ModelBox(helmet, 0, 16, -4.0F, -9.0F, -4.0F, 8, 8, 8, 0.6F));
		helmet.cubeList.add(new ModelBox(helmet, 0, 31, -4.0F, -17.0F, -2.0F, 8, 12, 6, 0.8F));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		helmet.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}