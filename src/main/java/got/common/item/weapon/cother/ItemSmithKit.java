package got.common.item.weapon.cother;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.GOT;
import got.common.database.GOTCreativeTabs;
import got.common.itemreg.GOTItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemSmithKit extends Item {

    public ItemSmithKit() {
        super();
        setCreativeTab(GOTCreativeTabs.tabTools);
        this.setMaxStackSize(1);
        this.setMaxDamage(5);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            stack.damageItem(1, player);
            player.openGui(GOT.instance, 53, world, (int) player.posX, (int) player.posY, (int) player.posZ);
        }
        return stack;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        super.addInformation(stack, player, list, advanced);
        list.add(EnumChatFormatting.DARK_PURPLE + "Портативная наковальня" + EnumChatFormatting.WHITE + " – ПКМ для использования.");
    }
}