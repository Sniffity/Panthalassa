package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.common.entity.EntityKronosaurus;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;


public class ModelKronosaurus<T extends EntityKronosaurus> extends EntityModel<T>  {
    private final ModelRenderer main;
    private final ModelRenderer neck_main;
    private final ModelRenderer head;
    private final ModelRenderer upper_head;
    private final ModelRenderer upper_head_back;
    private final ModelRenderer upper_head_side_1;
    private final ModelRenderer upper_head_side_2;
    private final ModelRenderer upper_head_jaw_connector;
    private final ModelRenderer upper_jaw;
    private final ModelRenderer upper_jaw_cover;
    private final ModelRenderer snout;
    private final ModelRenderer snout_connector_top;
    private final ModelRenderer upper_jaw_tip;
    private final ModelRenderer upper_jaw_base;
    private final ModelRenderer upper_teeth_1;
    private final ModelRenderer upper_teeth_2;
    private final ModelRenderer upper_teeth_3;
    private final ModelRenderer upper_teeth_4;
    private final ModelRenderer upper_teeth_5;
    private final ModelRenderer upper_teeth_6;
    private final ModelRenderer upper_teeth_7;
    private final ModelRenderer upper_teeth_8;
    private final ModelRenderer upper_teeth_9;
    private final ModelRenderer lower_jaw;
    private final ModelRenderer lower_jaw_base;
    private final ModelRenderer lower_teeth_1;
    private final ModelRenderer lower_jaw_base_2;
    private final ModelRenderer lower_teeth_2;
    private final ModelRenderer lower_teeth_3;
    private final ModelRenderer lower_teeth_4;
    private final ModelRenderer lower_teeth_5;
    private final ModelRenderer lower_teeth_6;
    private final ModelRenderer neck2;
    private final ModelRenderer upper_neck;
    private final ModelRenderer lower_neck;
    private final ModelRenderer torso;
    private final ModelRenderer belly;
    private final ModelRenderer back;
    private final ModelRenderer right_flipper_front;
    private final ModelRenderer right_flipper_front_tip;
    private final ModelRenderer left_flipper_front;
    private final ModelRenderer left_flipper_front_tip;
    private final ModelRenderer lower_torso;
    private final ModelRenderer lower_torso_tail;
    private final ModelRenderer tail_section_1;
    private final ModelRenderer tail_section_2;
    private final ModelRenderer tail_section_3;
    private final ModelRenderer tail_section_4;
    private final ModelRenderer lower_torso_tail_base;
    private final ModelRenderer right_back_flipper;
    private final ModelRenderer right_back_flipper_tip;
    private final ModelRenderer left_back_flipper;
    private final ModelRenderer left_back_flipper_tip;

