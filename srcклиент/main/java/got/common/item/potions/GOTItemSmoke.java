package got.common.item.potions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.client.render.other.GOTRenderLingeringDispenser;
import got.common.entity.potion.GOTEntitySmoke;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
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

public class GOTItemSmoke extends ItemPotion {

    @SideOnly(Side.CLIENT)
    private IIcon field_94592_ct;

    @SideOnly(Side.CLIENT)
    public IIcon bottle;

    public List<PotionEffect> effectsList;

    public GOTItemSmoke(PotionEffect... effects) {
        this.effectsList = Lists.newArrayList(effects);
        setCreativeTab(CreativeTabs.tabBrewing);
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, new GOTRenderLingeringDispenser());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean isComplex) {
        List<PotionEffect> effects = getEffects(stack);
        HashMultimap<String, AttributeModifier> attributes = HashMultimap.create();

        if (effects == null || effects.isEmpty()) {
            String s = StatCollector.translateToLocal("potion.empty").trim();
            list.add(EnumChatFormatting.GRAY + s);
        } else {
            for (PotionEffect potioneffect : effects) {
                String s1 = StatCollector.translateToLocal(potioneffect.getEffectName()).trim();
                Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                Map<IAttribute, AttributeModifier> map = potion.func_111186_k();

                if (map != null && map.size() > 0) {
                    for (Entry<IAttribute, AttributeModifier> entry : map.entrySet()) {
                        AttributeModifier attributemodifier = entry.getValue();
                        AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.func_111183_a(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                        attributes.put(entry.getKey().getAttributeUnlocalizedName(), attributemodifier1);
                    }
                }

                if (potioneffect.getAmplifier() > 0) {
                    s1 = s1 + " " + StatCollector.translateToLocal("potion.potency." + potioneffect.getAmplifier()).trim();
                }
                if (potioneffect.getDuration() > 20) {
                    s1 = s1 + " (" + Potion.getDurationString(potioneffect) + ")";
                }

                if (potion.isBadEffect()) {
                    list.add(EnumChatFormatting.RED + s1);
                } else {
                    list.add(EnumChatFormatting.GRAY + s1);
                }
            }
        }

        if (!attributes.isEmpty()) {
            list.add("");
            list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("potion.effects.whenDrank"));

            for (Entry<String, AttributeModifier> entry1 : attributes.entries()) {
                AttributeModifier attributemodifier2 = entry1.getValue();
                double d0 = attributemodifier2.getAmount();
                double d1;

                if (attributemodifier2.getOperation() != 1 && attributemodifier2.getOperation() != 2) {
                    d1 = attributemodifier2.getAmount();
                } else {
                    d1 = attributemodifier2.getAmount() * 100.0D;
                }

                if (d0 > 0.0D) {
                    list.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier2.getOperation(), ItemStack.field_111284_a.format(d1), StatCollector.translateToLocal("attribute.name." + entry1.getKey())));
                } else if (d0 < 0.0D) {
                    d1 *= -1.0D;
                    list.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + attributemodifier2.getOperation(), ItemStack.field_111284_a.format(d1), StatCollector.translateToLocal("attribute.name." + entry1.getKey())));
                }
            }
        }
    }

    @Override
    public List<PotionEffect> getEffects(int meta) {
        List<PotionEffect> effects = new ArrayList<>();

        if (this.effectsList != null && !this.effectsList.isEmpty()) {
            for (PotionEffect effect : this.effectsList) {
                effects.add(effect);
            }
        }

        return effects;
    }

    @Override
    public List<PotionEffect> getEffects(ItemStack stack) {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("CustomPotionEffects", 9))
            return getEffects(stack.getItemDamage());
        List<PotionEffect> list = new ArrayList<>();
        NBTTagList nbttaglist = stack.getTagCompound().getTagList("CustomPotionEffects", 10);

        for (int i = 0; i < nbttaglist.tagCount(); i++) {
            NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
            PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbt);
            if (potioneffect != null) {
                list.add(potioneffect);
            }
        }

        return list;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return this.bottle;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String s = StatCollector.translateToLocal("got.potion").trim() + " ";

        List<PotionEffect> list = getEffects(stack);
        String s1;

        if (list != null && !list.isEmpty()) {
            s1 = list.get(0).getEffectName();
            s1 = s1 + ".postfix";
            return s + StatCollector.translateToLocal(s1).trim();
        }
        s1 = PotionHelper.func_77905_c(stack.getItemDamage());
        return StatCollector.translateToLocal(s1).trim() + " " + super.getItemStackDisplayName(stack);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.none;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack, int pass) {
        return true;
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
        return stack;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!player.capabilities.isCreativeMode) {
            stack.stackSize--;
        }

        world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote) {
            world.spawnEntityInWorld(new GOTEntitySmoke(world, player, stack));
        }

        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int p_77618_1_, int p_77618_2_) {
        return p_77618_2_ == 0 ? this.field_94592_ct : super.getIconFromDamageForRenderPass(p_77618_1_, p_77618_2_);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {
        this.field_94592_ct = reg.registerIcon("got:lingering_potion_overlay");
        this.bottle = reg.registerIcon("got:lingering_potion_bottle_lingering");
    }

}
