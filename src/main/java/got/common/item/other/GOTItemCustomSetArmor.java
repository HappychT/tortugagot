package got.common.item.other;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.client.model.customarmor.CustomArmorModelCache;
import got.common.GOTLevelData;
import got.common.database.GOTArmorModels;
import got.common.database.GOTCreativeTabs;
import got.common.enchant.GOTEnchantment;
import got.common.enchant.GOTEnchantmentHelper;
import got.common.entity.other.GOTEntityNPC;
import got.common.faction.GOTFaction;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class GOTItemCustomSetArmor extends GOTItemArmor {
    private final String helmetModelPrefix;
    private final String bodyModelPrefix;
    private final String texturePrefix;
    private final boolean showBodyOnHeadSlot;

    // Поле для хранения фракции
    public GOTFaction faction;

    public GOTItemCustomSetArmor(ArmorMaterial material, int slotType, String modelPrefix, String texturePrefix) {
        this(material, slotType, modelPrefix, modelPrefix, texturePrefix, GOTCreativeTabs.tabCombat, false);
    }

    public GOTItemCustomSetArmor(ArmorMaterial material, int slotType, String modelPrefix, String texturePrefix, GOTCreativeTabs tab) {
        this(material, slotType, modelPrefix, modelPrefix, texturePrefix, tab, false);
    }

    public GOTItemCustomSetArmor(ArmorMaterial material, int slotType, String modelPrefix, String texturePrefix, GOTCreativeTabs tab, boolean showBodyOnHeadSlot) {
        this(material, slotType, modelPrefix, modelPrefix, texturePrefix, tab, showBodyOnHeadSlot);
    }

    public GOTItemCustomSetArmor(ArmorMaterial material, int slotType, String helmetModelPrefix, String bodyModelPrefix, String texturePrefix, GOTCreativeTabs tab, boolean showBodyOnHeadSlot) {
        super(material, slotType, tab);
        this.helmetModelPrefix = helmetModelPrefix;
        this.bodyModelPrefix = bodyModelPrefix;
        this.texturePrefix = texturePrefix;
        this.showBodyOnHeadSlot = showBodyOnHeadSlot;
    }

    // Метод для привязки брони к фракции
    public GOTItemCustomSetArmor setFactionArmor(GOTFaction fac) {
        this.faction = fac;
        return this;
    }

    // Логика наложения эффекта слабости, если игрок не из нужной фракции
    @Override
    public void onArmorTick(World world, EntityPlayer entityplayer, ItemStack itemstack) {
        super.onArmorTick(world, entityplayer, itemstack);

        // Проверяем, привязана ли броня к фракции
        if (this.faction != null && !GOTEnchantmentHelper.hasEnchant(itemstack, GOTEnchantment.multifracConverter)) {
            if (GOTLevelData.getData(entityplayer).getPledgeFaction() != faction && !entityplayer.isPotionActive(Potion.weakness)) {
                entityplayer.addPotionEffect(new PotionEffect(Potion.weakness.id, 200));
            }
        }
    }

    // Добавление названия фракции в тултип предмета
    @SideOnly(value = Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean show) {
        super.addInformation(itemstack, entityplayer, list, show);

        if (this.faction != null) {
            switch (faction) {
                case ARRYN:
                    list.add(EnumChatFormatting.DARK_BLUE + StatCollector.translateToLocalFormatted("got.faction.ARRYN.name"));
                    break;
                case DORNE:
                    list.add(EnumChatFormatting.GOLD + StatCollector.translateToLocalFormatted("got.faction.DORNE.name"));
                    break;
                case DRAGONSTONE:
                    list.add(EnumChatFormatting.DARK_RED + StatCollector.translateToLocalFormatted("got.faction.DRAGONSTONE.name"));
                    break;
                case IRONBORN:
                    list.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocalFormatted("got.faction.IRONBORN.name"));
                    break;
                case NORTH:
                    list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted("got.faction.NORTH.name"));
                    break;
                case REACH:
                    list.add(EnumChatFormatting.GREEN + StatCollector.translateToLocalFormatted("got.faction.REACH.name"));
                    break;
                case RIVERLANDS:
                    list.add(EnumChatFormatting.DARK_AQUA + StatCollector.translateToLocalFormatted("got.faction.RIVERLANDS.name"));
                    break;
                case STORMLANDS:
                    list.add(EnumChatFormatting.DARK_GREEN + StatCollector.translateToLocalFormatted("got.faction.STORMLANDS.name"));
                    break;
                case WESTERLANDS:
                    list.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("got.faction.WESTERLANDS.name"));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        if (itemStack == null || itemStack.getItem() != this) {
            return null;
        }

        ModelBiped armorModel;
        switch (this.armorType) {
            case 0:
                armorModel = CustomArmorModelCache.getHelmetModel(helmetModelPrefix);
                break;
            case 1:
                armorModel = CustomArmorModelCache.getBodyModel(bodyModelPrefix);
                break;
            case 2:
                armorModel = CustomArmorModelCache.getLeggingsModel();
                break;
            case 3:
                armorModel = CustomArmorModelCache.getBootsModel();
                break;
            default:
                armorModel = CustomArmorModelCache.getBodyModel(bodyModelPrefix);
                break;
        }

        if (armorModel == null) {
            return null;
        }

        applyEntityState(armorModel, entityLiving);
        setVisibleParts(armorModel, armorSlot);
        return armorModel;
    }

    private void applyEntityState(ModelBiped armorModel, EntityLivingBase entityLiving) {
        boolean hasEntity = entityLiving != null;
        armorModel.isSneak = hasEntity && entityLiving.isSneaking();
        armorModel.isRiding = hasEntity && entityLiving.isRiding();
        armorModel.isChild = hasEntity && entityLiving.isChild();
        armorModel.heldItemLeft = 0;
        armorModel.heldItemRight = 0;
        armorModel.aimedBow = false;

        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            ItemStack heldItem = player.getHeldItem();
            if (heldItem != null) {
                armorModel.heldItemRight = 1;
                if (player.getItemInUseCount() > 0) {
                    EnumAction useAction = heldItem.getItemUseAction();
                    if (useAction == EnumAction.block) {
                        armorModel.heldItemRight = 3;
                    } else if (GOTArmorModels.usesBowArmPose(heldItem)) {
                        armorModel.aimedBow = true;
                    }
                }
            }
        } else if (GOTArmorModels.INSTANCE != null) {
            GOTArmorModels.INSTANCE.setupHeldItem(armorModel, entityLiving, GOTArmorModels.INSTANCE.getHeldItemRight(entityLiving), true);
            if (entityLiving instanceof GOTEntityNPC || GOTArmorModels.INSTANCE.getCustomNpc(entityLiving) != null) {
                GOTArmorModels.INSTANCE.setupHeldItem(armorModel, entityLiving, GOTArmorModels.INSTANCE.getHeldItemLeft(entityLiving), false);
            }
        }

        if (GOTArmorModels.INSTANCE != null) {
            GOTArmorModels.INSTANCE.syncArmorModelWithRenderer(armorModel, entityLiving);
        }
    }

    private void setVisibleParts(ModelBiped armorModel, int armorSlot) {
        armorModel.bipedHead.showModel = armorSlot == 0;
        armorModel.bipedHeadwear.showModel = armorSlot == 0;
        armorModel.bipedBody.showModel = armorSlot == 1 || armorSlot == 2;
        armorModel.bipedRightArm.showModel = armorSlot == 1;
        armorModel.bipedLeftArm.showModel = armorSlot == 1;
        armorModel.bipedRightLeg.showModel = armorSlot == 1 || armorSlot == 2 || armorSlot == 3;
        armorModel.bipedLeftLeg.showModel = armorSlot == 1 || armorSlot == 2 || armorSlot == 3;

        if (showBodyOnHeadSlot && armorSlot == 0) {
            armorModel.bipedBody.showModel = true;
        }
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        String texturePart;
        switch (this.armorType) {
            case 0:
                texturePart = "helmet";
                break;
            case 1:
                texturePart = "body";
                break;
            case 2:
                texturePart = "legs";
                break;
            case 3:
                texturePart = "boots";
                break;
            default:
                texturePart = "body";
                break;
        }
        return "got:textures/armor/custom/" + texturePrefix + "_" + texturePart + ".png";
    }
}