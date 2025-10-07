package brain.factions.servers;

import got.common.database.GOTRegistry;
import net.minecraft.item.Item;
import java.util.HashMap;
import java.util.Map;

public class ItemToProvisionMap {

    private static final Map<Item, Integer> itemToProvisionValue = new HashMap<>();

    public static void init() {
        add(GOTRegistry.processedGold, 1);
        add(GOTRegistry.processedSilver, 1);
        add(GOTRegistry.processedIron, 1);
        add(GOTRegistry.barrelCod, 1);
        add(GOTRegistry.barrelSalmon, 1);
        add(GOTRegistry.barrelClownfish, 1);
        add(GOTRegistry.barrelPufferfish, 1);
        add(GOTRegistry.hayBale, 1);
        add(GOTRegistry.sackPotatoes, 1);
        add(GOTRegistry.basketCarrots, 1);
        add(GOTRegistry.cratePumpkins, 1);
        add(GOTRegistry.crateMelons, 1);
        add(GOTRegistry.bundleFlax, 1);
        add(GOTRegistry.bundleTobacco, 1);
        add(GOTRegistry.meatCarcass, 1);
        add(GOTRegistry.woolYarn, 1);
        add(GOTRegistry.bundleFeathers, 1);
        add(GOTRegistry.tannedLeather, 1);
        add(GOTRegistry.bundleFur, 1);
        add(GOTRegistry.bundleHornsClaws, 1);
        add(GOTRegistry.gameCarcass, 1);
        add(GOTRegistry.bundlePoppy, 1);
        add(GOTRegistry.bundlePlantain, 1);
        add(GOTRegistry.sackClay, 1);
        add(GOTRegistry.sackSand, 1);
        add(GOTRegistry.sackGravel, 1);
        add(GOTRegistry.blockDiorite, 1);
        add(GOTRegistry.blockAndesite, 1);
        add(GOTRegistry.blockGranite, 1);
        add(GOTRegistry.pouchAlmonds, 1);
        add(GOTRegistry.barrelApples, 1);
        add(GOTRegistry.crateBananas, 1);
        add(GOTRegistry.basketCherries, 1);
        add(GOTRegistry.bundleDates, 1);
        add(GOTRegistry.crateGrapes, 1);
        add(GOTRegistry.basketLemons, 1);
        add(GOTRegistry.basketLimes, 1);
        add(GOTRegistry.basketMangoes, 1);
        add(GOTRegistry.barrelOlives, 1);
        add(GOTRegistry.basketOranges, 1);
        add(GOTRegistry.basketPears, 1);
        add(GOTRegistry.basketPlums, 1);
        add(GOTRegistry.cratePomegranates, 1);
        add(GOTRegistry.bundleFirewood, 1);
    }

    private static void add(Item item, int value) {
        if (item != null) {
            itemToProvisionValue.put(item, value);
        } else {
            CoreFaction.LOGGER.warning("Рег null item для провизий. беда в классе ItemToProvisionMap.java");
        }
    }

    public static int getProvisionValue(Item item) {
        return itemToProvisionValue.getOrDefault(item, 0);
    }

    public static boolean isProvisionItem(Item item) {
        return itemToProvisionValue.containsKey(item);
    }
}