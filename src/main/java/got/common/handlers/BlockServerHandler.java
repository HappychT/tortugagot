package got.common.handlers;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import got.common.database.GOTEffects;
import got.common.database.GOTRegistry;
import got.common.entity.other.GOTEntityCrossbowBolt;
import got.common.item.weapon.GOTItemShieldPike;
import got.common.item.weapon.GOTItemShieldSpear;
import got.common.systems.GOTCoreBlockingSystem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.Random;

public class BlockServerHandler {

    public static final BlockServerHandler INSTANCE = new BlockServerHandler();

    Random rand = new Random();

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onLivingHurt(LivingHurtEvent event) {

        World world = event.entityLiving.worldObj;

        if (!(event.entity instanceof EntityPlayer)) {
            if(event.source.getEntity() instanceof EntityPlayer) {
                EntityPlayer attacker = (EntityPlayer) event.source.getEntity();
                if (attacker.getHeldItem() != null && attacker.getHeldItem().getItem() instanceof ItemSword) {
                    StaminaServerHandler.drainStaminaByPercent(GOTCoreBlockingSystem.getBlockData(attacker.getHeldItem().getItem().getClass(), attacker).getStaminaAttackPercent() / 5.0, attacker);
                }
            }
        }

        if (!(event.entity instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) event.entity;

        if (event.source.getEntity() instanceof EntityPlayer) {
            EntityPlayer attacker = (EntityPlayer) event.source.getEntity();
            if (attacker.getHeldItem() != null && attacker.getHeldItem().getItem() instanceof ItemSword) {
                StaminaServerHandler.drainStaminaByPercent(GOTCoreBlockingSystem.getBlockData(attacker.getHeldItem().getItem().getClass(), player).getStaminaAttackPercent(), attacker);
            }
            if (isBlocking(player)) {
                float[] blockAngles = {
                        GOTCoreBlockingSystem.getBlockData(player.getHeldItem().getItem().getClass(), player).getLeftBlockAngle(),
                        GOTCoreBlockingSystem.getBlockData(player.getHeldItem().getItem().getClass(), player).getRightBlockAngle()
                };

                if (isDamageBlocked(player, event.source, blockAngles)) {
                    player.getHeldItem().damageItem(3, player);
                    player.worldObj.playSoundAtEntity(player, "got:combat_block", 1, 1);
                    event.setCanceled(true);
                    player.addPotionEffect(new PotionEffect(GOTEffects.antiEffect.id, 1));
                    if (attacker.getHeldItem() != null && attacker.getHeldItem().getItem() instanceof ItemSword) {
                        StaminaServerHandler.drainStaminaByPercent(GOTCoreBlockingSystem.getBlockData(attacker.getHeldItem().getItem().getClass(), player).getStaminaHitPercent(), player);
                    }
                } else {
                    if (!event.source.isUnblockable()) {
                        event.ammount += event.ammount;
                    }
                    if (attacker.getHeldItem() != null && attacker.getHeldItem().getItem() instanceof ItemSword) {
                        StaminaServerHandler.drainStaminaByPercent(GOTCoreBlockingSystem.getBlockData(attacker.getHeldItem().getItem().getClass(), player).getStaminaHitPercent() / 5.0, player);
                    }
                    if (attacker.getHeldItem() != null && attacker.getHeldItem().getItem() == GOTRegistry.syrioForelSword) {
                        StaminaServerHandler.drainStaminaByPercent(3.0, player);

                        int amountToRegain = (int) (StaminaServerHandler.MAX_STAMINA * 0.03);
                        StaminaServerHandler.regainStamina(amountToRegain, attacker);
                    }
                }

            } else {
                if (attacker.getHeldItem() != null && attacker.getHeldItem().getItem() instanceof ItemSword) {
                    StaminaServerHandler.drainStaminaByPercent(GOTCoreBlockingSystem.getBlockData(attacker.getHeldItem().getItem().getClass(), player).getStaminaHitPercent() / 5.0, player);
                }
            }

            if (event.source.isProjectile()) {
                if (event.source.getSourceOfDamage() instanceof EntityArrow) {
                    StaminaServerHandler.drainStaminaByPercent(1, player);
                }
                if (event.source.getSourceOfDamage() instanceof GOTEntityCrossbowBolt)
                    StaminaServerHandler.drainStaminaByPercent(2, player);
            }
        }
        
        if (!(event.source.getEntity() instanceof EntityPlayer)) {
            if (!event.source.isFireDamage() && !event.source.isMagicDamage()) {  //not a player,fire or magic

                if (isBlocking(player)) {
                    float[] blockAngles = {
                            GOTCoreBlockingSystem.getBlockData(player.getHeldItem().getItem().getClass(), player).getLeftBlockAngle(),
                            GOTCoreBlockingSystem.getBlockData(player.getHeldItem().getItem().getClass(), player).getRightBlockAngle()
                    };

                    if (isDamageBlocked(player, event.source, blockAngles)) {
                        player.getHeldItem().damageItem(2, player);
                        player.worldObj.playSoundAtEntity(player, "got:combat_block", 1, 1);
                        event.setCanceled(true);
                        player.addPotionEffect(new PotionEffect(GOTEffects.antiEffect.id, 1));
                    } else {
                        if (!event.source.isUnblockable()) {
                            event.ammount += event.ammount;
                        }
                    }
                }
                StaminaServerHandler.drainStaminaByPercent(0.5, player);
            }
        }





//        if (isBlocking(player)) {
//            float[] blockAngles = {
//                    GOTCoreBlockingSystem.getBlockData(player.getHeldItem().getItem().getClass(), player).getLeftBlockAngle(),
//                    GOTCoreBlockingSystem.getBlockData(player.getHeldItem().getItem().getClass(), player).getRightBlockAngle()
//            };
////            if(event.source.getEntity() instanceof EntityPlayer) {
////                EntityPlayer attacker = (EntityPlayer) event.source.getEntity();
////                if (attacker.getHeldItem() != null && attacker.getHeldItem().getItem() instanceof ItemSword) {
////                    StaminaServerHandler.drainStaminaByPercent(GOTCoreBlockingSystem.getBlockData(attacker.getHeldItem().getItem().getClass(), player).getStaminaMissPercent(), attacker);
////                }
////            }
//            if (isDamageBlocked(player, event.source, blockAngles)) {
//                player.getHeldItem().damageItem(3, player);
////                StaminaServerHandler.drainStaminaByPercent(3.0, player);
//                player.worldObj.playSoundAtEntity(player, "got:combat_block", 1, 1);
//                event.setCanceled(true);
//            } else {
////                StaminaServerHandler.drainStaminaByPercent(4.0, player);
//                if (event.source.getEntity() instanceof EntityLivingBase) {
//                    event.ammount += event.ammount;
//                }
//            }
//        } else {
//            if(event.source.getEntity() instanceof EntityPlayer) {
//                EntityPlayer attacker = (EntityPlayer) event.source.getEntity();
//                StaminaServerHandler.drainStaminaByPercent(1.5, player);
////                if (attacker.getHeldItem() != null && attacker.getHeldItem().getItem() instanceof ItemSword) {
////                    StaminaServerHandler.drainStaminaByPercent(GOTCoreBlockingSystem.getBlockData(attacker.getHeldItem().getItem().getClass(), player).getStaminaMissPercent(), attacker);
////                }
//            }
//        }
//        if (!(event.source.getEntity() instanceof EntityPlayer)) {
//            if (!event.source.isFireDamage() && event.source.isMagicDamage()) {  //not a player,fire or magic
//                StaminaServerHandler.drainStaminaByPercent(0.5, player);
//            }
//        }
//
//        if (event.source.getEntity() instanceof EntityPlayer) {
//            EntityPlayer attacker = (EntityPlayer) event.source.getEntity();
////            StaminaServerHandler.drainStaminaByPercent(1.5, player);
//            if (attacker.getHeldItem() != null && attacker.getHeldItem().getItem() instanceof ItemSword) {
//                StaminaServerHandler.drainStaminaByPercent(GOTCoreBlockingSystem.getBlockData(attacker.getHeldItem().getItem().getClass(), player).getStaminaMissPercent(), attacker);
//            }
//        }
    }

    public boolean isBlocking(EntityPlayer player) {
        return player.isUsingItem() && player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemSword && player.isBlocking();
    }

    private boolean isDamageBlocked(EntityPlayer player, DamageSource source, float[] blockAngles) {
        if (source.getEntity() == null) {
            return false;
        }

        if (source.isProjectile()) {
            if (player.getHeldItem().getItem() instanceof GOTItemShieldPike || player.getHeldItem().getItem() instanceof GOTItemShieldSpear) {
                int chance = rand.nextInt(3);
                if (chance == 0 || chance == 1) {
                    return false;
                }
            }
            else {
                int chance = rand.nextInt(4);
                if (chance == 0 || chance == 1 || chance == 2) {
                    return false;
                }
            }
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