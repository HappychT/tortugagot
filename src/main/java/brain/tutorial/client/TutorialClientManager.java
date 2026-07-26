package brain.tutorial.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraft.entity.Entity;

import java.util.List;

public class TutorialClientManager {

    public static final TutorialClientManager INSTANCE = new TutorialClientManager();
    private float currentZoom = 1.0f;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityClientPlayerMP player = mc.thePlayer;
        if (player == null || mc.theWorld == null) return;
        
        // Видимость NPC теперь управляется через GOTEntityTutorialNPC.isInvisibleToPlayer(),
        // что надёжнее чем setInvisible (не сбивается серверной синхронизацией DataWatcher).

        if (TutorialClientState.isTutorialActive) {
            if (TutorialClientState.tutorialStage == 2 || TutorialClientState.tutorialStage == 10 || 
               (TutorialClientState.tutorialStage == 9 && TutorialClientState.tutorialProgress == 1)) {
                // Block movement
                player.movementInput.moveForward = 0.0f;
                player.movementInput.moveStrafe = 0.0f;
                player.movementInput.jump = false;
                player.movementInput.sneak = false;
            }

            if (TutorialClientState.tutorialStage == 2) {

                // Look at Captain
                List<got.common.entity.tutorial.GOTEntityTutorialCaptain> list = mc.theWorld.getEntitiesWithinAABB(
                        got.common.entity.tutorial.GOTEntityTutorialCaptain.class, 
                        player.boundingBox.expand(15, 15, 15));
                
                for (got.common.entity.tutorial.GOTEntityTutorialCaptain captain : list) {
                    double d0 = captain.posX - player.posX;
                    double d1 = captain.posY + captain.getEyeHeight() - (player.posY + player.getEyeHeight());
                    double d2 = captain.posZ - player.posZ;
                    double d3 = (double)net.minecraft.util.MathHelper.sqrt_double(d0 * d0 + d2 * d2);
                    
                    float targetYaw = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
                    float targetPitch = (float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));
                    
                    // Smooth look
                    player.rotationYaw = wrapAngleTo180(player.rotationYaw);
                    targetYaw = wrapAngleTo180(targetYaw);
                    
                    float diffYaw = targetYaw - player.rotationYaw;
                    diffYaw = wrapAngleTo180(diffYaw);
                    
                    player.rotationYaw += diffYaw * 0.1f;
                    player.rotationPitch += (targetPitch - player.rotationPitch) * 0.1f;
                    break; // Just use the first one
                }
            }
        }
    }

    @SubscribeEvent
    public void onFOVUpdate(FOVUpdateEvent event) {
        if (TutorialClientState.isTutorialActive && TutorialClientState.tutorialStage == 2) {
            if (currentZoom > 0.6f) {
                currentZoom -= 0.02f;
            }
            event.newfov *= currentZoom;
        } else {
            if (currentZoom < 1.0f) {
                currentZoom += 0.05f;
                event.newfov *= currentZoom;
            } else {
                currentZoom = 1.0f;
            }
        }
    }

    private float wrapAngleTo180(float value) {
        value %= 360.0F;
        if (value >= 180.0F) {
            value -= 360.0F;
        }
        if (value < -180.0F) {
            value += 360.0F;
        }
        return value;
    }
}
