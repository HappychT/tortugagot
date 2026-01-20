package brain.armors.weapons;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemAxe;

public class ItemBattleaxeValyrian extends ItemAxe {
    
    @SideOnly(Side.CLIENT)
    private net.minecraft.util.IIcon itemIcon;
    
    public ItemBattleaxeValyrian() {
        super(ItemKingWeapon.VALYRIAN_STEEL);
        this.setUnlocalizedName("battleaxe_valyrian");
        this.setCreativeTab(net.minecraft.creativetab.CreativeTabs.tabCombat);
        this.setTextureName("armors:battleaxe_valyrian");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(net.minecraft.client.renderer.texture.IIconRegister register) {
        this.itemIcon = register.registerIcon("armors:battleaxe_valyrian");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public net.minecraft.util.IIcon getIconFromDamage(int damage) {
        return this.itemIcon;
    }
}