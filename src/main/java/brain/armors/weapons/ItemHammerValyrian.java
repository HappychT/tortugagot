package brain.armors.weapons;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ItemHammerValyrian extends ItemKingWeapon {
    
    public ItemHammerValyrian() {
        super(VALYRIAN_STEEL, "hammer_valyrian", "hammer_valyrian");
        this.setTextureName("armors:hammer_valyrian");
    }
    
    @Override
    public float func_150931_i() { 
        return super.func_150931_i() + 2.0F;
    }
    
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
      
        stack.damageItem(1, attacker);
        return true;
    }
}