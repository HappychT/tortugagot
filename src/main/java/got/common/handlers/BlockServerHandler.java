package got.common.handlers;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import got.common.database.GOTEffects;
import got.common.database.GOTRegistry;
import got.common.entity.other.GOTEntityCrossbowBolt;
import got.common.item.weapon.GOTItemShieldPike;
import got.common.item.weapon.GOTItemShieldSpear;
import got.common.item.weapon.cswords.*;
import got.common.network.GOTPacketHandler;
import got.common.network.PacketSyncWeaponHitCount;
import got.common.systems.GOTCoreBlockingSystem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
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
            if (event.source.getEntity() instanceof EntityPlayer) {
                EntityPlayer attacker = (EntityPlayer) event.source.getEntity();
                if (attacker.getHeldItem() != null && attacker.getHeldItem().getItem() instanceof ItemSword) {
                    StaminaServerHandler.drainStaminaByPercent(GOTCoreBlockingSystem.getBlockData(attacker.getHeldItem().getItem().getClass(), attacker).getStaminaAttackPercent() / 5.0, attacker);
                }
            }
            return;
        }

        EntityPlayer player = (EntityPlayer) event.entity;

        EntityPlayer attackerPlayer = null;
        if (event.source.getEntity() instanceof EntityPlayer) {
            attackerPlayer = (EntityPlayer) event.source.getEntity();
        }

        if (attackerPlayer != null) {
            ItemStack attackerWeapon = attackerPlayer.getHeldItem();
            if (attackerWeapon != null) {
                Item weapon = attackerWeapon.getItem();

                if (weapon instanceof ItemSword) {
                    StaminaServerHandler.drainStaminaByPercent(GOTCoreBlockingSystem.getBlockData(weapon.getClass(), player).getStaminaAttackPercent(), attackerPlayer);
                }

                if (weapon instanceof ItemPoisonedSandBlade) {
                    player.addPotionEffect(new PotionEffect(GOTEffects.neurotoxin.id, 160, 0));
                }

                if (weapon instanceof ItemNightKingSword) {
                    player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 120, 0));
                }

                if (weapon instanceof ItemLightbringer && attackerPlayer instanceof EntityPlayerMP) {
                    EntityPlayerMP attackerMP = (EntityPlayerMP) attackerPlayer;
                    NBTTagCompound nbt = attackerWeapon.getTagCompound();
                    if (nbt == null) {
                        nbt = new NBTTagCompound();
                        attackerWeapon.setTagCompound(nbt);
                    }

                    int hitCount = nbt.getInteger("LightbringerHits");
                    hitCount++;

                    if (hitCount >= 3) {
                        player.setFire(5);
                        if (world instanceof net.minecraft.world.WorldServer) {
                            ((net.minecraft.world.WorldServer) world).func_147487_a("flame", player.posX, player.posY + player.height / 2.0D, player.posZ, 30, player.width / 2.0D, player.height / 2.0D, player.width / 2.0D, 0.0D);
                            ((net.minecraft.world.WorldServer) world).func_147487_a("largesmoke", player.posX, player.posY + player.height / 2.0D, player.posZ, 15, player.width / 2.0D, player.height / 2.0D, player.width / 2.0D, 0.0D);
                        }
                        hitCount = 0;
                    }
                    nbt.setInteger("LightbringerHits", hitCount);
                    GOTPacketHandler.networkWrapper.sendTo(new PacketSyncWeaponHitCount(weapon, hitCount), attackerMP);
                }

                if (weapon instanceof ItemDecoyClaymore && attackerPlayer instanceof EntityPlayerMP) {
                    EntityPlayerMP attackerMP = (EntityPlayerMP) attackerPlayer;
                    NBTTagCompound nbt = attackerWeapon.getTagCompound();
                    if (nbt == null) {
                        nbt = new NBTTagCompound();
                        attackerWeapon.setTagCompound(nbt);
                    }

                    int count = nbt.getInteger("DecoyClaymoreHits");
                    count++;

                    if (count >= 3) {
                        player.addPotionEffect(new PotionEffect(GOTEffects.staminaLock.id, 120, 0));
                        count = 0;
                    }
                    nbt.setInteger("DecoyClaymoreHits", count);
                    GOTPacketHandler.networkWrapper.sendTo(new PacketSyncWeaponHitCount(weapon, count), attackerMP);
                }

                if (weapon instanceof ItemStunningHammer && attackerPlayer instanceof EntityPlayerMP) {
                    EntityPlayerMP attackerMP = (EntityPlayerMP) attackerPlayer;
                    NBTTagCompound nbt = attackerWeapon.getTagCompound();
                    if (nbt == null) {
                        nbt = new NBTTagCompound();
                        attackerWeapon.setTagCompound(nbt);
                    }

                    int count = nbt.getInteger("StunningHammerHits");
                    count++;

                    if (count >= 3) {
                        player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 120, 1));
                        count = 0;
                    }
                    nbt.setInteger("StunningHammerHits", count);
                    GOTPacketHandler.networkWrapper.sendTo(new PacketSyncWeaponHitCount(weapon, count), attackerMP);
                }
            }
        }

        if (isBlocking(player)) {
            float[] blockAngles = {
                    GOTCoreBlockingSystem.getBlockData(player.getHeldItem().getItem().getClass(), player).getLeftBlockAngle(),
                    GOTCoreBlockingSystem.getBlockData(player.getHeldItem().getItem().getClass(), player).getRightBlockAngle()
            };

            if (isDamageBlocked(player, event.source, blockAngles)) {

                int itemDamage = (attackerPlayer != null) ? 3 : 2;
                player.getHeldItem().damageItem(itemDamage, player);

                player.worldObj.playSoundAtEntity(player, "got:combat_block", 1, 1);
                event.setCanceled(true);
                player.addPotionEffect(new PotionEffect(GOTEffects.antiEffect.id, 1));

                if (attackerPlayer != null && attackerPlayer.getHeldItem() != null && attackerPlayer.getHeldItem().getItem() instanceof ItemSword) {
                    StaminaServerHandler.drainStaminaByPercent(GOTCoreBlockingSystem.getBlockData(attackerPlayer.getHeldItem().getItem().getClass(), player).getStaminaHitPercent(), player);
                } else {
                    StaminaServerHandler.drainStaminaByPercent(0.5, player);
                }

            } else {
                if (!event.source.isUnblockable()) {
                    event.ammount += event.ammount;
                }

                if (attackerPlayer != null) {
                    if (attackerPlayer.getHeldItem() != null && attackerPlayer.getHeldItem().getItem() instanceof ItemSword) {
                        StaminaServerHandler.drainStaminaByPercent(GOTCoreBlockingSystem.getBlockData(attackerPlayer.getHeldItem().getItem().getClass(), player).getStaminaHitPercent() / 5.0, player);
                    }
                    if (attackerPlayer.getHeldItem() != null && attackerPlayer.getHeldItem().getItem() == GOTRegistry.syrioForelSword) {
                        StaminaServerHandler.drainStaminaByPercent(3.0, player);
                        int amountToRegain = (int) (StaminaServerHandler.MAX_STAMINA * 0.03);
                        StaminaServerHandler.regainStamina(amountToRegain, attackerPlayer);
                    }
                } else {
                    StaminaServerHandler.drainStaminaByPercent(0.5, player);
                }
            }

        } else {
            if (attackerPlayer != null && attackerPlayer.getHeldItem() != null && attackerPlayer.getHeldItem().getItem() instanceof ItemSword) {
                StaminaServerHandler.drainStaminaByPercent(GOTCoreBlockingSystem.getBlockData(attackerPlayer.getHeldItem().getItem().getClass(), player).getStaminaHitPercent() / 5.0, player);
            }

            if (attackerPlayer == null && !event.source.isFireDamage() && !event.source.isMagicDamage()) {
                StaminaServerHandler.drainStaminaByPercent(0.5, player);
            }
        }

        if (event.source.isProjectile()) {
            if (event.source.getSourceOfDamage() instanceof EntityArrow) {
                StaminaServerHandler.drainStaminaByPercent(1, player);
            }
            if (event.source.getSourceOfDamage() instanceof GOTEntityCrossbowBolt)
                StaminaServerHandler.drainStaminaByPercent(2, player);
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