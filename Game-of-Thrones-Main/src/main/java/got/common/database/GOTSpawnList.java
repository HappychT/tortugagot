package got.common.database;

import java.util.*;

import got.common.entity.animal.*;
import got.common.entity.essos.*;
import got.common.entity.essos.gold.*;
import got.common.entity.other.*;
import got.common.entity.westeros.arryn.*;
import got.common.entity.westeros.crownlands.*;
import got.common.entity.westeros.dorne.*;
import got.common.entity.westeros.dragonstone.*;
import got.common.entity.westeros.gift.*;
import got.common.entity.westeros.hillmen.*;
import got.common.entity.westeros.ice.*;
import got.common.entity.westeros.ironborn.*;
import got.common.entity.westeros.north.*;
import got.common.entity.westeros.north.hillmen.*;
import got.common.entity.westeros.reach.*;
import got.common.entity.westeros.riverlands.*;
import got.common.entity.westeros.stormlands.*;
import got.common.entity.westeros.westerlands.*;
import got.common.entity.westeros.wildling.*;
import got.common.entity.westeros.wildling.thenn.*;
import got.common.faction.GOTFaction;
import got.common.world.spawning.GOTSpawnEntry;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

public class GOTSpawnList {
	public static List<GOTSpawnEntry> ARRYN_CIVILIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> ARRYN_CONQUEST_L = new ArrayList<>();
	public static List<GOTSpawnEntry> ARRYN_CONQUEST_l = new ArrayList<>();
	public static List<GOTSpawnEntry> ARRYN_GUARDIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> ARRYN_MILITARY_L = new ArrayList<>();
	public static List<GOTSpawnEntry> ASSHAI_CIVILIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> ASSHAI_MILITARY_L = new ArrayList<>();
	public static List<GOTSpawnEntry> CROCODILE_L = new ArrayList<>();
	public static List<GOTSpawnEntry> CROWNLANDS_CIVILIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> CROWNLANDS_GUARDIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> CROWNLANDS_MILITARY_L = new ArrayList<>();
	public static List<GOTSpawnEntry> DESERT_SCORPION_L = new ArrayList<>();
	public static List<GOTSpawnEntry> DORNE_CIVILIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> DORNE_CONQUEST_L = new ArrayList<>();
	public static List<GOTSpawnEntry> DORNE_MILITARY_L = new ArrayList<>();
	public static List<GOTSpawnEntry> DRAGONSTONE_CIVILIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> DRAGONSTONE_CONQUEST_L = new ArrayList<>();
	public static List<GOTSpawnEntry> DRAGONSTONE_MILITARY_L = new ArrayList<>();
	public static List<GOTSpawnEntry> GIFT_CIVILIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> GIFT_GUARDIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> GOLDEN_MILITARY_L = new ArrayList<>();
	public static List<GOTSpawnEntry> HILL_TRIBES_CIVILIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> HILL_TRIBES_MILITARY_L = new ArrayList<>();
	public static List<GOTSpawnEntry> IBBEN_CIVILIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> IBBEN_MILITARY_L = new ArrayList<>();
	public static List<GOTSpawnEntry> IFEKEVRON_L = new ArrayList<>();
	public static List<GOTSpawnEntry> IRONBORN_CIVILIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> IRONBORN_CONQUEST_L = new ArrayList<>();
	public static List<GOTSpawnEntry> IRONBORN_MILITARY_L = new ArrayList<>();
	public static List<GOTSpawnEntry> JOGOS_MILITARY_L = new ArrayList<>();
	public static List<GOTSpawnEntry> JUNGLE_SCORPION_L = new ArrayList<>();
	public static List<GOTSpawnEntry> MANTICORE_L = new ArrayList<>();
	public static List<GOTSpawnEntry> MOSSOVY_CIVILIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> MOSSOVY_MILITARY_L = new ArrayList<>();
	public static List<GOTSpawnEntry> MOSSOVY_WEREWOLF_L = new ArrayList<>();
	public static List<GOTSpawnEntry> NORTH_CIVILIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> NORTH_CONQUEST_L = new ArrayList<>();
	public static List<GOTSpawnEntry> NORTH_GUARDIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> NORTH_HILLMEN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> NORTH_MILITARY_L = new ArrayList<>();
	public static List<GOTSpawnEntry> REACH_CIVILIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> REACH_CONQUEST_L = new ArrayList<>();
	public static List<GOTSpawnEntry> REACH_GUARDIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> REACH_MILITARY_L = new ArrayList<>();
	public static List<GOTSpawnEntry> RED_SCORPION_L = new ArrayList<>();
	public static List<GOTSpawnEntry> RIVERLANDS_CIVILIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> RIVERLANDS_CONQUEST_L = new ArrayList<>();
	public static List<GOTSpawnEntry> RIVERLANDS_MILITARY_L = new ArrayList<>();
	public static List<GOTSpawnEntry> SHRYKE_L = new ArrayList<>();
	public static List<GOTSpawnEntry> SOTHORYOS_CIVILIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> SOTHORYOS_MILITARY_L = new ArrayList<>();
	public static List<GOTSpawnEntry> STORMLANDS_CIVILIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> STORMLANDS_CONQUEST_L = new ArrayList<>();
	public static List<GOTSpawnEntry> STORMLANDS_MILITARY_L = new ArrayList<>();
	public static List<GOTSpawnEntry> SUMMER_CIVILIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> SUMMER_MILITARY_L = new ArrayList<>();
	public static List<GOTSpawnEntry> ULTHOS_L = new ArrayList<>();
	public static List<GOTSpawnEntry> VALYRIA_L = new ArrayList<>();
	public static List<GOTSpawnEntry> WALKERS_CONQUEST_L = new ArrayList<>();
	public static List<GOTSpawnEntry> WALKERS_MILITARY_L = new ArrayList<>();
	public static List<GOTSpawnEntry> WESTERLANDS_CIVILIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> WESTERLANDS_CONQUEST_L = new ArrayList<>();
	public static List<GOTSpawnEntry> WESTERLANDS_GUARDIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> WESTERLANDS_MILITARY_L = new ArrayList<>();
	public static List<GOTSpawnEntry> WILDING_GIANT_L = new ArrayList<>();
	public static List<GOTSpawnEntry> WILDING_MILITARY_L = new ArrayList<>();
	public static List<GOTSpawnEntry> WILDING_THENN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> WYVERN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> YITI_CIVILIAN_L = new ArrayList<>();
	public static List<GOTSpawnEntry> YITI_CONQUEST_L = new ArrayList<>();
	public static List<GOTSpawnEntry> YITI_MILITARY_L = new ArrayList<>();

