package got.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashSet;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class ClientChannelingAnimator {

    private static final Set<Integer> ANIMATING_PLAYERS = new HashSet<>();

    public static void startAnimation(int entityId) {
        ANIMATING_PLAYERS.add(entityId);
    }

    public static void stopAnimation(int entityId) {
        ANIMATING_PLAYERS.remove(entityId);
    }

    public static boolean isAnimating(int entityId) {
        return ANIMATING_PLAYERS.contains(entityId);
    }
}