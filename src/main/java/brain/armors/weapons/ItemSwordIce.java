package brain.armors.weapons;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ItemSwordIce extends ItemKingWeapon {
    
    public ItemSwordIce() {
        super(ICE, "sword_ice", "sword_ice");
        this.setTextureName("armors:sword_ice");
    }
    
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }
}