	static {
		ARRYN_CIVILIAN_L.add(new GOTSpawnEntry(GOTEntityArrynMan.class, 10, 1, 2));
		ARRYN_CONQUEST_L.add(new GOTSpawnEntry(GOTEntityArrynSoldier.class, 10, 1, 2));
		ARRYN_CONQUEST_l.add(new GOTSpawnEntry(GOTEntityArrynSoldierArcher.class, 5, 1, 2));
		ARRYN_GUARDIAN_L.add(new GOTSpawnEntry(GOTEntityArrynGuard.class, 10, 1, 2));
		ARRYN_MILITARY_L.add(new GOTSpawnEntry(GOTEntityArrynLevyman.class, 10, 1, 2));
		ARRYN_MILITARY_L.add(new GOTSpawnEntry(GOTEntityArrynLevymanArcher.class, 5, 1, 2));
		CROCODILE_L.add(new GOTSpawnEntry(GOTEntityCrocodile.class, 10, 1, 1));
		CROWNLANDS_CIVILIAN_L.add(new GOTSpawnEntry(GOTEntityCrownlandsMan.class, 10, 1, 2));
		CROWNLANDS_GUARDIAN_L.add(new GOTSpawnEntry(GOTEntityCrownlandsGuard.class, 10, 1, 2));
		CROWNLANDS_MILITARY_L.add(new GOTSpawnEntry(GOTEntityCrownlandsLevyman.class, 10, 1, 2));
		CROWNLANDS_MILITARY_L.add(new GOTSpawnEntry(GOTEntityCrownlandsLevymanArcher.class, 5, 1, 2));
		DESERT_SCORPION_L.add(new GOTSpawnEntry(GOTEntityDesertScorpion.class, 10, 1, 1));
		DORNE_CIVILIAN_L.add(new GOTSpawnEntry(GOTEntityDorneMan.class, 10, 1, 2));
		DORNE_CONQUEST_L.add(new GOTSpawnEntry(GOTEntityDorneSoldier.class, 10, 1, 2));
		DORNE_CONQUEST_L.add(new GOTSpawnEntry(GOTEntityDorneSoldierArcher.class, 5, 1, 2));
		DORNE_MILITARY_L.add(new GOTSpawnEntry(GOTEntityDorneLevyman.class, 10, 1, 2));
		DORNE_MILITARY_L.add(new GOTSpawnEntry(GOTEntityDorneLevymanArcher.class, 5, 1, 2));
		DRAGONSTONE_CIVILIAN_L.add(new GOTSpawnEntry(GOTEntityDragonstoneMan.class, 10, 1, 2));
		DRAGONSTONE_CONQUEST_L.add(new GOTSpawnEntry(GOTEntityDragonstoneSoldier.class, 10, 1, 2));
		DRAGONSTONE_CONQUEST_L.add(new GOTSpawnEntry(GOTEntityDragonstoneSoldierArcher.class, 5, 1, 2));
		DRAGONSTONE_MILITARY_L.add(new GOTSpawnEntry(GOTEntityDragonstoneLevyman.class, 10, 1, 2));
		DRAGONSTONE_MILITARY_L.add(new GOTSpawnEntry(GOTEntityDragonstoneLevymanArcher.class, 5, 1, 2));
		GIFT_CIVILIAN_L.add(new GOTSpawnEntry(GOTEntityGiftMan.class, 10, 1, 2));
		GIFT_GUARDIAN_L.add(new GOTSpawnEntry(GOTEntityGiftGuard.class, 10, 1, 2));
		GOLDEN_MILITARY_L.add(new GOTSpawnEntry(GOTEntityGoldenSpearman.class, 5, 1, 2));
		GOLDEN_MILITARY_L.add(new GOTSpawnEntry(GOTEntityGoldenWarrior.class, 10, 1, 2));
		HILL_TRIBES_CIVILIAN_L.add(new GOTSpawnEntry(GOTEntityHillman.class, 10, 1, 2));
		HILL_TRIBES_MILITARY_L.add(new GOTSpawnEntry(GOTEntityHillmanArcher.class, 5, 1, 2));
		HILL_TRIBES_MILITARY_L.add(new GOTSpawnEntry(GOTEntityHillmanAxeThrower.class, 3, 1, 2));
		HILL_TRIBES_MILITARY_L.add(new GOTSpawnEntry(GOTEntityHillmanBerserker.class, 3, 1, 2));
		HILL_TRIBES_MILITARY_L.add(new GOTSpawnEntry(GOTEntityHillmanWarrior.class, 10, 1, 2));
		IFEKEVRON_L.add(new GOTSpawnEntry(GOTEntityIfekevron.class, 10, 1, 2));
		IRONBORN_CIVILIAN_L.add(new GOTSpawnEntry(GOTEntityIronbornMan.class, 10, 1, 2));
		IRONBORN_CONQUEST_L.add(new GOTSpawnEntry(GOTEntityIronbornSoldier.class, 10, 1, 2));
		IRONBORN_CONQUEST_L.add(new GOTSpawnEntry(GOTEntityIronbornSoldierArcher.class, 5, 1, 2));
		IRONBORN_MILITARY_L.add(new GOTSpawnEntry(GOTEntityIronbornLevyman.class, 10, 1, 2));
		IRONBORN_MILITARY_L.add(new GOTSpawnEntry(GOTEntityIronbornLevymanArcher.class, 5, 1, 2));
		JUNGLE_SCORPION_L.add(new GOTSpawnEntry(GOTEntityJungleScorpion.class, 10, 1, 1));
		MANTICORE_L.add(new GOTSpawnEntry(GOTEntityManticore.class, 10, 1, 1));
		NORTH_CIVILIAN_L.add(new GOTSpawnEntry(GOTEntityNorthMan.class, 10, 1, 2));
		NORTH_CONQUEST_L.add(new GOTSpawnEntry(GOTEntityNorthSoldier.class, 10, 1, 2));
		NORTH_CONQUEST_L.add(new GOTSpawnEntry(GOTEntityNorthSoldierArcher.class, 5, 1, 2));
		NORTH_GUARDIAN_L.add(new GOTSpawnEntry(GOTEntityNorthGuard.class, 10, 1, 2));
		NORTH_HILLMEN_L.add(new GOTSpawnEntry(GOTEntityNorthHillman.class, 10, 1, 2));
		NORTH_HILLMEN_L.add(new GOTSpawnEntry(GOTEntityNorthHillmanArcher.class, 5, 1, 2));
		NORTH_HILLMEN_L.add(new GOTSpawnEntry(GOTEntityNorthHillmanCannibal.class, 5, 1, 2));
		NORTH_HILLMEN_L.add(new GOTSpawnEntry(GOTEntityNorthHillmanMercenary.class, 5, 1, 2));
		NORTH_HILLMEN_L.add(new GOTSpawnEntry(GOTEntityNorthHillmanWarrior.class, 5, 1, 2));
		NORTH_MILITARY_L.add(new GOTSpawnEntry(GOTEntityNorthLevyman.class, 10, 1, 2));
		NORTH_MILITARY_L.add(new GOTSpawnEntry(GOTEntityNorthLevymanArcher.class, 5, 1, 2));
		REACH_CIVILIAN_L.add(new GOTSpawnEntry(GOTEntityReachMan.class, 10, 1, 2));
		REACH_CONQUEST_L.add(new GOTSpawnEntry(GOTEntityReachSoldier.class, 10, 1, 2));
		REACH_CONQUEST_L.add(new GOTSpawnEntry(GOTEntityReachSoldierArcher.class, 5, 1, 2));
		REACH_GUARDIAN_L.add(new GOTSpawnEntry(GOTEntityReachGuard.class, 10, 1, 2));
		REACH_MILITARY_L.add(new GOTSpawnEntry(GOTEntityReachLevyman.class, 10, 1, 2));
		REACH_MILITARY_L.add(new GOTSpawnEntry(GOTEntityReachLevymanArcher.class, 5, 1, 2));
		RED_SCORPION_L.add(new GOTSpawnEntry(GOTEntityRedScorpion.class, 10, 1, 1));
		RIVERLANDS_CIVILIAN_L.add(new GOTSpawnEntry(GOTEntityRiverlandsMan.class, 10, 1, 2));
		RIVERLANDS_CONQUEST_L.add(new GOTSpawnEntry(GOTEntityRiverlandsSoldier.class, 10, 1, 2));
		RIVERLANDS_CONQUEST_L.add(new GOTSpawnEntry(GOTEntityRiverlandsSoldierArcher.class, 5, 1, 2));
		RIVERLANDS_MILITARY_L.add(new GOTSpawnEntry(GOTEntityRiverlandsLevyman.class, 10, 1, 2));
		RIVERLANDS_MILITARY_L.add(new GOTSpawnEntry(GOTEntityRiverlandsLevymanArcher.class, 5, 1, 2));
		SHRYKE_L.add(new GOTSpawnEntry(GOTEntityShryke.class, 10, 1, 2));
		STORMLANDS_CIVILIAN_L.add(new GOTSpawnEntry(GOTEntityStormlandsMan.class, 10, 1, 2));
		STORMLANDS_CONQUEST_L.add(new GOTSpawnEntry(GOTEntityStormlandsSoldier.class, 10, 1, 2));
		STORMLANDS_CONQUEST_L.add(new GOTSpawnEntry(GOTEntityStormlandsSoldierArcher.class, 5, 1, 2));
		STORMLANDS_MILITARY_L.add(new GOTSpawnEntry(GOTEntityStormlandsLevyman.class, 10, 1, 2));
		STORMLANDS_MILITARY_L.add(new GOTSpawnEntry(GOTEntityStormlandsLevymanArcher.class, 5, 1, 2));
		ULTHOS_L.add(new GOTSpawnEntry(GOTEntityUlthosSpider.class, 10, 1, 2));
		VALYRIA_L.add(new GOTSpawnEntry(GOTEntityStoneMan.class, 10, 1, 2));
		WALKERS_CONQUEST_L.add(new GOTSpawnEntry(GOTEntityIceSpider.class, 5, 1, 2));
		WALKERS_CONQUEST_L.add(new GOTSpawnEntry(GOTEntityWhiteWalker.class, 10, 1, 2));
		WALKERS_MILITARY_L.add(new GOTSpawnEntry(GOTEntityWhiteWalker.class, 10, 1, 2));
		WESTERLANDS_CIVILIAN_L.add(new GOTSpawnEntry(GOTEntityWesterlandsMan.class, 10, 1, 2));
		WESTERLANDS_CONQUEST_L.add(new GOTSpawnEntry(GOTEntityWesterlandsSoldier.class, 10, 1, 2));
		WESTERLANDS_CONQUEST_L.add(new GOTSpawnEntry(GOTEntityWesterlandsSoldierArcher.class, 5, 1, 2));
		WESTERLANDS_GUARDIAN_L.add(new GOTSpawnEntry(GOTEntityWesterlandsGuard.class, 10, 1, 2));
		WESTERLANDS_MILITARY_L.add(new GOTSpawnEntry(GOTEntityWesterlandsLevyman.class, 10, 1, 2));
		WESTERLANDS_MILITARY_L.add(new GOTSpawnEntry(GOTEntityWesterlandsLevymanArcher.class, 5, 1, 2));
		WILDING_GIANT_L.add(new GOTSpawnEntry(GOTEntityGiant.class, 10, 1, 1));
		WILDING_MILITARY_L.add(new GOTSpawnEntry(GOTEntityWildling.class, 10, 1, 2));
		WILDING_MILITARY_L.add(new GOTSpawnEntry(GOTEntityWildlingArcher.class, 5, 1, 2));
		WILDING_THENN_L.add(new GOTSpawnEntry(GOTEntityThenn.class, 10, 1, 2));
		WILDING_THENN_L.add(new GOTSpawnEntry(GOTEntityThennArcher.class, 5, 1, 2));
		WILDING_THENN_L.add(new GOTSpawnEntry(GOTEntityThennBerserker.class, 10, 1, 2));
		WYVERN_L.add(new GOTSpawnEntry(GOTEntityWyvern.class, 10, 1, 1));

	}

