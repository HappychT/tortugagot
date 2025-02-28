package got.common.entity.other;

import java.util.List;

import got.common.entity.ai.GOTEntityAINearestAttackableTargetBasic;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.EntityCreature;
import net.minecraft.world.World;

public class GOTEntityDarkHuorn extends GOTEntityHuornBase {
    public GOTEntityDarkHuorn(World world) {
        super(world);
        this.addTargetTasks(true, GOTEntityAINearestAttackableTargetHuorn.class);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        setTreeType(0);
    }

    @Override
    public float getAlignmentBonus() {
        return 1.0f;
    }

    public static class GOTEntityAINearestAttackableTargetHuorn extends GOTEntityAINearestAttackableTargetBasic {
        public GOTEntityAINearestAttackableTargetHuorn(EntityCreature entity, Class targetClass, int chance, boolean flag) {
            super(entity, targetClass, chance, flag);
        }

        public GOTEntityAINearestAttackableTargetHuorn(EntityCreature entity, Class targetClass, int chance, boolean flag, IEntitySelector selector) {
            super(entity, targetClass, chance, flag, selector);
        }

        @Override
        public boolean shouldExecute() {
            int chance = 400;
            List nearbyHuorns = this.taskOwner.worldObj.getEntitiesWithinAABB(GOTEntityHuornBase.class, this.taskOwner.boundingBox.expand(24.0, 8.0, 24.0));
            for (Object nearbyHuorn : nearbyHuorns) {
                GOTEntityHuornBase huorn = (GOTEntityHuornBase)nearbyHuorn;
                if (huorn.getAttackTarget() == null) {
                    continue;
                }
                chance /= 2;
            }
            if (chance < 20) {
                chance = 20;
            }
            if (this.taskOwner.getRNG().nextInt(chance) != 0)
                return false;
            return super.shouldExecute();
        }
    }
}