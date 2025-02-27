package got.common.item.weapon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.HashMultimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.database.GOTCreativeTabs;
import got.common.entity.other.GOTEntityThrowingBomb;
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
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class GOTItemThrowingBomb extends Item {

    private final List<PotionEffect> listEffects;
    private final double[] radius;
    private final float damage;

    public GOTItemThrowingBomb(double[] radius, float damage, PotionEffect... list) {
        setMaxStackSize(16);
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(GOTCreativeTabs.tabCombat);
        this.listEffects = Arrays.asList(list);
        this.radius = radius;
        this.damage = damage;
    }

    public List<PotionEffect> getEffects(ItemStack p_77832_1_) {
        if (p_77832_1_.hasTagCompound() && p_77832_1_.getTagCompound().hasKey("CustomPotionEffects", 9)) {
            ArrayList<PotionEffect> arraylist = new ArrayList<PotionEffect>();
            NBTTagList nbttaglist = p_77832_1_.getTagCompound().getTagList("CustomPotionEffects", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);

                if (potioneffect != null) {
                    arraylist.add(potioneffect);
                }
            }

            return arraylist;
        } else
            return this.listEffects;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack p_77626_1_) {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_) {
        return EnumAction.drink;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (!entityplayer.capabilities.isCreativeMode) {
            --itemstack.stackSize;
        }

        world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote) {
            world.spawnEntityInWorld(new GOTEntityThrowingBomb(world, entityplayer, this.listEffects, this.radius).setDamage(this.damage));
        }

        return itemstack;
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float f, float f1, float f2) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromDamage(int meta) {
        return PotionHelper.func_77915_a(meta, false);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemstack, int tintIndex) {
        return tintIndex > 0 ? 16777215 : getColorFromDamage(itemstack.getItemDamage());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public boolean isEffectInstant(int meta) {
        if (this.listEffects != null && !this.listEffects.isEmpty()) {
            Iterator<PotionEffect> iterator = this.listEffects.iterator();
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
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List inf, boolean p_77624_4_) {
        if (itemstack.getItemDamage() != 0) {
            List<PotionEffect> list = Items.potionitem.getEffects(itemstack);
            HashMultimap<String, AttributeModifier> hashmultimap = HashMultimap.create();
            if (list != null && !list.isEmpty()) {

                for (PotionEffect potioneffect : list) {
                    String s1 = StatCollector.translateToLocal(potioneffect.getEffectName()).trim();
                    Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                    Map<IAttribute, AttributeModifier> map = potion.func_111186_k();

                    if (map != null && map.size() > 0) {
                        for (Entry<IAttribute, AttributeModifier> entry : map.entrySet()) {
                            AttributeModifier attributemodifier = entry.getValue();
                            AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.func_111183_a(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                            hashmultimap.put(entry.getKey().getAttributeUnlocalizedName(), attributemodifier1);
                        }
                    }

                    if (potioneffect.getAmplifier() > 0) {
                        s1 = s1 + " " + StatCollector.translateToLocal("potion.potency." + potioneffect.getAmplifier()).trim();
                    }

                    if (potioneffect.getDuration() > 20) {
                        s1 = s1 + " (" + Potion.getDurationString(potioneffect) + ")";
                    }

                    if (potion.isBadEffect()) {
                        inf.add(EnumChatFormatting.RED + s1);
                    } else {
                        inf.add(EnumChatFormatting.GRAY + s1);
                    }
                }
            } else {
                String s = StatCollector.translateToLocal("potion.empty").trim();
                inf.add(EnumChatFormatting.GRAY + s);
            }

            if (!hashmultimap.isEmpty()) {
                inf.add("");
                inf.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("potion.effects.whenDrank"));
                for (Entry<String, AttributeModifier> entry1 : hashmultimap.entries()) {
                    AttributeModifier attributemodifier2 = entry1.getValue();
                    double d0 = attributemodifier2.getAmount();
                    double d1;

                    if (attributemodifier2.getOperation() != 1 && attributemodifier2.getOperation() != 2) {
                        d1 = attributemodifier2.getAmount();
                    } else {
                        d1 = attributemodifier2.getAmount() * 100.0D;
                    }

                    if (d0 > 0.0D) {
                        inf.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier2.getOperation(), new Object[] {ItemStack.field_111284_a.format(d1), StatCollector.translateToLocal("attribute.name." + (String)entry1.getKey())}));
                    } else if (d0 < 0.0D) {
                        d1 *= -1.0D;
                        inf.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + attributemodifier2.getOperation(), new Object[] {ItemStack.field_111284_a.format(d1), StatCollector.translateToLocal("attribute.name." + (String)entry1.getKey())}));
                    }
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemstack) {
        return false;
    }

}