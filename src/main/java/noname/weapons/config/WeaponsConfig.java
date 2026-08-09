package noname.weapons.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import java.io.File;

public class WeaponsConfig {

    private static Configuration config;

    public static int reloadTimeCatapult = 500;
    public static float rotationSpeedCatapult = 5.0F;
    public static double catapultProjectileSpeed = 1.5D;
    public static float baseDamage = 1.0F;
    public static float damageFalloff = 0.1F;
    public static float explosionRadius = 5.0F;
    public static float damagePerHarvestLevel = 1.0F;
    public static float entityDirectDamage = 1000.0F;
    public static float entitySplashRadius = 4.0F;
    public static float entitySplashDamageFalloff = 0.1F;
    public static float maxHealthCatapult = 500.0F;
    public static float armor = 20.0F;
    public static float reloadTimeTribushet = 500;
    public static float rotationSpeedTribushet = 5.0F;
    public static float maxHealthTribushet = 500.0F;
    public static double tribushetProjectileSpeed = 2.5D;

    public static float maxHealthBalista = 100.0F;
    public static float rotationSpeedBalista = 200.0F;
    public static float reloadTimeBalista = 10;
    public static double BalistaProjectileSpeed = 2.5D;
    public static float entitySplashRadiusBalista = 2.0F;
    public static float damageBalista = 600.0F;
    public static float maxRangeBalsita = 250.0F;
    public static float rotationSpeedBatteringRam = 2.0F;
    public static float movementSpeedBatteringRam = 0.15F;
    public static float maxHealthBatteringRam = 500.0F;
    public static float armorBatteringRam = 20.0F;


