package com.tortugagot.togcore.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ToffyCrateSweetToothModel extends ModelBase {

    private final ModelRenderer base;
    private final ModelRenderer flat_r1;
    private final ModelRenderer flat_r2;
    private final ModelRenderer tongue_base;
    private final ModelRenderer tongue1;
    private final ModelRenderer tongue2;
    private final ModelRenderer tongue_end;
    private final ModelRenderer claw_right;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer claw_left;
    private final ModelRenderer cube_r5;
    private final ModelRenderer cube_r6;
    private final ModelRenderer cube_r7;
    private final ModelRenderer cube_r8;
    private final ModelRenderer top;
    private final ModelRenderer horn_r1;
    private final ModelRenderer horn_r2;
    private final ModelRenderer horn_r3;
    private final ModelRenderer horn_r4;
    private final ModelRenderer horn_r5;
    private final ModelRenderer cube_r9;
    private final ModelRenderer cube_r10;
    private final ModelRenderer cube_r11;
    private final ModelRenderer cube_r12;
    private final ModelRenderer cube_r13;
    private final ModelRenderer eye_r1;
    private final ModelRenderer eye_r2;
    private final ModelRenderer eye_r3;
    private final ModelRenderer eye_r4;
    private final ModelRenderer cube_r14;
    private final ModelRenderer cube_r15;
    private final ModelRenderer horn_r6;
    private final ModelRenderer cube_r16;
    private final ModelRenderer cube_r17;
    private final ModelRenderer cube_r18;
    private final ModelRenderer cube_r19;
    private final ModelRenderer cube_r20;
    private final ModelRenderer cube_r21;
    private final ModelRenderer cube_r22;
    private final ModelRenderer cube_r23;
    private final ModelRenderer cube_r24;
    private final ModelRenderer cube_r25;
    private final ModelRenderer cube_r26;
    private final ModelRenderer cube_r27;
    private final ModelRenderer cube_r28;
    private final ModelRenderer cube_r29;
    private final ModelRenderer cube_r30;
    private final ModelRenderer cube_r31;
    private final ModelRenderer cube_r32;
    private final ModelRenderer cube_r33;
    private final ModelRenderer candy;

    private static float prevLimbSwing = 0F;

    public ToffyCrateSweetToothModel() {
        textureWidth = 128;
        textureHeight = 128;

        base = new ModelRenderer(this);
        base.setRotationPoint(0.0F, 21.0F, 1.7024F);
        base.cubeList.add(new ModelBox(base, 4, 85, -6.5F, 1.0F, -6.2024F, 13, 2, 9, 0.0F));
        base.cubeList.add(new ModelBox(base, 101, 91, -6.5F, -4.0F, -6.2024F, 2, 5, 2, 0.0F));
        base.cubeList.add(new ModelBox(base, 113, 54, -6.5F, -4.0F, -4.2024F, 2, 1, 5, 0.0F));
        base.cubeList.add(new ModelBox(base, 104, 53, 4.5F, -4.0F, -4.2024F, 2, 1, 5, 0.0F));
        base.cubeList.add(new ModelBox(base, 102, 114, -4.5F, -4.0F, 1.7976F, 9, 1, 1, 0.0F));
        base.cubeList.add(new ModelBox(base, 106, 46, -6.5F, -4.0F, 0.7976F, 2, 5, 2, 0.0F));
        base.cubeList.add(new ModelBox(base, 119, 62, 4.5F, -4.0F, -6.2024F, 2, 5, 2, 0.0F));
        base.cubeList.add(new ModelBox(base, 37, 71, -6.0F, -3.0F, -5.7024F, 12, 5, 8, 0.0F));
        base.cubeList.add(new ModelBox(base, 107, 62, 4.5F, -4.0F, 0.7976F, 2, 5, 2, 0.0F));

        flat_r1 = new ModelRenderer(this);
        flat_r1.setRotationPoint(6.499F, 1.0F, 2.7976F);
        base.addChild(flat_r1);
        setRotationAngle(flat_r1, 0.0F, 0.3927F, 0.0F);
        flat_r1.cubeList.add(new ModelBox(flat_r1, 12, 100, 0.0F, -2.0F, 0.0F, 0, 4, 5, 0.0F));

        flat_r2 = new ModelRenderer(this);
        flat_r2.setRotationPoint(-6.499F, 1.0F, 2.7976F);
        base.addChild(flat_r2);
        setRotationAngle(flat_r2, 0.0F, -0.3927F, 0.0F);
        flat_r2.cubeList.add(new ModelBox(flat_r2, 12, 96, 0.0F, -2.0F, 0.0F, 0, 4, 5, 0.0F));

        tongue_base = new ModelRenderer(this);
        tongue_base.setRotationPoint(0.0F, -3.0F, -6.7024F);
        base.addChild(tongue_base);
        setRotationAngle(tongue_base, -0.1309F, 0.0F, 0.0F);
        tongue_base.cubeList.add(new ModelBox(tongue_base, 82, 110, -4.0F, -1.0F, -1.0F, 8, 4, 2, 0.0F));

        tongue1 = new ModelRenderer(this);
        tongue1.setRotationPoint(0.0F, 3.0833F, 0.0F);
        tongue_base.addChild(tongue1);
        setRotationAngle(tongue1, -0.2618F, 0.0F, 0.0F);
        tongue1.cubeList.add(new ModelBox(tongue1, 83, 116, -3.0F, -0.0833F, -1.0F, 6, 3, 2, 0.0F));

        tongue2 = new ModelRenderer(this);
        tongue2.setRotationPoint(0.0F, 2.9167F, 0.0F);
        tongue1.addChild(tongue2);
        setRotationAngle(tongue2, -1.8326F, 0.0F, 0.0F);
        tongue2.cubeList.add(new ModelBox(tongue2, 73, 118, -2.0F, 0.0F, -0.5F, 4, 4, 1, 0.0F));

        tongue_end = new ModelRenderer(this);
        tongue_end.setRotationPoint(0.0F, 4.0F, 0.0F);
        tongue2.addChild(tongue_end);
        setRotationAngle(tongue_end, 1.8762F, 0.0F, 0.0F);
        tongue_end.cubeList.add(new ModelBox(tongue_end, 104, 118, -1.0F, 0.0F, -0.5F, 2, 3, 1, 0.0F));

        claw_right = new ModelRenderer(this);
        claw_right.setRotationPoint(-6.5F, 2.025F, -6.2024F);
        base.addChild(claw_right);
        claw_right.cubeList.add(new ModelBox(claw_right, 13, 115, -1.5F, -1.0F, -1.5F, 2, 2, 2, 0.0F));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(0.0F, -1.5F, 0.0F);
        claw_right.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.7854F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 3, 119, -0.5F, -0.5F, -1.0F, 1, 1, 1, 0.0F));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(-1.7988F, 0.525F, 0.6009F);
        claw_right.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, -0.3927F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 37, 120, -0.625F, -0.5F, -0.775F, 1, 1, 1, 0.0F));

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(0.409F, 0.525F, 0.409F);
        claw_right.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 0.3927F, 0.0F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 23, 120, 0.1676F, -0.5F, -2.6131F, 1, 1, 1, 0.0F));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(-1.591F, 0.525F, -1.591F);
        claw_right.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.7854F, 0.0F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 23, 118, -0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F));

        claw_left = new ModelRenderer(this);
        claw_left.setRotationPoint(6.5F, 2.025F, -6.2024F);
        base.addChild(claw_left);
        claw_left.cubeList.add(new ModelBox(claw_left, 7, 117, -0.5F, -1.0F, -1.5F, 2, 2, 2, 0.0F));

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(0.0F, -1.5F, 0.0F);
        claw_left.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, -0.7854F, 0.0F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 44, 119, -0.5F, -0.5F, -1.0F, 1, 1, 1, 0.0F));

        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(1.7988F, 0.525F, 0.6009F);
        claw_left.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 0.3927F, 0.0F);
        cube_r6.cubeList.add(new ModelBox(cube_r6, 1, 117, -0.375F, -0.5F, -0.775F, 1, 1, 1, 0.0F));

        cube_r7 = new ModelRenderer(this);
        cube_r7.setRotationPoint(-0.409F, 0.525F, 0.409F);
        claw_left.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, -0.3927F, 0.0F);
        cube_r7.cubeList.add(new ModelBox(cube_r7, 40, 119, -1.1676F, -0.5F, -2.6131F, 1, 1, 1, 0.0F));

        cube_r8 = new ModelRenderer(this);
        cube_r8.setRotationPoint(1.591F, 0.525F, -1.591F);
        claw_left.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.0F, -0.7854F, 0.0F);
        cube_r8.cubeList.add(new ModelBox(cube_r8, 5, 117, -0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F));

        top = new ModelRenderer(this);
        top.setRotationPoint(0.0F, -3.75F, 3.7976F);
        base.addChild(top);
        top.cubeList.add(new ModelBox(top, 100, 107, -6.0F, -2.25F, -12.5F, 12, 3, 2, 0.0F));
        top.cubeList.add(new ModelBox(top, 100, 102, -6.0F, -2.25F, -0.5F, 12, 3, 2, 0.0F));
        top.cubeList.add(new ModelBox(top, 106, 60, -8.525F, -0.2542F, -9.9937F, 2, 2, 9, 0.0F));
        top.cubeList.add(new ModelBox(top, 106, 71, 6.475F, -0.2542F, -9.9937F, 2, 2, 9, 0.0F));
        top.cubeList.add(new ModelBox(top, 0, 102, -8.0F, -4.25F, -9.5F, 1, 5, 8, 0.0F));
        top.cubeList.add(new ModelBox(top, 22, 96, 7.0F, -4.25F, -9.5F, 1, 5, 8, 0.0F));
        top.cubeList.add(new ModelBox(top, 0, 75, -7.0F, -0.2956F, -10.1851F, 14, 1, 9, 0.0F));

        horn_r1 = new ModelRenderer(this);
        horn_r1.setRotationPoint(5.0F, -7.75F, -2.0F);
        top.addChild(horn_r1);
        setRotationAngle(horn_r1, 0.0F, 0.0F, 0.7854F);
        horn_r1.cubeList.add(new ModelBox(horn_r1, 12, 119, -1.0F, -2.5F, -2.75F, 4, 6, 3, 0.0F));

        horn_r2 = new ModelRenderer(this);
        horn_r2.setRotationPoint(6.3891F, -10.0776F, -0.8699F);
        top.addChild(horn_r2);
        setRotationAngle(horn_r2, -0.3927F, 0.0F, 0.0F);
        horn_r2.cubeList.add(new ModelBox(horn_r2, 40, 121, 0.0F, -0.5F, -2.75F, 3, 4, 3, 0.0F));
        horn_r2.cubeList.add(new ModelBox(horn_r2, 0, 121, -15.7782F, -0.5F, -2.75F, 3, 4, 3, 0.0F));

        horn_r3 = new ModelRenderer(this);
        horn_r3.setRotationPoint(6.4261F, -12.2781F, -0.4693F);
        top.addChild(horn_r3);
        setRotationAngle(horn_r3, 0.0F, 0.0F, 0.3927F);
        horn_r3.cubeList.add(new ModelBox(horn_r3, 52, 123, -2.0F, -0.5F, -2.75F, 5, 2, 3, 0.0F));

        horn_r4 = new ModelRenderer(this);
        horn_r4.setRotationPoint(8.7697F, -14.0958F, 0.5127F);
        top.addChild(horn_r4);
        setRotationAngle(horn_r4, 0.3927F, 0.0F, 0.0F);
        horn_r4.cubeList.add(new ModelBox(horn_r4, 57, 119, -5.0F, -0.5F, -2.75F, 1, 1, 3, 0.0F));
        horn_r4.cubeList.add(new ModelBox(horn_r4, 49, 119, -13.5394F, -0.5F, -2.75F, 1, 1, 3, 0.0F));

        horn_r5 = new ModelRenderer(this);
        horn_r5.setRotationPoint(-6.4261F, -12.2781F, -0.4693F);
        top.addChild(horn_r5);
        setRotationAngle(horn_r5, 0.0F, 0.0F, -0.3927F);
        horn_r5.cubeList.add(new ModelBox(horn_r5, 68, 123, -3.0F, -0.5F, -2.75F, 5, 2, 3, 0.0F));

        cube_r9 = new ModelRenderer(this);
        cube_r9.setRotationPoint(0.0F, -1.019F, 1.0957F);
        top.addChild(cube_r9);
        setRotationAngle(cube_r9, -0.7854F, 0.0F, 0.0F);
        cube_r9.cubeList.add(new ModelBox(cube_r9, 94, 98, -7.5F, 0.0F, -1.0F, 15, 2, 2, 0.0F));

        cube_r10 = new ModelRenderer(this);
        cube_r10.setRotationPoint(8.0F, -1.7912F, -10.6934F);
        top.addChild(cube_r10);
        setRotationAngle(cube_r10, -0.3927F, 0.0F, 0.0F);
        cube_r10.cubeList.add(new ModelBox(cube_r10, 120, 5, -1.0F, -3.0F, 0.0F, 2, 3, 2, 0.0F));
        cube_r10.cubeList.add(new ModelBox(cube_r10, 120, 0, -1.5F, 0.0F, 0.0F, 2, 3, 2, 0.0F));
        cube_r10.cubeList.add(new ModelBox(cube_r10, 110, 35, -16.5F, 0.0F, 0.0F, 2, 3, 2, 0.0F));
        cube_r10.cubeList.add(new ModelBox(cube_r10, 110, 27, -17.0F, -3.0F, 0.0F, 2, 3, 2, 0.0F));

        cube_r11 = new ModelRenderer(this);
        cube_r11.setRotationPoint(10.0F, -5.75F, -5.5F);
        top.addChild(cube_r11);
        setRotationAngle(cube_r11, 0.0F, 0.0F, -0.3927F);
        cube_r11.cubeList.add(new ModelBox(cube_r11, 116, 16, -2.0F, -0.75F, -1.0F, 4, 2, 2, 0.0F));

        cube_r12 = new ModelRenderer(this);
        cube_r12.setRotationPoint(8.0F, -1.7912F, -0.3066F);
        top.addChild(cube_r12);
        setRotationAngle(cube_r12, 0.3927F, 0.0F, 0.0F);
        cube_r12.cubeList.add(new ModelBox(cube_r12, 104, 0, -1.5F, 0.0F, -2.0F, 2, 3, 2, 0.0F));
        cube_r12.cubeList.add(new ModelBox(cube_r12, 85, 85, -14.5F, 2.0F, -2.0F, 13, 1, 2, 0.0F));
        cube_r12.cubeList.add(new ModelBox(cube_r12, 112, 0, -1.0F, -3.0F, -2.0F, 2, 3, 2, 0.0F));
        cube_r12.cubeList.add(new ModelBox(cube_r12, 108, 17, -17.0F, -3.0F, -2.0F, 2, 3, 2, 0.0F));
        cube_r12.cubeList.add(new ModelBox(cube_r12, 108, 12, -16.5F, 0.0F, -2.0F, 2, 3, 2, 0.0F));

        cube_r13 = new ModelRenderer(this);
        cube_r13.setRotationPoint(3.8787F, 0.75F, -12.5F);
        top.addChild(cube_r13);
        setRotationAngle(cube_r13, 0.0F, 0.0F, 0.7854F);
        cube_r13.cubeList.add(new ModelBox(cube_r13, 121, 122, -4.25F, 1.25F, 0.25F, 2, 3, 0, 0.0F));
        cube_r13.cubeList.add(new ModelBox(cube_r13, 74, 114, -2.75F, -1.75F, 0.25F, 4, 4, 0, 0.0F));

        eye_r1 = new ModelRenderer(this);
        eye_r1.setRotationPoint(4.4F, 0.55F, -12.5F);
        top.addChild(eye_r1);
        setRotationAngle(eye_r1, 0.0F, 0.0F, -0.3927F);
        eye_r1.cubeList.add(new ModelBox(eye_r1, 84, 121, 1.19F, -2.52F, -0.5F, 1, 1, 1, 0.0F));

        eye_r2 = new ModelRenderer(this);
        eye_r2.setRotationPoint(3.8787F, 0.75F, -12.5F);
        top.addChild(eye_r2);
        setRotationAngle(eye_r2, 0.0F, 0.0F, -0.3927F);
        eye_r2.cubeList.add(new ModelBox(eye_r2, 96, 122, -0.25F, -2.5F, -0.5F, 2, 2, 1, 0.0F));

        eye_r3 = new ModelRenderer(this);
        eye_r3.setRotationPoint(-3.8787F, 0.8F, -12.5F);
        top.addChild(eye_r3);
        setRotationAngle(eye_r3, 0.0F, 0.0F, 0.3927F);
        eye_r3.cubeList.add(new ModelBox(eye_r3, 84, 123, -2.7713F, -2.55F, -0.5F, 1, 1, 1, 0.0F));

        eye_r4 = new ModelRenderer(this);
        eye_r4.setRotationPoint(-3.8787F, 0.75F, -12.5F);
        top.addChild(eye_r4);
        setRotationAngle(eye_r4, 0.0F, 0.0F, 0.3927F);
        eye_r4.cubeList.add(new ModelBox(eye_r4, 21, 115, -1.75F, -2.5F, -0.5F, 2, 2, 1, 0.0F));

        cube_r14 = new ModelRenderer(this);
        cube_r14.setRotationPoint(-3.8787F, 0.75F, -12.5F);
        top.addChild(cube_r14);
        setRotationAngle(cube_r14, 0.0F, 0.0F, -0.7854F);
        cube_r14.cubeList.add(new ModelBox(cube_r14, 109, 123, -1.5F, -1.5F, 0.25F, 2, 2, 0, 0.0F));

        cube_r15 = new ModelRenderer(this);
        cube_r15.setRotationPoint(-10.0F, -5.75F, -5.5F);
        top.addChild(cube_r15);
        setRotationAngle(cube_r15, 0.0F, 0.0F, 0.3927F);
        cube_r15.cubeList.add(new ModelBox(cube_r15, 104, 22, -2.0F, -0.75F, -1.0F, 4, 2, 2, 0.0F));

        horn_r6 = new ModelRenderer(this);
        horn_r6.setRotationPoint(-5.0F, -7.75F, -2.0F);
        top.addChild(horn_r6);
        setRotationAngle(horn_r6, 0.0F, 0.0F, -0.7854F);
        horn_r6.cubeList.add(new ModelBox(horn_r6, 26, 119, -3.0F, -2.5F, -2.75F, 4, 6, 3, 0.0F));

        cube_r16 = new ModelRenderer(this);
        cube_r16.setRotationPoint(-8.0F, -3.3287F, -3.0482F);
        top.addChild(cube_r16);
        setRotationAngle(cube_r16, -0.3927F, 0.0F, 0.0F);
        cube_r16.cubeList.add(new ModelBox(cube_r16, 114, 42, -2.0F, -1.75F, -2.5F, 2, 2, 4, 0.0F));
        cube_r16.cubeList.add(new ModelBox(cube_r16, 116, 20, 16.0F, -1.75F, -2.5F, 2, 2, 4, 0.0F));

        cube_r17 = new ModelRenderer(this);
        cube_r17.setRotationPoint(8.0F, -3.3287F, -7.9518F);
        top.addChild(cube_r17);
        setRotationAngle(cube_r17, 0.3927F, 0.0F, 0.0F);
        cube_r17.cubeList.add(new ModelBox(cube_r17, 116, 10, 0.0F, -1.75F, -1.0F, 2, 2, 4, 0.0F));
        cube_r17.cubeList.add(new ModelBox(cube_r17, 114, 48, -18.0F, -1.75F, -1.0F, 2, 2, 4, 0.0F));

        cube_r18 = new ModelRenderer(this);
        cube_r18.setRotationPoint(0.0F, -1.6846F, -2.8869F);
        top.addChild(cube_r18);
        setRotationAngle(cube_r18, 0.3927F, 0.0F, 0.0F);
        cube_r18.cubeList.add(new ModelBox(cube_r18, 40, 103, -8.0F, -2.0F, 1.0F, 16, 5, 1, 0.0F));

        cube_r19 = new ModelRenderer(this);
        cube_r19.setRotationPoint(0.0F, -3.2153F, -4.4176F);
        top.addChild(cube_r19);
        setRotationAngle(cube_r19, -0.3927F, 0.0F, 0.0F);
        cube_r19.cubeList.add(new ModelBox(cube_r19, 32, 98, -9.0F, -2.0F, -2.0F, 18, 1, 4, 0.0F));

        cube_r20 = new ModelRenderer(this);
        cube_r20.setRotationPoint(0.0F, -3.2153F, -6.5824F);
        top.addChild(cube_r20);
        setRotationAngle(cube_r20, 0.3927F, 0.0F, 0.0F);
        cube_r20.cubeList.add(new ModelBox(cube_r20, 39, 87, -9.0F, -2.0F, -2.0F, 18, 1, 4, 0.0F));

        cube_r21 = new ModelRenderer(this);
        cube_r21.setRotationPoint(0.0F, -1.6846F, -8.1131F);
        top.addChild(cube_r21);
        setRotationAngle(cube_r21, -0.3927F, 0.0F, 0.0F);
        cube_r21.cubeList.add(new ModelBox(cube_r21, 18, 109, -8.0F, -2.0F, -2.0F, 16, 5, 1, 0.0F));

        cube_r22 = new ModelRenderer(this);
        cube_r22.setRotationPoint(0.0F, -8.1392F, -6.9651F);
        top.addChild(cube_r22);
        setRotationAngle(cube_r22, 0.7854F, 0.0F, 0.0F);
        cube_r22.cubeList.add(new ModelBox(cube_r22, 100, 122, -1.0F, -1.0F, -4.0F, 3, 3, 3, 0.0F));

        cube_r23 = new ModelRenderer(this);
        cube_r23.setRotationPoint(0.0F, -8.1392F, -6.9651F);
        top.addChild(cube_r23);
        setRotationAngle(cube_r23, 0.0F, 0.3927F, 0.0F);
        cube_r23.cubeList.add(new ModelBox(cube_r23, 110, 116, -5.0F, -0.5F, -1.0F, 4, 3, 3, 0.0F));
        cube_r23.cubeList.add(new ModelBox(cube_r23, 65, 120, -1.0F, -2.0F, -1.0F, 2, 1, 2, 0.0F));

        cube_r24 = new ModelRenderer(this);
        cube_r24.setRotationPoint(3.4619F, -7.8305F, -6.4651F);
        top.addChild(cube_r24);
        setRotationAngle(cube_r24, 0.0F, 0.0F, -0.3927F);
        cube_r24.cubeList.add(new ModelBox(cube_r24, 112, 122, -2.0F, -1.0F, -1.5F, 3, 3, 3, 0.0F));

        cube_r25 = new ModelRenderer(this);
        cube_r25.setRotationPoint(0.0F, -8.1392F, -6.9651F);
        top.addChild(cube_r25);
        setRotationAngle(cube_r25, 0.0F, 0.7854F, 0.0F);
        cube_r25.cubeList.add(new ModelBox(cube_r25, 84, 121, -2.0F, -1.0F, -2.0F, 4, 3, 4, 0.0F));

        cube_r26 = new ModelRenderer(this);
        cube_r26.setRotationPoint(0.0F, -3.6846F, -2.8869F);
        top.addChild(cube_r26);
        setRotationAngle(cube_r26, 0.3927F, 0.0F, 0.0F);
        cube_r26.cubeList.add(new ModelBox(cube_r26, 74, 102, -5.0F, -2.0F, 0.0F, 10, 5, 2, 0.0F));

        cube_r27 = new ModelRenderer(this);
        cube_r27.setRotationPoint(0.0F, -5.2153F, -4.4176F);
        top.addChild(cube_r27);
        setRotationAngle(cube_r27, -0.3927F, 0.0F, 0.0F);
        cube_r27.cubeList.add(new ModelBox(cube_r27, 44, 92, -5.0F, -2.0F, -2.0F, 10, 2, 4, 0.0F));

        cube_r28 = new ModelRenderer(this);
        cube_r28.setRotationPoint(0.0F, -5.2153F, -6.5824F);
        top.addChild(cube_r28);
        setRotationAngle(cube_r28, 0.3927F, 0.0F, 0.0F);
        cube_r28.cubeList.add(new ModelBox(cube_r28, 82, 79, -5.0F, -2.0F, -2.0F, 10, 2, 4, 0.0F));

        cube_r29 = new ModelRenderer(this);
        cube_r29.setRotationPoint(0.0F, -3.6846F, -8.1131F);
        top.addChild(cube_r29);
        setRotationAngle(cube_r29, -0.3927F, 0.0F, 0.0F);
        cube_r29.cubeList.add(new ModelBox(cube_r29, 72, 93, -5.0F, -2.0F, -2.0F, 10, 5, 2, 0.0F));

        cube_r30 = new ModelRenderer(this);
        cube_r30.setRotationPoint(5.0F, -2.7912F, 0.1934F);
        top.addChild(cube_r30);
        setRotationAngle(cube_r30, 0.3927F, 0.0F, 0.0F);
        cube_r30.cubeList.add(new ModelBox(cube_r30, 112, 5, -1.0F, -4.0F, -1.0F, 2, 5, 2, 0.0F));
        cube_r30.cubeList.add(new ModelBox(cube_r30, 104, 5, -11.0F, -4.0F, -1.0F, 2, 5, 2, 0.0F));

        cube_r31 = new ModelRenderer(this);
        cube_r31.setRotationPoint(5.0F, -2.7912F, -11.1934F);
        top.addChild(cube_r31);
        setRotationAngle(cube_r31, -0.3927F, 0.0F, 0.0F);
        cube_r31.cubeList.add(new ModelBox(cube_r31, 107, 73, -1.0F, -4.0F, -1.0F, 2, 5, 2, 0.0F));
        cube_r31.cubeList.add(new ModelBox(cube_r31, 119, 73, -11.0F, -4.0F, -1.0F, 2, 5, 2, 0.0F));

        cube_r32 = new ModelRenderer(this);
        cube_r32.setRotationPoint(-5.025F, -1.2469F, -3.8247F);
        top.addChild(cube_r32);
        setRotationAngle(cube_r32, -0.3927F, 0.0F, 0.0F);
        cube_r32.cubeList.add(new ModelBox(cube_r32, 112, 34, -1.0F, -6.5F, -4.5F, 2, 2, 6, 0.0F));
        cube_r32.cubeList.add(new ModelBox(cube_r32, 112, 26, 9.0F, -6.5F, -4.5F, 2, 2, 6, 0.0F));

        cube_r33 = new ModelRenderer(this);
        cube_r33.setRotationPoint(-5.025F, -1.2469F, -7.1753F);
        top.addChild(cube_r33);
        setRotationAngle(cube_r33, 0.3927F, 0.0F, 0.0F);
        cube_r33.cubeList.add(new ModelBox(cube_r33, 109, 82, 9.0F, -6.5F, -1.0F, 2, 2, 6, 0.0F));
        cube_r33.cubeList.add(new ModelBox(cube_r33, 109, 90, -1.0F, -6.5F, -1.0F, 2, 2, 6, 0.0F));

        candy = new ModelRenderer(this);
        candy.setRotationPoint(0.0F, 20.5F, -0.5F);
        setRotationAngle(candy, -3.1416F, -1.5708F, -1.5708F);
        candy.cubeList.add(new ModelBox(candy, 56, 116, -0.5F, -0.5F, -0.5F, 1, 5, 1, 0.0F));
        candy.cubeList.add(new ModelBox(candy, 52, 109, -1.5F, -3.5F, -1.5F, 3, 3, 3, 0.0F));
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
        tongue1.rotateAngleX = (float) Math.toRadians(-50);
        tongue1.rotateAngleX += MathHelper.sin(ageInTicks * (float) Math.toRadians(5)) * (float) Math.toRadians(4);
        tongue2.rotateAngleX = (float) Math.toRadians(-40);
        tongue2.rotateAngleX += -MathHelper.sin(ageInTicks * (float) Math.toRadians(5)) * (float) Math.toRadians(3);
        tongue_end.rotateAngleX = (float) Math.toRadians(-15);

        base.rotateAngleY = MathHelper.sin(limbSwing * 0.9f) * (limbSwingAmount * 0.2f);
        base.rotateAngleZ = MathHelper.sin(limbSwing * 0.9f) * (limbSwingAmount * 0.2f);
        base.rotateAngleY = MathHelper.sin(limbSwing * 1.5f) * 0.3f * limbSwingAmount;
        claw_left.rotateAngleY = MathHelper.sin(limbSwing * 1.5f) * 0.9f * limbSwingAmount;
        claw_right.rotateAngleY = claw_left.rotateAngleY;

        if (limbSwing == prevLimbSwing) {
            top.rotateAngleX = MathHelper.sin(ageInTicks * (float) Math.toRadians(5)) * (float) Math.toRadians(5);
        } else {
            top.rotateAngleX = (float) Math.toRadians(-50);
            top.rotateAngleX += MathHelper.sin(ageInTicks * (float) Math.toRadians(5)) * (float) Math.toRadians(5);
        }
        prevLimbSwing = limbSwing;
    }

}
