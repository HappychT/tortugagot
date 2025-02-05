package got.common.item.other;

import got.common.database.GOTCreativeTabs;
import got.common.database.GOTEffects;
import got.common.enchant.GOTEnchantment;
import got.common.enchant.GOTEnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class GOTItemArmor extends ItemArmor {
    public String extraName;
    public String path;
    public int slot;
    int counter = 0;

    public GOTItemArmor(ArmorMaterial material, int slotType) {
        this(material, slotType, "");
        this.slot = slotType;
    }

    public GOTItemArmor(ArmorMaterial material, int slotType, String s) {
        super(material, 0, slotType);
        setCreativeTab(GOTCreativeTabs.tabCombat);
        this.extraName = s;
        this.slot = slotType;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer entityplayer, ItemStack itemstack) {
        if (GOTEnchantmentHelper.hasEnchant(itemstack, GOTEnchantment.secondBreathBuff) && !entityplayer.isPotionActive(GOTEffects.secondBreath)) {
            entityplayer.addPotionEffect(new PotionEffect(GOTEffects.secondBreath.id, 200));
        }

        if (GOTEnchantmentHelper.hasEnchant(itemstack, GOTEnchantment.dexterityBuff) && !entityplayer.isPotionActive(GOTEffects.dexterity)) {
            entityplayer.addPotionEffect(new PotionEffect(GOTEffects.dexterity.id, 200));
        }
    }


    public String getArmorName() {
        String suffix;
        String prefix = getArmorMaterial().name().substring("got".length() + 1).toLowerCase();
        suffix = this.armorType == 2 ? "2" : "1";
        if (!StringUtils.isNullOrEmpty(this.extraName)) {
            suffix = this.extraName;
        }
        return prefix + "_" + suffix;
    }

    @Override
    public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String type) {
        this.path = "got:textures/armor/";
        String armorName = getArmorName();
        StringBuilder texture = new StringBuilder(this.path).append(armorName);
        if (type != null) {
            texture.append("_").append(type);
        }
        return texture.append(".png").toString();
    }
}
