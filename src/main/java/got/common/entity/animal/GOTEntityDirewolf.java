package got.common.entity.animal;

import got.GOT;
import got.common.database.GOTRegistry;
import got.common.entity.ai.GOTEntityAIAttackOnCollide;
import got.common.entity.other.GOTMountFunctions;
import got.common.entity.other.GOTNPCMount;
import got.common.item.other.GOTBridleMountStats;
import got.common.world.biome.GOTBiome;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class GOTEntityDirewolf extends EntityAnimal implements GOTBiome.ImmuneToFrost, GOTNPCMount {
	public EntityAIBase attackAI = new GOTEntityAIAttackOnCollide(this, 1.4, false);
	public EntityAIBase panicAI = new EntityAIPanic(this, 1.5);
	public EntityAIBase targetNearAI = new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true);
	public int hostileTick;
	public boolean prevIsChild = true;
	public UUID bridleTamer;
    private int ownerLastAttackerTime;
    private int ownerRevengeTimer;
    private final InventoryBasic mountInventory = new InventoryBasic("DirewolfInv", false, 1);

	public GOTEntityDirewolf(World world) {
		super(world);
		setSize(0.9F, 1.1F);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(2, panicAI);
		tasks.addTask(3, new EntityAIMate(this, 1.0));
		tasks.addTask(4, new EntityAITempt(this, 1.4, Items.fish, false));
		tasks.addTask(5, new EntityAIFollowParent(this, 1.4));
		tasks.addTask(6, new EntityAIWander(this, 1.0));
		tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
		tasks.addTask(8, new EntityAILookIdle(this));
		targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
		targetTasks.addTask(1, targetNearAI);
	}

	@Override
	public void applyEntityAttributes() {
		super.applyEntityAttributes();
        GOTBridleMountStats stats = GOTBridleMountStats.getStats(getClass());
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(stats.hp);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(stats.speed);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(stats.attack);
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		float f = (float) getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
		return entity.attackEntityFrom(DamageSource.causeMobDamage(this), f);
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float f) {
		Entity attacker;
		boolean flag = super.attackEntityFrom(damagesource, f);
		if (flag && (attacker = damagesource.getEntity()) instanceof EntityLivingBase) {
			if (isChild()) {
				double range = 12.0;
				List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(range, range, range));
				for (Object obj : list) {
					GOTEntityDirewolf bear;
					Entity entity = (Entity) obj;
					if (!(entity instanceof GOTEntityDirewolf) || (bear = (GOTEntityDirewolf) entity).isChild()) {
						continue;
					}
					bear.becomeAngryAt((EntityLivingBase) attacker);
				}
			} else {
				becomeAngryAt((EntityLivingBase) attacker);
			}
		}
		return flag;
	}

	public void becomeAngryAt(EntityLivingBase entity) {
		setAttackTarget(entity);
		hostileTick = 200;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable entity) {
		return new GOTEntityDirewolf(worldObj);
	}

	@Override
	public void dropFewItems(boolean flag, int i) {
		int meat = 2 + rand.nextInt(2);
		for (int l = 0; l < meat; ++l) {
			dropItem(GOTRegistry.fur, 1);
		}
	}

	@Override
	public void entityInit() {
		super.entityInit();
		dataWatcher.addObject(20, (byte) 0);
		dataWatcher.addObject(21, (byte) 0);
		dataWatcher.addObject(22, (byte) 0);
	}

	@Override
	public boolean getCanSpawnHere() {
		if (super.getCanSpawnHere()) {
			int i = MathHelper.floor_double(posX);
			int j = MathHelper.floor_double(boundingBox.minY);
			int k = MathHelper.floor_double(posZ);
			if (j > 62 && j < 140 && worldObj.getBlock(i, j - 1, k) == worldObj.getBiomeGenForCoords(i, k).topBlock) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDeathSound() {
		return "got:direwolf.death";
	}

	@Override
	public String getHurtSound() {
		return "got:direwolf.hurt";
	}

	@Override
	public String getLivingSound() {
		return "got:direwolf.say";
	}

	@Override
	public int getTalkInterval() {
		return 200;
	}

	@Override
	public boolean interact(EntityPlayer entityplayer) {
		if (isHostile()) {
			return false;
		}
		if (!worldObj.isRemote && isBridleTamed() && riddenByEntity == null) {
			entityplayer.mountEntity(this);
			setAttackTarget(null);
			getNavigator().clearPathEntity();
			return true;
		}
		return super.interact(entityplayer);
	}

    @Override
    public int getTotalArmorValue() {
        return 5;
    }

	@Override
	public boolean isAIEnabled() {
		return true;
	}

	@Override
	public boolean isBreedingItem(ItemStack itemstack) {
		return itemstack.getItem() == Items.fish;
	}

	public boolean isHostile() {
		return dataWatcher.getWatchableObjectByte(20) == 1;
	}

	public boolean isBridleTamed() {
		return dataWatcher.getWatchableObjectByte(22) == 1;
	}

	@Override
	public boolean getBelongsToNPC() {
		return false;
	}

    public IInventory getMountInventory() {
        return mountInventory;
    }

	@Override
	public String getMountArmorTexture() {
		return null;
	}

	@Override
	public float getStepHeightWhileRiddenByPlayer() {
		return 1.0f;
	}

	@Override
	public boolean isMountArmorValid(ItemStack itemstack) {
		return false;
	}

	@Override
	public boolean isMountSaddled() {
		return dataWatcher.getWatchableObjectByte(21) == 1;
	}

	@Override
	public void moveEntityWithHeading(float strafe, float forward) {
		GOTMountFunctions.move(this, strafe, forward);
	}

	public void riderJump() {
		if (onGround) {
			motionY = 0.42D + GOTBridleMountStats.getStats(getClass()).jump * 0.1D;
			isAirBorne = true;
		}
	}

	@Override
	public void onLivingUpdate() {
		boolean isChild;
		EntityLivingBase entity;
		if (!worldObj.isRemote && (isChild = isChild()) != prevIsChild) {
			if (isChild) {
				tasks.removeTask(attackAI);
				tasks.addTask(2, panicAI);
				targetTasks.removeTask(targetNearAI);
			} else {
				tasks.removeTask(panicAI);
				if (hostileTick > 0) {
					tasks.addTask(1, attackAI);
					targetTasks.addTask(1, targetNearAI);
				} else {
					tasks.removeTask(attackAI);
					targetTasks.removeTask(targetNearAI);
				}
			}
		}
		super.onLivingUpdate();
		if (!worldObj.isRemote && getAttackTarget() != null && (!(entity = getAttackTarget()).isEntityAlive() || entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode)) {
			setAttackTarget(null);
		}
		if (!worldObj.isRemote) {
            setMountSaddled(mountInventory.getStackInSlot(0) != null && mountInventory.getStackInSlot(0).getItem() == Items.saddle);
            updateOwnerCombatTarget();
			if (hostileTick > 0 && getAttackTarget() == null) {
				--hostileTick;
			}
			setHostile(hostileTick > 0);
			if (isHostile()) {
				resetInLove();
			}
		}
		GOTMountFunctions.update(this);
	}

    private void updateOwnerCombatTarget() {
        if (!isBridleTamed() || bridleTamer == null || riddenByEntity != null) {
            return;
        }

        EntityPlayer owner = getBridleTamerPlayer();
        if (owner == null) {
            return;
        }

        EntityLivingBase ownerTarget = owner.getLastAttacker();
        int ownerAttackTime = owner.getLastAttackerTime();
        if (ownerTarget != null && ownerAttackTime != ownerLastAttackerTime && canAttackOwnerTarget(owner, ownerTarget)) {
            becomeAngryAt(ownerTarget);
            ownerLastAttackerTime = ownerAttackTime;
        }

        EntityLivingBase revengeTarget = owner.getAITarget();
        int revengeTime = owner.func_142015_aE();
        if (revengeTarget != null && revengeTime != ownerRevengeTimer && canAttackOwnerTarget(owner, revengeTarget)) {
            becomeAngryAt(revengeTarget);
            ownerRevengeTimer = revengeTime;
        }
    }

    private boolean canAttackOwnerTarget(EntityPlayer owner, EntityLivingBase target) {
        return target != this &&
                target.isEntityAlive() &&
                GOT.canPlayerAttackEntity(owner, target, false) &&
                boundingBox.expand(1.0, 1.0, 1.0).intersectsWith(target.boundingBox);
    }

    private EntityPlayer getBridleTamerPlayer() {
        for (Object obj : worldObj.playerEntities) {
            if (obj instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) obj;
                if (player.getUniqueID().equals(bridleTamer)) {
                    return player;
                }
            }
        }
        return null;
    }

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		hostileTick = nbt.getInteger("Angry");
		setBridleTamed(nbt.getBoolean("BridleTamed"));
        if (nbt.hasKey("MountSaddleItem")) {
            ItemStack saddle = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("MountSaddleItem"));
            mountInventory.setInventorySlotContents(0, saddle);
        } else if (nbt.getBoolean("MountSaddled")) {
            mountInventory.setInventorySlotContents(0, new ItemStack(Items.saddle));
        }
        setMountSaddled(mountInventory.getStackInSlot(0) != null);
		if (nbt.hasKey("BridleTamer")) {
			bridleTamer = UUID.fromString(nbt.getString("BridleTamer"));
		}
	}

	public void setHostile(boolean flag) {
		dataWatcher.updateObject(20, flag ? (byte) 1 : 0);
	}

	public void setMountSaddled(boolean flag) {
		dataWatcher.updateObject(21, flag ? (byte) 1 : 0);
	}

	public void setBridleTamed(boolean flag) {
		dataWatcher.updateObject(22, flag ? (byte) 1 : 0);
	}

	public void setBridleTamedBy(EntityPlayer entityplayer) {
		setBridleTamed(true);
		bridleTamer = entityplayer.getUniqueID();
	}

	@Override
	public void setBelongsToNPC(boolean flag) {
	}

	@Override
	public void super_moveEntityWithHeading(float strafe, float forward) {
		super.moveEntityWithHeading(strafe, forward);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setInteger("Angry", hostileTick);
		nbt.setBoolean("MountSaddled", isMountSaddled());
		nbt.setBoolean("BridleTamed", isBridleTamed());
        ItemStack saddle = mountInventory.getStackInSlot(0);
        if (saddle != null) {
            NBTTagCompound saddleData = new NBTTagCompound();
            saddle.writeToNBT(saddleData);
            nbt.setTag("MountSaddleItem", saddleData);
        }
		if (bridleTamer != null) {
			nbt.setString("BridleTamer", bridleTamer.toString());
		}
	}
}
