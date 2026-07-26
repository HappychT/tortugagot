package got.common.entity.tutorial;

import net.minecraft.world.World;
import net.minecraft.util.DamageSource;

public class GOTEntityTutorialCaptain extends GOTEntityTutorialNPC {
    public GOTEntityTutorialCaptain(World world) {
        super(world);
        this.setSize(0.6F, 1.8F);
        this.tasks.addTask(1, new net.minecraft.entity.ai.EntityAIWatchClosest(this, net.minecraft.entity.player.EntityPlayer.class, 8.0F));
        this.tasks.addTask(2, new net.minecraft.entity.ai.EntityAILookIdle(this));
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.worldObj.isRemote && this.getEntityData().hasKey("TutorialOwner")) {
            String owner = this.getEntityData().getString("TutorialOwner");
            net.minecraft.entity.player.EntityPlayer player = this.worldObj.getPlayerEntityByName(owner);
            if (player != null) {
                this.getLookHelper().setLookPositionWithEntity(player, 30.0F, 30.0F);
                this.rotationYawHead = this.rotationYawHead; // Update head naturally
            }
        }
    }
}