    public ModelKronosaurus() {
        textureWidth = 256;
        textureHeight = 256;

        main = new ModelRenderer(this);
        main.setRotationPoint(0.0F, 14.8F, -30.0F);
        setRotationAngle(main, -0.1361F, 0.0F, 0.0F);
        main.setTextureOffset(0, 0).addBox(-5.0F, -4.0F, -6.0F, 10.0F, 8.0F, 12.0F, 0.0F, false);

        neck_main = new ModelRenderer(this);
        neck_main.setRotationPoint(0.0F, 0.0F, -8.3F);
        main.addChild(neck_main);
        setRotationAngle(neck_main, 0.1257F, 0.0F, 0.0F);
        neck_main.setTextureOffset(44, 0).addBox(-4.5F, -3.5F, -4.0F, 9.0F, 7.0F, 8.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, -1.1F, -1.0F);
        neck_main.addChild(head);
        setRotationAngle(head, -0.0052F, 0.0F, 0.0F);
        head.setTextureOffset(78, 0).addBox(-3.0F, -3.0F, -8.0F, 6.0F, 6.0F, 8.0F, 0.0F, false);

        upper_head = new ModelRenderer(this);
        upper_head.setRotationPoint(0.0F, -2.1F, 0.4F);
        neck_main.addChild(upper_head);
        upper_head.setTextureOffset(106, 0).addBox(-3.0F, -4.0F, -4.0F, 6.0F, 4.0F, 8.0F, 0.0F, false);

        upper_head_back = new ModelRenderer(this);
        upper_head_back.setRotationPoint(0.0F, -2.2F, 5.4F);
        neck_main.addChild(upper_head_back);
        setRotationAngle(upper_head_back, -0.363F, 0.0F, 0.0F);
        upper_head_back.setTextureOffset(134, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 3.0F, 6.0F, 0.0F, false);

        upper_head_side_1 = new ModelRenderer(this);
        upper_head_side_1.setRotationPoint(-1.8F, -3.8F, 0.7F);
        neck_main.addChild(upper_head_side_1);
        setRotationAngle(upper_head_side_1, 0.0F, 0.0F, -1.1345F);
        upper_head_side_1.setTextureOffset(32, 0).addBox(-1.5F, -2.0F, -3.0F, 3.0F, 2.0F, 6.0F, 0.0F, false);

        upper_head_side_2 = new ModelRenderer(this);
        upper_head_side_2.setRotationPoint(1.8F, -3.8F, 0.7F);
        neck_main.addChild(upper_head_side_2);
        setRotationAngle(upper_head_side_2, 0.0F, 0.0F, 1.117F);
        upper_head_side_2.setTextureOffset(158, 0).addBox(-1.5F, -2.0F, -3.5F, 3.0F, 2.0F, 6.0F, 0.0F, false);

        upper_head_jaw_connector = new ModelRenderer(this);
        upper_head_jaw_connector.setRotationPoint(0.0F, -3.2F, -5.0F);
        neck_main.addChild(upper_head_jaw_connector);
        setRotationAngle(upper_head_jaw_connector, 0.2182F, 0.0F, 0.0F);
        upper_head_jaw_connector.setTextureOffset(176, 0).addBox(-3.5F, -2.0F, -3.5F, 7.0F, 4.0F, 7.0F, 0.0F, false);

        upper_jaw = new ModelRenderer(this);
        upper_jaw.setRotationPoint(0.0F, -2.7F, -3.6F);
        neck_main.addChild(upper_jaw);
        upper_jaw.setTextureOffset(187, 0).addBox(-2.5F, -2.0F, -17.0F, 5.0F, 4.0F, 17.0F, 0.0F, false);

        upper_jaw_cover = new ModelRenderer(this);
        upper_jaw_cover.setRotationPoint(0.0F, 0.3F, -7.7F);
        upper_jaw.addChild(upper_jaw_cover);
        setRotationAngle(upper_jaw_cover, 0.0698F, 0.0F, 0.0F);
        upper_jaw_cover.setTextureOffset(214, 4).addBox(-1.5F, -3.0F, -8.5F, 3.0F, 3.0F, 17.0F, 0.0F, false);

        snout = new ModelRenderer(this);
        snout.setRotationPoint(0.0F, 0.0F, -16.1F);
        upper_jaw.addChild(snout);
        setRotationAngle(snout, 0.0698F, 0.0F, 0.0F);
        snout.setTextureOffset(70, 0).addBox(-1.5F, -1.0F, -4.0F, 3.0F, 3.0F, 4.0F, 0.0F, false);

        snout_connector_top = new ModelRenderer(this);
        snout_connector_top.setRotationPoint(0.0F, -0.8F, -15.7F);
        upper_jaw.addChild(snout_connector_top);
        setRotationAngle(snout_connector_top, 0.1047F, 0.0F, 0.0F);
        snout_connector_top.setTextureOffset(98, 0).addBox(-1.0F, -1.0F, -5.0F, 2.0F, 3.0F, 5.0F, 0.0F, false);

        upper_jaw_tip = new ModelRenderer(this);
        upper_jaw_tip.setRotationPoint(0.0F, 0.7F, -18.1F);
        upper_jaw.addChild(upper_jaw_tip);
        setRotationAngle(upper_jaw_tip, 0.0698F, 0.0F, 0.0F);
        upper_jaw_tip.setTextureOffset(152, 8).addBox(-2.0F, -0.5F, -3.0F, 4.0F, 2.0F, 6.0F, 0.0F, false);

        upper_jaw_base = new ModelRenderer(this);
        upper_jaw_base.setRotationPoint(0.0F, 2.2F, -12.7F);
        upper_jaw.addChild(upper_jaw_base);
        upper_jaw_base.setTextureOffset(117, 9).addBox(-3.5F, -2.0F, -8.0F, 7.0F, 2.0F, 17.0F, 0.0F, false);

        upper_teeth_1 = new ModelRenderer(this);
        upper_teeth_1.setRotationPoint(-2.1F, 1.8F, -19.6F);
        upper_jaw.addChild(upper_teeth_1);
        setRotationAngle(upper_teeth_1, 1.5708F, -0.7854F, -0.7854F);
        upper_teeth_1.setTextureOffset(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        upper_teeth_2 = new ModelRenderer(this);
        upper_teeth_2.setRotationPoint(-2.1F, 1.8F, -18.3F);
        upper_jaw.addChild(upper_teeth_2);
        setRotationAngle(upper_teeth_2, 1.5708F, -0.7854F, -0.7854F);
        upper_teeth_2.setTextureOffset(4, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        upper_teeth_3 = new ModelRenderer(this);
        upper_teeth_3.setRotationPoint(-2.1F, 1.9F, -16.8F);
        upper_jaw.addChild(upper_teeth_3);
        setRotationAngle(upper_teeth_3, 1.5708F, -0.7854F, -0.7854F);
        upper_teeth_3.setTextureOffset(8, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        upper_teeth_4 = new ModelRenderer(this);
        upper_teeth_4.setRotationPoint(2.1F, 1.8F, -19.6F);
        upper_jaw.addChild(upper_teeth_4);
        setRotationAngle(upper_teeth_4, 1.5708F, -0.7854F, -0.7854F);
        upper_teeth_4.setTextureOffset(32, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        upper_teeth_5 = new ModelRenderer(this);
        upper_teeth_5.setRotationPoint(2.1F, 1.8F, -18.3F);
        upper_jaw.addChild(upper_teeth_5);
        setRotationAngle(upper_teeth_5, 1.5708F, -0.7854F, -0.7854F);
        upper_teeth_5.setTextureOffset(44, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        upper_teeth_6 = new ModelRenderer(this);
        upper_teeth_6.setRotationPoint(2.1F, 1.8F, -16.8F);
        upper_jaw.addChild(upper_teeth_6);
        setRotationAngle(upper_teeth_6, 1.5708F, -0.7854F, -0.7854F);
        upper_teeth_6.setTextureOffset(48, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        upper_teeth_7 = new ModelRenderer(this);
        upper_teeth_7.setRotationPoint(-1.0F, 1.9F, -20.0F);
        upper_jaw.addChild(upper_teeth_7);
        setRotationAngle(upper_teeth_7, 1.5708F, -0.7854F, -0.7854F);
        upper_teeth_7.setTextureOffset(70, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        upper_teeth_8 = new ModelRenderer(this);
        upper_teeth_8.setRotationPoint(1.0F, 1.9F, -20.0F);
        upper_jaw.addChild(upper_teeth_8);
        setRotationAngle(upper_teeth_8, 1.5708F, -0.7854F, -0.7854F);
        upper_teeth_8.setTextureOffset(80, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        upper_teeth_9 = new ModelRenderer(this);
        upper_teeth_9.setRotationPoint(0.0F, 1.9F, -20.0F);
        upper_jaw.addChild(upper_teeth_9);
        setRotationAngle(upper_teeth_9, 1.5708F, -0.7854F, -0.7854F);
        upper_teeth_9.setTextureOffset(98, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        lower_jaw = new ModelRenderer(this);
        lower_jaw.setRotationPoint(0.0F, 0.0F, -8.1F);
        neck_main.addChild(lower_jaw);
        setRotationAngle(lower_jaw, 0.1222F, 0.0F, 0.0F);
        lower_jaw.setTextureOffset(90, 12).addBox(-2.5F, -1.0F, -16.0F, 5.0F, 2.0F, 16.0F, 0.0F, false);

        lower_jaw_base = new ModelRenderer(this);
        lower_jaw_base.setRotationPoint(0.0F, -0.1F, -7.6F);
        lower_jaw.addChild(lower_jaw_base);
        lower_jaw_base.setTextureOffset(156, 12).addBox(-2.0F, 0.0F, -8.0F, 4.0F, 2.0F, 16.0F, 0.0F, false);

        lower_teeth_1 = new ModelRenderer(this);
        lower_teeth_1.setRotationPoint(-2.2F, -0.6F, -13.1F);
        lower_jaw.addChild(lower_teeth_1);
        setRotationAngle(lower_teeth_1, 1.5708F, -0.7854F, -1.0472F);
        lower_teeth_1.setTextureOffset(107, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        lower_jaw_base_2 = new ModelRenderer(this);
        lower_jaw_base_2.setRotationPoint(0.0F, -0.9F, -8.3F);
        lower_jaw.addChild(lower_jaw_base_2);
        lower_jaw_base_2.setTextureOffset(28, 15).addBox(-3.5F, 0.0F, -8.0F, 7.0F, 1.0F, 16.0F, 0.0F, false);

        lower_teeth_2 = new ModelRenderer(this);
        lower_teeth_2.setRotationPoint(2.2F, -0.6F, -13.1F);
        lower_jaw.addChild(lower_teeth_2);
        setRotationAngle(lower_teeth_2, 1.5708F, -0.7854F, -1.0472F);
        lower_teeth_2.setTextureOffset(126, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        lower_teeth_3 = new ModelRenderer(this);
        lower_teeth_3.setRotationPoint(-2.2F, -0.6F, -14.5F);
        lower_jaw.addChild(lower_teeth_3);
        setRotationAngle(lower_teeth_3, 1.5708F, -0.7854F, -1.0472F);
        lower_teeth_3.setTextureOffset(130, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        lower_teeth_4 = new ModelRenderer(this);
        lower_teeth_4.setRotationPoint(2.2F, -0.6F, -14.5F);
        lower_jaw.addChild(lower_teeth_4);
        setRotationAngle(lower_teeth_4, 1.5708F, -0.7854F, -1.0472F);
        lower_teeth_4.setTextureOffset(134, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        lower_teeth_5 = new ModelRenderer(this);
        lower_teeth_5.setRotationPoint(0.5F, -0.5F, -14.9F);
        lower_jaw.addChild(lower_teeth_5);
        setRotationAngle(lower_teeth_5, 1.5708F, -0.7854F, -1.0472F);
        lower_teeth_5.setTextureOffset(152, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        lower_teeth_6 = new ModelRenderer(this);
        lower_teeth_6.setRotationPoint(-0.5F, -0.5F, -14.9F);
        lower_jaw.addChild(lower_teeth_6);
        setRotationAngle(lower_teeth_6, 1.5708F, -0.7854F, -1.0472F);
        lower_teeth_6.setTextureOffset(156, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        neck2 = new ModelRenderer(this);
        neck2.setRotationPoint(0.0F, -0.1F, -5.2F);
        main.addChild(neck2);
        neck2.setTextureOffset(74, 14).addBox(-4.5F, -4.5F, -2.0F, 9.0F, 9.0F, 4.0F, 0.0F, false);

        upper_neck = new ModelRenderer(this);
        upper_neck.setRotationPoint(0.0F, -1.5F, 4.6F);
        main.addChild(upper_neck);
        setRotationAngle(upper_neck, 0.1396F, 0.0F, 0.0F);
        upper_neck.setTextureOffset(178, 21).addBox(-3.5F, -4.0F, -9.0F, 7.0F, 8.0F, 18.0F, 0.0F, false);

        lower_neck = new ModelRenderer(this);
        lower_neck.setRotationPoint(0.0F, -0.2F, -2.2F);
        main.addChild(lower_neck);
        lower_neck.setTextureOffset(56, 27).addBox(-3.5F, -4.0F, -9.0F, 7.0F, 8.0F, 18.0F, 0.0F, false);

        torso = new ModelRenderer(this);
        torso.setRotationPoint(0.0F, -0.9F, 20.5F);
        main.addChild(torso);
        setRotationAngle(torso, 0.1449F, 0.0F, 0.0F);
        torso.setTextureOffset(98, 28).addBox(-6.0F, -5.0F, -17.0F, 12.0F, 10.0F, 34.0F, 0.0F, false);

        belly = new ModelRenderer(this);
        belly.setRotationPoint(0.0F, 1.8F, -0.3F);
        torso.addChild(belly);
        belly.setTextureOffset(0, 32).addBox(-5.0F, 0.0F, -14.5F, 10.0F, 4.0F, 29.0F, 0.0F, false);

        back = new ModelRenderer(this);
        back.setRotationPoint(0.0F, -1.3F, 2.0F);
        torso.addChild(back);
        back.setTextureOffset(161, 47).addBox(-5.0F, -5.0F, -14.5F, 10.0F, 10.0F, 29.0F, 0.0F, false);

        right_flipper_front = new ModelRenderer(this);
        right_flipper_front.setRotationPoint(-3.9F, -0.3F, -14.1F);
        torso.addChild(right_flipper_front);
        setRotationAngle(right_flipper_front, 0.0698F, -0.7679F, -0.576F);
        right_flipper_front.setTextureOffset(214, 33).addBox(-3.0F, -1.0F, 0.0F, 6.0F, 2.0F, 14.0F, 0.0F, false);

        right_flipper_front_tip = new ModelRenderer(this);
        right_flipper_front_tip.setRotationPoint(0.0F, 0.0F, 9.6F);
        right_flipper_front.addChild(right_flipper_front_tip);
        right_flipper_front_tip.setTextureOffset(88, 30).addBox(-2.5F, -0.5F, 0.0F, 5.0F, 1.0F, 14.0F, 0.0F, false);

        left_flipper_front = new ModelRenderer(this);
        left_flipper_front.setRotationPoint(3.9F, -0.3F, -14.1F);
        torso.addChild(left_flipper_front);
        setRotationAngle(left_flipper_front, 0.0698F, 0.7679F, 0.576F);
        left_flipper_front.setTextureOffset(92, 45).addBox(-3.0F, -1.0F, 0.0F, 6.0F, 2.0F, 14.0F, 0.0F, false);

        left_flipper_front_tip = new ModelRenderer(this);
        left_flipper_front_tip.setRotationPoint(0.0F, 0.0F, 9.6F);
        left_flipper_front.addChild(left_flipper_front_tip);
        left_flipper_front_tip.setTextureOffset(210, 49).addBox(-2.5F, -0.5F, 0.0F, 5.0F, 1.0F, 14.0F, 0.0F, false);

        lower_torso = new ModelRenderer(this);
        lower_torso.setRotationPoint(0.0F, 0.2F, 14.4F);
        torso.addChild(lower_torso);
        lower_torso.setTextureOffset(60, 61).addBox(-5.0F, -6.0F, 0.0F, 10.0F, 12.0F, 18.0F, 0.0F, false);

        lower_torso_tail = new ModelRenderer(this);
        lower_torso_tail.setRotationPoint(0.0F, -1.2F, 14.4F);
        lower_torso.addChild(lower_torso_tail);
        setRotationAngle(lower_torso_tail, 0.0733F, 0.0F, 0.0F);
        lower_torso_tail.setTextureOffset(0, 65).addBox(-4.0F, -4.5F, 0.0F, 8.0F, 9.0F, 19.0F, 0.0F, false);

        tail_section_1 = new ModelRenderer(this);
        tail_section_1.setRotationPoint(0.0F, 0.0F, 14.5F);
        lower_torso_tail.addChild(tail_section_1);
        setRotationAngle(tail_section_1, 0.0541F, 0.0F, 0.0F);
        tail_section_1.setTextureOffset(87, 72).addBox(-3.0F, -3.5F, 0.0F, 6.0F, 7.0F, 29.0F, 0.0F, false);

        tail_section_2 = new ModelRenderer(this);
        tail_section_2.setRotationPoint(0.0F, -0.2F, 27.0F);
        tail_section_1.addChild(tail_section_2);
        setRotationAngle(tail_section_2, 0.0541F, 0.0F, 0.0F);
        tail_section_2.setTextureOffset(128, 72).addBox(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 15.0F, 0.0F, false);

        tail_section_3 = new ModelRenderer(this);
        tail_section_3.setRotationPoint(0.0F, -1.3F, 11.7F);
        tail_section_2.addChild(tail_section_3);
        setRotationAngle(tail_section_3, -0.0942F, 0.0F, 0.0F);
        tail_section_3.setTextureOffset(156, 35).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 12.0F, 0.0F, false);

        tail_section_4 = new ModelRenderer(this);
        tail_section_4.setRotationPoint(0.0F, 2.0F, 2.0F);
        tail_section_3.addChild(tail_section_4);
        setRotationAngle(tail_section_4, 0.1641F, 0.0F, 0.0F);
        tail_section_4.setTextureOffset(148, 16).addBox(-0.5F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F, 0.0F, false);

        lower_torso_tail_base = new ModelRenderer(this);
        lower_torso_tail_base.setRotationPoint(0.0F, -0.8F, 23.8F);
        lower_torso.addChild(lower_torso_tail_base);
        setRotationAngle(lower_torso_tail_base, 0.096F, 0.0F, 0.0F);
        lower_torso_tail_base.setTextureOffset(157, 86).addBox(-3.0F, 0.0F, -9.5F, 6.0F, 5.0F, 19.0F, 0.0F, false);

        right_back_flipper = new ModelRenderer(this);
        right_back_flipper.setRotationPoint(-3.1F, 0.9F, 5.3F);
        lower_torso.addChild(right_back_flipper);
        setRotationAngle(right_back_flipper, 0.0698F, -0.7679F, -0.576F);
        right_back_flipper.setTextureOffset(188, 86).addBox(-3.0F, -1.0F, 0.0F, 6.0F, 2.0F, 14.0F, 0.0F, false);

        right_back_flipper_tip = new ModelRenderer(this);
        right_back_flipper_tip.setRotationPoint(0.0F, 0.0F, 9.6F);
        right_back_flipper.addChild(right_back_flipper_tip);
        right_back_flipper_tip.setTextureOffset(214, 88).addBox(-2.5F, -0.5F, 0.0F, 5.0F, 1.0F, 14.0F, 0.0F, false);

        left_back_flipper = new ModelRenderer(this);
        left_back_flipper.setRotationPoint(3.1F, 0.9F, 5.3F);
        lower_torso.addChild(left_back_flipper);
        setRotationAngle(left_back_flipper, 0.0698F, 0.7679F, 0.576F);
        left_back_flipper.setTextureOffset(40, 91).addBox(-3.0F, -1.0F, 0.0F, 6.0F, 2.0F, 14.0F, 0.0F, false);

        left_back_flipper_tip = new ModelRenderer(this);
        left_back_flipper_tip.setRotationPoint(0.0F, 0.0F, 9.6F);
        left_back_flipper.addChild(left_back_flipper_tip);
        left_back_flipper_tip.setTextureOffset(0, 93).addBox(-2.5F, -0.5F, 0.0F, 5.0F, 1.0F, 14.0F, 0.0F, false);
    }

    public ModelKronosaurus(ModelRenderer main, ModelRenderer neck_main, ModelRenderer head, ModelRenderer upper_head, ModelRenderer upper_head_back, ModelRenderer upper_head_side_1, ModelRenderer upper_head_side_2, ModelRenderer upper_head_jaw_connector, ModelRenderer upper_jaw, ModelRenderer upper_jaw_cover, ModelRenderer snout, ModelRenderer snout_connector_top, ModelRenderer upper_jaw_tip, ModelRenderer upper_jaw_base, ModelRenderer upper_teeth_1, ModelRenderer upper_teeth_2, ModelRenderer upper_teeth_3, ModelRenderer upper_teeth_4, ModelRenderer upper_teeth_5, ModelRenderer upper_teeth_6, ModelRenderer upper_teeth_7, ModelRenderer upper_teeth_8, ModelRenderer upper_teeth_9, ModelRenderer lower_jaw, ModelRenderer lower_jaw_base, ModelRenderer lower_teeth_1, ModelRenderer lower_jaw_base_2, ModelRenderer lower_teeth_2, ModelRenderer lower_teeth_3, ModelRenderer lower_teeth_4, ModelRenderer lower_teeth_5, ModelRenderer lower_teeth_6, ModelRenderer neck2, ModelRenderer upper_neck, ModelRenderer lower_neck, ModelRenderer torso, ModelRenderer belly, ModelRenderer back, ModelRenderer right_flipper_front, ModelRenderer right_flipper_front_tip, ModelRenderer left_flipper_front, ModelRenderer left_flipper_front_tip, ModelRenderer lower_torso, ModelRenderer lower_torso_tail, ModelRenderer tail_section_1, ModelRenderer tail_section_2, ModelRenderer tail_section_3, ModelRenderer tail_section_4, ModelRenderer lower_torso_tail_base, ModelRenderer right_back_flipper, ModelRenderer right_back_flipper_tip, ModelRenderer left_back_flipper, ModelRenderer left_back_flipper_tip) {
        this.main = main;
        this.neck_main = neck_main;
        this.head = head;
        this.upper_head = upper_head;
        this.upper_head_back = upper_head_back;
        this.upper_head_side_1 = upper_head_side_1;
        this.upper_head_side_2 = upper_head_side_2;
        this.upper_head_jaw_connector = upper_head_jaw_connector;
        this.upper_jaw = upper_jaw;
        this.upper_jaw_cover = upper_jaw_cover;
        this.snout = snout;
        this.snout_connector_top = snout_connector_top;
        this.upper_jaw_tip = upper_jaw_tip;
        this.upper_jaw_base = upper_jaw_base;
        this.upper_teeth_1 = upper_teeth_1;
        this.upper_teeth_2 = upper_teeth_2;
        this.upper_teeth_3 = upper_teeth_3;
        this.upper_teeth_4 = upper_teeth_4;
        this.upper_teeth_5 = upper_teeth_5;
        this.upper_teeth_6 = upper_teeth_6;
        this.upper_teeth_7 = upper_teeth_7;
        this.upper_teeth_8 = upper_teeth_8;
        this.upper_teeth_9 = upper_teeth_9;
        this.lower_jaw = lower_jaw;
        this.lower_jaw_base = lower_jaw_base;
        this.lower_teeth_1 = lower_teeth_1;
        this.lower_jaw_base_2 = lower_jaw_base_2;
        this.lower_teeth_2 = lower_teeth_2;
        this.lower_teeth_3 = lower_teeth_3;
        this.lower_teeth_4 = lower_teeth_4;
        this.lower_teeth_5 = lower_teeth_5;
        this.lower_teeth_6 = lower_teeth_6;
        this.neck2 = neck2;
        this.upper_neck = upper_neck;
        this.lower_neck = lower_neck;
        this.torso = torso;
        this.belly = belly;
        this.back = back;
        this.right_flipper_front = right_flipper_front;
        this.right_flipper_front_tip = right_flipper_front_tip;
        this.left_flipper_front = left_flipper_front;
        this.left_flipper_front_tip = left_flipper_front_tip;
        this.lower_torso = lower_torso;
        this.lower_torso_tail = lower_torso_tail;
        this.tail_section_1 = tail_section_1;
        this.tail_section_2 = tail_section_2;
        this.tail_section_3 = tail_section_3;
        this.tail_section_4 = tail_section_4;
        this.lower_torso_tail_base = lower_torso_tail_base;
        this.right_back_flipper = right_back_flipper;
        this.right_back_flipper_tip = right_back_flipper_tip;
        this.left_back_flipper = left_back_flipper;
        this.left_back_flipper_tip = left_back_flipper_tip;
    }


    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red,
                       float green, float blue, float alpha) {
        main.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(EntityKronosaurus arg0, float arg1, float arg2, float arg3, float arg4, float arg5) {
    }
}

