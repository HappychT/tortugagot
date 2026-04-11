package got.common.faction;

import got.common.database.GOTRegistry;
import got.common.enchant.*;
import got.common.entity.GOTEnchaldBlacksmith;
import got.common.entity.other.GOTBlacksmithOffer;
import got.common.item.other.GOTItemArmor;
import got.common.item.other.GOTItemCoin;
import got.common.item.other.GOTItemFactionArmor;
import got.common.item.weapon.GOTFactionWeaponChecker;
import got.common.item.weapon.GOTItemSword;
import got.common.network.GOTPacketHandler;
import got.common.network.serverToClient.GOTPacketSyncInventory;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.*;

import java.util.Arrays;
import java.util.List;


public class GOTContainerFactionBlacksmith extends Container {
    private EntityPlayer entityplayer;
    private final GOTEnchantment[] enchantments;
    private GOTFaction faction;
    private GOTEnchaldBlacksmith.BlacksmithType type;
    public GOTEnchaldBlacksmith theBlacksmithNPC;
    public IInventory invInput;
    private ItemStack[] containerItemStacks = new ItemStack[9];
    ItemStack cursorStack = null;
    private int[] enchantmentsLevels;
    private int[] buttons;
    private final int enchantmentSlots;

    public GOTContainerFactionBlacksmith(EntityPlayer entityplayer, EntityCreature npc) {
        this.entityplayer = entityplayer;
        this.theBlacksmithNPC = (GOTEnchaldBlacksmith) npc;
        this.faction = theBlacksmithNPC.getFaction();
        this.type = theBlacksmithNPC.getBlacksmithType();
        this.enchantmentSlots = type.getSlots();
        this.buttons = new int[enchantmentSlots];
        this.enchantments = GOTBlacksmithOffer.getEnchantments(type);
        this.enchantmentsLevels = GOTBlacksmithOffer.getEnchantmentLevels(type);

        Arrays.fill(buttons, -1);

        this.invInput = new InventoryBasic("Repair", true, 1) {

            @Override
            public void markDirty() {
                super.markDirty();
                GOTContainerFactionBlacksmith.this.onCraftMatrixChanged(this);
            }
        };

        if (type != GOTEnchaldBlacksmith.BlacksmithType.RW)
            addSlotToContainer(new Slot(this.invInput, 0, 248, 332));
        else
            addSlotToContainer(new Slot(this.invInput, 0, 234, 229));

//        if (Minecraft.getMinecraft().gameSettings.guiScale == 0) {
//            for (int i = 0; i < 9; i++)
//                addSlotToContainer(new Slot(entityplayer.inventory, i, 152 + i * 24, 438 ));
//        }
        for (int i = 0; i < 9; i++)
            addSlotToContainer(new Slot(entityplayer.inventory, i, 152 + i * 24, 438));
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafting) {
        super.addCraftingToCrafters(crafting);
        theBlacksmithNPC.sendClientPacket((EntityPlayer) crafting);
    }

    public void takeUnlockItems(int slot) {
        InventoryPlayer playerInv = entityplayer.inventory;
        GOTBlacksmithOffer offer = GOTBlacksmithOffer.getUnlockOffer(slot, type).getUnlockCost(theBlacksmithNPC.getCoinMultiplier(), theBlacksmithNPC.getBlocksCounter());
        ItemStack itemstack;
        int cost;

        for (int i = 0; i < offer.getItems().length; i++) {
            itemstack = offer.getItems()[i];
            cost = (int) offer.getCosts()[i];
            if (i == 0)
                GOTItemCoin.takeCoins(cost, entityplayer);
            else {
                for (int j = 0; j < playerInv.mainInventory.length; j++) {
                    ItemStack is = playerInv.mainInventory[j];

                    if (is == null)
                        continue;

                    if (is.getItem() == itemstack.getItem()) {
                        if (cost >= is.stackSize) {
                            cost -= is.stackSize;
                            playerInv.mainInventory[j] = null;
                        } else {
                            playerInv.mainInventory[j].splitStack(cost);
                            break;
                        }
                    }
                }
            }
        }

        GOTPacketHandler.networkWrapper.sendTo(new GOTPacketSyncInventory(playerInv), (EntityPlayerMP) entityplayer);
    }

