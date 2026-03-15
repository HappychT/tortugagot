package got.common.item.weapon;

import got.common.database.GOTCreativeTabs;
import got.common.database.GOTEffects;
import got.common.database.GOTMaterial;
import got.common.dispense.GOTDispenseSpear;
import got.common.enchant.GOTEnchantment;
import got.common.enchant.GOTEnchantmentHelper;
import got.common.entity.other.GOTEntitySpear.GOTEntityThiefKnife;
import got.common.handlers.StaminaServerHandler;
import net.minecraft.block.BlockDispenser;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class GOTItemThiefKnife extends GOTItemSpear {

    public GOTItemThiefKnife() {
        super(GOTMaterial.THIEF_KNIFE);
        this.gotWeaponDamage = 5.0f;
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, new GOTDispenseSpear());
        setCreativeTab(GOTCreativeTabs.tabStory);

    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if(entity instanceof EntityLivingBase && player.isSneaking()) {
            ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(GOTEffects.bleeding.id, 3 * 20));
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.bow;
    }

    @Override
    public int getMaxDrawTime() {
        return 10;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack) {
        return 36000;
    }

    @Override
    public float getRangedDamageMultiplier(ItemStack itemstack, Entity shooter, Entity hit) {
        float damage = getGOTWeaponDamage();
        damage = shooter instanceof EntityLivingBase && hit instanceof EntityLivingBase ? (damage += EnchantmentHelper.getEnchantmentModifierLiving((EntityLivingBase) shooter, (EntityLivingBase) hit)) : (damage += EnchantmentHelper.func_152377_a(itemstack, EnumCreatureAttribute.UNDEFINED));
        return damage * 0.7f;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int i) {
        if (entityplayer.getHeldItem() != itemstack)
            return;
        int useTick = getMaxItemUseDuration(itemstack) - i;
        float charge = (float) useTick / (float) getMaxDrawTime();
        if (charge < 0.1f)
            return;
        charge = (charge * charge + charge * 2.0f) / 3.0f;
        charge = Math.min(charge, 1.0f);
        GOTEntityThiefKnife knife = new GOTEntityThiefKnife(world, entityplayer, itemstack.copy(), charge * 2.0f);
        if (charge >= 1.0f) {
            knife.setIsCritical(true);
        }
        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemstack) + GOTEnchantmentHelper.calcFireAspect(itemstack) > 0) {
            knife.setFire(100);
        }
        for (GOTEnchantment ench : GOTEnchantment.allEnchantments) {
            if (!ench.applyToProjectile() || !GOTEnchantmentHelper.hasEnchant(itemstack, ench)) {
                continue;
            }
            GOTEnchantmentHelper.setProjectileEnchantment(knife, ench);
        }
        if (entityplayer.capabilities.isCreativeMode) {
            knife.canBePickedUp = 2;
        }
        world.playSoundAtEntity(entityplayer, "random.bow", 1.0f, 1.0f / (itemRand.nextFloat() * 0.4f + 1.2f) + charge * 0.5f);
        if (!world.isRemote) {
            world.spawnEntityInWorld(knife);
            StaminaServerHandler.drainStaminaByPercent(2, entityplayer);
        }
        if (!entityplayer.capabilities.isCreativeMode) {
            itemstack.damageItem(itemstack.getMaxDamage() / 8, entityplayer);
        }
    }
}