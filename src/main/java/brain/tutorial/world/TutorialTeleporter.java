package brain.tutorial.world;

import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.Entity;

public class TutorialTeleporter extends Teleporter {
    
    public TutorialTeleporter(WorldServer world) {
        super(world);
    }
    
    @Override
    public void placeInPortal(Entity entity, double x, double y, double z, float yaw) {
        entity.setLocationAndAngles(x, y, z, entity.rotationYaw, 0.0F);
        entity.motionX = entity.motionY = entity.motionZ = 0.0D;
    }
    
    @Override
    public boolean placeInExistingPortal(Entity entity, double x, double y, double z, float yaw) {
        return true;
    }
    
    @Override
    public boolean makePortal(Entity entity) {
        return true;
    }
    
    @Override
    public void removeStalePortalLocations(long time) {
        // Empty
    }
}
