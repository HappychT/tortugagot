package got.common.decorations;


import got.client.model.customDecorations.base.WOWBaseDecAnimModel;
import got.client.model.customDecorations.base.WOWBaseDecModel;
import got.common.decorations.base.Decoration;
import got.common.itemreg.GOTItems;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.ArrayList;

public class DecorationsRegister {

    public static ArrayList<Decoration> decorations = new ArrayList<>();

    public static ArrayList<Decoration> getDecorations() {
        return decorations;
    }

    public static void registerDecorations() {
        decorations.add(new Decoration(GOTItems.WOWBarricadeBlock, new ResourceLocation("got",
                "textures/blocks/textures/decorations/dungeon_barricade.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/dungeon_barricade.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWHangingCage, new ResourceLocation("got",
                "textures/blocks/textures/decorations/dungeon_cage.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/hanging_cage.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWBigCactus, new ResourceLocation("got",
                "textures/blocks/textures/decorations/plants_big_cactus.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/plants_big_cactus.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWHangingPothos, new ResourceLocation("got",
                "textures/blocks/textures/decorations/plants_hanging_pothos.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/plants_hanging_pothos.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWVinesFlowering, new ResourceLocation("got",
                "textures/blocks/textures/decorations/plants_vines_flowering.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/plants_vines_flowering.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWSmallFir, new ResourceLocation("got",
                "textures/blocks/textures/decorations/plants_small_fir.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/plants_small_fir.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWDungeonShackles, new ResourceLocation("got",
                "textures/blocks/textures/decorations/dungeon_shackles.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/dungeon_shackles.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWDungeonPillory, new ResourceLocation("got",
                "textures/blocks/textures/decorations/dungeon_pillory.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/dungeon_pillory.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWGoblintekcoil, new ResourceLocation("got",
                "textures/blocks/textures/decorations/goblintek_coil.png"),
                new WOWBaseDecAnimModel(new ResourceLocation("got", "geo/goblintek_coil.geo.json")),
                new ResourceLocation("got", "animations/goblintekcoil.animation.json")));

        decorations.add(new Decoration(GOTItems.WOWSkibidiToilet, new ResourceLocation("got",
                "textures/blocks/textures/decorations/skibidi_toilet.png"),
                new WOWBaseDecAnimModel(new ResourceLocation("got", "geo/skibiditoilet.geo.json")),
                new ResourceLocation("got", "animations/skibiditoilet.animation.json")));

        decorations.add(new Decoration(GOTItems.BarrelDecor1, new ResourceLocation("got",
                "textures/blocks/textures/decorations/barrel.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/barrel.geo.json"))));

        decorations.add(new Decoration(GOTItems.BarrelDecor2, new ResourceLocation("got",
                "textures/blocks/textures/decorations/barrel2.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/barrel.geo.json"))));

        decorations.add(new Decoration(GOTItems.BarrelDecor3, new ResourceLocation("got",
                "textures/blocks/textures/decorations/barrel3.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/barrel.geo.json"))));

        decorations.add(new Decoration(GOTItems.leatherStand, new ResourceLocation("got",
                "textures/blocks/textures/decorations/leather_stand.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/leather_stand.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWWineBottle, new ResourceLocation("got",
                "textures/blocks/textures/decorations/wine.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/wine_bottle.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWGrass, new ResourceLocation("got",
                "textures/blocks/textures/decorations/grass_deco.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/grass_deco.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWDeer, new ResourceLocation("got",
                "textures/blocks/textures/decorations/deerbody.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/deer_body_table.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWDead1, new ResourceLocation("got",
                "textures/blocks/textures/decorations/corpse_1.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/deadbody_pose1.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWDead2, new ResourceLocation("got",
                "textures/blocks/textures/decorations/corpse_2.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/deadbody_pose1.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWDead3, new ResourceLocation("got",
                "textures/blocks/textures/decorations/corpse_3.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/deadbody_pose1.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWDead4, new ResourceLocation("got",
                "textures/blocks/textures/decorations/corpse_4.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/deadbody_pose1.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWDead5, new ResourceLocation("got",
                "textures/blocks/textures/decorations/corpse_1.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/deadbody_pose2.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWDead6, new ResourceLocation("got",
                "textures/blocks/textures/decorations/corpse_2.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/deadbody_pose2.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWDead7, new ResourceLocation("got",
                "textures/blocks/textures/decorations/corpse_3.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/deadbody_pose2.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWDead8, new ResourceLocation("got",
                "textures/blocks/textures/decorations/corpse_4.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/deadbody_pose2.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWCow, new ResourceLocation("got",
                "textures/blocks/textures/decorations/tablewithbody.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/tablewithbodyl.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWMixtures1, new ResourceLocation("got",
                "textures/blocks/textures/decorations/mixtures.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/mixtures_1.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWMixtures2, new ResourceLocation("got",
                "textures/blocks/textures/decorations/mixtures.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/mixtures_2.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWMixtures3, new ResourceLocation("got",
                "textures/blocks/textures/decorations/mixtures.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/mixtures_3.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWMixtures4, new ResourceLocation("got",
                "textures/blocks/textures/decorations/mixtures.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/mixtures_4.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWLeatherLighter, new ResourceLocation("got",
                "textures/blocks/textures/decorations/leather_lighter.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/leather_lighter.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWLighter, new ResourceLocation("got",
                "textures/blocks/textures/decorations/copper_light.png"),
                new WOWBaseDecAnimModel(new ResourceLocation("got", "geo/copper_light.geo.json")),
                new ResourceLocation("got", "animations/copper_light.animation.json")));

        decorations.add(new Decoration(GOTItems.WOWDeerDeco, new ResourceLocation("got",
                "textures/blocks/textures/decorations/deer_deco.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/deer_deco.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWSpearHead1, new ResourceLocation("got",
                "textures/blocks/textures/decorations/head_on_spear.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/head_on_spear1.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWSpearHead2, new ResourceLocation("got",
                "textures/blocks/textures/decorations/head_on_spear2.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/head_on_spear2.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWHandWheel, new ResourceLocation("got",
                "textures/blocks/textures/decorations/handwheel.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/hand_wheel.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWChains1, new ResourceLocation("got",
                "textures/blocks/textures/decorations/chain_texture.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/chains_single.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWChains2, new ResourceLocation("got",
                "textures/blocks/textures/decorations/chain_texture.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/double_chains.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWChains3, new ResourceLocation("got",
                "textures/blocks/textures/decorations/chain_texture.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/triple_chains.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWCandle1, new ResourceLocation("got",
                "textures/blocks/textures/decorations/candle.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/candle_t1.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWCandle2, new ResourceLocation("got",
                "textures/blocks/textures/decorations/candle.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/candle_t2.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWCandle3, new ResourceLocation("got",
                "textures/blocks/textures/decorations/candle.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/candle_t3.geo.json"))));

        decorations.add(new Decoration(GOTItems.WOWCandle4, new ResourceLocation("got",
                "textures/blocks/textures/decorations/candle.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/candle_t4.geo.json"))));

        
    }



    public static Decoration findDecorationByModel(AnimatedGeoModel<?> model) {
        for (Decoration decoration : decorations) {
            if (decoration.getModel().equals(model)) {
                return decoration;
            }
        }
        return null; // Return null if no matching decoration is found
    }

    public static Decoration findDecorationByBlock(Block block) {
        for (Decoration decoration : decorations) {
            if (decoration.getItem().equals(block)) {
                return decoration;
            }
        }
        return null; // Return null if no matching decoration is found
    }
}
