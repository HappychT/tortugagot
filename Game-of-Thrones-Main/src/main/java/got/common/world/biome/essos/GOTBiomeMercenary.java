package got.common.world.biome.essos;

import java.util.ArrayList;

import got.client.sound.GOTBiomeMusic;
import got.client.sound.GOTBiomeMusic.MusicRegion;
import got.common.database.*;
import got.common.world.biome.GOTBiome;
import got.common.world.spawning.*;
import got.common.world.spawning.GOTBiomeSpawnList.SpawnListContainer;
import got.common.world.structure.other.GOTStructureStoneRuin;

public class GOTBiomeMercenary extends GOTBiomeEssos {
	public GOTBiomeMercenary(int i, boolean major) {
		super(i, major);
		ArrayList<SpawnListContainer> c0 = new ArrayList<>();
		c0.add(GOTBiomeSpawnList.entry(GOTSpawnList.GOLDEN_MILITARY, 10).setSpawnChance(GOTBiome.SPAWN));
		npcSpawnList.newFactionList(10).add(c0);
		decorator.addRandomStructure(new GOTStructureStoneRuin.RuinSandstone(1, 4), 400);
		decorator.addRandomStructure(new GOTStructureStoneRuin.RuinStone(1, 4), 400);
	}

	@Override
	public boolean disableNoise() {
		return false;
	}

	@Override
	public GOTAchievement getBiomeAchievement() {
		return GOTAchievement.enterMercenary;
	}

	@Override
	public MusicRegion getBiomeMusic() {
		return GOTBiomeMusic.ESSOS.getSubregion("mercenary");
	}
}