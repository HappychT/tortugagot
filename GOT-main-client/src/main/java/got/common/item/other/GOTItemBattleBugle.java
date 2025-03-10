package got.common.item.other;

import java.util.List;

import got.common.GOTLevelData;
import got.common.database.GOTCreativeTabs;
import got.common.database.GOTEffects;
import got.common.faction.GOTFaction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class GOTItemBattleBugle extends Item {
    private static PotionEffect effect = new PotionEffect(GOTEffects.combatSpirit.id, 2 * 60 * 20);

    public GOTItemBattleBugle() {
        setCreativeTab(GOTCreativeTabs.tabMisc);
        setMaxDamage(100);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.bow;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack) {
        return 40;
    }

    @Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if(!world.isRemote) {
            GOTFaction fac = GOTLevelData.getData(entityplayer).getPledgeFaction();
            entityplayer.addPotionEffect(new PotionEffect(effect));

            if(fac != null) {
                List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class, entityplayer.boundingBox.copy().expand(2.5, 2.5, 2.5));

                if(list != null) {
                    for(EntityPlayer otherPlayer : list) {
                        GOTFaction playerFac = GOTLevelData.getData(otherPlayer).getPledgeFaction();
                        if(playerFac == fac || playerFac.isAlly(fac)) {
                            otherPlayer.addPotionEffect(new PotionEffect(effect));
                        } else {
                            otherPlayer.removePotionEffect(GOTEffects.combatSpirit.id);
                        }
                    }
                }
            }

            if(!entityplayer.capabilities.isCreativeMode)
            {
                itemstack.damageItem(1, entityplayer);
            }
        }
        return itemstack;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        return itemstack;
    }
}