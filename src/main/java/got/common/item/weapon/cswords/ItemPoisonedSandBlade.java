package got.common.item.weapon.cswords;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.database.GOTCreativeTabs;
import got.common.database.GOTEffects;
import got.common.item.weapon.GOTItemPolearm;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class ItemPoisonedSandBlade extends GOTItemPolearm {

    public ItemPoisonedSandBlade(ToolMaterial material) {
        super(material);
        setCreativeTab(GOTCreativeTabs.tabStory);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        super.addInformation(stack, player, list, advanced);
        list.add("");
        list.add(EnumChatFormatting.GREEN + "Нейротоксин" + EnumChatFormatting.WHITE + " – снижает регенерацию врага на 3 сек.");
    }
}