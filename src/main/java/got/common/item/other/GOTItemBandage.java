package got.common.item.other;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.database.GOTCreativeTabs;
import got.common.database.GOTRegistry;
import got.common.systems.AccessorySystem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

public class GOTItemBandage extends Item {
    private final float healAmount;

    public GOTItemBandage(float healAmount) {
        this.healAmount = healAmount;
        this.setMaxStackSize(1);
        setCreativeTab(GOTCreativeTabs.tabCombat);
    }
    public float getHealAmount() {
        return this.healAmount;
    }
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.bow;
    }
    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 100;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        Entity target = getEntityPlayerLookingAt(player, 3.5D);

        if (target instanceof EntityPlayer && target != player) {
            if (itemStack.stackTagCompound == null) {
                itemStack.setTagCompound(new NBTTagCompound());
            }
            itemStack.stackTagCompound.setInteger("healingTargetID", target.getEntityId());

            player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
        } else {
            if (!world.isRemote) {
                player.addChatMessage(new ChatComponentText("Цель не найдена или слишком далеко."));
            }
        }
        return itemStack;
    }
    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        World world = player.worldObj;
        if (!world.isRemote) {
            if (stack.stackTagCompound == null || !stack.stackTagCompound.hasKey("healingTargetID")) {
                player.stopUsingItem();
                return;
            }
            int targetID = stack.stackTagCompound.getInteger("healingTargetID");
            Entity target = world.getEntityByID(targetID);

            if (target == null || !(target instanceof EntityPlayer) || player.getDistanceToEntity(target) > 4.0D) {
                player.stopUsingItem();
            }
        }
    }
    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey("healingTargetID")) {
                int targetID = stack.stackTagCompound.getInteger("healingTargetID");
                Entity target = world.getEntityByID(targetID);

                if (target instanceof EntityPlayer) {
                    EntityPlayer targetPlayer = (EntityPlayer) target;

                    float finalHealAmount = this.healAmount;

                    if (AccessorySystem.hasAccessory(player, GOTRegistry.firstAidKit)) {
                        finalHealAmount *= 1.5F;
                    }

                    targetPlayer.heal(finalHealAmount);

                    player.addChatMessage(new ChatComponentText("You successfully healed " + targetPlayer.getDisplayName() + "."));
                    targetPlayer.addChatMessage(new ChatComponentText("You have been healed!"));
                    world.playSoundAtEntity(targetPlayer, "random.orb", 1.0F, 1.0F);
                }
            }
        }
        if (stack.stackTagCompound != null) {
            stack.stackTagCompound.removeTag("healingTargetID");
        }
        return stack;
    }


    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int itemInUseCount) {
        if (stack.stackTagCompound != null) {
            stack.stackTagCompound.removeTag("healingTargetID");
        }
        if (!world.isRemote) {
            player.addChatMessage(new ChatComponentText("Применение бинта прервано."));
        }
    }
    private Entity getEntityPlayerLookingAt(EntityPlayer player, double range) {
        Vec3 look = player.getLookVec();
        Vec3 start = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3 end = start.addVector(look.xCoord * range, look.yCoord * range, look.zCoord * range);

        Entity pointedEntity = null;
        double minDistance = range;

        List<Entity> list = player.worldObj.getEntitiesWithinAABBExcludingEntity(player,
                player.boundingBox.addCoord(look.xCoord * range, look.yCoord * range, look.zCoord * range).expand(1.0D, 1.0D, 1.0D));

        for (Entity entity : list) {
            if (entity instanceof EntityPlayer && entity.canBeCollidedWith()) {
                float border = entity.getCollisionBorderSize();
                AxisAlignedBB aabb = entity.boundingBox.expand(border, border, border);
                MovingObjectPosition mop = aabb.calculateIntercept(start, end);

                if (aabb.isVecInside(start)) {
                    if (0.0D < minDistance || minDistance == 0.0D) {
                        pointedEntity = entity;
                        minDistance = 0.0D;
                    }
                } else if (mop != null) {
                    double distance = start.distanceTo(mop.hitVec);
                    if (distance < minDistance || minDistance == 0.0D) {
                        pointedEntity = entity;
                        minDistance = distance;
                    }
                }
            }
        }

        return pointedEntity;
    }
}