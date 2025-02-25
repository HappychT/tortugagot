package got.common.enchant;

import got.common.item.weapon.GOTItemPolearm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;

public class GOTEnchantmentProtectionPolearm extends GOTEnchantmentProtectionSpecial {

    public GOTEnchantmentProtectionPolearm(String s, int level) {
        super(s, new GOTEnchantmentType[] {GOTEnchantmentType.ARMOR_BODY,GOTEnchantmentType.ARMOR_LEGS}, level);
    }

    @Override
    public int calcIntProtection() {
        return protectLevel;
    }

    @Override
    public String getDescription(ItemStack itemstack) {
        return StatCollector.translateToLocalFormatted("got.enchant.protectPolearm.desc", formatAdditiveInt(calcIntProtection()));
    }

    @Override
    public boolean isCompatibleWithOtherProtection() {
        return true;
    }

    @Override
    public boolean isWeaponProtection() {
        return true;
    }

    @Override
    public boolean protectsAgainst(DamageSource source) {
        return source.getEntity() instanceof EntityPlayer && ((EntityPlayer) source.getEntity()).getHeldItem() != null &&  (((EntityPlayer) source.getEntity()).getHeldItem().getItem() instanceof GOTItemPolearm);
    }
}