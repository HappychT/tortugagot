package got.common.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import got.rome.ExtendedPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.List;

public class AttackHandler {

    public static final AttackHandler INSTANCE = new AttackHandler();

    @SubscribeEvent
    public void onLivingAttacked(LivingAttackEvent event) {
        if(event.source.getSourceOfDamage() instanceof EntityPlayer) {
            EntityPlayer attacker = (EntityPlayer) event.source.getSourceOfDamage();

            if (ExtendedPlayer.get(attacker).getAttackCooldown() > 0) {
                event.setCanceled(true);
                DamageSource source = event.source;
                if (source instanceof EntityDamageSourceIndirect) {
                    source.getSourceOfDamage();
                }
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
