package got.common.item.other;

import got.common.database.GOTCreativeTabs;
import got.common.database.GOTRegistry;
import got.common.handlers.BandageCooldownHandler;
import got.common.systems.AccessorySystem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

public class GOTItemBandage extends Item {
    private final float healAmount;
    public static final String HEALING_TARGET_ID_NBT = "healingTargetID";

    public GOTItemBandage(float healAmount) {
        this.healAmount = healAmount;
        this.setMaxStackSize(1);
        setCreativeTab(GOTCreativeTabs.tabMisc);
    }
    public float getHealAmount() {
        return this.healAmount;
    }
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.bow;
    }

    private int getUseDuration(EntityPlayer player) {
        if (player.inventory.hasItem(GOTRegistry.gauzeSet)) {
            return 30;
        }
        return 50;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 50;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        long cooldown = BandageCooldownHandler.getCooldown(player);
        if (cooldown > 0) {
            if (!world.isRemote) {
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Бинты можно использовать снова через " + cooldown + " сек."));
            }
            return itemStack;
        }

        if (itemStack.stackTagCompound == null) {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        Entity target = getEntityPlayerLookingAt(player, 3.5D);

        // Если зажат Shift и игрок смотрит на тиммейта — лечим тиммейта. Иначе лечим себя.
        if (player.isSneaking() && target instanceof EntityPlayer && target != player) {
            itemStack.stackTagCompound.setInteger(HEALING_TARGET_ID_NBT, target.getEntityId());
        } else {
            itemStack.stackTagCompound.setInteger(HEALING_TARGET_ID_NBT, player.getEntityId());
        }

        player.setItemInUse(itemStack, this.getUseDuration(player));

        return itemStack;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        World world = player.worldObj;
        if (!world.isRemote) {
            if (stack.stackTagCompound == null || !stack.stackTagCompound.hasKey(HEALING_TARGET_ID_NBT)) {
                player.stopUsingItem();
                return;
            }
            int targetID = stack.stackTagCompound.getInteger(HEALING_TARGET_ID_NBT);
            Entity target = world.getEntityByID(targetID);

            if (target == null || !(target instanceof EntityPlayer) || player.getDistanceToEntity(target) > 4.0D) {
                player.stopUsingItem();
            }
        }
    }
    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(HEALING_TARGET_ID_NBT)) {
                int targetID = stack.stackTagCompound.getInteger(HEALING_TARGET_ID_NBT);
                Entity target = world.getEntityByID(targetID);

                if (target instanceof EntityPlayer) {
                    EntityPlayer targetPlayer = (EntityPlayer) target;

                    float finalHealAmount = this.healAmount;

                    if (AccessorySystem.hasAccessory(player, GOTRegistry.firstAidKit)) {
                        finalHealAmount *= 1.5F;
                    }

                    targetPlayer.heal(finalHealAmount);

                    BandageCooldownHandler.setCooldown(player, 35);

                    if (targetPlayer == player) {
                        player.addChatMessage(new ChatComponentText("Вы перевязали свои раны."));
                        world.playSoundAtEntity(player, "random.orb", 1.0F, 1.0F);
                    } else {
                        player.addChatMessage(new ChatComponentText("Вы вылечили " + targetPlayer.getDisplayName() + "."));
                        targetPlayer.addChatMessage(new ChatComponentText("Вас вылечили!"));
                        world.playSoundAtEntity(targetPlayer, "random.orb", 1.0F, 1.0F);
                    }
                }
            }
        }
        if (stack.stackTagCompound != null) {
            stack.stackTagCompound.removeTag(HEALING_TARGET_ID_NBT);
        }

        stack.stackSize--;

        return stack;
    }


    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int itemInUseCount) {
        if (stack.stackTagCompound != null) {
            stack.stackTagCompound.removeTag(HEALING_TARGET_ID_NBT);
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