package got.common.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import got.common.database.GOTEffects;
import got.common.enchant.GOTEnchantment;
import got.common.enchant.GOTEnchantmentHelper;
import got.common.network.base.PacketDispatcher;
import got.common.network.serverToClient.PacketSendBounceCooldown;
import got.common.network.serverToClient.PacketSendSecondBreathCooldown;
import got.common.network.serverToClient.PacketSendStamina;
import got.rome.ExtendedPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;

public class StaminaServerHandler {

    public static final StaminaServerHandler INSTANCE = new StaminaServerHandler();

    public static final int MAX_STAMINA = 10000; // Maximum stamina, increase if needed

    private static final int BOUNCE_RANGE = 1; // The range for the bounce, change to your liking
    private static final int BOUNCE_PERCENT = 3; // The percentage of stamina to drain when bouncing, change to your liking
    private static final int REGAIN_RATE = 100; // The rate at which stamina is regained, change to your liking
    private static final int STANDING_STILL_COOLDOWN = 25; // The cooldown before stamina regen, when standing in-place, change to your liking
    private static final int SECONDBREATH_COOLDWON = 6000;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {

        if (event.phase != TickEvent.Phase.START)
            return;

        EntityPlayer player = event.player;
        if (player.worldObj.isRemote)
            return;

        ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
        boolean isRunning = player.isSprinting();
        boolean isJumping = player.motionY > 0 && !player.onGround;

        double currentPosX = player.posX;
        double currentPosZ = player.posZ;
        double previousPosX = extendedPlayer.getPreviousPosX();
        double previousPosZ = extendedPlayer.getPreviousPosZ();

        boolean isMoving = currentPosX != previousPosX || currentPosZ != previousPosZ;

        if (isMoving) {
            if (isRunning) {
                drainStaminaByPercent(0.025, player);
            }
            extendedPlayer.setStandingStillCooldown(STANDING_STILL_COOLDOWN);
        }

        if (isJumping) {
            drainStaminaByPercent(0.3, player);
            extendedPlayer.setStandingStillCooldown(STANDING_STILL_COOLDOWN);
        }

        if (extendedPlayer.getStandingStillCooldown() > 0 && !isMoving && !isJumping) {
            extendedPlayer.setStandingStillCooldown(extendedPlayer.getStandingStillCooldown() - 1);
        }

        if (player.isPotionActive(GOTEffects.rest)) {
            if (player.getCurrentArmor(0) != null && GOTEnchantmentHelper.hasEnchant(player.getCurrentArmor(0), GOTEnchantment.restBuff)) { // Resting potion effect stamina regen, stacks with normal regen
                regainStamina(15, player);
            }
            else { // Resting potion effect stamina regen, stacks with normal regen
                regainStamina(REGAIN_RATE / 10, player); // May be too OP, adjust to your liking
            }
        }

        if (!isMoving && player.onGround && !isJumping && extendedPlayer.getStandingStillCooldown() == 0) {
            regainStamina(REGAIN_RATE, player);
        }

        // Decrease bounce cooldown
        if (extendedPlayer.getBounceCooldown() > 0) {
            extendedPlayer.setBounceCooldown(extendedPlayer.getBounceCooldown() - 1);
            PacketDispatcher.sendTo(new PacketSendBounceCooldown(extendedPlayer.getBounceCooldown()), (EntityPlayerMP) player);
        }

        if (extendedPlayer.getSecondBreathCooldown() > 0) {
            extendedPlayer.setSecondBreathCooldown(extendedPlayer.getSecondBreathCooldown() - 1);
            PacketDispatcher.sendTo(new PacketSendSecondBreathCooldown(extendedPlayer.getSecondBreathCooldown()), (EntityPlayerMP) player);
        }

        if (extendedPlayer.getStamina() == 0) {
            if (player.isPotionActive(GOTEffects.secondBreath) && extendedPlayer.getSecondBreathCooldown() == 0) {
                regainStamina((int) (MAX_STAMINA * 0.1), player);
                player.removePotionEffect(GOTEffects.secondBreath.id);
                extendedPlayer.setSecondBreathCooldown(SECONDBREATH_COOLDWON);
                PacketDispatcher.sendTo(new PacketSendSecondBreathCooldown(extendedPlayer.getSecondBreathCooldown()), (EntityPlayerMP) player);
            } else {
                player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 20, 0, true));
                // Oops, I added my own effect accidentally, but you can use whatever suits your needs, mine does not effect the dig speed, only attack speed and block degrees
                // But I've added requested functionality anyway to the vanilla potion effect
                //.addPotionEffect(new PotionEffect(GOTEffects.exhaustion.id, 20, 0, true));
                player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 20, 0, true));
            }
        } else {
            //player.removePotionEffect(Potion.moveSlowdown.id);
        }

        extendedPlayer.setPreviousPosX(currentPosX);
        extendedPlayer.setPreviousPosZ(currentPosZ);

    }

    public void handleBounceRequest(EntityPlayer player, int direction) {
        if (!player.onGround)
            return;

        ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);

        if (extendedPlayer.getBounceCooldown() > 0)
            return;

        if (extendedPlayer.getStamina() < (MAX_STAMINA * (BOUNCE_PERCENT / 100.0)))
            return;

        if (direction != 0) {
            executeBounce(player, direction);
        }
    }

    private void executeBounce(EntityPlayer player, int direction) {
        double bounceRange = BOUNCE_RANGE; // Example bounce range
        double motionX = 0;
        double motionZ = 0;
        double motionY = 0.5; // Adding a vertical component

        switch (direction) {
            case 1: //"backward"
                motionX = Math.sin(Math.toRadians(player.rotationYaw)) * bounceRange;
                motionZ = -Math.cos(Math.toRadians(player.rotationYaw)) * bounceRange;
                break;
            case 2: //"right"
                motionX = -Math.cos(Math.toRadians(player.rotationYaw)) * bounceRange;
                motionZ = -Math.sin(Math.toRadians(player.rotationYaw)) * bounceRange;
                break;
            case 3: //"left"
                motionX = Math.cos(Math.toRadians(player.rotationYaw)) * bounceRange;
                motionZ = Math.sin(Math.toRadians(player.rotationYaw)) * bounceRange;
                break;
        }

        // Apply motion over a few ticks for smoothness
        int duration = 5; // Number of ticks over which to apply the motion
        for (int i = 0; i < duration; i++) {
            player.motionX += motionX / duration;
            player.motionZ += motionZ / duration;
            player.motionY += motionY / duration;
        }
        player.velocityChanged = true; // Ensure the server updates the player's velocity

        ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
        drainStaminaByPercent(BOUNCE_PERCENT, player);
        extendedPlayer.setBounceCooldown(20 * 3); // Set cooldown for bounce
        PacketDispatcher.sendTo(new PacketSendBounceCooldown(extendedPlayer.getBounceCooldown()), (EntityPlayerMP) player);

        player.worldObj.playSoundAtEntity(player, "got:combat_bounce", 1, 1);
    }

    public static void drainStamina(int amount, EntityPlayer player) {
        if(player.capabilities.isCreativeMode)
            return;

        ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
        int dexterityLevel = player.getActivePotionEffect(GOTEffects.dexterity) != null ? player.getActivePotionEffect(GOTEffects.dexterity).getAmplifier() + 1 : 0;
        double reductionFactor = 1.0 - (0.1 * Math.min(dexterityLevel, 3));
        int adjustedAmount = (int) (amount * reductionFactor);
        extendedPlayer.setStamina(Math.max(0, extendedPlayer.getStamina() - adjustedAmount));
        PacketDispatcher.sendTo(new PacketSendStamina(extendedPlayer.getStamina()), (EntityPlayerMP) player);
    }

    public static void drainStaminaByPercent(double percent, EntityPlayer player) {
        if(player.capabilities.isCreativeMode)
            return;

        ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
        int dexterityLevel = player.getActivePotionEffect(GOTEffects.dexterity) != null ? player.getActivePotionEffect(GOTEffects.dexterity).getAmplifier() + 1 : 0;
        double reductionFactor = 1.0 - (0.1 * Math.min(dexterityLevel, 3));
        int amountToDrain = (int) Math.round((MAX_STAMINA * (percent / 100.0) * reductionFactor));
        extendedPlayer.setStamina(Math.max(0, extendedPlayer.getStamina() - amountToDrain));
        PacketDispatcher.sendTo(new PacketSendStamina(extendedPlayer.getStamina()), (EntityPlayerMP) player);
    }

    public static void regainStamina(int amount, EntityPlayer player) {
        ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
        extendedPlayer.setStamina(Math.min(MAX_STAMINA, extendedPlayer.getStamina() + amount));
        PacketDispatcher.sendTo(new PacketSendStamina(extendedPlayer.getStamina()), (EntityPlayerMP) player);
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (event.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            if(player.worldObj.isRemote)
                return;
            DamageSource source = event.source;

            if (source == DamageSource.fall) {
                drainStaminaByPercent(0.1, player);
            } else if (source.isProjectile()) {
                drainStaminaByPercent(0.33, player);
            }
        }
    }

    @SubscribeEvent
    public void onArrowLoose(ArrowLooseEvent event) {
        EntityPlayer player = event.entityPlayer;
        if(player.worldObj.isRemote)
            return;
        drainStaminaByPercent(0.4, player);
    }

}
