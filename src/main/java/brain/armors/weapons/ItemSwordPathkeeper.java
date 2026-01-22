package brain.armors.weapons;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ItemSwordPathkeeper extends ItemKingWeapon {
    
    public ItemSwordPathkeeper() {
        super(PATHKEEPER, "sword_oathkeeper", "sword_oathkeeper");
        this.setTextureName("armors:sword_oathkeeper");
    }
    
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {     
        stack.damageItem(1, attacker);
        return true;
    }
}