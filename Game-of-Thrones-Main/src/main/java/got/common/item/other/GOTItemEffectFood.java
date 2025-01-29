package got.common.item.other;


import got.common.database.GOTCreativeTabs;
import got.common.potions.CustomPotion;
import got.common.registers.EffectRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class GOTItemEffectFood extends GOTItemFood {
    public int effectID;
    public int duration;
    public boolean isStew;

    public GOTItemEffectFood(int healAmount, float saturation, boolean canWolfEat, int effectID, int duration, boolean isStew) {
        super(healAmount, saturation, canWolfEat);
        this.effectID = effectID;
        this.duration = duration * 20;
        this.isStew = isStew;
        if (this.isStew) {
            setMaxStackSize(1);
            setContainerItem(Items.bowl);
        }
        setCreativeTab(GOTCreativeTabs.tabFood);
    }

    public GOTItemEffectFood(int healAmount, float saturation, boolean canWolfEat, int effectID, int duration) {
        this(healAmount, saturation, canWolfEat, effectID, duration, false);
    }

    @Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer){
        entityplayer.addPotionEffect(new PotionEffect(effectID, duration));
        if (isStew) {
            super.onEaten(itemstack, world, entityplayer);
            return new ItemStack(Items.bowl);
        }
        return super.onEaten(itemstack, world, entityplayer);
    }
}