	public static GOTSpawnList ARRYN_CIVILIAN = new GOTSpawnList(ARRYN_CIVILIAN_L);
	public static GOTSpawnList ARRYN_CONQUEST = new GOTSpawnList(ARRYN_CONQUEST_L);
	public static GOTSpawnList ARRYN_GUARDIAN = new GOTSpawnList(ARRYN_GUARDIAN_L);
	public static GOTSpawnList ARRYN_MILITARY = new GOTSpawnList(ARRYN_MILITARY_L);
	public static GOTSpawnList ASSHAI_CIVILIAN = new GOTSpawnList(ASSHAI_CIVILIAN_L);
	public static GOTSpawnList ASSHAI_MILITARY = new GOTSpawnList(ASSHAI_MILITARY_L);
	public static GOTSpawnList CROCODILE = new GOTSpawnList(CROCODILE_L);
	public static GOTSpawnList CROWNLANDS_CIVILIAN = new GOTSpawnList(CROWNLANDS_CIVILIAN_L);
	public static GOTSpawnList CROWNLANDS_GUARDIAN = new GOTSpawnList(CROWNLANDS_GUARDIAN_L);
	public static GOTSpawnList CROWNLANDS_MILITARY = new GOTSpawnList(CROWNLANDS_MILITARY_L);
	public static GOTSpawnList DESERT_SCORPION = new GOTSpawnList(DESERT_SCORPION_L);
	public static GOTSpawnList DORNE_CIVILIAN = new GOTSpawnList(DORNE_CIVILIAN_L);
	public static GOTSpawnList DORNE_CONQUEST = new GOTSpawnList(DORNE_CONQUEST_L);
	public static GOTSpawnList DORNE_MILITARY = new GOTSpawnList(DORNE_MILITARY_L);
	public static GOTSpawnList DRAGONSTONE_CIVILIAN = new GOTSpawnList(DRAGONSTONE_CIVILIAN_L);
	public static GOTSpawnList DRAGONSTONE_CONQUEST = new GOTSpawnList(DRAGONSTONE_CONQUEST_L);
	public static GOTSpawnList DRAGONSTONE_MILITARY = new GOTSpawnList(DRAGONSTONE_MILITARY_L);
	public static GOTSpawnList GIFT_CIVILIAN = new GOTSpawnList(GIFT_CIVILIAN_L);
	public static GOTSpawnList GIFT_GUARDIAN = new GOTSpawnList(GIFT_GUARDIAN_L);
	public static GOTSpawnList GOLDEN_MILITARY = new GOTSpawnList(GOLDEN_MILITARY_L);
	public static GOTSpawnList HILL_TRIBES_CIVILIAN = new GOTSpawnList(HILL_TRIBES_CIVILIAN_L);
	public static GOTSpawnList HILL_TRIBES_MILITARY = new GOTSpawnList(HILL_TRIBES_MILITARY_L);
	public static GOTSpawnList IBBEN_CIVILIAN = new GOTSpawnList(IBBEN_CIVILIAN_L);
	public static GOTSpawnList IBBEN_MILITARY = new GOTSpawnList(IBBEN_MILITARY_L);
	public static GOTSpawnList IFEKEVRON = new GOTSpawnList(IFEKEVRON_L);
	public static GOTSpawnList IRONBORN_CIVILIAN = new GOTSpawnList(IRONBORN_CIVILIAN_L);
	public static GOTSpawnList IRONBORN_CONQUEST = new GOTSpawnList(IRONBORN_CONQUEST_L);
	public static GOTSpawnList IRONBORN_MILITARY = new GOTSpawnList(IRONBORN_MILITARY_L);
	public static GOTSpawnList JOGOS_MILITARY = new GOTSpawnList(JOGOS_MILITARY_L);
	public static GOTSpawnList JUNGLE_SCORPION = new GOTSpawnList(JUNGLE_SCORPION_L);
	public static GOTSpawnList MANTICORE = new GOTSpawnList(MANTICORE_L);
	public static GOTSpawnList MOSSOVY_CIVILIAN = new GOTSpawnList(MOSSOVY_CIVILIAN_L);
	public static GOTSpawnList MOSSOVY_MILITARY = new GOTSpawnList(MOSSOVY_MILITARY_L);
	public static GOTSpawnList MOSSOVY_WEREWOLF = new GOTSpawnList(MOSSOVY_WEREWOLF_L);
	public static GOTSpawnList NORTH_CIVILIAN = new GOTSpawnList(NORTH_CIVILIAN_L);
	public static GOTSpawnList NORTH_CONQUEST = new GOTSpawnList(NORTH_CONQUEST_L);
	public static GOTSpawnList NORTH_GUARDIAN = new GOTSpawnList(NORTH_GUARDIAN_L);
	public static GOTSpawnList NORTH_HILLMEN = new GOTSpawnList(NORTH_HILLMEN_L);
	public static GOTSpawnList NORTH_MILITARY = new GOTSpawnList(NORTH_MILITARY_L);
	public static GOTSpawnList REACH_CIVILIAN = new GOTSpawnList(REACH_CIVILIAN_L);
	public static GOTSpawnList REACH_CONQUEST = new GOTSpawnList(REACH_CONQUEST_L);
	public static GOTSpawnList REACH_GUARDIAN = new GOTSpawnList(REACH_GUARDIAN_L);
	public static GOTSpawnList REACH_MILITARY = new GOTSpawnList(REACH_MILITARY_L);
	public static GOTSpawnList RED_SCORPION = new GOTSpawnList(RED_SCORPION_L);
	public static GOTSpawnList RIVERLANDS_CIVILIAN = new GOTSpawnList(RIVERLANDS_CIVILIAN_L);
	public static GOTSpawnList RIVERLANDS_CONQUEST = new GOTSpawnList(RIVERLANDS_CONQUEST_L);
	public static GOTSpawnList RIVERLANDS_MILITARY = new GOTSpawnList(RIVERLANDS_MILITARY_L);
	public static GOTSpawnList SHRYKE = new GOTSpawnList(SHRYKE_L);
	public static GOTSpawnList SOTHORYOS_CIVILIAN = new GOTSpawnList(SOTHORYOS_CIVILIAN_L);
	public static GOTSpawnList SOTHORYOS_MILITARY = new GOTSpawnList(SOTHORYOS_MILITARY_L);
	public static GOTSpawnList STORMLANDS_CIVILIAN = new GOTSpawnList(STORMLANDS_CIVILIAN_L);
	public static GOTSpawnList STORMLANDS_CONQUEST = new GOTSpawnList(STORMLANDS_CONQUEST_L);
	public static GOTSpawnList STORMLANDS_MILITARY = new GOTSpawnList(STORMLANDS_MILITARY_L);
	public static GOTSpawnList SUMMER_CIVILIAN = new GOTSpawnList(SUMMER_CIVILIAN_L);
	public static GOTSpawnList SUMMER_MILITARY = new GOTSpawnList(SUMMER_MILITARY_L);
	public static GOTSpawnList ULTHOS = new GOTSpawnList(ULTHOS_L);
	public static GOTSpawnList VALYRIA = new GOTSpawnList(VALYRIA_L);
	public static GOTSpawnList WALKERS_CONQUEST = new GOTSpawnList(WALKERS_CONQUEST_L);
	public static GOTSpawnList WALKERS_MILITARY = new GOTSpawnList(WALKERS_MILITARY_L);
	public static GOTSpawnList WESTERLANDS_CIVILIAN = new GOTSpawnList(WESTERLANDS_CIVILIAN_L);
	public static GOTSpawnList WESTERLANDS_CONQUEST = new GOTSpawnList(WESTERLANDS_CONQUEST_L);
	public static GOTSpawnList WESTERLANDS_GUARDIAN = new GOTSpawnList(WESTERLANDS_GUARDIAN_L);
	public static GOTSpawnList WESTERLANDS_MILITARY = new GOTSpawnList(WESTERLANDS_MILITARY_L);
	public static GOTSpawnList WILDING_GIANT = new GOTSpawnList(WILDING_GIANT_L);
	public static GOTSpawnList WILDING_MILITARY = new GOTSpawnList(WILDING_MILITARY_L);
	public static GOTSpawnList WILDING_THENN = new GOTSpawnList(WILDING_THENN_L);
	public static GOTSpawnList WYVERN = new GOTSpawnList(WYVERN_L);
	public static GOTSpawnList YITI_CIVILIAN = new GOTSpawnList(YITI_CIVILIAN_L);
	public static GOTSpawnList YITI_CONQUEST = new GOTSpawnList(YITI_CONQUEST_L);
	public static GOTSpawnList YITI_MILITARY = new GOTSpawnList(YITI_MILITARY_L);

