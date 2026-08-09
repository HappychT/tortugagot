package got.common.entity.tutorial;
import net.minecraft.world.World;
public class GOTEntityTutorialDummy extends GOTEntityTutorialNPC {
    public GOTEntityTutorialDummy(World world) {
        super(world);
        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();
    }
    
    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void collideWithEntity(net.minecraft.entity.Entity entity) {}


    @Override
    public boolean isAIEnabled() {
        return false;
    }

    @Override
    public void knockBack(net.minecraft.entity.Entity p_70653_1_, float p_70653_2_, double p_70653_3_, double p_70653_5_) {
        // No knockback
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        this.rotationYawHead = this.rotationYaw;
        this.renderYawOffset = this.rotationYaw;
    }
}
