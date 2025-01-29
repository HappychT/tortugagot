package got.common.potions;

import cpw.mods.fml.common.gameevent.PlayerEvent;
import got.common.registers.EffectRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;

public class CombatlogPotion extends Potion {

    public CombatlogPotion(int id, boolean isBadEffect, int color) {
        super(id, isBadEffect, color);
        setPotionName("potion.got.combatLogPotion");
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        System.out.println("sssss");
        EntityPlayer player = event.player;
        PotionEffect effect = player.getActivePotionEffect(this);
        if (effect != null) {
            // Kick the player with a message
            player.setHealth(0);
            // Log the kick to the server console
            System.out.println(player.getDisplayName() + " was kicked for leaving while the Potion of Killing is active.");
        }
    }


}
