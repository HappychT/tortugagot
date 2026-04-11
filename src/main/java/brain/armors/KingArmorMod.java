package brain.armors;

import brain.armors.mantle.ItemPlashArmor;
import brain.armors.weapons.ItemBattleaxeValyrian;
import brain.armors.weapons.ItemHammerValyrian;
import brain.armors.weapons.ItemSpearValyrian;
import brain.armors.weapons.ItemSwordIce;
import brain.armors.weapons.ItemSwordPathkeeper;
import brain.armors.weapons.ItemValyrianSword;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

public class KingArmorMod {
    
    public static ItemArmor.ArmorMaterial KING_GUARD_MATERIAL;
    public static ItemArmor.ArmorMaterial PLASH_MATERIAL;
    
    public static Item kingGuardHelmet;
    public static Item kingGuardChestplate;
    public static Item kingGuardLeggings;
    public static Item kingGuardBoots;
    
    public static Item plash1;
    public static Item plash2;
    public static Item plash3;
    public static Item plash4;
    
    public static Item valyrianSword;
    public static Item swordIce;
    public static Item swordPathkeeper;
    public static Item battleaxeValyrian;
    public static Item hammerValyrian;
    public static Item spearValyrian;
    
    public static void preInit(FMLPreInitializationEvent event) {
        KING_GUARD_MATERIAL = EnumHelper.addArmorMaterial(
            "armors:king_guard", 
            35, 
            new int[]{3, 8, 6, 3}, 
            25
        );
        
        PLASH_MATERIAL = EnumHelper.addArmorMaterial(
            "armors:plash", 
            10,
            new int[]{0, 1, 0, 0}, 
            10 
        );
        
        kingGuardHelmet = new ItemKingGuardArmor(KING_GUARD_MATERIAL, 0, 0)
            .setUnlocalizedName("king_guard_helmet")
            .setTextureName("armors:king_guard/king_guard_helmet")
            .setCreativeTab(CreativeTabs.tabCombat);
        
        kingGuardChestplate = new ItemKingGuardArmor(KING_GUARD_MATERIAL, 0, 1)
            .setUnlocalizedName("king_guard_body")
            .setTextureName("armors:king_guard/king_guard_body")
            .setCreativeTab(CreativeTabs.tabCombat);
        
        kingGuardLeggings = new ItemKingGuardArmor(KING_GUARD_MATERIAL, 0, 2)
            .setUnlocalizedName("king_guard_legs")
            .setTextureName("armors:king_guard/king_guard_legs")
            .setCreativeTab(CreativeTabs.tabCombat);
        
        kingGuardBoots = new ItemKingGuardArmor(KING_GUARD_MATERIAL, 0, 3)
            .setUnlocalizedName("king_guard_boots")
            .setTextureName("armors:king_guard/king_guard_boots")
            .setCreativeTab(CreativeTabs.tabCombat);

        GameRegistry.registerItem(kingGuardHelmet, "king_guard_helmet");
        GameRegistry.registerItem(kingGuardChestplate, "king_guard_body");
        GameRegistry.registerItem(kingGuardLeggings, "king_guard_legs");
        GameRegistry.registerItem(kingGuardBoots, "king_guard_boots");

        plash1 = new ItemPlashArmor(PLASH_MATERIAL, 0, 0, 0)
            .setUnlocalizedName("plash1")
            .setCreativeTab(CreativeTabs.tabCombat);
        
        plash2 = new ItemPlashArmor(PLASH_MATERIAL, 0, 0, 1)
            .setUnlocalizedName("plash2")
            .setCreativeTab(CreativeTabs.tabCombat);
        
        plash3 = new ItemPlashArmor(PLASH_MATERIAL, 0, 0, 2)
            .setUnlocalizedName("plash3")
            .setCreativeTab(CreativeTabs.tabCombat);
        
        plash4 = new ItemPlashArmor(PLASH_MATERIAL, 0, 0, 3)
            .setUnlocalizedName("plash4")
            .setCreativeTab(CreativeTabs.tabCombat);
        

        GameRegistry.registerItem(plash1, "plash1");
        GameRegistry.registerItem(plash2, "plash2");
        GameRegistry.registerItem(plash3, "plash3");
        GameRegistry.registerItem(plash4, "plash4");
        

        valyrianSword = new ItemValyrianSword()
            .setUnlocalizedName("valyrian_sword")
            .setCreativeTab(CreativeTabs.tabCombat);
        
        swordIce = new ItemSwordIce()
            .setUnlocalizedName("sword_ice")
            .setCreativeTab(CreativeTabs.tabCombat);
        
        swordPathkeeper = new ItemSwordPathkeeper()
            .setUnlocalizedName("sword_pathkeeper")
            .setCreativeTab(CreativeTabs.tabCombat);
        
        battleaxeValyrian = new ItemBattleaxeValyrian()
            .setUnlocalizedName("battleaxe_valyrian")
            .setCreativeTab(CreativeTabs.tabCombat);
        
        hammerValyrian = new ItemHammerValyrian()
            .setUnlocalizedName("hammer_valyrian")
            .setCreativeTab(CreativeTabs.tabCombat);
        
        spearValyrian = new ItemSpearValyrian()
            .setUnlocalizedName("spear_valyrian")
            .setCreativeTab(CreativeTabs.tabCombat);
        
        GameRegistry.registerItem(valyrianSword, "valyrian_sword");
        GameRegistry.registerItem(swordIce, "sword_ice");
        GameRegistry.registerItem(swordPathkeeper, "sword_pathkeeper");
        GameRegistry.registerItem(battleaxeValyrian, "battleaxe_valyrian");
        GameRegistry.registerItem(hammerValyrian, "hammer_valyrian");
        GameRegistry.registerItem(spearValyrian, "spear_valyrian");

    }
    
    public static void init(FMLInitializationEvent event) {
    }

    @SideOnly(Side.CLIENT)
    private static void registerRenderers() {
    }
}
