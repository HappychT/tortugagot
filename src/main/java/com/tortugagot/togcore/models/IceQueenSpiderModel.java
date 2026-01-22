package com.tortugagot.togcore.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class IceQueenSpiderModel extends ModelBase {

    private final ModelRenderer root;
    private final ModelRenderer body;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer cube_r6;
    private final ModelRenderer cube_r7;
    private final ModelRenderer cube_r8;
    private final ModelRenderer cube_r9;
    private final ModelRenderer cube_r10;
    private final ModelRenderer cube_r11;
    private final ModelRenderer cube_r12;
    private final ModelRenderer cube_r13;
    private final ModelRenderer cube_r14;
    private final ModelRenderer cube_r15;
    private final ModelRenderer cube_r16;
    private final ModelRenderer cube_r17;
    private final ModelRenderer cube_r18;
    private final ModelRenderer cube_r19;
    private final ModelRenderer l_mondible;
    private final ModelRenderer cube_r20;
    private final ModelRenderer cube_r21;
    private final ModelRenderer cube_r22;
    private final ModelRenderer r_mondible;
    private final ModelRenderer cube_r23;
    private final ModelRenderer cube_r24;
    private final ModelRenderer cube_r25;
    private final ModelRenderer body2;
    private final ModelRenderer cube_r26;
    private final ModelRenderer cube_r27;
    private final ModelRenderer cube_r28;
    private final ModelRenderer cube_r29;
    private final ModelRenderer cube_r30;
    private final ModelRenderer cube_r31;
    private final ModelRenderer cube_r32;
    private final ModelRenderer cube_r33;
    private final ModelRenderer cube_r34;
    private final ModelRenderer cube_r35;
    private final ModelRenderer cube_r36;
    private final ModelRenderer cube_r37;
    private final ModelRenderer cube_r38;
    private final ModelRenderer cube_r39;
    private final ModelRenderer cube_r40;
    private final ModelRenderer cube_r41;
    private final ModelRenderer left_bone4;
    private final ModelRenderer cube_r42;
    private final ModelRenderer cube_r43;
    private final ModelRenderer cube_r44;
    private final ModelRenderer cube_r45;
    private final ModelRenderer right_bone4;
    private final ModelRenderer cube_r46;
    private final ModelRenderer cube_r47;
    private final ModelRenderer cube_r48;
    private final ModelRenderer cube_r49;
    private final ModelRenderer left_bone3;
    private final ModelRenderer cube_r50;
    private final ModelRenderer cube_r51;
    private final ModelRenderer cube_r52;
    private final ModelRenderer cube_r53;
    private final ModelRenderer right_bone3;
    private final ModelRenderer cube_r54;
    private final ModelRenderer cube_r55;
    private final ModelRenderer cube_r56;
    private final ModelRenderer cube_r57;
    private final ModelRenderer left_bone2;
    private final ModelRenderer cube_r58;
    private final ModelRenderer cube_r59;
    private final ModelRenderer cube_r60;
    private final ModelRenderer cube_r61;
    private final ModelRenderer right_bone2;
    private final ModelRenderer cube_r62;
    private final ModelRenderer cube_r63;
    private final ModelRenderer cube_r64;
    private final ModelRenderer cube_r65;
    private final ModelRenderer left_bone1;
    private final ModelRenderer cube_r66;
    private final ModelRenderer cube_r67;
    private final ModelRenderer cube_r68;
    private final ModelRenderer cube_r69;
    private final ModelRenderer right_bone1;
    private final ModelRenderer cube_r70;
    private final ModelRenderer cube_r71;
    private final ModelRenderer cube_r72;
    private final ModelRenderer cube_r73;

    public IceQueenSpiderModel() {
        textureWidth = 256;
        textureHeight = 256;

        root = new ModelRenderer(this);
        root.setRotationPoint(0.0F, -4.0555F, -5.6901F);

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, -3.4445F, 0.6901F);
        root.addChild(body);
        body.cubeList.add(new ModelBox(body, 0, 57, -14.0F, -8.5F, -17.0F, 28, 17, 35, 0.0F));
        body.cubeList.add(new ModelBox(body, 127, 57, -10.0F, -7.5F, 16.0F, 20, 15, 19, 0.0F));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(-3.0F, 0.75F, -30.0F);
        body.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.3099F, 0.1664F, -0.053F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 135, 49, -4.0F, -4.0F, -2.0F, 4, 4, 3, 0.0F));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(6.0F, -4.5F, -28.0F);
        body.addChild(cube_r2);
        setRotationAngle(cube_r2, -0.3099F, -0.1664F, 0.053F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 127, 100, -1.0F, -5.0F, -2.0F, 5, 5, 3, 0.0F));

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(-6.0F, -4.5F, -28.0F);
        body.addChild(cube_r3);
        setRotationAngle(cube_r3, -0.3099F, 0.1664F, -0.053F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 127, 100, -4.0F, -5.0F, -2.0F, 5, 5, 3, 0.0F));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(-3.0F, -10.0F, -26.0F);
        body.addChild(cube_r4);
        setRotationAngle(cube_r4, -0.3099F, 0.1664F, -0.053F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 127, 100, -4.0F, -5.0F, -2.0F, 5, 5, 3, 0.0F));

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(-0.5F, -4.25F, -29.25F);
        body.addChild(cube_r5);
        setRotationAngle(cube_r5, -0.3099F, 0.1664F, -0.053F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 135, 49, -4.0F, -4.0F, -2.0F, 4, 4, 3, 0.0F));

        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(0.5F, -4.25F, -29.25F);
        body.addChild(cube_r6);
        setRotationAngle(cube_r6, -0.3099F, -0.1664F, 0.053F);
        cube_r6.cubeList.add(new ModelBox(cube_r6, 135, 49, 0.0F, -4.0F, -2.0F, 4, 4, 3, 0.0F));

        cube_r7 = new ModelRenderer(this);
        cube_r7.setRotationPoint(3.0F, 0.75F, -30.0F);
        body.addChild(cube_r7);
        setRotationAngle(cube_r7, -0.3099F, -0.1664F, 0.053F);
        cube_r7.cubeList.add(new ModelBox(cube_r7, 135, 49, 0.0F, -4.0F, -2.0F, 4, 4, 3, 0.0F));

        cube_r8 = new ModelRenderer(this);
        cube_r8.setRotationPoint(3.0F, -10.0F, -26.0F);
        body.addChild(cube_r8);
        setRotationAngle(cube_r8, -0.3099F, -0.1664F, 0.053F);
        cube_r8.cubeList.add(new ModelBox(cube_r8, 127, 100, -1.0F, -5.0F, -2.0F, 5, 5, 3, 0.0F));

        cube_r9 = new ModelRenderer(this);
        cube_r9.setRotationPoint(12.0511F, -16.7517F, 10.9763F);
        body.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.0F, -0.1745F, 0.2182F);
        cube_r9.cubeList.add(new ModelBox(cube_r9, 108, 164, -3.0F, -7.0F, -26.5F, 0, 17, 12, 0.0F));

        cube_r10 = new ModelRenderer(this);
        cube_r10.setRotationPoint(-12.0511F, -16.7517F, 10.9763F);
        body.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.0F, 0.1745F, -0.2182F);
        cube_r10.cubeList.add(new ModelBox(cube_r10, 108, 164, 3.0F, -7.0F, -26.5F, 0, 17, 12, 0.0F));

        cube_r11 = new ModelRenderer(this);
        cube_r11.setRotationPoint(-10.0F, -8.5F, 10.0F);
        body.addChild(cube_r11);
        setRotationAngle(cube_r11, 0.0F, -0.2182F, -0.2182F);
        cube_r11.cubeList.add(new ModelBox(cube_r11, 108, 164, 0.0F, -17.0F, -5.0F, 0, 17, 12, 0.0F));

        cube_r12 = new ModelRenderer(this);
        cube_r12.setRotationPoint(-5.0F, -8.5F, 13.0F);
        body.addChild(cube_r12);
        setRotationAngle(cube_r12, 0.0304F, -0.5328F, -0.0802F);
        cube_r12.cubeList.add(new ModelBox(cube_r12, 0, 151, 0.0F, -25.0F, -7.0F, 0, 25, 14, 0.0F));

        cube_r13 = new ModelRenderer(this);
        cube_r13.setRotationPoint(10.0F, -8.5F, 10.0F);
        body.addChild(cube_r13);
        setRotationAngle(cube_r13, 0.0F, 0.2182F, 0.2182F);
        cube_r13.cubeList.add(new ModelBox(cube_r13, 108, 164, 0.0F, -17.0F, -5.0F, 0, 17, 12, 0.0F));

        cube_r14 = new ModelRenderer(this);
        cube_r14.setRotationPoint(5.0F, -8.5F, 13.0F);
        body.addChild(cube_r14);
        setRotationAngle(cube_r14, 0.0304F, 0.5328F, 0.0802F);
        cube_r14.cubeList.add(new ModelBox(cube_r14, 0, 151, 0.0F, -25.0F, -7.0F, 0, 25, 14, 0.0F));

        cube_r15 = new ModelRenderer(this);
        cube_r15.setRotationPoint(5.0F, -8.25F, -1.0F);
        body.addChild(cube_r15);
        setRotationAngle(cube_r15, 0.1561F, 0.5124F, 0.3311F);
        cube_r15.cubeList.add(new ModelBox(cube_r15, 0, 151, 0.0F, -25.0F, -7.0F, 0, 25, 14, 0.0F));

        cube_r16 = new ModelRenderer(this);
        cube_r16.setRotationPoint(-14.0F, -6.5F, -8.0F);
        body.addChild(cube_r16);
        setRotationAngle(cube_r16, 0.1186F, -0.1836F, -0.7963F);
        cube_r16.cubeList.add(new ModelBox(cube_r16, 108, 164, 0.0F, -17.0F, -5.0F, 0, 17, 12, 0.0F));

        cube_r17 = new ModelRenderer(this);
        cube_r17.setRotationPoint(-13.0F, -7.5F, 10.0F);
        body.addChild(cube_r17);
        setRotationAngle(cube_r17, 0.1166F, 0.0331F, -0.7707F);
        cube_r17.cubeList.add(new ModelBox(cube_r17, 108, 164, 0.0F, -17.0F, -5.0F, 0, 17, 12, 0.0F));

        cube_r18 = new ModelRenderer(this);
        cube_r18.setRotationPoint(-5.0F, -8.25F, -1.0F);
        body.addChild(cube_r18);
        setRotationAngle(cube_r18, 0.1561F, -0.5124F, -0.3311F);
        cube_r18.cubeList.add(new ModelBox(cube_r18, 0, 151, 0.0F, -25.0F, -7.0F, 0, 25, 14, 0.0F));

        cube_r19 = new ModelRenderer(this);
        cube_r19.setRotationPoint(0.0F, 3.5F, -14.5F);
        body.addChild(cube_r19);
        setRotationAngle(cube_r19, -0.3491F, 0.0F, 0.0F);
        cube_r19.cubeList.add(new ModelBox(cube_r19, 101, 127, -9.0F, -18.0F, -15.0F, 18, 21, 15, 0.0F));

        l_mondible = new ModelRenderer(this);
        l_mondible.setRotationPoint(9.8934F, 0.6719F, -19.3258F);
        body.addChild(l_mondible);

        cube_r20 = new ModelRenderer(this);
        cube_r20.setRotationPoint(-2.6624F, 12.8433F, -21.0391F);
        l_mondible.addChild(cube_r20);
        setRotationAngle(cube_r20, -0.8858F, 0.4896F, 0.2595F);
        cube_r20.cubeList.add(new ModelBox(cube_r20, 87, 151, -2.5F, -5.5F, -1.5F, 3, 7, 3, 0.0F));

        cube_r21 = new ModelRenderer(this);
        cube_r21.setRotationPoint(-0.5F, 5.5F, -8.0F);
        l_mondible.addChild(cube_r21);
        setRotationAngle(cube_r21, -0.9631F, -0.2035F, -0.0392F);
        cube_r21.cubeList.add(new ModelBox(cube_r21, 135, 0, -2.8445F, -16.8512F, -4.9451F, 7, 21, 11, 0.0F));

        cube_r22 = new ModelRenderer(this);
        cube_r22.setRotationPoint(-0.5F, 5.5F, -8.0F);
        l_mondible.addChild(cube_r22);
        setRotationAngle(cube_r22, -0.9645F, 0.1932F, 0.2328F);
        cube_r22.cubeList.add(new ModelBox(cube_r22, 135, 33, -0.1488F, 1.4362F, -3.5549F, 5, 9, 6, 0.0F));

        r_mondible = new ModelRenderer(this);
        r_mondible.setRotationPoint(-9.8934F, 0.6719F, -19.3258F);
        body.addChild(r_mondible);

        cube_r23 = new ModelRenderer(this);
        cube_r23.setRotationPoint(2.6624F, 12.8433F, -21.0391F);
        r_mondible.addChild(cube_r23);
        setRotationAngle(cube_r23, -0.8858F, -0.4896F, -0.2595F);
        cube_r23.cubeList.add(new ModelBox(cube_r23, 87, 151, -0.5F, -5.5F, -1.5F, 3, 7, 3, 0.0F));

        cube_r24 = new ModelRenderer(this);
        cube_r24.setRotationPoint(0.5F, 5.5F, -8.0F);
        r_mondible.addChild(cube_r24);
        setRotationAngle(cube_r24, -0.9645F, -0.1932F, -0.2328F);
        cube_r24.cubeList.add(new ModelBox(cube_r24, 135, 33, -4.8512F, 1.4362F, -3.5549F, 5, 9, 6, 0.0F));

        cube_r25 = new ModelRenderer(this);
        cube_r25.setRotationPoint(0.5F, 5.5F, -8.0F);
        r_mondible.addChild(cube_r25);
        setRotationAngle(cube_r25, -0.9631F, 0.2035F, 0.0392F);
        cube_r25.cubeList.add(new ModelBox(cube_r25, 135, 0, -4.1555F, -16.8512F, -4.9451F, 7, 21, 11, 0.0F));

        body2 = new ModelRenderer(this);
        body2.setRotationPoint(0.0F, -5.4445F, 32.69F);
        root.addChild(body2);

        cube_r26 = new ModelRenderer(this);
        cube_r26.setRotationPoint(-15.0F, -10.5F, -5.0F);
        body2.addChild(cube_r26);
        setRotationAngle(cube_r26, 0.4666F, 0.5741F, -0.5156F);
        cube_r26.cubeList.add(new ModelBox(cube_r26, 0, 151, 0.0F, -25.0F, -7.0F, 0, 25, 14, 0.0F));

        cube_r27 = new ModelRenderer(this);
        cube_r27.setRotationPoint(-15.0F, -37.5F, 15.0F);
        body2.addChild(cube_r27);
        setRotationAngle(cube_r27, -0.3927F, -0.2182F, -0.2182F);
        cube_r27.cubeList.add(new ModelBox(cube_r27, 108, 164, 0.0F, -17.0F, -5.0F, 0, 17, 12, 0.0F));

        cube_r28 = new ModelRenderer(this);
        cube_r28.setRotationPoint(-8.0F, -35.5F, 19.0F);
        body2.addChild(cube_r28);
        setRotationAngle(cube_r28, -0.3927F, -0.2182F, -0.2182F);
        cube_r28.cubeList.add(new ModelBox(cube_r28, 108, 164, 0.0F, -17.0F, -5.0F, 0, 17, 12, 0.0F));

        cube_r29 = new ModelRenderer(this);
        cube_r29.setRotationPoint(15.0F, -33.5F, 9.0F);
        body2.addChild(cube_r29);
        setRotationAngle(cube_r29, 1.2217F, 0.2182F, 0.2182F);
        cube_r29.cubeList.add(new ModelBox(cube_r29, 108, 164, 0.0F, -17.0F, -5.0F, 0, 17, 12, 0.0F));

        cube_r30 = new ModelRenderer(this);
        cube_r30.setRotationPoint(8.0F, -31.5F, 13.0F);
        body2.addChild(cube_r30);
        setRotationAngle(cube_r30, 1.2217F, 0.2182F, 0.2182F);
        cube_r30.cubeList.add(new ModelBox(cube_r30, 108, 164, 0.0F, -17.0F, -5.0F, 0, 17, 12, 0.0F));

        cube_r31 = new ModelRenderer(this);
        cube_r31.setRotationPoint(-16.0F, -28.5F, 10.0F);
        body2.addChild(cube_r31);
        setRotationAngle(cube_r31, 0.1114F, 1.1723F, -0.8698F);
        cube_r31.cubeList.add(new ModelBox(cube_r31, 0, 151, 0.0F, -25.0F, -7.0F, 0, 25, 14, 0.0F));

        cube_r32 = new ModelRenderer(this);
        cube_r32.setRotationPoint(16.0F, -17.5F, 3.0F);
        body2.addChild(cube_r32);
        setRotationAngle(cube_r32, 0.1114F, -1.1723F, 0.8698F);
        cube_r32.cubeList.add(new ModelBox(cube_r32, 0, 151, 0.0F, -25.0F, -7.0F, 0, 25, 14, 0.0F));

        cube_r33 = new ModelRenderer(this);
        cube_r33.setRotationPoint(16.0F, -28.5F, 10.0F);
        body2.addChild(cube_r33);
        setRotationAngle(cube_r33, 0.1114F, -1.1723F, 0.8698F);
        cube_r33.cubeList.add(new ModelBox(cube_r33, 0, 151, 0.0F, -25.0F, -7.0F, 0, 25, 14, 0.0F));

        cube_r34 = new ModelRenderer(this);
        cube_r34.setRotationPoint(15.0F, -10.5F, -5.0F);
        body2.addChild(cube_r34);
        setRotationAngle(cube_r34, 0.4666F, -0.5741F, 0.5156F);
        cube_r34.cubeList.add(new ModelBox(cube_r34, 0, 151, 0.0F, -25.0F, -7.0F, 0, 25, 14, 0.0F));

        cube_r35 = new ModelRenderer(this);
        cube_r35.setRotationPoint(15.0F, -37.5F, 15.0F);
        body2.addChild(cube_r35);
        setRotationAngle(cube_r35, -0.3927F, 0.2182F, 0.2182F);
        cube_r35.cubeList.add(new ModelBox(cube_r35, 108, 164, 0.0F, -17.0F, -5.0F, 0, 17, 12, 0.0F));

        cube_r36 = new ModelRenderer(this);
        cube_r36.setRotationPoint(-8.0F, -31.5F, 13.0F);
        body2.addChild(cube_r36);
        setRotationAngle(cube_r36, 1.2217F, -0.2182F, -0.2182F);
        cube_r36.cubeList.add(new ModelBox(cube_r36, 108, 164, 0.0F, -17.0F, -5.0F, 0, 17, 12, 0.0F));

        cube_r37 = new ModelRenderer(this);
        cube_r37.setRotationPoint(8.0F, -35.5F, 19.0F);
        body2.addChild(cube_r37);
        setRotationAngle(cube_r37, -0.3927F, 0.2182F, 0.2182F);
        cube_r37.cubeList.add(new ModelBox(cube_r37, 108, 164, 0.0F, -17.0F, -5.0F, 0, 17, 12, 0.0F));

        cube_r38 = new ModelRenderer(this);
        cube_r38.setRotationPoint(-15.0F, -33.5F, 9.0F);
        body2.addChild(cube_r38);
        setRotationAngle(cube_r38, 1.2217F, -0.2182F, -0.2182F);
        cube_r38.cubeList.add(new ModelBox(cube_r38, 108, 164, 0.0F, -17.0F, -5.0F, 0, 17, 12, 0.0F));

        cube_r39 = new ModelRenderer(this);
        cube_r39.setRotationPoint(-16.0F, -17.5F, 3.0F);
        body2.addChild(cube_r39);
        setRotationAngle(cube_r39, 0.1114F, 1.1723F, -0.8698F);
        cube_r39.cubeList.add(new ModelBox(cube_r39, 0, 151, 0.0F, -25.0F, -7.0F, 0, 25, 14, 0.0F));

        cube_r40 = new ModelRenderer(this);
        cube_r40.setRotationPoint(0.0F, -4.5F, 5.0F);
        body2.addChild(cube_r40);
        setRotationAngle(cube_r40, 1.0472F, 0.0F, 0.0F);
        cube_r40.cubeList.add(new ModelBox(cube_r40, 0, 0, -19.0F, -12.0F, 5.0F, 38, 27, 29, 0.0F));

        cube_r41 = new ModelRenderer(this);
        cube_r41.setRotationPoint(0.0F, -8.5F, 7.0F);
        body2.addChild(cube_r41);
        setRotationAngle(cube_r41, 0.7418F, 0.0F, 0.0F);
        cube_r41.cubeList.add(new ModelBox(cube_r41, 0, 110, -16.0F, -10.0F, -14.0F, 32, 22, 18, 0.0F));

        left_bone4 = new ModelRenderer(this);
        left_bone4.setRotationPoint(13.7547F, 0.3681F, 6.1637F);
        root.addChild(left_bone4);

        cube_r42 = new ModelRenderer(this);
        cube_r42.setRotationPoint(19.6036F, -35.2908F, 14.9193F);
        left_bone4.addChild(cube_r42);
        setRotationAngle(cube_r42, -0.5976F, -0.2849F, 1.1791F);
        cube_r42.cubeList.add(new ModelBox(cube_r42, 127, 92, -2.0F, -8.5F, 0.0F, 43, 7, 0, 0.0F));

        cube_r43 = new ModelRenderer(this);
        cube_r43.setRotationPoint(26.0F, 0.0F, -7.5F);
        left_bone4.addChild(cube_r43);
        setRotationAngle(cube_r43, 0.5148F, -0.4238F, -0.9422F);
        cube_r43.cubeList.add(new ModelBox(cube_r43, 127, 92, -13.6888F, -21.0211F, 21.6804F, 43, 7, 0, 0.0F));
        cube_r43.cubeList.add(new ModelBox(cube_r43, 101, 110, -13.6888F, -14.0211F, 17.1804F, 43, 7, 9, 0.0F));

        cube_r44 = new ModelRenderer(this);
        cube_r44.setRotationPoint(26.0F, 0.0F, -7.5F);
        left_bone4.addChild(cube_r44);
        setRotationAngle(cube_r44, 0.3405F, -0.5704F, -0.5807F);
        cube_r44.cubeList.add(new ModelBox(cube_r44, 29, 151, 19.583F, -19.5988F, 18.0804F, 6, 32, 7, 0.0F));

        cube_r45 = new ModelRenderer(this);
        cube_r45.setRotationPoint(26.0F, 0.0F, -7.5F);
        left_bone4.addChild(cube_r45);
        setRotationAngle(cube_r45, 0.0668F, -0.6516F, -0.1098F);
        cube_r45.cubeList.add(new ModelBox(cube_r45, 87, 164, 24.7181F, 1.9219F, 18.9804F, 5, 28, 5, 0.0F));

        right_bone4 = new ModelRenderer(this);
        right_bone4.setRotationPoint(-13.7547F, 0.3681F, 6.1637F);
        root.addChild(right_bone4);

        cube_r46 = new ModelRenderer(this);
        cube_r46.setRotationPoint(-19.6036F, -35.2908F, 14.9193F);
        right_bone4.addChild(cube_r46);
        setRotationAngle(cube_r46, -0.5976F, 0.2849F, -1.1791F);
        cube_r46.cubeList.add(new ModelBox(cube_r46, 127, 92, -41.0F, -8.5F, 0.0F, 43, 7, 0, 0.0F));

        cube_r47 = new ModelRenderer(this);
        cube_r47.setRotationPoint(-26.0F, 0.0F, -7.5F);
        right_bone4.addChild(cube_r47);
        setRotationAngle(cube_r47, 0.5148F, 0.4238F, 0.9422F);
        cube_r47.cubeList.add(new ModelBox(cube_r47, 127, 92, -29.3112F, -21.0211F, 21.6804F, 43, 7, 0, 0.0F));
        cube_r47.cubeList.add(new ModelBox(cube_r47, 101, 110, -29.3112F, -14.0211F, 17.1804F, 43, 7, 9, 0.0F));

        cube_r48 = new ModelRenderer(this);
        cube_r48.setRotationPoint(-26.0F, 0.0F, -7.5F);
        right_bone4.addChild(cube_r48);
        setRotationAngle(cube_r48, 0.3405F, 0.5704F, 0.5807F);
        cube_r48.cubeList.add(new ModelBox(cube_r48, 29, 151, -25.583F, -19.5988F, 18.0804F, 6, 32, 7, 0.0F));

        cube_r49 = new ModelRenderer(this);
        cube_r49.setRotationPoint(-26.0F, 0.0F, -7.5F);
        right_bone4.addChild(cube_r49);
        setRotationAngle(cube_r49, 0.0668F, 0.6516F, 0.1098F);
        cube_r49.cubeList.add(new ModelBox(cube_r49, 87, 164, -29.7181F, 1.9219F, 18.9804F, 5, 28, 5, 0.0F));

        left_bone3 = new ModelRenderer(this);
        left_bone3.setRotationPoint(14.2547F, 0.3681F, 3.1637F);
        root.addChild(left_bone3);

        cube_r50 = new ModelRenderer(this);
        cube_r50.setRotationPoint(32.2909F, -29.4294F, 2.9218F);
        left_bone3.addChild(cube_r50);
        setRotationAngle(cube_r50, -0.0774F, -0.0403F, 1.0924F);
        cube_r50.cubeList.add(new ModelBox(cube_r50, 0, 211, -12.5F, -3.25F, 0.0F, 43, 7, 0, 0.0F));

        cube_r51 = new ModelRenderer(this);
        cube_r51.setRotationPoint(25.5F, 0.0F, -4.5F);
        left_bone3.addChild(cube_r51);
        setRotationAngle(cube_r51, 0.0644F, -0.0589F, -0.8309F);
        cube_r51.cubeList.add(new ModelBox(cube_r51, 0, 211, -20.6636F, -28.6328F, 6.8017F, 43, 7, 0, 0.0F));
        cube_r51.cubeList.add(new ModelBox(cube_r51, 101, 110, -20.6636F, -21.6328F, 2.3017F, 43, 7, 9, 0.0F));

        cube_r52 = new ModelRenderer(this);
        cube_r52.setRotationPoint(25.5F, 0.0F, -4.5F);
        left_bone3.addChild(cube_r52);
        setRotationAngle(cube_r52, 0.0404F, -0.0774F, -0.4815F);
        cube_r52.cubeList.add(new ModelBox(cube_r52, 29, 151, 10.4254F, -24.3659F, 3.2017F, 6, 32, 7, 0.0F));

        cube_r53 = new ModelRenderer(this);
        cube_r53.setRotationPoint(25.5F, 0.0F, -4.5F);
        left_bone3.addChild(cube_r53);
        setRotationAngle(cube_r53, 0.0076F, -0.0869F, -0.0876F);
        cube_r53.cubeList.add(new ModelBox(cube_r53, 87, 164, 14.4333F, 1.0221F, 4.1017F, 5, 28, 5, 0.0F));

        right_bone3 = new ModelRenderer(this);
        right_bone3.setRotationPoint(-14.2547F, 0.3681F, 3.1637F);
        root.addChild(right_bone3);

        cube_r54 = new ModelRenderer(this);
        cube_r54.setRotationPoint(-32.2909F, -29.4294F, 2.9218F);
        right_bone3.addChild(cube_r54);
        setRotationAngle(cube_r54, -0.0774F, 0.0403F, -1.0924F);
        cube_r54.cubeList.add(new ModelBox(cube_r54, 0, 211, -30.5F, -3.25F, 0.0F, 43, 7, 0, 0.0F));

        cube_r55 = new ModelRenderer(this);
        cube_r55.setRotationPoint(-25.5F, 0.0F, -4.5F);
        right_bone3.addChild(cube_r55);
        setRotationAngle(cube_r55, 0.0644F, 0.0589F, 0.8309F);
        cube_r55.cubeList.add(new ModelBox(cube_r55, 0, 211, -22.3364F, -28.6328F, 6.8017F, 43, 7, 0, 0.0F));
        cube_r55.cubeList.add(new ModelBox(cube_r55, 101, 110, -22.3364F, -21.6328F, 2.3017F, 43, 7, 9, 0.0F));

        cube_r56 = new ModelRenderer(this);
        cube_r56.setRotationPoint(-25.5F, 0.0F, -4.5F);
        right_bone3.addChild(cube_r56);
        setRotationAngle(cube_r56, 0.0404F, 0.0774F, 0.4815F);
        cube_r56.cubeList.add(new ModelBox(cube_r56, 29, 151, -16.4254F, -24.3659F, 3.2017F, 6, 32, 7, 0.0F));

        cube_r57 = new ModelRenderer(this);
        cube_r57.setRotationPoint(-25.5F, 0.0F, -4.5F);
        right_bone3.addChild(cube_r57);
        setRotationAngle(cube_r57, 0.0076F, 0.0869F, 0.0876F);
        cube_r57.cubeList.add(new ModelBox(cube_r57, 87, 164, -19.4333F, 1.0221F, 4.1017F, 5, 28, 5, 0.0F));

        left_bone2 = new ModelRenderer(this);
        left_bone2.setRotationPoint(13.7547F, 0.8681F, -5.3363F);
        root.addChild(left_bone2);

        cube_r58 = new ModelRenderer(this);
        cube_r58.setRotationPoint(37.5933F, -19.3441F, -5.5053F);
        left_bone2.addChild(cube_r58);
        setRotationAngle(cube_r58, 0.1162F, 0.0603F, 1.0943F);
        cube_r58.cubeList.add(new ModelBox(cube_r58, 0, 204, -21.5F, -3.2F, 0.0F, 43, 7, 0, 0.0F));

        cube_r59 = new ModelRenderer(this);
        cube_r59.setRotationPoint(26.0F, -0.5F, 4.5F);
        left_bone2.addChild(cube_r59);
        setRotationAngle(cube_r59, -0.0968F, 0.0883F, -0.8333F);
        cube_r59.cubeList.add(new ModelBox(cube_r59, 0, 204, -20.7047F, -28.6776F, -8.4065F, 43, 7, 0, 0.0F));
        cube_r59.cubeList.add(new ModelBox(cube_r59, 101, 110, -20.7047F, -21.6776F, -12.9065F, 43, 7, 9, 0.0F));

        cube_r60 = new ModelRenderer(this);
        cube_r60.setRotationPoint(26.0F, -0.5F, 4.5F);
        left_bone2.addChild(cube_r60);
        setRotationAngle(cube_r60, -0.0607F, 0.116F, -0.4835F);
        cube_r60.cubeList.add(new ModelBox(cube_r60, 29, 151, 10.3715F, -24.394F, -12.0065F, 6, 32, 7, 0.0F));

        cube_r61 = new ModelRenderer(this);
        cube_r61.setRotationPoint(26.0F, -0.5F, 4.5F);
        left_bone2.addChild(cube_r61);
        setRotationAngle(cube_r61, -0.0115F, 0.1304F, -0.088F);
        cube_r61.cubeList.add(new ModelBox(cube_r61, 87, 164, 14.3727F, 1.0168F, -11.1065F, 5, 28, 5, 0.0F));

        right_bone2 = new ModelRenderer(this);
        right_bone2.setRotationPoint(-13.7547F, 0.8681F, -5.3363F);
        root.addChild(right_bone2);

        cube_r62 = new ModelRenderer(this);
        cube_r62.setRotationPoint(-37.5933F, -19.3441F, -5.5053F);
        right_bone2.addChild(cube_r62);
        setRotationAngle(cube_r62, 0.1162F, -0.0603F, -1.0943F);
        cube_r62.cubeList.add(new ModelBox(cube_r62, 0, 204, -21.5F, -3.2F, 0.0F, 43, 7, 0, 0.0F));

        cube_r63 = new ModelRenderer(this);
        cube_r63.setRotationPoint(-26.0F, -0.5F, 4.5F);
        right_bone2.addChild(cube_r63);
        setRotationAngle(cube_r63, -0.0968F, -0.0883F, 0.8333F);
        cube_r63.cubeList.add(new ModelBox(cube_r63, 0, 204, -22.2953F, -28.6776F, -8.4065F, 43, 7, 0, 0.0F));
        cube_r63.cubeList.add(new ModelBox(cube_r63, 101, 110, -22.2953F, -21.6776F, -12.9065F, 43, 7, 9, 0.0F));

        cube_r64 = new ModelRenderer(this);
        cube_r64.setRotationPoint(-26.0F, -0.5F, 4.5F);
        right_bone2.addChild(cube_r64);
        setRotationAngle(cube_r64, -0.0607F, -0.116F, 0.4835F);
        cube_r64.cubeList.add(new ModelBox(cube_r64, 29, 151, -16.3715F, -24.394F, -12.0065F, 6, 32, 7, 0.0F));

        cube_r65 = new ModelRenderer(this);
        cube_r65.setRotationPoint(-26.0F, -0.5F, 4.5F);
        right_bone2.addChild(cube_r65);
        setRotationAngle(cube_r65, -0.0115F, -0.1304F, 0.088F);
        cube_r65.cubeList.add(new ModelBox(cube_r65, 87, 164, -19.3727F, 1.0168F, -11.1065F, 5, 28, 5, 0.0F));

        left_bone1 = new ModelRenderer(this);
        left_bone1.setRotationPoint(15.2392F, -1.3374F, -11.6323F);
        root.addChild(left_bone1);

        cube_r66 = new ModelRenderer(this);
        cube_r66.setRotationPoint(29.0958F, -17.5128F, -21.1619F);
        left_bone1.addChild(cube_r66);
        setRotationAngle(cube_r66, 0.5558F, 0.268F, 1.1677F);
        cube_r66.cubeList.add(new ModelBox(cube_r66, 127, 92, -23.0F, -3.25F, 0.0F, 43, 7, 0, 0.0F));

        cube_r67 = new ModelRenderer(this);
        cube_r67.setRotationPoint(24.5155F, 1.7054F, 10.2961F);
        left_bone1.addChild(cube_r67);
        setRotationAngle(cube_r67, -0.4766F, 0.3979F, -0.9269F);
        cube_r67.cubeList.add(new ModelBox(cube_r67, 127, 92, -14.5357F, -21.9454F, -23.1417F, 43, 7, 0, 0.0F));
        cube_r67.cubeList.add(new ModelBox(cube_r67, 101, 110, -14.5357F, -14.9454F, -27.1417F, 43, 7, 9, 0.0F));

        cube_r68 = new ModelRenderer(this);
        cube_r68.setRotationPoint(24.5155F, 1.7054F, 10.2961F);
        left_bone1.addChild(cube_r68);
        setRotationAngle(cube_r68, -0.061F, 0.6082F, -0.1064F);
        cube_r68.cubeList.add(new ModelBox(cube_r68, 87, 164, 23.4692F, 1.8126F, -25.3417F, 5, 28, 5, 0.0F));

        cube_r69 = new ModelRenderer(this);
        cube_r69.setRotationPoint(24.5155F, 1.7054F, 10.2961F);
        left_bone1.addChild(cube_r69);
        setRotationAngle(cube_r69, -0.3127F, 0.5338F, -0.5661F);
        cube_r69.cubeList.add(new ModelBox(cube_r69, 29, 151, 18.471F, -20.1776F, -26.2417F, 6, 32, 7, 0.0F));

        right_bone1 = new ModelRenderer(this);
        right_bone1.setRotationPoint(-15.2392F, -1.3374F, -11.6323F);
        root.addChild(right_bone1);

        cube_r70 = new ModelRenderer(this);
        cube_r70.setRotationPoint(-29.0958F, -17.5128F, -21.1619F);
        right_bone1.addChild(cube_r70);
        setRotationAngle(cube_r70, 0.5558F, -0.268F, -1.1677F);
        cube_r70.cubeList.add(new ModelBox(cube_r70, 127, 92, -20.0F, -3.25F, 0.0F, 43, 7, 0, 0.0F));

        cube_r71 = new ModelRenderer(this);
        cube_r71.setRotationPoint(-24.5155F, 1.7054F, 10.2961F);
        right_bone1.addChild(cube_r71);
        setRotationAngle(cube_r71, -0.4766F, -0.3979F, 0.9269F);
        cube_r71.cubeList.add(new ModelBox(cube_r71, 127, 92, -28.4643F, -21.9454F, -23.1417F, 43, 7, 0, 0.0F));
        cube_r71.cubeList.add(new ModelBox(cube_r71, 101, 110, -28.4643F, -14.9454F, -27.1417F, 43, 7, 9, 0.0F));

        cube_r72 = new ModelRenderer(this);
        cube_r72.setRotationPoint(-24.5155F, 1.7054F, 10.2961F);
        right_bone1.addChild(cube_r72);
        setRotationAngle(cube_r72, -0.061F, -0.6082F, 0.1064F);
        cube_r72.cubeList.add(new ModelBox(cube_r72, 87, 164, -28.4692F, 1.8126F, -25.3417F, 5, 28, 5, 0.0F));

        cube_r73 = new ModelRenderer(this);
        cube_r73.setRotationPoint(-24.5155F, 1.7054F, 10.2961F);
        right_bone1.addChild(cube_r73);
        setRotationAngle(cube_r73, -0.3127F, -0.5338F, 0.5661F);
        cube_r73.cubeList.add(new ModelBox(cube_r73, 29, 151, -24.471F, -20.1776F, -26.2417F, 6, 32, 7, 0.0F));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        root.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
