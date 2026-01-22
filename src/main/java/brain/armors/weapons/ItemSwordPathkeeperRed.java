package brain.armors.weapons;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ItemSwordPathkeeperRed extends ItemKingWeapon {
    
    public ItemSwordPathkeeperRed() {
        super(PATHKEEPER, "sword_oathkeeper_red", "sword_oathkeeper_red");
        this.setTextureName("armors:sword_oathkeeper_red");
    }
    
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        target.setFire(5);
        stack.damageItem(1, attacker);
        return true;
    }
}
