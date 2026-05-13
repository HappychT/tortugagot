package com.blocknpc.client.model;

import com.blocknpc.entity.EntityBlockNPC;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Timer;
import org.lwjgl.opengl.GL11;

public class ModelBlockNPC extends ModelBase {

    private final RenderBlocks renderBlocks = new RenderBlocks();

    public ModelBlockNPC() {
        textureWidth = 64;
        textureHeight = 64;
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount,
                       float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        Block block = Blocks.stone;
        int meta = 0;

        if (entity instanceof EntityLivingBase) {
            ItemStack heldItem = ((EntityLivingBase) entity).getEquipmentInSlot(0);
            if (heldItem != null && heldItem.getItem() instanceof ItemBlock) {
                block = Block.getBlockFromItem(heldItem.getItem());
                meta = heldItem.getItemDamage();
            } else if (entity instanceof EntityBlockNPC) {
                EntityBlockNPC npc = (EntityBlockNPC) entity;
                block = Block.getBlockById(npc.getBlockId());
                meta = npc.getBlockMeta();
            }
        }

        if (block == null || block == Blocks.air) block = Blocks.stone;

        GL11.glPushMatrix();

        GL11.glTranslatef(0.0F, 1.5F, 0.0F);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);

        if (entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) entity;

            Timer timer = ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "timer", "field_71428_T");
            float partialTicks = timer.renderPartialTicks;

            float interpYaw = living.prevRenderYawOffset + (living.renderYawOffset - living.prevRenderYawOffset) * partialTicks;

            GL11.glRotatef(interpYaw - 180.0F, 0.0F, 1.0F, 0.0F);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        RenderHelper.disableStandardItemLighting();

        GL11.glTranslatef(0.0F, 0.5F, 0.0F);

        renderBlocks.renderBlockAsItem(block, meta, 1.0F);

        RenderHelper.enableStandardItemLighting();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);

        GL11.glPopMatrix();
    }
}