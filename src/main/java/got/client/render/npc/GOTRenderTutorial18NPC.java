package got.client.render.npc;

import got.client.model.GOTModelTutorial18;

public class GOTRenderTutorial18NPC extends GOTRenderFactionNPC {

    public GOTRenderTutorial18NPC(String texture) {
        super(texture);
        this.mainModel = new GOTModelTutorial18(0.0f);
        this.modelBipedMain = (got.client.model.GOTModelBiped) this.mainModel;
    }

    public GOTRenderTutorial18NPC(String texture, float height) {
        super(texture, height);
        this.mainModel = new GOTModelTutorial18(0.0f);
        this.modelBipedMain = (got.client.model.GOTModelBiped) this.mainModel;
    }
}
