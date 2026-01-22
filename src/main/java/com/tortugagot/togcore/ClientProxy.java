package com.tortugagot.togcore;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

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
import com.tortugagot.togcore.models.BeaverModel;
import com.tortugagot.togcore.models.DeerModel;
import com.tortugagot.togcore.models.DuckModel;
import com.tortugagot.togcore.models.FauxcaseModel;
import com.tortugagot.togcore.models.GooseModel;
import com.tortugagot.togcore.models.HedgehogModel;
import com.tortugagot.togcore.models.IceQueenSpiderModel;
import com.tortugagot.togcore.models.KnightModel;
import com.tortugagot.togcore.models.KnightOnHorseModel;
import com.tortugagot.togcore.models.MimicModel;
import com.tortugagot.togcore.models.PlantMonsterModel;
import com.tortugagot.togcore.models.SproutMinionModel;
import com.tortugagot.togcore.models.SproutModel;
import com.tortugagot.togcore.models.SquirrelModel;
import com.tortugagot.togcore.models.StoneGolemMinionModel;
import com.tortugagot.togcore.models.StoneGolemModel;
import com.tortugagot.togcore.models.ToffyCrateCreatureModel;
import com.tortugagot.togcore.models.ToffyCrateSweetToothModel;
import com.tortugagot.togcore.models.WraithModel;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(Beaver.class, new RenderLiving(new BeaverModel(), 0.5f) {

            @Override
            protected ResourceLocation getEntityTexture(Entity entity) {
                return new ResourceLocation("togcore:textures/entity/beaver.png");
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(Deer.class, new RenderLiving(new DeerModel(), 0.5f) {

            @Override
            protected ResourceLocation getEntityTexture(Entity entity) {
                return new ResourceLocation("togcore:textures/entity/deer_male.png");
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(Duck.class, new RenderLiving(new DuckModel(), 0.5f) {

            @Override
            protected ResourceLocation getEntityTexture(Entity entity) {
                return new ResourceLocation("togcore:textures/entity/duck_male.png");
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(Fauxcase.class, new RenderLiving(new FauxcaseModel(), 0.5f) {

            @Override
            protected ResourceLocation getEntityTexture(Entity entity) {
                return new ResourceLocation("togcore:textures/entity/fauxcase.png");
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(Goose.class, new RenderLiving(new GooseModel(), 0.5f) {

            @Override
            protected ResourceLocation getEntityTexture(Entity entity) {
                return new ResourceLocation("togcore:textures/entity/goose.png");
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(Hedgehog.class, new RenderLiving(new HedgehogModel(), 0.5f) {

            @Override
            protected ResourceLocation getEntityTexture(Entity entity) {
                return new ResourceLocation("togcore:textures/entity/hedgehog.png");
            }
        });
        RenderingRegistry
            .registerEntityRenderingHandler(IceQueenSpider.class, new RenderLiving(new IceQueenSpiderModel(), 0.5f) {

                @Override
                protected ResourceLocation getEntityTexture(Entity entity) {
                    return new ResourceLocation("togcore:textures/entity/ice_queen_spider.png");
                }
            });
        RenderingRegistry.registerEntityRenderingHandler(Knight.class, new RenderLiving(new KnightModel(), 0.5f) {

            @Override
            protected ResourceLocation getEntityTexture(Entity entity) {
                return new ResourceLocation("togcore:textures/entity/knight.png");
            }
        });
        RenderingRegistry
            .registerEntityRenderingHandler(KnightOnHorse.class, new RenderLiving(new KnightOnHorseModel(), 0.5f) {

                @Override
                protected ResourceLocation getEntityTexture(Entity entity) {
                    return new ResourceLocation("togcore:textures/entity/knight_on_horse.png");
                }
            });
        RenderingRegistry.registerEntityRenderingHandler(Mimic.class, new RenderLiving(new MimicModel(), 0.5f) {

            @Override
            protected ResourceLocation getEntityTexture(Entity entity) {
                return new ResourceLocation("togcore:textures/entity/mimic.png");
            }
        });
        RenderingRegistry
            .registerEntityRenderingHandler(PlantMonster.class, new RenderLiving(new PlantMonsterModel(), 0.5f) {

                @Override
                protected ResourceLocation getEntityTexture(Entity entity) {
                    return new ResourceLocation("togcore:textures/entity/plant_monster.png");
                }
            });
        RenderingRegistry.registerEntityRenderingHandler(Sprout.class, new RenderLiving(new SproutModel(), 0.5f) {

            @Override
            protected ResourceLocation getEntityTexture(Entity entity) {
                return new ResourceLocation("togcore:textures/entity/sprout.png");
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(Squirrel.class, new RenderLiving(new SquirrelModel(), 0.5f) {

            @Override
            protected ResourceLocation getEntityTexture(Entity entity) {
                return new ResourceLocation("togcore:textures/entity/squirrel.png");
            }
        });
        RenderingRegistry
            .registerEntityRenderingHandler(StoneGolem.class, new RenderLiving(new StoneGolemModel(), 0.5f) {

                @Override
                protected ResourceLocation getEntityTexture(Entity entity) {
                    return new ResourceLocation("togcore:textures/entity/stone_golem.png");
                }
            });
        RenderingRegistry.registerEntityRenderingHandler(
            StoneGolemMinion.class,
            new RenderLiving(new StoneGolemMinionModel(), 0.5f) {

                @Override
                protected ResourceLocation getEntityTexture(Entity entity) {
                    return new ResourceLocation("togcore:textures/entity/stone_golem_minion.png");
                }
            });
        RenderingRegistry
            .registerEntityRenderingHandler(SproutMinion.class, new RenderLiving(new SproutMinionModel(), 0.5f) {

                @Override
                protected ResourceLocation getEntityTexture(Entity entity) {
                    return new ResourceLocation("togcore:textures/entity/sprout_minion.png");
                }
            });
        RenderingRegistry.registerEntityRenderingHandler(
            ToffyCrateCreature.class,
            new RenderLiving(new ToffyCrateCreatureModel(), 0.5f) {

                @Override
                protected ResourceLocation getEntityTexture(Entity entity) {
                    return new ResourceLocation("togcore:textures/entity/toffy_crate_creature.png");
                }
            });
        RenderingRegistry.registerEntityRenderingHandler(
            ToffyCrateSweetTooth.class,
            new RenderLiving(new ToffyCrateSweetToothModel(), 0.5f) {

                @Override
                protected ResourceLocation getEntityTexture(Entity entity) {
                    return new ResourceLocation("togcore:textures/entity/toffy_crate_sweet_tooth.png");
                }
            });
        RenderingRegistry.registerEntityRenderingHandler(Wraith.class, new RenderLiving(new WraithModel(), 0.5f) {

            @Override
            protected ResourceLocation getEntityTexture(Entity entity) {
                return new ResourceLocation("togcore:textures/entity/wraith.png");
            }
        });
    }
}
