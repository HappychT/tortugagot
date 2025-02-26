package got.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class GOTModelBattleRam extends ModelBase {
    private final ModelRenderer Structure;
    private final ModelRenderer Structure_r1;
    private final ModelRenderer Structure_r2;
    private final ModelRenderer Structure_r3;
    private final ModelRenderer Structure_r4;
    private final ModelRenderer Structure_r5;
    private final ModelRenderer Structure_r6;
    private final ModelRenderer Ram;
    private final ModelRenderer Ram_r1;
    private final ModelRenderer Ram_r2;
    private final ModelRenderer Ram_r3;
    private final ModelRenderer Ram_r4;
    private final ModelRenderer RamHead;
    private final ModelRenderer RamHead_r1;
    private final ModelRenderer RamHead_r2;
    private final ModelRenderer RamHead_r3;
    private final ModelRenderer RamHead_r4;
    private final ModelRenderer RamHead_r5;
    private final ModelRenderer RamHead_r6;
    private final ModelRenderer RamHead_r7;
    private final ModelRenderer RamHead_r8;
    private final ModelRenderer RamHead_r9;
    private final ModelRenderer RamHead_r10;
    private final ModelRenderer RamHead_r11;
    private final ModelRenderer RamHead_r12;
    private final ModelRenderer RamHead_r13;
    private final ModelRenderer RamHead_r14;
    private final ModelRenderer RamHead_r15;
    private final ModelRenderer FirstDuoRopes;
    private final ModelRenderer Rope1;
    private final ModelRenderer Rope1_r1;
    private final ModelRenderer Rope4;
    private final ModelRenderer Rope4_r1;
    private final ModelRenderer SecondDuoRopes;
    private final ModelRenderer Rope2;
    private final ModelRenderer Rope2_r1;
    private final ModelRenderer Rope5;
    private final ModelRenderer Rope5_r1;
    private final ModelRenderer ThirdDuoRopes;
    private final ModelRenderer Rope3;
    private final ModelRenderer Rope3_r1;
    private final ModelRenderer Rope6;
    private final ModelRenderer Rope6_r1;
    private final ModelRenderer Wheel1;
    private final ModelRenderer Wheel2;
    private final ModelRenderer Wheel3;
    private final ModelRenderer Wheel4;
    private final ModelRenderer Wheel5;
    private final ModelRenderer Wheel6;

    public GOTModelBattleRam() {
        textureWidth = 512;
        textureHeight = 512;

        Structure = new ModelRenderer(this);
        Structure.setRotationPoint(-0.5163F, -12.9232F, 0.0167F);
        Structure.cubeList.add(new ModelBox(Structure, 44, 112, -23.9837F, 24.4232F, -37.4167F, 2, 2, 2, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 152, 0, 22.0163F, 24.4232F, -37.4167F, 2, 2, 2, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 40, 149, 22.0163F, 24.4232F, -1.0167F, 2, 2, 2, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 59, 112, 22.0163F, 24.4232F, 35.5833F, 2, 2, 2, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 32, 112, -23.9837F, 24.4232F, -1.0167F, 2, 2, 2, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 20, 112, -23.9837F, 24.4232F, 35.5833F, 2, 2, 2, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 168, 256, 17.5163F, 22.9232F, -40.0167F, 4, 5, 80, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 0, 41, -17.4837F, 23.4232F, -1.5167F, 35, 4, 3, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 0, 27, -17.4837F, 23.3232F, -38.0167F, 35, 4, 3, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 0, 34, -17.4837F, 23.4232F, 34.9833F, 35, 4, 3, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 0, 18, -17.4837F, -4.5768F, 32.9833F, 35, 4, 5, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 0, 9, -17.4837F, -4.5768F, -38.0167F, 35, 4, 5, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 0, 0, -17.4837F, -4.5768F, -2.5167F, 35, 4, 5, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 236, 0, 17.5163F, -5.0768F, -40.0167F, 4, 5, 80, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 172, 171, -21.4837F, -5.0768F, -40.0167F, 4, 5, 80, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 0, 164, -1.9837F, -22.0768F, -41.0167F, 4, 7, 82, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 157, 35, -1.9837F, -15.0768F, -37.5167F, 4, 11, 4, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 157, 20, -1.9837F, -15.0768F, 33.4833F, 4, 11, 4, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 62, 147, -1.9837F, -15.0768F, -2.0167F, 4, 11, 4, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 0, 253, -21.4837F, 22.9232F, -40.0167F, 4, 5, 80, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 141, 110, -20.9837F, -0.0768F, 33.9833F, 3, 23, 5, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 141, 82, 18.0163F, -0.0768F, 33.9833F, 3, 23, 5, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 141, 0, 18.0163F, -0.0768F, -2.5167F, 3, 23, 5, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 141, 28, -20.9837F, -0.0768F, -2.5167F, 3, 23, 5, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 48, 112, 18.0163F, -0.0768F, -38.9167F, 3, 23, 5, 0.0F));
        Structure.cubeList.add(new ModelBox(Structure, 64, 119, -20.9837F, -0.0768F, -38.9167F, 3, 23, 5, 0.0F));

        Structure_r1 = new ModelRenderer(this);
        Structure_r1.setRotationPoint(-16.4837F, 52.3232F, -35.5167F);
        Structure.addChild(Structure_r1);
        setRotationAngle(Structure_r1, 0.0F, 0.0F, -0.7854F);
        Structure_r1.cubeList.add(new ModelBox(Structure_r1, 68, 63, 55.8F, -19.5F, -1.0F, 3, 13, 2, 0.0F));
        Structure_r1.cubeList.add(new ModelBox(Structure_r1, 68, 48, 55.8F, -19.5F, 34.5F, 3, 13, 2, 0.0F));
        Structure_r1.cubeList.add(new ModelBox(Structure_r1, 68, 48, 55.8F, -19.5F, 70.0F, 3, 13, 2, 0.0F));

        Structure_r2 = new ModelRenderer(this);
        Structure_r2.setRotationPoint(-16.4837F, 11.4232F, -35.5167F);
        Structure.addChild(Structure_r2);
        setRotationAngle(Structure_r2, 0.0F, 0.0F, 0.7854F);
        Structure_r2.cubeList.add(new ModelBox(Structure_r2, 0, 164, -6.5F, -13.5F, 70.0F, 3, 13, 2, 0.0F));
        Structure_r2.cubeList.add(new ModelBox(Structure_r2, 10, 164, -6.5F, -13.5F, 34.5F, 3, 13, 2, 0.0F));
        Structure_r2.cubeList.add(new ModelBox(Structure_r2, 20, 164, -6.5F, -13.5F, -1.0F, 3, 13, 2, 0.0F));

        Structure_r3 = new ModelRenderer(this);
        Structure_r3.setRotationPoint(2.0163F, 4.873F, -37.4522F);
        Structure.addChild(Structure_r3);
        setRotationAngle(Structure_r3, 0.7854F, 0.0F, 0.0F);
        Structure_r3.cubeList.add(new ModelBox(Structure_r3, 68, 82, -22.5F, -1.0F, 2.0F, 2, 33, 4, 0.0F));
        Structure_r3.cubeList.add(new ModelBox(Structure_r3, 0, 112, 16.5F, -1.0F, 2.0F, 2, 33, 4, 0.0F));

        Structure_r4 = new ModelRenderer(this);
        Structure_r4.setRotationPoint(1.5163F, 21.1364F, 9.9417F);
        Structure.addChild(Structure_r4);
        setRotationAngle(Structure_r4, -0.7854F, 0.0F, 0.0F);
        Structure_r4.cubeList.add(new ModelBox(Structure_r4, 24, 112, -22.0F, -32.0F, 2.0F, 2, 33, 4, 0.0F));
        Structure_r4.cubeList.add(new ModelBox(Structure_r4, 36, 112, 17.0F, -32.0F, 2.0F, 2, 33, 4, 0.0F));

        Structure_r5 = new ModelRenderer(this);
        Structure_r5.setRotationPoint(-0.4837F, -6.5768F, -18.0167F);
        Structure.addChild(Structure_r5);
        setRotationAngle(Structure_r5, 0.0F, 0.0F, -0.5236F);
        Structure_r5.cubeList.add(new ModelBox(Structure_r5, 0, 54, -27.5F, -12.0F, -1.0F, 32, 4, 2, 0.0F));
        Structure_r5.cubeList.add(new ModelBox(Structure_r5, 0, 66, -27.5F, -12.0F, 35.0F, 32, 4, 2, 0.0F));
        Structure_r5.cubeList.add(new ModelBox(Structure_r5, 0, 82, -25.5F, -11.5F, -22.5F, 30, 1, 81, 0.0F));
        Structure_r5.cubeList.add(new ModelBox(Structure_r5, 0, 82, -27.5F, -12.0F, 52.5F, 32, 4, 2, 0.0F));
        Structure_r5.cubeList.add(new ModelBox(Structure_r5, 0, 94, -27.5F, -12.0F, -18.5F, 32, 4, 2, 0.0F));
        Structure_r5.cubeList.add(new ModelBox(Structure_r5, 0, 106, -27.5F, -12.0F, 17.0F, 32, 4, 2, 0.0F));

        Structure_r6 = new ModelRenderer(this);
        Structure_r6.setRotationPoint(0.5163F, -6.5768F, -18.0167F);
        Structure.addChild(Structure_r6);
        setRotationAngle(Structure_r6, 0.0F, 0.0F, 0.5236F);
        Structure_r6.cubeList.add(new ModelBox(Structure_r6, 0, 48, -4.5F, -12.0F, -1.0F, 32, 4, 2, 0.0F));
        Structure_r6.cubeList.add(new ModelBox(Structure_r6, 0, 60, -4.5F, -12.0F, 35.0F, 32, 4, 2, 0.0F));
        Structure_r6.cubeList.add(new ModelBox(Structure_r6, 0, 0, -4.5F, -11.5F, -22.5F, 30, 1, 81, 0.0F));
        Structure_r6.cubeList.add(new ModelBox(Structure_r6, 0, 72, -4.5F, -12.0F, 52.5F, 32, 4, 2, 0.0F));
        Structure_r6.cubeList.add(new ModelBox(Structure_r6, 0, 88, -4.5F, -12.0F, -18.5F, 32, 4, 2, 0.0F));
        Structure_r6.cubeList.add(new ModelBox(Structure_r6, 0, 100, -4.5F, -12.0F, 17.0F, 32, 4, 2, 0.0F));

        Ram = new ModelRenderer(this);
        Ram.setRotationPoint(-0.4733F, -12.9232F, 1.2587F);
        Ram.cubeList.add(new ModelBox(Ram, 137, 79, -3.5267F, 12.7232F, -46.0587F, 7, 7, 85, 0.0F));
        Ram.cubeList.add(new ModelBox(Ram, 157, 92, -4.0267F, 12.2232F, -32.5587F, 8, 8, 2, 0.0F));
        Ram.cubeList.add(new ModelBox(Ram, 0, 149, -4.0267F, 12.2232F, -45.0587F, 8, 8, 2, 0.0F));
        Ram.cubeList.add(new ModelBox(Ram, 157, 82, -4.0267F, 12.2232F, 5.4413F, 8, 8, 2, 0.0F));
        Ram.cubeList.add(new ModelBox(Ram, 20, 149, -4.0267F, 12.2232F, -14.5587F, 8, 8, 2, 0.0F));
        Ram.cubeList.add(new ModelBox(Ram, 155, 153, -4.0267F, 12.2232F, 22.4413F, 8, 8, 2, 0.0F));
        Ram.cubeList.add(new ModelBox(Ram, 155, 71, -4.0267F, 12.2232F, 33.9413F, 8, 8, 2, 0.0F));
        Ram.cubeList.add(new ModelBox(Ram, 64, 116, -5.8052F, 13.8791F, 22.9403F, 1, 5, 1, 0.0F));
        Ram.cubeList.add(new ModelBox(Ram, 171, 124, -5.8052F, 13.8791F, 5.9403F, 1, 5, 1, 0.0F));
        Ram.cubeList.add(new ModelBox(Ram, 164, 132, -5.9052F, 13.8791F, -14.0597F, 1, 5, 1, 0.0F));
        Ram.cubeList.add(new ModelBox(Ram, 179, 26, -5.9052F, 13.8791F, -32.0597F, 1, 5, 1, 0.0F));
        Ram.cubeList.add(new ModelBox(Ram, 179, 32, 4.9045F, 13.8792F, -32.0597F, 1, 5, 1, 0.0F));
        Ram.cubeList.add(new ModelBox(Ram, 164, 50, 4.9045F, 13.8792F, -14.0597F, 1, 5, 1, 0.0F));
        Ram.cubeList.add(new ModelBox(Ram, 30, 179, 4.9045F, 13.8792F, 5.9423F, 1, 5, 1, 0.0F));
        Ram.cubeList.add(new ModelBox(Ram, 44, 153, 4.9045F, 13.8792F, 22.9423F, 1, 5, 1, 0.0F));
        Ram.cubeList.add(new ModelBox(Ram, 48, 140, -0.5267F, 13.2232F, 38.9413F, 1, 2, 1, 0.0F));
        Ram.cubeList.add(new ModelBox(Ram, 0, 0, -0.5267F, 14.2232F, 39.9413F, 1, 4, 1, 0.0F));
        Ram.cubeList.add(new ModelBox(Ram, 141, 0, -0.5267F, 17.2232F, 38.9413F, 1, 2, 1, 0.0F));

        Ram_r1 = new ModelRenderer(this);
        Ram_r1.setRotationPoint(4.4733F, 17.4232F, 23.4413F);
        Ram.addChild(Ram_r1);
        setRotationAngle(Ram_r1, 0.0F, 0.0F, -0.3927F);
        Ram_r1.cubeList.add(new ModelBox(Ram_r1, 24, 78, -1.8F, 0.6F, -0.5F, 2, 1, 1, 0.0F));
        Ram_r1.cubeList.add(new ModelBox(Ram_r1, 66, 82, -1.8F, 0.6F, -17.5F, 2, 1, 1, 0.0F));
        Ram_r1.cubeList.add(new ModelBox(Ram_r1, 30, 78, -1.8F, 0.6F, -37.5F, 2, 1, 1, 0.0F));
        Ram_r1.cubeList.add(new ModelBox(Ram_r1, 28, 159, -1.8F, 0.6F, -55.5F, 2, 1, 1, 0.0F));

        Ram_r2 = new ModelRenderer(this);
        Ram_r2.setRotationPoint(4.4733F, 17.4232F, 23.4413F);
        Ram.addChild(Ram_r2);
        setRotationAngle(Ram_r2, 0.0F, 0.0F, 0.3927F);
        Ram_r2.cubeList.add(new ModelBox(Ram_r2, 18, 78, -2.55F, -3.5F, -0.5F, 2, 1, 1, 0.0F));
        Ram_r2.cubeList.add(new ModelBox(Ram_r2, 152, 82, -2.55F, -3.5F, -17.5F, 2, 1, 1, 0.0F));
        Ram_r2.cubeList.add(new ModelBox(Ram_r2, 36, 78, -2.55F, -3.5F, -37.5F, 2, 1, 1, 0.0F));
        Ram_r2.cubeList.add(new ModelBox(Ram_r2, 22, 159, -2.55F, -3.5F, -55.5F, 2, 1, 1, 0.0F));

        Ram_r3 = new ModelRenderer(this);
        Ram_r3.setRotationPoint(-5.1364F, 17.452F, -31.5587F);
        Ram.addChild(Ram_r3);
        setRotationAngle(Ram_r3, 0.0F, 0.0F, 0.3927F);
        Ram_r3.cubeList.add(new ModelBox(Ram_r3, 72, 78, 0.4F, 0.3F, -0.5F, 2, 1, 1, 0.0F));
        Ram_r3.cubeList.add(new ModelBox(Ram_r3, 42, 78, 0.4F, 0.3F, 17.5F, 2, 1, 1, 0.0F));
        Ram_r3.cubeList.add(new ModelBox(Ram_r3, 54, 78, 0.4F, 0.3F, 37.5F, 2, 1, 1, 0.0F));
        Ram_r3.cubeList.add(new ModelBox(Ram_r3, 12, 78, 0.4F, 0.3F, 54.5F, 2, 1, 1, 0.0F));

        Ram_r4 = new ModelRenderer(this);
        Ram_r4.setRotationPoint(-5.4204F, 14.0872F, -31.5587F);
        Ram.addChild(Ram_r4);
        setRotationAngle(Ram_r4, 0.0F, 0.0F, -0.3927F);
        Ram_r4.cubeList.add(new ModelBox(Ram_r4, 66, 78, 0.1437F, -0.014F, -0.5F, 2, 1, 1, 0.0F));
        Ram_r4.cubeList.add(new ModelBox(Ram_r4, 48, 78, 0.1437F, -0.014F, 17.5F, 2, 1, 1, 0.0F));
        Ram_r4.cubeList.add(new ModelBox(Ram_r4, 60, 78, 0.1437F, -0.014F, 37.5F, 2, 1, 1, 0.0F));
        Ram_r4.cubeList.add(new ModelBox(Ram_r4, 6, 78, 0.1437F, -0.014F, 54.5F, 2, 1, 1, 0.0F));

        RamHead = new ModelRenderer(this);
        RamHead.setRotationPoint(-0.0267F, 16.2232F, -46.5587F);
        Ram.addChild(RamHead);
        RamHead.cubeList.add(new ModelBox(RamHead, 167, 102, -2.5F, -2.5F, -1.5F, 5, 5, 2, 0.0F));
        RamHead.cubeList.add(new ModelBox(RamHead, 157, 115, -2.0F, -2.9F, -4.5F, 4, 4, 5, 0.0F));
        RamHead.cubeList.add(new ModelBox(RamHead, 153, 106, -2.5F, -4.0F, -8.5F, 5, 5, 4, 0.0F));
        RamHead.cubeList.add(new ModelBox(RamHead, 18, 159, -3.0F, -4.2802F, -8.4769F, 1, 2, 2, 0.0F));
        RamHead.cubeList.add(new ModelBox(RamHead, 75, 0, 2.0F, -4.2802F, -8.4769F, 1, 2, 2, 0.0F));

        RamHead_r1 = new ModelRenderer(this);
        RamHead_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        RamHead.addChild(RamHead_r1);
        setRotationAngle(RamHead_r1, 0.0F, 0.0F, 0.7854F);
        RamHead_r1.cubeList.add(new ModelBox(RamHead_r1, 0, 78, -2.2F, -2.2F, -3.5F, 2, 2, 1, 0.0F));
        RamHead_r1.cubeList.add(new ModelBox(RamHead_r1, 8, 112, -2.4F, -2.4F, -2.5F, 3, 3, 1, 0.0F));
        RamHead_r1.cubeList.add(new ModelBox(RamHead_r1, 141, 76, -2.7F, -2.7F, -1.5F, 4, 4, 1, 0.0F));
        RamHead_r1.cubeList.add(new ModelBox(RamHead_r1, 169, 132, -3.0F, -3.0F, -0.5F, 6, 6, 1, 0.0F));

        RamHead_r2 = new ModelRenderer(this);
        RamHead_r2.setRotationPoint(0.0F, 0.6F, -2.0F);
        RamHead.addChild(RamHead_r2);
        setRotationAngle(RamHead_r2, -0.3927F, 0.0F, 0.0F);
        RamHead_r2.cubeList.add(new ModelBox(RamHead_r2, 35, 155, -1.0F, 0.1F, -2.5F, 2, 1, 5, 0.0F));

        RamHead_r3 = new ModelRenderer(this);
        RamHead_r3.setRotationPoint(3.2833F, -1.2738F, -6.0944F);
        RamHead.addChild(RamHead_r3);
        setRotationAngle(RamHead_r3, 0.6981F, 0.0F, 0.0F);
        RamHead_r3.cubeList.add(new ModelBox(RamHead_r3, 75, 9, -0.9833F, -3.8217F, 0.2632F, 1, 2, 2, 0.0F));

        RamHead_r4 = new ModelRenderer(this);
        RamHead_r4.setRotationPoint(3.4F, -4.2873F, -5.834F);
        RamHead.addChild(RamHead_r4);
        setRotationAngle(RamHead_r4, 1.3963F, 0.0F, 0.0F);
        RamHead_r4.cubeList.add(new ModelBox(RamHead_r4, 75, 18, -0.6F, -1.1F, -1.0F, 1, 2, 2, 0.0F));

        RamHead_r5 = new ModelRenderer(this);
        RamHead_r5.setRotationPoint(3.5F, -0.7864F, -5.6095F);
        RamHead.addChild(RamHead_r5);
        setRotationAngle(RamHead_r5, 0.5672F, 0.0F, 0.0F);
        RamHead_r5.cubeList.add(new ModelBox(RamHead_r5, 75, 119, -0.5F, -3.0F, 1.0F, 1, 2, 2, 0.0F));

        RamHead_r6 = new ModelRenderer(this);
        RamHead_r6.setRotationPoint(3.5F, -3.5478F, -4.9973F);
        RamHead.addChild(RamHead_r6);
        setRotationAngle(RamHead_r6, -0.1658F, 0.0F, 0.0F);
        RamHead_r6.cubeList.add(new ModelBox(RamHead_r6, 57, 140, -0.3F, 0.0F, -0.7F, 1, 2, 2, 0.0F));

        RamHead_r7 = new ModelRenderer(this);
        RamHead_r7.setRotationPoint(3.7F, -2.512F, -4.8665F);
        RamHead.addChild(RamHead_r7);
        setRotationAngle(RamHead_r7, -0.5149F, 0.0F, 0.0F);
        RamHead_r7.cubeList.add(new ModelBox(RamHead_r7, 157, 102, -0.6F, 0.5F, -0.8F, 1, 2, 2, 0.0F));

        RamHead_r8 = new ModelRenderer(this);
        RamHead_r8.setRotationPoint(3.65F, 0.2219F, -5.6784F);
        RamHead.addChild(RamHead_r8);
        setRotationAngle(RamHead_r8, -0.8901F, 0.0F, 0.0F);
        RamHead_r8.cubeList.add(new ModelBox(RamHead_r8, 0, 9, -0.45F, -1.4F, -0.85F, 1, 2, 1, 0.0F));
        RamHead_r8.cubeList.add(new ModelBox(RamHead_r8, 0, 18, -0.65F, -0.6F, -1.25F, 1, 2, 1, 0.0F));

        RamHead_r9 = new ModelRenderer(this);
        RamHead_r9.setRotationPoint(-3.5409F, -3.1927F, 4.034F);
        RamHead.addChild(RamHead_r9);
        setRotationAngle(RamHead_r9, -0.5149F, 0.0F, 0.0F);
        RamHead_r9.cubeList.add(new ModelBox(RamHead_r9, 141, 158, -0.5591F, 5.4753F, -8.2114F, 1, 2, 2, 0.0F));

        RamHead_r10 = new ModelRenderer(this);
        RamHead_r10.setRotationPoint(-3.5409F, -3.1927F, 4.034F);
        RamHead.addChild(RamHead_r10);
        setRotationAngle(RamHead_r10, -0.1658F, 0.0F, 0.0F);
        RamHead_r10.cubeList.add(new ModelBox(RamHead_r10, 147, 158, -0.6591F, 1.1404F, -9.6661F, 1, 2, 2, 0.0F));

        RamHead_r11 = new ModelRenderer(this);
        RamHead_r11.setRotationPoint(-3.5409F, -3.1927F, 4.034F);
        RamHead.addChild(RamHead_r11);
        setRotationAngle(RamHead_r11, -0.8901F, 0.0F, 0.0F);
        RamHead_r11.cubeList.add(new ModelBox(RamHead_r11, 76, 82, -0.4591F, 9.0968F, -4.7086F, 1, 2, 1, 0.0F));
        RamHead_r11.cubeList.add(new ModelBox(RamHead_r11, 0, 112, -0.6591F, 8.2968F, -4.3086F, 1, 2, 1, 0.0F));

        RamHead_r12 = new ModelRenderer(this);
        RamHead_r12.setRotationPoint(-3.5409F, -3.1927F, 4.034F);
        RamHead.addChild(RamHead_r12);
        setRotationAngle(RamHead_r12, 0.5672F, 0.0F, 0.0F);
        RamHead_r12.cubeList.add(new ModelBox(RamHead_r12, 0, 159, -0.4591F, -6.152F, -8.4262F, 1, 2, 2, 0.0F));

        RamHead_r13 = new ModelRenderer(this);
        RamHead_r13.setRotationPoint(-3.5409F, -3.1927F, 4.034F);
        RamHead.addChild(RamHead_r13);
        setRotationAngle(RamHead_r13, 1.3963F, 0.0F, 0.0F);
        RamHead_r13.cubeList.add(new ModelBox(RamHead_r13, 6, 159, -0.2591F, -11.0082F, -1.6356F, 1, 2, 2, 0.0F));

        RamHead_r14 = new ModelRenderer(this);
        RamHead_r14.setRotationPoint(-3.5409F, -3.1927F, 4.034F);
        RamHead.addChild(RamHead_r14);
        setRotationAngle(RamHead_r14, 0.6981F, 0.0F, 0.0F);
        RamHead_r14.cubeList.add(new ModelBox(RamHead_r14, 12, 159, 0.2409F, -8.8621F, -8.7291F, 1, 2, 2, 0.0F));

        RamHead_r15 = new ModelRenderer(this);
        RamHead_r15.setRotationPoint(-1.5F, 1.5142F, -6.0535F);
        RamHead.addChild(RamHead_r15);
        setRotationAngle(RamHead_r15, 0.4363F, 0.0F, 0.0F);
        RamHead_r15.cubeList.add(new ModelBox(RamHead_r15, 157, 124, 0.0F, -1.5F, -2.0F, 3, 4, 4, 0.0F));

        FirstDuoRopes = new ModelRenderer(this);
        FirstDuoRopes.setRotationPoint(-0.4F, -12.9232F, -35.0F);


        Rope1 = new ModelRenderer(this);
        Rope1.setRotationPoint(-11.6502F, 9.3019F, 34.0F);
        FirstDuoRopes.addChild(Rope1);


        Rope1_r1 = new ModelRenderer(this);
        Rope1_r1.setRotationPoint(5.933F, -3.616F, -34.3F);
        Rope1.addChild(Rope1_r1);
        setRotationAngle(Rope1_r1, 0.0F, 0.0F, -0.3927F);
        Rope1_r1.cubeList.add(new ModelBox(Rope1_r1, 76, 27, -0.5F, -8.0F, 0.0F, 1, 17, 0, 0.0F));

        Rope4 = new ModelRenderer(this);
        Rope4.setRotationPoint(0.4F, 31.9232F, 35.0F);
        FirstDuoRopes.addChild(Rope4);


        Rope4_r1 = new ModelRenderer(this);
        Rope4_r1.setRotationPoint(5.204F, -25.4162F, -35.5F);
        Rope4.addChild(Rope4_r1);
        setRotationAngle(Rope4_r1, 0.0F, 0.0F, 0.3927F);
        Rope4_r1.cubeList.add(new ModelBox(Rope4_r1, 78, 61, -0.5F, -8.0F, 0.0F, 1, 16, 0, 0.0F));

        SecondDuoRopes = new ModelRenderer(this);
        SecondDuoRopes.setRotationPoint(-0.4F, -12.9232F, 0.0F);


        Rope2 = new ModelRenderer(this);
        Rope2.setRotationPoint(-11.6502F, 9.3019F, -1.0F);
        SecondDuoRopes.addChild(Rope2);


        Rope2_r1 = new ModelRenderer(this);
        Rope2_r1.setRotationPoint(5.933F, -3.616F, 1.2F);
        Rope2.addChild(Rope2_r1);
        setRotationAngle(Rope2_r1, 0.0F, 0.0F, -0.3927F);
        Rope2_r1.cubeList.add(new ModelBox(Rope2_r1, 78, 27, -0.5F, -8.0F, 0.0F, 1, 17, 0, 0.0F));

        Rope5 = new ModelRenderer(this);
        Rope5.setRotationPoint(0.4F, 31.9232F, 0.0F);
        SecondDuoRopes.addChild(Rope5);


        Rope5_r1 = new ModelRenderer(this);
        Rope5_r1.setRotationPoint(5.204F, -25.4162F, -0.2F);
        Rope5.addChild(Rope5_r1);
        setRotationAngle(Rope5_r1, 0.0F, 0.0F, 0.3927F);
        Rope5_r1.cubeList.add(new ModelBox(Rope5_r1, 78, 147, -0.5F, -8.0F, 0.0F, 1, 16, 0, 0.0F));

        ThirdDuoRopes = new ModelRenderer(this);
        ThirdDuoRopes.setRotationPoint(-0.4F, -12.9232F, 35.0F);


        Rope3 = new ModelRenderer(this);
        Rope3.setRotationPoint(-6.6502F, 6.3019F, 35.0F);
        ThirdDuoRopes.addChild(Rope3);
        setRotationAngle(Rope3, 0.0F, 0.0F, 0.6981F);


        Rope3_r1 = new ModelRenderer(this);
        Rope3_r1.setRotationPoint(6.6303F, 3.22F, -114.466F);
        Rope3.addChild(Rope3_r1);
        setRotationAngle(Rope3_r1, 0.0F, 0.0F, -1.0472F);
        Rope3_r1.cubeList.add(new ModelBox(Rope3_r1, 78, 44, -0.0265F, -14.852F, 79.966F, 1, 17, 0, 0.0F));

        Rope6 = new ModelRenderer(this);
        Rope6.setRotationPoint(0.4F, 31.9232F, -35.0F);
        ThirdDuoRopes.addChild(Rope6);


        Rope6_r1 = new ModelRenderer(this);
        Rope6_r1.setRotationPoint(5.3703F, -25.9096F, 35.3F);
        Rope6.addChild(Rope6_r1);
        setRotationAngle(Rope6_r1, 0.0F, 0.0F, 0.3927F);
        Rope6_r1.cubeList.add(new ModelBox(Rope6_r1, 78, 164, -0.5F, -8.0F, 0.0F, 1, 16, 0, 0.0F));

        Wheel1 = new ModelRenderer(this);
        Wheel1.setRotationPoint(22.0F, -12.9232F, 36.6F);
        Wheel1.cubeList.add(new ModelBox(Wheel1, 169, 65, -1.0F, 22.9232F, -7.5F, 2, 5, 1, 0.0F));
        Wheel1.cubeList.add(new ModelBox(Wheel1, 171, 109, -1.0F, 20.9232F, -6.5F, 2, 9, 1, 0.0F));
        Wheel1.cubeList.add(new ModelBox(Wheel1, 130, 164, -1.0F, 19.9232F, -5.5F, 2, 11, 1, 0.0F));
        Wheel1.cubeList.add(new ModelBox(Wheel1, 30, 164, -1.0F, 18.9232F, -4.5F, 2, 13, 2, 0.0F));
        Wheel1.cubeList.add(new ModelBox(Wheel1, 48, 140, -1.0F, 17.9232F, -2.5F, 2, 15, 5, 0.0F));
        Wheel1.cubeList.add(new ModelBox(Wheel1, 38, 164, -1.0F, 18.9232F, 2.5F, 2, 13, 2, 0.0F));
        Wheel1.cubeList.add(new ModelBox(Wheel1, 169, 139, -1.0F, 19.9232F, 4.5F, 2, 11, 1, 0.0F));
        Wheel1.cubeList.add(new ModelBox(Wheel1, 173, 36, -1.0F, 20.9232F, 5.5F, 2, 9, 1, 0.0F));
        Wheel1.cubeList.add(new ModelBox(Wheel1, 175, 75, -1.0F, 22.9232F, 6.5F, 2, 5, 1, 0.0F));

        Wheel2 = new ModelRenderer(this);
        Wheel2.setRotationPoint(22.0F, -12.9232F, 0.0F);
        Wheel2.cubeList.add(new ModelBox(Wheel2, 177, 56, -1.0F, 22.9232F, -7.5F, 2, 5, 1, 0.0F));
        Wheel2.cubeList.add(new ModelBox(Wheel2, 175, 65, -1.0F, 20.9232F, -6.5F, 2, 9, 1, 0.0F));
        Wheel2.cubeList.add(new ModelBox(Wheel2, 171, 0, -1.0F, 19.9232F, -5.5F, 2, 11, 1, 0.0F));
        Wheel2.cubeList.add(new ModelBox(Wheel2, 46, 164, -1.0F, 18.9232F, -4.5F, 2, 13, 2, 0.0F));
        Wheel2.cubeList.add(new ModelBox(Wheel2, 141, 56, -1.0F, 17.9232F, -2.5F, 2, 15, 5, 0.0F));
        Wheel2.cubeList.add(new ModelBox(Wheel2, 54, 164, -1.0F, 18.9232F, 2.5F, 2, 13, 2, 0.0F));
        Wheel2.cubeList.add(new ModelBox(Wheel2, 171, 12, -1.0F, 19.9232F, 4.5F, 2, 11, 1, 0.0F));
        Wheel2.cubeList.add(new ModelBox(Wheel2, 175, 119, -1.0F, 20.9232F, 5.5F, 2, 9, 1, 0.0F));
        Wheel2.cubeList.add(new ModelBox(Wheel2, 177, 109, -1.0F, 22.9232F, 6.5F, 2, 5, 1, 0.0F));

        Wheel3 = new ModelRenderer(this);
        Wheel3.setRotationPoint(22.0F, -12.9232F, -36.4F);
        Wheel3.cubeList.add(new ModelBox(Wheel3, 178, 171, -1.0F, 22.9232F, 6.5F, 2, 5, 1, 0.0F));
        Wheel3.cubeList.add(new ModelBox(Wheel3, 175, 139, -1.0F, 20.9232F, 5.5F, 2, 9, 1, 0.0F));
        Wheel3.cubeList.add(new ModelBox(Wheel3, 136, 171, -1.0F, 19.9232F, 4.5F, 2, 11, 1, 0.0F));
        Wheel3.cubeList.add(new ModelBox(Wheel3, 62, 164, -1.0F, 18.9232F, 2.5F, 2, 13, 2, 0.0F));
        Wheel3.cubeList.add(new ModelBox(Wheel3, 141, 138, -1.0F, 17.9232F, -2.5F, 2, 15, 5, 0.0F));
        Wheel3.cubeList.add(new ModelBox(Wheel3, 70, 164, -1.0F, 18.9232F, -4.5F, 2, 13, 2, 0.0F));
        Wheel3.cubeList.add(new ModelBox(Wheel3, 142, 171, -1.0F, 19.9232F, -5.5F, 2, 11, 1, 0.0F));
        Wheel3.cubeList.add(new ModelBox(Wheel3, 175, 149, -1.0F, 20.9232F, -6.5F, 2, 9, 1, 0.0F));
        Wheel3.cubeList.add(new ModelBox(Wheel3, 178, 177, -1.0F, 22.9232F, -7.5F, 2, 5, 1, 0.0F));

        Wheel4 = new ModelRenderer(this);
        Wheel4.setRotationPoint(-23.0F, -12.9232F, 36.6F);
        Wheel4.cubeList.add(new ModelBox(Wheel4, 154, 171, -1.0F, 19.9232F, 4.5F, 2, 11, 1, 0.0F));
        Wheel4.cubeList.add(new ModelBox(Wheel4, 177, 0, -1.0F, 20.9232F, 5.5F, 2, 9, 1, 0.0F));
        Wheel4.cubeList.add(new ModelBox(Wheel4, 98, 164, -1.0F, 18.9232F, 2.5F, 2, 13, 2, 0.0F));
        Wheel4.cubeList.add(new ModelBox(Wheel4, 155, 51, -1.0F, 17.9232F, -2.5F, 2, 15, 5, 0.0F));
        Wheel4.cubeList.add(new ModelBox(Wheel4, 90, 164, -1.0F, 18.9232F, -4.5F, 2, 13, 2, 0.0F));
        Wheel4.cubeList.add(new ModelBox(Wheel4, 148, 171, -1.0F, 19.9232F, -5.5F, 2, 11, 1, 0.0F));
        Wheel4.cubeList.add(new ModelBox(Wheel4, 130, 176, -1.0F, 20.9232F, -6.5F, 2, 9, 1, 0.0F));
        Wheel4.cubeList.add(new ModelBox(Wheel4, 0, 179, -1.0F, 22.9232F, -7.5F, 2, 5, 1, 0.0F));
        Wheel4.cubeList.add(new ModelBox(Wheel4, 6, 179, -1.0F, 22.9232F, 6.5F, 2, 5, 1, 0.0F));

        Wheel5 = new ModelRenderer(this);
        Wheel5.setRotationPoint(-23.0F, -12.9232F, 0.0F);
        Wheel5.cubeList.add(new ModelBox(Wheel5, 24, 179, -1.0F, 22.9232F, 6.5F, 2, 5, 1, 0.0F));
        Wheel5.cubeList.add(new ModelBox(Wheel5, 173, 24, -1.0F, 19.9232F, 4.5F, 2, 11, 1, 0.0F));
        Wheel5.cubeList.add(new ModelBox(Wheel5, 177, 82, -1.0F, 20.9232F, 5.5F, 2, 9, 1, 0.0F));
        Wheel5.cubeList.add(new ModelBox(Wheel5, 122, 164, -1.0F, 18.9232F, 2.5F, 2, 13, 2, 0.0F));
        Wheel5.cubeList.add(new ModelBox(Wheel5, 157, 0, -1.0F, 17.9232F, -2.5F, 2, 15, 5, 0.0F));
        Wheel5.cubeList.add(new ModelBox(Wheel5, 169, 50, -1.0F, 18.9232F, -4.5F, 2, 13, 2, 0.0F));
        Wheel5.cubeList.add(new ModelBox(Wheel5, 172, 171, -1.0F, 19.9232F, -5.5F, 2, 11, 1, 0.0F));
        Wheel5.cubeList.add(new ModelBox(Wheel5, 177, 92, -1.0F, 20.9232F, -6.5F, 2, 9, 1, 0.0F));
        Wheel5.cubeList.add(new ModelBox(Wheel5, 179, 20, -1.0F, 22.9232F, -7.5F, 2, 5, 1, 0.0F));

        Wheel6 = new ModelRenderer(this);
        Wheel6.setRotationPoint(-23.0F, -12.9232F, -36.4F);
        Wheel6.cubeList.add(new ModelBox(Wheel6, 18, 179, -1.0F, 22.9232F, 6.5F, 2, 5, 1, 0.0F));
        Wheel6.cubeList.add(new ModelBox(Wheel6, 177, 46, -1.0F, 20.9232F, 5.5F, 2, 9, 1, 0.0F));
        Wheel6.cubeList.add(new ModelBox(Wheel6, 166, 171, -1.0F, 19.9232F, 4.5F, 2, 11, 1, 0.0F));
        Wheel6.cubeList.add(new ModelBox(Wheel6, 114, 164, -1.0F, 18.9232F, 2.5F, 2, 13, 2, 0.0F));
        Wheel6.cubeList.add(new ModelBox(Wheel6, 155, 133, -1.0F, 17.9232F, -2.5F, 2, 15, 5, 0.0F));
        Wheel6.cubeList.add(new ModelBox(Wheel6, 106, 164, -1.0F, 18.9232F, -4.5F, 2, 13, 2, 0.0F));
        Wheel6.cubeList.add(new ModelBox(Wheel6, 160, 171, -1.0F, 19.9232F, -5.5F, 2, 11, 1, 0.0F));
        Wheel6.cubeList.add(new ModelBox(Wheel6, 177, 10, -1.0F, 20.9232F, -6.5F, 2, 9, 1, 0.0F));
        Wheel6.cubeList.add(new ModelBox(Wheel6, 12, 179, -1.0F, 22.9232F, -7.5F, 2, 5, 1, 0.0F));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        Structure.render(f5);
        Ram.render(f5);
        FirstDuoRopes.render(f5);
        SecondDuoRopes.render(f5);
        ThirdDuoRopes.render(f5);
        Wheel1.render(f5);
        Wheel2.render(f5);
        Wheel3.render(f5);
        Wheel4.render(f5);
        Wheel5.render(f5);
        Wheel6.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}