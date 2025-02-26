package got.common.entity.other;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class GOTEntityBattleRam extends Entity {
    public GOTEntityBattleRam(World world) {
        super(world);
        setSize(4.0F, 2.5F);
    }

    @Override
    public void entityInit() {
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) source.getEntity();
            if (player.capabilities.isCreativeMode) {
                setDead();
            }
        }

        return true;
    }
}
