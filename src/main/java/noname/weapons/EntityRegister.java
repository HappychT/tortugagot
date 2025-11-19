package noname.weapons;

import cpw.mods.fml.common.registry.EntityRegistry;
import got.GOT;
import noname.weapons.entity.*;

public class EntityRegister {

    public static void registerEntities() {

        EntityRegistry.registerModEntity(
                EntityCatapult.class,
                "catapult",
                100,
                GOT.instance,
                80,
                3,
                true
        );


        EntityRegistry.registerModEntity(
                EntityStoneProjectile.class,
                "stone_projectile",
                101,
                GOT.instance,
                64,
                10,
                true
        );
        EntityRegistry.registerModEntity(
                EntityTribushet.class,
                "tribushet",
                102,
                GOT.instance,
                80,
                3,
                true
        );
        EntityRegistry.registerModEntity(
                EntityBatteringRam.class,
                "battering_ram",
                103,
                GOT.instance,
                80,
                3,
                true
        );
        EntityRegistry.registerModEntity(
                EntityRamSeat.class,
                "battering_ram_seat",
                104,
                GOT.instance,
                64,
                1,
                false
        );
        EntityRegistry.registerModEntity(
                EntityBalista.class,
                "balista",
                105,
                GOT.instance,
                80,
                3,
                true
        );
        EntityRegistry.registerModEntity(
                EntityBalistaProjectile.class,
                "balista_projectile",
                106,
                GOT.instance,
                80,
                1,
                true
        );
    }
}