    public void takeRepairItems() {
        ItemStack itemstack = invInput.getStackInSlot(0);

        int repairCost = 0;
        int itemMaxDamage = itemstack.getMaxDamage();
        int currentDurability = itemMaxDamage - itemstack.getItemDamage();

        if (currentDurability <= itemMaxDamage * 0.25F)
            repairCost = 6;
        else if (currentDurability <= itemMaxDamage * 0.50F)
            repairCost = 4;
        else if (currentDurability <= itemMaxDamage * 0.75F)
            repairCost = 2;
        else if (currentDurability < itemMaxDamage)
            repairCost = 1;

        ItemStack[] mainInventory = entityplayer.inventory.mainInventory;
        for (int i = 0; i < mainInventory.length; i++) {

            ItemStack is = mainInventory[i];
            if (is == null)
                continue;

            if (is.getItem() == GOTRegistry.alloySteelIngot) {
                if (repairCost >= is.stackSize) {
                    repairCost -= is.stackSize;
                    mainInventory[i] = null;
                } else {
                    mainInventory[i].splitStack(repairCost);
                }

            }
        }
        GOTPacketHandler.networkWrapper.sendTo(new GOTPacketSyncInventory(entityplayer.inventory), (EntityPlayerMP) entityplayer);
    }

    public void takeEnchantItems(EntityPlayer entityplayer) {
        InventoryPlayer playerInv = entityplayer.inventory;
        int[] costs = {0, 0};
        ItemStack[] itemstacks = GOTBlacksmithOffer.getEnchantmentPrice(0).getItems();
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == 0) {
                for (int j = 0; j < itemstacks.length; j++) {
                    costs[j] += (int) GOTBlacksmithOffer.getEnchantmentPrice(enchantmentsLevels[i] - 1).getCosts()[j];
                }
            }
        }

        ItemStack itemstack;
        int cost;
        for (int i = 0; i < itemstacks.length; i++) {
            itemstack = itemstacks[i];

            if (itemstack == null)
                continue;

            cost = costs[i];

            if (i == 0)
                GOTItemCoin.takeCoins(costs[0], entityplayer);
            else {
                for (int j = 0; j < playerInv.mainInventory.length; j++) {
                    ItemStack is = playerInv.mainInventory[j];
                    if (is == null)
                        continue;

                    if (is.getItem() == itemstack.getItem()) {
                        if (cost >= is.stackSize) {
                            cost -= is.stackSize;
                            playerInv.mainInventory[j] = null;
                        } else {
                            playerInv.mainInventory[j].splitStack(cost);
                            break;
                        }
                    }
                }
            }
        }
        GOTPacketHandler.networkWrapper.sendTo(new GOTPacketSyncInventory(entityplayer.inventory), (EntityPlayerMP) entityplayer);
    }
    
    public void applyWeaponEnchantments() {
        if (invInput.getStackInSlot(0) == null)
            return;

        ItemStack inputCopy = invInput.getStackInSlot(0).copy();
        List<GOTEnchantment> itemEnchantments = GOTEnchantmentHelper.getEnchantList(inputCopy);
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == 0) {
                GOTEnchantment choosenEnch = enchantments[i];
                for (GOTEnchantment itemEnch : itemEnchantments) {

                    if (itemEnch.getClass() == choosenEnch.getClass()) {
                        GOTEnchantmentHelper.removeEnchant(inputCopy, itemEnch);
                    }
                }
                GOTEnchantmentHelper.addEnchant(inputCopy, choosenEnch);
                invInput.setInventorySlotContents(0, inputCopy);
            }
        }
    }

    public void renameItem(String name) {
        if (invInput.getStackInSlot(0) == null)
            return;
        ItemStack inputCopy = invInput.getStackInSlot(0).copy();
        inputCopy.setStackDisplayName(name);
        invInput.setInventorySlotContents(0, inputCopy);
    }

    public void switchButtonState(int index) {
        buttons[index] = buttons[index] == 0 ? -1 : 0;
    }

    public int countItemEnchantments() {
        if (invInput.getStackInSlot(0) != null)
            return GOTEnchantmentHelper.getEnchantList(invInput.getStackInSlot(0)).size();
        return 0;
    }

    public boolean detectInventoryInteraction() {
        boolean changed = false;
        ItemStack newCursorStack = entityplayer.inventory.getItemStack();
        ItemStack oldCursorStack = cursorStack;
        if (newCursorStack != oldCursorStack && (newCursorStack == null || oldCursorStack == null || newCursorStack.stackSize != oldCursorStack.stackSize || newCursorStack.getItem() != oldCursorStack.getItem())) {
            cursorStack = newCursorStack != null ? newCursorStack.copy() : null;
            return true;
        }
        for (int i = 0; i < 9; i++) {
            ItemStack newStack = ((Slot) inventorySlots.get(i + 1)).getStack();
            ItemStack oldStack = containerItemStacks[i];
            if (newStack != oldStack && (newStack == null || oldStack == null || newStack.stackSize != oldStack.stackSize || newStack.getItem() != oldStack.getItem())) {
                containerItemStacks[i] = newStack != null ? newStack.copy() : null;
                changed = true;
            }
        }
        return changed;
    }

    public boolean canAffordAndApplyRepair() {
        if (invInput.getStackInSlot(0) == null)
            return false;

        ItemStack itemstack = invInput.getStackInSlot(0);
        Item item = itemstack.getItem();
        switch (type) {
            case MW:
                if (!(item instanceof GOTItemSword))
                    return false;
                break;
            case RW:
                if (!(item instanceof ItemBow))
                    return false;
                break;
            case ARM:
                if (!(item instanceof GOTItemArmor))
                    return false;
                break;
            case TOOL:
                if (!(item instanceof ItemTool))
                    return false;
                break;
            default:
                return false;
        }

        if (!itemstack.isItemStackDamageable() || itemstack.getMaxDamage() - itemstack.getItemDamage() >= itemstack.getMaxDamage()) {
            return false;
        }

        int repairCost = 0;
        int itemMaxDamage = itemstack.getMaxDamage();
        int currentDurability = itemMaxDamage - itemstack.getItemDamage();
        if (currentDurability <= itemMaxDamage * 0.25F)
            repairCost = 6;
        else if (currentDurability <= itemMaxDamage * 0.50F)
            repairCost = 4;
        else if (currentDurability <= itemMaxDamage * 0.75F)
            repairCost = 2;
        else if (currentDurability < itemMaxDamage)
            repairCost = 1;

        int amount = 0;
        ItemStack[] mainInventory = entityplayer.inventory.mainInventory;
        for (ItemStack is : mainInventory) {

            if (is == null)
                continue;

            if (is.getItem() == GOTRegistry.alloySteelIngot) {
                amount += is.stackSize;

                if (amount >= repairCost)
                    return true;
            }

        }
        return false;
    }

    public boolean canAffordToUnlockSlot(EntityPlayer entityplayer, int slot) {
        InventoryPlayer playerInv = entityplayer.inventory;
        GOTBlacksmithOffer offer = GOTBlacksmithOffer.getUnlockOffer(slot, type).getUnlockCost(theBlacksmithNPC.getCoinMultiplier(), theBlacksmithNPC.getBlocksCounter());
        ItemStack is;
        int cost;

        boolean canUnlock = true;
        for (int i = 0; i < offer.getItems().length; i++) {
            is = offer.getItems()[i];
            cost = (int) offer.getCosts()[i];
            int amount = 0;
            if (i == 0) {
                canUnlock = GOTItemCoin.getInventoryValue(entityplayer, false) >= cost;
            } else {
                if (canUnlock) {
                    canUnlock = false;
                    for (ItemStack itemstack : playerInv.mainInventory) {
                        if (itemstack == null)
                            continue;

                        if (itemstack.getItem() == is.getItem())
                            amount += itemstack.stackSize;

                        if (amount >= cost) {
                            canUnlock = true;
                            break;
                        }
                    }
                }
            }
        }
        return canUnlock;
    }

    public boolean canAffordToEnchant(EntityPlayer entityplayer, int[] buttons) {
        InventoryPlayer playerInv = entityplayer.inventory;
        int[] costs = {0, 0};
        ItemStack[] itemstacks = GOTBlacksmithOffer.getEnchantmentPrice(0).getItems();
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == 0) {
                for (int j = 0; j < itemstacks.length; j++) {
                    costs[j] += (int) GOTBlacksmithOffer.getEnchantmentPrice(enchantmentsLevels[i] - 1).getCosts()[j];
                }
            }
        }

        int amount;
        boolean canAfford = true;
        for (int i = 0; i < itemstacks.length; i++) {
            if (itemstacks[i] == null) {
                continue;
            }
            amount = 0;
            if (i == 0)
                canAfford = GOTItemCoin.getInventoryValue(entityplayer, false) >= costs[0];
            else {
                if (canAfford) {
                    canAfford = false;
                    for (ItemStack itemstack : playerInv.mainInventory) {

                        if (itemstack == null)
                            continue;

                        if (itemstacks[i].getItem() == itemstack.getItem())
                            amount += itemstack.stackSize;

                        if (amount >= costs[i]) {
                            canAfford = true;
                            break;
                        }
                    }
                }
            }
        }
        return canAfford;
    }

    public boolean canApplyWeaponEnchantments(int enchantSlot) {
        ItemStack inputItem = invInput.getStackInSlot(0);
        Item item = inputItem.getItem();

        switch (type) {
            case MW:
                if (!(item instanceof GOTItemSword))
                    return false;
                if (item instanceof GOTFactionWeaponChecker && ((GOTFactionWeaponChecker) item).getFaction() != theBlacksmithNPC.getFaction())
                    return false;
                break;
            case RW:
                if (!(item instanceof ItemBow))
                    return false;
                if (item instanceof GOTFactionWeaponChecker && ((GOTFactionWeaponChecker) item).getFaction() != theBlacksmithNPC.getFaction())
                    return false;
                break;
            case ARM:
                if (!(item instanceof GOTItemArmor))
                    return false;
                if (item instanceof GOTItemFactionArmor && ((GOTItemFactionArmor) item).faction != theBlacksmithNPC.getFaction())
                    return false;
                break;
            case TOOL:
                if (!(item instanceof ItemTool))
                    return false;
                break;
            default:
                break;
        }

        List<GOTEnchantment> itemEnchantments = GOTEnchantmentHelper.getEnchantList(inputItem);

        GOTEnchantment ench = enchantments[enchantSlot];

        for (GOTEnchantment itemEnch : itemEnchantments) {
            if (ench == itemEnch)
                return false;

            if (itemEnch.getClass() == ench.getClass() && !(ench.getEnchantWeight() < itemEnch.getEnchantWeight())) {
                return false;
            }
        }

        if (type == GOTEnchaldBlacksmith.BlacksmithType.ARM && ench.isWeaponProtection()) {
            for (int i = 0; i < itemEnchantments.size(); i++) {
                if (itemEnchantments.get(i).isWeaponProtection() && itemEnchantments.get(i).getClass() != ench.getClass())
                    return false;
            }
        }

        return ench.canApply(inputItem, false);
    }

    public boolean canRename(ItemStack is) {
        Item item = is.getItem();

        switch (type) {
            case MW:
                if (!(item instanceof GOTItemSword))
                    return false;
                if (item instanceof GOTFactionWeaponChecker && ((GOTFactionWeaponChecker) item).getFaction() != theBlacksmithNPC.getFaction())
                    return false;
                break;
            case RW:
                if (!(item instanceof ItemBow))
                    return false;
                if (item instanceof GOTFactionWeaponChecker && ((GOTFactionWeaponChecker) item).getFaction() != theBlacksmithNPC.getFaction())
                    return false;
                break;
            case ARM:
                if (!(item instanceof GOTItemArmor))
                    return false;
                if (item instanceof GOTItemFactionArmor && ((GOTItemFactionArmor) item).faction != theBlacksmithNPC.getFaction())
                    return false;
                break;
            case TOOL:
                if (!(item instanceof ItemTool))
                    return false;
                break;
            default:
                break;
        }
        
        return true;
    }

    public boolean isSlotUnlocked(int i) {
        return theBlacksmithNPC.isSlotUnlocked(i);
    }

    private boolean areMaxSlotsUnlocked() {
        for (int i = 0; i < enchantmentSlots; i++) {
            if (enchantmentsLevels[i] == 3 && !isSlotUnlocked(i))
                return false;
        }
        return true;
    }

    public boolean isAnyButtonSelected() {
        for (int i = 0; i < 9; i++) {
            if (buttons[i] == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean canUnlockSlot(int slot) {
        return enchantmentsLevels[slot] == 1 || (enchantmentsLevels[slot] == 2 && isSlotUnlocked(slot - 1)) || (enchantmentsLevels[slot] == 3 && isSlotUnlocked(slot - 1) || areMaxSlotsUnlocked());
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer) {
        super.onContainerClosed(entityplayer);
        if (!entityplayer.worldObj.isRemote) {
            ItemStack itemstack = this.invInput.getStackInSlotOnClosing(0);
            if (itemstack != null)
                entityplayer.dropPlayerItemWithRandomChoice(itemstack, false);
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityplayer, int i) {
//        запутанная механика с багами. нужно будет переделать
//        ItemStack itemstack = null;
//        Slot slot = (Slot) this.inventorySlots.get(i);
//        if (slot != null && slot.getHasStack()) {
//            ItemStack itemstack1 = slot.getStack();
//            itemstack = itemstack1.copy();
//            if (i == 0) {
//                if (!mergeItemStack(itemstack1, 1, 10, true))
//                    return null;
//                slot.onSlotChange(itemstack1, itemstack);
//            } else if (i >= 1 ? i < 10 && !mergeItemStack(itemstack1, 0, 1, false) : !mergeItemStack(itemstack1, 1, 10, false))
//                return null;
//            if (itemstack1.stackSize == 0) {
//                System.out.println("ya tut");
//                slot.putStack(null);
//            } else {
//                slot.onSlotChanged();
//            }
//            if (itemstack1.stackSize == itemstack.stackSize)
//                return null;
//            slot.onPickupFromSlot(entityplayer, itemstack1);
//        }
//        return itemstack;
        return null;
    }
}
