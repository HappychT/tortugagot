package got.common.world.map;

import java.util.*;

import got.common.entity.westeros.ironborn.GOTEntityIronbornSoldier;
import got.common.world.biome.GOTBiome;
import got.common.world.feature.*;
import got.common.world.structure.other.*;
import got.common.world.structure.westeros.arryn.GOTStructureArrynCity;
import got.common.world.structure.westeros.crownlands.GOTStructureCrownlandsCity;
import got.common.world.structure.westeros.dorne.GOTStructureDorneCity;
import got.common.world.structure.westeros.dragonstone.GOTStructureDragonstoneCity;
import got.common.world.structure.westeros.gift.GOTStructureGiftVillage;
import got.common.world.structure.westeros.ironborn.GOTStructureIronbornCity;
import got.common.world.structure.westeros.north.GOTStructureNorthCity;
import got.common.world.structure.westeros.north.hillmen.GOTStructureNorthHillmanVillage;
import got.common.world.structure.westeros.reach.GOTStructureReachCity;
import got.common.world.structure.westeros.riverlands.GOTStructureRiverlandsCity;
import got.common.world.structure.westeros.stormlands.GOTStructureStormlandsCity;
import got.common.world.structure.westeros.westerlands.GOTStructureWesterlandsCity;
import got.common.world.structure.westeros.wildling.GOTStructureWildlingVillage;
import net.minecraft.world.World;

public class GOTFixer {
	public static Map<GOTWaypoint, GOTStructureBase> structures = new EnumMap<>(GOTWaypoint.class);
	public static GOTVillageGen[] f = new GOTVillageGen[39];

	public static void addSpecialLocations(World world, Random random, int i, int k) {
		if (GOTFixedStructures.fixedAt(i, k, GOTWaypoint.WhiteWood) || GOTFixedStructures.fixedAt(i, k, GOTWaypoint.Winterfell)) {
			((GOTWorldGenPartyTrees) GOTTreeType.WEIRWOOD.create(false, random)).disableRestrictions().generate(world, random, i + 50, world.getTopSolidOrLiquidBlock(i + 50, k), k);
		}
	}

