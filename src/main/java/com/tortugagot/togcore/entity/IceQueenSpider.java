package com.tortugagot.togcore.entity;

import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
public class IceQueenSpider extends TOGAbstractEntity implements IAnimatable {

    public IceQueenSpider(World world) {
        super(world);
        this.setSize(3.6F, 3.6F);
    }

    private final AnimationFactory factory = new AnimationFactory(this);

    protected static final AnimationBuilder IDLE = new AnimationBuilder().addAnimation("idle");
    protected static final AnimationBuilder WALK = new AnimationBuilder().addAnimation("walk");

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "idle", 5, this::predicate));
    }

    protected <E extends IceQueenSpider> PlayState predicate(final AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(WALK);
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimation(IDLE);
            return PlayState.CONTINUE;
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
