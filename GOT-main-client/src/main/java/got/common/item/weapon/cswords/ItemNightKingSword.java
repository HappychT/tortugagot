package got.common.item.weapon.cswords;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.database.GOTCreativeTabs;
import got.common.item.weapon.GOTItemGreatsword;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class ItemNightKingSword extends GOTItemGreatsword {

    public ItemNightKingSword(ToolMaterial material) {
        super(material);
        setCreativeTab(GOTCreativeTabs.tabCombat);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);

        target.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 100, 0));

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        super.addInformation(stack, player, list, advanced);
        list.add("");
        list.add(EnumChatFormatting.DARK_AQUA + "Обморожение" + EnumChatFormatting.WHITE + " – замедляет врага.");
        list.add(EnumChatFormatting.DARK_PURPLE + "Клинок ночи" + EnumChatFormatting.WHITE + " – ночью урон +2.");
        list.add("");
        list.add(EnumChatFormatting.GRAY + "Меч, выкованный Иными, сияющий ледяным светом.");
    }
}