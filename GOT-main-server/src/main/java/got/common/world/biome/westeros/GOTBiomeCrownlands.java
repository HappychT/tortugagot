package got.common.world.biome.westeros;

import java.util.ArrayList;

import got.client.sound.GOTBiomeMusic;
import got.client.sound.GOTBiomeMusic.MusicRegion;
import got.common.database.*;
import got.common.world.biome.GOTBiome;
import got.common.world.map.GOTWaypoint.Region;
import got.common.world.spawning.*;
import got.common.world.spawning.GOTBiomeSpawnList.SpawnListContainer;
import got.common.world.structure.westeros.crownlands.*;

public class GOTBiomeCrownlands extends GOTBiomeWesteros {
	public GOTBiomeCrownlands(int i, boolean major) {
		super(i, major);
		decorator.addVillage(new GOTStructureCrownlandsCity(this, 1.0f));
		decorator.addRandomStructure(new GOTStructureCrownlandsWatchfort(false), 800);
		ArrayList<SpawnListContainer> c7 = new ArrayList<>();
		c7.add(GOTBiomeSpawnList.entry(GOTSpawnList.WALKERS_CONQUEST, 10).setSpawnChance(GOTBiome.CONQUEST_SPAWN));
		npcSpawnList.newFactionList(0).add(c7);
	}

	@Override
	public GOTAchievement getBiomeAchievement() {
		return GOTAchievement.enterCrownlands;
	}

	@Override
	public MusicRegion getBiomeMusic() {
		return GOTBiomeMusic.WESTEROS.getSubregion("crownlands");
	}

	@Override
	public Region getBiomeWaypoints() {
		return Region.CROWNLANDS;
	}
}