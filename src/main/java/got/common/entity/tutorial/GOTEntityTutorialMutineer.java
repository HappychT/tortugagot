package got.common.entity.tutorial;
import got.common.entity.ai.GOTEntityAIAttackOnCollide;
import net.minecraft.entity.ai.*;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;

public class GOTEntityTutorialMutineer extends GOTEntityTutorialNPC implements IRangedAttackMob {
    private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);
    private GOTEntityAIAttackOnCollide aiAttackOnCollide = new GOTEntityAIAttackOnCollide(this, 1.0D, false);

    public GOTEntityTutorialMutineer(World world) {
        super(world);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(3, new EntityAIWatchClosest2(this, EntityPlayer.class, 8.0F, 1.0F));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));

        if (world != null && !world.isRemote) {
            this.setCombatTask();
        }
    }

    public void setCombatTask() {
        this.tasks.removeTask(this.aiAttackOnCollide);
        this.tasks.removeTask(this.aiArrowAttack);
        ItemStack itemstack = this.getHeldItem();
        if (itemstack != null && itemstack.getItem() == net.minecraft.init.Items.bow) {
            this.tasks.addTask(1, this.aiArrowAttack);
        } else {
            this.tasks.addTask(1, this.aiAttackOnCollide);
        }
    }

    @Override
    public void setCurrentItemOrArmor(int slot, ItemStack stack) {
        super.setCurrentItemOrArmor(slot, stack);
        if (!this.worldObj.isRemote && slot == 0) {
            this.setCombatTask();
        }
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float damage) {
        EntityArrow entityarrow = new EntityArrow(this.worldObj, this, target, 1.6F, (float)(14 - this.worldObj.difficultySetting.getDifficultyId() * 4));
        int i = net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.enchantment.Enchantment.power.effectId, this.getHeldItem());
        int j = net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.enchantment.Enchantment.punch.effectId, this.getHeldItem());
        entityarrow.setDamage((double)(damage * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.worldObj.difficultySetting.getDifficultyId() * 0.11F));
        if (i > 0) entityarrow.setDamage(entityarrow.getDamage() + (double)i * 0.5D + 0.5D);
        if (j > 0) entityarrow.setKnockbackStrength(j);
        if (net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.enchantment.Enchantment.flame.effectId, this.getHeldItem()) > 0) {
            entityarrow.setFire(100);
        }
        this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.worldObj.spawnEntityInWorld(entityarrow);
    }
}

