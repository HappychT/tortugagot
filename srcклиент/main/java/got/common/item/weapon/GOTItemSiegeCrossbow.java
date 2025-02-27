package got.common.item.weapon;

import got.common.database.GOTRegistry;
import got.common.entity.other.GOTEntityCrossbowBolt;
import got.common.recipe.GOTRecipe;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class GOTItemSiegeCrossbow extends GOTItemCrossbow {

    public GOTItemSiegeCrossbow() {
        super(ToolMaterial.IRON);
        setMaxDamage((int) (this.crossbowMaterial.getMaxUses() * 1.25f));
        setMaxStackSize(1);
        this.boltDamageFactor = 1.25f;
    }

    @Override
    public int getInvBoltSlot(EntityPlayer entityplayer) {
        for (int slot = 0; slot < entityplayer.inventory.mainInventory.length; ++slot) {
            ItemStack invItem = entityplayer.inventory.mainInventory[slot];
            if (invItem == null || !(invItem.getItem() instanceof GOTItemCrossbowBolt)) {
                continue;
            }
            return slot;
        }
        return -1;
    }

    @Override
    public boolean getIsRepairable(ItemStack itemstack, ItemStack repairItem) {
        if (GOTRecipe.checkItemEquals(this.crossbowMaterial.getRepairItemStack(), repairItem))
            return true;
        return super.getIsRepairable(itemstack, repairItem);
    }

    @Override
    public int getMaxDrawTime() {
        return 200;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack) {
        return 36000;
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

    @Override
    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int useTick) {
        int ticksInUse = getMaxItemUseDuration(itemstack) - useTick;
        if (ticksInUse >= getMaxDrawTime() && !GOTItemCrossbow.isLoaded(itemstack)) {
            ItemStack boltItem = null;
            int boltSlot = getInvBoltSlot(entityplayer);
            if (boltSlot >= 0) {
                boltItem = entityplayer.inventory.mainInventory[boltSlot];
            }
            boolean shouldConsume = shouldConsumeBolt(itemstack, entityplayer);
            if (boltItem == null && !shouldConsume) {
                boltItem = new ItemStack(GOTRegistry.crossbowBolt);
            }
            if (boltItem != null) {
                if (shouldConsume && boltSlot >= 0) {
                    --boltItem.stackSize;
                    if (boltItem.stackSize <= 0) {
                        entityplayer.inventory.mainInventory[boltSlot] = null;
                    }
                }
                if (!world.isRemote) {
                    setLoaded(itemstack, boltItem.copy());
                }
            }
            entityplayer.clearItemInUse();
        }
    }

    @Override
    public void onUsingTick(ItemStack itemstack, EntityPlayer entityplayer, int count) {
        World world = entityplayer.worldObj;
        if (!world.isRemote && !GOTItemCrossbow.isLoaded(itemstack) && getMaxItemUseDuration(itemstack) - count == getMaxDrawTime()) {
            world.playSoundAtEntity(entityplayer, "got:item.crossbowLoad", 1.0f, 1.5f + world.rand.nextFloat() * 0.2f);
        }
    }

    /*@Override
    public void setLoaded(ItemStack itemstack, ItemStack ammo) {
        if (itemstack != null && itemstack.getItem() instanceof GOTItemCrossbow) {
            NBTTagCompound nbt = itemstack.getTagCompound();
            if (nbt == null) {
                nbt = new NBTTagCompound();
                itemstack.setTagCompound(nbt);
            }
            if (ammo != null) {
                NBTTagCompound ammoData = new NBTTagCompound();
                ammo.writeToNBT(ammoData);
                nbt.setTag("GOTCrossbowAmmo", ammoData);
            } else {
                nbt.removeTag("GOTCrossbowAmmo");
            }
            if (nbt.hasKey("GOTCrossbowLoaded")) {
                nbt.removeTag("GOTCrossbowLoaded");
            }
        }
    }*/

    @Override
    public boolean shouldConsumeBolt(ItemStack itemstack, EntityPlayer entityplayer) {
        return !entityplayer.capabilities.isCreativeMode && EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemstack) == 0;
    }

}