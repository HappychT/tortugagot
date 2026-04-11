package got.common.item.weapon.cswords;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.database.GOTCreativeTabs;
import got.common.item.weapon.GOTItemBattleaxe;
import got.common.item.weapon.GOTItemGreatsword;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class ItemBattleaxeWestros extends GOTItemBattleaxe {

    public ItemBattleaxeWestros(ToolMaterial material) {
        super(material);
        setCreativeTab(GOTCreativeTabs.tabCombat);
    }



    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        super.addInformation(stack, player, list, advanced);
    }
}