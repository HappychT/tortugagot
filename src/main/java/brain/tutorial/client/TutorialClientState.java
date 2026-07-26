package brain.tutorial.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class TutorialClientState {
    public static boolean isTutorialActive = false;
    public static int tutorialStage = 0;
    public static int tutorialProgress = 0;
    public static String currentSubtitle = "";
    public static Set<Integer> tutorialEntities = new HashSet<>();
    
    public static boolean inQueue = false;
    public static int queuePosition = 0;
    public static int queueTotal = 0;
    
    // Fade state: 0=none, 1=fading-out (to black), 2=black, 3=fading-in (to clear)
    public static int fadeState = 0;
    public static long fadeStartTime = 0;
    
    public static void startFadeOut() {
        fadeState = 1;
        fadeStartTime = net.minecraft.client.Minecraft.getSystemTime();
    }
    
    public static void startFadeIn() {
        fadeState = 3;
        fadeStartTime = net.minecraft.client.Minecraft.getSystemTime();
    }
    
    public static float getFadeAlpha() {
        if (fadeState == 0) return 0f;
        if (fadeState == 2) return 1f;
        
        long elapsed = net.minecraft.client.Minecraft.getSystemTime() - fadeStartTime;
        float progress = Math.min(1f, elapsed / 1000f); // 1 second fade duration
        
        if (fadeState == 1) { // fading to black
            if (progress >= 1f) fadeState = 2;
            return progress;
        } else if (fadeState == 3) { // fading to clear
            if (progress >= 1f) {
                fadeState = 0;
                return 0f;
            }
            return 1f - progress;
        }
        return 0f;
    }
    
    public static boolean isFading() {
        return fadeState != 0;
    }

    public static int getExpectedMenuButtonForProgress() {
        if (tutorialProgress == 0) return 2; // Achievements
        if (tutorialProgress == 1) return 3; // Map
        if (tutorialProgress == 4) return 4; // Factions
        if (tutorialProgress == 36) return 8; // Languages
        if (tutorialProgress == 37) return 6; // Fellowships
        if (tutorialProgress == 43) return 7; // Titles
        if (tutorialProgress == 44) return 5; // Attributes
        if (tutorialProgress == 45) return 1; // Options
        return -1;
    }

    // Client-side text definitions
    private static final Map<Integer, String[]> subtitles = new HashMap<>();
    
    // Add flags for specific allowed actions based on current stage
    public static boolean isMenuAllowed() {
        return !isTutorialActive || tutorialStage >= 3;
    }

    public static String getSubStepSubtitle() {
        return TutorialTextsClient.get("substep." + tutorialStage + "." + tutorialProgress);
    }

    public static boolean isChatAllowed() {
        if (!isTutorialActive) return true;
        return false;
    }

    public static boolean isTeleportAllowed() {
        if (!isTutorialActive) return true;
        return false;
    }
}
