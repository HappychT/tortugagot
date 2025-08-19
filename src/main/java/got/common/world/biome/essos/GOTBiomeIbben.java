package got.common.world.biome.essos;

import java.util.ArrayList;

import got.client.sound.GOTBiomeMusic;
import got.client.sound.GOTBiomeMusic.MusicRegion;
import got.common.database.*;
import got.common.world.biome.GOTBiome;
import got.common.world.feature.GOTTreeType;
import got.common.world.map.GOTWaypoint.Region;
import got.common.world.spawning.*;
import got.common.world.spawning.GOTBiomeSpawnList.SpawnListContainer;

public class GOTBiomeIbben extends GOTBiomeEssosCold {
	public GOTBiomeIbben(int i, boolean major) {
		super(i, major);
		decorator.addTree(GOTTreeType.IBBEN_PINE, 500);
	}

	@Override
	public GOTAchievement getBiomeAchievement() {
		return GOTAchievement.enterIbben;
	}

	@Override
	public MusicRegion getBiomeMusic() {
		return GOTBiomeMusic.ESSOS.getSubregion("ibben");
	}

	@Override
	public Region getBiomeWaypoints() {
		return Region.IBBEN;
	}
}