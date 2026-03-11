package got.common.item.weapon;

import java.util.ArrayList;
import java.util.List;

import got.common.database.GOTMaterial;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class GOTItemGiantClub extends GOTItemSword {

    public GOTItemGiantClub() {
        super(GOTMaterial.GIANT_CLUB);
        this.gotWeaponDamage = 10.0f;
        setMaxDamage(7000);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.none;
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase entity, EntityLivingBase attacker) {
        itemstack.damageItem(1, attacker);
        useTwoHandedSword(itemstack, entity.worldObj, entity, attacker);
        return true;
    }

    public void useTwoHandedSword(ItemStack itemstack, World world, EntityLivingBase target, EntityLivingBase user) {
        user.swingItem();
        if (!world.isRemote) {
            double radius = 3.0;

            Vec3 position = Vec3.createVectorHelper(user.posX, user.posY, user.posZ);
            Vec3 look = user.getLookVec();
            Vec3 sight = position.addVector(look.xCoord * radius, look.yCoord * radius, look.zCoord * radius);
            float sightWidth = 1.0f;

            List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(user, user.boundingBox.addCoord(look.xCoord * radius, look.yCoord * radius, look.zCoord * radius).expand(sightWidth, sightWidth, sightWidth));

            ArrayList<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
            if (!entities.isEmpty()) {
                for (Entity entity : entities) {
                    if (!(entity instanceof EntityLivingBase) || entity == user.ridingEntity && !((EntityLivingBase)entity).canRiderInteract() || !entity.canBeCollidedWith()) {
                        continue;
                    }
                    float width = 1.0f;
                    AxisAlignedBB axisalignedbb = entity.boundingBox.expand(width, width, width);
                    MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(position, sight);
                    if (axisalignedbb.isVecInside(position)) {
                        targets.add((EntityLivingBase)entity);
                        continue;
                    }
                    if (movingobjectposition == null) {
                        continue;
                    }
                    targets.add((EntityLivingBase)entity);
                }
            }

            if (!targets.isEmpty()) {
                for (EntityLivingBase entity : targets) {
                    if (entity == user || entity == target
                            || entity instanceof EntityPlayer && (!(user instanceof EntityPlayer)
                                    ? user instanceof EntityLiving && ((EntityLiving) user).getAttackTarget() != entity
                                    : !MinecraftServer.getServer().isPVPEnabled())) {
                        continue;
                    }
                    if (user instanceof EntityPlayer) {
                        entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) user), this.gotWeaponDamage * 0.6f);
                    } else {
                        entity.attackEntityFrom(DamageSource.causeMobDamage(user), this.gotWeaponDamage * 0.6f);
                    }
                }
            }
        }
    }
}