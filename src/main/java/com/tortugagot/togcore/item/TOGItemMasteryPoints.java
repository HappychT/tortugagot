package com.tortugagot.togcore.item;

import com.tortugagot.togcore.TogCore;
import com.tortugagot.togcore.technology.TOGTechnologyPlayerData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.database.GOTCreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class TOGItemMasteryPoints extends Item {
    private final int points;

    public TOGItemMasteryPoints(String name, int points, String textureName) {
        this.points = points;
        setMaxStackSize(64);
        setCreativeTab(GOTCreativeTabs.tabMisc);
        setUnlocalizedName(TogCore.MODID + ":" + name);
        setTextureName(textureName);
    }

    public int getPoints() {
        return points;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            if (!TOGMasteryItemOwnership.ensureOwnedBy(stack, player)) {
                String ownerName = TOGMasteryItemOwnership.getOwnerName(stack);
                ChatComponentTranslation message = new ChatComponentTranslation("togcore.chat.masteryItemWrongOwner", ownerName != null ? ownerName : "?");
                message.getChatStyle().setColor(EnumChatFormatting.RED);
                player.addChatMessage(message);
                return stack;
            }

            TOGTechnologyPlayerData data = TOGTechnologyPlayerData.get(player);
            if (data == null) {
                TOGTechnologyPlayerData.register(player);
                data = TOGTechnologyPlayerData.get(player);
            }
            if (data != null) {
                stack.stackSize--;
                data.addMasteryPoints(points);
                data.sync();

                ChatComponentTranslation message = new ChatComponentTranslation("togcore.chat.masteryPointsAdded", points, data.getMasteryPoints());
                message.getChatStyle().setColor(EnumChatFormatting.GREEN);
                player.addChatMessage(message);
            }
        }
        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted("item.togcore:mastery_point.desc", points));
        String ownerName = TOGMasteryItemOwnership.getOwnerName(stack);
        if (ownerName != null) {
            list.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocalFormatted("item.togcore:mastery_point.owner", ownerName));
        } else {
            list.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("item.togcore:mastery_point.unbound"));
        }
    }
}
