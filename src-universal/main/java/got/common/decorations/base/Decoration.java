package got.common.decorations.base;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.provider.GeoModelProvider;

public class Decoration {

    private Block item;
    private ResourceLocation tex;
    private GeoModelProvider<?> model;
    private ResourceLocation anim;

    public Decoration(Block item, ResourceLocation tex, GeoModelProvider<?> model) {
        this.item = item;
        this.tex = tex;
        this.model = model;
    }

    public Decoration(Block item, ResourceLocation tex, GeoModelProvider<?> model, ResourceLocation anim) {
        this.item = item;
        this.tex = tex;
        this.model = model;
        this.anim = anim;
    }

    public ResourceLocation getAnim() {
        return anim;
    }

    public Block getItem() {
        return item;
    }

    public ResourceLocation getTex() {
        return tex;
    }

    public GeoModelProvider<?> getModel() {
        return model;
    }


}
