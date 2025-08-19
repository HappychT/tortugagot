package got.common.systems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AccessorySystem {
    public static boolean hasAccessory(EntityPlayer player, Item accessory) {
        if (player == null || accessory == null) {
            return false;
        }

        for (int i = 0; i < player.inventory.mainInventory.length; i++) {
            ItemStack stack = player.inventory.mainInventory[i];
            if (stack != null && stack.getItem() == accessory) {
                return true;
            }
        }
        return false;
    }
}