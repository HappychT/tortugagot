package got.client.gui;

import got.common.item.other.GOTBridleMountStats;
import got.common.util.GOTReflection;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.StatCollector;

public final class GOTGuiMountStats {
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
    private static final int BORDER_COLOR = 0xFF8B7355;

    private GOTGuiMountStats() {
    }

    public static void draw(StatDrawer drawer, FontRenderer fontRenderer, EntityLivingBase mount, int armor) {
        int x = PANEL_X;
        int y = PANEL_Y;
        GOTBridleMountStats stats = GOTBridleMountStats.getStats(mount.getClass());
        float currentHp = mount.getHealth();
        double maxHp = mount.getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
        double speed = mount.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
        IAttributeInstance jumpAttr = mount.getEntityAttribute(GOTReflection.getHorseJumpStrength());
        double jump = jumpAttr != null ? jumpAttr.getAttributeValue() : stats.jump;

        drawer.drawRect(x, y, x + PANEL_W, y + PANEL_H, BG_COLOR);
        drawer.drawRect(x, y, x + PANEL_W, y + 1, BORDER_COLOR);
        drawer.drawRect(x, y + PANEL_H - 1, x + PANEL_W, y + PANEL_H, BORDER_COLOR);
        drawer.drawRect(x, y, x + 1, y + PANEL_H, BORDER_COLOR);
        drawer.drawRect(x + PANEL_W - 1, y, x + PANEL_W, y + PANEL_H, BORDER_COLOR);

        String title = StatCollector.translateToLocal("got.gui.mountStats.title");
        fontRenderer.drawString(title, x + (PANEL_W - fontRenderer.getStringWidth(title)) / 2, y + 2, TITLE_COLOR);
        y += 9;

        drawStatRow(fontRenderer, x, y, StatCollector.translateToLocal("got.gui.mountStats.health"), String.format("%.0f/%.0f", currentHp, maxHp));
        y += ROW;
        drawStatRow(fontRenderer, x, y, StatCollector.translateToLocal("got.gui.mountStats.speed"), String.format("%.2f", speed));
        y += ROW;
        drawStatRow(fontRenderer, x, y, StatCollector.translateToLocal("got.gui.mountStats.jump"), String.format("%.2f", jump));
        y += ROW;
        drawStatRow(fontRenderer, x, y, StatCollector.translateToLocal("got.gui.mountStats.armor"), String.format("%d", armor));
    }

    private static void drawStatRow(FontRenderer fontRenderer, int x, int y, String label, String value) {
        fontRenderer.drawString(label, x + PAD, y, LABEL_COLOR);
        int valueW = fontRenderer.getStringWidth(value);
        fontRenderer.drawString(value, x + PANEL_W - PAD - valueW, y, VALUE_COLOR);
    }

    public interface StatDrawer {
        void drawRect(int left, int top, int right, int bottom, int color);
    }
}