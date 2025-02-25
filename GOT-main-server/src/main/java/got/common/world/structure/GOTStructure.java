package got.common.world.structure;

import got.common.faction.GOTFaction;
import got.common.util.DatabaseGenerator;
import got.common.world.biome.GOTBiome;
import got.common.world.structure.other.*;
import got.common.world.structure.other.GOTStructureRegistry.IVillageProperties;
import got.common.world.structure.westeros.arryn.*;
import got.common.world.structure.westeros.crownlands.*;
import got.common.world.structure.westeros.dorne.*;
import got.common.world.structure.westeros.dragonstone.*;
import got.common.world.structure.westeros.gift.*;
import got.common.world.structure.westeros.hillmen.*;
import got.common.world.structure.westeros.ironborn.*;
import got.common.world.structure.westeros.north.*;
import got.common.world.structure.westeros.north.hillmen.*;
import got.common.world.structure.westeros.reach.*;
import got.common.world.structure.westeros.riverlands.*;
import got.common.world.structure.westeros.stormlands.*;
import got.common.world.structure.westeros.westerlands.*;
import got.common.world.structure.westeros.wildling.*;
import got.common.world.structure.westeros.wildling.thenn.*;

public class GOTStructure {
	public static int id;

	public static void onInit() {
		GOTStructureRegistry.register(id++, GOTStructureBarrow.class, 9605778);
		GOTStructureRegistry.register(id++, GOTStructureRuinedHouse.class, 9605778);
		GOTStructureRegistry.register(id++, GOTStructureBurntHouse.class, 9605778);
		GOTStructureRegistry.register(id++, GOTStructureRottenHouse.class, 9605778);
		GOTStructureRegistry.register(id++, GOTStructureSmallStoneRuin.class, 9605778);
		GOTStructureRegistry.register(id++, GOTStructureStoneRuin.RuinStone.class, 9605778);
		GOTStructureRegistry.register(id++, GOTStructureStoneRuin.RuinYiTi.class, 9605778);
		GOTStructureRegistry.register(id++, GOTStructureStoneRuin.RuinAsshai.class, 9605778);
		GOTStructureRegistry.register(id++, GOTStructureStoneRuin.RuinSandstone.class, 9605778);
		GOTStructureRegistry.register(id++, GOTStructureStoneRuin.RuinSothoryos.class, 9605778);

		GOTStructureRegistry.register(id++, GOTStructureWildlingHouse.class, GOTFaction.WILDLING);
		GOTStructureRegistry.register(id++, GOTStructureWildlingChieftainHouse.class, GOTFaction.WILDLING);

		GOTStructureRegistry.register(id++, GOTStructureThennHouse.class, GOTFaction.WILDLING);
		GOTStructureRegistry.register(id++, GOTStructureThennMagnarHouse.class, GOTFaction.WILDLING);

		GOTStructureRegistry.register(id++, GOTStructureGiftHouse.class, GOTFaction.NIGHT_WATCH);
		GOTStructureRegistry.register(id++, GOTStructureGiftLodge.class, GOTFaction.NIGHT_WATCH);
		GOTStructureRegistry.register(id++, GOTStructureGiftStables.class, GOTFaction.NIGHT_WATCH);
		GOTStructureRegistry.register(id++, GOTStructureGiftSmithy.class, GOTFaction.NIGHT_WATCH);
		GOTStructureRegistry.register(id++, GOTStructureGiftVillageLight.class, GOTFaction.NIGHT_WATCH);

		GOTStructureRegistry.register(id++, GOTStructureNorthHillmanHouse.class, GOTFaction.NORTH);
		GOTStructureRegistry.register(id++, GOTStructureNorthHillmanChieftainHouse.class, GOTFaction.NORTH);

		GOTStructureRegistry.register(id++, GOTStructureNorthWatchfort.class, GOTFaction.NORTH);
		GOTStructureRegistry.register(id++, GOTStructureNorthSmithy.class, GOTFaction.NORTH);
		GOTStructureRegistry.register(id++, GOTStructureNorthTower.class, GOTFaction.NORTH);
		GOTStructureRegistry.register(id++, GOTStructureNorthHouse.class, GOTFaction.NORTH);
		GOTStructureRegistry.register(id++, GOTStructureNorthCottage.class, GOTFaction.NORTH);
		GOTStructureRegistry.register(id++, GOTStructureNorthStoneHouse.class, GOTFaction.NORTH);
		GOTStructureRegistry.register(id++, GOTStructureNorthWatchtower.class, GOTFaction.NORTH);
		GOTStructureRegistry.register(id++, GOTStructureNorthStables.class, GOTFaction.NORTH);
		GOTStructureRegistry.register(id++, GOTStructureNorthBarn.class, GOTFaction.NORTH);
		GOTStructureRegistry.register(id++, GOTStructureNorthFortress.class, GOTFaction.NORTH);
		GOTStructureRegistry.register(id++, GOTStructureNorthTavern.class, GOTFaction.NORTH);
		GOTStructureRegistry.register(id++, GOTStructureNorthBath.class, GOTFaction.NORTH);

		GOTStructureRegistry.register(id++, GOTStructureIronbornBarn.class, GOTFaction.IRONBORN);
		GOTStructureRegistry.register(id++, GOTStructureIronbornBath.class, GOTFaction.IRONBORN);
		GOTStructureRegistry.register(id++, GOTStructureIronbornCottage.class, GOTFaction.IRONBORN);
		GOTStructureRegistry.register(id++, GOTStructureIronbornFortress.class, GOTFaction.IRONBORN);
		GOTStructureRegistry.register(id++, GOTStructureIronbornHouse.class, GOTFaction.IRONBORN);
		GOTStructureRegistry.register(id++, GOTStructureIronbornSmithy.class, GOTFaction.IRONBORN);
		GOTStructureRegistry.register(id++, GOTStructureIronbornStables.class, GOTFaction.IRONBORN);
		GOTStructureRegistry.register(id++, GOTStructureIronbornStoneHouse.class, GOTFaction.IRONBORN);
		GOTStructureRegistry.register(id++, GOTStructureIronbornTavern.class, GOTFaction.IRONBORN);
		GOTStructureRegistry.register(id++, GOTStructureIronbornTower.class, GOTFaction.IRONBORN);
		GOTStructureRegistry.register(id++, GOTStructureIronbornWatchfort.class, GOTFaction.IRONBORN);
		GOTStructureRegistry.register(id++, GOTStructureIronbornWatchtower.class, GOTFaction.IRONBORN);

		GOTStructureRegistry.register(id++, GOTStructureWesterlandsWatchfort.class, GOTFaction.WESTERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureWesterlandsSmithy.class, GOTFaction.WESTERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureWesterlandsTower.class, GOTFaction.WESTERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureWesterlandsHouse.class, GOTFaction.WESTERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureWesterlandsCottage.class, GOTFaction.WESTERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureWesterlandsStoneHouse.class, GOTFaction.WESTERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureWesterlandsWatchtower.class, GOTFaction.WESTERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureWesterlandsStables.class, GOTFaction.WESTERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureWesterlandsBarn.class, GOTFaction.WESTERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureWesterlandsFortress.class, GOTFaction.WESTERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureWesterlandsTavern.class, GOTFaction.WESTERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureWesterlandsBath.class, GOTFaction.WESTERLANDS);

		GOTStructureRegistry.register(id++, GOTStructureRiverlandsWatchfort.class, GOTFaction.RIVERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureRiverlandsSmithy.class, GOTFaction.RIVERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureRiverlandsTower.class, GOTFaction.RIVERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureRiverlandsHouse.class, GOTFaction.RIVERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureRiverlandsCottage.class, GOTFaction.RIVERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureRiverlandsStoneHouse.class, GOTFaction.RIVERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureRiverlandsWatchtower.class, GOTFaction.RIVERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureRiverlandsStables.class, GOTFaction.RIVERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureRiverlandsBarn.class, GOTFaction.RIVERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureRiverlandsFortress.class, GOTFaction.RIVERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureRiverlandsTavern.class, GOTFaction.RIVERLANDS);
		GOTStructureRegistry.register(id++, GOTStructureRiverlandsBath.class, GOTFaction.RIVERLANDS);

		GOTStructureRegistry.register(id++, GOTStructureHillmanHouse.class, GOTFaction.HILL_TRIBES);
		GOTStructureRegistry.register(id++, GOTStructureHillmanTavern.class, GOTFaction.HILL_TRIBES);
		GOTStructureRegistry.register(id++, GOTStructureHillmanFort.class, GOTFaction.HILL_TRIBES);

		GOTStructureRegistry.register(id++, GOTStructureArrynWatchfort.class, GOTFaction.ARRYN);
		GOTStructureRegistry.register(id++, GOTStructureArrynSmithy.class, GOTFaction.ARRYN);
		GOTStructureRegistry.register(id++, GOTStructureArrynTower.class, GOTFaction.ARRYN);
		GOTStructureRegistry.register(id++, GOTStructureArrynHouse.class, GOTFaction.ARRYN);
		GOTStructureRegistry.register(id++, GOTStructureArrynCottage.class, GOTFaction.ARRYN);
		GOTStructureRegistry.register(id++, GOTStructureArrynStoneHouse.class, GOTFaction.ARRYN);
		GOTStructureRegistry.register(id++, GOTStructureArrynWatchtower.class, GOTFaction.ARRYN);
		GOTStructureRegistry.register(id++, GOTStructureArrynStables.class, GOTFaction.ARRYN);
		GOTStructureRegistry.register(id++, GOTStructureArrynBarn.class, GOTFaction.ARRYN);
		GOTStructureRegistry.register(id++, GOTStructureArrynFortress.class, GOTFaction.ARRYN);
		GOTStructureRegistry.register(id++, GOTStructureArrynTavern.class, GOTFaction.ARRYN);
		GOTStructureRegistry.register(id++, GOTStructureArrynBath.class, GOTFaction.ARRYN);

		GOTStructureRegistry.register(id++, GOTStructureDragonstoneWatchfort.class, GOTFaction.DRAGONSTONE);
		GOTStructureRegistry.register(id++, GOTStructureDragonstoneSmithy.class, GOTFaction.DRAGONSTONE);
		GOTStructureRegistry.register(id++, GOTStructureDragonstoneTower.class, GOTFaction.DRAGONSTONE);
		GOTStructureRegistry.register(id++, GOTStructureDragonstoneHouse.class, GOTFaction.DRAGONSTONE);
		GOTStructureRegistry.register(id++, GOTStructureDragonstoneCottage.class, GOTFaction.DRAGONSTONE);
		GOTStructureRegistry.register(id++, GOTStructureDragonstoneStoneHouse.class, GOTFaction.DRAGONSTONE);
		GOTStructureRegistry.register(id++, GOTStructureDragonstoneWatchtower.class, GOTFaction.DRAGONSTONE);
		GOTStructureRegistry.register(id++, GOTStructureDragonstoneStables.class, GOTFaction.DRAGONSTONE);
		GOTStructureRegistry.register(id++, GOTStructureDragonstoneBarn.class, GOTFaction.DRAGONSTONE);
		GOTStructureRegistry.register(id++, GOTStructureDragonstoneFortress.class, GOTFaction.DRAGONSTONE);
		GOTStructureRegistry.register(id++, GOTStructureDragonstoneTavern.class, GOTFaction.DRAGONSTONE);
		GOTStructureRegistry.register(id++, GOTStructureDragonstoneBath.class, GOTFaction.DRAGONSTONE);

		GOTStructureRegistry.register(id++, GOTStructureCrownlandsWatchfort.class, GOTFaction.CROWNLANDS);
		GOTStructureRegistry.register(id++, GOTStructureCrownlandsSmithy.class, GOTFaction.CROWNLANDS);
		GOTStructureRegistry.register(id++, GOTStructureCrownlandsTower.class, GOTFaction.CROWNLANDS);
		GOTStructureRegistry.register(id++, GOTStructureCrownlandsHouse.class, GOTFaction.CROWNLANDS);
		GOTStructureRegistry.register(id++, GOTStructureCrownlandsCottage.class, GOTFaction.CROWNLANDS);
		GOTStructureRegistry.register(id++, GOTStructureCrownlandsStoneHouse.class, GOTFaction.CROWNLANDS);
		GOTStructureRegistry.register(id++, GOTStructureCrownlandsWatchtower.class, GOTFaction.CROWNLANDS);
		GOTStructureRegistry.register(id++, GOTStructureCrownlandsStables.class, GOTFaction.CROWNLANDS);
		GOTStructureRegistry.register(id++, GOTStructureCrownlandsBarn.class, GOTFaction.CROWNLANDS);
		GOTStructureRegistry.register(id++, GOTStructureCrownlandsFortress.class, GOTFaction.CROWNLANDS);
		GOTStructureRegistry.register(id++, GOTStructureCrownlandsTavern.class, GOTFaction.CROWNLANDS);
		GOTStructureRegistry.register(id++, GOTStructureCrownlandsBath.class, GOTFaction.CROWNLANDS);

		GOTStructureRegistry.register(id++, GOTStructureStormlandsWatchfort.class, GOTFaction.STORMLANDS);
		GOTStructureRegistry.register(id++, GOTStructureStormlandsSmithy.class, GOTFaction.STORMLANDS);
		GOTStructureRegistry.register(id++, GOTStructureStormlandsTower.class, GOTFaction.STORMLANDS);
		GOTStructureRegistry.register(id++, GOTStructureStormlandsHouse.class, GOTFaction.STORMLANDS);
		GOTStructureRegistry.register(id++, GOTStructureStormlandsCottage.class, GOTFaction.STORMLANDS);
		GOTStructureRegistry.register(id++, GOTStructureStormlandsStoneHouse.class, GOTFaction.STORMLANDS);
		GOTStructureRegistry.register(id++, GOTStructureStormlandsWatchtower.class, GOTFaction.STORMLANDS);
		GOTStructureRegistry.register(id++, GOTStructureStormlandsStables.class, GOTFaction.STORMLANDS);
		GOTStructureRegistry.register(id++, GOTStructureStormlandsBarn.class, GOTFaction.STORMLANDS);
		GOTStructureRegistry.register(id++, GOTStructureStormlandsFortress.class, GOTFaction.STORMLANDS);
		GOTStructureRegistry.register(id++, GOTStructureStormlandsTavern.class, GOTFaction.STORMLANDS);
		GOTStructureRegistry.register(id++, GOTStructureStormlandsBath.class, GOTFaction.STORMLANDS);

		GOTStructureRegistry.register(id++, GOTStructureReachBrewery.class, GOTFaction.REACH);
		GOTStructureRegistry.register(id++, GOTStructureReachWatchfort.class, GOTFaction.REACH);
		GOTStructureRegistry.register(id++, GOTStructureReachSmithy.class, GOTFaction.REACH);
		GOTStructureRegistry.register(id++, GOTStructureReachTower.class, GOTFaction.REACH);
		GOTStructureRegistry.register(id++, GOTStructureReachHouse.class, GOTFaction.REACH);
		GOTStructureRegistry.register(id++, GOTStructureReachCottage.class, GOTFaction.REACH);
		GOTStructureRegistry.register(id++, GOTStructureReachStoneHouse.class, GOTFaction.REACH);
		GOTStructureRegistry.register(id++, GOTStructureReachWatchtower.class, GOTFaction.REACH);
		GOTStructureRegistry.register(id++, GOTStructureReachStables.class, GOTFaction.REACH);
		GOTStructureRegistry.register(id++, GOTStructureReachBarn.class, GOTFaction.REACH);
		GOTStructureRegistry.register(id++, GOTStructureReachFortress.class, GOTFaction.REACH);
		GOTStructureRegistry.register(id++, GOTStructureReachTavern.class, GOTFaction.REACH);
		GOTStructureRegistry.register(id++, GOTStructureReachBath.class, GOTFaction.REACH);

		GOTStructureRegistry.register(id++, GOTStructureDorneWatchfort.class, GOTFaction.DORNE);
		GOTStructureRegistry.register(id++, GOTStructureDorneSmithy.class, GOTFaction.DORNE);
		GOTStructureRegistry.register(id++, GOTStructureDorneTower.class, GOTFaction.DORNE);
		GOTStructureRegistry.register(id++, GOTStructureDorneHouse.class, GOTFaction.DORNE);
		GOTStructureRegistry.register(id++, GOTStructureDorneCottage.class, GOTFaction.DORNE);
		GOTStructureRegistry.register(id++, GOTStructureDorneStoneHouse.class, GOTFaction.DORNE);
		GOTStructureRegistry.register(id++, GOTStructureDorneWatchtower.class, GOTFaction.DORNE);
		GOTStructureRegistry.register(id++, GOTStructureDorneStables.class, GOTFaction.DORNE);
		GOTStructureRegistry.register(id++, GOTStructureDorneBarn.class, GOTFaction.DORNE);
		GOTStructureRegistry.register(id++, GOTStructureDorneFortress.class, GOTFaction.DORNE);
		GOTStructureRegistry.register(id++, GOTStructureDorneTavern.class, GOTFaction.DORNE);
		GOTStructureRegistry.register(id++, GOTStructureDorneBath.class, GOTFaction.DORNE);

		GOTStructureRegistry.register(id++, new GOTStructureWildlingVillage(GOTBiome.hauntedForest, 1.0f), "WildlingVillage", GOTFaction.WILDLING, (IVillageProperties<GOTStructureWildlingVillage.Instance>) instance -> instance.villageType = GOTStructureWildlingVillage.VillageType.VILLAGE);
		GOTStructureRegistry.register(id++, new GOTStructureThennVillage(GOTBiome.thenn, 1.0f), "ThennVillage", GOTFaction.WILDLING, (IVillageProperties<GOTStructureThennVillage.Instance>) instance -> {
		});

		GOTStructureRegistry.register(id++, new GOTStructureGiftVillage(GOTBiome.giftNew, 1.0f), "GiftVillage", GOTFaction.NIGHT_WATCH, (IVillageProperties<GOTStructureGiftVillage.Instance>) instance -> instance.villageType = GOTStructureGiftVillage.VillageType.VILLAGE);
		GOTStructureRegistry.register(id++, new GOTStructureNorthHillmanVillage(GOTBiome.skagos, 1.0f), "NorthHillmanVillage", GOTFaction.NORTH, (IVillageProperties<GOTStructureNorthHillmanVillage.Instance>) instance -> {
		});

		GOTStructureRegistry.register(id++, new GOTStructureNorthCity(GOTBiome.north, 1.0f), "NorthVillage", GOTFaction.NORTH, (IVillageProperties<GOTStructureNorthCity.Instance>) instance -> instance.villageType = GOTStructureNorthCity.VillageType.VILLAGE);
		GOTStructureRegistry.register(id++, new GOTStructureNorthCity(GOTBiome.north, 1.0f), "NorthTown", GOTFaction.NORTH, (IVillageProperties<GOTStructureNorthCity.Instance>) instance -> instance.villageType = GOTStructureNorthCity.VillageType.TOWN);
		GOTStructureRegistry.register(id++, new GOTStructureNorthCity(GOTBiome.north, 1.0f), "NorthFortVillage", GOTFaction.NORTH, (IVillageProperties<GOTStructureNorthCity.Instance>) instance -> instance.villageType = GOTStructureNorthCity.VillageType.FORT);

		GOTStructureRegistry.register(id++, new GOTStructureIronbornCity(GOTBiome.ironborn, 1.0f), "IronbornCityLittle", GOTFaction.IRONBORN, (IVillageProperties<GOTStructureIronbornCity.Instance>) instance -> instance.villageType = GOTStructureIronbornCity.VillageType.VILLAGE);
		GOTStructureRegistry.register(id++, new GOTStructureIronbornCity(GOTBiome.ironborn, 1.0f), "IronbornCityBig", GOTFaction.IRONBORN, (IVillageProperties<GOTStructureIronbornCity.Instance>) instance -> instance.villageType = GOTStructureIronbornCity.VillageType.TOWN);
		GOTStructureRegistry.register(id++, new GOTStructureIronbornCity(GOTBiome.ironborn, 1.0f), "IronbornCityMedium", GOTFaction.IRONBORN, (IVillageProperties<GOTStructureIronbornCity.Instance>) instance -> instance.villageType = GOTStructureIronbornCity.VillageType.FORT);

		GOTStructureRegistry.register(id++, new GOTStructureWesterlandsCity(GOTBiome.westerlands, 1.0f), "WesterlandsCityLittle", GOTFaction.WESTERLANDS, (IVillageProperties<GOTStructureWesterlandsCity.Instance>) instance -> instance.villageType = GOTStructureWesterlandsCity.VillageType.VILLAGE);
		GOTStructureRegistry.register(id++, new GOTStructureWesterlandsCity(GOTBiome.westerlands, 1.0f), "WesterlandsCityBig", GOTFaction.WESTERLANDS, (IVillageProperties<GOTStructureWesterlandsCity.Instance>) instance -> instance.villageType = GOTStructureWesterlandsCity.VillageType.TOWN);
		GOTStructureRegistry.register(id++, new GOTStructureWesterlandsCity(GOTBiome.westerlands, 1.0f), "WesterlandsCityMedium", GOTFaction.WESTERLANDS, (IVillageProperties<GOTStructureWesterlandsCity.Instance>) instance -> instance.villageType = GOTStructureWesterlandsCity.VillageType.FORT);

		GOTStructureRegistry.register(id++, new GOTStructureRiverlandsCity(GOTBiome.riverlands, 1.0f), "RiverlandsCityLittle", GOTFaction.RIVERLANDS, (IVillageProperties<GOTStructureRiverlandsCity.Instance>) instance -> instance.villageType = GOTStructureRiverlandsCity.VillageType.VILLAGE);
		GOTStructureRegistry.register(id++, new GOTStructureRiverlandsCity(GOTBiome.riverlands, 1.0f), "RiverlandsCityBig", GOTFaction.RIVERLANDS, (IVillageProperties<GOTStructureRiverlandsCity.Instance>) instance -> instance.villageType = GOTStructureRiverlandsCity.VillageType.TOWN);
		GOTStructureRegistry.register(id++, new GOTStructureRiverlandsCity(GOTBiome.riverlands, 1.0f), "RiverlandsCityMedium", GOTFaction.RIVERLANDS, (IVillageProperties<GOTStructureRiverlandsCity.Instance>) instance -> instance.villageType = GOTStructureRiverlandsCity.VillageType.FORT);

		GOTStructureRegistry.register(id++, new GOTStructureHillmanVillage(GOTBiome.sothoryosJungle, 1.0f), "HillmanVillage", GOTFaction.HILL_TRIBES, (IVillageProperties<GOTStructureHillmanVillage.Instance>) instance -> {
		});

		GOTStructureRegistry.register(id++, new GOTStructureArrynCity(GOTBiome.arryn, 1.0f), "ArrynCityLittle", GOTFaction.ARRYN, (IVillageProperties<GOTStructureArrynCity.Instance>) instance -> instance.villageType = GOTStructureArrynCity.VillageType.VILLAGE);
		GOTStructureRegistry.register(id++, new GOTStructureArrynCity(GOTBiome.arryn, 1.0f), "ArrynCityBig", GOTFaction.ARRYN, (IVillageProperties<GOTStructureArrynCity.Instance>) instance -> instance.villageType = GOTStructureArrynCity.VillageType.TOWN);
		GOTStructureRegistry.register(id++, new GOTStructureArrynCity(GOTBiome.arryn, 1.0f), "ArrynCityMedium", GOTFaction.ARRYN, (IVillageProperties<GOTStructureArrynCity.Instance>) instance -> instance.villageType = GOTStructureArrynCity.VillageType.FORT);

		GOTStructureRegistry.register(id++, new GOTStructureDragonstoneCity(GOTBiome.dragonstone, 1.0f), "DragonstoneCityLittle", GOTFaction.DRAGONSTONE, (IVillageProperties<GOTStructureDragonstoneCity.Instance>) instance -> instance.villageType = GOTStructureDragonstoneCity.VillageType.VILLAGE);
		GOTStructureRegistry.register(id++, new GOTStructureDragonstoneCity(GOTBiome.dragonstone, 1.0f), "DragonstoneCityBig", GOTFaction.DRAGONSTONE, (IVillageProperties<GOTStructureDragonstoneCity.Instance>) instance -> instance.villageType = GOTStructureDragonstoneCity.VillageType.TOWN);
		GOTStructureRegistry.register(id++, new GOTStructureDragonstoneCity(GOTBiome.dragonstone, 1.0f), "DragonstoneCityMedium", GOTFaction.DRAGONSTONE, (IVillageProperties<GOTStructureDragonstoneCity.Instance>) instance -> instance.villageType = GOTStructureDragonstoneCity.VillageType.FORT);

		GOTStructureRegistry.register(id++, new GOTStructureCrownlandsCity(GOTBiome.crownlands, 1.0f), "CrownlandsCityLittle", GOTFaction.CROWNLANDS, (IVillageProperties<GOTStructureCrownlandsCity.Instance>) instance -> instance.villageType = GOTStructureCrownlandsCity.VillageType.VILLAGE);
		GOTStructureRegistry.register(id++, new GOTStructureCrownlandsCity(GOTBiome.crownlands, 1.0f), "CrownlandsCityBig", GOTFaction.CROWNLANDS, (IVillageProperties<GOTStructureCrownlandsCity.Instance>) instance -> instance.villageType = GOTStructureCrownlandsCity.VillageType.TOWN);
		GOTStructureRegistry.register(id++, new GOTStructureCrownlandsCity(GOTBiome.crownlands, 1.0f), "CrownlandsCityMedium", GOTFaction.CROWNLANDS, (IVillageProperties<GOTStructureCrownlandsCity.Instance>) instance -> instance.villageType = GOTStructureCrownlandsCity.VillageType.FORT);

		GOTStructureRegistry.register(id++, new GOTStructureStormlandsCity(GOTBiome.stormlands, 1.0f), "StormlandsCityLittle", GOTFaction.STORMLANDS, (IVillageProperties<GOTStructureStormlandsCity.Instance>) instance -> instance.villageType = GOTStructureStormlandsCity.VillageType.VILLAGE);
		GOTStructureRegistry.register(id++, new GOTStructureStormlandsCity(GOTBiome.stormlands, 1.0f), "StormlandsCityBig", GOTFaction.STORMLANDS, (IVillageProperties<GOTStructureStormlandsCity.Instance>) instance -> instance.villageType = GOTStructureStormlandsCity.VillageType.TOWN);
		GOTStructureRegistry.register(id++, new GOTStructureStormlandsCity(GOTBiome.stormlands, 1.0f), "StormlandsCityMedium", GOTFaction.STORMLANDS, (IVillageProperties<GOTStructureStormlandsCity.Instance>) instance -> instance.villageType = GOTStructureStormlandsCity.VillageType.FORT);

		GOTStructureRegistry.register(id++, new GOTStructureReachCity(GOTBiome.reach, 1.0f), "ReachCityLittle", GOTFaction.REACH, (IVillageProperties<GOTStructureReachCity.Instance>) instance -> instance.villageType = GOTStructureReachCity.VillageType.VILLAGE);
		GOTStructureRegistry.register(id++, new GOTStructureReachCity(GOTBiome.reach, 1.0f), "ReachCityBig", GOTFaction.REACH, (IVillageProperties<GOTStructureReachCity.Instance>) instance -> instance.villageType = GOTStructureReachCity.VillageType.TOWN);
		GOTStructureRegistry.register(id++, new GOTStructureReachCity(GOTBiome.reach, 1.0f), "ReachCityMedium", GOTFaction.REACH, (IVillageProperties<GOTStructureReachCity.Instance>) instance -> instance.villageType = GOTStructureReachCity.VillageType.FORT);

		GOTStructureRegistry.register(id++, new GOTStructureDorneCity(GOTBiome.dorne, 1.0f), "DorneCityLittle", GOTFaction.DORNE, (IVillageProperties<GOTStructureDorneCity.Instance>) instance -> instance.villageType = GOTStructureDorneCity.VillageType.VILLAGE);
		GOTStructureRegistry.register(id++, new GOTStructureDorneCity(GOTBiome.dorne, 1.0f), "DorneCityBig", GOTFaction.DORNE, (IVillageProperties<GOTStructureDorneCity.Instance>) instance -> instance.villageType = GOTStructureDorneCity.VillageType.TOWN);
		GOTStructureRegistry.register(id++, new GOTStructureDorneCity(GOTBiome.dorne, 1.0f), "DorneCityMedium", GOTFaction.DORNE, (IVillageProperties<GOTStructureDorneCity.Instance>) instance -> instance.villageType = GOTStructureDorneCity.VillageType.FORT);

		GOTStructureRegistry.register(id++, DatabaseGenerator.class, 9605778);
	}
}