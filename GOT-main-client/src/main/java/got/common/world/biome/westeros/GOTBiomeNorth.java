package got.common.world.biome.westeros;

import java.util.ArrayList;

import got.client.sound.GOTBiomeMusic;
import got.client.sound.GOTBiomeMusic.MusicRegion;
import got.common.database.*;
import got.common.world.biome.GOTBiome;
import got.common.world.biome.variant.GOTBiomeVariant;
import got.common.world.feature.GOTTreeType;
import got.common.world.map.GOTWaypoint.Region;
import got.common.world.spawning.*;
import got.common.world.spawning.GOTBiomeSpawnList.SpawnListContainer;
import got.common.world.structure.westeros.north.*;

public class GOTBiomeNorth extends GOTBiomeWesteros {
	public GOTBiomeNorth(int i, boolean major) {
		super(i, major);
		clearBiomeVariants();
		addBiomeVariant(GOTBiomeVariant.FOREST);
		addBiomeVariant(GOTBiomeVariant.FOREST_LIGHT);
		addBiomeVariant(GOTBiomeVariant.HILLS);
		addBiomeVariant(GOTBiomeVariant.HILLS_FOREST);
		addBiomeVariant(GOTBiomeVariant.FOREST_ASPEN, 0.2f);
		addBiomeVariant(GOTBiomeVariant.FOREST_BEECH, 0.2f);
		addBiomeVariant(GOTBiomeVariant.FOREST_BIRCH, 0.2f);
		addBiomeVariant(GOTBiomeVariant.FOREST_LARCH, 0.2f);
		addBiomeVariant(GOTBiomeVariant.FOREST_MAPLE, 0.2f);
		addBiomeVariant(GOTBiomeVariant.FOREST_PINE, 0.2f);
		decorator.clearTrees();
		decorator.addTree(GOTTreeType.SPRUCE, 400);
		decorator.addTree(GOTTreeType.SPRUCE_THIN, 400);
		decorator.addTree(GOTTreeType.LARCH, 300);
		decorator.addTree(GOTTreeType.SPRUCE_MEGA, 100);
		decorator.addTree(GOTTreeType.SPRUCE_MEGA_THIN, 20);
		decorator.addTree(GOTTreeType.FIR, 500);
		decorator.addTree(GOTTreeType.PINE, 500);
		decorator.addRandomStructure(new GOTStructureNorthWatchfort(false), 800);
		decorator.addVillage(new GOTStructureNorthCity(this, 1.0f));
		ArrayList<SpawnListContainer> c3 = new ArrayList<>();
		c3.add(GOTBiomeSpawnList.entry(GOTSpawnList.WALKERS_CONQUEST, 10).setSpawnChance(GOTBiome.CONQUEST_SPAWN));
		npcSpawnList.newFactionList(0).add(c3);

	}

	@Override
	public GOTAchievement getBiomeAchievement() {
		return GOTAchievement.enterNorth;
	}

	@Override
	public MusicRegion getBiomeMusic() {
		return GOTBiomeMusic.WESTEROS.getSubregion("north");
	}

	@Override
	public Region getBiomeWaypoints() {
		return Region.NORTH;
	}
}