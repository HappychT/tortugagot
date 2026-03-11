package got.client.gui;

import got.common.entity.animal.GOTEntityHorse;
import got.common.inventory.GOTContainerMountInventory;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.StatCollector;

public class GOTGuiMountInventory extends GuiScreenHorseInventory {

	private final GOTEntityHorse mount;
	private final IInventory playerInventory;

	public GOTGuiMountInventory(IInventory playerInv, IInventory horseInv, GOTEntityHorse horse) {
		super(playerInv, horseInv, horse);
		inventorySlots = new GOTContainerMountInventory(playerInv, horseInv, horse);
		this.mount = horse;
		this.playerInventory = playerInv;
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
	}

	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRendererObj.drawString(getMountDisplayName(), 8, 6, 4210752);
		fontRendererObj.drawString(playerInventory.hasCustomInventoryName() ? playerInventory.getInventoryName() : StatCollector.translateToLocal(playerInventory.getInventoryName()), 8, ySize - 96 + 2, 4210752);
		if (mount == null || mount.isDead) {
			return;
		}
		drawStatsPanel();
	}

	private String getMountDisplayName() {
		if (mount == null) {
			return StatCollector.translateToLocal("got.bridle_mount");
		}
		if (mount.hasCustomNameTag()) {
			return mount.getCustomNameTag();
		}
		return mount.getCommandSenderName();
	}

	private void drawStatsPanel() {
		GOTGuiMountStats.draw((left, top, right, bottom, color) -> drawRect(left, top, right, bottom, color), fontRendererObj, mount, mount.getTotalArmorValue());
	}
}
