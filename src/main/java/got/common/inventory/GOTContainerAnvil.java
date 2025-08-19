package got.common.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.GOT;
import got.common.GOTConfig;
import got.common.GOTLevelData;
import got.common.database.GOTAchievement;
import got.common.database.GOTMaterial;
import got.common.database.GOTRegistry;
import got.common.database.GOTTradeEntries;
import got.common.enchant.GOTEnchantment;
import got.common.enchant.GOTEnchantmentCombining;
import got.common.enchant.GOTEnchantmentHelper;
import got.common.entity.other.GOTEntityNPC;
import got.common.entity.other.GOTTradeEntry;
import got.common.entity.other.GOTTradeable;
import got.common.entity.westeros.GOTEntityWesterosScrapTrader;
import got.common.item.AnvilNameColorProvider;
import got.common.item.GOTMaterialFinder;
import got.common.item.other.GOTItemArmor;
import got.common.item.other.GOTItemChisel;
import got.common.item.other.GOTItemCoin;
import got.common.item.other.GOTItemEnchantment;
import got.common.item.other.GOTItemModifierTemplate;
import got.common.item.other.GOTItemOwnership;
import got.common.item.weapon.GOTItemCrossbow;
import got.common.item.weapon.GOTItemSarbacane;
import got.common.item.weapon.GOTItemSword;
import got.common.item.weapon.GOTItemThrowingAxe;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class GOTContainerAnvil extends Container {
    public static int maxReforgeTime = 40;
    public IInventory invOutput;
    public IInventory invInput;
    public EntityPlayer thePlayer;
    public World theWorld;
    public boolean isTrader;
    public int xCoord;
    public int yCoord;
    public int zCoord;
    public GOTEntityNPC theNPC;
    public GOTTradeable theTrader;
    public int materialCost;
    public int reforgeCost;
    public int engraveOwnerCost;
    public String repairedItemName;
    public long lastReforgeTime = -1L;
    public int clientReforgeTime;
    public boolean doneMischief;
    public boolean isSmithScrollCombine;

    public GOTContainerAnvil(EntityPlayer entityplayer, boolean trader) {
        this.thePlayer = entityplayer;
        this.theWorld = entityplayer.worldObj;
        this.isTrader = trader;
        this.invOutput = new InventoryCraftResult();
        this.invInput = new InventoryBasic("Repair", true, this.isTrader ? 2 : 3) {

            @Override
            public void markDirty() {
                super.markDirty();
                GOTContainerAnvil.this.onCraftMatrixChanged(this);
            }
        };
        addSlotToContainer(new Slot(this.invInput, 0, 27, 58));
        addSlotToContainer(new Slot(this.invInput, 1, 76, 47));
        if (!this.isTrader) {
            addSlotToContainer(new Slot(this.invInput, 2, 76, 70));
        }
        addSlotToContainer(new GOTSlotAnvilOutput(this, this.invOutput, 0, 134, 58));
        for (int j1 = 0; j1 < 3; ++j1) {
            for (int i1 = 0; i1 < 9; ++i1) {
                addSlotToContainer(new Slot(entityplayer.inventory, i1 + j1 * 9 + 9, 8 + i1 * 18, 116 + j1 * 18));
            }
        }
        for (int i1 = 0; i1 < 9; ++i1) {
            addSlotToContainer(new Slot(entityplayer.inventory, i1, 8 + i1 * 18, 174));
        }
    }

    public GOTContainerAnvil(EntityPlayer entityplayer, GOTEntityNPC npc) {
        this(entityplayer, true);
        this.theNPC = npc;
        this.theTrader = (GOTTradeable) npc;
    }

    public GOTContainerAnvil(EntityPlayer entityplayer, int i, int j, int k) {
        this(entityplayer, false);
        this.xCoord = i;
        this.yCoord = j;
        this.zCoord = k;
    }

    public boolean applyMischief(ItemStack itemstack) {
        boolean changed = false;
        Random rand = this.theWorld.rand;
        if (rand.nextFloat() < 0.8f) {
            String name = itemstack.getDisplayName();
            if ((name = OddmentCollectorNameMischief.garbleName(name, rand)).equals(itemstack.getItem().getItemStackDisplayName(itemstack))) {
                itemstack.func_135074_t();
            } else {
                itemstack.setStackDisplayName(name);
            }
            changed = true;
        }
        if (rand.nextFloat() < 0.2f) {
            GOTEnchantmentHelper.applyRandomEnchantments(itemstack, rand, false, true);
            changed = true;
        }
        return changed;
    }

    public boolean canEngraveNewOwner(ItemStack itemstack, EntityPlayer entityplayer) {
        String currentOwner = GOTItemOwnership.getCurrentOwner(itemstack);
        if (currentOwner == null)
            return true;
        return !currentOwner.equals(entityplayer.getCommandSenderName());
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        if (this.isTrader)
            return this.theNPC != null && entityplayer.getDistanceToEntity(this.theNPC) <= 12.0 && this.theNPC.isEntityAlive() && this.theNPC.getAttackTarget() == null && this.theTrader.canTradeWith(entityplayer);
        return this.theWorld.getBlock(this.xCoord, this.yCoord, this.zCoord) == Blocks.anvil && entityplayer.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) <= 64.0;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (Object crafter : this.crafters) {
            ICrafting crafting = (ICrafting) crafter;
            crafting.sendProgressBarUpdate(this, 0, this.materialCost);
            crafting.sendProgressBarUpdate(this, 1, this.reforgeCost);
            crafting.sendProgressBarUpdate(this, 3, this.engraveOwnerCost);
        }
    }

    public void engraveOwnership() {
        ItemStack inputItem = this.invInput.getStackInSlot(0);
        if (inputItem != null && this.engraveOwnerCost > 0 && hasMaterialOrCoinAmount(this.engraveOwnerCost)) {
            int cost = this.engraveOwnerCost;
            GOTItemOwnership.setCurrentOwner(inputItem, this.thePlayer.getCommandSenderName());
            if (this.isTrader && this.theNPC instanceof GOTEntityWesterosScrapTrader && applyMischief(inputItem)) {
                this.doneMischief = true;
            }
            this.invInput.setInventorySlotContents(0, inputItem);
            takeMaterialOrCoinAmount(cost);
            playAnvilSound();
            GOTLevelData.getData(this.thePlayer).addAchievement(GOTAchievement.engraveOwnership);
        }
    }

    public List<EnumChatFormatting> getActiveItemNameFormatting() {
        ItemStack inputItem = this.invInput.getStackInSlot(0);
        ItemStack resultItem = this.invOutput.getStackInSlot(0);
        if (resultItem != null)
            return GOTContainerAnvil.getAppliedFormattingCodes(resultItem.getDisplayName());
        if (inputItem != null)
            return GOTContainerAnvil.getAppliedFormattingCodes(inputItem.getDisplayName());
        return new ArrayList<>();
    }

    public float getTraderMaterialPrice(ItemStack inputItem) {
        float materialPrice = 0.0f;
        GOTTradeEntry[] sellTrades = this.theNPC.traderNPCInfo.getSellTrades();
        if (sellTrades != null) {
            for (GOTTradeEntry trade : sellTrades) {
                ItemStack tradeItem = trade.createTradeItem();
                if (!isRepairMaterial(inputItem, tradeItem)) {
                    continue;
                }
                materialPrice = (float) trade.getCost() / (float) trade.createTradeItem().stackSize;
                break;
            }
        }
        if (materialPrice <= 0.0f) {
            GOTTradeEntries sellPool = this.theTrader.getSellPool();
            for (GOTTradeEntry trade : sellPool.tradeEntries) {
                ItemStack tradeItem = trade.createTradeItem();
                if (!isRepairMaterial(inputItem, tradeItem)) {
                    continue;
                }
                materialPrice = (float) trade.getCost() / (float) trade.createTradeItem().stackSize;
                break;
            }
        }
        return materialPrice;
    }

    public boolean hasMaterialOrCoinAmount(int cost) {
        if (this.isTrader)
            return GOTItemCoin.getInventoryValue(this.thePlayer, false) >= cost;
        ItemStack inputItem = this.invInput.getStackInSlot(0);
        ItemStack materialItem = this.invInput.getStackInSlot(2);
        if (materialItem != null)
            return isRepairMaterial(inputItem, materialItem) && materialItem.stackSize >= cost;
        return false;
    }

    public boolean isRepairMaterial(ItemStack inputItem, ItemStack materialItem) {

        if (this.invInput.getStackInSlot(1) != null && this.invInput.getStackInSlot(1).getItem() instanceof GOTItemModifierTemplate)
            return materialItem.getItem() == GOTRegistry.alloySteelIngot;

        if (inputItem.getItem().getIsRepairable(inputItem, materialItem))
            return true;
        Item item = inputItem.getItem();
        if (item == Items.bow && materialItem.getItem() == Items.string || item instanceof ItemFishingRod && materialItem.getItem() == Items.string)
            return true;
        if (item instanceof ItemShears && materialItem.getItem() == Items.iron_ingot || item instanceof GOTItemChisel && materialItem.getItem() == Items.iron_ingot)
            return true;
        if (item instanceof ItemEnchantedBook && materialItem.getItem() == Items.paper)
            return true;
        Item.ToolMaterial material = null;
        if (item instanceof ItemTool) {
            material = Item.ToolMaterial.valueOf(((ItemTool) item).getToolMaterialName());
        } else if (item instanceof ItemSword) {
            material = Item.ToolMaterial.valueOf(((ItemSword) item).getToolMaterialName());
        }
        if (material == Item.ToolMaterial.WOOD)
            return GOT.isOreNameEqual(materialItem, "plankWood");
        if (item instanceof ItemArmor && ((ItemArmor) item).getArmorMaterial() == GOTMaterial.BONE)
            return GOT.isOreNameEqual(materialItem, "bone");
        return false;
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer) {
        super.onContainerClosed(entityplayer);
        if (!this.theWorld.isRemote) {
            for (int i = 0; i < this.invInput.getSizeInventory(); ++i) {
                ItemStack itemstack = this.invInput.getStackInSlotOnClosing(i);
                if (itemstack == null) {
                    continue;
                }
                entityplayer.dropPlayerItemWithRandomChoice(itemstack, false);
            }
            if (this.doneMischief && this.isTrader && this.theNPC instanceof GOTEntityWesterosScrapTrader) {
                this.theNPC.sendSpeechBank(entityplayer, ((GOTEntityWesterosScrapTrader) this.theNPC).getSmithSpeechBank());
            }
        }
    }

    @Override
    public void onCraftMatrixChanged(IInventory inv) {
        super.onCraftMatrixChanged(inv);
        if (inv == this.invInput) {
            updateRepairOutput();
        }
    }

    public void playAnvilSound() {
        if (!this.theWorld.isRemote) {
            int i;
            int j;
            int k;
            if (this.isTrader) {
                i = MathHelper.floor_double(this.theNPC.posX);
                j = MathHelper.floor_double(this.theNPC.posY);
                k = MathHelper.floor_double(this.theNPC.posZ);
            } else {
                i = this.xCoord;
                j = this.yCoord;
                k = this.zCoord;
            }
            this.theWorld.playAuxSFX(1021, i, j, k, 0);
        }
    }

    public void reforgeItem() {
        if(this.isTrader)
            return;
        ItemStack inputItem;
        long curTime = System.currentTimeMillis();
        if ((this.lastReforgeTime < 0L || curTime - this.lastReforgeTime >= 2000L) && (inputItem = this.invInput.getStackInSlot(0)) != null && this.reforgeCost > 0 && hasMaterialOrCoinAmount(this.reforgeCost)) {
            int cost = this.reforgeCost;
            if (inputItem.isItemStackDamageable()) {
                inputItem.setItemDamage(0);
            }
            GOTEnchantmentHelper.applyRandomEnchantments(inputItem, this.theWorld.rand, false, true);
            GOTEnchantmentHelper.setAnvilCost(inputItem, 0);
            if (this.isTrader && this.theNPC instanceof GOTEntityWesterosScrapTrader && applyMischief(inputItem)) {
                this.doneMischief = true;
            }
            this.invInput.setInventorySlotContents(0, inputItem);
            takeMaterialOrCoinAmount(cost);
            playAnvilSound();
            this.lastReforgeTime = curTime;
            ((EntityPlayerMP) this.thePlayer).sendProgressBarUpdate(this, 2, 0);
            if (!this.isTrader) {
                GOTLevelData.getData(this.thePlayer).addAchievement(GOTAchievement.reforge);
            }
        }
    }

    @Override
    public ItemStack slotClick(int slotNo, int j, int k, EntityPlayer entityplayer) {
        ItemStack resultCopy;
        ItemStack resultItem = this.invOutput.getStackInSlot(0);
        resultItem = ItemStack.copyItemStack(resultItem);
        boolean changed = false;
        if (resultItem != null && slotNo == getSlotFromInventory(this.invOutput, 0).slotNumber && !this.theWorld.isRemote && this.isTrader && this.theNPC instanceof GOTEntityWesterosScrapTrader && (changed = applyMischief(resultCopy = resultItem.copy()))) {
            this.invOutput.setInventorySlotContents(0, resultCopy);
        }
        ItemStack slotClickResult = super.slotClick(slotNo, j, k, entityplayer);
        if (changed) {
            this.doneMischief = true;
            if (this.invOutput.getStackInSlot(0) != null) {
                this.invOutput.setInventorySlotContents(0, resultItem.copy());
            }
        }
        return slotClickResult;
    }

    public void takeMaterialOrCoinAmount(int cost) {
        if (this.isTrader) {
            if (!this.theWorld.isRemote) {
                GOTItemCoin.takeCoins(cost, this.thePlayer);
                detectAndSendChanges();
                this.theNPC.playTradeSound();
            }
        } else {
            ItemStack materialItem = this.invInput.getStackInSlot(2);
            if (materialItem != null) {
                materialItem.stackSize -= cost;
                if (materialItem.stackSize <= 0) {
                    this.invInput.setInventorySlotContents(2, null);
                } else {
                    this.invInput.setInventorySlotContents(2, materialItem);
                }
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityplayer, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            int inputSize = this.invInput.getSizeInventory();
            if (i == inputSize) {
                if (!mergeItemStack(itemstack1, inputSize + 1, inputSize + 37, true))
                    return null;
                slot.onSlotChange(itemstack1, itemstack);
            } else if (i >= inputSize + 1 ? i >= inputSize + 1 && i < inputSize + 37 && !mergeItemStack(itemstack1, 0, inputSize, false) : !mergeItemStack(itemstack1, inputSize + 1, inputSize + 37, false))
                return null;
            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.stackSize == itemstack.stackSize)
                return null;
            slot.onPickupFromSlot(entityplayer, itemstack1);
        }
        return itemstack;
    }

    public void updateItemName(String name) {
        List<EnumChatFormatting> colors = GOTContainerAnvil.getAppliedFormattingCodes(name);
        name = GOTContainerAnvil.stripFormattingCodes(name);
        this.repairedItemName = name = ChatAllowedCharacters.filerAllowedCharacters(name);
        ItemStack itemstack = this.invOutput.getStackInSlot(0);
        if (itemstack != null) {
            if (StringUtils.isBlank(this.repairedItemName)) {
                itemstack.func_135074_t();
            } else {
                itemstack.setStackDisplayName(this.repairedItemName);
            }
            if (!colors.isEmpty()) {
                itemstack.setStackDisplayName(GOTContainerAnvil.applyFormattingCodes(itemstack.getDisplayName(), colors));
            }
        }
        updateRepairOutput();
    }

    @Override
    @SideOnly(value = Side.CLIENT)
    public void updateProgressBar(int i, int j) {
        if (i == 0) {
            this.materialCost = j;
        }
        if (i == 1) {
            this.reforgeCost = j;
        }
        if (i == 2) {
            this.clientReforgeTime = 40;
        }
        if (i == 3) {
            this.engraveOwnerCost = j;
        }
    }

    public void updateRepairOutput() {
        ItemStack inputItem = this.invInput.getStackInSlot(0);
        this.materialCost = 0;
        this.reforgeCost = 0;
        this.engraveOwnerCost = 0;
        this.isSmithScrollCombine = false;
        int baseAnvilCost = 0;
        int repairCost = 0;
        int combineCost = 0;
        int renameCost = 0;
        if (inputItem == null) {
            this.invOutput.setInventorySlotContents(0, null);
            this.materialCost = 0;
        } else {
            int oneItemRepair;
            GOTEnchantmentCombining.CombineRecipe scrollCombine;
            boolean repairing;
            ItemStack inputCopy = inputItem.copy();
            ItemStack combinerItem = this.invInput.getStackInSlot(1);
            ItemStack materialItem = this.isTrader ? null : this.invInput.getStackInSlot(2);
            Map inputEnchants = EnchantmentHelper.getEnchantments(inputCopy);
            boolean enchantingWithBook = false;
            List<GOTEnchantment> inputModifiers = GOTEnchantmentHelper.getEnchantList(inputCopy);
            baseAnvilCost = GOTEnchantmentHelper.getAnvilCost(inputItem) + (combinerItem == null ? 0 : GOTEnchantmentHelper.getAnvilCost(combinerItem));
            this.materialCost = 0;
            String previousDisplayName = inputCopy.getDisplayName();
            String defaultItemName = inputCopy.getItem().getItemStackDisplayName(inputCopy);
            String formattedNameToApply = this.repairedItemName;
            ArrayList<EnumChatFormatting> colorsToApply = new ArrayList<>(GOTContainerAnvil.getAppliedFormattingCodes(inputCopy.getDisplayName()));
            boolean alteringNameColor = false;
            if (GOTContainerAnvil.costsToRename(inputItem) && combinerItem != null) {
                if (combinerItem.getItem() instanceof AnvilNameColorProvider) {
                    boolean isDifferentColor;
                    AnvilNameColorProvider nameColorProvider = (AnvilNameColorProvider) combinerItem.getItem();
                    EnumChatFormatting newColor = nameColorProvider.getAnvilNameColor();
                    isDifferentColor = !colorsToApply.contains(newColor);
                    if (isDifferentColor) {
                        for (EnumChatFormatting ecf : EnumChatFormatting.values()) {
                            if (!ecf.isColor()) {
                                continue;
                            }
                            while (colorsToApply.contains(ecf)) {
                                colorsToApply.remove(ecf);
                            }
                        }
                        colorsToApply.add(newColor);
                        alteringNameColor = true;
                    }
                } else if (combinerItem.getItem() == Items.flint && !colorsToApply.isEmpty()) {
                    colorsToApply.clear();
                    alteringNameColor = true;
                }
                if (alteringNameColor) {
                    ++renameCost;
                }
            }
            if (!colorsToApply.isEmpty()) {
                if (StringUtils.isBlank(formattedNameToApply)) {
                    formattedNameToApply = defaultItemName;
                }
                formattedNameToApply = GOTContainerAnvil.applyFormattingCodes(formattedNameToApply, colorsToApply);
            }
            boolean nameChange = false;
            if (formattedNameToApply != null && !formattedNameToApply.equals(previousDisplayName)) {
                if (StringUtils.isBlank(formattedNameToApply) || formattedNameToApply.equals(defaultItemName)) {
                    if (inputCopy.hasDisplayName()) {
                        inputCopy.func_135074_t();
                        if (!GOTContainerAnvil.stripFormattingCodes(previousDisplayName).equals(GOTContainerAnvil.stripFormattingCodes(formattedNameToApply))) {
                            nameChange = true;
                        }
                    }
                } else {
                    inputCopy.setStackDisplayName(formattedNameToApply);
                    if (!GOTContainerAnvil.stripFormattingCodes(previousDisplayName).equals(GOTContainerAnvil.stripFormattingCodes(formattedNameToApply))) {
                        nameChange = true;
                    }
                }
            }
            if (nameChange && GOTContainerAnvil.costsToRename(inputItem)) {
                ++renameCost;
            }
            if (this.isTrader && (scrollCombine = GOTEnchantmentCombining.getCombinationResult(inputItem, combinerItem)) != null) {
                this.invOutput.setInventorySlotContents(0, scrollCombine.createOutputItem());
                this.materialCost = scrollCombine.cost;
                this.reforgeCost = 0;
                this.engraveOwnerCost = 0;
                this.isSmithScrollCombine = true;
                return;
            }
            boolean combining = false;
            if (combinerItem != null) {
                enchantingWithBook = combinerItem.getItem() == Items.enchanted_book && Items.enchanted_book.func_92110_g(combinerItem).tagCount() > 0;
                if (enchantingWithBook && !GOTConfig.enchantingVanilla) {
                    this.invOutput.setInventorySlotContents(0, null);
                    this.materialCost = 0;
                    return;
                }
                GOTEnchantment combinerItemEnchant = null;
                if (combinerItem.getItem() instanceof GOTItemEnchantment) {
                    combinerItemEnchant = ((GOTItemEnchantment) combinerItem.getItem()).theEnchant;
                } else if (combinerItem.getItem() instanceof GOTItemModifierTemplate) {
                    combinerItemEnchant = GOTItemModifierTemplate.getModifier(combinerItem);
                }
                if (!enchantingWithBook && combinerItemEnchant == null) {
                    if (inputCopy.isItemStackDamageable() && inputCopy.getItem() == combinerItem.getItem()) {
                        int inputUseLeft = inputItem.getMaxDamage() - inputItem.getItemDamageForDisplay();
                        int combinerUseLeft = combinerItem.getMaxDamage() - combinerItem.getItemDamageForDisplay();
                        int restoredUses = combinerUseLeft + inputCopy.getMaxDamage() * 12 / 100;
                        int newUsesLeft = inputUseLeft + restoredUses;
                        int newDamage = inputCopy.getMaxDamage() - newUsesLeft;
                        newDamage = Math.max(newDamage, 0);
                        if (newDamage < inputCopy.getItemDamage()) {
                            inputCopy.setItemDamage(newDamage);
                            int restoredUses1 = inputCopy.getMaxDamage() - inputUseLeft;
                            int restoredUses2 = inputCopy.getMaxDamage() - combinerUseLeft;
                            combineCost += Math.max(0, Math.min(restoredUses1, restoredUses2) / 100);
                        }
                        combining = true;
                    } else if (!alteringNameColor) {
                        this.invOutput.setInventorySlotContents(0, null);
                        this.materialCost = 0;
                        return;
                    }
                }
                HashMap outputEnchants = new HashMap<>(inputEnchants);
                if (GOTConfig.enchantingVanilla) {
                    Map combinerEnchants = EnchantmentHelper.getEnchantments(combinerItem);
                    for (Object obj : combinerEnchants.keySet()) {
                        int combinerEnchLevel;
                        int combinerEnchID = (Integer) obj;
                        Enchantment combinerEnch = Enchantment.enchantmentsList[combinerEnchID];
                        int inputEnchLevel = 0;
                        if (outputEnchants.containsKey(combinerEnchID)) {
                            inputEnchLevel = (Integer) outputEnchants.get(combinerEnchID);
                        }
                        int combinedEnchLevel = inputEnchLevel == (combinerEnchLevel = (Integer) combinerEnchants.get(combinerEnchID)) ? ++combinerEnchLevel : Math.max(combinerEnchLevel, inputEnchLevel);
                        combinerEnchLevel = combinedEnchLevel;
                        int levelsAdded = combinerEnchLevel - inputEnchLevel;
                        boolean canApply = combinerEnch.canApply(inputItem);
                        if (this.thePlayer.capabilities.isCreativeMode || inputItem.getItem() == Items.enchanted_book) {
                            canApply = true;
                        }
                        for (Object objIn : outputEnchants.keySet()) {
                            int inputEnchID = (Integer) objIn;
                            Enchantment inputEnch = Enchantment.enchantmentsList[inputEnchID];
                            if (inputEnchID == combinerEnchID || combinerEnch.canApplyTogether(inputEnch) && inputEnch.canApplyTogether(combinerEnch)) {
                                continue;
                            }
                            canApply = false;
                            combineCost += levelsAdded;
                        }
                        if (!canApply) {
                            continue;
                        }
                        combinerEnchLevel = Math.min(combinerEnchLevel, combinerEnch.getMaxLevel());
                        outputEnchants.put(combinerEnchID, combinerEnchLevel);
                        int costPerLevel = 0;
                        int enchWeight = combinerEnch.getWeight();
                        switch (enchWeight) {
                            case 1:
                                costPerLevel = 8;
                                break;
                            case 2:
                                costPerLevel = 4;
                                break;
                            case 5:
                                costPerLevel = 2;
                                break;
                            case 10:
                                costPerLevel = 1;
                                break;
                            default:
                                break;
                        }
                        combineCost += costPerLevel * levelsAdded;
                    }
                } else {
                    outputEnchants.clear();
                }
                EnchantmentHelper.setEnchantments(outputEnchants, inputCopy);
                int maxMods = 5;
                ArrayList<GOTEnchantment> outputMods = new ArrayList<>(inputModifiers);
                List<GOTEnchantment> combinerMods = GOTEnchantmentHelper.getEnchantList(combinerItem);
                if (combinerItemEnchant != null) {
                    if (!combinerItemEnchant.canApply(inputItem, false)) {
                        this.invOutput.setInventorySlotContents(0, null);
                        this.materialCost = 0;
                        return;
                    }
                    combinerMods.add(combinerItemEnchant);
                }
                for (GOTEnchantment combinerMod : combinerMods) {
                    boolean canApply = combinerMod.canApply(inputItem, false);
                    if (canApply) {
                        for (GOTEnchantment mod : outputMods) {
                            if (mod.isCompatibleWith(combinerMod) && combinerMod.isCompatibleWith(mod)) {
                                continue;
                            }
                            canApply = false;
                        }
                    }
                    int numOutputMods = 0;
                    for (GOTEnchantment mod : outputMods) {
                        if (mod.bypassAnvilLimit()) {
                            continue;
                        }
                        ++numOutputMods;
                    }
                    if (!combinerMod.bypassAnvilLimit() && numOutputMods >= maxMods) {
                        canApply = false;
                    }
                    if (!canApply) {
                        continue;
                    }
                    outputMods.add(combinerMod);
                    if (!combinerMod.isBeneficial()) {
                        continue;
                    }
                    combineCost = 20;
                }
                GOTEnchantmentHelper.setEnchantList(inputCopy, outputMods);
                if(outputMods.contains(GOTEnchantment.valyrianSeal)) {
                    if(inputCopy.hasTagCompound()) {
                        inputCopy.getTagCompound().setDouble("sealChance", 1.0);
                    }
                }
            }
            if (combineCost > 0) {
                combining = true;
            }
            //			int numEnchants = 0;
            //			for (Object obj : inputEnchants.keySet()) {
            //				int enchID = (Integer) obj;
            //				Enchantment ench = Enchantment.enchantmentsList[enchID];
            //				int enchLevel = (Integer) inputEnchants.get(enchID);
            //				++numEnchants;
            //				int costPerLevel = 0;
            //				int enchWeight = ench.getWeight();
            //				switch (enchWeight) {
            //				case 1:
            //					costPerLevel = 8;
            //					break;
            //				case 2:
            //					costPerLevel = 4;
            //					break;
            //				case 5:
            //					costPerLevel = 2;
            //					break;
            //				case 10:
            //					costPerLevel = 1;
            //					break;
            //				default:
            //					break;
            //				}
            //				baseAnvilCost += numEnchants + enchLevel * costPerLevel;
            //			}
            if (enchantingWithBook && !inputCopy.getItem().isBookEnchantable(inputCopy, combinerItem)) {
                inputCopy = null;
            }
            //			for (GOTEnchantment mod : inputModifiers) {
            //				if (!mod.isBeneficial()) {
            //					continue;
            //				}
            //				baseAnvilCost += Math.max(1, (int) mod.getValueModifier());
            //			}
            if (inputCopy.isItemStackDamageable()) {
                boolean canRepair = false;
                int availableMaterials = 0;
                if (this.isTrader) {
                    canRepair = getTraderMaterialPrice(inputItem) > 0.0f;
                    availableMaterials = Integer.MAX_VALUE;
                } else {
                    canRepair = materialItem != null && isRepairMaterial(inputItem, materialItem);
                    if (materialItem != null) {
                        availableMaterials = materialItem.stackSize - combineCost - renameCost;
                    }
                }
                oneItemRepair = Math.min(inputCopy.getItemDamageForDisplay(), inputCopy.getMaxDamage() / 4);
                if (canRepair && availableMaterials > 0 && oneItemRepair > 0) {
                    availableMaterials -= baseAnvilCost;
                    if (availableMaterials > 0) {
                        int usedMaterials;
                        for (usedMaterials = 0; oneItemRepair > 0 && usedMaterials < availableMaterials; ++usedMaterials) {
                            int newDamage = inputCopy.getItemDamageForDisplay() - oneItemRepair;
                            inputCopy.setItemDamage(newDamage);
                            oneItemRepair = Math.min(inputCopy.getItemDamageForDisplay(), inputCopy.getMaxDamage() / 4);
                        }
                        repairCost += usedMaterials;
                    } else if (!nameChange && !combining) {
                        repairCost = 1;
                        int newDamage = inputCopy.getItemDamageForDisplay() - oneItemRepair;
                        inputCopy.setItemDamage(newDamage);
                    }
                }
            }
            repairing = repairCost > 0;
            if (combining || repairing) {
                this.materialCost = baseAnvilCost;
                this.materialCost += combineCost + repairCost;
            } else {
                this.materialCost = 0;
            }
            this.materialCost += renameCost;
            if (inputCopy != null) {
                int nextAnvilCost = GOTEnchantmentHelper.getAnvilCost(inputItem);
                if (combinerItem != null) {
                    int combinerAnvilCost = GOTEnchantmentHelper.getAnvilCost(combinerItem);
                    nextAnvilCost = Math.max(nextAnvilCost, combinerAnvilCost);
                }
                if (combining) {
                    nextAnvilCost += 0;
                } else if (repairing) {
                    ++nextAnvilCost;
                }
                nextAnvilCost = Math.max(nextAnvilCost, 0);
                if (nextAnvilCost > 0) {
                    GOTEnchantmentHelper.setAnvilCost(inputCopy, nextAnvilCost);
                }
            }
            if (GOTEnchantmentHelper.isReforgeable(inputItem)) {
                ItemStack reforgeCopy;
                this.reforgeCost = 2;
                if (inputItem.getItem() instanceof ItemArmor) {
                    this.reforgeCost = 3;
                }
                if (inputItem.isItemStackDamageable() && (oneItemRepair = Math.min((reforgeCopy = inputItem.copy()).getItemDamageForDisplay(), reforgeCopy.getMaxDamage() / 4)) > 0) {
                    int usedMaterials = 0;
                    while (oneItemRepair > 0) {
                        int newDamage = reforgeCopy.getItemDamageForDisplay() - oneItemRepair;
                        reforgeCopy.setItemDamage(newDamage);
                        oneItemRepair = Math.min(reforgeCopy.getItemDamageForDisplay(), reforgeCopy.getMaxDamage() / 4);
                        ++usedMaterials;
                    }
                    this.reforgeCost += usedMaterials;
                }
                this.engraveOwnerCost = 2;
            } else {
                this.reforgeCost = 0;
                this.engraveOwnerCost = 0;
            }
            if (isRepairMaterial(inputItem, new ItemStack(Items.string))) {
                int stringFactor = 3;
                this.materialCost *= stringFactor;
                this.reforgeCost *= stringFactor;
                this.engraveOwnerCost *= stringFactor;
            }
            if (this.isTrader) {
                boolean isCommonRenameOnly = nameChange && this.materialCost == 0;
                float materialPrice = getTraderMaterialPrice(inputItem);
                if (materialPrice > 0.0f) {
                    this.materialCost = Math.round(this.materialCost * materialPrice);
                    this.materialCost = Math.max(this.materialCost, 1);
                    this.reforgeCost = Math.round(this.reforgeCost * materialPrice);
                    this.reforgeCost = Math.max(this.reforgeCost, 1);
                    this.engraveOwnerCost = Math.round(this.engraveOwnerCost * materialPrice);
                    this.engraveOwnerCost = Math.max(this.engraveOwnerCost, 1);
                    if (this.theTrader instanceof GOTEntityWesterosScrapTrader) {
                        this.materialCost = MathHelper.ceiling_float_int(this.materialCost * 0.5f);
                        this.materialCost = Math.max(this.materialCost, 1);
                        this.reforgeCost = MathHelper.ceiling_float_int(this.reforgeCost * 0.5f);
                        this.reforgeCost = Math.max(this.reforgeCost, 1);
                        this.engraveOwnerCost = MathHelper.ceiling_float_int(this.engraveOwnerCost * 0.5f);
                        this.engraveOwnerCost = Math.max(this.engraveOwnerCost, 1);
                    }
                } else if (!isCommonRenameOnly) {
                    this.invOutput.setInventorySlotContents(0, null);
                    this.materialCost = 0;
                    this.reforgeCost = 0;
                    this.engraveOwnerCost = 0;
                    return;
                }
            }
            if (combining || repairing || nameChange || alteringNameColor) {
                this.invOutput.setInventorySlotContents(0, inputCopy);
            } else {
                this.invOutput.setInventorySlotContents(0, null);
                this.materialCost = 0;
            }
            detectAndSendChanges();
        }
    }

    public static String applyFormattingCodes(String name, List<EnumChatFormatting> colors) {
        for (EnumChatFormatting color : colors) {
            name = color + name;
        }
        return name;
    }

    public static boolean costsToRename(ItemStack itemstack) {
        Item item = itemstack.getItem();
        if (item instanceof ItemSword || item instanceof ItemTool || item instanceof ItemArmor && ((ItemArmor) item).damageReduceAmount > 0)
            return true;
        return item instanceof ItemBow || item instanceof GOTItemCrossbow || item instanceof GOTItemThrowingAxe || item instanceof GOTItemSarbacane;
    }

    public static List<EnumChatFormatting> getAppliedFormattingCodes(String name) {
        ArrayList<EnumChatFormatting> colors = new ArrayList<>();
        for (EnumChatFormatting color : EnumChatFormatting.values()) {
            String formatCode = color.toString();
            if (!name.startsWith(formatCode)) {
                continue;
            }
            colors.add(color);
        }
        return colors;
    }

    public static String stripFormattingCodes(String name) {
        for (EnumChatFormatting color : EnumChatFormatting.values()) {
            String formatCode = color.toString();
            if (!name.startsWith(formatCode)) {
                continue;
            }
            name = name.substring(formatCode.length());
        }
        return name;
    }

}
