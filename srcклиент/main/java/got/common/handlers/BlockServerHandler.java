package got.common.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import got.common.systems.GOTCoreBlockingSystem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.lwjgl.Sys;

public class BlockServerHandler {

    public static final BlockServerHandler INSTANCE = new BlockServerHandler();

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {

        if (!(event.entity instanceof EntityPlayer)) {
            if(event.source.getEntity() instanceof EntityPlayer) {
                EntityPlayer attacker = (EntityPlayer) event.source.getEntity();
                if (attacker.getHeldItem() != null && attacker.getHeldItem().getItem() instanceof ItemSword) {
                    StaminaServerHandler.drainStaminaByPercent(GOTCoreBlockingSystem.getBlockData(attacker.getHeldItem().getItem().getClass(), attacker).getStaminaMissPercent(), attacker);
                }
            }
        }

        if (!(event.entity instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) event.entity;
        if (isBlocking(player)) {
            float[] blockAngles = {
                    GOTCoreBlockingSystem.getBlockData(player.getHeldItem().getItem().getClass(), player).getLeftBlockAngle(),
                    GOTCoreBlockingSystem.getBlockData(player.getHeldItem().getItem().getClass(), player).getRightBlockAngle()
            };
            if(event.source.getEntity() instanceof EntityPlayer) {
                EntityPlayer attacker = (EntityPlayer) event.source.getEntity();
                if (attacker.getHeldItem() != null && attacker.getHeldItem().getItem() instanceof ItemSword) {
                    StaminaServerHandler.drainStaminaByPercent(GOTCoreBlockingSystem.getBlockData(attacker.getHeldItem().getItem().getClass(), player).getStaminaMissPercent(), attacker);
                }
            }
            if (isDamageBlocked(player, event.source, blockAngles)) {
                StaminaServerHandler.drainStaminaByPercent(0.5, player);
                player.worldObj.playSoundAtEntity(player, "got:combat_block", 1, 1);
                event.setCanceled(true);
            } else {
                StaminaServerHandler.drainStaminaByPercent(0.6, player);
                event.ammount += (float) (event.ammount * 0.15); // 15% more damage to make the 20% total reduction of damage(50% vanilla reduction + 30% more damage)
            }
        } else {
            if(event.source.getEntity() instanceof EntityPlayer) {
                EntityPlayer attacker = (EntityPlayer) event.source.getEntity();
                if (attacker.getHeldItem() != null && attacker.getHeldItem().getItem() instanceof ItemSword) {
                    StaminaServerHandler.drainStaminaByPercent(GOTCoreBlockingSystem.getBlockData(attacker.getHeldItem().getItem().getClass(), player).getStaminaHitPercent(), attacker);
                }
            }
        }
    }

    public boolean isBlocking(EntityPlayer player) {
        return player.isUsingItem() && player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemSword && player.isBlocking();
    }

    private boolean isDamageBlocked(EntityPlayer player, DamageSource source, float[] blockAngles) {
        if (source.getEntity() == null) {
            return false;
        }

        float rawYaw = player.rotationYaw;
        float realYaw = (rawYaw + 90) % 360;
        if(realYaw < 0) realYaw += 360;

        double dx = source.getEntity().posX - player.posX;
        double dz = source.getEntity().posZ - player.posZ;
        float attackYaw = (float) Math.toDegrees(Math.atan2(dz, dx)) % 360;
        if (attackYaw < 0) attackYaw += 360;

        float diff = attackYaw - realYaw;
        diff = (diff + 360) % 360;

        return (diff <= blockAngles[1]) || (diff >= 360 - blockAngles[0]);
    }

}