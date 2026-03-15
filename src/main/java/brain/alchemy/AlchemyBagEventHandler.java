package brain.alchemy;

import got.common.item.potions.GOTItemLingeringPotion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class AlchemyBagEventHandler {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {

            EntityPlayer player = event.entityPlayer;
            ItemStack stack = player.getCurrentEquippedItem();

            if (stack != null && stack.getItem() instanceof ItemAlchemyBag) {
                if (!event.world.isRemote) {
                    ((ItemAlchemyBag) stack.getItem()).cycleActivePotion(stack, player);
                }
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onItemPickup(EntityItemPickupEvent event) {
        EntityPlayer player = event.entityPlayer;
        ItemStack pickedItem = event.item.getEntityItem();

        if (pickedItem != null) {
            boolean isSplash = pickedItem.getItem() == Items.potionitem && ItemPotion.isSplash(pickedItem.getItemDamage());
            boolean isLingering = pickedItem.getItem() instanceof GOTItemLingeringPotion;

            if (isSplash || isLingering) {
                ItemStack bagStack = findAlchemyBagInInventory(player);
                if (bagStack != null) {
                    ItemAlchemyBag bag = (ItemAlchemyBag) bagStack.getItem();
                    if (bag.addPotionSilent(bagStack, pickedItem, player)) {
                        event.item.setDead();
                        event.setCanceled(true);
                        player.worldObj.playSoundAtEntity(player, "random.pop", 0.2F, 2.0F);
                    }
                }
            }
        }
    }

    private ItemStack findAlchemyBagInInventory(EntityPlayer player) {

        for (int i = 0; i < player.inventory.mainInventory.length; i++) {
            ItemStack stack = player.inventory.mainInventory[i];
            if (stack != null && stack.getItem() instanceof ItemAlchemyBag) {
                return stack;
            }
        }
        return null;
    }

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {

    }
}
