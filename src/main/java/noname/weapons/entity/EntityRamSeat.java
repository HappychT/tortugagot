package noname.weapons.entity;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityRamSeat extends Entity implements IEntityAdditionalSpawnData {

    private EntityBatteringRam parent;
    private int seatIndex;
    private float offsetX;
    private float offsetY;
    private float offsetZ;
    private Entity lastPassenger;
    private int parentEntityId = -1;

    public EntityRamSeat(World world) {
        super(world);
        this.noClip = true;
        this.preventEntitySpawning = true;
        this.setSize(0.1F, 0.1F);
    }

    public EntityRamSeat(EntityBatteringRam parent, int seatIndex, float offsetX, float offsetY, float offsetZ) {
        this(parent.worldObj);
        this.parent = parent;
        this.seatIndex = seatIndex;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.parentEntityId = parent != null ? parent.getEntityId() : -1;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if ((parent == null || parent.isDead) && worldObj.isRemote && parentEntityId >= 0) {
            Entity candidate = worldObj.getEntityByID(parentEntityId);
            if (candidate instanceof EntityBatteringRam && !candidate.isDead) {
                parent = (EntityBatteringRam) candidate;
            }
        }

        if (parent == null || parent.isDead) {
            this.setDead();
            return;
        }

        double yawRad = Math.toRadians(parent.rotationYaw);
        double sin = Math.sin(yawRad);
        double cos = Math.cos(yawRad);

        double posX = parent.posX + offsetX * cos - offsetZ * sin;
        double posY = parent.posY + offsetY;
        double posZ = parent.posZ + offsetX * sin + offsetZ * cos;

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.setPosition(posX, posY, posZ);
        this.rotationYaw = parent.rotationYaw;
        this.rotationPitch = parent.rotationPitch;

        if (!this.worldObj.isRemote) {
            Entity currentPassenger = this.riddenByEntity;
            if (currentPassenger != lastPassenger) {
                parent.onSeatOccupantChanged(seatIndex, lastPassenger, currentPassenger);
                lastPassenger = currentPassenger;
            }
        }

        if (this.riddenByEntity != null) {
            this.riddenByEntity.rotationYaw = parent.rotationYaw;
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
    }

    public void setParent(EntityBatteringRam parent) {
        this.parent = parent;
        this.parentEntityId = parent != null ? parent.getEntityId() : -1;
    }

    public void setSeatIndex(int seatIndex) {
        this.seatIndex = seatIndex;
    }

    public void setOffset(float offsetX, float offsetY, float offsetZ) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(parent != null ? parent.getEntityId() : -1);
        buffer.writeInt(seatIndex);
        buffer.writeFloat(offsetX);
        buffer.writeFloat(offsetY);
        buffer.writeFloat(offsetZ);
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        this.parentEntityId = buffer.readInt();
        this.seatIndex = buffer.readInt();
        this.offsetX = buffer.readFloat();
        this.offsetY = buffer.readFloat();
        this.offsetZ = buffer.readFloat();
        if (this.parentEntityId >= 0) {
            Entity entity = this.worldObj.getEntityByID(this.parentEntityId);
            if (entity instanceof EntityBatteringRam) {
                this.parent = (EntityBatteringRam) entity;
            }
        }
    }
}

