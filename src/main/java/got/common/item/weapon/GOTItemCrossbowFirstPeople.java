package got.common.item.weapon;

import got.common.database.GOTCreativeTabs;
import got.common.database.GOTMaterial;
import got.common.entity.other.GOTEntityCrossbowBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GOTItemCrossbowFirstPeople extends GOTItemCrossbow {

    public GOTItemCrossbowFirstPeople() {
        super(GOTMaterial.IRON);
        boltDamageFactor = 1.6f;
        setMaxDamage(7000);
        setCreativeTab(GOTCreativeTabs.tabStory);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (GOTItemCrossbow.isLoaded(itemstack)) {
            ItemStack boltItem = GOTItemCrossbow.getLoaded(itemstack);
            if (boltItem != null) {
                float charge = 1.0f;
                ItemStack shotBolt = boltItem.copy();
                shotBolt.stackSize = 1;
                GOTEntityCrossbowBolt bolt = new GOTEntityCrossbowBolt(world, entityplayer, shotBolt, charge * 2.0f * GOTItemCrossbow.getCrossbowLaunchSpeedFactor(itemstack));
                bolt.isBleeding = true;
                if (bolt.boltDamageFactor < 1.0) {
                    bolt.boltDamageFactor = 1.0;
                }
                if (charge >= 1.0f) {
                    bolt.setIsCritical(true);
                }
                GOTItemCrossbow.applyCrossbowModifiers(bolt, itemstack);
                if (!shouldConsumeBolt(itemstack, entityplayer)) {
                    bolt.canBePickedUp = 2;
                }
                if (!world.isRemote) {
                    world.spawnEntityInWorld(bolt);
                }
                world.playSoundAtEntity(entityplayer, "got:item.crossbow", 1.0f, 1.0f / (itemRand.nextFloat() * 0.4f + 1.2f) + charge * 0.5f);
                itemstack.damageItem(1, entityplayer);
                if (!world.isRemote) {
                    setLoaded(itemstack, null);
                }
            }
        } else if (!shouldConsumeBolt(itemstack, entityplayer) || getInvBoltSlot(entityplayer) >= 0) {
            entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        }
        return itemstack;
    }
}