	public static void affixWaypointLocations(GOTBiome biome) {
		int slot = 0;
		f[slot] = new GOTStructureArrynCity(biome, 0.0f).setIsCastle();
		f[slot].affix(GOTWaypoint.BloodyGate.info(0, -1, 0), GOTWaypoint.TheEyrie, GOTWaypoint.ColdwaterBurn, GOTWaypoint.GreyGlen, GOTWaypoint.HeartsHome, GOTWaypoint.IronOak, GOTWaypoint.LongbowHall, GOTWaypoint.Ninestars, GOTWaypoint.OldAnchor, GOTWaypoint.Pebble, GOTWaypoint.Redfort, GOTWaypoint.Runestone, GOTWaypoint.Snakewood, GOTWaypoint.Strongsong, GOTWaypoint.ThePaps, GOTWaypoint.Wickenden, GOTWaypoint.WitchIsle, GOTWaypoint.GateOfTheMoon.info(0, 1, 0));
		slot++;
		f[slot] = new GOTStructureArrynCity(biome, 0.0f).setIsTown();
		f[slot].affix(GOTWaypoint.Gulltown, GOTWaypoint.Sisterton);
		slot++;
		f[slot] = new GOTStructureGiftVillage(biome, 0.0f).setIsCastleBlack();
		f[slot].affix(GOTWaypoint.CastleBlack.info(0, 0, 0));
		slot++;
		f[slot] = new GOTStructureWildlingVillage(biome, 0.0f).setIsCraster();
		f[slot].affix(GOTWaypoint.CrastersKeep);
		slot++;
		f[slot] = new GOTStructureRiverlandsCity(biome, 0.0f).setIsCrossroads();
		f[slot].affix(GOTWaypoint.CrossroadsInn);
		slot++;
		f[slot] = new GOTStructureCrownlandsCity(biome, 0.0f).setIsKingsLanding();
		f[slot].affix(GOTWaypoint.KingsLanding.info(1, 0, 1));
		slot++;
		f[slot] = new GOTStructureCrownlandsCity(biome, 0.0f).setIsCastle();
		f[slot].affix(GOTWaypoint.Antlers, GOTWaypoint.Brownhollow, GOTWaypoint.DyreDen, GOTWaypoint.Stokeworth, GOTWaypoint.Hayford.info(-1, 0, 0), GOTWaypoint.RooksRest.info(0, -1, 0), GOTWaypoint.Rosby.info(0, -1, 0));
		slot++;
		f[slot] = new GOTStructureCrownlandsCity(biome, 0.0f).setIsTown();
		f[slot].affix(GOTWaypoint.Duskendale.info(-2, 0, 3));
		slot++;
		f[slot] = new GOTStructureCrownlandsCity(biome, 0.0f);
		f[slot].affix(GOTWaypoint.Briarwhite);
		slot++;
		f[slot] = new GOTStructureDorneCity(biome, 0.0f).setIsCastle();
		f[slot].affix(GOTWaypoint.Blackmont, GOTWaypoint.GhostHill, GOTWaypoint.Godsgrace, GOTWaypoint.Hellholt, GOTWaypoint.HighHermitage, GOTWaypoint.Tor, GOTWaypoint.Vaith, GOTWaypoint.WaterGardens, GOTWaypoint.Spottswood, GOTWaypoint.Lemonwood, GOTWaypoint.Saltshore, GOTWaypoint.Sandstone, GOTWaypoint.Kingsgrave.info(-1, 0, 0), GOTWaypoint.SkyReach.info(0, 1, 0), GOTWaypoint.Starfall.info(0, -1, 0), GOTWaypoint.Wyl.info(0, -1, 0), GOTWaypoint.Yronwood.info(1, 0, 0));
		slot++;
		f[slot] = new GOTStructureDorneCity(biome, 0.0f).setIsTown();
		f[slot].affix(GOTWaypoint.GhastonGrey, GOTWaypoint.PlankyTown, GOTWaypoint.Sunspear);
		slot++;
		f[slot] = new GOTStructureDragonstoneCity(biome, 0.0f).setIsCastle();
		f[slot].affix(GOTWaypoint.ClawIsle, GOTWaypoint.Dragonstone, GOTWaypoint.Driftmark, GOTWaypoint.HighTide, GOTWaypoint.SharpPoint, GOTWaypoint.Stonedance, GOTWaypoint.SweetportSound);
		slot++;
		f[slot] = new GOTStructureDragonstoneCity(biome, 0.0f).setIsTown();
		f[slot].affix(GOTWaypoint.Hull);
		slot++;
		f[slot] = new GOTStructureGiftVillage(biome, 0.0f).setIsEastWatch();
		f[slot].affix(GOTWaypoint.EastWatch);
		slot++;
		f[slot] = new GOTStructureGiftVillage(biome, 0.0f);
		f[slot].affix(GOTWaypoint.Moletown, GOTWaypoint.Queenscrown);
		slot++;
		f[slot] = new GOTStructureWildlingVillage(biome, 0.0f).setIsHardhome();
		f[slot].affix(GOTWaypoint.Hardhome);
		slot++;
		f[slot] = new GOTStructureIronbornCity(biome, 0.0f).setIsCastle();
		f[slot].affix(GOTWaypoint.CatfishRock.info(0, 1, 0), GOTWaypoint.Blackhaven.info(0, 0, 0), GOTWaypoint.Blacktyde, GOTWaypoint.CorpseLake, GOTWaypoint.CrowSpikeKeep, GOTWaypoint.Downdelving, GOTWaypoint.DrummCastle, GOTWaypoint.GreyGarden, GOTWaypoint.Hammerhorn, GOTWaypoint.HarlawHall, GOTWaypoint.HarridanHill, GOTWaypoint.IronHolt, GOTWaypoint.LonelyLight, GOTWaypoint.MyreCastle, GOTWaypoint.Orkwood, GOTWaypoint.Pyke, GOTWaypoint.Saltcliffe, GOTWaypoint.SealskinPoint, GOTWaypoint.Shatterstone, GOTWaypoint.SparrCastle, GOTWaypoint.Stonehouse, GOTWaypoint.StonetreeCastle, GOTWaypoint.Sunderly, GOTWaypoint.TawneyCastle, GOTWaypoint.TenTowers, GOTWaypoint.Volmark);
		slot++;
		f[slot] = new GOTStructureIronbornCity(biome, 0.0f).setIsTown();
		f[slot].affix(GOTWaypoint.Lordsport, GOTWaypoint.RedHaven);
		slot++;
		f[slot] = new GOTStructureIronbornCity(biome, 0.0f);
		f[slot].affix(GOTWaypoint.Pebbleton);
		slot++;
		f[slot] = new GOTStructureNorthCity(biome, 0.0f).setIsCastle();
		f[slot].affix(GOTWaypoint.CatfishRock.info(0, 0, 0), GOTWaypoint.BreakstoneHill, GOTWaypoint.Goldgrass.info(0, 1, 0), GOTWaypoint.RisvellsCastle.info(0, 1, 0), GOTWaypoint.ServinsCastle.info(-1, 0, 0), GOTWaypoint.Winterfell.info(0, 0, 1), GOTWaypoint.BlackPool, GOTWaypoint.DeepwoodMotte, GOTWaypoint.Dreadfort, GOTWaypoint.FlintsFinger, GOTWaypoint.Highpoint, GOTWaypoint.TorhensSquare, GOTWaypoint.WidowsWatch, GOTWaypoint.Hornwood, GOTWaypoint.Ironrath, GOTWaypoint.Karhold, GOTWaypoint.LastHearth, GOTWaypoint.MoatKailin, GOTWaypoint.MormontsKeep, GOTWaypoint.OldCastle, GOTWaypoint.RamsGate, GOTWaypoint.RillwaterCrossing);
		slot++;
		f[slot] = new GOTStructureNorthCity(biome, 0.0f).setIsSmallTown();
		f[slot].affix(GOTWaypoint.Barrowtown.info(0, 1, 2));
		slot++;
		f[slot] = new GOTStructureNorthCity(biome, 0.0f).setIsTown();
		f[slot].affix(GOTWaypoint.WhiteHarbour.info(0, 0, 1));
		slot++;
		f[slot] = new GOTStructureNorthHillmanVillage(biome, 0.0f);
		f[slot].affix(GOTWaypoint.Skane, GOTWaypoint.Deepdown, GOTWaypoint.DriftwoodHall, GOTWaypoint.Kingshouse);
		slot++;
		f[slot] = new GOTStructureNorthCity(biome, 0.0f);
		f[slot].affix(GOTWaypoint.GreywaterWatch);
		slot++;
		f[slot] = new GOTStructureReachCity(biome, 0.0f).setIsCastle();
		f[slot].affix(GOTWaypoint.Highgarden.info(0, -1, 0), GOTWaypoint.Appleton.info(0, -1, 0), GOTWaypoint.Ashford.info(0, 1, 0), GOTWaypoint.Bandallon, GOTWaypoint.Goldengrove, GOTWaypoint.GrassyVale, GOTWaypoint.Greenshield, GOTWaypoint.Grimston, GOTWaypoint.Hammerhal, GOTWaypoint.RedLake, GOTWaypoint.Ring, GOTWaypoint.Southshield, GOTWaypoint.Uplands, GOTWaypoint.Holyhall, GOTWaypoint.Honeyholt, GOTWaypoint.HornHill, GOTWaypoint.IvyHall, GOTWaypoint.Longtable, GOTWaypoint.NewBarrel, GOTWaypoint.Blackcrown, GOTWaypoint.BrightwaterKeep, GOTWaypoint.CiderHall, GOTWaypoint.Coldmoat, GOTWaypoint.DarkDell, GOTWaypoint.Dunstonbury, GOTWaypoint.Bitterbridge.info(0, 1, 0), GOTWaypoint.GarnetGrove.info(-1, 0, 0), GOTWaypoint.HewettTown.info(0, -1, 0), GOTWaypoint.OldOak.info(-1, 0, 0), GOTWaypoint.SunHouse.info(0, -1, 0), GOTWaypoint.Whitegrove.info(-1, 0, 0));
		slot++;
		f[slot] = new GOTStructureReachCity(biome, 0.0f).setIsTown();
		f[slot].affix(GOTWaypoint.Oldtown.info(-1, 0, 3), GOTWaypoint.Appleton.info(0, 1, 2), GOTWaypoint.Ashford.info(0, -1, 0), GOTWaypoint.HewettTown.info(0, 1, 0), GOTWaypoint.Smithyton.info(0, 1, 2), GOTWaypoint.StarfishHarbor, GOTWaypoint.Vinetown, GOTWaypoint.Ryamsport, GOTWaypoint.Tumbleton.info(0, -1, 0));
		slot++;
		f[slot] = new GOTStructureCrownlandsCity(biome, 0.0f).setIsRedKeep();
		f[slot].affix(GOTWaypoint.KingsLanding.info(2, 0, 1));
		slot++;
		f[slot] = new GOTStructureRiverlandsCity(biome, 0.0f).setIsCastle();
		f[slot].affix(GOTWaypoint.Darry.info(1, 0, 0), GOTWaypoint.Maidenpool.info(1, 0, 0), GOTWaypoint.PinkmaidenCastle, GOTWaypoint.RaventreeHall, GOTWaypoint.WayfarerRest, GOTWaypoint.AcornHall, GOTWaypoint.Atranta, GOTWaypoint.Riverrun.info(1, 0, 0), GOTWaypoint.Seagard.info(0, -1, 0), GOTWaypoint.StoneHedge.info(0, 1, 0));
		slot++;
		f[slot] = new GOTStructureRiverlandsCity(biome, 0.0f).setIsTown();
		f[slot].affix(GOTWaypoint.Maidenpool.info(-1, 0, 3), GOTWaypoint.Saltpans, GOTWaypoint.StoneySept, GOTWaypoint.Seagard.info(0, 1, 2));
		slot++;
		f[slot] = new GOTStructureRiverlandsCity(biome, 0.0f);
		f[slot].affix(GOTWaypoint.FairMarket, GOTWaypoint.Harroway, GOTWaypoint.Pennytree, GOTWaypoint.Sevenstreams);
		slot++;
		f[slot] = new GOTStructureGiftVillage(biome, 0.0f).setIsShadowTower();
		f[slot].affix(GOTWaypoint.ShadowTower);
		slot++;
		f[slot] = new GOTStructureStormlandsCity(biome, 0.0f).setIsCastle();
		f[slot].affix(GOTWaypoint.CrowsNest, GOTWaypoint.GriffinsRoost, GOTWaypoint.Blackhaven.info(-1, 0, 0), GOTWaypoint.Bronzegate.info(1, 0, 0), GOTWaypoint.DeatsHear, GOTWaypoint.EvenfallHall, GOTWaypoint.Fawntown, GOTWaypoint.Amberly, GOTWaypoint.BlackHeart, GOTWaypoint.BroadArch, GOTWaypoint.Parchments, GOTWaypoint.Poddingfield, GOTWaypoint.RainHouse, GOTWaypoint.SeaworthCastle, GOTWaypoint.Stonehelm, GOTWaypoint.StormsEnd, GOTWaypoint.TudburyHoll, GOTWaypoint.Gallowsgrey, GOTWaypoint.Grandview, GOTWaypoint.Greenstone, GOTWaypoint.HarvestHall, GOTWaypoint.HaystackHall, GOTWaypoint.Mistwood, GOTWaypoint.Felwood.info(0, 1, 0), GOTWaypoint.Nightsong.info(0, 1, 0));
		slot++;
		f[slot] = new GOTStructureStormlandsCity(biome, 0.0f).setIsTown();
		f[slot].affix(GOTWaypoint.WeepingTown);
		slot++;
		f[slot] = new GOTStructureTower(biome, 0.0f);
		f[slot].affix(GOTWaypoint.ThreeTowers1.info(0, 0, 1), GOTWaypoint.ThreeTowers2.info(0, 0, 1), GOTWaypoint.ThreeTowers3.info(0, 0, 1), GOTWaypoint.TowerOfGlimmering, GOTWaypoint.BaelishKeep, GOTWaypoint.HightowerLitehouse, GOTWaypoint.RamseyTower, GOTWaypoint.Standfast, GOTWaypoint.TwinsLeft.info(0, 0, 2), GOTWaypoint.TwinsRight);
		slot++;
		f[slot] = new GOTStructureIronbornCity(biome, 0.0f).setIsCamp();
		f[slot].affix(GOTWaypoint.VictarionLanding);
		slot++;
		f[slot] = new GOTStructureWesterlandsCity(biome, 0.0f).setIsCastle();
		f[slot].affix(GOTWaypoint.CasterlyRock.info(-1, 0, 0), GOTWaypoint.Crakehall.info(-1, 0, 0), GOTWaypoint.GoldenTooth.info(0, 1, 0), GOTWaypoint.Kayce.info(1, 0, 0), GOTWaypoint.Sarsfield.info(0, -1, 0), GOTWaypoint.Silverhill, GOTWaypoint.Wyndhall, GOTWaypoint.Plumwood, GOTWaypoint.Riverspring, GOTWaypoint.Greenfield, GOTWaypoint.Hornvale, GOTWaypoint.DeepDen, GOTWaypoint.Faircastle, GOTWaypoint.Feastfires, GOTWaypoint.CleganesKeep, GOTWaypoint.Cornfield, GOTWaypoint.Crag, GOTWaypoint.Ashemark, GOTWaypoint.Banefort);
		slot++;
		f[slot] = new GOTStructureWesterlandsCity(biome, 0.0f).setIsTown();
		f[slot].affix(GOTWaypoint.Kayce.info(0, 0, 3), GOTWaypoint.Lannisport.info(-1, 0, 3));
		slot++;
		f[slot] = new GOTStructureWesterlandsCity(biome, 0.0f);
		f[slot].affix(GOTWaypoint.Oxcross);
		slot++;
		f[slot] = new GOTStructureGiftVillage(biome, 0.0f).setIsAbandoned();
		f[slot].affix(GOTWaypoint.Greenguard, GOTWaypoint.Torches, GOTWaypoint.LongBarrow, GOTWaypoint.Rimegate, GOTWaypoint.SableHall, GOTWaypoint.Woodswatch, GOTWaypoint.Nightfort, GOTWaypoint.DeepLake, GOTWaypoint.Oakenshield, GOTWaypoint.Icemark, GOTWaypoint.HoarfrostHill, GOTWaypoint.Stonedoor, GOTWaypoint.Greyguard, GOTWaypoint.Queensgate, GOTWaypoint.SentinelStand);
		slot++;

		for (GOTVillageGen element : f) {
			biome.decorator.addFixedVillage(element);
		}
	}

