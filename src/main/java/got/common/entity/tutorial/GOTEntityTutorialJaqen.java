package got.common.entity.tutorial;
import net.minecraft.world.World;
public class GOTEntityTutorialJaqen extends GOTEntityTutorialNPC {
    public GOTEntityTutorialJaqen(World world) {
        super(world);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.worldObj.isRemote && this.getEntityData().hasKey("TutorialOwner")) {
            String owner = this.getEntityData().getString("TutorialOwner");
            net.minecraft.entity.player.EntityPlayer player = this.worldObj.getPlayerEntityByName(owner);
            if (player != null) {
                this.getLookHelper().setLookPositionWithEntity(player, 30.0F, 30.0F);
            }
        }
    }
    
    @Override
    public boolean attackEntityFrom(net.minecraft.util.DamageSource source, float amount) {
        return false;
    }
    
    @Override
    public void applyEntityCollision(net.minecraft.entity.Entity entity) {
        // Do nothing to prevent being pushed
    }
}
