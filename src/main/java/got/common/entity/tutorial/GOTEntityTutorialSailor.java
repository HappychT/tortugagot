package got.common.entity.tutorial;
import got.common.entity.ai.GOTEntityAIAttackOnCollide;
import net.minecraft.entity.ai.*;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;

public class GOTEntityTutorialSailor extends GOTEntityTutorialNPC {
    public GOTEntityTutorialSailor(World world) {
        super(world);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new GOTEntityAIAttackOnCollide(this, 1.0D, false));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(3, new EntityAIWatchClosest2(this, EntityPlayer.class, 8.0F, 1.0F));
        this.tasks.addTask(4, new EntityAILookIdle(this));
    }
}
