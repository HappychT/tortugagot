package got.common.entity.potion;

import java.util.List;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import got.common.item.potions.GOTItemSmoke;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class GOTEntitySmokeEffect extends Entity implements IEntityAdditionalSpawnData {
    public static int TICKS_DATA_WATCHER = 10;
    public static int WIDTH_DATA_WATCHER = 11;
    public static int HEIGHT_DATA_WATCHER = 12;
    public EntityLivingBase thrower;
    public ItemStack stack;
    public int MAX_TICKS = 30 * 20;

    public GOTEntitySmokeEffect(World world) {
        super(world);
        this.yOffset = 0;
        setSize(1, 1);
    }

    public GOTEntitySmokeEffect(World world, GOTEntitySmoke potion) {
        this(world, potion.getStack(), potion.getThrower());
        setPosition(potion.posX, potion.posY, potion.posZ);
    }

    public GOTEntitySmokeEffect(World world, ItemStack stack, EntityLivingBase thrower) {
        this(world);
        this.stack = stack;
        this.thrower = thrower;
    }

    @Override
    public void addVelocity(double x, double y, double z) {
    }

    @Override
    public void applyEntityCollision(Entity e) {
        if (!(e instanceof EntityLivingBase))
            return;
        EntityLivingBase entity = (EntityLivingBase) e;
        List<PotionEffect> effects = ((GOTItemSmoke)this.stack.getItem()).getEffects(this.stack);
        boolean addedEffect = false;
        for (PotionEffect effect : effects) {
            int effectID = effect.getPotionID();
            if (!entity.isPotionActive(effectID)) {
                entity.addPotionEffect(effect);
                addedEffect = true;
            }
        }
        if (addedEffect) {
            this.dataWatcher.getWatchableObjectInt(TICKS_DATA_WATCHER);
            this.dataWatcher.updateObject(TICKS_DATA_WATCHER, 0);
        }
    }

    @Override
    public boolean canBePushed() {
        return true;
    }

    @Override
    public void entityInit() {
        this.dataWatcher.addObject(TICKS_DATA_WATCHER, 0);
        this.dataWatcher.addObject(WIDTH_DATA_WATCHER, 6.0F);
        this.dataWatcher.addObject(HEIGHT_DATA_WATCHER, 0.5F);
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public EntityLivingBase getThrower() {
        return this.thrower;
    }

    @Override
    public void moveEntity(double x, double y, double z) {
    }

    @Override
    public void onUpdate() {
        int ticks = this.dataWatcher.getWatchableObjectInt(TICKS_DATA_WATCHER);
        if (this.worldObj.isRemote) {
            float w = this.dataWatcher.getWatchableObjectFloat(WIDTH_DATA_WATCHER);
            if (w != this.width) {
                this.width = w;
            }
            float h = this.dataWatcher.getWatchableObjectFloat(HEIGHT_DATA_WATCHER);
            if (h != this.height) {
                this.height = h;
            }
            if (this.ticksExisted % 5 == 0) {
                double radius = 3 * ((double) (this.MAX_TICKS - ticks) / this.MAX_TICKS);
                int colour = this.stack.getItem().getColorFromItemStack(this.stack, 0);
                if(this.stack.getItem() instanceof GOTItemSmoke) {
                    colour =
                            Potion.potionTypes[((GOTItemSmoke)this.stack.getItem()).getEffects(this.stack).get(0).getPotionID()]
                                    .getLiquidColor();
                }
                float red = (colour >> 16 & 255) / 255.0F;
                float green = (colour >> 8 & 255) / 255.0F;
                float blue = (colour >> 0 & 255) / 255.0F;
                for (int i = 0; i < 30; i++) {
                    float variation = 0.75F + this.rand.nextFloat() * 0.25F;
                    this.worldObj.spawnParticle("mobSpell", this.posX - radius + this.rand.nextFloat() * radius * 2, this.posY, this.posZ - radius + this.rand.nextFloat() * radius * 2, red * variation, green * variation, blue * variation);
                }
            }
            return;
        }
        ticks++;
        setTickCount(ticks);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        setTickCount(nbt.getInteger("Ticks"));
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
    public void setSize(float width, float height) {
        super.setSize(width, height);
        this.dataWatcher.updateObject(WIDTH_DATA_WATCHER, this.width);
        this.dataWatcher.updateObject(HEIGHT_DATA_WATCHER, this.height);
    }

    public boolean setTickCount(int ticks) {
        this.dataWatcher.updateObject(TICKS_DATA_WATCHER, ticks);
        if (ticks >= this.MAX_TICKS) {
            setDead();
            return true;
        }
        double radius = 3 * ((double) (this.MAX_TICKS - ticks) / this.MAX_TICKS);
        setSize((float) radius * 2, 0.5F);
        return false;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setInteger("Ticks", this.dataWatcher.getWatchableObjectInt(TICKS_DATA_WATCHER));
        if (this.stack != null) {
            nbt.setTag("Potion", this.stack.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        ByteBufUtils.writeItemStack(buffer, this.stack);
    }
}