	public static void onInit() {

		structures.put(GOTWaypoint.Euron, new Spawner() {
			@Override
			public boolean generate(World world, Random random, int i, int j, int k, int rotation) {
				this.setOriginAndRotation(world, i, j, k, rotation, 0);
				loadStrScan("euron_ship");
				if (!disable) {
					generateStrScan(world, random, 16, 27, -58);
				}
				for (int l = 0; l < 10; ++l) {
					spawnLegendaryNPC(new GOTEntityIronbornSoldier(world), world, 0, 1, 0);
				}
				return true;
			}
		});

		structures.put(GOTWaypoint.NightKing, new Spawner() {
			@Override
			public boolean generate(World world, Random random, int i, int j, int k, int rotation) {
				this.setOriginAndRotation(world, i, j, k, rotation, 0);
				loadStrScan("night_king");
				if (!disable) {
					generateStrScan(world, random, 0, 0, 0);
				}
				return true;
			}
		});

	}

	public static class Spawner extends GOTStructureBase {

		@Override
		public boolean generate(World world, Random random, int i, int j, int k, int rotation) {
			this.setOriginAndRotation(world, i, j, k, rotation, 0);
			spawnLegendaryNPC(world);
			return true;
		}

		public void spawnLegendaryNPC(World world) {
		}
	}
}