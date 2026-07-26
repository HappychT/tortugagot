package got.common.inventory;

import got.common.GOTLevelData;
import got.common.database.GOTAchievement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class GOTSlotAnvilOutput extends Slot {
	public GOTContainerAnvil theAnvil;

	public GOTSlotAnvilOutput(GOTContainerAnvil container, IInventory inv, int id, int i, int j) {
		super(inv, id, i, j);
		theAnvil = container;
	}

	@Override
	public boolean canTakeStack(EntityPlayer entityplayer) {
		if (getHasStack()) {
			if (theAnvil.materialCost > 0) {
				return theAnvil.hasMaterialOrCoinAmount(theAnvil.materialCost);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return false;
	}

	@Override
	public void onPickupFromSlot(EntityPlayer entityplayer, ItemStack itemstack) {
		if (!entityplayer.worldObj.isRemote) {
			got.rome.ExtendedPlayer ext = got.rome.ExtendedPlayer.get(entityplayer);
			if (ext != null && ext.isTutorialActive() && ext.getTutorialStage() == 6) {
				if (ext.getTutorialProgress() == 0 && itemstack.getItem() == got.common.database.GOTRegistry.bronzeChestplate) {
					brain.tutorial.TutorialManager.getInstance().advanceStage6(entityplayer, 1);
				} else if (ext.getTutorialProgress() == 1 && itemstack.getItem() == got.common.database.GOTRegistry.bronzeSword) {
					brain.tutorial.TutorialManager.getInstance().startStage7(entityplayer, ext);
				}
			}
		}

		int materials = theAnvil.materialCost;
		theAnvil.invInput.setInventorySlotContents(0, null);
		boolean wasSmithCombine = theAnvil.isSmithScrollCombine;
		ItemStack combinerItem = theAnvil.invInput.getStackInSlot(1);
		if (combinerItem != null) {
			--combinerItem.stackSize;
			if (combinerItem.stackSize <= 0) {
				theAnvil.invInput.setInventorySlotContents(1, null);
			} else {
				theAnvil.invInput.setInventorySlotContents(1, combinerItem);
			}
		}
		if (materials > 0) {
			theAnvil.takeMaterialOrCoinAmount(materials);
		}
		
		got.rome.ExtendedPlayer ext = got.rome.ExtendedPlayer.get(entityplayer);
		if (ext != null && ext.isTutorialActive() && ext.getTutorialStage() == 6 && ext.getTutorialProgress() == 1) {
		    brain.tutorial.TutorialManager.getInstance().startStage7(entityplayer, ext);
		}
		if (!entityplayer.worldObj.isRemote && wasSmithCombine) {
			GOTLevelData.getData(entityplayer).addAchievement(GOTAchievement.combineSmithScrolls);
		}
        if (!entityplayer.worldObj.isRemote) {
            // Unconditional Stage 6 skip removed here
        }
		theAnvil.materialCost = 0;
		theAnvil.isSmithScrollCombine = false;
		theAnvil.playAnvilSound();
	}
}
