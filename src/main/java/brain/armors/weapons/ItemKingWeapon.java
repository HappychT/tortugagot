package brain.armors.weapons;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.EnumHelper;

public class ItemKingWeapon extends ItemSword {
    
    @SideOnly(Side.CLIENT)
    private IIcon itemIcon;
    private String textureName;
    
    public static ToolMaterial VALYRIAN_STEEL = EnumHelper.addToolMaterial(
        "VALYRIAN_STEEL", 
        3, 
        2500, 
        9.0F, 
        2.0F,
        22 
    );
    
    public static ToolMaterial ICE = EnumHelper.addToolMaterial(
        "ICE",
        3,
        1800,
        8.5F,
        2.5F,
        18
    );
    
    public static ToolMaterial PATHKEEPER = EnumHelper.addToolMaterial(
        "PATHKEEPER",
        3,
        2000,
        8.0F,
        2.8F,
        20
    );
    
    public ItemKingWeapon(ToolMaterial material, String unlocalizedName, String textureName) {
        super(material);
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.tabCombat);
        this.textureName = textureName;
        this.setMaxDamage(material.getMaxUses());
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon("armors:" + textureName);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return this.itemIcon;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconIndex(ItemStack stack) {
        return getIconFromDamage(stack.getItemDamage());
    }
    
   
}