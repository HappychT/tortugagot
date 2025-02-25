package got.client.model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import got.common.entity.other.GOTEntityHuornBase;
import got.common.entity.other.GOTEntityTree;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class GOTModelHuorn extends ModelBase {
    private List<ModelRenderer> woodBlocks = new ArrayList<ModelRenderer>();
    private List<ModelRenderer> leafBlocks = new ArrayList<ModelRenderer>();
    private ModelRenderer face;
    private int baseX = 2;
    private int baseY = 0;
    private int baseZ = 2;
    private Random rand = new Random();

    public GOTModelHuorn() {
        int j;
        this.rand.setSeed(100L);
        int height = 6;
        int leafStart = 3;
        int leafRangeMin = 0;
        for (j = this.baseY - leafStart + height; j <= this.baseY + height; ++j) {
            int j1 = j - (this.baseY + height);
            int leafRange = leafRangeMin + 1 - j1 / 2;
            for (int i = this.baseX - leafRange; i <= this.baseX + leafRange; ++i) {
                int i1 = i - this.baseX;
                for (int k = this.baseZ - leafRange; k <= this.baseZ + leafRange; ++k) {
                    int k1 = k - this.baseZ;
                    if (Math.abs(i1) == leafRange && Math.abs(k1) == leafRange && (this.rand.nextInt(2) == 0 || j1 == 0)) {
                        continue;
                    }
                    ModelRenderer block = new ModelRenderer(this, 0, 0);
                    block.addBox(-8.0f, -8.0f, -8.0f, 16, 16, 16);
                    block.setRotationPoint(i * 16.0f, 16.0f - j * 16.0f, k * 16.0f);
                    this.leafBlocks.add(block);
                }
            }
        }
        for (j = 0; j < height; ++j) {
            ModelRenderer block = new ModelRenderer(this, 0, 0);
            block.addBox(-8.0f, -8.0f, -8.0f, 16, 16, 16);
            block.setRotationPoint(this.baseX * 16.0f, 16.0f - j * 16.0f, this.baseZ * 16.0f);
            this.woodBlocks.add(block);
        }
        this.face = new ModelRenderer(this, 0, 0);
        this.face.addBox(-8.0f, -8.0f, -8.0f, 16, 16, 16, 0.5f);
        this.face.setRotationPoint(0.0f, 0.0f, 0.0f);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        ModelRenderer block;
        int i;
        GOTEntityHuornBase huorn = (GOTEntityHuornBase)entity;
        if (huorn.isHuornActive()) {
            this.face.render(f5);
        }
        GL11.glPushMatrix();
        GL11.glEnable(2884);
        GL11.glTranslatef((-((float)this.baseX)), (-((float)this.baseY)), (-((float)this.baseZ)));
        for (i = 0; i < this.woodBlocks.size(); ++i) {
            if (i == 0) {
                GOTHuornTextures.INSTANCE.bindWoodTexture(huorn);
            }
            block = this.woodBlocks.get(i);
            block.render(f5);
        }
        for (i = 0; i < this.leafBlocks.size(); ++i) {
            if (i == 0) {
                GOTHuornTextures.INSTANCE.bindLeafTexture(huorn);
            }
            block = this.leafBlocks.get(i);
            block.render(f5);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(2884);
        GL11.glPopMatrix();
    }

    public static class GOTHuornTextures implements IResourceManagerReloadListener {
        public static GOTHuornTextures INSTANCE = new GOTHuornTextures();
        private RenderManager renderManager = RenderManager.instance;
        private HashMap<Integer, ResourceLocation> woodTextures = new HashMap<Integer, ResourceLocation>();
        private HashMap<Integer, ResourceLocation> leafTexturesFast = new HashMap<Integer, ResourceLocation>();
        private HashMap<Integer, ResourceLocation> leafTexturesFancy = new HashMap<Integer, ResourceLocation>();

        @Override
        public void onResourceManagerReload(IResourceManager resourcemanager) {
            this.woodTextures.clear();
            this.leafTexturesFast.clear();
            this.leafTexturesFancy.clear();
        }

        public void bindWoodTexture(GOTEntityHuornBase entity) {
            int treeType = entity.getTreeType();
            Block block = GOTEntityTree.WOOD_BLOCKS[treeType];
            int meta = GOTEntityTree.WOOD_META[treeType];
            ResourceLocation texture = this.woodTextures.get(treeType);
            if (texture == null) {
                texture = getDynamicHuornTexture(block, meta);
                this.woodTextures.put(treeType, texture);
            }
            this.renderManager.renderEngine.bindTexture(texture);
        }

        public void bindLeafTexture(GOTEntityHuornBase entity) {
            int treeType = entity.getTreeType();
            Block block = GOTEntityTree.LEAF_BLOCKS[treeType];
            int meta = GOTEntityTree.LEAF_META[treeType];
            ResourceLocation texture = this.leafTexturesFast.get(treeType);
            if (Minecraft.isFancyGraphicsEnabled()) {
                texture = this.leafTexturesFancy.get(treeType);
            }
            if (texture == null) {
                texture = getDynamicHuornTexture(block, meta);
                if (Minecraft.isFancyGraphicsEnabled()) {
                    this.leafTexturesFancy.put(treeType, texture);
                } else {
                    this.leafTexturesFast.put(treeType, texture);
                }
            }
            this.renderManager.renderEngine.bindTexture(texture);
            int color = block.getRenderColor(meta);
            if (block == Blocks.leaves && meta == 0) {
                int i = MathHelper.floor_double(entity.posX);
                int j = MathHelper.floor_double(entity.boundingBox.minY);
                int k = MathHelper.floor_double(entity.posZ);
                color = entity.worldObj.getBiomeGenForCoords(i, k).getBiomeFoliageColor(i, j, k);
            }
            float r = (color >> 16 & 255) / 255.0f;
            float g = (color >> 8 & 255) / 255.0f;
            float b = (color & 255) / 255.0f;
            GL11.glColor4f(r, g, b, 1.0f);
        }

        private ResourceLocation getDynamicHuornTexture(Block block, int meta) {
            try {
                boolean aF = Minecraft.getMinecraft().gameSettings.anisotropicFiltering > 1;
                BufferedImage[] icons = new BufferedImage[6];
                int width = block.getIcon(0, meta).getIconWidth();
                if (aF) {
                    width -= 16;
                }
                for (int i = 0; i < 6; ++i) {
                    IIcon icon = block.getIcon(i, meta);
                    int iconWidth = icon.getIconWidth();
                    int iconHeight = icon.getIconHeight();
                    if (aF) {
                        iconWidth -= 16;
                        iconHeight -= 16;
                    }
                    if (iconWidth != width || iconHeight != width)
                        throw new RuntimeException("Error registering Huorn textures: all icons for block " + block.getUnlocalizedName() + " must have the same texture dimensions");
                    ResourceLocation iconPath = new ResourceLocation(icon.getIconName());
                    ResourceLocation r = new ResourceLocation(iconPath.getResourceDomain(), "textures/blocks/" + iconPath.getResourcePath() + ".png");
                    BufferedImage iconImage = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(r).getInputStream());
                    icons[i] = iconImage.getSubimage(0, 0, width, width);
                }
                BufferedImage image = new BufferedImage(width * 4, width * 2, 2);
                Graphics2D g2d = image.createGraphics();
                g2d.drawImage(icons[1], null, width, 0);
                g2d.drawImage(icons[0], null, width * 2, 0);
                g2d.drawImage(icons[4], null, 0, width);
                g2d.drawImage(icons[2], null, width, width);
                g2d.drawImage(icons[5], null, width * 2, width);
                g2d.drawImage(icons[3], null, width * 3, width);
                g2d.dispose();
                return this.renderManager.renderEngine.getDynamicTextureLocation("got:huorn", new DynamicTexture(image));
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        static {
            ((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(INSTANCE);
        }
    }
}