	public List<GOTSpawnEntry> spawnList;
	public GOTFaction discoveredFaction;

	public GOTSpawnList(List<GOTSpawnEntry> entries) {
		spawnList = entries;
	}

	public GOTFaction getListCommonFaction(World world) {
		if (discoveredFaction != null) {
			return discoveredFaction;
		}
		GOTFaction commonFaction = null;
		for (GOTSpawnEntry entry : spawnList) {
			Class<? extends GOTEntityNPC> entityClass = entry.entityClass;
			if (GOTEntityNPC.class.isAssignableFrom(entityClass)) {
				try {
					GOTEntityNPC npc = (GOTEntityNPC) GOTEntityRegistry.createEntityByClass(entityClass, world);
					GOTFaction fac = npc.getFaction();
					if (commonFaction == null) {
						commonFaction = fac;
						continue;
					}
					if (commonFaction == fac) {
						continue;
					}
					throw new IllegalArgumentException("Spawn lists must contain only one faction! Mismatched entity class: " + entityClass.getName());
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
			throw new IllegalArgumentException("Spawn list must contain only NPCs - invalid " + entityClass.getName());
		}
		if (commonFaction != null) {
			discoveredFaction = commonFaction;
			return discoveredFaction;
		}
		throw new IllegalArgumentException("Failed to discover faction for spawn list");
	}

	public GOTSpawnEntry getRandomSpawnEntry(Random rand) {
		return (GOTSpawnEntry) WeightedRandom.getRandomItem(rand, spawnList);
	}

	public List<GOTSpawnEntry> getReadOnlyList() {
		return new ArrayList<>(spawnList);
	}
}