package got.common.item.weapon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GOTItemCavalryLance extends GOTItemLance {
    public GOTItemCavalryLance(Item.ToolMaterial material) {
        super(material);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer entityplayer, Entity target) {
        if (!entityplayer.isRiding()) {
            entityplayer.addChatMessage(new ChatComponentTranslation("got.lance.warning"));
            return true;
        }
        return super.onLeftClickEntity(stack, entityplayer, target);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
        list.add(StatCollector.translateToLocalFormatted("got.lance.dmginfo"));
    }
}
