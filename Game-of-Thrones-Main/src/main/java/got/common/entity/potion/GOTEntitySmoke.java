package got.common.entity.potion;

import cpw.mods.fml.common.network.ByteBufUtils;
import got.GOT;
import got.common.item.potions.GOTItemSmoke;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class GOTEntitySmoke extends GOTEntityLingeringPotion {
    public ItemStack stack;

    public GOTEntitySmoke(World world) {
        super(world);
    }

    public GOTEntitySmoke(World world, double x, double y, double z, ItemStack stack) {
        super(world, x, y, z, stack);
        this.stack = stack;
    }

    public GOTEntitySmoke(World world, EntityLivingBase thrower, ItemStack stack) {
        super(world, thrower, stack);
        this.stack = stack;
    }

    @Override
    public ItemStack getStack() {
        return this.stack;
    }

    @Override
    public void onImpact(MovingObjectPosition mop) {
        if (this.worldObj.isRemote)
            return;
        this.worldObj.spawnEntityInWorld(new GOTEntitySmokeEffect(this.worldObj, this));
        if(this.stack.getItem() instanceof GOTItemSmoke) {
            GOT.proxy.addFXPotion(this.worldObj, this.stack, 0, (int)Math.round(this.posX), (int)Math.round(this.posY), (int)Math.round(this.posZ),
                    Potion.potionTypes[((GOTItemSmoke)this.stack.getItem()).getEffects(this.stack).get(0).getPotionID()].getLiquidColor());
        }
        setDead();
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.stack = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Potion"));
        if (this.stack == null) {
            setDead();
        }
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        this.stack = ByteBufUtils.readItemStack(buffer);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        if (this.stack != null) {
            nbt.setTag("Potion", this.stack.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        ByteBufUtils.writeItemStack(buffer, this.stack);
    }
}
