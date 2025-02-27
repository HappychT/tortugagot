package got.common.world.map;

import got.common.world.biome.GOTBiome;
import net.minecraft.util.StatCollector;

public enum GOTMapLabels {
	NARROW("narrow", 1050, 1600, 9.0f, 70, -6.0f, 2.0f), SUNSET("sunset", 100, 1500, 15.0f, -70, -6.0f, -1.0f);

	public int posX;
	public int posY;
	public float scale;
	public int angle;
	public float minZoom;
	public float maxZoom;
	public GOTBiome biome;
	public String labelName;

	GOTMapLabels(Object label, int x, int y, float f, int r, float z1, float z2) {
		posX = x;
		posY = y;
		scale = f;
		angle = r;
		minZoom = z1;
		maxZoom = z2;
		if (label instanceof GOTBiome) {
			biome = (GOTBiome) label;
		} else {
			labelName = (String) label;
		}
	}

	public String getDisplayName() {
		if (labelName != null) {
			return StatCollector.translateToLocal("got.map." + labelName);
		}
		return biome.getBiomeDisplayName();
	}

	public static GOTMapLabels[] allMapLabels() {
		return GOTMapLabels.values();
	}
}
