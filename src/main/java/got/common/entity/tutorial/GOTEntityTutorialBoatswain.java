package got.common.entity.tutorial;

import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.util.DamageSource;

public class GOTEntityTutorialBoatswain extends GOTEntityTutorialNPC {
    public GOTEntityTutorialBoatswain(World world) {
        super(world);
        this.setSize(0.6F, 1.8F);
        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();
        this.tasks.addTask(1, new EntityAIWatchClosest2(this, EntityPlayer.class, 16.0F, 1.0F));
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }
}
