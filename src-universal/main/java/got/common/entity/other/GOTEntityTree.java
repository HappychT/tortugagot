package got.common.entity.other;

import got.common.database.GOTRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public abstract class GOTEntityTree extends GOTEntityNPC {
    public static Block[] WOOD_BLOCKS = new Block[]{Blocks.log, GOTRegistry.wood2, Blocks.log};
    public static Block[] LEAF_BLOCKS = new Block[]{Blocks.leaves, GOTRegistry.leaves2, Blocks.leaves};
    public static Block[] SAPLING_BLOCKS = new Block[]{Blocks.sapling, GOTRegistry.sapling2, Blocks.sapling};
    public static int[] WOOD_META = new int[]{0, 1, 2, 3};
    public static int[] LEAF_META = new int[]{0, 1, 2, 3};
    public static int[] SAPLING_META = new int[]{0, 1, 2, 3};
    public static String[] TYPES = new String[]{"oak", "beech", "birch", "alder"};

    public GOTEntityTree(World world) {
        super(world);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (byte)0);
        if (this.rand.nextInt(6) == 0) {
            setTreeType(3);
        } else if (this.rand.nextInt(9) == 0) {
            setTreeType(2);
        } else if (this.rand.nextInt(3) == 0) {
            setTreeType(1);
        } else {
            setTreeType(0);
        }
    }

    public int getTreeType() {
        byte i = this.dataWatcher.getWatchableObjectByte(16);
        if (i < 0 || i >= TYPES.length) {
            i = 0;
        }
        return i;
    }

    public void setTreeType(int i) {
        this.dataWatcher.updateObject(16, (byte)i);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setByte("EntType", (byte)getTreeType());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        setTreeType(nbt.getByte("EntType"));
    }

    @Override
    public void setAttackTarget(EntityLivingBase target, boolean speak) {
        if (target instanceof GOTEntityTree)
            return;
        super.setAttackTarget(target, speak);
    }

    @Override
    public void knockBack(Entity entity, float f, double d, double d1) {
        super.knockBack(entity, f, d, d1);
        this.motionX /= 2.0;
        this.motionY /= 2.0;
        this.motionZ /= 2.0;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (doTreeDamageCalculation() && !isTreeEffectiveDamage(damagesource)) {
            f /= 3.0f;
        }
        return super.attackEntityFrom(damagesource, f);
    }

    protected boolean doTreeDamageCalculation() {
        return true;
    }

    protected final boolean isTreeEffectiveDamage(DamageSource damagesource) {
        ItemStack itemstack;
        if (damagesource.isFireDamage())
            return true;
        return damagesource.getEntity() instanceof EntityLivingBase && damagesource.getSourceOfDamage() == damagesource.getEntity() && (itemstack = ((EntityLivingBase)damagesource.getEntity()).getHeldItem()) != null && ForgeHooks.canToolHarvestBlock(Blocks.log, 0, itemstack);
    }

    @Override
    public void addPotionEffect(PotionEffect effect) {
        if (effect.getPotionID() == Potion.poison.id)
            return;
        super.addPotionEffect(effect);
    }

    @Override
    public void dropFewItems(boolean flag, int i) {
        super.dropFewItems(flag, i);
        int logs = MathHelper.getRandomIntegerInRange(this.rand, 3, 10) + this.rand.nextInt(4 * (i + 1));
        for (int l = 0; l < logs; ++l) {
            int treeType = getTreeType();
            entityDropItem(new ItemStack(WOOD_BLOCKS[treeType], 1, WOOD_META[treeType]), 0.0f);
        }
        int sticks = MathHelper.getRandomIntegerInRange(this.rand, 6, 16) + this.rand.nextInt(5 * (i + 1));
        for (int l = 0; l < sticks; ++l) {
            dropItem(Items.stick, 1);
        }
    }

    @Override
    public boolean canDropRares() {
        return false;
    }

    @Override
    public boolean getCanSpawnHere() {
        if (super.getCanSpawnHere()) {
            if (this.liftSpawnRestrictions)
                return true;
            int i = MathHelper.floor_double(this.posX);
            int j = MathHelper.floor_double(this.boundingBox.minY);
            int k = MathHelper.floor_double(this.posZ);
            Block block = this.worldObj.getBlock(i, j - 1, k);
            //            int meta = this.worldObj.getBlockMetadata(i, j - 1, k);
            return j > 62 && (block == Blocks.grass || block == Blocks.dirt);
        }
        return false;
    }

    @Override
    public float getBlockPathWeight(int i, int j, int k) {
        return 0.0f;
    }

    @Override
    public boolean canReEquipHired(int slot, ItemStack itemstack) {
        return false;
    }
}