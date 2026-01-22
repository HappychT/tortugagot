package brain.armors.weapons;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ItemSpearValyrian extends ItemKingWeapon {
    
    public ItemSpearValyrian() {
        super(VALYRIAN_STEEL, "spear_valyrian", "spear_valyrian");
        this.setTextureName("armors:spear_valyrian");
    }
    
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }
    
    @Override
    public boolean onLeftClickEntity(ItemStack stack, net.minecraft.entity.player.EntityPlayer player, net.minecraft.entity.Entity entity) {
        return super.onLeftClickEntity(stack, player, entity);
    }
}