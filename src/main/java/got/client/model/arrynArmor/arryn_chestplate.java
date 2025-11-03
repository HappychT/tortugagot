package got.client.model.arrynArmor;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class arryn_chestplate extends ModelBase {
	private final ModelRenderer template;
	private final ModelRenderer cuirass;
	private final ModelRenderer right_arm;
	private final ModelRenderer left_arm;

	public arryn_chestplate() {
		textureWidth = 64;
		textureHeight = 64;

		template = new ModelRenderer(this);
		template.setRotationPoint(8.0F, 24.0F, -8.0F);
		

		cuirass = new ModelRenderer(this);
		cuirass.setRotationPoint(0.0F, -23.0F, 0.0F);
		template.addChild(cuirass);
		cuirass.cubeList.add(new ModelBox(cuirass, 24, 48, -12.0F, -1.0F, 6.0F, 8, 12, 4, 0.5F));
		cuirass.cubeList.add(new ModelBox(cuirass, 0, 48, -12.0F, -1.0F, 6.0F, 8, 12, 4, 0.7F));
		cuirass.cubeList.add(new ModelBox(cuirass, 0, 32, -12.0F, -1.0F, 5.932F, 8, 12, 4, 0.2F));

		right_arm = new ModelRenderer(this);
		right_arm.setRotationPoint(-5.0F, -21.0F, 0.0F);
		template.addChild(right_arm);
		right_arm.cubeList.add(new ModelBox(right_arm, 48, 48, -11.0F, -3.0F, 6.0F, 4, 12, 4, 0.6F));
		right_arm.cubeList.add(new ModelBox(right_arm, 48, 20, -11.0F, -3.0F, 6.0F, 4, 12, 4, 0.3F));

		left_arm = new ModelRenderer(this);
		left_arm.setRotationPoint(5.0F, -21.0F, 0.0F);
		template.addChild(left_arm);
		left_arm.cubeList.add(new ModelBox(left_arm, 48, 48, -9.0F, -3.0F, 6.0F, 4, 12, 4, 0.6F));
		left_arm.cubeList.add(new ModelBox(left_arm, 48, 20, -9.0F, -3.0F, 6.0F, 4, 12, 4, 0.3F));
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