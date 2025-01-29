package got.client.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import got.common.network.base.PacketDispatcher;
import got.common.network.clientToServer.PacketBounceRequest;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GOTClientStaminaHandler {

    public static final GOTClientStaminaHandler INSTANCE = new GOTClientStaminaHandler();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            onClientTick();
        }
    }

    public void onClientTick() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        if (player != null && Keyboard.isCreated() && mc.inGameHasFocus && mc.gameSettings != null) {
            boolean jumpKey = isKeyDownSafe(mc.gameSettings.keyBindJump.getKeyCode());
            boolean rightMouseButton = player.isBlocking(); // Check for right mouse button press
            boolean sneakKey = isKeyDownSafe(mc.gameSettings.keyBindSneak.getKeyCode());
            boolean leftKey = isKeyDownSafe(mc.gameSettings.keyBindLeft.getKeyCode());
            boolean rightKey = isKeyDownSafe(mc.gameSettings.keyBindRight.getKeyCode());
            boolean backKey = isKeyDownSafe(mc.gameSettings.keyBindBack.getKeyCode());
            //System.out.println(jumpKey + " " + rightMouseButton + " " + sneakKey + " " + leftKey + " " + rightKey + " " + backKey);

            if (jumpKey && rightMouseButton) {
                String direction = getMovementDirection(leftKey, rightKey, backKey);
                if (!direction.isEmpty()) {
                    //System.out.println("Bouncing in " + direction);
                    PacketBounceRequest packet = new PacketBounceRequest(direction);
                    PacketDispatcher.sendToServer(packet);
                }
            }
        }
    }

    private boolean isKeyDownSafe(int keyCode) {
        try {
            return Keyboard.isKeyDown(keyCode);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    private String getMovementDirection(boolean leftKey, boolean rightKey, boolean backKey) {
        if (backKey) return "backward";
        if (rightKey) return "right";
        if (leftKey) return "left";
        return "";
    }
}