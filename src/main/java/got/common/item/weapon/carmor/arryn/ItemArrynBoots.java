package got.common.item.weapon.carmor.arryn;

import brain.factions.Annot;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.item.other.GOTItemFactionArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ItemArrynBoots extends GOTItemFactionArmor {

    public ItemArrynBoots(ArmorMaterial material) {
        super(material, 3);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        // Указываем путь к текстуре ботинок Арренов
        return "got:textures/armor/arryn_boots.png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        if (!Annot.SERVER) {
            // Используем новую модель ботинок
            final got.client.model.armor.GOTModelArrynBoots armorModel = new got.client.model.armor.GOTModelArrynBoots();
            armorModel.isSneak = entityLiving.isSneaking();
            armorModel.isRiding = entityLiving.isRiding();
            armorModel.isChild = entityLiving.isChild();
            armorModel.heldItemRight = entityLiving.getEquipmentInSlot(0) != null ? 1 : 0;
            return armorModel;
        }
        return null;
    }
}
