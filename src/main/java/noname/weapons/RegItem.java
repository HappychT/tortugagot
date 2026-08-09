package noname.weapons;

import cpw.mods.fml.common.registry.GameRegistry;
import noname.weapons.item.*;

public class RegItem {

    public static ItemCatapultSpawner catapultSpawner;
    public static ItemStoneProjectile stoneProjectile;
    public static ItemTribushetSpawner tribushetSpawner;
    public static ItemBatteringRamSpawner batteringRamSpawner;
    public static ItemBalistaSpawner balistaSpawner;
    public static ItemRepairKit repairKit;
    public static ItemBalistaBolt balistaBolt;

    public static void regitem() {
        catapultSpawner = new ItemCatapultSpawner();
        GameRegistry.registerItem(catapultSpawner, "catapult_spawner");

        stoneProjectile = new ItemStoneProjectile();
        GameRegistry.registerItem(stoneProjectile, "stone_projectile");

        tribushetSpawner = new ItemTribushetSpawner();
        GameRegistry.registerItem(tribushetSpawner, "tribute_spawner");

        batteringRamSpawner = new ItemBatteringRamSpawner();
        GameRegistry.registerItem(batteringRamSpawner, "battering_ram_spawner");

        balistaSpawner = new ItemBalistaSpawner();
        GameRegistry.registerItem(balistaSpawner, "balista_spawner");

        repairKit = new ItemRepairKit();
        GameRegistry.registerItem(repairKit, "repair_kit");

        balistaBolt = new ItemBalistaBolt();
        GameRegistry.registerItem(balistaBolt, "balista_bolt");
    }
}
