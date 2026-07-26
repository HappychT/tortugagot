package got.common.item.weapon;

import java.util.List;

import got.common.database.GOTCreativeTabs;
import got.common.database.GOTEffects;
import got.common.dispense.GOTDispenseThrowingKnife;
import got.common.enchant.GOTEnchantment;
import got.common.enchant.GOTEnchantmentHelper;
import got.common.entity.other.GOTEntityThrowingKnife;
import got.common.item.GOTMaterialFinder;
import got.common.recipe.GOTRecipe;
import net.minecraft.block.BlockDispenser;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class GOTItemThrowingKnife extends Item implements GOTMaterialFinder {
    public Item.ToolMaterial knifeMaterial;
    private int effectType;
    public static int timer = 100;

    public GOTItemThrowingKnife(Item.ToolMaterial material, int effectType) {
        this.knifeMaterial = material;
        setMaxStackSize(1);
        setMaxDamage(material.getMaxUses());
        setFull3D();
        setCreativeTab(GOTCreativeTabs.tabCombat);
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, new GOTDispenseThrowingKnife());
        this.effectType = effectType;
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
        PotionEffect potion = this.effectType == 0 ? new PotionEffect(Potion.poison.id, 5 * 20) : new PotionEffect(GOTEffects.bleeding.id, 3 * 20);
        String s1 = StatCollector.translateToLocal(potion.getEffectName()).trim();
        s1 = s1 + " (" + Potion.getDurationString(potion) + ")";
        list.add(EnumChatFormatting.RED + s1);
    }

    @Override
    public boolean getIsRepairable(ItemStack itemstack, ItemStack repairItem) {
        if (GOTRecipe.checkItemEquals(this.knifeMaterial.getRepairItemStack(), repairItem))
            return true;
     return super.getIsRepairable(itemstack, repairItem);
    }

    @Override
    public ToolMaterial getMaterial() {
        return this.knifeMaterial;
    }

    public float getRangedDamageMultiplier(ItemStack itemstack, Entity shooter, Entity hit) {
        float damage = this.knifeMaterial.getDamageVsEntity() + 4.0f;
        damage = shooter instanceof EntityLivingBase && hit instanceof EntityLivingBase ? (damage += EnchantmentHelper.getEnchantmentModifierLiving((EntityLivingBase) shooter, (EntityLivingBase) hit)) : (damage += EnchantmentHelper.func_152377_a(itemstack, EnumCreatureAttribute.UNDEFINED));
        return damage * 0.5f;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (!itemstack.hasTagCompound()) {
            itemstack.stackTagCompound = new NBTTagCompound();
            itemstack.stackTagCompound.setInteger("timer", 0);
        }
        if (itemstack.stackTagCompound.getInteger("timer") == 0) {
            GOTEntityThrowingKnife knife = new GOTEntityThrowingKnife(world, entityplayer, itemstack.copy(), 2.0f);
            knife.setIsCritical(true);
            knife.effectType = this.effectType;
            int fireAspect = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemstack) + GOTEnchantmentHelper.calcFireAspect(itemstack);
            if (fireAspect > 0) {
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
            } else {
                knife.canBePickedUp = 0;
            }
            world.playSoundAtEntity(entityplayer, "random.bow", 1.0f, 1.0f / (itemRand.nextFloat() * 0.4f + 1.2f) + 0.25f);
            if (!world.isRemote) {
                world.spawnEntityInWorld(knife);
            }
            if (!entityplayer.capabilities.isCreativeMode) {
                itemstack.damageItem(10, entityplayer);
            }
            itemstack.stackTagCompound.setInteger("timer", 100);
        } else {
            if(!world.isRemote) {
                entityplayer.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("got.jewelry.desc.cooldown")
                        + ' ' + ticksToElapsedTime(itemstack.stackTagCompound.getInteger("timer"))));
            }
        }
        return itemstack;
    }

    protected static String ticksToElapsedTime(int ticks) {
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        seconds %= 60;
        return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int meta, boolean b) {
        if (!stack.hasTagCompound()) {
            stack.stackTagCompound = new NBTTagCompound();
            stack.stackTagCompound.setInteger("timer", 0);
        }
        if (stack.stackTagCompound.getInteger("timer") > 0) {
            stack.stackTagCompound.setInteger("timer", stack.stackTagCompound.getInteger("timer") - 1);
        }
    }
}
