package got.common.item.weapon.cswords;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.database.GOTCreativeTabs;
import got.common.handlers.PlayerParryData;
import got.common.item.weapon.GOTItemSword;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemSyrioForelSword extends GOTItemSword {

    public ItemSyrioForelSword(ToolMaterial material) {
        super(material);
        setCreativeTab(GOTCreativeTabs.tabStory);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.block;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        PlayerParryData parryData = PlayerParryData.get(player);
        parryData.setBlockStartTime(world.getTotalWorldTime());
        parryData.setTryingToBlock(true);

        player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        return stack;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int itemInUseCount) {
        PlayerParryData parryData = PlayerParryData.get(player);
        parryData.setBlockStartTime(0);
        parryData.setTryingToBlock(false);
    }
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        super.addInformation(stack, player, list, advanced);
        list.add("");
        list.add(EnumChatFormatting.AQUA + "Контратака" + EnumChatFormatting.WHITE + " – идеальный блок наносит 2 урона.");
        list.add(EnumChatFormatting.DARK_AQUA + "Поглощение стамины" + EnumChatFormatting.WHITE + " – ворует 3% при пробитии блока.");
        list.add(EnumChatFormatting.YELLOW + "Мастерство фехтования" + EnumChatFormatting.WHITE + " – +5% скорости после контратаки.");
        list.add("");
        list.add(EnumChatFormatting.GRAY + "Легкий клинок для мастеров дуэлей.");
    }
}