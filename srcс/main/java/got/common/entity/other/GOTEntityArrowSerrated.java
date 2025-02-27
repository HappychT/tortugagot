package got.common.entity.other;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;

public class GOTEntityArrowSerrated extends EntityArrow implements IEntityAdditionalSpawnData {
    public GOTEntityArrowSerrated(World world) {
        super(world);
    }

    public GOTEntityArrowSerrated(World world, double d, double d1, double d2) {
        super(world, d, d1, d2);
    }

    public GOTEntityArrowSerrated(World world, EntityLivingBase shooter, EntityLivingBase target, float charge, float inaccuracy) {
        super(world, shooter, target, charge, inaccuracy);
    }

    public GOTEntityArrowSerrated(World world, EntityLivingBase shooter, float charge) {
        super(world, shooter, charge);
    }

    @Override
    public void readSpawnData(ByteBuf data) {
        Entity entity;
        this.motionX = data.readDouble();
        this.motionY = data.readDouble();
        this.motionZ = data.readDouble();
        int id = data.readInt();
        if (id >= 0 && (entity = this.worldObj.getEntityByID(id)) != null) {
            this.shootingEntity = entity;
        }
    }

    @Override
    public void writeSpawnData(ByteBuf data) {
        data.writeDouble(this.motionX);
        data.writeDouble(this.motionY);
        data.writeDouble(this.motionZ);
        data.writeInt(this.shootingEntity == null ? -1 : this.shootingEntity.getEntityId());
    }
}