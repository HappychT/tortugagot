package com.tortugagot.togcore.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ToffyCrateCreatureModel extends ModelBase {

    private final ModelRenderer base;
    private final ModelRenderer tentacle_left_r1;
    private final ModelRenderer tentacle_left_r2;
    private final ModelRenderer tentacle_left_r3;
    private final ModelRenderer tentacle_left_r4;
    private final ModelRenderer tentacle_left_r5;
    private final ModelRenderer tentacle_left_r6;
    private final ModelRenderer tentacle_right_r1;
    private final ModelRenderer tentacle_right_r2;
    private final ModelRenderer tentacle_right_r3;
    private final ModelRenderer tentacle_left_r7;
    private final ModelRenderer top;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer top_tentacle;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer handle;
    private final ModelRenderer cube_r5;
    private final ModelRenderer lid;
    private final ModelRenderer cube_r6;
    private final ModelRenderer leg_right;
    private final ModelRenderer cube_r7;
    private final ModelRenderer leg_right_low;
    private final ModelRenderer cube_r8;
    private final ModelRenderer cube_r9;
    private final ModelRenderer leg_left;
    private final ModelRenderer cube_r10;
    private final ModelRenderer leg_left_low;
    private final ModelRenderer cube_r11;
    private final ModelRenderer cube_r12;
    private final ModelRenderer leg_left_back;
    private final ModelRenderer cube_r13;
    private final ModelRenderer leg_left_low_back;
    private final ModelRenderer cube_r14;
    private final ModelRenderer cube_r15;
    private final ModelRenderer leg_right_back;
    private final ModelRenderer cube_r16;
    private final ModelRenderer leg_right_low_back;
    private final ModelRenderer cube_r17;
    private final ModelRenderer cube_r18;
    private final ModelRenderer low_chain;
    private final ModelRenderer low_chain2;
    private final ModelRenderer chain0;
    private final ModelRenderer cube_r19;
    private final ModelRenderer chain1;
    private final ModelRenderer cube_r20;
    private final ModelRenderer chain2;
    private final ModelRenderer cube_r21;
    private final ModelRenderer iris;
    private final ModelRenderer key;
    private final ModelRenderer cube_r22;

    public ToffyCrateCreatureModel() {
        textureWidth = 128;
        textureHeight = 128;

        base = new ModelRenderer(this);
        base.setRotationPoint(0.0F, 13.6429F, -2.4286F);
        base.cubeList.add(new ModelBox(base, 0, 51, -10.0F, 5.3571F, -3.5714F, 20, 2, 12, 0.0F));
        base.cubeList.add(new ModelBox(base, 14, 83, -11.0F, -0.6429F, -4.5714F, 22, 4, 14, 0.0F));
        base.cubeList.add(new ModelBox(base, 2, 65, -9.0F, -2.6429F, -2.5714F, 18, 8, 10, 0.0F));
        base.cubeList.add(new ModelBox(base, 1, 37, -9.5F, -5.6429F, -3.0714F, 19, 3, 11, 0.0F));
        base.cubeList.add(new ModelBox(base, 58, 27, -8.0F, -7.6429F, -1.5714F, 16, 2, 8, 0.0F));
        base.cubeList.add(new ModelBox(base, 50, 39, -3.5F, -2.6429F, -6.5714F, 7, 7, 2, 0.0F));
        base.cubeList.add(new ModelBox(base, 48, 68, -2.5F, -1.6429F, -7.5714F, 5, 5, 2, 0.0F));
        base.cubeList.add(new ModelBox(base, 8, 109, -2.5F, -5.1429F, -6.0714F, 1, 3, 1, 0.0F));
        base.cubeList.add(new ModelBox(base, 8, 101, 1.5F, -5.1429F, -6.0714F, 1, 3, 1, 0.0F));
        base.cubeList.add(new ModelBox(base, 0, 103, -1.5F, -5.1429F, -6.0714F, 3, 1, 1, 0.0F));

        tentacle_left_r1 = new ModelRenderer(this);
        tentacle_left_r1.setRotationPoint(12.8265F, -2.6578F, 2.1392F);
        base.addChild(tentacle_left_r1);
        setRotationAngle(tentacle_left_r1, -0.3927F, 0.0F, 0.0F);
        tentacle_left_r1.cubeList.add(new ModelBox(tentacle_left_r1, 46, 19, -1.75F, -0.75F, -0.5F, 2, 2, 4, 0.0F));
        tentacle_left_r1.cubeList.add(new ModelBox(tentacle_left_r1, 46, 19, -25.903F, -0.75F, -0.5F, 2, 2, 4, 0.0F));

        tentacle_left_r2 = new ModelRenderer(this);
        tentacle_left_r2.setRotationPoint(12.8265F, -0.6397F, 3.3845F);
        base.addChild(tentacle_left_r2);
        setRotationAngle(tentacle_left_r2, 0.7854F, 0.0F, 0.0F);
        tentacle_left_r2.cubeList.add(new ModelBox(tentacle_left_r2, 116, 122, -1.75F, -1.5F, 0.5F, 3, 3, 3, 0.0F));
        tentacle_left_r2.cubeList.add(new ModelBox(tentacle_left_r2, 84, 91, -26.903F, -1.5F, 0.5F, 3, 3, 3, 0.0F));

        tentacle_left_r3 = new ModelRenderer(this);
        tentacle_left_r3.setRotationPoint(12.8265F, -3.7461F, 7.6824F);
        base.addChild(tentacle_left_r3);
        setRotationAngle(tentacle_left_r3, 0.3927F, 0.0F, 0.0F);
        tentacle_left_r3.cubeList.add(new ModelBox(tentacle_left_r3, 78, 73, -2.25F, -1.5F, -2.5F, 4, 4, 6, 0.0F));
        tentacle_left_r3.cubeList.add(new ModelBox(tentacle_left_r3, 94, 50, -27.403F, -1.5F, -2.5F, 4, 4, 6, 0.0F));

        tentacle_left_r4 = new ModelRenderer(this);
        tentacle_left_r4.setRotationPoint(12.8265F, -4.35F, 10.342F);
        base.addChild(tentacle_left_r4);
        setRotationAngle(tentacle_left_r4, -0.7854F, 0.0F, 0.0F);
        tentacle_left_r4.cubeList.add(new ModelBox(tentacle_left_r4, 58, 73, -2.5F, -1.5F, -1.5F, 5, 5, 5, 0.0F));
        tentacle_left_r4.cubeList.add(new ModelBox(tentacle_left_r4, 62, 63, -28.153F, -1.5F, -1.5F, 5, 5, 5, 0.0F));

        tentacle_left_r5 = new ModelRenderer(this);
        tentacle_left_r5.setRotationPoint(9.3562F, -1.1429F, 10.3943F);
        base.addChild(tentacle_left_r5);
        setRotationAngle(tentacle_left_r5, 0.0F, -0.3927F, 0.0F);
        tentacle_left_r5.cubeList.add(new ModelBox(tentacle_left_r5, 27, 3, -2.5F, 0.475F, -1.5F, 5, 2, 5, 0.0F));

        tentacle_left_r6 = new ModelRenderer(this);
        tentacle_left_r6.setRotationPoint(5.0F, -1.1429F, 8.4286F);
        base.addChild(tentacle_left_r6);
        setRotationAngle(tentacle_left_r6, 0.0F, 0.7854F, 0.0F);
        tentacle_left_r6.cubeList.add(new ModelBox(tentacle_left_r6, 86, 60, -2.5F, -2.5F, -4.5F, 5, 5, 8, 0.0F));

        tentacle_right_r1 = new ModelRenderer(this);
        tentacle_right_r1.setRotationPoint(-12.5585F, -2.9448F, 2.4642F);
        base.addChild(tentacle_right_r1);
        setRotationAngle(tentacle_right_r1, 0.0F, 0.0F, -0.3927F);
        tentacle_right_r1.cubeList.add(new ModelBox(tentacle_right_r1, 48, 25, 0.0F, 0.25F, -1.5F, 3, 1, 1, 0.0F));

        tentacle_right_r2 = new ModelRenderer(this);
        tentacle_right_r2.setRotationPoint(-9.3562F, -1.1429F, 10.3943F);
        base.addChild(tentacle_right_r2);
        setRotationAngle(tentacle_right_r2, 0.0F, 0.3927F, 0.0F);
        tentacle_right_r2.cubeList.add(new ModelBox(tentacle_right_r2, 7, 3, -2.5F, 0.475F, -1.5F, 5, 2, 5, 0.0F));

        tentacle_right_r3 = new ModelRenderer(this);
        tentacle_right_r3.setRotationPoint(-5.0F, -1.1429F, 8.4286F);
        base.addChild(tentacle_right_r3);
        setRotationAngle(tentacle_right_r3, 0.0F, -0.7854F, 0.0F);
        tentacle_right_r3.cubeList.add(new ModelBox(tentacle_right_r3, 68, 50, -2.5F, -2.5F, -4.5F, 5, 5, 8, 0.0F));

        tentacle_left_r7 = new ModelRenderer(this);
        tentacle_left_r7.setRotationPoint(12.5585F, -2.9448F, 2.4642F);
        base.addChild(tentacle_left_r7);
        setRotationAngle(tentacle_left_r7, 0.0F, 0.0F, 0.3927F);
        tentacle_left_r7.cubeList.add(new ModelBox(tentacle_left_r7, 48, 25, -3.0F, 0.25F, -1.5F, 3, 1, 1, 0.0F));

        top = new ModelRenderer(this);
        top.setRotationPoint(0.0F, -3.6429F, 8.4286F);
        base.addChild(top);
        top.cubeList.add(new ModelBox(top, 52, 51, -2.0F, -9.6213F, 1.6569F, 4, 8, 4, 0.0F));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(0.0F, -7.5F, 0.0F);
        top.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.7854F, 0.0F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 98, 74, -1.5F, -3.5F, 2.5F, 3, 6, 3, 0.0F));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(0.0F, 0.5F, 0.0F);
        top.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.7854F, 0.0F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 12, 12, -2.5F, -2.5F, -2.5F, 5, 5, 8, 0.0F));

        top_tentacle = new ModelRenderer(this);
        top_tentacle.setRotationPoint(0.0F, -13.75F, 1.25F);
        top.addChild(top_tentacle);
        top_tentacle.cubeList.add(new ModelBox(top_tentacle, 68, 42, -1.525F, -0.114F, -4.8358F, 3, 3, 5, 0.0F));

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(0.0F, 6.25F, -6.25F);
        top_tentacle.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.7854F, 0.0F, 0.0F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 86, 63, -1.0F, -1.5F, 0.5F, 2, 3, 2, 0.0F));
        cube_r3.cubeList.add(new ModelBox(cube_r3, 86, 49, -1.0F, -3.5F, 0.5F, 2, 2, 5, 0.0F));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(0.0F, 5.7626F, -5.8718F);
        top_tentacle.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.3927F, 0.0F, 0.0F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 73, 73, -1.025F, -0.5F, 0.5F, 2, 2, 3, 0.0F));

        handle = new ModelRenderer(this);
        handle.setRotationPoint(0.0F, 4.0F, -4.25F);
        top_tentacle.addChild(handle);

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(3.0F, 2.75F, -3.0F);
        handle.addChild(cube_r5);
        setRotationAngle(cube_r5, -0.7854F, 0.0F, 0.0F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 72, 52, 0.0F, -4.5F, -0.5F, 1, 5, 1, 0.0F));
        cube_r5.cubeList.add(new ModelBox(cube_r5, 79, 45, -6.0F, -4.5F, -0.5F, 6, 1, 1, 0.0F));
        cube_r5.cubeList.add(new ModelBox(cube_r5, 68, 52, -7.0F, -4.5F, -0.5F, 1, 5, 1, 0.0F));
        cube_r5.cubeList.add(new ModelBox(cube_r5, 66, 37, -5.5F, -5.0F, -1.0F, 5, 2, 2, 0.0F));

        lid = new ModelRenderer(this);
        lid.setRotationPoint(0.0F, 2.75F, -3.0F);
        handle.addChild(lid);
        lid.cubeList.add(new ModelBox(lid, 68, 15, 4.0F, -1.0F, -1.0F, 3, 2, 2, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 68, 15, -7.0F, -1.0F, -1.0F, 3, 2, 2, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 2, 25, -9.0F, 1.0F, -5.0F, 18, 2, 10, 0.0F));

        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(1.25F, 0.5F, -4.0F);
        lid.addChild(cube_r6);
        setRotationAngle(cube_r6, -0.7854F, 0.0F, 0.0F);
        cube_r6.cubeList.add(new ModelBox(cube_r6, 72, 12, -2.25F, -0.75F, -1.5F, 2, 1, 1, 0.0F));
        cube_r6.cubeList.add(new ModelBox(cube_r6, 1, 100, -0.5F, -0.75F, -0.5F, 1, 1, 2, 0.0F));
        cube_r6.cubeList.add(new ModelBox(cube_r6, 103, 29, -3.0F, -0.75F, -0.5F, 1, 1, 2, 0.0F));

        leg_right = new ModelRenderer(this);
        leg_right.setRotationPoint(-7.9218F, 7.7503F, -1.5714F);
        base.addChild(leg_right);
        setRotationAngle(leg_right, 0.0948F, -0.8249F, -0.1288F);
        leg_right.cubeList.add(new ModelBox(leg_right, 50, 27, -5.0F, -1.0F, -1.0F, 6, 2, 2, 0.0F));

        cube_r7 = new ModelRenderer(this);
        cube_r7.setRotationPoint(-9.0782F, 1.6069F, 0.0F);
        leg_right.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, 0.0F, -0.3927F);
        cube_r7.cubeList.add(new ModelBox(cube_r7, 0, 83, 4.0F, -5.0F, -0.975F, 2, 6, 2, 0.0F));

        leg_right_low = new ModelRenderer(this);
        leg_right_low.setRotationPoint(-6.3282F, -4.6432F, 0.0F);
        leg_right.addChild(leg_right_low);
        setRotationAngle(leg_right_low, 0.0F, 0.0F, 0.4363F);

        cube_r8 = new ModelRenderer(this);
        cube_r8.setRotationPoint(-2.25F, 6.25F, 0.0F);
        leg_right_low.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.0F, 0.7854F, 0.0F);
        cube_r8.cubeList.add(new ModelBox(cube_r8, 109, 30, -0.25F, -10.0F, -0.25F, 2, 11, 2, 0.0F));

        cube_r9 = new ModelRenderer(this);
        cube_r9.setRotationPoint(-0.25F, 0.25F, 0.0F);
        leg_right_low.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.0F, 0.7854F, 0.0F);
        cube_r9.cubeList.add(new ModelBox(cube_r9, 110, 74, -2.4142F, -1.0F, -2.4142F, 3, 6, 3, 0.0F));

        leg_left = new ModelRenderer(this);
        leg_left.setRotationPoint(7.9218F, 7.7503F, -1.5714F);
        base.addChild(leg_left);
        setRotationAngle(leg_left, 0.0948F, 0.8249F, 0.1288F);
        leg_left.cubeList.add(new ModelBox(leg_left, 56, 23, -1.0F, -1.0F, -1.0F, 6, 2, 2, 0.0F));

        cube_r10 = new ModelRenderer(this);
        cube_r10.setRotationPoint(9.0782F, 1.6069F, 0.0F);
        leg_left.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.0F, 0.0F, 0.3927F);
        cube_r10.cubeList.add(new ModelBox(cube_r10, 112, 66, -6.0F, -5.0F, -0.975F, 2, 6, 2, 0.0F));

        leg_left_low = new ModelRenderer(this);
        leg_left_low.setRotationPoint(6.3282F, -4.6432F, 0.0F);
        leg_left.addChild(leg_left_low);
        setRotationAngle(leg_left_low, 0.0F, 0.0F, -0.4363F);

        cube_r11 = new ModelRenderer(this);
        cube_r11.setRotationPoint(2.25F, 6.25F, 0.0F);
        leg_left_low.addChild(cube_r11);
        setRotationAngle(cube_r11, 0.0F, -0.7854F, 0.0F);
        cube_r11.cubeList.add(new ModelBox(cube_r11, 109, 43, -1.75F, -10.0F, -0.25F, 2, 11, 2, 0.0F));

        cube_r12 = new ModelRenderer(this);
        cube_r12.setRotationPoint(0.25F, 0.25F, 0.0F);
        leg_left_low.addChild(cube_r12);
        setRotationAngle(cube_r12, 0.0F, -0.7854F, 0.0F);
        cube_r12.cubeList.add(new ModelBox(cube_r12, 72, 88, -0.5858F, -1.0F, -2.4142F, 3, 6, 3, 0.0F));

        leg_left_back = new ModelRenderer(this);
        leg_left_back.setRotationPoint(7.9218F, 7.7503F, 6.4286F);
        base.addChild(leg_left_back);
        setRotationAngle(leg_left_back, -0.0948F, -0.8249F, 0.1288F);
        leg_left_back.cubeList.add(new ModelBox(leg_left_back, 50, 31, -1.0F, -1.0F, -1.0F, 6, 2, 2, 0.0F));

        cube_r13 = new ModelRenderer(this);
        cube_r13.setRotationPoint(9.0782F, 1.6069F, 0.0F);
        leg_left_back.addChild(cube_r13);
        setRotationAngle(cube_r13, 0.0F, 0.0F, 0.3927F);
        cube_r13.cubeList.add(new ModelBox(cube_r13, 0, 92, -6.0F, -5.0F, -0.975F, 2, 6, 2, 0.0F));

        leg_left_low_back = new ModelRenderer(this);
        leg_left_low_back.setRotationPoint(6.3282F, -4.6432F, 0.0F);
        leg_left_back.addChild(leg_left_low_back);
        setRotationAngle(leg_left_low_back, 0.0F, 0.0F, -0.4363F);

        cube_r14 = new ModelRenderer(this);
        cube_r14.setRotationPoint(2.25F, 6.25F, 0.0F);
        leg_left_low_back.addChild(cube_r14);
        setRotationAngle(cube_r14, 0.0F, 0.7854F, 0.0F);
        cube_r14.cubeList.add(new ModelBox(cube_r14, 117, 43, -1.75F, -10.0F, -1.75F, 2, 11, 2, 0.0F));

        cube_r15 = new ModelRenderer(this);
        cube_r15.setRotationPoint(0.25F, 0.25F, 0.0F);
        leg_left_low_back.addChild(cube_r15);
        setRotationAngle(cube_r15, 0.0F, 0.7854F, 0.0F);
        cube_r15.cubeList.add(new ModelBox(cube_r15, 0, 39, -0.5858F, -1.0F, -0.5858F, 3, 6, 3, 0.0F));

        leg_right_back = new ModelRenderer(this);
        leg_right_back.setRotationPoint(-7.9218F, 7.7503F, 6.4286F);
        base.addChild(leg_right_back);
        setRotationAngle(leg_right_back, -0.0948F, 0.8249F, -0.1288F);
        leg_right_back.cubeList.add(new ModelBox(leg_right_back, 62, 19, -5.0F, -1.0F, -1.0F, 6, 2, 2, 0.0F));

        cube_r16 = new ModelRenderer(this);
        cube_r16.setRotationPoint(-9.0782F, 1.6069F, 0.0F);
        leg_right_back.addChild(cube_r16);
        setRotationAngle(cube_r16, 0.0F, 0.0F, -0.3927F);
        cube_r16.cubeList.add(new ModelBox(cube_r16, 8, 83, 4.0F, -5.0F, -0.975F, 2, 6, 2, 0.0F));

        leg_right_low_back = new ModelRenderer(this);
        leg_right_low_back.setRotationPoint(-6.3282F, -4.6432F, 0.0F);
        leg_right_back.addChild(leg_right_low_back);
        setRotationAngle(leg_right_low_back, 0.0F, 0.0F, 0.4363F);

        cube_r17 = new ModelRenderer(this);
        cube_r17.setRotationPoint(-2.25F, 6.25F, 0.0F);
        leg_right_low_back.addChild(cube_r17);
        setRotationAngle(cube_r17, 0.0F, -0.7854F, 0.0F);
        cube_r17.cubeList.add(new ModelBox(cube_r17, 78, 10, -0.25F, -10.0F, -1.75F, 2, 11, 2, 0.0F));

        cube_r18 = new ModelRenderer(this);
        cube_r18.setRotationPoint(-0.25F, 0.25F, 0.0F);
        leg_right_low_back.addChild(cube_r18);
        setRotationAngle(cube_r18, 0.0F, -0.7854F, 0.0F);
        cube_r18.cubeList.add(new ModelBox(cube_r18, 111, 57, -2.4142F, -1.0F, -0.5858F, 3, 6, 3, 0.0F));

        low_chain = new ModelRenderer(this);
        low_chain.setRotationPoint(-2.5F, 3.6071F, -4.8214F);
        base.addChild(low_chain);
        setRotationAngle(low_chain, -0.1082F, 0.7464F, -0.7491F);
        low_chain.cubeList.add(new ModelBox(low_chain, 93, 44, -4.75F, -1.75F, -1.0F, 6, 3, 2, 0.0F));

        low_chain2 = new ModelRenderer(this);
        low_chain2.setRotationPoint(2.5F, 3.6071F, -4.8214F);
        base.addChild(low_chain2);
        setRotationAngle(low_chain2, -0.1082F, -0.7464F, 0.7491F);
        low_chain2.cubeList.add(new ModelBox(low_chain2, 79, 40, -1.25F, -1.75F, -1.0F, 6, 3, 2, 0.0F));

        chain0 = new ModelRenderer(this);
        chain0.setRotationPoint(0.0F, -4.5154F, -5.4169F);
        base.addChild(chain0);

        cube_r19 = new ModelRenderer(this);
        cube_r19.setRotationPoint(0.0F, 0.8725F, -0.1545F);
        chain0.addChild(cube_r19);
        setRotationAngle(cube_r19, -0.3927F, 0.0F, 0.0F);
        cube_r19.cubeList.add(new ModelBox(cube_r19, 0, 109, -0.5F, -4.0F, -1.75F, 1, 1, 3, 0.0F));
        cube_r19.cubeList.add(new ModelBox(cube_r19, 8, 105, -0.5F, -3.0F, 0.5F, 1, 3, 1, 0.0F));
        cube_r19.cubeList.add(new ModelBox(cube_r19, 0, 105, -0.5F, 0.0F, -1.75F, 1, 1, 3, 0.0F));
        cube_r19.cubeList.add(new ModelBox(cube_r19, 8, 109, -0.5F, -3.0F, -2.0F, 1, 3, 1, 0.0F));

        chain1 = new ModelRenderer(this);
        chain1.setRotationPoint(0.0F, -1.6275F, 0.5955F);
        chain0.addChild(chain1);

        cube_r20 = new ModelRenderer(this);
        cube_r20.setRotationPoint(0.0F, -1.0F, -1.0F);
        chain1.addChild(cube_r20);
        setRotationAngle(cube_r20, -0.3927F, 0.0F, 0.0F);
        cube_r20.cubeList.add(new ModelBox(cube_r20, 64, 12, -1.5F, 0.0F, 0.75F, 3, 1, 1, 0.0F));
        cube_r20.cubeList.add(new ModelBox(cube_r20, 8, 101, 0.75F, -3.0F, 0.75F, 1, 3, 1, 0.0F));
        cube_r20.cubeList.add(new ModelBox(cube_r20, 0, 103, -1.5F, -4.0F, 0.75F, 3, 1, 1, 0.0F));
        cube_r20.cubeList.add(new ModelBox(cube_r20, 98, 31, -1.75F, -3.0F, 0.75F, 1, 3, 1, 0.0F));

        chain2 = new ModelRenderer(this);
        chain2.setRotationPoint(0.0F, -3.1225F, 0.4045F);
        chain1.addChild(chain2);

        cube_r21 = new ModelRenderer(this);
        cube_r21.setRotationPoint(0.0F, 3.6443F, 4.1946F);
        chain2.addChild(cube_r21);
        setRotationAngle(cube_r21, -0.7854F, 0.0F, 0.0F);
        cube_r21.cubeList.add(new ModelBox(cube_r21, 8, 105, -0.5F, -3.0F, -4.5F, 1, 3, 1, 0.0F));
        cube_r21.cubeList.add(new ModelBox(cube_r21, 0, 109, -0.5F, -4.0F, -6.75F, 1, 1, 3, 0.0F));
        cube_r21.cubeList.add(new ModelBox(cube_r21, 0, 105, -0.5F, 0.0F, -6.75F, 1, 1, 3, 0.0F));
        cube_r21.cubeList.add(new ModelBox(cube_r21, 8, 109, -0.5F, -3.0F, -7.0F, 1, 3, 1, 0.0F));

        iris = new ModelRenderer(this);
        iris.setRotationPoint(0.0F, 0.8571F, -7.3214F);
        base.addChild(iris);
        iris.cubeList.add(new ModelBox(iris, 82, 68, -0.5F, -2.0F, -0.5F, 1, 4, 1, 0.0F));

        key = new ModelRenderer(this);
        key.setRotationPoint(0.0F, 21.0F, -25.3F);
        key.cubeList.add(new ModelBox(key, 0, 25, 0.0F, 0.5F, -1.7F, 0, 4, 6, 0.0F));
        key.cubeList.add(new ModelBox(key, 48, 0, -0.5F, -1.0F, -7.2F, 1, 1, 14, 0.0F));
        key.cubeList.add(new ModelBox(key, 38, 21, -1.0F, -1.5F, -9.2F, 2, 2, 2, 0.0F));
        key.cubeList.add(new ModelBox(key, 30, 11, -1.0F, -1.5F, -2.2F, 2, 2, 7, 0.0F));

        cube_r22 = new ModelRenderer(this);
        cube_r22.setRotationPoint(0.0F, -0.5F, 3.8F);
        key.addChild(cube_r22);
        setRotationAngle(cube_r22, 0.0F, 0.0F, -0.7854F);
        cube_r22.cubeList.add(new ModelBox(cube_r22, 64, 53, -1.0F, -1.0F, 2.0F, 2, 2, 0, 0.0F));
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
        base.render(scaleFactor);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor, Entity entity) {
        base.rotateAngleY = MathHelper.sin(limbSwing * 0.9f) * (limbSwingAmount * 0.2f);
        base.rotateAngleZ = MathHelper.sin(limbSwing * 0.9f) * (limbSwingAmount * 0.2f);

        leg_left.rotateAngleY = MathHelper.sin(limbSwing * 0.9f) * 2f * (limbSwingAmount * 0.5f);
        leg_left.rotateAngleZ = MathHelper.sin(limbSwing * -0.9f) * (limbSwingAmount * 0.5f);
        leg_left_back.rotateAngleY = MathHelper.sin(limbSwing * 0.9f) * 2f * (limbSwingAmount * 0.5f);
        leg_left_back.rotateAngleZ = MathHelper.sin(limbSwing * -0.9f) * (limbSwingAmount * 0.5f);
        leg_right.rotateAngleY = leg_left.rotateAngleY;
        leg_right.rotateAngleZ = leg_left.rotateAngleZ;
        leg_right_back.rotateAngleY = leg_left_back.rotateAngleY;
        leg_right_back.rotateAngleZ = leg_left_back.rotateAngleZ;
    }
}
