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

        decorations.add(new Decoration(GOTItems.BarrelDecor, new ResourceLocation("got",
                "textures/blocks/textures/decorations/barrel.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/barrel.geo.json"))));

        decorations.add(new Decoration(GOTItems.leatherStand, new ResourceLocation("got",
                "textures/blocks/textures/decorations/leather_stand.png"),
                new WOWBaseDecModel(new ResourceLocation("got", "geo/leather_stand.geo.json"))));

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
