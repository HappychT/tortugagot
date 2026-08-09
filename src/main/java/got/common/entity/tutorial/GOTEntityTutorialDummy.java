package got.common.entity.tutorial;
import net.minecraft.world.World;
public class GOTEntityTutorialDummy extends GOTEntityTutorialNPC {

    private int moveTick = 0;
    private int moveDir = 1;

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
        boolean moving = this.getEntityData().getBoolean("TutorialDummyMoving");
        if (moving && !this.worldObj.isRemote) {
            moveTick++;
            // Oscillate: move for 30 ticks in one direction, then reverse
            if (moveTick >= 30) {
                moveTick = 0;
                moveDir = -moveDir;
            }
            this.motionX = moveDir * 0.15D;
            this.motionY = 0;
            this.motionZ = 0;
        } else {
            this.motionX = 0;
            this.motionY = 0;
            this.motionZ = 0;
        }
        this.rotationYawHead = this.rotationYaw;
        this.renderYawOffset = this.rotationYaw;
    }
}
