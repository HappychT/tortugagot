package got.common.handlers;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import got.common.database.GOTEffects;
import got.common.database.GOTRegistry;
import got.rome.ExtendedPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

import java.util.List;

public class AttackHandler {

    public static final AttackHandler INSTANCE = new AttackHandler();
    private static final int PARRY_WINDOW = 7;

    //@SubscribeEvent
    //public void onLivingAttacked(LivingAttackEvent event) {
       // if(event.source.getSourceOfDamage() instanceof EntityPlayer) {
        //    EntityPlayer attacker = (EntityPlayer) event.source.getSourceOfDamage();

            //if (ExtendedPlayer.get(attacker).getAttackCooldown() > 0) {
             //   event.setCanceled(true);
             //   DamageSource source = event.source;
             //   if (source instanceof EntityDamageSourceIndirect) {
             //       source.getSourceOfDamage();
              //  }
            //}
       // }


   // }
    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer) {
            if (PlayerParryData.get((EntityPlayer) event.entity) == null) {

                PlayerParryData.register((EntityPlayer) event.entity);
            }
        }
    }

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer defendingPlayer = (EntityPlayer) event.entityLiving;
            PlayerParryData parryData = PlayerParryData.get(defendingPlayer);

            if (parryData == null) return;

            if (parryData.isTryingToBlock() && defendingPlayer.getHeldItem() != null && defendingPlayer.getHeldItem().getItem() == GOTRegistry.syrioForelSword) {
                long blockStartTime = parryData.getBlockStartTime();
                long currentTime = defendingPlayer.worldObj.getTotalWorldTime();

                if (blockStartTime > 0 && (currentTime - blockStartTime) <= PARRY_WINDOW) {

                    if (event.source.getEntity() instanceof EntityLivingBase) {
                        EntityLivingBase attacker = (EntityLivingBase) event.source.getEntity();
                        attacker.attackEntityFrom(DamageSource.causePlayerDamage(defendingPlayer), 2.0F);
                    }

                    parryData.setLastSyrioCounterTime(defendingPlayer.worldObj.getTotalWorldTime());
                    parryData.setBlockStartTime(0);
                }
            }
        }
    }


    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (!(event.source.getEntity() instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) event.source.getEntity();
        ItemStack heldItem = player.getHeldItem();
        if (heldItem == null) return;
        if (player.getEntityData().hasKey("claymoreJumpAttack")) {
            player.getEntityData().removeTag("claymoreJumpAttack");
            return;
        }
        Item heldItemType = heldItem.getItem();
        EntityLivingBase target = event.entityLiving;
        if (heldItemType == GOTRegistry.decoyClaymore) {
            event.ammount *= 1.5F;
            player.onCriticalHit(target);
        }
        else if (heldItemType == GOTRegistry.stunningHammer) {
            if (target.isPotionActive(Potion.moveSlowdown.id)) {
                event.ammount += 2.0F;
            }
        } else if (heldItemType == GOTRegistry.iceStarkSword) {
            if (player.getHealth() < (player.getMaxHealth() * 0.25F)) {
                event.ammount += 2.0F;
            }
            if (target.getHealth() < 5.0F) {
                event.ammount += 1.0F;
            }
        } else if (heldItemType == GOTRegistry.nightkingSword) {
            if (!player.worldObj.isDaytime()) {
                event.ammount += 2.0F;
            }
        } else if (heldItemType == GOTRegistry.baratheonHammer) {
            if (player.fallDistance > 0.0F && !player.onGround) {
                ItemStack chestplate = target.getEquipmentInSlot(3);
                if (chestplate != null) {
                    chestplate.damageItem(25, player);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerAttack(AttackEntityEvent event) {
        EntityPlayer player = event.entityPlayer;
        ItemStack heldItem = player.getHeldItem();

        if (heldItem != null && heldItem.getItem() == GOTRegistry.decoyClaymore) {
            boolean isJumping = player.fallDistance > 0.0F && !player.onGround;

            if (isJumping) {
                event.setCanceled(true);
                player.fallDistance = 0.0F;

                if (event.target instanceof EntityLivingBase) {
                    EntityLivingBase livingTarget = (EntityLivingBase) event.target;

                    player.getEntityData().setBoolean("claymoreJumpAttack", true);

                    float damage = (float) player.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
                    livingTarget.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
                    livingTarget.addPotionEffect(new PotionEffect(Potion.weakness.id, 60, 0));
                }
            }
        }
    }
    @SubscribeEvent
    public void onLivingHeal(LivingHealEvent event) {
        if (event.entityLiving.isPotionActive(GOTEffects.neurotoxin)) {
            event.amount *= 0.4F;
        }
    }
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (!(event.source.getEntity() instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer killer = (EntityPlayer) event.source.getEntity();
        ItemStack heldItem = killer.getHeldItem();

        if (heldItem == null) {
            return;
        }

        if (heldItem.getItem() == GOTRegistry.baratheonHammer) {
            if (event.entityLiving instanceof EntityPlayer) {
                killer.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 100, 0));
                killer.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 100, 0));
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity instanceof EntityPlayer) {
            EntityPlayer ply = (EntityPlayer) event.entity;
            if(ExtendedPlayer.get(ply).getAttackCooldown() > 0) {
                ExtendedPlayer.get(ply).setAttackCooldown(ExtendedPlayer.get(ply).getAttackCooldown() - 1);
            }
        }

    }

}
