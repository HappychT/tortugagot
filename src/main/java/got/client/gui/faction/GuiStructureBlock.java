package got.client.gui.faction;

import brain.factions.Faction;
import brain.factions.network.PacketInfoFactions;
import brain.factions.network.PacketMessage;
import brain.factions.network.PacketStructureAction;
import brain.factions.servers.BarracksManager;
import brain.factions.network.PacketFactionManage;
import brain.factions.structures.FactionStructureSlot;
import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import got.common.faction.GOTFaction;
import got.common.faction.GOTFactionRelations;
import got.client.gui.utils.GuiApi;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public class GuiStructureBlock extends GuiScreen {

    private final int x, y, z;
    private FactionStructureSlot structure;
    private Faction playerFaction;
    private ScreenState currentState;
    private FortressState fortressState = FortressState.MAIN;
    private Overlay activeOverlay = Overlay.NONE;
    private String selectedCategory = "";

    private List<BarracksManager.PlayerProfile> barracksPlayers = new ArrayList<>();
    private String selectedBarracksPlayerName;
    private List<String> availablePlayersForBarracks = new ArrayList<>();
    private Map<String, List<String>> availableStructures = new HashMap<>();

    private GuiTextField provisionsAmountField;
    private GuiTextField capacityAmountField;

    private List<GuiButton> mainButtons = new ArrayList<>();
    private List<GuiButton> overlayButtons = new ArrayList<>();

    private enum ScreenState { UNOWNED, OWNED_SELF, OWNED_ALLY, OWNED_ENEMY, PURCHASE_CATEGORY, PURCHASE_SUBTYPE }
    private enum FortressState { MAIN, BARRACKS }
    private enum Overlay { NONE, DEPOSIT_PROVISIONS, ADD_BARRACKS_PLAYER, INCREASE_CAPACITY }

    public GuiStructureBlock(int x, int y, int z, FactionStructureSlot structure) {
        this.x = x; this.y = y; this.z = z;
        this.structure = structure;

        GOTPlayerData pd = GOTLevelData.getData(Minecraft.getMinecraft().thePlayer);
        if (pd.getPledgeFaction() != null) {
            this.playerFaction = PacketInfoFactions.getFactions().get(pd.getPledgeFaction().codeName());
        }
        determineState();
    }

    private void determineState() {
        if (structure == null || structure.ownerFactionID == null) currentState = ScreenState.UNOWNED;
        else if (playerFaction != null && playerFaction.getID().equals(structure.ownerFactionID)) currentState = ScreenState.OWNED_SELF;
        else {
            GOTFaction ownerFaction = GOTFaction.forName(structure.ownerFactionID);
            GOTFaction playerGOTFaction = (playerFaction != null) ? GOTFaction.forName(playerFaction.getID()) : null;
            if (playerGOTFaction != null && ownerFaction != null && GOTFactionRelations.areFactionsHostile(ownerFaction, playerGOTFaction)) currentState = ScreenState.OWNED_ENEMY;
            else currentState = ScreenState.OWNED_ALLY;
        }
    }

    public void updateData(FactionStructureSlot newSlot, Map<String, List<String>> structures) {
        this.structure = newSlot;
        this.availableStructures = structures;
        if (this.currentState != ScreenState.PURCHASE_CATEGORY && this.currentState != ScreenState.PURCHASE_SUBTYPE) {
            determineState();
        }
        this.initGui();
    }

    public void receiveBarracksPlayers(List<BarracksManager.PlayerProfile> players) {
        this.barracksPlayers = players != null ? players : new ArrayList<>();
    }

    private void setCurrentOverlay(Overlay overlay) {
        this.activeOverlay = overlay;
        this.initGui();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.mainButtons.clear();
        this.overlayButtons.clear();

        if (activeOverlay != Overlay.NONE) {
            initOverlayButtons();
        } else {
            initMainButtons();
        }
        this.buttonList.clear();
        this.buttonList.addAll(mainButtons);
        this.buttonList.addAll(overlayButtons);
    }

    private void initMainButtons() {
        int guiLeft = (width - 256) / 2;
        int guiTop = (height - 200) / 2;

        GuiButton backButton = new GuiButton(100, guiLeft + 10, guiTop + 170, 100, 20, "Назад");
        GuiButton closeButton = new GuiButton(99, guiLeft + 146, guiTop + 170, 100, 20, "Закрыть");
        mainButtons.add(backButton);
        mainButtons.add(closeButton);

        backButton.visible = (fortressState != FortressState.MAIN || currentState == ScreenState.PURCHASE_CATEGORY || currentState == ScreenState.PURCHASE_SUBTYPE);

        switch (currentState) {
            case UNOWNED:
                mainButtons.add(new GuiButton(0, guiLeft + (256 - 150) / 2, guiTop + 80, 150, 20, "Захватить точку"));
                break;
            case OWNED_SELF:
                if (structure.type == FactionStructureSlot.StructureType.FORTRESS) setupFortressButtons(guiLeft, guiTop);
                else setupResourcePointButtons(guiLeft, guiTop);
                break;
            case PURCHASE_CATEGORY:
            case PURCHASE_SUBTYPE:
                setupPurchaseButtons(guiLeft, guiTop);
                break;
        }
    }

    private void initOverlayButtons() {
        int overlayX = width / 2;
        int overlayY = height / 2;

        GuiButton confirmButton = new GuiButton(200, overlayX - 102, overlayY + 20, 100, 20, "Подтвердить");
        GuiButton cancelButton = new GuiButton(201, overlayX + 2, overlayY + 20, 100, 20, "Отмена");
        overlayButtons.add(confirmButton);
        overlayButtons.add(cancelButton);

        switch(activeOverlay) {
            case DEPOSIT_PROVISIONS:
                provisionsAmountField = new GuiTextField(fontRendererObj, overlayX - 100, overlayY - 10, 200, 20);
                provisionsAmountField.setFocused(true);
                break;
            case INCREASE_CAPACITY:
                capacityAmountField = new GuiTextField(fontRendererObj, overlayX - 100, overlayY - 10, 200, 20);
                capacityAmountField.setFocused(true);
                break;
            case ADD_BARRACKS_PLAYER:
                overlayButtons.clear();
                overlayButtons.add(new GuiButton(201, overlayX - 50, overlayY + 70, 100, 20, "Отмена"));
                break;
        }
    }

    private String getCategoryDisplayName(String categoryKey) {
        if (categoryKey == null) return "";
        switch (categoryKey.toUpperCase()) {
            case "FARMS": return "Фермы";
            case "INDUSTRY": return "Промышленность";
            case "BARN": return "Амбар";
            case "ENGINEERING_WORKSHOP": return "Инженерная мастерская";
            case "FORTRESS": return "Крепости";
            default: return categoryKey;
        }
    }

    private String getCategoryKey(String displayName) {
        if (displayName == null) return "";
        if ("Фермы".equals(displayName)) return "FARMS";
        if ("Промышленность".equals(displayName)) return "INDUSTRY";
        if ("Амбар".equals(displayName)) return "BARN";
        if ("Инженерная мастерская".equals(displayName)) return "ENGINEERING_WORKSHOP";
        if ("Крепости".equals(displayName)) return "FORTRESS";
        return displayName;
    }


    private void setupFortressButtons(int guiLeft, int guiTop) {
        boolean isMainFortress = playerFaction != null && structure.id.equals(playerFaction.getMainFortressId());

        if (fortressState == FortressState.MAIN) {
            if (isMainFortress) {
                mainButtons.add(new GuiButton(30, guiLeft + (256 - 150) / 2, guiTop + 100, 150, 20, "Внести продовольствие"));
                mainButtons.add(new GuiButton(31, guiLeft + (256 - 150) / 2, guiTop + 125, 150, 20, "Управление казармой"));
            } else {
                mainButtons.add(new GuiButton(1, guiLeft + (256 - 150) / 2, guiTop + 80, 150, 20, "Собрать ресурсы"));
            }
            mainButtons.add(new GuiButton(2, guiLeft + 200, guiTop + 10, 50, 20, "Улучшить"));

        } else if (fortressState == FortressState.BARRACKS) {
            mainButtons.add(new GuiButton(40, guiLeft + 180, guiTop + 40, 70, 20, "Добавить"));
            GuiButton kickBtn = new GuiButton(41, guiLeft + 180, guiTop + 65, 70, 20, "Исключить");
            kickBtn.enabled = selectedBarracksPlayerName != null;
            mainButtons.add(kickBtn);
            mainButtons.add(new GuiButton(42, guiLeft + 180, guiTop + 90, 70, 20, "Купить слот"));
        }
    }

    private void setupResourcePointButtons(int guiLeft, int guiTop) {
        mainButtons.add(new GuiButton(1, guiLeft + (256 - 150) / 2, guiTop + 80, 150, 20, "Собрать ресурсы"));
        mainButtons.add(new GuiButton(2, guiLeft + (256 - 150) / 2, guiTop + 105, 150, 20, "Улучшить"));
    }

    private void setupPurchaseButtons(int guiLeft, int guiTop) {
        int yPos = guiTop + 50;
        int buttonWidth = 150;
        int buttonX = guiLeft + (256 - buttonWidth) / 2;
        int id = 10;
        if (currentState == ScreenState.PURCHASE_CATEGORY) {
            for (String categoryKey : this.availableStructures.keySet()) {
                mainButtons.add(new GuiButton(id++, buttonX, yPos, buttonWidth, 20, getCategoryDisplayName(categoryKey)));
                yPos += 25;
            }
        } else if (currentState == ScreenState.PURCHASE_SUBTYPE) {
            List<String> subTypes = this.availableStructures.get(selectedCategory);
            if (subTypes != null) {
                for (String subTypeName : subTypes) {
                    mainButtons.add(new GuiButton(id++, buttonX, yPos, buttonWidth, 20, subTypeName));
                    yPos += 25;
                }
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (!button.enabled) return;

        if (activeOverlay != Overlay.NONE) {
            handleOverlayActions(button);
            return;
        }

        if (button.id == 99) {
            mc.displayGuiScreen(null);
            return;
        }
        if (button.id == 100) {
            handleBackButton();
            initGui();
            return;
        }

        switch (currentState) {
            case UNOWNED:
                if (button.id == 0) {
                    currentState = ScreenState.PURCHASE_CATEGORY;
                    initGui();
                }
                break;
            case OWNED_SELF:
                handleOwnedSelfActions(button);
                break;
            case PURCHASE_CATEGORY:
                if (button.id >= 10) {
                    selectedCategory = getCategoryKey(button.displayString);
                    currentState = ScreenState.PURCHASE_SUBTYPE;
                    initGui();
                }
                break;
            case PURCHASE_SUBTYPE:
                if (button.id >= 10 && this.structure != null) {
                    brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("purchase", structure.id, selectedCategory, button.displayString));
                    mc.displayGuiScreen(null);
                }
                break;
        }
    }

    private void handleOverlayActions(GuiButton button) {
        if (button.id == 201) {
            setCurrentOverlay(Overlay.NONE);
            return;
        }

        if (button.id == 200) {
            try {
                if (activeOverlay == Overlay.DEPOSIT_PROVISIONS) {
                    int amount = Integer.parseInt(provisionsAmountField.getText());
                    if (amount > 0) brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.depositProvisions(amount, structure.id));
                } else if (activeOverlay == Overlay.INCREASE_CAPACITY) {
                    int amount = Integer.parseInt(capacityAmountField.getText());
                    if (amount > 0) brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.increaseBarracksCapacity(structure.id, amount));
                }
            } catch (NumberFormatException e) {
            }
            setCurrentOverlay(Overlay.NONE);
        }
    }

    private void handleBackButton() {
        if (fortressState == FortressState.BARRACKS) {
            fortressState = FortressState.MAIN;
            selectedBarracksPlayerName = null;
        } else if (currentState == ScreenState.PURCHASE_SUBTYPE) {
            currentState = ScreenState.PURCHASE_CATEGORY;
            selectedCategory = "";
        } else if (currentState == ScreenState.PURCHASE_CATEGORY) {
            currentState = ScreenState.UNOWNED;
        }
    }

    private void handleOwnedSelfActions(GuiButton button) {
        boolean stateChanged = false;
        boolean isMainFortress = playerFaction != null && structure.id.equals(playerFaction.getMainFortressId());

        if (button.id == 1) brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("collect", structure.id));
        if (button.id == 2) brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("upgrade", structure.id));

        if (isMainFortress) {
            if (button.id == 30) {
                setCurrentOverlay(Overlay.DEPOSIT_PROVISIONS);
                return;
            }
            if (button.id == 31) {
                fortressState = FortressState.BARRACKS;
                brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketMessage("requestBarracksPlayers#" + structure.id));
                stateChanged = true;
            }
        }

        if (fortressState == FortressState.BARRACKS) {
            if (button.id == 40) {
                Set<String> allPlayers = playerFaction != null ? playerFaction.getPlayers().keySet() : null;
                if (allPlayers != null) {
                    Set<String> barracksPlayerNames = barracksPlayers.stream().map(BarracksManager.PlayerProfile::getName).collect(Collectors.toSet());
                    this.availablePlayersForBarracks = allPlayers.stream().filter(name -> !barracksPlayerNames.contains(name)).sorted().collect(Collectors.toList());
                }
                setCurrentOverlay(Overlay.ADD_BARRACKS_PLAYER);
                return;
            }
            if (button.id == 41 && selectedBarracksPlayerName != null) {
                brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.barracksAction("remove_player", selectedBarracksPlayerName, structure.id));
                selectedBarracksPlayerName = null;
                stateChanged = true;
            }
            if (button.id == 42) {
                setCurrentOverlay(Overlay.INCREASE_CAPACITY);
                return;
            }
        }
        if (stateChanged) {
            initGui();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (activeOverlay == Overlay.NONE) {
            drawDefaultBackground();
            drawMainScreen(mouseX, mouseY, partialTicks);
        } else {
            drawMainScreen(mouseX, mouseY, partialTicks);
            drawRect(0, 0, this.width, this.height, 0x90000000);
            drawOverlay(mouseX, mouseY, partialTicks);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawMainScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int guiLeft = (width - 256) / 2;
        int guiTop = (height - 200) / 2;
        String title = (structure != null && structure.name != null && !structure.name.isEmpty()) ? structure.name : "Нейтральная территория";
        this.drawCenteredString(this.fontRendererObj, title, width / 2, guiTop + 15, 0xFFFFFF);

        switch (currentState) {
            case UNOWNED:
                this.drawCenteredString(this.fontRendererObj, "Эта точка никем не занята.", width / 2, guiTop + 45, 0xAAAAAA);
                if (structure != null) this.drawCenteredString(this.fontRendererObj, "Цена захвата: §e" + structure.price, width / 2, guiTop + 60, 0xFFFFFF);
                break;
            case OWNED_SELF:
                if (structure.type == FactionStructureSlot.StructureType.FORTRESS) {
                    if (fortressState == FortressState.MAIN) drawFortressMainScreen(guiTop + 35);
                    else if (fortressState == FortressState.BARRACKS) drawFortressBarracksScreen(guiLeft, guiTop, mouseX, mouseY);
                } else {
                    this.drawCenteredString(this.fontRendererObj, "Владелец: §aВаша фракция", width / 2, guiTop + 45, 0xFFFFFF);
                    this.drawCenteredString(this.fontRendererObj, "Уровень: §b" + structure.level, width / 2, guiTop + 57, 0xFFFFFF);
                }
                break;
            case OWNED_ALLY: case OWNED_ENEMY:
                if (structure != null && structure.ownerFactionID != null) {
                    String ownerName = GOTFaction.forName(structure.ownerFactionID).factionName();
                    String color = (currentState == ScreenState.OWNED_ALLY) ? "§2" : "§c";
                    this.drawCenteredString(this.fontRendererObj, "Владелец: " + color + ownerName, width / 2, guiTop + 45, 0xFFFFFF);
                    this.drawCenteredString(this.fontRendererObj, "Прочность: §c" + structure.health, width / 2, guiTop + 60, 0xFFFFFF);
                }
                break;
            case PURCHASE_CATEGORY:
                this.drawCenteredString(this.fontRendererObj, "Выберите категорию постройки:", width / 2, guiTop + 35, 0xFFFFFF);
                break;
            case PURCHASE_SUBTYPE:
                this.drawCenteredString(this.fontRendererObj, "Выберите тип постройки:", width / 2, guiTop + 35, 0xFFFFFF);
                break;
        }
    }

    private void drawOverlay(int mouseX, int mouseY, float partialTicks) {
        int overlayX = width / 2;
        int overlayY = height / 2;

        switch(activeOverlay) {
            case DEPOSIT_PROVISIONS:
                this.drawCenteredString(this.fontRendererObj, "Внести продовольствие", overlayX, overlayY - 30, 0xFFFFFF);
                this.drawString(this.fontRendererObj, "Количество:", overlayX - 100, overlayY - 22, 0xA0A0A0);
                provisionsAmountField.drawTextBox();
                break;
            case INCREASE_CAPACITY:
                this.drawCenteredString(this.fontRendererObj, "Увеличить вместимость казармы", overlayX, overlayY - 40, 0xFFFFFF);
                if(structure != null) this.drawCenteredString(this.fontRendererObj, "Стоимость: " + 500 + " прод. за 1 слот", overlayX, overlayY - 28, 0xAAAAAA);
                this.drawString(this.fontRendererObj, "Количество:", overlayX - 100, overlayY - 22, 0xA0A0A0);
                capacityAmountField.drawTextBox();
                break;
            case ADD_BARRACKS_PLAYER:
                this.drawCenteredString(this.fontRendererObj, "Добавить игрока в казарму", overlayX, overlayY - 80, 0xFFFFFF);
                int listX = overlayX - 100, listY = overlayY - 60, listW = 200, listH = 120;
                Gui.drawRect(listX, listY, listX + listW, listY + listH, 0x80000000);
                for (int i = 0; i < availablePlayersForBarracks.size(); i++) {
                    String name = availablePlayersForBarracks.get(i);
                    int entryY = listY + 5 + i * 15;
                    boolean hovered = mouseX >= listX && mouseX < listX+listW && mouseY >= entryY && mouseY < entryY + 15;
                    this.drawCenteredString(this.fontRendererObj, (hovered ? "§a" : "") + name, overlayX, entryY, 0xFFFFFF);
                }
                break;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        if (activeOverlay != Overlay.NONE) {
            handleOverlayMouseClick(mouseX, mouseY, button);
            return;
        }

        if (button == 0) {
            for (Object obj : new ArrayList(this.buttonList)) {
                GuiButton guiButton = (GuiButton) obj;
                if (guiButton.mousePressed(this.mc, mouseX, mouseY)) {
                    guiButton.func_146113_a(this.mc.getSoundHandler());
                    this.actionPerformed(guiButton);
                    return;
                }
            }
        }


        if (currentState == ScreenState.OWNED_SELF && fortressState == FortressState.BARRACKS) {
            int listX = (width - 256) / 2 + 10;
            int listY = (height - 200) / 2 + 40;
            int listWidth = 160;
            if (mouseX >= listX && mouseX < listX + listWidth) {
                int slot = (mouseY - listY) / 15;
                if (slot >= 0 && slot < barracksPlayers.size()) {
                    selectedBarracksPlayerName = barracksPlayers.get(slot).getName();
                    initGui();
                }
            }
        }

        super.mouseClicked(mouseX, mouseY, button);
    }

    private void handleOverlayMouseClick(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (activeOverlay == Overlay.DEPOSIT_PROVISIONS) provisionsAmountField.mouseClicked(mouseX, mouseY, button);
        if (activeOverlay == Overlay.INCREASE_CAPACITY) capacityAmountField.mouseClicked(mouseX, mouseY, button);

        if (activeOverlay == Overlay.ADD_BARRACKS_PLAYER) {
            int listX = width/2 - 100, listY = height/2 - 60, listW = 200;
            if (mouseX >= listX && mouseX < listX + listW) {
                int slot = (mouseY - (listY + 5)) / 15;
                if(slot >= 0 && slot < availablePlayersForBarracks.size()) {
                    String name = availablePlayersForBarracks.get(slot);
                    brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.barracksAction("add_player", name, structure.id));
                    setCurrentOverlay(Overlay.NONE);
                }
            }
        }
    }

    @Override
    protected void keyTyped(char c, int key) {
        if (activeOverlay != Overlay.NONE) {
            if (key == 1) {
                setCurrentOverlay(Overlay.NONE);
                return;
            }
            if (activeOverlay == Overlay.DEPOSIT_PROVISIONS && provisionsAmountField.isFocused()) provisionsAmountField.textboxKeyTyped(c, key);
            if (activeOverlay == Overlay.INCREASE_CAPACITY && capacityAmountField.isFocused()) capacityAmountField.textboxKeyTyped(c, key);
        } else {
            super.keyTyped(c, key);
        }
    }

    private void drawFortressMainScreen(int yPos) {
        if (structure == null) return;
        boolean isMainFortress = playerFaction != null && structure.id.equals(playerFaction.getMainFortressId());

        this.drawCenteredString(this.fontRendererObj, "Владелец: §aВаша фракция", width / 2, yPos, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, "Уровень: §b" + structure.level, width / 2, yPos + 12, 0xFFFFFF);

        if (isMainFortress) {
            this.drawCenteredString(this.fontRendererObj, "§a(Главная крепость)", width / 2, yPos - 12, 0x55FF55);
            this.drawCenteredString(this.fontRendererObj, "Продовольствие: §6" + structure.provisions, width / 2, yPos + 24, 0xFFFFFF);
            this.drawCenteredString(this.fontRendererObj, "Казарма: §3" + barracksPlayers.size() + " / " + structure.barracksCapacity, width / 2, yPos + 36, 0xFFFFFF);
        } else {
            this.drawCenteredString(this.fontRendererObj, "§e(Крепость)", width / 2, yPos - 12, 0xFFFF55);
            this.drawCenteredString(this.fontRendererObj, "Здесь можно собрать ресурсы.", width / 2, yPos + 24, 0xAAAAAA);
        }
    }

    private void drawFortressBarracksScreen(int guiLeft, int guiTop, int mouseX, int mouseY) {
        int listX = guiLeft + 10;
        int listY = guiTop + 40;
        int listWidth = 160;
        int listHeight = 120;

        this.drawCenteredString(this.fontRendererObj, "Управление гарнизоном", width / 2, guiTop + 25, 0xFFFFFF);
        Gui.drawRect(listX, listY, listX + listWidth, listY + listHeight, 0x80000000);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GuiApi.glScissor(listX, listY, listWidth, listHeight, false);

        for (int i = 0; i < barracksPlayers.size(); i++) {
            BarracksManager.PlayerProfile player = barracksPlayers.get(i);
            int entryY = listY + i * 15;
            boolean selected = player.getName().equals(selectedBarracksPlayerName);
            boolean hovered = mouseX >= listX && mouseX < listWidth && mouseY >= entryY && mouseY < entryY + 15;

            int bgColor = selected ? 0x80FFFFFF : (hovered ? 0x50FFFFFF : 0);
            if (bgColor != 0) Gui.drawRect(listX, entryY, listX + listWidth, entryY + 15, bgColor);

            this.fontRendererObj.drawString(player.getName(), listX + 5, entryY + 3, 0xFFFFFF);
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}