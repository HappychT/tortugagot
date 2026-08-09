package got.client.gui;

import got.common.entity.animal.GOTEntityHorse;
import got.common.inventory.GOTContainerMountInventory;
import got.common.item.other.GOTBridleMountStats;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.StatCollector;

public class GOTGuiMountInventory extends GuiScreenHorseInventory {

	private static final int PANEL_X = 79;
	private static final int PANEL_Y = 17;
	private static final int PANEL_W = 90;
	private static final int PANEL_H = 53;
	private static final int PAD = 4;
	private static final int ROW = 10;
	private static final int TITLE_COLOR = 0x404040;
	private static final int LABEL_COLOR = 0x555555;
	private static final int VALUE_COLOR = 0x303030;
	private static final int BG_COLOR = 0xE8F0E0E8;

	private final GOTEntityHorse mount;

	/** Область слота брони: закрашиваем с запасом и цветом фона контейнера, чтобы не было следа. */
	private static final int ARMOR_SLOT_X = 6;
	private static final int ARMOR_SLOT_Y = 34;
	private static final int ARMOR_SLOT_W = 22;
	private static final int ARMOR_SLOT_H = 22;
	private static final int ARMOR_SLOT_BG = 0xFFC6B896;

	public GOTGuiMountInventory(IInventory playerInv, IInventory horseInv, GOTEntityHorse horse) {
		super(playerInv, horseInv, horse);
		inventorySlots = new GOTContainerMountInventory(playerInv, horseInv, horse);
		this.mount = horse;
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
	}

	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		if (mount == null || mount.isDead) {
			return;
		}
		drawStatsPanel();
	}

	private void drawStatsPanel() {
		int x = PANEL_X;
		int y = PANEL_Y;
		GOTBridleMountStats stats = GOTBridleMountStats.getStats(mount.getClass());
		float currentHp = mount.getHealth();

		drawRect(x, y, x + PANEL_W, y + PANEL_H, BG_COLOR);
		drawRect(x, y, x + PANEL_W, y + 1, 0xFF8B7355);
		drawRect(x, y + PANEL_H - 1, x + PANEL_W, y + PANEL_H, 0xFF8B7355);
		drawRect(x, y, x + 1, y + PANEL_H, 0xFF8B7355);
		drawRect(x + PANEL_W - 1, y, x + PANEL_W, y + PANEL_H, 0xFF8B7355);

		String title = StatCollector.translateToLocal("got.gui.mountStats.title");
		fontRendererObj.drawString(title, x + (PANEL_W - fontRendererObj.getStringWidth(title)) / 2, y + 2, TITLE_COLOR);
		y += 9;

		drawStatRow(x, y, StatCollector.translateToLocal("got.gui.mountStats.health"), String.format("%.0f/%.0f", currentHp, stats.hp));
		y += ROW;
		drawStatRow(x, y, StatCollector.translateToLocal("got.gui.mountStats.speed"), String.format("%.2f", stats.speed));
		y += ROW;
		drawStatRow(x, y, StatCollector.translateToLocal("got.gui.mountStats.jump"), String.format("%.2f", stats.jump));
		y += ROW;
		drawStatRow(x, y, StatCollector.translateToLocal("got.gui.mountStats.attack"), String.format("%.0f", stats.attack));
	}

	/** Одна строка: подпись слева, значение справа (выравнивание по уровню одинаковое для всех). */
	private void drawStatRow(int x, int y, String label, String value) {
		fontRendererObj.drawString(label, x + PAD, y, LABEL_COLOR);
		int valueW = fontRendererObj.getStringWidth(value);
		fontRendererObj.drawString(value, x + PANEL_W - PAD - valueW, y, VALUE_COLOR);
	}
}
