package got.common.entity.tutorial;

import got.common.entity.ai.GOTEntityAIAttackOnCollide;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GOTEntityTutorialDrunkard extends GOTEntityTutorialNPC {

    public GOTEntityTutorialDrunkard(World world) {
        super(world);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new GOTEntityAIAttackOnCollide(this, 1.0D, false));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(3, new EntityAIWatchClosest2(this, EntityPlayer.class, 8.0F, 1.0F));
        this.tasks.addTask(4, new EntityAILookIdle(this));

        // EntityAIHurtByTarget — чтобы отвечать на удар игрока
        // Защита от чужих игроков обеспечивается переопределением setAttackTarget/attackEntityAsMob
        // в базовом классе GOTEntityTutorialNPC — лишняя кастомизация не нужна.
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        // 5 HP (2.5 сердца)
        this.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.maxHealth).setBaseValue(5.0D);
    }
}

