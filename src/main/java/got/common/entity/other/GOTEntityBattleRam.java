package got.common.entity.other;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class GOTEntityBattleRam extends EntityCreature {
    public GOTEntityBattleRam(World world) {
        super(world);
        setSize(4.0F, 3.4F);
    }

    @Override
    public void entityInit() {
        super.entityInit();
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean canBePushed() {
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

        return false;
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }
}
