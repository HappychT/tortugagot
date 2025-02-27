package got.common.world.biome.essos;

import java.util.ArrayList;

import got.common.database.GOTSpawnList;
import got.common.world.biome.GOTBiome;
import got.common.world.spawning.GOTBiomeSpawnList;
import got.common.world.spawning.GOTBiomeSpawnList.SpawnListContainer;

public class GOTBiomeNewGhis extends GOTBiomeGhiscar {
	public GOTBiomeNewGhis(int i, boolean major) {
		super(i, major);
		npcSpawnList.clear();
	}
}