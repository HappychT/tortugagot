package com.tortugagot.togcore.client.renderer;

import com.tortugagot.togcore.TogCore;
import com.tortugagot.togcore.client.model.entity.*;
import com.tortugagot.togcore.client.renderer.entity.IceQueenSpiderRenderer;
import com.tortugagot.togcore.entity.*;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class TOGRenderers {
    static {
        register(Beaver.class, new BeaverModel(), "togcore:textures/entity/beaver.png");
        register(Deer.class, new DeerModel(), "togcore:textures/entity/deer_male.png");
        register(Duck.class, new DuckModel(), "togcore:textures/entity/duck_male.png");
        register(Fauxcase.class, new FauxcaseModel(), "togcore:textures/entity/fauxcase.png");
        register(Goose.class, new GooseModel(), "togcore:textures/entity/goose.png");
        register(Hedgehog.class, new HedgehogModel(), "togcore:textures/entity/hedgehog.png");
        register(Knight.class, new KnightModel(), "togcore:textures/entity/knight.png");
        register(KnightOnHorse.class, new KnightOnHorseModel(), "togcore:textures/entity/knight_on_horse.png");
        register(Mimic.class, new MimicModel(), "togcore:textures/entity/mimic.png");
        register(PlantMonster.class, new PlantMonsterModel(), "togcore:textures/entity/plant_monster.png");
        register(Sprout.class, new SproutModel(), "togcore:textures/entity/sprout.png");
        register(SproutMinion.class, new SproutMinionModel(), "togcore:textures/entity/sprout_minion.png");
        register(Squirrel.class, new SquirrelModel(), "togcore:textures/entity/squirrel.png");
        register(StoneGolem.class, new StoneGolemModel(), "togcore:textures/entity/stone_golem.png");
        register(StoneGolemMinion.class, new StoneGolemMinionModel(), "togcore:textures/entity/stone_golem_minion.png");
        register(ToffyCrateCreature.class, new ToffyCrateCreatureModel(), "togcore:textures/entity/toffy_crate_creature.png");
        register(ToffyCrateSweetTooth.class, new ToffyCrateSweetToothModel(), "togcore:textures/entity/toffy_crate_sweet_tooth.png");
        register(Wraith.class, new WraithModel(), "togcore:textures/entity/wraith.png");

        // geo models
        RenderingRegistry.registerEntityRenderingHandler(IceQueenSpider.class, new IceQueenSpiderRenderer());
    }

    private static void register (Class<? extends Entity> entity, ModelBase model, String resourceLocation) {
        RenderingRegistry.registerEntityRenderingHandler(entity, new RenderLiving(model, 0.5f) {
            @Override
            protected ResourceLocation getEntityTexture(Entity entity) {
                return new ResourceLocation(resourceLocation);
            }
        });
    }

    public static void init() {
        TogCore.LOG.debug("Register renderers for "+TogCore.MODID);
    }
}
