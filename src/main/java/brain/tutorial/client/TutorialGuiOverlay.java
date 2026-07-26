package brain.tutorial.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TutorialGuiOverlay {

    public static final TutorialGuiOverlay INSTANCE = new TutorialGuiOverlay();

    private TutorialGuiOverlay() {}

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        if (Minecraft.getMinecraft().currentScreen == null) {
            drawTutorialOverlay();
        }
    }

    @SubscribeEvent
    public void onGuiScreenRender(net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Post event) {
        drawTutorialOverlay();
    }

    private void drawTutorialOverlay() {
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fr = mc.fontRenderer;
        ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

        if (TutorialClientState.inQueue) {
            String qText = String.format(TutorialTextsClient.get("queue.status"), TutorialClientState.queuePosition);
            int qX = sr.getScaledWidth() - fr.getStringWidth(qText) - 10;
            int qY = 10;
            fr.drawStringWithShadow(qText, qX, qY, 0xFFAA00);
        }

        if (TutorialClientState.isTutorialActive) {
            org.lwjgl.opengl.GL11.glPushMatrix();
            org.lwjgl.opengl.GL11.glTranslatef(0, 0, 500); // Draw above GUI
            
            String title = TutorialTextsClient.get("stage" + TutorialClientState.tutorialStage + ".title");
            String progress = "";
            
            if (TutorialClientState.tutorialStage == 1) {
                progress = String.format(TutorialTextsClient.get("stage1.progress"), TutorialClientState.tutorialProgress);
            } else if (TutorialClientState.tutorialStage == 3) {
                progress = String.format(TutorialTextsClient.get("stage3.progress"), TutorialClientState.tutorialProgress + 1);
            } else if (TutorialClientState.tutorialStage == 8) {
                progress = String.format(TutorialTextsClient.get("stage8.progress"), TutorialClientState.tutorialProgress);
            }
            
            int x = sr.getScaledWidth() - fr.getStringWidth(title) - 10;
            int y = 10;
            
            fr.drawStringWithShadow(title, x, y, 0xFFFFFF);
            
            if (!progress.isEmpty()) {
                int px = sr.getScaledWidth() - fr.getStringWidth(progress) - 10;
                fr.drawStringWithShadow(progress, px, y + 10, 0xAAAAAA);
            }

            if (TutorialClientState.tutorialStage == 7) {
                int p = TutorialClientState.tutorialProgress;
                if (p == 0) {
                    drawBeautifulBorder(sr.getScaledWidth() / 2 - 91, sr.getScaledHeight() - 40, 80, 10, 0xFF0000); // Health
                    drawBeautifulBorder(sr.getScaledWidth() / 2 - 91, sr.getScaledHeight() - 50, 80, 10, 0x00AAFF); // Armor
                    drawBeautifulBorder(sr.getScaledWidth() / 2 + 11, sr.getScaledHeight() - 40, 80, 10, 0xFFAA00); // Food
                } else if (p == 1) {
                    int aX = sr.getScaledWidth() / 2 - 10;
                    int aY = sr.getScaledHeight() + 40;
                    float newPosX = aX - 89.5f;
                    float newPosY = aY - 114 + 40;
                    drawBeautifulBorder((int)newPosX, (int)newPosY, 200, 14, 0x00FF00); // Stamina (width doubled to 200)
                } else if (p == 8) {
                    drawBlockAngleIndicator(mc, sr);
                }
            }

            String sub = TutorialClientState.currentSubtitle;
            if (TutorialClientState.tutorialStage == 3 || TutorialClientState.tutorialStage == 4) {
                int prog = TutorialClientState.tutorialProgress;
                // Шаги 33-35 — технические переходные шаги закрытия фракций, текст подавляем
                if (TutorialClientState.tutorialStage == 3 && prog >= 33 && prog <= 35) {
                    sub = "";
                } else {
                    String stepSub = TutorialClientState.getSubStepSubtitle();
                    if (stepSub != null && !stepSub.isEmpty()) {
                        sub = stepSub;
                    }
                }
                if (TutorialClientState.tutorialStage == 3 && prog == 0 && Minecraft.getMinecraft().currentScreen == null) {
                    sub = TutorialTextsClient.get("overlay.open_menu_prompt");
                }
                if (Minecraft.getMinecraft().currentScreen instanceof got.client.gui.faction.GOTGuiFactions || 
                    Minecraft.getMinecraft().currentScreen instanceof got.client.gui.GOTGuiFellowships) {
                    sub = ""; // Don't show subtitle, info panel handles it
                }
            }
            
            if (sub != null && !sub.isEmpty()) {
                // Word-wrap: ограничиваем ширину блока, а не растягиваем на весь экран
                int maxWidth = Math.min(400, sr.getScaledWidth() - 40);
                java.util.List<String> lines = fr.listFormattedStringToWidth(sub, maxWidth);
                int totalHeight = lines.size() * (fr.FONT_HEIGHT + 2);
                int subY = sr.getScaledHeight() - sr.getScaledHeight() / 3;

                int maxLineWidth = 0;
                for (String line : lines) {
                    maxLineWidth = Math.max(maxLineWidth, fr.getStringWidth(line));
                }
                int subX = sr.getScaledWidth() / 2 - maxLineWidth / 2;

                net.minecraft.client.gui.Gui.drawRect(subX - 6, subY - 6, subX + maxLineWidth + 6, subY + totalHeight + 4, 0xAA000000);
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    int lineX = sr.getScaledWidth() / 2 - fr.getStringWidth(line) / 2;
                    fr.drawStringWithShadow(line, lineX, subY + i * (fr.FONT_HEIGHT + 2), 0xFFFFFF);
                }
            }

            
            // Fade Transition Logic
            if (TutorialClientState.tutorialStage == 9 && TutorialClientState.tutorialProgress == 2) {
                if (!TutorialClientState.isFading() || TutorialClientState.fadeState == 3) {
                    TutorialClientState.startFadeOut();
                }
            } else if (TutorialClientState.tutorialStage == 10 && TutorialClientState.tutorialProgress == 0) {
                if (TutorialClientState.fadeState == 2) {
                    TutorialClientState.startFadeIn();
                }
            }

            if (TutorialClientState.isFading()) {
                float fadeAlpha = TutorialClientState.getFadeAlpha();
                if (fadeAlpha > 0f) {
                    org.lwjgl.opengl.GL11.glPushMatrix();
                    org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_BLEND);
                    org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_TEXTURE_2D);
                    org.lwjgl.opengl.GL11.glBlendFunc(org.lwjgl.opengl.GL11.GL_SRC_ALPHA, org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA);
                    org.lwjgl.opengl.GL11.glColor4f(0f, 0f, 0f, fadeAlpha);
                    
                    net.minecraft.client.renderer.Tessellator tessellator = net.minecraft.client.renderer.Tessellator.instance;
                    tessellator.startDrawingQuads();
                    tessellator.addVertex(0.0D, (double)sr.getScaledHeight(), 1000.0D);
                    tessellator.addVertex((double)sr.getScaledWidth(), (double)sr.getScaledHeight(), 1000.0D);
                    tessellator.addVertex((double)sr.getScaledWidth(), 0.0D, 1000.0D);
                    tessellator.addVertex(0.0D, 0.0D, 1000.0D);
                    tessellator.draw();
                    
                    org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_TEXTURE_2D);
                    org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_BLEND);
                    org.lwjgl.opengl.GL11.glPopMatrix();
                }
            }

            org.lwjgl.opengl.GL11.glPopMatrix();
        }
    }

    private void drawBlockAngleIndicator(Minecraft mc, ScaledResolution sr) {
        net.minecraft.entity.player.EntityPlayer player = mc.thePlayer;
        if (player == null || player.getHeldItem() == null) return;
        
        boolean isBlocking = player.isBlocking();
        if (!isBlocking) {
            try {
                if (got.common.handlers.BlockServerHandler.INSTANCE.isBlocking(player)) isBlocking = true;
            } catch (Exception e) {}
        }
        if (!isBlocking) return;
        
        got.common.systems.GOTCoreBlockingSystem.WeaponBlockData blockData = null;
        try {
            blockData = got.common.systems.GOTCoreBlockingSystem.getBlockData(player.getHeldItem().getItem().getClass(), player);
        } catch (Exception e) {
            return;
        }
        if (blockData == null) return;
            
        float leftAngle = blockData.getLeftBlockAngle();
        float rightAngle = blockData.getRightBlockAngle();
        
        int cx = sr.getScaledWidth() / 2;
        int cy = sr.getScaledHeight() / 2;
        int radius = 40;
        
        org.lwjgl.opengl.GL11.glPushMatrix();
        org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_TEXTURE_2D);
        org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_BLEND);
        org.lwjgl.opengl.GL11.glBlendFunc(org.lwjgl.opengl.GL11.GL_SRC_ALPHA, org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        // Draw background circle (thin gray)
        org.lwjgl.opengl.GL11.glColor4f(1f, 1f, 1f, 0.15f);
        org.lwjgl.opengl.GL11.glLineWidth(2.0f);
        org.lwjgl.opengl.GL11.glBegin(org.lwjgl.opengl.GL11.GL_LINE_LOOP);
        for (int i = 0; i < 360; i += 5) {
            double rad = Math.toRadians(i);
            org.lwjgl.opengl.GL11.glVertex2d(cx + Math.sin(rad) * radius, cy - Math.cos(rad) * radius);
        }
        org.lwjgl.opengl.GL11.glEnd();
        
        // Draw active block cone (green)
        org.lwjgl.opengl.GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.6f);
        org.lwjgl.opengl.GL11.glLineWidth(4.0f);
        org.lwjgl.opengl.GL11.glBegin(org.lwjgl.opengl.GL11.GL_LINE_STRIP);
        for (float angle = -leftAngle; angle <= rightAngle; angle += 2) {
            double rad = Math.toRadians(angle);
            org.lwjgl.opengl.GL11.glVertex2d(cx + Math.sin(rad) * radius, cy - Math.cos(rad) * radius);
        }
        org.lwjgl.opengl.GL11.glEnd();
        
        // Draw enemies as red dots on the radar
        java.util.List<net.minecraft.entity.Entity> entities = player.worldObj.getEntitiesWithinAABB(
                got.common.entity.tutorial.GOTEntityTutorialSailor.class, 
                player.boundingBox.expand(16, 16, 16));
        
        org.lwjgl.opengl.GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.9f);
        org.lwjgl.opengl.GL11.glPointSize(6.0f);
        org.lwjgl.opengl.GL11.glBegin(org.lwjgl.opengl.GL11.GL_POINTS);
        
        float rawYaw = player.rotationYaw;
        float realYaw = (rawYaw + 90) % 360;
        if (realYaw < 0) realYaw += 360;
        
        for (net.minecraft.entity.Entity e : entities) {
            double dx = e.posX - player.posX;
            double dz = e.posZ - player.posZ;
            float attackYaw = (float) Math.toDegrees(Math.atan2(dz, dx)) % 360;
            if (attackYaw < 0) attackYaw += 360;
            
            float diff = attackYaw - realYaw;
            diff = (diff + 360) % 360;
            
            float visualAngle = diff;
            if (visualAngle > 180) visualAngle -= 360; 
            
            double rad = Math.toRadians(visualAngle);
            org.lwjgl.opengl.GL11.glVertex2d(cx + Math.sin(rad) * radius, cy - Math.cos(rad) * radius);
        }
        org.lwjgl.opengl.GL11.glEnd();
        
        org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_TEXTURE_2D);
        org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_BLEND);
        org.lwjgl.opengl.GL11.glPopMatrix();
    }

    private void drawBeautifulBorder(int x, int y, int w, int h, int colorBase) {
        float alpha = 0.55f + 0.45f * (float) Math.sin(Minecraft.getSystemTime() / 300.0);
        int r = (colorBase >> 16) & 0xFF;
        int g = (colorBase >> 8) & 0xFF;
        int b = colorBase & 0xFF;
        
        int thickness = 2;
        int color = ((int)(alpha * 200) << 24) | (r << 16) | (g << 8) | b;
        int glowColor = ((int)(alpha * 80) << 24) | (r << 16) | (g << 8) | b;

        // Outer glow
        net.minecraft.client.gui.Gui.drawRect(x - thickness - 2, y - thickness - 2, x + w + thickness + 2, y - thickness, glowColor);
        net.minecraft.client.gui.Gui.drawRect(x - thickness - 2, y + h + thickness, x + w + thickness + 2, y + h + thickness + 2, glowColor);
        net.minecraft.client.gui.Gui.drawRect(x - thickness - 2, y - thickness, x - thickness, y + h + thickness, glowColor);
        net.minecraft.client.gui.Gui.drawRect(x + w + thickness, y - thickness, x + w + thickness + 2, y + h + thickness, glowColor);

        // Inner bright border
        net.minecraft.client.gui.Gui.drawRect(x - thickness, y - thickness, x + w + thickness, y, color);
        net.minecraft.client.gui.Gui.drawRect(x - thickness, y + h, x + w + thickness, y + h + thickness, color);
        net.minecraft.client.gui.Gui.drawRect(x - thickness, y, x, y + h, color);
        net.minecraft.client.gui.Gui.drawRect(x + w, y, x + w + thickness, y + h, color);
        
        // Add a transparent filled background to make it stand out more
        int bgColor = ((int)(alpha * 60) << 24) | (r << 16) | (g << 8) | b;
        net.minecraft.client.gui.Gui.drawRect(x, y, x + w, y + h, bgColor);
    }

    @SubscribeEvent
    public void onRenderPlayer(net.minecraftforge.client.event.RenderPlayerEvent.Pre event) {
        if (TutorialClientState.isTutorialActive) {
            if (event.entityPlayer != Minecraft.getMinecraft().thePlayer) {
                event.setCanceled(true); // Hide other players during tutorial
            }
        }
    }

    @SubscribeEvent
    public void onRenderLiving(net.minecraftforge.client.event.RenderLivingEvent.Pre event) {
        if (event.entity instanceof got.common.entity.tutorial.GOTEntityTutorialNPC) {
            if (TutorialClientState.isTutorialActive) {
                if (!TutorialClientState.tutorialEntities.contains(event.entity.getEntityId())) {
                    event.setCanceled(true); // Hide OTHER players' tutorial NPCs
                }
            } else {
                event.setCanceled(true); // Hide ALL tutorial NPCs if not in tutorial
            }
        }
    }
}


