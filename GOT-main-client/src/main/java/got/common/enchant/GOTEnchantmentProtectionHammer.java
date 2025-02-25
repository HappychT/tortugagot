package got.common.enchant;

import got.common.item.weapon.GOTItemHammer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;

public class GOTEnchantmentProtectionHammer extends GOTEnchantmentProtectionSpecial {

    public GOTEnchantmentProtectionHammer(String s, int level){
        super(s, new GOTEnchantmentType[] {GOTEnchantmentType.ARMOR_BODY,GOTEnchantmentType.ARMOR_LEGS}, level);
    }

    @Override
    public int calcIntProtection() {
        return protectLevel;
    }

    @Override
    public String getDescription(ItemStack itemstack) {
        return StatCollector.translateToLocalFormatted("got.enchant.protectHammer.desc", formatAdditiveInt(calcIntProtection()));
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
        return source.getEntity() instanceof EntityPlayer && ((EntityPlayer) source.getEntity()).getHeldItem() != null &&  (((EntityPlayer) source.getEntity()).getHeldItem().getItem() instanceof GOTItemHammer);
    }
}
