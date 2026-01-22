package com.tortugagot.togcore.registry;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import com.tortugagot.togcore.TogCore;
import com.tortugagot.togcore.entity.Beaver;
import com.tortugagot.togcore.entity.Deer;
import com.tortugagot.togcore.entity.Duck;
import com.tortugagot.togcore.entity.Fauxcase;
import com.tortugagot.togcore.entity.Goose;
import com.tortugagot.togcore.entity.Hedgehog;
import com.tortugagot.togcore.entity.IceQueenSpider;
import com.tortugagot.togcore.entity.Knight;
import com.tortugagot.togcore.entity.KnightOnHorse;
import com.tortugagot.togcore.entity.Mimic;
import com.tortugagot.togcore.entity.PlantMonster;
import com.tortugagot.togcore.entity.Sprout;
import com.tortugagot.togcore.entity.SproutMinion;
import com.tortugagot.togcore.entity.Squirrel;
import com.tortugagot.togcore.entity.StoneGolem;
import com.tortugagot.togcore.entity.StoneGolemMinion;
import com.tortugagot.togcore.entity.ToffyCrateCreature;
import com.tortugagot.togcore.entity.ToffyCrateSweetTooth;
import com.tortugagot.togcore.entity.Wraith;

import cpw.mods.fml.common.registry.EntityRegistry;

public class TOGEntityRegistry {

    public static void registerEntities() {
        registerEntity(Beaver.class, "beaver", 0x996600, 0xFFFFFF);
        registerEntity(Deer.class, "deer", 0x996600, 0xFFFFFF);
        registerEntity(Duck.class, "duck", 0x996600, 0xFFFFFF);
        registerEntity(Fauxcase.class, "fauxcase", 0x996600, 0xFFFFFF);
        registerEntity(Goose.class, "goose", 0x996600, 0xFFFFFF);
        registerEntity(Hedgehog.class, "hedgehog", 0x996600, 0xFFFFFF);
        registerEntity(IceQueenSpider.class, "ice_queen_spider", 0x996600, 0xFFFFFF);
        registerEntity(Knight.class, "knight", 0x996600, 0xFFFFFF);
        registerEntity(KnightOnHorse.class, "knight_on_horse", 0x996600, 0xFFFFFF);
        registerEntity(Mimic.class, "mimic", 0x996600, 0xFFFFFF);
        registerEntity(PlantMonster.class, "plant_monster", 0x996600, 0xFFFFFF);
        registerEntity(Sprout.class, "sprout", 0x996600, 0xFFFFFF);
        registerEntity(SproutMinion.class, "sprout_minion", 0x996600, 0xFFFFFF);
        registerEntity(Squirrel.class, "squirrel", 0x996600, 0xFFFFFF);
        registerEntity(StoneGolem.class, "stone_golem", 0x996600, 0xFFFFFF);
        registerEntity(StoneGolemMinion.class, "stone_golem_minion", 0x996600, 0xFFFFFF);
        registerEntity(ToffyCrateSweetTooth.class, "toffy_crate_sweet_tooth", 0x996600, 0xFFFFFF);
        registerEntity(ToffyCrateCreature.class, "toffy_crate_creature", 0x996600, 0xFFFFFF);
        registerEntity(Wraith.class, "wraith", 0x996600, 0xFFFFFF);
    }

    private static void registerEntity(Class<? extends Entity> entityClass, String name, int eggColor1, int eggColor2) {
        int id = EntityRegistry.findGlobalUniqueEntityId();
        ResourceLocation rl = new ResourceLocation(TogCore.MODID, name);
        EntityRegistry.registerGlobalEntityID(entityClass, rl.toString(), id, eggColor1, eggColor2);
        EntityRegistry.registerModEntity(entityClass, name, id, TogCore.instance, 64, 1, true);
    }
}
