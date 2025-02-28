package got.common.potions;

import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;

public class GOTPotionCombatlog extends GOTCustomPotion {

    public GOTPotionCombatlog(int id) {
        super(id, false, 9388224, "combatLogPotion");
        setPotionName("potion.got.combatLogPotion");
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
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
