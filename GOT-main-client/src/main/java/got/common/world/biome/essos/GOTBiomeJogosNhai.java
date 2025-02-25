package got.common.world.biome.essos;

import got.client.sound.GOTBiomeMusic;
import got.client.sound.GOTBiomeMusic.MusicRegion;
import got.common.database.GOTAchievement;
import got.common.world.map.GOTWaypoint.Region;

public class GOTBiomeJogosNhai extends GOTBiomeEssosPlains {
	public GOTBiomeJogosNhai(int i, boolean major) {
		super(i, major);
	}

	@Override
	public GOTAchievement getBiomeAchievement() {
		return GOTAchievement.enterJogos;
	}

	@Override
	public MusicRegion getBiomeMusic() {
		return GOTBiomeMusic.ESSOS.getSubregion("jogosNhai");
	}

	@Override
	public Region getBiomeWaypoints() {
		return Region.JOGOS;
	}
}