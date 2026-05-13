package com.blocknpc.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;


public class EntityBlockNPC extends EntityCreature {

    private static final int DW_BLOCK_ID   = 17;
    private static final int DW_BLOCK_META = 18;

    public EntityBlockNPC(World world) {
        super(world);
        setSize(1.0f, 1.0f);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        tasks.addTask(2, new EntityAIWander(this, 0.5D));
        tasks.addTask(3, new EntityAILookIdle(this));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(DW_BLOCK_ID,   Block.getIdFromBlock(Blocks.stone));
        dataWatcher.addObject(DW_BLOCK_META, 0);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
    }


    public int getBlockId() {
        return dataWatcher.getWatchableObjectInt(DW_BLOCK_ID);
    }

    public int getBlockMeta() {
        return dataWatcher.getWatchableObjectInt(DW_BLOCK_META);
    }

    public void setBlock(Block block, int meta) {
        dataWatcher.updateObject(DW_BLOCK_ID,   Block.getIdFromBlock(block));
        dataWatcher.updateObject(DW_BLOCK_META, meta);
    }

    @Override
    public boolean interact(EntityPlayer player) {
        if (!worldObj.isRemote) {
            ItemStack held = player.getHeldItem();
            if (held != null && held.getItem() instanceof ItemBlock) {
                Block block = Block.getBlockFromItem(held.getItem());
                if (block != null && block != Blocks.air) {
                    setBlock(block, held.getItemDamage());
                    player.addChatMessage(new net.minecraft.util.ChatComponentText(
                        "§aTexture set to: §f" + block.getLocalizedName()
                    ));
                    return true;
                }
            } else if (held == null) {
                Block current = Block.getBlockById(getBlockId());
                player.addChatMessage(new net.minecraft.util.ChatComponentText(
                    "§7Current texture: §f" + (current != null ? current.getLocalizedName() : "unknown")
                ));
                return true;
            }
        }
        return false;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("BlockId",   getBlockId());
        nbt.setInteger("BlockMeta", getBlockMeta());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        dataWatcher.updateObject(DW_BLOCK_ID,   nbt.getInteger("BlockId"));
        dataWatcher.updateObject(DW_BLOCK_META, nbt.getInteger("BlockMeta"));
    }


    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @Override
    public net.minecraft.util.AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public net.minecraft.util.AxisAlignedBB getCollisionBox(Entity entity) {
        return entity.boundingBox;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void collideWithEntity(Entity entity) {
    }

    @Override protected String getLivingSound() { return null; }
    @Override protected String getHurtSound()   { return "mob.zombie.hurt"; }
    @Override protected String getDeathSound()  { return "mob.zombie.death"; }
    @Override protected net.minecraft.item.Item getDropItem() { return null; }
}
