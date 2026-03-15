package com.tortugagot.togcore.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class KnightOnHorseModel extends ModelBase {

    private final ModelRenderer horse_body;
    private final ModelRenderer mount;
    private final ModelRenderer cube_r1;
    private final ModelRenderer tail_1;
    private final ModelRenderer tail_2;
    private final ModelRenderer tail_3;
    private final ModelRenderer horse_neck;
    private final ModelRenderer cube_r2;
    private final ModelRenderer horse_head;
    private final ModelRenderer cube_r3;
    private final ModelRenderer rein;
    private final ModelRenderer front_left_leg;
    private final ModelRenderer front_left_shin;
    private final ModelRenderer front_left_foot;
    private final ModelRenderer front_right_leg;
    private final ModelRenderer front_right_shin;
    private final ModelRenderer front_right_foot;
    private final ModelRenderer front_left_leg2;
    private final ModelRenderer cube_r4;
    private final ModelRenderer front_left_shin2;
    private final ModelRenderer front_left_foot2;
    private final ModelRenderer front_right_leg2;
    private final ModelRenderer cube_r5;
    private final ModelRenderer front_right_shin2;
    private final ModelRenderer front_right_foot2;
    private final ModelRenderer torso;
    private final ModelRenderer torso_top;
    private final ModelRenderer h_head;
    private final ModelRenderer cube_r6;
    private final ModelRenderer cube_r7;
    private final ModelRenderer cube_r8;
    private final ModelRenderer cube_r9;
    private final ModelRenderer cape_1;
    private final ModelRenderer cape_2;
    private final ModelRenderer cape_3;
    private final ModelRenderer right_pauldron;
    private final ModelRenderer cube_r10;
    private final ModelRenderer left_pauldron;
    private final ModelRenderer cube_r11;
    private final ModelRenderer right_arm;
    private final ModelRenderer right_forearm;
    private final ModelRenderer cube_r12;
    private final ModelRenderer right_hand;
    private final ModelRenderer sword;
    private final ModelRenderer cube_r13;
    private final ModelRenderer cube_r14;
    private final ModelRenderer left_arm;
    private final ModelRenderer left_forearm;
    private final ModelRenderer cube_r15;
    private final ModelRenderer shield_arm;
    private final ModelRenderer cube_r16;
    private final ModelRenderer cube_r17;
    private final ModelRenderer left_hand;
    private final ModelRenderer weapon_back;
    private final ModelRenderer shield_back;
    private final ModelRenderer tabard;
    private final ModelRenderer right_leg;
    private final ModelRenderer right_shin;
    private final ModelRenderer cube_r18;
    private final ModelRenderer left_leg;
    private final ModelRenderer left_shin;
    private final ModelRenderer cube_r19;
    private final ModelRenderer sheath;
    private final ModelRenderer cube_r20;
    private final ModelRenderer cube_r21;
    private final ModelRenderer flag;
    private final ModelRenderer flag_1;
    private final ModelRenderer flag_2;
    private final ModelRenderer flag_3;

    private static float prevLimbSwing = 0F;

    public KnightOnHorseModel() {
        textureWidth = 256;
        textureHeight = 256;

        horse_body = new ModelRenderer(this);
        horse_body.setRotationPoint(0.0F, -2.0F, 11.0F);
        setRotationAngle(horse_body, -0.0436F, 0.0F, 0.0F);

        mount = new ModelRenderer(this);
        mount.setRotationPoint(0.0F, -5.0F, -4.0F);
        horse_body.addChild(mount);
        mount.cubeList.add(new ModelBox(mount, 170, 0, -6.5F, -2.5F, -18.5F, 13, 16, 30, 0.0F));
        mount.cubeList.add(new ModelBox(mount, 190, 218, -7.0F, -3.5F, -9.5F, 14, 7, 12, 0.0F));
        mount.cubeList.add(new ModelBox(mount, 196, 210, -8.0F, -5.5F, 0.5F, 16, 4, 4, 0.0F));
        mount.cubeList.add(new ModelBox(mount, 196, 70, 6.5F, 13.5F, -18.5F, 0, 5, 30, 0.0F));
        mount.cubeList.add(new ModelBox(mount, 196, 65, -6.5F, 13.5F, -18.5F, 0, 5, 30, 0.0F));
        mount.cubeList.add(new ModelBox(mount, 230, 181, -6.5F, 13.5F, -18.5F, 13, 5, 0, 0.0F));
        mount.cubeList.add(new ModelBox(mount, 56, 212, -6.0F, -2.0F, -18.0F, 12, 15, 29, 0.0F));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(0.5F, -5.25F, -8.25F);
        mount.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.3927F, 0.0F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 150, 128, -1.5F, -1.6523F, -3.5317F, 3, 2, 4, 0.0F));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 164, 128, -1.5F, 0.3477F, -2.5317F, 3, 3, 3, 0.0F));

        tail_1 = new ModelRenderer(this);
        tail_1.setRotationPoint(0.0F, -5.0019F, 7.0872F);
        horse_body.addChild(tail_1);
        setRotationAngle(tail_1, -0.7854F, 0.0F, 0.0F);
        tail_1.cubeList.add(new ModelBox(tail_1, 169, 134, -6.0F, 0.0F, 0.0F, 12, 0, 9, 0.0F));

        tail_2 = new ModelRenderer(this);
        tail_2.setRotationPoint(0.0F, 0.0F, 9.0F);
        tail_1.addChild(tail_2);
        setRotationAngle(tail_2, -0.6109F, 0.0F, 0.0F);
        tail_2.cubeList.add(new ModelBox(tail_2, 37, 125, -6.0F, 0.0F, 0.0F, 12, 0, 9, 0.0F));

        tail_3 = new ModelRenderer(this);
        tail_3.setRotationPoint(0.0F, 0.0F, 9.0F);
        tail_2.addChild(tail_3);
        tail_3.cubeList.add(new ModelBox(tail_3, -7, 124, -6.0F, 0.0F, 0.0F, 12, 0, 9, 0.0F));

        horse_neck = new ModelRenderer(this);
        horse_neck.setRotationPoint(0.0F, -4.4853F, -20.4142F);
        horse_body.addChild(horse_neck);
        setRotationAngle(horse_neck, -0.2618F, 0.0F, 0.0F);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(0.0F, 0.0F, -5.0F);
        horse_neck.addChild(cube_r2);
        setRotationAngle(cube_r2, -0.7854F, 0.0F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 202, 106, -4.0F, -5.5F, -8.0F, 8, 10, 19, 0.0F));
        cube_r2.cubeList.add(new ModelBox(cube_r2, 138, 228, -3.5F, -5.0F, -8.0F, 7, 9, 19, 0.0F));

        horse_head = new ModelRenderer(this);
        horse_head.setRotationPoint(-1.75F, -7.7383F, -10.2336F);
        horse_neck.addChild(horse_head);
        setRotationAngle(horse_head, 1.0472F, 0.0F, 0.0F);
        horse_head.cubeList.add(new ModelBox(horse_head, 190, 237, -2.25F, -5.0F, -7.25F, 8, 9, 10, 0.0F));
        horse_head.cubeList.add(new ModelBox(horse_head, 216, 135, -2.75F, -6.0F, -7.75F, 9, 10, 11, 0.0F));
        horse_head.cubeList.add(new ModelBox(horse_head, 224, 156, -2.75F, -6.0F, -14.75F, 9, 9, 7, 0.0F));
        horse_head.cubeList.add(new ModelBox(horse_head, 176, 109, 1.75F, -8.0F, -7.25F, 0, 12, 13, 0.0F));
        horse_head.cubeList.add(new ModelBox(horse_head, 226, 242, -2.25F, -5.0F, -14.25F, 8, 7, 7, 0.0F));

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(5.25F, -6.5F, -0.25F);
        horse_head.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.3927F, 0.0F, 0.0F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 36, 127, -2.5F, -3.5F, 0.0F, 5, 7, 0, 0.0F));
        cube_r3.cubeList.add(new ModelBox(cube_r3, 26, 127, -9.5F, -3.5F, 0.0F, 5, 7, 0, 0.0F));

        rein = new ModelRenderer(this);
        rein.setRotationPoint(1.5F, -1.0F, -11.0F);
        horse_head.addChild(rein);
        setRotationAngle(rein, -0.7854F, 0.0F, 0.0F);
        rein.cubeList.add(new ModelBox(rein, 210, 149, -4.5F, -2.1161F, -2.9697F, 0, 9, 23, 0.0F));
        rein.cubeList.add(new ModelBox(rein, 194, 175, 5.0F, -2.1161F, -2.9697F, 0, 9, 23, 0.0F));
        rein.cubeList.add(new ModelBox(rein, 238, 207, -4.5F, -2.1161F, 20.0303F, 9, 2, 0, 0.0F));

        front_left_leg = new ModelRenderer(this);
        front_left_leg.setRotationPoint(5.0F, 3.0F, -19.0F);
        horse_body.addChild(front_left_leg);
        setRotationAngle(front_left_leg, 0.0875F, 0.0831F, 0.0511F);
        front_left_leg.cubeList.add(new ModelBox(front_left_leg, 86, 125, -1.5F, 8.0F, -1.0F, 4, 4, 5, 0.0F));
        front_left_leg.cubeList.add(new ModelBox(front_left_leg, 27, 224, -2.0F, -4.0F, -3.0F, 5, 12, 8, 0.0F));
        front_left_leg.cubeList.add(new ModelBox(front_left_leg, 226, 8, -2.5F, -5.0F, -3.5F, 6, 13, 9, 0.0F));

        front_left_shin = new ModelRenderer(this);
        front_left_shin.setRotationPoint(0.5F, 12.0F, -1.0F);
        front_left_leg.addChild(front_left_shin);
        front_left_shin.cubeList.add(new ModelBox(front_left_shin, 120, 122, -1.5F, 0.0F, 0.0F, 3, 8, 4, 0.0F));

        front_left_foot = new ModelRenderer(this);
        front_left_foot.setRotationPoint(0.0F, 8.0F, 0.0F);
        front_left_shin.addChild(front_left_foot);
        setRotationAngle(front_left_foot, -0.0873F, 0.0F, 0.0F);
        front_left_foot.cubeList.add(new ModelBox(front_left_foot, 70, 126, -2.0F, 0.0F, 0.0F, 4, 4, 4, 0.0F));

        front_right_leg = new ModelRenderer(this);
        front_right_leg.setRotationPoint(-5.0F, 3.0F, -19.0F);
        horse_body.addChild(front_right_leg);
        setRotationAngle(front_right_leg, 0.0875F, -0.0831F, -0.0511F);
        front_right_leg.cubeList.add(new ModelBox(front_right_leg, 20, 247, -2.5F, 8.0F, -1.0F, 4, 4, 5, 0.0F));
        front_right_leg.cubeList
                .add(new ModelBox(front_right_leg, 152, 208, -3.0F, -4.0F, -3.0F, 5, 12, 8, 0.0F));
        front_right_leg.cubeList
                .add(new ModelBox(front_right_leg, 226, 73, -3.5F, -4.5F, -3.5F, 6, 13, 9, 0.0F));

        front_right_shin = new ModelRenderer(this);
        front_right_shin.setRotationPoint(-0.5F, 12.0F, -1.0F);
        front_right_leg.addChild(front_right_shin);
        front_right_shin.cubeList.add(new ModelBox(front_right_shin, 6, 244, -1.5F, 0.0F, 0.0F, 3, 8, 4, 0.0F));

        front_right_foot = new ModelRenderer(this);
        front_right_foot.setRotationPoint(0.0F, 8.0F, 0.0F);
        front_right_shin.addChild(front_right_foot);
        setRotationAngle(front_right_foot, -0.0873F, 0.0F, 0.0F);
        front_right_foot.cubeList
                .add(new ModelBox(front_right_foot, 104, 126, -2.0F, 0.0F, 0.0F, 4, 4, 4, 0.0F));

        front_left_leg2 = new ModelRenderer(this);
        front_left_leg2.setRotationPoint(5.0F, 3.0F, 3.0F);
        horse_body.addChild(front_left_leg2);
        setRotationAngle(front_left_leg2, 0.1991F, -0.2049F, 0.0743F);
        front_left_leg2.cubeList
                .add(new ModelBox(front_left_leg2, 53, 217, -3.0F, -6.0F, -3.0F, 6, 14, 10, 0.0F));
        front_left_leg2.cubeList
                .add(new ModelBox(front_left_leg2, 164, 4, -3.0F, -6.5F, -3.5F, 7, 15, 11, 0.0F));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(1.0F, 8.891F, 0.0751F);
        front_left_leg2.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.3927F, 0.0F, 0.0F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 45, 215, -2.4976F, -1.9965F, -2.5486F, 4, 7, 5, 0.0F));

        front_left_shin2 = new ModelRenderer(this);
        front_left_shin2.setRotationPoint(0.5F, 12.5F, 4.25F);
        front_left_leg2.addChild(front_left_shin2);
        setRotationAngle(front_left_shin2, -0.6109F, 0.0F, 0.0F);
        front_left_shin2.cubeList
                .add(new ModelBox(front_left_shin2, 6, 229, -1.5F, 0.0F, -4.0F, 3, 11, 4, 0.0F));

        front_left_foot2 = new ModelRenderer(this);
        front_left_foot2.setRotationPoint(0.0F, 11.0F, -4.0F);
        front_left_shin2.addChild(front_left_foot2);
        setRotationAngle(front_left_foot2, 0.3491F, 0.0F, 0.0F);
        front_left_foot2.cubeList
                .add(new ModelBox(front_left_foot2, 134, 126, -2.0F, 0.0F, 0.0F, 4, 4, 4, 0.0F));

        front_right_leg2 = new ModelRenderer(this);
        front_right_leg2.setRotationPoint(-5.0F, 3.0F, 3.0F);
        horse_body.addChild(front_right_leg2);
        setRotationAngle(front_right_leg2, 0.1991F, 0.2049F, -0.0743F);
        front_right_leg2.cubeList
                .add(new ModelBox(front_right_leg2, 220, 47, -3.5F, -6.5F, -3.5F, 7, 15, 11, 0.0F));
        front_right_leg2.cubeList
                .add(new ModelBox(front_right_leg2, 109, 217, -3.0F, -6.0F, -3.0F, 6, 14, 10, 0.0F));

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(-1.0F, 8.891F, 0.0751F);
        front_right_leg2.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.3927F, 0.0F, 0.0F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 38, 244, -1.5024F, -1.9965F, -2.5486F, 4, 7, 5, 0.0F));

        front_right_shin2 = new ModelRenderer(this);
        front_right_shin2.setRotationPoint(-0.5F, 12.5F, 4.25F);
        front_right_leg2.addChild(front_right_shin2);
        setRotationAngle(front_right_shin2, -0.6109F, 0.0F, 0.0F);
        front_right_shin2.cubeList
                .add(new ModelBox(front_right_shin2, 6, 214, -1.5F, 0.0F, -4.0F, 3, 11, 4, 0.0F));

        front_right_foot2 = new ModelRenderer(this);
        front_right_foot2.setRotationPoint(0.0F, 11.0F, -4.0F);
        front_right_shin2.addChild(front_right_foot2);
        setRotationAngle(front_right_foot2, 0.3491F, 0.0F, 0.0F);
        front_right_foot2.cubeList
                .add(new ModelBox(front_right_foot2, 135, 118, -2.0F, 0.0F, 0.0F, 4, 4, 4, 0.0F));

        torso = new ModelRenderer(this);
        torso.setRotationPoint(0.0F, -10.999F, -6.0436F);
        horse_body.addChild(torso);
        setRotationAngle(torso, -0.1309F, 0.0F, 0.0F);
        torso.cubeList.add(new ModelBox(torso, 30, 1, -4.0F, -4.0F, -3.0F, 8, 5, 5, 0.0F));

        torso_top = new ModelRenderer(this);
        torso_top.setRotationPoint(0.0F, -4.0F, 2.0F);
        torso.addChild(torso_top);
        setRotationAngle(torso_top, 0.1745F, 0.0F, 0.0F);
        torso_top.cubeList.add(new ModelBox(torso_top, 0, 0, -5.0F, -6.0F, -5.0F, 10, 6, 5, 0.0F));

        h_head = new ModelRenderer(this);
        h_head.setRotationPoint(-0.214F, -6.125F, -2.9277F);
        torso_top.addChild(h_head);
        h_head.cubeList.add(new ModelBox(h_head, 56, 0, -3.786F, -7.875F, -4.0723F, 8, 8, 8, 0.0F));

        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(5.0392F, -1.375F, 0.1512F);
        h_head.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 0.3927F, 0.0F);
        cube_r6.cubeList.add(new ModelBox(cube_r6, 51, -5, 0.0F, -2.5F, -2.5F, 0, 5, 5, 0.0F));

        cube_r7 = new ModelRenderer(this);
        cube_r7.setRotationPoint(-4.6112F, -1.375F, 0.1512F);
        h_head.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, -0.3927F, 0.0F);
        cube_r7.cubeList.add(new ModelBox(cube_r7, 25, -5, 0.0F, -2.5F, -2.5F, 0, 5, 5, 0.0F));

        cube_r8 = new ModelRenderer(this);
        cube_r8.setRotationPoint(-3.3056F, -3.875F, -3.9274F);
        h_head.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.0F, 0.3927F, 0.0F);
        cube_r8.cubeList.add(new ModelBox(cube_r8, 14, 11, -1.0F, 0.0F, -0.5F, 5, 5, 2, 0.0F));

        cube_r9 = new ModelRenderer(this);
        cube_r9.setRotationPoint(3.7028F, -3.875F, -3.9274F);
        h_head.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.0F, -0.3927F, 0.0F);
        cube_r9.cubeList.add(new ModelBox(cube_r9, 0, 11, -4.0F, 0.0F, -0.5F, 5, 5, 2, 0.0F));

        cape_1 = new ModelRenderer(this);
        cape_1.setRotationPoint(0.0F, -6.0F, 0.0F);
        torso_top.addChild(cape_1);
        setRotationAngle(cape_1, 0.3927F, 0.0F, 0.0F);
        cape_1.cubeList.add(new ModelBox(cape_1, 112, 0, -4.0F, 0.0F, 0.0F, 8, 5, 0, 0.0F));

        cape_2 = new ModelRenderer(this);
        cape_2.setRotationPoint(0.0F, 5.0F, 0.0F);
        cape_1.addChild(cape_2);
        cape_2.cubeList.add(new ModelBox(cape_2, 80, 0, -4.0F, 0.0F, 0.0F, 8, 5, 0, 0.0F));

        cape_3 = new ModelRenderer(this);
        cape_3.setRotationPoint(0.0F, 5.0F, 0.0F);
        cape_2.addChild(cape_3);
        cape_3.cubeList.add(new ModelBox(cape_3, 96, 0, -4.0F, 0.0F, 0.0F, 8, 5, 0, 0.0F));

        right_pauldron = new ModelRenderer(this);
        right_pauldron.setRotationPoint(-4.5F, -5.0F, -2.5F);
        torso_top.addChild(right_pauldron);
        setRotationAngle(right_pauldron, 0.1776F, 0.0283F, 0.086F);
        right_pauldron.cubeList.add(new ModelBox(right_pauldron, 0, 18, -4.0F, -2.0F, -3.0F, 5, 5, 6, 0.0F));

        cube_r10 = new ModelRenderer(this);
        cube_r10.setRotationPoint(2.3858F, 1.0F, -3.574F);
        right_pauldron.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.0F, 0.3927F, 0.0F);
        cube_r10.cubeList.add(new ModelBox(cube_r10, 42, 11, -1.5F, -2.0F, 0.0F, 3, 4, 0, 0.0F));

        left_pauldron = new ModelRenderer(this);
        left_pauldron.setRotationPoint(4.5F, -5.0F, -2.5F);
        torso_top.addChild(left_pauldron);
        setRotationAngle(left_pauldron, -0.7747F, 0.1252F, 0.0694F);
        left_pauldron.cubeList.add(new ModelBox(left_pauldron, 0, 29, -1.0F, -2.0F, -3.0F, 5, 5, 6, 0.0F));

        cube_r11 = new ModelRenderer(this);
        cube_r11.setRotationPoint(-2.3858F, 1.0F, -3.574F);
        left_pauldron.addChild(cube_r11);
        setRotationAngle(cube_r11, 0.0F, -0.3927F, 0.0F);
        cube_r11.cubeList.add(new ModelBox(cube_r11, 28, 13, -1.5F, -2.0F, 0.0F, 3, 4, 0, 0.0F));

        right_arm = new ModelRenderer(this);
        right_arm.setRotationPoint(-4.5F, -4.75F, -2.5F);
        torso_top.addChild(right_arm);
        setRotationAngle(right_arm, 0.4022F, 0.2265F, 0.4935F);
        right_arm.cubeList.add(new ModelBox(right_arm, 44, 11, -3.0F, -1.25F, -2.0F, 4, 7, 4, 0.0F));

        right_forearm = new ModelRenderer(this);
        right_forearm.setRotationPoint(-1.2641F, 5.6667F, 1.9021F);
        right_arm.addChild(right_forearm);
        setRotationAngle(right_forearm, -0.1745F, 0.0F, 0.0F);
        right_forearm.cubeList
                .add(new ModelBox(right_forearm, 0, 40, -1.2359F, 0.0833F, -3.9021F, 3, 5, 4, 0.0F));

        cube_r12 = new ModelRenderer(this);
        cube_r12.setRotationPoint(-0.5281F, 1.5833F, 0.8043F);
        right_forearm.addChild(cube_r12);
        setRotationAngle(cube_r12, 0.0F, 0.7854F, 0.0F);
        cube_r12.cubeList.add(new ModelBox(cube_r12, 0, -2, 0.0F, -1.5F, -1.0F, 0, 3, 2, 0.0F));

        right_hand = new ModelRenderer(this);
        right_hand.setRotationPoint(-1.2359F, 5.0833F, -1.9021F);
        right_forearm.addChild(right_hand);
        setRotationAngle(right_hand, 0.0F, -0.0436F, -0.2182F);
        right_hand.cubeList.add(new ModelBox(right_hand, 114, 5, 0.0F, 0.0F, -2.0F, 3, 4, 4, 0.0F));

        sword = new ModelRenderer(this);
        sword.setRotationPoint(1.5F, 2.0F, 0.0F);
        right_hand.addChild(sword);
        sword.cubeList.add(new ModelBox(sword, 98, 30, 0.05F, -1.0F, -18.5F, 0, 2, 15, 0.0F));
        sword.cubeList.add(new ModelBox(sword, 16, 18, -0.5F, -2.5F, -3.5F, 1, 5, 1, 0.0F));
        sword.cubeList.add(new ModelBox(sword, 69, 31, -0.5F, -0.5F, -2.5F, 1, 1, 6, 0.0F));

        cube_r13 = new ModelRenderer(this);
        cube_r13.setRotationPoint(1.0F, 0.8358F, -1.7929F);
        sword.addChild(cube_r13);
        setRotationAngle(cube_r13, 0.7854F, 0.0F, 0.0F);
        cube_r13.cubeList.add(new ModelBox(cube_r13, 0, 21, -1.5F, -0.5F, -2.75F, 1, 1, 1, 0.0F));

        cube_r14 = new ModelRenderer(this);
        cube_r14.setRotationPoint(1.0F, -3.9142F, -1.7929F);
        sword.addChild(cube_r14);
        setRotationAngle(cube_r14, 0.7854F, 0.0F, 0.0F);
        cube_r14.cubeList.add(new ModelBox(cube_r14, 56, 23, -1.5F, -0.5F, -2.75F, 1, 1, 1, 0.0F));

        left_arm = new ModelRenderer(this);
        left_arm.setRotationPoint(4.5F, -4.75F, -2.5F);
        torso_top.addChild(left_arm);
        setRotationAngle(left_arm, -0.7217F, 0.5995F, -0.2438F);
        left_arm.cubeList.add(new ModelBox(left_arm, 112, 13, -1.0F, -1.25F, -2.0F, 4, 7, 4, 0.0F));

        left_forearm = new ModelRenderer(this);
        left_forearm.setRotationPoint(1.2641F, 5.6667F, 1.9021F);
        left_arm.addChild(left_forearm);
        setRotationAngle(left_forearm, -0.1745F, 0.0F, 0.0F);
        left_forearm.cubeList
                .add(new ModelBox(left_forearm, 114, 28, -1.7641F, 0.0833F, -3.9021F, 3, 5, 4, 0.0F));

        cube_r15 = new ModelRenderer(this);
        cube_r15.setRotationPoint(0.5281F, 1.5833F, 0.8043F);
        left_forearm.addChild(cube_r15);
        setRotationAngle(cube_r15, 0.0F, -0.7854F, 0.0F);
        cube_r15.cubeList.add(new ModelBox(cube_r15, 108, 7, 0.0F, -1.5F, -1.0F, 0, 3, 2, 0.0F));

        shield_arm = new ModelRenderer(this);
        shield_arm.setRotationPoint(2.4437F, 4.3324F, -2.4758F);
        left_forearm.addChild(shield_arm);
        setRotationAngle(shield_arm, 1.5708F, 0.0F, 1.5708F);

        cube_r16 = new ModelRenderer(this);
        cube_r16.setRotationPoint(-3.0422F, 3.0F, -0.3827F);
        shield_arm.addChild(cube_r16);
        setRotationAngle(cube_r16, 0.0F, -0.3927F, 0.0F);
        cube_r16.cubeList.add(new ModelBox(cube_r16, 94, 17, -2.5F, -9.0F, -0.5F, 6, 11, 1, 0.0F));
        cube_r16.cubeList.add(new ModelBox(cube_r16, 108, 5, -0.5F, 2.0F, -0.5F, 4, 3, 1, 0.0F));

        cube_r17 = new ModelRenderer(this);
        cube_r17.setRotationPoint(3.0422F, 3.0F, -0.3827F);
        shield_arm.addChild(cube_r17);
        setRotationAngle(cube_r17, 0.0F, 0.3927F, 0.0F);
        cube_r17.cubeList.add(new ModelBox(cube_r17, 118, 24, -3.5F, 2.0F, -0.5F, 4, 3, 1, 0.0F));
        cube_r17.cubeList.add(new ModelBox(cube_r17, 94, 5, -3.5F, -9.0F, -0.5F, 6, 11, 1, 0.0F));

        left_hand = new ModelRenderer(this);
        left_hand.setRotationPoint(1.2359F, 5.0833F, -1.9021F);
        left_forearm.addChild(left_hand);
        setRotationAngle(left_hand, 0.0F, 0.0436F, 0.2182F);
        left_hand.cubeList.add(new ModelBox(left_hand, 114, 37, -3.0F, 0.0F, -2.0F, 3, 4, 4, 0.0F));

        weapon_back = new ModelRenderer(this);
        weapon_back.setRotationPoint(-0.5422F, -4.001F, 1.9263F);
        torso_top.addChild(weapon_back);

        shield_back = new ModelRenderer(this);
        shield_back.setRotationPoint(-0.5422F, -2.001F, 1.9263F);
        torso_top.addChild(shield_back);

        tabard = new ModelRenderer(this);
        tabard.setRotationPoint(0.0F, 1.0F, -3.0F);
        torso.addChild(tabard);
        setRotationAngle(tabard, -1.0908F, 0.0F, 0.0F);
        tabard.cubeList.add(new ModelBox(tabard, 68, 20, -3.0F, 0.0F, 0.0F, 6, 7, 0, 0.0F));

        right_leg = new ModelRenderer(this);
        right_leg.setRotationPoint(-3.3333F, 1.0254F, -1.4609F);
        torso.addChild(right_leg);
        setRotationAngle(right_leg, 0.1794F, 0.0766F, 0.7127F);
        right_leg.cubeList.add(new ModelBox(right_leg, 44, 33, -1.6667F, -0.0254F, -1.0391F, 4, 7, 4, 0.0F));

        right_shin = new ModelRenderer(this);
        right_shin.setRotationPoint(0.3333F, 6.9746F, -1.0391F);
        right_leg.addChild(right_shin);
        setRotationAngle(right_shin, 0.2921F, 0.0905F, -0.2921F);
        right_shin.cubeList.add(new ModelBox(right_shin, 95, 29, -1.5F, 0.0F, 0.0F, 3, 6, 3, 0.0F));

        cube_r18 = new ModelRenderer(this);
        cube_r18.setRotationPoint(0.0F, -0.9239F, -0.3827F);
        right_shin.addChild(cube_r18);
        setRotationAngle(cube_r18, 0.3927F, 0.0F, 0.0F);
        cube_r18.cubeList.add(new ModelBox(cube_r18, 36, 11, -1.5F, -1.0F, 0.0F, 3, 2, 0, 0.0F));

        left_leg = new ModelRenderer(this);
        left_leg.setRotationPoint(3.3333F, 1.0254F, -1.4609F);
        torso.addChild(left_leg);
        setRotationAngle(left_leg, -0.1696F, -0.0766F, -0.7127F);
        left_leg.cubeList.add(new ModelBox(left_leg, 44, 22, -2.3333F, -0.0254F, -1.0391F, 4, 7, 4, 0.0F));

        left_shin = new ModelRenderer(this);
        left_shin.setRotationPoint(-0.3333F, 6.9746F, -1.0391F);
        left_leg.addChild(left_shin);
        setRotationAngle(left_shin, 0.4185F, -0.1274F, 0.2783F);
        left_shin.cubeList.add(new ModelBox(left_shin, 83, 29, -1.5F, 0.0F, 0.0F, 3, 6, 3, 0.0F));

        cube_r19 = new ModelRenderer(this);
        cube_r19.setRotationPoint(0.0F, -0.9239F, -0.3827F);
        left_shin.addChild(cube_r19);
        setRotationAngle(cube_r19, 0.3927F, 0.0F, 0.0F);
        cube_r19.cubeList.add(new ModelBox(cube_r19, 36, 13, -1.5F, -1.0F, 0.0F, 3, 2, 0, 0.0F));

        sheath = new ModelRenderer(this);
        sheath.setRotationPoint(4.5F, -0.9743F, -0.8916F);
        torso.addChild(sheath);
        setRotationAngle(sheath, -0.6207F, 0.1163F, -0.847F);
        sheath.cubeList.add(new ModelBox(sheath, 27, 29, -0.5F, -1.0F, -2.5F, 1, 2, 15, 0.0F));
        sheath.cubeList.add(new ModelBox(sheath, 64, 20, -0.5F, -2.5F, -3.5F, 1, 5, 1, 0.0F));
        sheath.cubeList.add(new ModelBox(sheath, 8, 43, -0.5F, -0.5F, -9.5F, 1, 1, 6, 0.0F));

        cube_r20 = new ModelRenderer(this);
        cube_r20.setRotationPoint(1.0F, 0.8358F, -4.2071F);
        sheath.addChild(cube_r20);
        setRotationAngle(cube_r20, -0.7854F, 0.0F, 0.0F);
        cube_r20.cubeList.add(new ModelBox(cube_r20, 0, 18, -1.5F, -0.5F, 1.25F, 1, 1, 1, 0.0F));

        cube_r21 = new ModelRenderer(this);
        cube_r21.setRotationPoint(1.0F, -3.9142F, -4.2071F);
        sheath.addChild(cube_r21);
        setRotationAngle(cube_r21, -0.7854F, 0.0F, 0.0F);
        cube_r21.cubeList.add(new ModelBox(cube_r21, 64, 26, -1.5F, -0.5F, 1.25F, 1, 1, 1, 0.0F));

        flag = new ModelRenderer(this);
        flag.setRotationPoint(-7.5F, -5.375F, -2.0F);
        horse_body.addChild(flag);
        setRotationAngle(flag, -0.1744F, -0.0076F, -0.043F);
        flag.cubeList.add(new ModelBox(flag, 160, 0, -0.1488F, -43.6694F, -0.4044F, 1, 56, 1, 0.0F));

        flag_1 = new ModelRenderer(this);
        flag_1.setRotationPoint(0.3512F, -40.1694F, 0.5956F);
        flag.addChild(flag_1);
        setRotationAngle(flag_1, 0.0F, -0.1309F, 0.0F);
        flag_1.cubeList.add(new ModelBox(flag_1, 240, 185, 0.0F, -3.5F, 0.0F, 0, 7, 8, 0.0F));

        flag_2 = new ModelRenderer(this);
        flag_2.setRotationPoint(0.0F, 0.0F, 8.0F);
        flag_1.addChild(flag_2);
        setRotationAngle(flag_2, 0.0F, 0.5672F, 0.0F);
        flag_2.cubeList.add(new ModelBox(flag_2, 240, 192, 0.0F, -3.5F, 0.0F, 0, 7, 8, 0.0F));

        flag_3 = new ModelRenderer(this);
        flag_3.setRotationPoint(0.0F, 0.0F, 8.0F);
        flag_2.addChild(flag_3);
        setRotationAngle(flag_3, 0.0F, -1.0036F, 0.0F);
        flag_3.cubeList.add(new ModelBox(flag_3, 240, 178, 0.0F, -3.5F, 0.0F, 0, 7, 8, 0.0F));
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
        horse_body.render(scaleFactor);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor, Entity entity) {
        // mob idle, if that values is equals
        flag_1.rotateAngleY = MathHelper.sin(ageInTicks * 0.4f) * 0.3f;
        if (limbSwing == prevLimbSwing) {
            torso.rotateAngleX = MathHelper.sin((float) Math.toRadians(1));
            torso_top.rotateAngleX = MathHelper.sin(ageInTicks * 0.1f) * 0.02f;
            horse_neck.rotateAngleX = MathHelper.sin(ageInTicks * 0.1f) * 0.05f;
            horse_body.rotationPointY += -MathHelper.sin(ageInTicks * 0.1f) * 0.001f;
            front_left_leg.rotationPointY += MathHelper.sin(ageInTicks * 0.1f) * 0.001f;
            front_left_leg2.rotationPointY += MathHelper.sin(ageInTicks * 0.1f) * 0.001f;
            front_right_leg.rotationPointY += MathHelper.sin(ageInTicks * 0.1f) * 0.001f;
            front_right_leg2.rotationPointY += MathHelper.sin(ageInTicks * 0.1f) * 0.001f;
            tail_1.rotateAngleX = MathHelper.sin(ageInTicks * 0.1f) * 0.2f;
        } else {
            torso.rotateAngleX = MathHelper.sin((float) Math.toRadians(15));
            front_left_leg.rotateAngleX = MathHelper.sin(limbSwing * 0.8f) * (limbSwingAmount * 1f);
            front_left_leg2.rotateAngleX = -MathHelper.sin(limbSwing * 0.8f) * (limbSwingAmount * 1f);
            front_right_leg.rotateAngleX = -front_left_leg.rotateAngleX;
            front_right_leg2.rotateAngleX = -front_left_leg2.rotateAngleX;

            front_left_shin.rotateAngleX = -MathHelper.sin(limbSwing * 0.8f) * (limbSwingAmount * 0.3f);
            front_left_shin2.rotateAngleX = MathHelper.sin(limbSwing * 0.8f) * (limbSwingAmount * 0.3f);
            front_right_shin.rotateAngleX = -front_left_shin.rotateAngleX;
            front_right_shin2.rotateAngleX = -front_left_shin2.rotateAngleX;
            tail_1.rotateAngleX = MathHelper.sin(limbSwing * 1.6f) * (limbSwingAmount * 0.2f);
        }
        prevLimbSwing = limbSwing;
    }
}
