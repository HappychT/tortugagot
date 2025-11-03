package got.common.item.weapon.carmor.west;

import brain.factions.Annot;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.item.other.GOTItemFactionArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ItemWesterlandsBoots extends GOTItemFactionArmor {

    public ItemWesterlandsBoots(ArmorMaterial material) {
        super(material, 3);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return "got:textures/armor/westland_boots.png";
    }

    @Override
    public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        if (!Annot.SERVER) {
            final got.client.model.somearmor.WestlandBootsModel armorModel = new got.client.model.somearmor.WestlandBootsModel();
            armorModel.isSneak = entityLiving.isSneaking();
            armorModel.isRiding = entityLiving.isRiding();
            armorModel.isChild = entityLiving.isChild();
            armorModel.heldItemRight = entityLiving.getEquipmentInSlot(0) != null ? 1 : 0;
            return armorModel;
        }
        return null;
    }
}