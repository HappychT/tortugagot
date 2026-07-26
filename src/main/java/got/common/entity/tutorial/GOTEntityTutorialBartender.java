package got.common.entity.tutorial;

import brain.tutorial.TutorialManager;
import got.common.database.GOTInvasions;
import got.common.database.GOTTradeEntries;
import got.common.database.GOTUnitTradeEntries;
import got.common.entity.other.GOTBartender;
import got.common.entity.other.GOTTradeable;
import got.rome.ExtendedPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GOTEntityTutorialBartender extends GOTEntityTutorialNPC implements GOTBartender, GOTTradeable {

    public GOTEntityTutorialBartender(World world) {
        super(world);
    }

    @Override
    public boolean canTradeWith(EntityPlayer entityplayer) {
        if (entityplayer.worldObj.isRemote) {
            return brain.tutorial.client.TutorialClientState.isTutorialActive;
        } else {
            ExtendedPlayer ext = ExtendedPlayer.get(entityplayer);
            return ext != null && ext.isTutorialActive();
        }
    }

    @Override
    public GOTTradeEntries getBuyPool() {
        return GOTTradeEntries.C_BARTENDER_BUY; // Еда которую игрок может купить у трактирщика
    }

    @Override
    public GOTTradeEntries getSellPool() {
        return null; // Игрок ничего не продаёт трактирщику
    }

    @Override
    public GOTUnitTradeEntries getUnits() {
        return null;
    }

    @Override
    public GOTInvasions getWarhorn() {
        return null;
    }

    @Override
    public void onPlayerTrade(EntityPlayer entityplayer, GOTTradeEntries.TradeType type, ItemStack itemstack) {
        ExtendedPlayer ext = ExtendedPlayer.get(entityplayer);
        if (ext != null && ext.isTutorialActive() && ext.getTutorialStage() == 5) {
            TutorialManager.getInstance().advanceStage5(entityplayer, 1);
        }
    }

    @Override
    public void onUnitTrade(EntityPlayer entityplayer) {
    }
}
