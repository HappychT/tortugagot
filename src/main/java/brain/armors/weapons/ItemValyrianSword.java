package brain.armors.weapons;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ItemValyrianSword extends ItemKingWeapon {
    
    public ItemValyrianSword() {
        super(VALYRIAN_STEEL, "valyrian_sword", "valyrian_sword");
        this.setTextureName("armors:valyrian_sword");
    }
    
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }
}