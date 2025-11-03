package got.common.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import got.common.database.GOTRegistry;
import got.common.systems.AccessorySystem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class AccessoryEventHandler {


    @SubscribeEvent
    public void onPotionDrink(PlayerUseItemEvent.Finish event) {
        if (event.entityLiving instanceof EntityPlayer && event.item.getItem() instanceof ItemPotion) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;

            if (AccessorySystem.hasAccessory(player, GOTRegistry.alchemicalJars)) {
                ItemStack potionStack = event.item;
                List<PotionEffect> effects = ((ItemPotion) potionStack.getItem()).getEffects(potionStack);

                if (effects != null) {
                    for (PotionEffect effect : effects) {
                        PotionEffect doubledEffect = new PotionEffect(effect.getPotionID(), effect.getDuration() * 2, effect.getAmplifier());
                        player.addPotionEffect(doubledEffect);
                    }
                }
            }
        }
    }


    /*@SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && !event.player.worldObj.isRemote) {
            EntityPlayer player = event.player;

            if (player.ticksExisted % 20 == 0) {
                if (AccessorySystem.hasAccessory(player, GOTRegistry.gauzeSet)) {
                    Collection<PotionEffect> activeEffects = player.getActivePotionEffects();
                    if (activeEffects.isEmpty()) {
                        return;
                    }

                    PotionEffect toRemove = null;
                    for (PotionEffect effect : activeEffects) {
                        if (isPotionNegative(effect.getPotionID())) {
                            toRemove = effect;
                            break;
                        }
                    }

                    if (toRemove != null) {
                        player.removePotionEffect(toRemove.getPotionID());
                    }
                }
            }
        }
    *///}
    private boolean isPotionNegative(int potionId) {
        switch (potionId) {
            case 2:
            case 4:
            case 9:
            case 15:
            case 17:
            case 18:
            case 19:
            case 20:
                return true;
            default:
                return false;
        }
    }
}