package got.client.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import got.common.network.GOTPacketActivateRam;
import got.common.network.GOTPacketHandler;
import got.common.network.base.PacketDispatcher;
import got.common.network.clientToServer.PacketBounceRequest;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import noname.weapons.entity.EntityBatteringRam;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GOTClientStaminaHandler {

    public static final GOTClientStaminaHandler INSTANCE = new GOTClientStaminaHandler();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            onClientTick();


            Minecraft mc = Minecraft.getMinecraft();

            EntityPlayer player = mc.thePlayer;

            if (player != null && mc.currentScreen == null) {

                boolean swing = Mouse.isButtonDown(0);

                if (player.ridingEntity instanceof EntityBatteringRam) {
                    EntityBatteringRam batteringRam = (EntityBatteringRam) player.ridingEntity;
                    GOTPacketHandler.networkWrapper.sendToServer(new GOTPacketActivateRam(batteringRam.getEntityId(), swing));
                }


            }
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
                int direction = getMovementDirection(leftKey, rightKey, backKey);
                if (direction != 0) {
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

    private int getMovementDirection(boolean leftKey, boolean rightKey, boolean backKey) {
        if (backKey) return 1;
        if (rightKey) return 2;
        if (leftKey) return 3;
        return 0;
    }
}