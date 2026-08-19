package got.client.render.npc;

import got.common.entity.GOTEnchaldBlacksmith;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class GOTRenderBlacksmith extends GOTRenderFactionNPC {

    public GOTRenderBlacksmith() {
        super("blacksmiths");
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        if (entity instanceof GOTEnchaldBlacksmith) {
            GOTEnchaldBlacksmith smith = (GOTEnchaldBlacksmith) entity;
            String faction = smith.getFaction() != null ? smith.getFaction().codeName() : "UNALIGNED";
            GOTEnchaldBlacksmith.BlacksmithType type = smith.getBlacksmithType();
            String typeName = type != null ? type.name() : "MW";
            return new ResourceLocation("got:textures/entity/blacksmiths/" + faction + "_" + typeName + ".png");
        }
        return super.getEntityTexture(entity);
    }
}
