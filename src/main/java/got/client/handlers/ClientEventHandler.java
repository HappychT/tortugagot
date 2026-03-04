package got.client.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.client.GOTClientProxy;
import got.common.database.GOTEffects;
import got.common.database.GOTRegistry;
import got.common.item.other.GOTItemBandage;
import got.common.item.other.GOTItemBridle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final RenderItem itemRenderer = new RenderItem();

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase living = event.entityLiving;

        if (living.worldObj.isRemote && living instanceof EntityPlayerSP) {
            EntityPlayerSP player = (EntityPlayerSP) living;


            boolean hasInversion = player.isPotionActive(GOTEffects.inversion);
            boolean isUsingInvertedInput = player.movementInput instanceof GOTMovementInputInverted;

            if (hasInversion && !isUsingInvertedInput) {
                player.movementInput = new GOTMovementInputInverted(player.movementInput);
            }
            else if (!hasInversion && isUsingInvertedInput) {
                player.movementInput = ((GOTMovementInputInverted) player.movementInput).originalInput;
            }

        }
    }


    @SideOnly(Side.CLIENT)
    private void invertMovement(Minecraft mc) {
        GameSettings settings = mc.gameSettings;

        boolean forward = Keyboard.isKeyDown(settings.keyBindForward.getKeyCode());
        boolean back = Keyboard.isKeyDown(settings.keyBindBack.getKeyCode());
        boolean left = Keyboard.isKeyDown(settings.keyBindLeft.getKeyCode());
        boolean right = Keyboard.isKeyDown(settings.keyBindRight.getKeyCode());
        boolean jump = Keyboard.isKeyDown(settings.keyBindJump.getKeyCode());
        boolean sneak = Keyboard.isKeyDown(settings.keyBindSneak.getKeyCode());

        mc.thePlayer.movementInput.moveForward = 0;
        mc.thePlayer.movementInput.moveStrafe = 0;

        if (forward) mc.thePlayer.movementInput.moveForward -= 1.0F;
        if (back) mc.thePlayer.movementInput.moveForward += 1.0F;
        if (left) mc.thePlayer.movementInput.moveStrafe += 1.0F;
        if (right) mc.thePlayer.movementInput.moveStrafe -= 1.0F;

        mc.thePlayer.movementInput.jump = sneak;
        mc.thePlayer.movementInput.sneak = jump;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderHUD(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.HOTBAR) return;

        EntityPlayer player = mc.thePlayer;
        if (player == null) return;

        if (player.getHeldItem() != null) {
            Item heldItem = player.getHeldItem().getItem();
            Integer hitCount = GOTClientProxy.weaponHitCounts.get(heldItem);

            if (hitCount != null) {
                String text = "Комбо: " + hitCount + " / 3";
                int color = 0xFFFFFF;
                int x = event.resolution.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(text) / 2;
                int y = event.resolution.getScaledHeight() - 55;
                mc.fontRenderer.drawStringWithShadow(text, x, y, color);
            }
        }

        if (player.isUsingItem() && player.getItemInUse().getItem() instanceof GOTItemBandage) {
            ItemStack bandageStack = player.getItemInUse();


            int maxUseDurationTicks = player.inventory.hasItem(GOTRegistry.gauzeSet) ? 60 : 100;

            int elapsedTicks = player.getItemInUseCount();

            int remainingTicks = player.getItemInUseCount();

            float remainingSeconds = (float) remainingTicks / 20.0f;
            if (remainingSeconds < 0) remainingSeconds = 0.0f;


            int screenWidth = event.resolution.getScaledWidth();
            int screenHeight = event.resolution.getScaledHeight();

            int x = screenWidth / 2 + 91 + 10;
            int y = screenHeight - 18;

            GL11.glPushMatrix();
            try {
                RenderHelper.enableGUIStandardItemLighting();

                ItemStack renderStack = new ItemStack(bandageStack.getItem());
                itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), renderStack, x, y - 2);

                RenderHelper.disableStandardItemLighting();

                String timerText = String.format("%.1f", remainingSeconds);
                int textX = x + 20;
                int textY = y + 4;
                mc.fontRenderer.drawStringWithShadow(timerText, textX, textY, 0xFFFFFF);

            } finally {
                GL11.glPopMatrix();
            }
        }

        if (player.isUsingItem() && player.getItemInUse() != null && player.getItemInUse().getItem() instanceof GOTItemBridle) {
            drawBridleSummonProgress(event, player);
        }
    }

    /** Оставшееся время удержания поводья (8.0 → 0.0 с) над хотбаром. */
    private void drawBridleSummonProgress(RenderGameOverlayEvent.Post event, EntityPlayer player) {
        int remainingTicks = player.getItemInUseCount();
        if (remainingTicks <= 0) return;

        float remainingSec = (float) remainingTicks / 20.0f;
        if (remainingSec < 0) remainingSec = 0;

        int screenWidth = event.resolution.getScaledWidth();
        int screenHeight = event.resolution.getScaledHeight();
        String text = String.format("%.1f", remainingSec);
        int x = screenWidth / 2 - mc.fontRenderer.getStringWidth(text) / 2;
        int y = screenHeight - 58;
        mc.fontRenderer.drawStringWithShadow(text, x, y, 0xFFFFFF);
    }
}