package got.common.entity.animal;

import java.util.List;

import got.GOT;
import got.common.entity.ai.GOTEntityAIAttackOnCollide;
import got.common.world.biome.GOTBiome;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class GOTEntityBoar extends GOTEntityHorse implements GOTBiome.ImmuneToFrost {
	private boolean allowRiddenStepUp = true;

	public GOTEntityBoar(World world) {
		super(world);
		setSize(1.325f, 0.8f);
	}

	@Override
	public void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0);
	}

	@Override
	public double clampChildHealth(double health) {
		return MathHelper.clamp_double(health, 10.0, 30.0);
	}

	@Override
	public double clampChildJump(double jump) {
		return MathHelper.clamp_double(jump, 0.3, 1.0);
	}

	@Override
	public double clampChildSpeed(double speed) {
		return MathHelper.clamp_double(speed, 0.08, 0.35);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source == DamageSource.inWall && riddenByEntity instanceof EntityPlayer) {
			return false;
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public EntityAIBase createMountAttackAI() {
		return new GOTEntityAIAttackOnCollide(this, 1.2, true);
	}

	@Override
	public void dropFewItems(boolean flag, int i) {
		int meat = rand.nextInt(3) + 1 + rand.nextInt(1 + i);
		for (int l = 0; l < meat; ++l) {
			if (isBurning()) {
				dropItem(Items.cooked_porkchop, 1);
				continue;
			}
			dropItem(Items.porkchop, 1);
		}
	}

	@Override
	public void func_145780_a(int i, int j, int k, Block block) {
		playSound("mob.pig.step", 0.15f, 1.0f);
	}

	@Override
	public float getStepHeightWhileRiddenByPlayer() {
		return allowRiddenStepUp ? super.getStepHeightWhileRiddenByPlayer() : 0.0f;
	}

	@Override
	public String getAngrySoundName() {
		return "mob.pig.say";
	}

	@Override
	public String getDeathSound() {
		return "mob.pig.death";
	}

	@Override
	public int getHorseType() {
		return 0;
	}

	@Override
	public String getHurtSound() {
		return "mob.pig.say";
	}

	@Override
	public String getLivingSound() {
		return "mob.pig.say";
	}

	@Override
	public void moveEntityWithHeading(float strafe, float forward) {
		boolean prevAllowRiddenStepUp = allowRiddenStepUp;
		allowRiddenStepUp = canStepUpFrontBlock(strafe, forward);
		super.moveEntityWithHeading(strafe, forward);
		allowRiddenStepUp = prevAllowRiddenStepUp;
	}

	@Override
	public void updateRiderPosition() {
		if (riddenByEntity != null) {
			float yawRad = renderYawOffset * 3.1415927f / 180.0f;
			double backOffset = 0.18;
			double riderX = posX + MathHelper.sin(yawRad) * backOffset;
			double riderY = posY + getMountedYOffset() + riddenByEntity.getYOffset();
			double riderZ = posZ - MathHelper.cos(yawRad) * backOffset;
			riddenByEntity.setPosition(riderX, riderY, riderZ);
		}
	}

	@Override
	public boolean isBreedingItem(ItemStack itemstack) {
		return itemstack != null && itemstack.getItem() == Items.carrot;
	}

	@Override
	public boolean isMountHostile() {
		return true;
	}

	@Override
	public void onGOTHorseSpawn() {
		double maxHealth = getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
		maxHealth = Math.min(maxHealth, 25.0);
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxHealth);
		double speed = getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(speed *= 1.0);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!worldObj.isRemote) {
			if (riddenByEntity instanceof EntityLivingBase) {
				EntityLivingBase boarRider = (EntityLivingBase) riddenByEntity;
				float momentum = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
				setSprinting(momentum > 0.16f);
				if (momentum >= 0.18f) {
					float strength = momentum * 8.0f;
					Vec3.createVectorHelper(posX, posY, posZ);
					Vec3 look = getLookVec();
					float sightWidth = 0.8f;
					double range = 0.4;
					List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.contract(0.6, 0.6, 0.6).addCoord(look.xCoord * range, look.yCoord * range, look.zCoord * range).expand(sightWidth, sightWidth, sightWidth));
					boolean hitAnyEntities = false;
					for (Object element : list) {
						EntityLiving entityliving;
						EntityLivingBase entity;
						Entity obj = (Entity) element;
						if (!(obj instanceof EntityLivingBase) || (entity = (EntityLivingBase) obj) == boarRider || boarRider instanceof EntityPlayer && !GOT.canPlayerAttackEntity((EntityPlayer) boarRider, entity, false) || boarRider instanceof EntityCreature && !GOT.canNPCAttackEntity((EntityCreature) boarRider, entity, false) || !entity.attackEntityFrom(DamageSource.causeMobDamage(this), strength)) {
							continue;
						}
						float knockback = strength * 0.045f;
						entity.addVelocity(-MathHelper.sin(rotationYaw * 3.1415927f / 180.0f) * knockback, knockback * 0.5f, MathHelper.cos(rotationYaw * 3.1415927f / 180.0f) * knockback);
						hitAnyEntities = true;
						if (!(entity instanceof EntityLiving) || (entityliving = (EntityLiving) entity).getAttackTarget() != this) {
							continue;
						}
						entityliving.getNavigator().clearPathEntity();
						entityliving.setAttackTarget(boarRider);
					}
					if (hitAnyEntities) {
						worldObj.playSoundAtEntity(this, "mob.pig.say", 1.0f, (rand.nextFloat() - rand.nextFloat()) * 0.2f + 0.8f);
					}
				}
			} else if (getAttackTarget() != null) {
				float momentum = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
				setSprinting(momentum > 0.16f);
			} else {
				setSprinting(false);
			}
		}
	}

	private boolean canStepUpFrontBlock(float strafe, float forward) {
		if (!(riddenByEntity instanceof EntityPlayer) || !isMountSaddled()) {
			return true;
		}

		EntityPlayer rider = (EntityPlayer) riddenByEntity;
		float riderStrafe = rider.moveStrafing * 0.5f;
		float riderForward = rider.moveForward;
		if (riderForward <= 0.0f) {
			riderForward *= 0.25f;
		}
		if (Math.abs(riderForward) < 0.001f && Math.abs(riderStrafe) < 0.001f) {
			return true;
		}

		double yawRadians = Math.toRadians(rotationYaw);
		double moveX = -Math.sin(yawRadians) * riderForward + Math.cos(yawRadians) * riderStrafe;
		double moveZ = Math.cos(yawRadians) * riderForward + Math.sin(yawRadians) * riderStrafe;
		double moveLengthSq = moveX * moveX + moveZ * moveZ;
		if (moveLengthSq < 1.0E-4) {
			return true;
		}

		double moveLength = Math.sqrt(moveLengthSq);
		moveX /= moveLength;
		moveZ /= moveLength;

		double checkDistance = width * 0.5 + 0.35;
		int i = MathHelper.floor_double(posX + moveX * checkDistance);
		int j = MathHelper.floor_double(boundingBox.minY + 0.001);
		int k = MathHelper.floor_double(posZ + moveZ * checkDistance);

		AxisAlignedBB lowerBox = worldObj.getBlock(i, j, k).getCollisionBoundingBoxFromPool(worldObj, i, j, k);
		if (lowerBox == null) {
			return true;
		}

		AxisAlignedBB upperBox = worldObj.getBlock(i, j + 1, k).getCollisionBoundingBoxFromPool(worldObj, i, j + 1, k);
		boolean lowerIsFullBlock = lowerBox.maxY >= j + 0.999;
		return lowerIsFullBlock && upperBox == null;
	}
}
