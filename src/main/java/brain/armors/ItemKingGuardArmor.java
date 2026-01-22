package brain.armors;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemKingGuardArmor extends ItemArmor {
    @SideOnly(Side.CLIENT)
    private IIcon itemIcon;
    
    public ItemKingGuardArmor(ArmorMaterial material, int renderIndex, int armorType) {
        super(material, renderIndex, armorType);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        String itemName = this.getUnlocalizedName().substring(this.getUnlocalizedName().lastIndexOf(".") + 1);
        this.itemIcon = register.registerIcon("armors:" + itemName);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return this.itemIcon;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconIndex(ItemStack stack) {
        return getIconFromDamage(stack.getItemDamage());
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        ModelBiped armorModel = null;
        
        if (itemStack != null && itemStack.getItem() == this) {
            switch (this.armorType) {
                case 0: // Шлем
                    armorModel = new KingGuardHelmet(0F);
                    break;
                case 1: // Нагрудник
                    armorModel = new KingGuardBody(0F);
                    break;
                case 2: // Поножи
                    armorModel = new KingGuardLegs(0F);
                    break;
                case 3: // Ботинки
                    armorModel = new KingGuardBoots(0F);
                    break;
            }
            
            if (armorModel != null) {
                armorModel.bipedHead.showModel = armorSlot == 0;
                armorModel.bipedHeadwear.showModel = armorSlot == 0;
                armorModel.bipedBody.showModel = armorSlot == 1 || armorSlot == 2;
                armorModel.bipedRightArm.showModel = armorSlot == 1;
                armorModel.bipedLeftArm.showModel = armorSlot == 1;
                armorModel.bipedRightLeg.showModel = armorSlot == 2 || armorSlot == 3;
                armorModel.bipedLeftLeg.showModel = armorSlot == 2 || armorSlot == 3;
                
                armorModel.isSneak = entityLiving.isSneaking();
                armorModel.isRiding = entityLiving.isRiding();
                armorModel.isChild = entityLiving.isChild();
                
                if (entityLiving instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entityLiving;
                    
                    ItemStack heldItem = player.getHeldItem();
                    if (heldItem != null) {
                        armorModel.heldItemRight = 1;
                        
                        if (player.getItemInUseCount() > 0) {
                            armorModel.aimedBow = true;
                        } else {
                            armorModel.aimedBow = false;
                        }
                    } else {
                        armorModel.heldItemRight = 0;
                        armorModel.aimedBow = false;
                    }
                }
                
                armorModel.setRotationAngles(0, 0, 0, 0, 0, 0.0625F, entityLiving);
                
                armorModel.bipedHead.rotateAngleX = entityLiving.prevRotationPitch * 0.017453292F;
                armorModel.bipedHead.rotateAngleY = entityLiving.rotationYawHead * 0.017453292F;
            }
        }
        
        return armorModel;
    }
    
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return "armors:textures/models/armor/king_guard.png";
    }
}