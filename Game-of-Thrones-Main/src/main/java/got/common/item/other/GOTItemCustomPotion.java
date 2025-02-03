package got.common.item.other;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.HashMultimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.entity.potion.GOTEntityCustomPotion;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

public class GOTItemCustomPotion extends Item {
    private List<PotionEffect> effectCache = new ArrayList<PotionEffect>();
    @SideOnly(Side.CLIENT)
    private IIcon field_94591_c;
    @SideOnly(Side.CLIENT)
    private IIcon field_94590_d;
    @SideOnly(Side.CLIENT)
    private IIcon field_94592_ct;

    public GOTItemCustomPotion(PotionEffect... list) {
        setMaxStackSize(1);
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(CreativeTabs.tabBrewing);
        this.effectCache = Arrays.asList(list);
    }

    public List<PotionEffect> getEffects(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("CustomPotionEffects", 9)) {
            ArrayList<PotionEffect> arraylist = new ArrayList<PotionEffect>();
            NBTTagList nbttaglist = stack.getTagCompound().getTagList("CustomPotionEffects", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);

                if (potioneffect != null) {
                    arraylist.add(potioneffect);
                }
            }

            return arraylist;
        } else
            return this.effectCache;
    }

    @Override
    public ItemStack onEaten(ItemStack p_77654_1_, World p_77654_2_, EntityPlayer p_77654_3_) {
        if (!p_77654_3_.capabilities.isCreativeMode) {
            --p_77654_1_.stackSize;
        }

        if (!p_77654_2_.isRemote) {
            List<PotionEffect> list = getEffects(p_77654_1_);

            if (list != null) {
                for (PotionEffect potioneffect : list) {
                    p_77654_3_.addPotionEffect(new PotionEffect(potioneffect));
                }
            }
        }

        if (!p_77654_3_.capabilities.isCreativeMode) {
            if (p_77654_1_.stackSize <= 0)
                return new ItemStack(Items.glass_bottle);

            p_77654_3_.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
        }

        return p_77654_1_;
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
    public int getMaxItemUseDuration(ItemStack p_77626_1_) {
        return 32;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_) {
        return EnumAction.drink;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
        if (isSplash(p_77659_1_.getItemDamage())) {
            if (!p_77659_3_.capabilities.isCreativeMode) {
                --p_77659_1_.stackSize;
            }

            p_77659_2_.playSoundAtEntity(p_77659_3_, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            if (!p_77659_2_.isRemote) {
                p_77659_2_.spawnEntityInWorld(new GOTEntityCustomPotion(p_77659_2_, p_77659_3_, p_77659_1_));
            }

            return p_77659_1_;
        } else {
            p_77659_3_.setItemInUse(p_77659_1_, getMaxItemUseDuration(p_77659_1_));
            return p_77659_1_;
        }
    }

    @Override
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_) {
        return isSplash(p_77617_1_) ? this.field_94591_c : this.field_94590_d;
    }

    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int p_77618_1_, int p_77618_2_) {
        return p_77618_2_ == 0 ? this.field_94592_ct : super.getIconFromDamageForRenderPass(p_77618_1_, p_77618_2_);
    }

    public static boolean isSplash(int damage) {
        return damage != 0;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromDamage(int p_77620_1_) {
        return this.effectCache == null || this.effectCache.isEmpty()
                ? 16777215 : Potion.potionTypes[this.effectCache.get(0).getPotionID()].getLiquidColor();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int tint) {
        return tint > 0 ? 16777215 : getColorFromDamage(stack.getItemDamage());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public boolean isEffectInstant(int p_77833_1_) {

        if (this.effectCache != null && !this.effectCache.isEmpty()) {
            Iterator<PotionEffect> iterator = this.effectCache.iterator();
            PotionEffect potioneffect;

            do {
                if (!iterator.hasNext())
                    return false;

                potioneffect = (PotionEffect)iterator.next();
            } while (!Potion.potionTypes[potioneffect.getPotionID()].isInstant());

            return true;
        } else
            return false;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String s = "";

        if (isSplash(stack.getItemDamage())) {
            s = StatCollector.translateToLocal("potion.prefix.grenade").trim() + " ";
        }

        List<PotionEffect> list = getEffects(stack);
        String s1;

        if (list != null && !list.isEmpty()) {
            s1 = ((PotionEffect)list.get(0)).getEffectName();
            s1 = s1 + ".postfix";
            return s + StatCollector.translateToLocal(s1).trim();
        } else {
            s1 = PotionHelper.func_77905_c(stack.getItemDamage());
            return StatCollector.translateToLocal(s1).trim() + " " + super.getItemStackDisplayName(stack);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List<String> p_77624_3_, boolean p_77624_4_) {
        List<PotionEffect> list = getEffects(p_77624_1_);
        HashMultimap<String, AttributeModifier> hashmultimap = HashMultimap.create();
        if (list != null && !list.isEmpty()) {
            for(PotionEffect potioneffect : list) {
                String s1 = StatCollector.translateToLocal(potioneffect.getEffectName()).trim();
                Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                Map<?, ?> map = potion.func_111186_k();

                if (map != null && map.size() > 0) {
                    Iterator<?> iterator = map.entrySet().iterator();

                    while (iterator.hasNext()) {
                        Entry<?, ?> entry = (Entry<?, ?>)iterator.next();
                        AttributeModifier attributemodifier = (AttributeModifier)entry.getValue();
                        AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.func_111183_a(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                        hashmultimap.put(((IAttribute)entry.getKey()).getAttributeUnlocalizedName(), attributemodifier1);
                    }
                }

                if (potioneffect.getAmplifier() > 0) {
                    s1 = s1 + " " + StatCollector.translateToLocal("potion.potency." + potioneffect.getAmplifier()).trim();
                }

                if (potioneffect.getDuration() > 20) {
                    s1 = s1 + " (" + Potion.getDurationString(potioneffect) + ")";
                }

                if (potion.isBadEffect()) {
                    p_77624_3_.add(EnumChatFormatting.RED + s1);
                } else {
                    p_77624_3_.add(EnumChatFormatting.GRAY + s1);
                }
            }
        } else {
            String s = StatCollector.translateToLocal("potion.empty").trim();
            p_77624_3_.add(EnumChatFormatting.GRAY + s);
        }

        if (!hashmultimap.isEmpty()) {
            p_77624_3_.add("");
            p_77624_3_.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("potion.effects.whenDrank"));

            for (Entry<String, AttributeModifier> entry1 : hashmultimap.entries()) {
                AttributeModifier attributemodifier2 = (AttributeModifier)entry1.getValue();
                double d0 = attributemodifier2.getAmount();
                double d1;

                if (attributemodifier2.getOperation() != 1 && attributemodifier2.getOperation() != 2) {
                    d1 = attributemodifier2.getAmount();
                } else {
                    d1 = attributemodifier2.getAmount() * 100.0D;
                }

                if (d0 > 0.0D) {
                    p_77624_3_.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier2.getOperation(), new Object[] {ItemStack.field_111284_a.format(d1), StatCollector.translateToLocal("attribute.name." + (String)entry1.getKey())}));
                } else if (d0 < 0.0D) {
                    d1 *= -1.0D;
                    p_77624_3_.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + attributemodifier2.getOperation(), new Object[] {ItemStack.field_111284_a.format(d1), StatCollector.translateToLocal("attribute.name." + (String)entry1.getKey())}));
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        List<PotionEffect> list = getEffects(stack);
        return list != null && !list.isEmpty();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_) {
        this.field_94590_d = p_94581_1_.registerIcon("potion_bottle_drinkable");
        this.field_94591_c = p_94581_1_.registerIcon("potion_bottle_splash");
        this.field_94592_ct = p_94581_1_.registerIcon("potion_overlay");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        if(pass == 0) return this.field_94592_ct;
        return getIconFromDamage(stack.getItemDamage());
    }
}