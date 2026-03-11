package noname.weapons.render;

import noname.weapons.entity.EntityBatteringRam;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBatteringRam extends Render {

    private IModelCustom model;
    private IModelCustom modelArm1;
    private long lastAnimationStartTime = 0;
    private boolean wasBreaking = false;
    private float currentAnimationTime = 0.0f;
    private int lastProcessedAnimationState = 0;

    private static final ResourceLocation texture = new ResourceLocation("got", "textures/entity/noname/taran.png");
    private static final float Y_OFFSET = 0F;

    public RenderBatteringRam() {
        this.shadowSize = 1.8F;
        this.model = AdvancedModelLoader.loadModel(new ResourceLocation("got", "models/taran_based.obj"));
        this.modelArm1 = AdvancedModelLoader.loadModel(new ResourceLocation("got", "models/taranArm1.obj"));
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        EntityBatteringRam ram = (EntityBatteringRam) entity;
        float renderYaw = ram.prevRotationYaw + (ram.rotationYaw - ram.prevRotationYaw) * partialTicks;

        GL11.glPushMatrix();
        GL11.glTranslated(x, y + Y_OFFSET, z);
        GL11.glRotatef(180.0F - renderYaw, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.bindEntityTexture(entity);
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        model.renderAll();
        if (modelArm1 != null) {
            float rotationAngle = calculateArmRotation(ram, partialTicks);
            GL11.glPushMatrix();
            GL11.glTranslatef(0, 1.2f, 0.0f);
            GL11.glRotatef(rotationAngle, 1, 0, 0);
            GL11.glTranslatef(0, -1.2f, 0.0f);
            modelArm1.renderAll();
            GL11.glPopMatrix();
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        renderStatusOverlay(ram, x, y, z);
    }

    private void renderStatusOverlay(EntityBatteringRam ram, double x, double y, double z) {
        float health = ram.getHealth();
        float maxHealth = ram.getMaxHealth();
        if (health >= maxHealth && ram.getPassengerCount() == 0) return;

        double distSq = x * x + y * y + z * z;
        if (distSq > 1024.0D) return;

        float healthPct = health / maxHealth;
        int barWidth = 80;
        int barHeight = 5;
        int barX = -barWidth / 2;

        GL11.glPushMatrix();
        GL11.glTranslated(x, y + 3.2D, z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-0.025F, -0.025F, 0.025F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        Tessellator tess = Tessellator.instance;

        tess.startDrawingQuads();
        tess.setColorRGBA(0, 0, 0, 100);
        tess.addVertex(barX - 1, -1, 0);
        tess.addVertex(barX - 1, barHeight + 1, 0);
        tess.addVertex(barX + barWidth + 1, barHeight + 1, 0);
        tess.addVertex(barX + barWidth + 1, -1, 0);
        tess.draw();

        int fillWidth = (int) (barWidth * healthPct);
        int r = (int) ((1.0f - healthPct) * 255);
        int g = (int) (healthPct * 255);

        tess.startDrawingQuads();
        tess.setColorRGBA(r, g, 0, 200);
        tess.addVertex(barX, 0, 0);
        tess.addVertex(barX, barHeight, 0);
        tess.addVertex(barX + fillWidth, barHeight, 0);
        tess.addVertex(barX + fillWidth, 0, 0);
        tess.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        FontRenderer fr = this.renderManager.getFontRenderer();
        if (fr != null) {
            String hpText = String.format("%.0f / %.0f", health, maxHealth);
            int textWidth = fr.getStringWidth(hpText);
            fr.drawString(hpText, -textWidth / 2, barHeight + 3, 0xFFFFFF);

            int crew = ram.getPassengerCount();
            if (crew > 0) {
                String driverName = "";
                if (ram.riddenByEntity instanceof EntityPlayer) {
                    driverName = ((EntityPlayer) ram.riddenByEntity).getDisplayName();
                }
                String crewText = crew + "/7";
                if (!driverName.isEmpty()) {
                    crewText = driverName + " | " + crewText;
                }
                int crewWidth = fr.getStringWidth(crewText);
                fr.drawString(crewText, -crewWidth / 2, barHeight + 14, 0xCCCCCC);
            }
        }

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    private float calculateArmRotation(EntityBatteringRam batteringRam, float partialTicks) {
        int animationState = batteringRam.getDataWatcher().getWatchableObjectInt(25);

        boolean active = animationState != 0;

        if (active && !wasBreaking) {
            lastAnimationStartTime = System.currentTimeMillis();
            wasBreaking = true;
        }

        if (!active) {
            wasBreaking = false;
            return 0.0f;
        }

        float maxAngle = 10.0f;
        float cycleDuration = 1.0f;
        long currentTime = System.currentTimeMillis();
        float elapsedSeconds = (currentTime - lastAnimationStartTime) / 1000.0f;

        float cycleTime = elapsedSeconds % cycleDuration;

        float normalizedTime = cycleTime / cycleDuration;
        float angle;
        if (normalizedTime < 0.5f) {
            float phaseProgress = normalizedTime / 0.5f;
            angle = -maxAngle * smoothStep(phaseProgress);
        } else {
            float phaseProgress = (normalizedTime - 0.5f) / 0.5f;
            angle = -maxAngle * (1.0f - smoothStep(phaseProgress));
        }

        return angle;
    }

    private float smoothStep(float t) {
        return t * t * (3.0f - 2.0f * t);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }
}
