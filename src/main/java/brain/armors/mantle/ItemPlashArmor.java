package brain.armors.mantle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.database.GOTArmorModels;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.EnumAction;
import net.minecraft.util.IIcon;

public class ItemPlashArmor extends ItemArmor {
    @SideOnly(Side.CLIENT)
    private IIcon itemIcon;
    private final int plashType; 
    
    public ItemPlashArmor(ArmorMaterial material, int renderIndex, int armorType, int plashType) {
        super(material, renderIndex, armorType);
        this.plashType = plashType;
        this.setMaxDamage(0);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        String[] plashNames = {"plash1", "plash2", "plash3", "plash4"};
        if (plashType >= 0 && plashType < plashNames.length) {
            this.itemIcon = register.registerIcon("armors:" + plashNames[plashType]);
        } else {
            this.itemIcon = register.registerIcon("armors:plash1");
        }
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
        boolean hasEntity = entityLiving != null;
        
        if (itemStack != null && itemStack.getItem() == this) {
            switch (plashType) {
                case 0:
                    armorModel = new Plash1();
                    break;
                case 1:
                    armorModel = new Plash2(0);
                    break;
                case 2:
                    armorModel = new Plash3(0);
                    break;
                case 3:
                    armorModel = new Plash4(0);
                    break;
                default:
                    armorModel = new Plash1();
                    break;
            }
            
            if (armorModel != null) {
                armorModel.bipedHeadwear.showModel = false;
                armorModel.bipedBody.showModel = armorSlot == 0;
                armorModel.bipedRightArm.showModel = false;
                armorModel.bipedLeftArm.showModel = false;
                armorModel.bipedRightLeg.showModel = false;
                armorModel.bipedLeftLeg.showModel = false;
                
                armorModel.isSneak = hasEntity && entityLiving.isSneaking();
                armorModel.isRiding = hasEntity && entityLiving.isRiding();
                armorModel.isChild = hasEntity && entityLiving.isChild();
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
                        } else {
                            armorModel.aimedBow = false;
                        }
                    } else {
                        armorModel.heldItemRight = 0;
                        armorModel.aimedBow = false;
                    }
                }
                
              
            }
        }
        
        return armorModel;
    }
    
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return "armors:textures/models/armor/plash_" + plashType + ".png";
    }
}
