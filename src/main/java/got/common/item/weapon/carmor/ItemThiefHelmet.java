package got.common.item.weapon.carmor;

import brain.factions.Annot;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.item.other.GOTItemArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ItemThiefHelmet extends GOTItemArmor {


    public ItemThiefHelmet(ArmorMaterial material) {
        super(material, 0);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return "got:textures/armor/thief_helmet.png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        if (!Annot.SERVER) {
            final got.client.model.thiefArmor.GOTModelThiefHelmet armorModel = new got.client.model.thiefArmor.GOTModelThiefHelmet();
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