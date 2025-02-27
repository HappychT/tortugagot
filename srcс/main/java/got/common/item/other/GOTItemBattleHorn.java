package got.common.item.other;

import java.util.List;

import got.common.database.GOTCreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class GOTItemBattleHorn extends Item {
    private PotionEffect effect;
    private double radius;

    public GOTItemBattleHorn(PotionEffect effect) {
        this(effect, 1.5);
    }

    public GOTItemBattleHorn(PotionEffect effect, double radius) {
        setCreativeTab(GOTCreativeTabs.tabMisc);
        setMaxDamage(50);
        this.effect = effect;
        this.radius = radius;
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
            List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class,
                    entityplayer.boundingBox.copy().expand(this.radius, this.radius, this.radius)
                    );
            entityplayer.addPotionEffect(this.effect);

            if(list != null) {
                for(EntityPlayer otherPlayer : list) {
                    otherPlayer.addPotionEffect(this.effect);
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