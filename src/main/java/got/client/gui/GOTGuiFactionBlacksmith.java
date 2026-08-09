package got.client.gui;

import codechicken.nei.NEIClientConfig;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import got.common.enchant.GOTEnchantment;
import got.common.enchant.GOTEnchantmentHelper;
import got.common.entity.GOTEnchaldBlacksmith;
import got.common.entity.other.GOTBlacksmithOffer;
import got.common.faction.GOTContainerFactionBlacksmith;
import got.common.network.GOTPacketHandler;
import got.common.network.base.PacketDispatcher;
import got.common.network.clientToServer.*;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import java.util.HashMap;
import java.util.HashSet;


public class GOTGuiFactionBlacksmith extends GuiContainer {
    private EntityPlayer player;
    public GOTContainerFactionBlacksmith container;
    private GOTEnchantment[] enchantments;
    private GOTEnchaldBlacksmith theBlacksmithNPC;
    private GOTEnchaldBlacksmith.BlacksmithType type;
    private GuiTextField textField;
    private HashMap<Integer, GuiButton> guiButtons = new HashMap<>();
    private HashSet<Class<? extends GOTEnchantment>> selectedEnchantments = new HashSet<>();
    private ItemStack itemstack = null;
    private final ResourceLocation RW = new ResourceLocation("got", "textures/gui/RW.png");
    private final ResourceLocation anvil = new ResourceLocation("got", "textures/gui/anv.png");
    private final ResourceLocation[] resources = new ResourceLocation[14];
    private String itemName = "";
    private static final boolean HAS_NEI = Loader.isModLoaded("NotEnoughItems");
    private boolean anyButtonSelected;
    private boolean hasItemInSlot;
    public boolean isBuyingSlot = false;
    private boolean isSpecialProtectionSelected = false;
    private boolean needSwitchButton = false;
    private boolean neiOverlayHidden;
    private final int MAX_SLOTS = 5;
    private final int frameDelay = 76;
    private final int[] buttons;
    private final int[] enchantmentLevels;
    private long lastTime = 0;
    private int currentFrame = 0;
    private int enchantmentSlots;
    private int slotToUnlock = -1;

