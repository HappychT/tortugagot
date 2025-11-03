package got.common.item.weapon.carmor.west;

import brain.factions.Annot;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.item.other.GOTItemFactionArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ItemWesterlandsLeggings extends GOTItemFactionArmor {

    public ItemWesterlandsLeggings(ArmorMaterial material) {
        super(material, 2);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return "got:textures/armor/westland_leggins.png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        if (!Annot.SERVER) {
            final got.client.model.somearmor.WestlandLeggingsModel armorModel = new got.client.model.somearmor.WestlandLeggingsModel();
            if (armorModel != null) {
                armorModel.isSneak = entityLiving.isSneaking();
                armorModel.isRiding = entityLiving.isRiding();
                armorModel.isChild = entityLiving.isChild();
                armorModel.heldItemRight = entityLiving.getEquipmentInSlot(0) != null ? 1 : 0;
            }
            return armorModel;
        }
        return null;
    }
}