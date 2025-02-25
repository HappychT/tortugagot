package got.common.entity.other;

import java.util.Random;

import got.common.entity.ai.GOTEntityAIAttackOnCollide;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class GOTEntityEnt extends GOTEntityTree {
    private Random branchRand = new Random();
    public int eyesClosed;

    public GOTEntityEnt(World world) {
        super(world);
        setSize(1.4f, 4.6f);
        getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new GOTEntityAIAttackOnCollide(this, 2.0, false));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0));
        this.tasks.addTask(3, new EntityAIWatchClosest2(this, EntityPlayer.class, 12.0f, 0.02f));
        this.tasks.addTask(3, new EntityAIWatchClosest2(this, GOTEntityNPC.class, 8.0f, 0.02f));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityLiving.class, 10.0f, 0.02f));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.addTargetTasks(true);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(18, (byte)0);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0);
        getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(24.0);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2);
        getEntityAttribute(npcAttackDamage).setBaseValue(7.0);
    }

    public int getExtraHeadBranches() {
        long l = getUniqueID().getLeastSignificantBits();
        l = l * 365620672396L ^ l * 12784892284L ^ l;
        l = l * l * 18569660L + l * 6639092L;
        this.branchRand.setSeed(l);
        if (this.branchRand.nextBoolean())
            return 0;
        return MathHelper.getRandomIntegerInRange(this.branchRand, 2, 5);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.worldObj.isRemote) {
            if (this.eyesClosed > 0) {
                --this.eyesClosed;
            } else if (this.rand.nextInt(400) == 0) {
                this.eyesClosed = 30;
            }
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if (super.attackEntityAsMob(entity)) {
            float knockbackModifier = 1.5f;
            entity.addVelocity(-MathHelper.sin(this.rotationYaw * 3.1415927f / 180.0f) * knockbackModifier * 0.5f, 0.15, MathHelper.cos(this.rotationYaw * 3.1415927f / 180.0f) * knockbackModifier * 0.5f);
            return true;
        }
        return false;
    }

    @Override
    public int getExperiencePoints(EntityPlayer entityplayer) {
        return 5 + this.rand.nextInt(6);
    }

    @Override
    protected float getSoundVolume() {
        return 1.5f;
    }
}