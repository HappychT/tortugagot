package got.common.world.biome.westeros;

import java.util.ArrayList;

import got.client.sound.GOTBiomeMusic;
import got.client.sound.GOTBiomeMusic.MusicRegion;
import got.common.database.*;
import got.common.world.biome.GOTBiome;
import got.common.world.map.GOTWaypoint.Region;
import got.common.world.spawning.GOTBiomeSpawnList;
import got.common.world.spawning.GOTBiomeSpawnList.SpawnListContainer;
import got.common.world.structure.westeros.ironborn.*;

public class GOTBiomeIronborn extends GOTBiomeWesteros {
	public GOTBiomeIronborn(int i, boolean major) {
		super(i, major);
		decorator.addVillage(new GOTStructureIronbornCity(this, 1.0f));
		decorator.addRandomStructure(new GOTStructureIronbornWatchfort(false), 800);
	}

	@Override
	public GOTAchievement getBiomeAchievement() {
		return GOTAchievement.enterIronborn;
	}

	@Override
	public MusicRegion getBiomeMusic() {
		return GOTBiomeMusic.WESTEROS.getSubregion("ironborn");
	}

	@Override
	public Region getBiomeWaypoints() {
		return Region.IRONBORN;
	}
}