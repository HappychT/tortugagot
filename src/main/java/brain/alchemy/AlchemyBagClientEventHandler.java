package brain.alchemy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AlchemyBagClientEventHandler {

    private final RenderItem itemRender = new RenderItem();

    @SubscribeEvent
    public void onMouseEvent(MouseEvent event) {
        if (event.button == 0 && event.buttonstate) {
            Minecraft mc = Minecraft.getMinecraft();
            ItemStack stack = mc.thePlayer.getCurrentEquippedItem();

            if (stack != null && stack.getItem() instanceof ItemAlchemyBag) {
                MovingObjectPosition mop = mc.objectMouseOver;
                if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.MISS) {
                    AlchemyBagMod.network.sendToServer(new PacketCyclePotion());
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            Minecraft mc = Minecraft.getMinecraft();
            ItemStack bag = mc.thePlayer.getCurrentEquippedItem();

            if (bag != null && bag.getItem() instanceof ItemAlchemyBag) {
                renderActivePotion(mc, bag, event.resolution);
            }
        }
    }

    private void renderActivePotion(Minecraft mc, ItemStack bag, ScaledResolution res) {
        if (!bag.hasTagCompound()) return;
        NBTTagCompound nbt = bag.getTagCompound();
        
        int activeSlot = nbt.getInteger("ActiveSlot");
        NBTTagList potions = nbt.getTagList("Potions", 10);
        
        ItemStack activePotionStack = null;

        for (int i = 0; i < potions.tagCount(); i++) {
            NBTTagCompound potionTag = potions.getCompoundTagAt(i);
            if (potionTag.getInteger("Slot") == activeSlot) {
                activePotionStack = ItemStack.loadItemStackFromNBT(potionTag);
                activePotionStack.stackSize = potionTag.getInteger("Count");
                break;
            }
        }

        if (activePotionStack != null) {
            int width = res.getScaledWidth();
            int height = res.getScaledHeight();

            int x = width / 2 - 8;
            int y = height - 75; 

            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.enableGUIStandardItemLighting();
            
            itemRender.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), activePotionStack, x, y);
            itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), activePotionStack, x, y);
            
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            
        }
    }
}