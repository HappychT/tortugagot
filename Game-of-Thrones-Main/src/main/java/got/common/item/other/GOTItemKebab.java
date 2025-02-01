package got.common.item.other;

import got.common.database.GOTCreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class GOTItemKebab extends GOTItemFood {
	public int effectID;
	public int duration;

	public GOTItemKebab(int healAmount, float saturation, boolean canWolfEat, int effectID, int duration) {
		super(healAmount, saturation, canWolfEat);
		this.effectID = effectID;
		this.duration = duration * 20;
		setCreativeTab(GOTCreativeTabs.tabFood);
	}


	public GOTItemKebab(int healAmount, float saturation, boolean canWolfEat) {
		this(healAmount, saturation, canWolfEat, 0, 0);
	}

	@Override
	public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if (!world.isRemote && world.rand.nextInt(100) == 0) {
			entityplayer.addChatMessage(new ChatComponentTranslation("got.chat.goodkebab"));
			entityplayer.addPotionEffect(new PotionEffect(effectID, duration * 10));
			return super.onEaten(itemstack, world, entityplayer);
		}
		entityplayer.addPotionEffect(new PotionEffect(effectID, duration));
		return super.onEaten(itemstack, world, entityplayer);
	}
}