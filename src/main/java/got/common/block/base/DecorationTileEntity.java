package got.common.block.base;

import got.common.decorations.DecorationsRegister;
import net.minecraft.tileentity.TileEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class DecorationTileEntity extends TileEntity implements IAnimatable{

    private final AnimationFactory manager = new AnimationFactory(this);

    public DecorationTileEntity() {
    }

    private <E extends TileEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        //event.getController().setAnimation((new AnimationBuilder()).addAnimation("idle", true));

        if (DecorationsRegister.findDecorationByBlock(this.getBlockType()) != null) {

            if (DecorationsRegister.findDecorationByBlock(this.getBlockType()).getAnim() != null) {
                event.getController().setAnimation((new AnimationBuilder()).addAnimation("idle", true));
            }
        }


        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0.0F, this::predicate));
    }

    public AnimationFactory getFactory() {
        return this.manager;
    }

}