    public GOTGuiFactionBlacksmith(EntityPlayer entityplayer, EntityCreature npc) {
        super(new GOTContainerFactionBlacksmith(entityplayer, npc));
        xSize = 512;
        ySize = 512;
//        ScaledResolution scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
//
//        int guiScale = scaledResolution.getScaleFactor();
//        System.out.println(guiScale);
//        if (mc.gameSettings.guiScale == 3) {
//            xSize /= 2;
//            ySize /= 2;
//        }
        this.player = entityplayer;
        this.container = (GOTContainerFactionBlacksmith) inventorySlots;
        this.theBlacksmithNPC = (GOTEnchaldBlacksmith) npc;
        this.type = theBlacksmithNPC.getBlacksmithType();
        this.enchantmentSlots = type.getSlots();
        this.buttons = new int[enchantmentSlots];
        this.enchantmentLevels = GOTBlacksmithOffer.getEnchantmentLevels(type);
        this.enchantments = GOTBlacksmithOffer.getEnchantments(type);

        for (int i = 0; i < enchantmentSlots; i++) {
            buttons[i] = -1;
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        guiButtons.clear();
        if (HAS_NEI) {
            neiOverlayHidden = isNEIOverlayHidden();
            setNEIOverlayHidden(true);
        }
//        boolean flag = true;
//        if (flag = mc.gameSettings.guiScale == 0) {
////            xSize = 512 / 2;
////            ySize = 512 / 2;
//            guiLeft = (width - xSize / 2) / 2;
//            guiTop = (height - ySize / 2) / 2;
//            for (Slot slot : slots) {
//            slot.xDisplayPosition *= 2;
//            slot.yDisplayPosition *= 2;
//            }
//        }
        textField = new GuiCenteredTextField(fontRendererObj, guiLeft + 147, guiTop + 65, 214, 15);
        textField.setEnabled(true);
        textField.setTextColor(0x000000);
        textField.setDisabledTextColour(-1);
        textField.setEnableBackgroundDrawing(false);
        textField.setMaxStringLength(40);

        if (type != GOTEnchaldBlacksmith.BlacksmithType.RW) {
            for (int i = 0; i < 14; i++)
                this.resources[i] = new ResourceLocation("got", "textures/gui/" + type + "/" + i + ".png");
        }

        addButton(new GOTGuiButtonBlacksmith(100, guiLeft + 422, guiTop + 378, 26, 26, "Выковать"), true, false);
        addButton(new GOTGuiButtonBlacksmith(101, guiLeft + 422, guiTop + 347, 26, 26, "Починить"), true, false);
        addButton(new GOTGuiButtonBlacksmith(102, guiLeft + 422, guiTop + 409, 26, 26, "Перегравировать"), true, false);
        addButton(new GuiButton(103, width / 2 - 51, guiTop + 366, 20, 20, "Да"), false, false);
        addButton(new GuiButton(104, width / 2 + 29, guiTop + 366, 20, 20, "Нет"), false, false);

        if (type == GOTEnchaldBlacksmith.BlacksmithType.MW) {
            int i = 0;
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 131, guiTop + 225, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 179, guiTop + 177, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 227, guiTop + 129, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 202, guiTop + 225, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 250, guiTop + 177, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 298, guiTop + 129, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 273, guiTop + 225, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 321, guiTop + 177, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 369, guiTop + 129, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 344, guiTop + 225, 26, 26, ""), false, true);

        } else if (type == GOTEnchaldBlacksmith.BlacksmithType.RW) {
            int i = 0;
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 160, guiTop + 225, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 107, guiTop + 225, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 107, guiTop + 173, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 296, guiTop + 225, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 349, guiTop + 225, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 349, guiTop + 173, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 229, guiTop + 304, 26, 26, ""), false, true);
        } else if (type == GOTEnchaldBlacksmith.BlacksmithType.ARM) {
            int i = 0;
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 243, guiTop + 193, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 243, guiTop + 141, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 290, guiTop + 193, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 290, guiTop + 141, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 290, guiTop + 89, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 196, guiTop + 298, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 148, guiTop + 250, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 100, guiTop + 202, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 290, guiTop + 298, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 338, guiTop + 250, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 386, guiTop + 202, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 290, guiTop + 245, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 338, guiTop + 197, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 386, guiTop + 149, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 196, guiTop + 245, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 148, guiTop + 197, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 100, guiTop + 149, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 71, guiTop + 409, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 71, guiTop + 357, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 71, guiTop + 305, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 196, guiTop + 193, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 196, guiTop + 141, 26, 26, ""), false, true);
            addButton(new GOTGuiButtonBlacksmith(i++, guiLeft + 196, guiTop + 89, 26, 26, ""), false, true);
        } else if (type == GOTEnchaldBlacksmith.BlacksmithType.TOOL) {
            int j = 0;
            int k = 0;
            for (int i = 0; i < enchantmentSlots; i++) {
                if (i % 3 == 0) {
                    j += 100;
                    k = 0;
                }
                addButton(new GuiButton(i, guiLeft + j - 60, guiTop + (k += 30), 20, 20, ""), false, true);
            }
        }
        updateState();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        textField.updateCursorCounter();
        
        if (isBuyingSlot) {
            updateButtonPositions();
        }
        detectInputSlotInteraction();
        if (container.detectInventoryInteraction()) {
            updateState();
        }
    }

    public void updateState() {

        anyButtonSelected = isAnyButtonSelected();
        boolean availableToSelect = MAX_SLOTS - container.countItemEnchantments() > countSelectedButtons();
        if (hasItemInSlot && container.canRename(itemstack))
            textField.setText(itemstack.getDisplayName());
        else
            textField.setText("");
        for (int i = 0; i < enchantmentSlots; i++) {
            boolean slotUnlocked = theBlacksmithNPC.isSlotUnlocked(i);
            setVisible(getButton(i), !isBuyingSlot);
            setEnabled(getButton(i), (!slotUnlocked && container.canUnlockSlot(i)) || (hasItemInSlot && slotUnlocked && container.canApplyWeaponEnchantments(i) && availableToSelect));

            if (!availableToSelect) {
                setEnabled(getButton(i), isButtonSelected(i));
                if (i > 0 && GOTEnchantmentHelper.hasEnchant(itemstack, enchantments[i - 1]) && enchantmentLevels[i] > enchantmentLevels[i - 1] && slotUnlocked) {
                    setEnabled(getButton(i), true);
                }
            }

            if (needSwitchButton || isBuyingSlot || !hasItemInSlot) {
                needSwitchButton = false;
                unselectButtons();
            }

            if (anyButtonSelected && selectedEnchantments.contains(enchantments[i].getClass()) && !isButtonSelected(i)) {
                setEnabled(getButton(i), false);
            }

            if (isSpecialProtectionSelected && !isButtonSelected(i) && enchantments[i].isWeaponProtection())
                setEnabled(getButton(i), false);

            if (isButtonSelected(i) && !getButton(i).enabled)
                switchButtonState(i);
        }

        setVisible(getButton(100), !isBuyingSlot);
        setVisible(getButton(101), !isBuyingSlot);
        setVisible(getButton(102), !isBuyingSlot);
        setEnabled(getButton(100), !isBuyingSlot && isAnyButtonSelected() && container.canAffordToEnchant(player, buttons));
        setEnabled(getButton(101), !isBuyingSlot && container.canAffordAndApplyRepair());
        setVisible(getButton(103), isBuyingSlot);
        setEnabled(getButton(103), isBuyingSlot && container.canAffordToUnlockSlot(player, slotToUnlock));
        setVisible(getButton(104), isBuyingSlot);
        setEnabled(getButton(104), isBuyingSlot);
    }

    private void updateButtonPositions() {
        GuiButton yes = getButton(103);
        GuiButton no = getButton(104);

        if (yes != null && no != null) {

            int boxWidth = 350;
            int boxHeight = 180;
            int boxX = guiLeft + (xSize - boxWidth) / 2;
            int boxY = guiTop + (ySize - boxHeight) / 2;
            
            int buttonY = boxY + boxHeight - 40;
            int buttonSpacing = 40;
            int totalWidth = yes.width + no.width + buttonSpacing;
            int startX = boxX + (boxWidth - totalWidth) / 2;
            yes.xPosition = startX;
            yes.yPosition = buttonY;
            no.xPosition = startX + yes.width + buttonSpacing;
            no.yPosition = buttonY;
        }
    }

    public void detectInputSlotInteraction() {
        ItemStack is = container.invInput.getStackInSlot(0);
        hasItemInSlot = is != null;
        if (hasItemInSlot && !ItemStack.areItemStacksEqual(itemstack, is)) {
            itemstack = is;
            itemName = is.getDisplayName();
            updateState();
        } else if (!hasItemInSlot && itemstack != null) {
            itemstack = null;
            itemName = "";
            updateState();
        }
        setEnabled(getButton(102), !isBuyingSlot && itemstack != null && !textField.getText().isEmpty() && container.canRename(itemstack) && !itemName.equals(textField.getText()));

    }

    private boolean isAnyButtonSelected() {
        for (int i = 0; i < enchantmentSlots; i++) {
            if (buttons[i] == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean isButtonSelected(int id) {
        return buttons[id] == 0;
    }

    private void switchButtonState(int id) {
        buttons[id] = buttons[id] == 0 ? -1 : 0;
        PacketDispatcher.sendToServer(new GOTPacketBlacksmithEnchantmentButton(id));
        if (isButtonSelected(id)) {
            selectedEnchantments.add(enchantments[id].getClass());
            if (type == GOTEnchaldBlacksmith.BlacksmithType.ARM && enchantments[id].isWeaponProtection())
                isSpecialProtectionSelected = true;
        } else {
            selectedEnchantments.remove(enchantments[id].getClass());
            if (type == GOTEnchaldBlacksmith.BlacksmithType.ARM && enchantments[id].isWeaponProtection())
                isSpecialProtectionSelected = false;
        }
    }

    private void unselectButtons() {
        for (int i = 0; i < enchantmentSlots; i++) {
            if (isButtonSelected(i)) {
                buttons[i] = -1;
                PacketDispatcher.sendToServer(new GOTPacketBlacksmithEnchantmentButton(i));
                selectedEnchantments.remove(enchantments[i].getClass());
                isSpecialProtectionSelected = false;
            }
        }
    }

    private int countSelectedButtons() {
        int count = 0;
        for (int i = 0; i < enchantmentSlots; i++) {
            if (buttons[i] == 0)
                count += 1;
        }
        return count;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (!getButton(button.id).visible)
            return;

        if (button.id < enchantmentSlots && !theBlacksmithNPC.isSlotUnlocked(button.id)) {
            isBuyingSlot = true;
            slotToUnlock = button.id;
            updateButtonPositions();
        } else if (button.id < enchantmentSlots) {
            switchButtonState(button.id);
        }

        switch (button.id) {
            case 100:
                PacketDispatcher.sendToServer(new GOTPacketBlacksmithForge());
                needSwitchButton = true;
                break;
            case 101:
                GOTPacketHandler.networkWrapper.sendToServer(new GOTPacketBlacksmithRepair());
                break;
            case 102:
                GOTPacketHandler.networkWrapper.sendToServer(new GOTPacketBlacksmithRename(this.textField.getText()));
                break;
            case 103:
                GOTPacketHandler.networkWrapper.sendToServer(new GOTPacketBlacksmithUnlock(slotToUnlock));
                mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
                isBuyingSlot = false;
                break;
            case 104:
                isBuyingSlot = false;
                mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
                break;
        }
        updateState();
    }

    protected void addButton(GuiButton button, boolean visible, boolean enabled) {
        guiButtons.put(button.id, button);
        buttonList.add(button);
        button.enabled = enabled;
        button.visible = visible;
    }

    protected GuiButton getButton(int id) {
        return guiButtons.get(id);
    }

    protected void setEnabled(GuiButton button, boolean enabled) {
        button.enabled = enabled;
    }

    protected void setVisible(GuiButton button, boolean visible) {
        button.visible = visible;
    }

    protected int[] getCosts() {
        int[] costs = {0, 0};
        for (int i = 0; i < enchantmentSlots; i++) {
            if (buttons[i] == 0) {
                for (int j = 0; j < 2; j++) {
                    costs[j] += (int) GOTBlacksmithOffer.getEnchantmentPrice(enchantmentLevels[i] - 1).getCosts()[j];
                }
            }
        }
        return costs;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (brain.tutorial.client.TutorialGuiBlacksmithHighlight.handleMouseClick(this, mouseX, mouseY, button)) return;
        if (isBuyingSlot) {
            GuiButton yes = getButton(103);
            GuiButton no = getButton(104);

            if (yes.visible && yes.enabled && mouseX >= yes.xPosition && mouseX < yes.xPosition + yes.width && mouseY >= yes.yPosition && mouseY < yes.yPosition + yes.height) {
                actionPerformed(yes);
                return;
            }

            if (no.visible && no.enabled && mouseX >= no.xPosition && mouseX < no.xPosition + no.width && mouseY >= no.yPosition && mouseY < no.yPosition + no.height) {
                actionPerformed(no);
                return;
            }
            return;
        }

        super.mouseClicked(mouseX, mouseY, button);
        if (textField != null) {
            textField.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (isBuyingSlot) {
            if (keyCode == 1) {              
                isBuyingSlot = false;
                actionPerformed(getButton(104));
            }
        } else {
            if (!textField.textboxKeyTyped(typedChar, keyCode)) {
                super.keyTyped(typedChar, keyCode);
            }
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (HAS_NEI) {
            setNEIOverlayHidden(neiOverlayHidden);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float p_73863_3_) {
        super.drawScreen(mouseX, mouseY, p_73863_3_);
        textField.drawTextBox();
        
        if (brain.tutorial.client.TutorialClientState.tutorialStage == 6) {
            int prog = brain.tutorial.client.TutorialClientState.tutorialProgress;
            if (type == got.common.entity.GOTEnchaldBlacksmith.BlacksmithType.ARM) {
                // Бронник: 1 (ждет предмет) -> 2 (ждет чар) -> 3 (ждет рефордж)
                if (prog == 1 && hasItemInSlot) {
                    brain.tutorial.client.TutorialClientState.tutorialProgress = 2;
                } else if (prog == 2 && isAnyButtonSelected()) {
                    brain.tutorial.client.TutorialClientState.tutorialProgress = 3;
                } else if (prog > 1 && !hasItemInSlot) {
                    brain.tutorial.client.TutorialClientState.tutorialProgress = 1;
                }
            } else {
                // Оружейник: 5 (ждет предмет) -> 6 (ждет чар) -> 7 (ждет рефордж)
                if (prog == 5 && hasItemInSlot) {
                    brain.tutorial.client.TutorialClientState.tutorialProgress = 6;
                } else if (prog == 6 && isAnyButtonSelected()) {
                    brain.tutorial.client.TutorialClientState.tutorialProgress = 7;
                } else if (prog > 5 && !hasItemInSlot) {
                    brain.tutorial.client.TutorialClientState.tutorialProgress = 5;
                }
            }
        }

        
        brain.tutorial.client.TutorialGuiBlacksmithHighlight.drawHighlight(this);
//        int originalWidth = this.width;
//        int originalHeight = this.height;
//
//        this.width = (int)(originalWidth / 0.5f);
//        this.height = (int)(originalHeight / 0.5f);
//
//
//        int scaledMouseX = (int)(p_73863_1_ / 0.5f);
//        int scaledMouseY = (int)(p_73863_2_ / 0.5f);
//
//
//        GlStateManager.pushMatrix();
//        GlStateManager.scale(0.5f, 0.5f, 1.0f);

//        super.drawScreen(scaledMouseX, scaledMouseY, p_73863_3_);
//        GL11.popMatrix();
//        this.width = originalWidth;
//        this.height = originalHeight;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {

        if (type == GOTEnchaldBlacksmith.BlacksmithType.ARM || type == GOTEnchaldBlacksmith.BlacksmithType.MW) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime > frameDelay) {
                currentFrame = (currentFrame + 1) % 14;
                lastTime = currentTime;
            }
            mc.getTextureManager().bindTexture(resources[currentFrame]);
        } else {
            mc.getTextureManager().bindTexture(RW);
        }
        drawTextureCustomSize(guiLeft, guiTop, 0, 0, xSize, ySize, xSize, ySize);
        mc.getTextureManager().bindTexture(anvil);
        drawTextureCustomSize(guiLeft + 422, guiTop + 347, 0, 0, 26, 26, 26, 26);

        if (isBuyingSlot) {
            int boxWidth = 350;
            int boxHeight = 180;
            int boxX = guiLeft + (xSize - boxWidth) / 2;
            int boxY = guiTop + (ySize - boxHeight) / 2;

            drawRect(0, 0, width, height, 0xAA000000);
            drawRect(boxX - 2, boxY - 2, boxX + boxWidth + 2, boxY - 1, 0xFFFFFFFF);
            drawRect(boxX - 2, boxY + boxHeight + 1, boxX + boxWidth + 2, boxY + boxHeight + 2, 0xFFFFFFFF);
            drawRect(boxX - 2, boxY, boxX - 1, boxY + boxHeight, 0xFFFFFFFF);
            drawRect(boxX + boxWidth + 1, boxY, boxX + boxWidth + 2, boxY + boxHeight, 0xFFFFFFFF);
            drawRect(boxX, boxY, boxX + boxWidth, boxY + boxHeight, 0xFF000000);
        }
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if (isBuyingSlot) {
            
            int boxWidth = 350;
            int boxHeight = 180;
            int boxX = (xSize - boxWidth) / 2;
            int boxY = (ySize - boxHeight) / 2;

            GOTBlacksmithOffer offer = GOTBlacksmithOffer.getUnlockOffer(slotToUnlock, type).getUnlockCost(theBlacksmithNPC.getCoinMultiplier(), theBlacksmithNPC.getBlocksCounter());

            String title = "Приобрести данное улучшение за следующую цену?";
            drawCenteredString(fontRendererObj, title, boxX + boxWidth / 2, boxY + 20, 0xFFFFFF);

            int itemCount = offer.getItems().length;
            int itemSpacing = 100;
            int totalWidth = itemCount * itemSpacing;
            
            int startX = boxX + (boxWidth - totalWidth) / 2 + itemSpacing / 2;
            int y = boxY + 60;

            for (int i = 0; i < itemCount; i++) {

                int centerX = startX + i * itemSpacing;
                int itemX = centerX - 8;
                int itemY = y;

                itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.renderEngine, offer.getItems()[i], itemX, itemY);

                String price = String.valueOf((int) offer.getCosts()[i]);
                int priceWidth = fontRendererObj.getStringWidth(price);
                int priceX = centerX - priceWidth / 2;
                int priceY = y + 20;

                fontRendererObj.drawStringWithShadow(price, priceX, priceY, 0xFFFFFF);
            }
        } else {
            super.drawGuiContainerForegroundLayer(mouseX, mouseY);
            if (anyButtonSelected) {

                ItemStack item1 = GOTBlacksmithOffer.getEnchantmentPrice(0).getItems()[0];
                ItemStack item2 = GOTBlacksmithOffer.getEnchantmentPrice(0).getItems()[1];
                int[] costs = getCosts();

                String price1 = String.valueOf(costs[0]);
                String price2 = String.valueOf(costs[1]);

                int price1Width = fontRendererObj.getStringWidth(price1);
                int price2Width = fontRendererObj.getStringWidth(price2);
                
                int contentWidth = 60 + price1Width + price2Width;
                int boxWidth = contentWidth + 20;
                int boxHeight = 30;

                GuiButton forgeButton = getButton(100);
                int boxX = forgeButton.xPosition - guiLeft - boxWidth - 10;
                int boxY = forgeButton.yPosition - guiTop + forgeButton.height / 2 - boxHeight / 2;
                drawTooltipBg(boxX, boxY, boxWidth, boxHeight);

                int startX = boxX + 10;
                int secondX = startX + 40 + price1Width;
                int iconY = boxY + 6;
                renderPriceItem(item1, price1, startX, iconY);
                renderPriceItem(item2, price2, secondX, iconY);
            }
            drawTooltip(mouseX, mouseY);
        }
    }

    private void drawTooltip(int mouseX, int mouseY) {
        for (int i = 0; i < buttons.length; i++) {
            GuiButton button = getButton(i);

            if (button.visible && container.isSlotUnlocked(i) && isMouseOverButton(button, mouseX, mouseY)) {
                drawEnchantmentTooltip(i, mouseX, mouseY);
                break;
            } else if (isMouseOverButton(button, mouseX, mouseY)) {
                String displayName = enchantments[i].getDisplayName();
                String description = StatCollector.translateToLocal(enchantments[i].getDescription(null));

                int tooltipX = mouseX - guiLeft + 20;
                int tooltipY = mouseY - guiTop - 25;

                int width = Math.max(fontRendererObj.getStringWidth(displayName), fontRendererObj.getStringWidth(description)) + 10;
                int height = fontRendererObj.FONT_HEIGHT * 2 + 6;

                drawTooltipBg(tooltipX, tooltipY, width, height);

                fontRendererObj.drawStringWithShadow(displayName, tooltipX + 5, tooltipY + 3, 0xd6942e);
                fontRendererObj.drawStringWithShadow(description, tooltipX + 5, tooltipY + 13, 0xaaaaaa);
            }
        }

        for (int i = 100; i < 103; i++) {
            GuiButton button = getButton(i);
            if (button.enabled && isMouseOverButton(button, mouseX, mouseY)) {
                String text;
                switch (button.id) {
                    case 100:
                        text = "Выковать";
                        break;
                    case 101:
                        text = "Починить";
                        break;
                    case 102:
                        text = "Перегравировать";
                        break;
                    default:
                        text = null;
                }
                if (text != null) {
                    int tooltipX = mouseX - guiLeft + 20;
                    int tooltipY = mouseY - guiTop - 20;

                    int width = fontRendererObj.getStringWidth(text) + 10;
                    int height = fontRendererObj.FONT_HEIGHT + 6;

                    drawTooltipBg(tooltipX, tooltipY, width, height);
                    fontRendererObj.drawStringWithShadow(text, tooltipX + 5, tooltipY + 3, 0xd6942e);
                }
                break;
            }
        }
    }

    private boolean isMouseOverButton(GuiButton button, int mouseX, int mouseY) {
        return mouseX >= button.xPosition && mouseY >= button.yPosition && mouseX < button.xPosition + button.width && mouseY < button.yPosition + button.height;
    }

    private void drawEnchantmentTooltip(int index, int mouseX, int mouseY) {
        String displayName = enchantments[index].getDisplayName();
        String description = StatCollector.translateToLocal(enchantments[index].getDescription(null));

        String price1 = String.valueOf((int) GOTBlacksmithOffer.getEnchantmentPrice(enchantmentLevels[index] - 1).getCosts()[0]);
        String price2 = String.valueOf((int) GOTBlacksmithOffer.getEnchantmentPrice(enchantmentLevels[index] - 1).getCosts()[1]);

        ItemStack item1 = GOTBlacksmithOffer.getEnchantmentPrice(0).getItems()[0];
        ItemStack item2 = GOTBlacksmithOffer.getEnchantmentPrice(0).getItems()[1];

        int nameWidth = fontRendererObj.getStringWidth(displayName);
        int descWidth = fontRendererObj.getStringWidth(description);
        int price1Width = fontRendererObj.getStringWidth(price1);
        int price2Width = fontRendererObj.getStringWidth(price2);

        int priceLineWidth = 16 + 4 + price1Width + 20 + 16 + 4 + price2Width;
        int tooltipWidth = Math.max(Math.max(nameWidth, descWidth), priceLineWidth) + 20;
        tooltipWidth = Math.max(tooltipWidth, 120);
        int lineHeight = fontRendererObj.FONT_HEIGHT + 2;
        int padding = 8;
        int tooltipHeight = padding * 2 + lineHeight * 2 + lineHeight + 20;
        int tooltipX = mouseX - guiLeft + 20;
        int tooltipY = mouseY - guiTop - tooltipHeight / 2;

        if (tooltipY < 0) {
            tooltipY = mouseY - guiTop + 20;
        }
        if (tooltipY + tooltipHeight > height) {
            tooltipY = height - tooltipHeight - 5;
        }

        drawTooltipBg(tooltipX, tooltipY, tooltipWidth, tooltipHeight);
        int currentY = tooltipY + padding;
        fontRendererObj.drawStringWithShadow(displayName, tooltipX + padding, currentY, 0xd6942e);
        currentY += lineHeight;
        fontRendererObj.drawStringWithShadow(description, tooltipX + padding, currentY, 0xaaaaaa);
        currentY += lineHeight + 4;
        fontRendererObj.drawStringWithShadow("Цена:", tooltipX + padding, currentY, 0xFFFFFF);
        currentY += lineHeight;

        renderPriceItem(item1, price1, tooltipX + padding, currentY);
        int firstItemWidth = 16 + 4 + price1Width;
        renderPriceItem(item2, price2, tooltipX + padding + firstItemWidth + 20, currentY);
    }

    private void renderPriceItem(ItemStack item, String price, int x, int y) {
        itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), item, x, y);
        fontRendererObj.drawStringWithShadow(price, x + 20, y + 4, 0xFFFFFF);
    }

    private void drawTooltipBg(int x, int y, int width, int height) {
        drawGradientRect(x, y, x + width, y + height, -267386864, -267386864);
        drawRect(x - 1, y - 1, x + width + 1, y, 0xFF808080);
        drawRect(x - 1, y + height, x + width + 1, y + height + 1, 0xFF808080);
        drawRect(x - 1, y, x, y + height, 0xFF808080);
        drawRect(x + width, y, x + width + 1, y + height, 0xFF808080);
    }
    
    public static void drawTextureCustomSize(double posX, double posY, double startPixX, double startPixY, double pieceSizeX, double pieceSizeY, float sizeTextureX, float sizeTextureY) {
        float f4 = 1.0F / sizeTextureX;
        float f5 = 1.0F / sizeTextureY;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(posX, posY + pieceSizeY, 0.0D, startPixX * f4, (startPixY + pieceSizeY) * f5);
        tessellator.addVertexWithUV(posX + pieceSizeX, posY + pieceSizeY, 0.0D, (startPixX + pieceSizeX) * f4, (startPixY + pieceSizeY) * f5);
        tessellator.addVertexWithUV(posX + pieceSizeX, posY, 0.0D, (startPixX + pieceSizeX) * f4, startPixY * f5);
        tessellator.addVertexWithUV(posX, posY, 0.0D, startPixX * f4, startPixY * f5);
        tessellator.draw();
    }

    private void drawSelectionBox(int x, int y, int width, int height) {
        int color = 0xFFFFFFFF;
        drawRect(x - 2, y - 2, x + width + 2, y - 1, color);
        drawRect(x - 2, y + height + 1, x + width + 2, y + height + 2, color);
        drawRect(x - 2, y - 1, x - 1, y + height + 2, color);
        drawRect(x + width + 1, y - 1, x + width + 2, y + height + 2, color);
    }

    @Optional.Method(modid = "NotEnoughItems")
    private void setNEIOverlayHidden(boolean value) {
        NEIClientConfig.getSetting("inventory.hidden").setBooleanValue(value);
    }

    @Optional.Method(modid = "NotEnoughItems")
    private boolean isNEIOverlayHidden() {
        return NEIClientConfig.isHidden();
    }

    class GuiCenteredTextField extends GuiTextField {

        FontRenderer renderer;
        public GuiCenteredTextField(FontRenderer fr, int x, int y, int w, int h) {
            super(fr, x, y, w, h);
            renderer = fr;
        }

        @Override                             
        public void drawTextBox() {
            if (!this.getVisible()) return;

            String text = this.getText();
            int textWidth = this.renderer.getStringWidth(text);

            int drawX = this.xPosition + (this.width - textWidth) / 2;

            this.renderer.drawString(text, drawX, this.yPosition + (this.height - 8) / 2, 0x000000);
        }
    }
}