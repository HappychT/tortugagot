package got.common.item.weapon;

import got.common.database.GOTCreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GOTItemLeaderBattleaxe extends GOTItemBattleaxe {

    public GOTItemLeaderBattleaxe(ToolMaterial material) {
        super(material);
        setCreativeTab(GOTCreativeTabs.tabStory);
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase hitEntity, EntityLivingBase user) {
        if (hitEntity instanceof EntityPlayer) {
            ((EntityPlayer) hitEntity).inventory.damageArmor(2.0F);
        }
        return true;
    }

}
