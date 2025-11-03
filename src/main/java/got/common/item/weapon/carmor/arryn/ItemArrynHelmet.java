package got.common.item.weapon.carmor.arryn;

import brain.factions.Annot;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.item.other.GOTItemFactionArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ItemArrynHelmet extends GOTItemFactionArmor {


    public ItemArrynHelmet(ArmorMaterial material) {
        super(material, 0);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        // Указываем путь к текстуре шлема Арренов
        return "got:textures/armor/arryn_helmet.png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        if (!Annot.SERVER) {
            // Используем новую модель шлема
            final got.client.model.armor.GOTModelArrynHelmet armorModel = new got.client.model.armor.GOTModelArrynHelmet();
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
