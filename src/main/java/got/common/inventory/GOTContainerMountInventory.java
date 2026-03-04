package got.common.inventory;

import java.util.ArrayList;

import got.common.entity.animal.GOTEntityHorse;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * Инвентарь маунта без слота брони — только седло и слоты игрока.
 */
public class GOTContainerMountInventory extends ContainerHorseInventory {

	public GOTContainerMountInventory(IInventory playerInv, IInventory horseInv, GOTEntityHorse horse) {
		super(playerInv, horseInv, horse);
		ArrayList slots = new ArrayList(inventorySlots);
		inventorySlots.clear();
		inventoryItemStacks.clear();
		addSlotToContainer((Slot) slots.get(0)); // седло
		// слот 1 (броня) не добавляем — надевание брони через GUI отключено
		for (int i = 2; i < slots.size(); ++i) {
			addSlotToContainer((Slot) slots.get(i));
		}
	}
}
