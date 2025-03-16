package got.common.item.other;

import java.util.ArrayList;
import java.util.List;

import got.common.database.GOTCreativeTabs;
import got.common.database.GOTRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class GOTItemJewelryActive extends Item {
    private List<PotionEffect> effects = new ArrayList<PotionEffect>();
    public static int timer = 200;

    public GOTItemJewelryActive(int duration) {
        setMaxStackSize(1);
        setCreativeTab(GOTCreativeTabs.tabMisc);
        setMaxDamage(duration);
    }

    public GOTItemJewelryActive addPotionEffect(int i, int seconds) {
        this.effects.add(new PotionEffect(i, seconds * 20));
        return this;
    }

    public GOTItemJewelryActive addPotionEffect(int i, int seconds, int ampl) {
        this.effects.add(new PotionEffect(i, seconds * 20, ampl));
        return this;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
//        if (!stack.hasTagCompound()) {
//            stack.stackTagCompound = new NBTTagCompound();
//            stack.stackTagCompound.setInteger("timer", 0);
//        }

        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) {
            nbt = new NBTTagCompound();
            stack.setTagCompound(nbt);
            nbt.setInteger("timer", 0);
        }

        if (nbt.getInteger("timer") == 0) {
            if(!world.isRemote) {
                for(PotionEffect effect : this.effects) {
                    player.addPotionEffect(new PotionEffect(effect));
                }
                if(!player.capabilities.isCreativeMode)
                {
                    if(this == GOTRegistry.ritualBlade) {
                        player.attackEntityFrom(DamageSource.generic, 6.0f);
                    }
                    if(this == GOTRegistry.direwolfFang) {
                        player.attackEntityFrom(DamageSource.generic, 4.0f);
                    }
                    stack.damageItem(1, player);
                }
                nbt.setInteger("timer", timer);
            }
        }
        else {
            if(!world.isRemote) {
                player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("got.jewelry.desc.cooldown")
                        + ' ' + ticksToElapsedTime(nbt.getInteger("timer"))));
            }
        }
        return stack;
    }

    protected static String ticksToElapsedTime(int ticks) {
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        seconds %= 60;
        return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int meta, boolean b) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) {
            nbt = new NBTTagCompound();
            stack.setTagCompound(nbt);
            nbt.setInteger("timer", 0);
        }
        if (nbt.getInteger("timer") > 0) {
            nbt.setInteger("timer", nbt.getInteger("timer") - 1);
        }
    }

}