    public static void loadConfig(FMLPreInitializationEvent event) {
        File configFile = new File(event.getModConfigurationDirectory(), "Test.cfg");
        config = new Configuration(configFile);

        try {
            config.load();
            Property reloadTimeProp = config.get("catapult", "reloadTimeCatapult", 500,
                    "Time in ticks for catapult to reload (20 ticks = 1 second). Default: 500 (25 seconds)");
            reloadTimeCatapult = reloadTimeProp.getInt();
            Property maxHealthProp = config.get("catapult", "maxHealthCatapult", 500.0D,
                    "Maximum health of catapult. Default: 500.0");
            maxHealthCatapult = (float) maxHealthProp.getDouble();

            Property armorProp = config.get("catapult", "armor", 20.0D,
                    "Armor of catapult (reduces damage taken). Default: 20.0");
            armor = (float) armorProp.getDouble();

            Property rotationSpeedProp = config.get("catapult", "rotationSpeedCatapult", 5.0D,
                    "Rotation speed of catapult in degrees per tick. Default: 5.0 (higher = faster). Min: 0.1, Max: 180.0");
            rotationSpeedCatapult = (float) rotationSpeedProp.getDouble();

            Property catapultProjectileSpeedProp = config.get("catapult", "projectileSpeed", 1.5D,
                    "Launch speed for catapult projectile. Default: 1.5");
            catapultProjectileSpeed = catapultProjectileSpeedProp.getDouble();

            Property reloadTimeTribushetProp =config.get("tribushet", "reloadTimeTribushet", 600,
                    "Time in ticks for tribushet to reload (20 ticks = 1 second). Default: 600 (30 seconds)");
            reloadTimeTribushet = reloadTimeTribushetProp.getInt();

            Property rotationSpeedTribushetProp = config.get("tribushet", "rotationSpeedTribushet", 5.0D,
                    "Rotation speed of tribushet in degrees per tick. Default: 5.0 (higher = faster). Min: 0.1, Max: 180.0");
            rotationSpeedTribushet = (float) rotationSpeedTribushetProp.getDouble();

            Property maxHealthTribushetProp = config.get("tribushet", "maxHealthTribushet", 500.0D,
                    "Maximum health of tribushet. Default: 500.0");
            maxHealthTribushet = (float) maxHealthTribushetProp.getDouble();

            Property tribushetProjectileSpeedProp = config.get("tribushet", "projectileSpeed", 2.5D,
                    "Launch speed for tribushet projectile. Default: 2.5");
            tribushetProjectileSpeed = tribushetProjectileSpeedProp.getDouble();

            Property maxHealthBalistaProp = config.get("balista", "maxHealthBalista", 100.0D,
                    "Maximum health of balista. Default: 100.0");
            maxHealthBalista = (float) maxHealthBalistaProp.getDouble();

            Property rotationSpeedBalistaProp = config.get("balista", "rotationSpeedBalista", 5.0D,
                    "Rotation speed of balista in degrees per tick. Default: 5.0");
            rotationSpeedBalista = (float) rotationSpeedBalistaProp.getDouble();

            Property reloadTimeBalistaProp = config.get("balista", "reloadTimeBalista", 10,
                    "Time in ticks for balista to reload (20 ticks = 1 second). Default: 10 (0.5 seconds)");
            reloadTimeBalista = reloadTimeBalistaProp.getInt();

            Property balistaProjectileSpeedProp = config.get("balista", "projectileSpeed", 2.5D,
                    "Launch speed for balista projectile. Default: 2.5");
            BalistaProjectileSpeed = balistaProjectileSpeedProp.getDouble();

            Property entitySplashRadiusBalistaProp = config.get("balista", "entitySplashRadiusBalista", 2.0D,
                    "Splash damage radius for balista projectile. Default: 2.0");
            entitySplashRadiusBalista = (float) entitySplashRadiusBalistaProp.getDouble();

            Property damageBalistaProp = config.get("balista", "damageBalista", 600.0D,
                    "Damage dealt by balista projectile. Default: 600.0");
            damageBalista = (float) damageBalistaProp.getDouble();

            Property maxRangeBalistaProp = config.get("balista", "maxRangeBalista", 250.0D,
                    "Maximum range for balista projectile. Default: 250.0");
            maxRangeBalsita = (float) maxRangeBalistaProp.getDouble();

            Property rotationSpeedBatteringRamProp = config.get("battering_ram", "rotationSpeedBatteringRam", 2.0D,
                    "Maximum rotation change per tick for the battering ram. Default: 2.0");
            rotationSpeedBatteringRam = (float) rotationSpeedBatteringRamProp.getDouble();

            Property MovementSpeedBatteringRamProp = config.get("battering_ram", "MovementSpeedBatteringRamProp", 0.15D,
                    "Maximum movement change per tick for the battering ram. Default: 0.15");
            movementSpeedBatteringRam = (float) MovementSpeedBatteringRamProp.getDouble();

            Property maxHealthBatteringRamProp = config.get("battering_ram", "maxHealthBatteringRam", 500.0D,
                    "Maximum health of battering ram. Default: 500.0");
            maxHealthBatteringRam = (float) maxHealthBatteringRamProp.getDouble();

            Property armorBatteringRamProp = config.get("battering_ram", "armorBatteringRam", 20.0D,
                    "Armor of battering ram (reduces damage). Default: 20.0");
            armorBatteringRam = (float) armorBatteringRamProp.getDouble();

            Property baseDamageProp = config.get("projectile", "baseDamage", 1.0D,
                    "Base damage dealt to blocks at epicenter. Default: 1.0");
            baseDamage = (float) baseDamageProp.getDouble();

            Property damageFalloffProp = config.get("projectile", "damageFalloff", 0.1D,
                    "Damage reduction per block distance from epicenter. Default: 0.1");
            damageFalloff = (float) damageFalloffProp.getDouble();

            Property explosionRadiusProp = config.get("projectile", "explosionRadius", 5.0D,
                    "Radius of explosion effect. Default: 5.0");
            explosionRadius = (float) explosionRadiusProp.getDouble();

            Property damagePerHarvestLevelProp = config.get("projectile", "damagePerHarvestLevel", 1.0D,
                    "Additional damage per block harvest level. Default: 1.0");
            damagePerHarvestLevel = (float) damagePerHarvestLevelProp.getDouble();

            Property entityDirectDamageProp = config.get("entity_damage", "directDamage", 1000.0D,
                    "Damage dealt to entity on direct hit (when projectile enters hitbox). Default: 1000.0");
            entityDirectDamage = (float) entityDirectDamageProp.getDouble();

            Property entitySplashRadiusProp = config.get("entity_damage", "splashRadius", 4.0D,
                    "Splash damage radius in blocks (approximately 4x4 blocks). Default: 4.0");
            entitySplashRadius = (float) entitySplashRadiusProp.getDouble();

            Property entitySplashDamageFalloffProp = config.get("entity_damage", "splashDamageFalloff", 0.1D,
                    "Splash damage reduction per block distance from epicenter. Default: 0.1");
            entitySplashDamageFalloff = (float) entitySplashDamageFalloffProp.getDouble();

        } catch (Exception ignored) {
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    public static Configuration getConfig() {
        return config;
    }
}