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
    private ScreenState currentState = ScreenState.UNOWNED;
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

    private final int xSize = 340;
    private final int ySize = 200;

    private enum ScreenState { UNOWNED, OWNED_SELF, OWNED_ALLY, OWNED_ENEMY, PURCHASE_META_CATEGORY, PURCHASE_CATEGORY, PURCHASE_SUBTYPE }
    private enum FortressState { MAIN, BARRACKS, SIEGE_EQUIPMENT }
    private enum Overlay { NONE, DEPOSIT_PROVISIONS, ADD_BARRACKS_PLAYER, INCREASE_CAPACITY }

    public GuiStructureBlock(int x, int y, int z, FactionStructureSlot initialStructure) {
        this.x = x; this.y = y; this.z = z;
        this.structure = initialStructure;
        this.availableStructures = new HashMap<>();

        GOTPlayerData pd = GOTLevelData.getData(Minecraft.getMinecraft().thePlayer);
        if (pd != null && pd.getPledgeFaction() != null) {
            this.playerFaction = PacketInfoFactions.getFactions().get(pd.getPledgeFaction().codeName());
        }
        determineState();
    }

    private void determineState() {
        if (structure == null || structure.ownerFactionID == null) {
            currentState = ScreenState.UNOWNED;
        } else if (playerFaction != null && playerFaction.getID() != null && playerFaction.getID().equals(structure.ownerFactionID)) {
            currentState = ScreenState.OWNED_SELF;
        } else {
            GOTFaction ownerFaction = GOTFaction.forName(structure.ownerFactionID);
            GOTFaction playerGOTFaction = (playerFaction != null && playerFaction.getID() != null) ? GOTFaction.forName(playerFaction.getID()) : null;

            if (playerGOTFaction != null && ownerFaction != null && GOTFactionRelations.areFactionsHostile(ownerFaction, playerGOTFaction)) {
                currentState = ScreenState.OWNED_ENEMY;
            } else {
                currentState = ScreenState.OWNED_ALLY;
            }
        }
        if (currentState != ScreenState.PURCHASE_META_CATEGORY && currentState != ScreenState.PURCHASE_CATEGORY && currentState != ScreenState.PURCHASE_SUBTYPE && structure != null && structure.ownerFactionID == null) {
            currentState = ScreenState.UNOWNED;
        }
    }

    public void updateData(FactionStructureSlot newSlot, Map<String, List<String>> structures) {
        this.structure = newSlot;
        this.availableStructures = structures != null ? structures : new HashMap<>();

        if (currentState != ScreenState.PURCHASE_META_CATEGORY && currentState != ScreenState.PURCHASE_CATEGORY && currentState != ScreenState.PURCHASE_SUBTYPE) {
            determineState();
        } else if (newSlot != null && newSlot.ownerFactionID != null) {
            determineState();
        }

        if (this.mc != null && this.mc.currentScreen == this) {
            this.initGui();
        }
    }

    public void receiveBarracksPlayers(List<BarracksManager.PlayerProfile> players) {
        this.barracksPlayers = players != null ? players : new ArrayList<>();
        if (currentState == ScreenState.OWNED_SELF && fortressState == FortressState.BARRACKS) {
            initGui();
        }
    }

    private void setCurrentOverlay(Overlay overlay) {
        this.activeOverlay = overlay;
        this.initGui();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        this.mainButtons.clear();
        this.overlayButtons.clear();

        int guiLeft = (width - xSize) / 2;
        int guiTop = (height - ySize) / 2;

        if (activeOverlay != Overlay.NONE) {
            initOverlayButtons();
        }

        if (currentState == ScreenState.UNOWNED) {
            mainButtons.add(new GuiCustomButton(0, guiLeft + (xSize - 120) / 2, guiTop + 130, 120, 20, "Захватить"));
        } else if (currentState == ScreenState.OWNED_SELF) {

            if (structure.category == FactionStructureSlot.StructureCategory.FORTRESS && fortressState == FortressState.MAIN) {
                mainButtons.add(new GuiCustomButton(80, guiLeft + 270, guiTop + 45, 50, 16, "Улучш."));
                mainButtons.add(new GuiCustomButton(87, guiLeft + 215, guiTop + 45, 50, 16, "Внести"));

                mainButtons.add(new GuiCustomButton(81, guiLeft + 270, guiTop + 65, 50, 16, "Улучш."));
                mainButtons.add(new GuiCustomButton(82, guiLeft + 215, guiTop + 65, 50, 16, "Войти"));
                mainButtons.add(new GuiCustomButton(83, guiLeft + 270, guiTop + 85, 50, 16, "Улучш."));
                mainButtons.add(new GuiCustomButton(84, guiLeft + 215, guiTop + 85, 50, 16, "Осадки"));
                mainButtons.add(new GuiCustomButton(85, guiLeft + 270, guiTop + 105, 50, 16, "Улучш."));
                mainButtons.add(new GuiCustomButton(86, guiLeft + 215, guiTop + 105, 50, 16, "Купить"));

                mainButtons.add(new GuiCustomButton(2, guiLeft + (xSize - 140) / 2, guiTop + 160, 140, 20, "Улучшить Крепость"));
            } else if (structure.category == FactionStructureSlot.StructureCategory.FORTRESS && fortressState == FortressState.BARRACKS) {
                mainButtons.add(new GuiCustomButton(10, guiLeft + 20, guiTop + 20, 50, 20, "Назад"));
                mainButtons.add(new GuiCustomButton(42, guiLeft + xSize - 110, guiTop + 20, 90, 20, "Расширить"));

                mainButtons.add(new GuiCustomButton(11, guiLeft + (xSize / 2) - 105, guiTop + 165, 100, 20, "Добавить"));
                GuiButton kickBtn = new GuiCustomButton(41, guiLeft + (xSize / 2) + 5, guiTop + 165, 100, 20, "Исключить");
                if (selectedBarracksPlayerName == null) kickBtn.enabled = false;
                mainButtons.add(kickBtn);

            } else if (structure.category == FactionStructureSlot.StructureCategory.FORTRESS && fortressState == FortressState.SIEGE_EQUIPMENT) {
                mainButtons.add(new GuiCustomButton(10, guiLeft + 20, guiTop + 20, 50, 20, "Назад"));

                mainButtons.add(new GuiCustomButton(90, guiLeft + 35, guiTop + 75, 120, 20, "Баллиста (18k)"));
                mainButtons.add(new GuiCustomButton(91, guiLeft + 185, guiTop + 75, 120, 20, "Катапульта (25k)"));
                mainButtons.add(new GuiCustomButton(92, guiLeft + 35, guiTop + 100, 120, 20, "Требушет (30k)"));
                mainButtons.add(new GuiCustomButton(93, guiLeft + 185, guiTop + 100, 120, 20, "Таран (40k)"));
            } else {
                setupResourcePointButtons(guiLeft, guiTop);
            }

        } else if (currentState == ScreenState.PURCHASE_META_CATEGORY) {
            mainButtons.add(new GuiCustomButton(15, guiLeft + (xSize - 140) / 2, guiTop + 70, 140, 20, "Ресурсная точка"));
            mainButtons.add(new GuiCustomButton(16, guiLeft + (xSize - 140) / 2, guiTop + 95, 140, 20, "Крепость"));
            mainButtons.add(new GuiCustomButton(99, guiLeft + (xSize - 140) / 2, guiTop + 160, 140, 20, "Отмена"));
        } else if (currentState == ScreenState.PURCHASE_CATEGORY) {
            int y = guiTop + 60;
            int id = 20;
            for (String category : availableStructures.keySet()) {
                mainButtons.add(new GuiCustomButton(id++, guiLeft + (xSize - 140) / 2, y, 140, 20, getCategoryDisplayName(category)));
                y += 25;
            }
            mainButtons.add(new GuiCustomButton(99, guiLeft + (xSize - 140) / 2, guiTop + 160, 140, 20, "Отмена"));
        } else if (currentState == ScreenState.PURCHASE_SUBTYPE) {
            int y = guiTop + 60;
            int id = 40;
            List<String> subTypes = availableStructures.get(selectedCategory);
            if (subTypes != null) {
                for (String subType : subTypes) {
                    mainButtons.add(new GuiCustomButton(id++, guiLeft + (xSize - 140) / 2, y, 140, 20, subType));
                    y += 25;
                }
            }
            mainButtons.add(new GuiCustomButton(100, guiLeft + (xSize - 140) / 2, guiTop + 160, 140, 20, "Назад"));
        }

        if (activeOverlay == Overlay.NONE) {
            for (GuiButton btn : mainButtons) buttonList.add(btn);
        } else {
            for (GuiButton btn : overlayButtons) buttonList.add(btn);
        }
    }

    private void initOverlayButtons() {
        int overlayX = width / 2;
        int overlayY = height / 2;

        GuiButton confirmButton = new GuiCustomButton(200, overlayX - 102, overlayY + 20, 100, 20, "Подтвердить");
        GuiButton cancelButton = new GuiCustomButton(201, overlayX + 2, overlayY + 20, 100, 20, "Отмена");
        overlayButtons.add(confirmButton);
        overlayButtons.add(cancelButton);

        switch(activeOverlay) {
            case DEPOSIT_PROVISIONS:
                provisionsAmountField = new GuiTextField(fontRendererObj, overlayX - 100, overlayY - 10, 200, 20);
                provisionsAmountField.setFocused(true);
                provisionsAmountField.setMaxStringLength(9);
                break;
            case INCREASE_CAPACITY:
                capacityAmountField = new GuiTextField(fontRendererObj, overlayX - 100, overlayY - 10, 200, 20);
                capacityAmountField.setFocused(true);
                capacityAmountField.setMaxStringLength(9);
                break;
            case ADD_BARRACKS_PLAYER:
                overlayButtons.clear();
                overlayButtons.add(new GuiCustomButton(201, overlayX - 50, overlayY + 70, 100, 20, "Отмена"));
                break;
            case NONE:
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
            case "STABLE": return "Конюшни";
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
        if ("Конюшни".equals(displayName)) return "STABLE";
        return displayName;
    }

    private void setupResourcePointButtons(int guiLeft, int guiTop) {
        if (structure != null && structure.category == FactionStructureSlot.StructureCategory.BARN) {
            mainButtons.add(new GuiCustomButton(30, guiLeft + (xSize - 150) / 2, guiTop + 90, 150, 20, "Внести еду"));
            mainButtons.add(new GuiCustomButton(2, guiLeft + (xSize - 150) / 2, guiTop + 115, 150, 20, "Улучшить"));
        } else if (structure != null && structure.category == FactionStructureSlot.StructureCategory.ENGINEERING_WORKSHOP) {
            mainButtons.add(new GuiCustomButton(50, guiLeft + 35, guiTop + 85, 120, 20, "Баллиста (18k)"));
            mainButtons.add(new GuiCustomButton(51, guiLeft + 185, guiTop + 85, 120, 20, "Катапульта (25k)"));
            mainButtons.add(new GuiCustomButton(52, guiLeft + 35, guiTop + 110, 120, 20, "Требушет (30k)"));
            mainButtons.add(new GuiCustomButton(53, guiLeft + 185, guiTop + 110, 120, 20, "Таран (40k)"));
            mainButtons.add(new GuiCustomButton(2, guiLeft + (xSize - 150) / 2, guiTop + 135, 150, 20, "Улучшить"));
        } else if (structure != null && structure.category == FactionStructureSlot.StructureCategory.STABLE) {
            mainButtons.add(new GuiCustomButton(60, guiLeft + (xSize - 150) / 2, guiTop + 90, 150, 20, "Купить коня (5000)"));
            mainButtons.add(new GuiCustomButton(2, guiLeft + (xSize - 150) / 2, guiTop + 115, 150, 20, "Улучшить"));
        } else {
            mainButtons.add(new GuiCustomButton(1, guiLeft + (xSize - 150) / 2, guiTop + 90, 150, 20, "Собрать ресурсы"));
            mainButtons.add(new GuiCustomButton(2, guiLeft + (xSize - 150) / 2, guiTop + 115, 150, 20, "Улучшить"));
        }
        if (structure != null && structure.category != FactionStructureSlot.StructureCategory.FORTRESS) {
            mainButtons.add(new GuiCustomButton(70, guiLeft + 35, guiTop + 165, 120, 20, "Защита (Монеты)"));
            mainButtons.add(new GuiCustomButton(71, guiLeft + 185, guiTop + 165, 120, 20, "Защита (Дублоны)"));
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
            return;
        }

        switch (currentState) {
            case UNOWNED:
                if (button.id == 0) {
                    currentState = ScreenState.PURCHASE_META_CATEGORY;
                    initGui();
                }
                break;
            case PURCHASE_META_CATEGORY:
                if (button.id == 15) {
                    currentState = ScreenState.PURCHASE_CATEGORY;
                    initGui();
                } else if (button.id == 16) {
                    selectedCategory = "FORTRESS";
                    currentState = ScreenState.PURCHASE_SUBTYPE;
                    initGui();
                }
                break;
            case OWNED_SELF:
                handleOwnedSelfActions(button);
                break;
            case PURCHASE_CATEGORY:
                if (button.id >= 20 && button.id < 40) {
                    selectedCategory = getCategoryKey(button.displayString);
                    currentState = ScreenState.PURCHASE_SUBTYPE;
                    initGui();
                }
                break;
            case PURCHASE_SUBTYPE:
                if (button.id >= 40 && button.id < 90 && this.structure != null) {
                    brain.factions.servers.CoreFaction.brainChannel.sendToServer(
                            new PacketStructureAction("purchase", structure.id, selectedCategory, button.displayString)
                    );
                    mc.displayGuiScreen(null);
                }
                break;
            case OWNED_ALLY:
            case OWNED_ENEMY:
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
                if (activeOverlay == Overlay.DEPOSIT_PROVISIONS && provisionsAmountField != null) {
                    int amount = Integer.parseInt(provisionsAmountField.getText());
                    if (amount > 0 && structure != null) {
                        brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.depositProvisions(amount, structure.id));
                    } else if (amount <= 0) {
                        provisionsAmountField.setText("§c> 0!");
                        return;
                    }
                } else if (activeOverlay == Overlay.INCREASE_CAPACITY && capacityAmountField != null) {
                    int amount = Integer.parseInt(capacityAmountField.getText());
                    if (amount > 0 && structure != null) {
                        brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.increaseBarracksCapacity(structure.id, amount));
                    } else if (amount <= 0) {
                        capacityAmountField.setText("§c> 0!");
                        return;
                    }
                }
                setCurrentOverlay(Overlay.NONE);

            } catch (NumberFormatException e) {
                if (activeOverlay == Overlay.DEPOSIT_PROVISIONS && provisionsAmountField != null) provisionsAmountField.setText("§cОшибка!");
                if (activeOverlay == Overlay.INCREASE_CAPACITY && capacityAmountField != null) capacityAmountField.setText("§cОшибка!");
                return;
            }
        }
    }

    private void handleBackButton() {
        boolean needsReinit = false;
        if (activeOverlay != Overlay.NONE) {
            setCurrentOverlay(Overlay.NONE);
            return;
        }

        if (fortressState == FortressState.BARRACKS || fortressState == FortressState.SIEGE_EQUIPMENT) {
            fortressState = FortressState.MAIN;
            selectedBarracksPlayerName = null;
            needsReinit = true;
        } else if (currentState == ScreenState.PURCHASE_SUBTYPE) {
            if ("FORTRESS".equals(selectedCategory)) {
                currentState = ScreenState.PURCHASE_META_CATEGORY;
            } else {
                currentState = ScreenState.PURCHASE_CATEGORY;
            }
            selectedCategory = "";
            needsReinit = true;
        } else if (currentState == ScreenState.PURCHASE_CATEGORY) {
            currentState = ScreenState.PURCHASE_META_CATEGORY;
            needsReinit = true;
        } else if (currentState == ScreenState.PURCHASE_META_CATEGORY) {
            currentState = ScreenState.UNOWNED;
            needsReinit = true;
        }

        if (needsReinit) {
            initGui();
        }
    }

    private void handleOwnedSelfActions(GuiButton button) {
        boolean stateChanged = false;

        if (structure == null) return;

        if (button.id == 1) brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("collect", structure.id));
        if (button.id == 2) brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("upgrade", structure.id));

        if (structure.category == FactionStructureSlot.StructureCategory.BARN) {
            if (button.id == 30) {
                setCurrentOverlay(Overlay.DEPOSIT_PROVISIONS);
                return;
            }
        }
        if (structure.category == FactionStructureSlot.StructureCategory.ENGINEERING_WORKSHOP) {
            if (button.id == 50) brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("buy_siege", structure.id, "ballista", ""));
            if (button.id == 51) brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("buy_siege", structure.id, "catapult", ""));
            if (button.id == 52) brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("buy_siege", structure.id, "trebuchet", ""));
            if (button.id == 53) brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("buy_siege", structure.id, "ram", ""));
        }
        if (structure.category == FactionStructureSlot.StructureCategory.STABLE) {
            if (button.id == 60) {
                brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("buy_horse", structure.id));
                return;
            }
        }

        if (structure.category == FactionStructureSlot.StructureCategory.FORTRESS) {
            if (fortressState == FortressState.MAIN) {
                if (button.id == 80) brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("upgrade_sub", structure.id, "barn", ""));
                if (button.id == 87) {
                    setCurrentOverlay(Overlay.DEPOSIT_PROVISIONS);
                    return;
                }
                if (button.id == 81) brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("upgrade_sub", structure.id, "barracks", ""));
                if (button.id == 82) {
                    fortressState = FortressState.BARRACKS;
                    brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketMessage("requestBarracksPlayers#" + structure.id));
                    stateChanged = true;
                }
                if (button.id == 83) brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("upgrade_sub", structure.id, "workshop", ""));
                if (button.id == 84) {
                    fortressState = FortressState.SIEGE_EQUIPMENT;
                    stateChanged = true;
                }
                if (button.id == 85) brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("upgrade_sub", structure.id, "stable", ""));
                if (button.id == 86) brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("buy_horse", structure.id));

            } else if (fortressState == FortressState.BARRACKS) {
                if (button.id == 10) {
                    fortressState = FortressState.MAIN;
                    stateChanged = true;
                }
                if (button.id == 11 || button.id == 40) {
                    Set<String> allPlayers = (playerFaction != null && playerFaction.getPlayers() != null) ? playerFaction.getPlayers().keySet() : null;
                    if (allPlayers != null) {
                        Set<String> currentBarracksPlayers = barracksPlayers.stream().map(BarracksManager.PlayerProfile::getName).collect(Collectors.toSet());
                        this.availablePlayersForBarracks = allPlayers.stream()
                                .filter(name -> !currentBarracksPlayers.contains(name))
                                .sorted()
                                .collect(Collectors.toList());
                    } else {
                        this.availablePlayersForBarracks = new ArrayList<>();
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
            } else if (fortressState == FortressState.SIEGE_EQUIPMENT) {
                if (button.id == 10) {
                    fortressState = FortressState.MAIN;
                    stateChanged = true;
                }
                if (button.id == 90) brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("buy_siege", structure.id, "ballista", ""));
                if (button.id == 91) brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("buy_siege", structure.id, "catapult", ""));
                if (button.id == 92) brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("buy_siege", structure.id, "trebuchet", ""));
                if (button.id == 93) brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("buy_siege", structure.id, "ram", ""));
            }
        }

        if (structure.category != FactionStructureSlot.StructureCategory.FORTRESS) {
            if (button.id == 70) {
                brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("upgrade_security", structure.id, "coins", ""));
                return;
            }
            if (button.id == 71) {
                brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketStructureAction("upgrade_security", structure.id, "dubloons", ""));
                return;
            }
        }

        if (stateChanged) {
            initGui();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawMainScreen(mouseX, mouseY, partialTicks);

        if (activeOverlay == Overlay.NONE) {
            super.drawScreen(mouseX, mouseY, partialTicks);
        } else {
            for (GuiButton btn : mainButtons) {
                btn.drawButton(this.mc, mouseX, mouseY);
            }

            drawRect(0, 0, this.width, this.height, 0x90000000);
            drawOverlay(mouseX, mouseY, partialTicks);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    private void drawMainScreen(int mouseX, int mouseY, float partialTicks) {
        org.lwjgl.opengl.GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int guiLeft = (width - xSize) / 2;
        int guiTop = (height - ySize) / 2;

        String title = "Структура";
        if (structure != null && structure.name != null && !structure.name.isEmpty() && currentState != ScreenState.UNOWNED) {
            title = structure.name;
        } else if (currentState == ScreenState.UNOWNED || currentState == ScreenState.PURCHASE_META_CATEGORY || currentState == ScreenState.PURCHASE_CATEGORY || currentState == ScreenState.PURCHASE_SUBTYPE) {
            title = "Нейтральная территория";
        }
        this.drawCenteredString(this.fontRendererObj, title, width / 2, guiTop + 15, 0xFFFFFF);

        if (structure == null && currentState != ScreenState.PURCHASE_META_CATEGORY && currentState != ScreenState.PURCHASE_CATEGORY && currentState != ScreenState.PURCHASE_SUBTYPE) {
            this.drawCenteredString(fontRendererObj, "Загрузка данных...", width / 2, guiTop + 90, 0xAAAAAA);
            return;
        }

        switch (currentState) {
            case UNOWNED:
                this.drawCenteredString(this.fontRendererObj, "Эта точка никем не занята.", width / 2, guiTop + 45, 0xAAAAAA);
                if (structure != null) this.drawCenteredString(this.fontRendererObj, "Цена захвата: §e" + structure.price, width / 2, guiTop + 60, 0xFFFFFF);
                break;
            case OWNED_SELF:
                if (structure != null) {
                    if (structure.category == FactionStructureSlot.StructureCategory.FORTRESS) {
                        if (fortressState == FortressState.MAIN) drawFortressMainScreen(guiTop);
                        else if (fortressState == FortressState.BARRACKS) drawFortressBarracksScreen(guiLeft, guiTop, mouseX, mouseY);
                        else if (fortressState == FortressState.SIEGE_EQUIPMENT) drawFortressSiegeScreen(guiLeft, guiTop);
                    } else if (structure.category == FactionStructureSlot.StructureCategory.BARN) {
                        this.drawCenteredString(this.fontRendererObj, "Владелец: §aВаша фракция", width / 2, guiTop + 35, 0xFFFFFF);
                        this.drawCenteredString(this.fontRendererObj, "Уровень: §b" + structure.level, width / 2, guiTop + 47, 0xFFFFFF);
                        int capacity = brain.factions.servers.StructureManager.getGranaryCapacity(structure.level);
                        this.drawCenteredString(this.fontRendererObj, "Мест под еду: §6" + structure.storedFoodItems + " / " + capacity + " шт.", width / 2, guiTop + 59, 0xFFFFFF);
                        this.drawCenteredString(this.fontRendererObj, "Всего продовольствия: §e" + String.format("%.1f", structure.provisions).replace(',', '.'), width / 2, guiTop + 71, 0xFFFFFF);
                    } else if (structure.category == FactionStructureSlot.StructureCategory.ENGINEERING_WORKSHOP) {
                        this.drawCenteredString(this.fontRendererObj, "Владелец: §aВаша фракция", width / 2, guiTop + 35, 0xFFFFFF);
                        this.drawCenteredString(this.fontRendererObj, "Уровень: §b" + structure.level, width / 2, guiTop + 47, 0xFFFFFF);

                        long cooldownDays = 11 - structure.level;
                        if (cooldownDays < 1) cooldownDays = 1;
                        long cooldownMs = cooldownDays * 24 * 60 * 60 * 1000L;

                        if (System.currentTimeMillis() - structure.lastSiegePurchaseTime < cooldownMs) {
                            long timeLeft = cooldownMs - (System.currentTimeMillis() - structure.lastSiegePurchaseTime);
                            long daysLeft = timeLeft / (24 * 60 * 60 * 1000L);
                            long hoursLeft = (timeLeft / (60 * 60 * 1000L)) % 24;
                            this.drawCenteredString(this.fontRendererObj, "КД покупки: §c" + daysLeft + " д. " + hoursLeft + " ч.", width / 2, guiTop + 59, 0xFFFFFF);
                        } else {
                            this.drawCenteredString(this.fontRendererObj, "КД покупки: §aГотово", width / 2, guiTop + 59, 0xFFFFFF);
                        }
                    } else if (structure.category == FactionStructureSlot.StructureCategory.STABLE) {
                        this.drawCenteredString(this.fontRendererObj, "Владелец: §aВаша фракция", width / 2, guiTop + 35, 0xFFFFFF);
                        this.drawCenteredString(this.fontRendererObj, "Уровень: §b" + structure.level, width / 2, guiTop + 47, 0xFFFFFF);

                        double[] healthLevels = {18, 21, 24, 27, 30, 33, 36, 39, 42, 45};
                        double[] speedLevels = {0.22, 0.23, 0.25, 0.26, 0.28, 0.29, 0.31, 0.32, 0.33, 0.34};
                        int levelIndex = Math.max(0, Math.min(9, structure.level - 1));

                        this.drawCenteredString(this.fontRendererObj, "Здоровье коня: §c" + healthLevels[levelIndex] + " хп", width / 2, guiTop + 59, 0xFFFFFF);
                        this.drawCenteredString(this.fontRendererObj, "Скорость: §b" + speedLevels[levelIndex], width / 2, guiTop + 71, 0xFFFFFF);
                    } else {
                        this.drawCenteredString(this.fontRendererObj, "Владелец: §aВаша фракция", width / 2, guiTop + 35, 0xFFFFFF);
                        this.drawCenteredString(this.fontRendererObj, "Уровень: §b" + structure.level, width / 2, guiTop + 47, 0xFFFFFF);
                        this.drawCenteredString(this.fontRendererObj, "Защищенность: §a" + structure.securityLevel, width / 2, guiTop + 59, 0xFFFFFF);

                        int maxHp = 100 + (structure.securityLevel * 10);
                        this.drawCenteredString(this.fontRendererObj, "Прочность: §c" + (int)structure.destructionCount + " / " + maxHp, width / 2, guiTop + 71, 0xFFFFFF);
                    }
                }
                break;
            case OWNED_ALLY: case OWNED_ENEMY:
                if (structure != null && structure.ownerFactionID != null) {
                    GOTFaction ownerGotFaction = GOTFaction.forName(structure.ownerFactionID);
                    String ownerName = ownerGotFaction != null ? ownerGotFaction.factionName() : "Неизвестно";
                    String color = (currentState == ScreenState.OWNED_ALLY) ? "§2" : "§c";
                    this.drawCenteredString(this.fontRendererObj, "Владелец: " + color + ownerName, width / 2, guiTop + 45, 0xFFFFFF);
                    if (currentState == ScreenState.OWNED_ENEMY) {
                        int maxHpEnemy = (structure.category == FactionStructureSlot.StructureCategory.FORTRESS)
                                ? (100 + ((structure.level > 0 ? structure.level : 1) - 1) * 10)
                                : (100 + structure.securityLevel * 10);
                        this.drawCenteredString(this.fontRendererObj, "Прочность: §c" + (int)structure.destructionCount + " / " + maxHpEnemy, width / 2, guiTop + 60, 0xFFFFFF);
                    } else {
                        this.drawCenteredString(this.fontRendererObj, "Уровень: §b" + structure.level, width / 2, guiTop + 60, 0xFFFFFF);
                    }
                }
                break;
            case PURCHASE_META_CATEGORY:
                this.drawCenteredString(this.fontRendererObj, "Выберите тип объекта:", width / 2, guiTop + 35, 0xFFFFFF);
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
                if (provisionsAmountField != null) provisionsAmountField.drawTextBox();
                break;
            case INCREASE_CAPACITY:
                this.drawCenteredString(this.fontRendererObj, "Увеличить вместимость казармы", overlayX, overlayY - 40, 0xFFFFFF);
                int costPerSlot = 500;
                this.drawCenteredString(this.fontRendererObj, "Стоимость: " + costPerSlot + " прод. за 1 слот", overlayX, overlayY - 28, 0xAAAAAA);
                this.drawString(this.fontRendererObj, "Количество слотов:", overlayX - 100, overlayY - 22, 0xA0A0A0);
                if (capacityAmountField != null) capacityAmountField.drawTextBox();
                break;
            case ADD_BARRACKS_PLAYER:
                this.drawCenteredString(this.fontRendererObj, "Добавить игрока в казарму", overlayX, overlayY - 80, 0xFFFFFF);
                int listX = overlayX - 100, listY = overlayY - 60, listW = 200, listH = 120;
                Gui.drawRect(listX -1, listY -1, listX + listW + 1, listY + listH + 1, 0xFF000000);
                Gui.drawRect(listX, listY, listX + listW, listY + listH, 0x80000000);

                int displayY = listY + 5;
                for (int i = 0; i < availablePlayersForBarracks.size(); i++) {
                    if (displayY + fontRendererObj.FONT_HEIGHT > listY + listH) break;
                    String name = availablePlayersForBarracks.get(i);
                    boolean hovered = mouseX >= listX && mouseX < listX+listW && mouseY >= displayY -2 && mouseY < displayY + fontRendererObj.FONT_HEIGHT + 2;
                    this.drawCenteredString(this.fontRendererObj, (hovered ? "§a" : "") + name, overlayX, displayY, 0xFFFFFF);
                    displayY += 15;
                }
                break;
            case NONE:
                break;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        if (activeOverlay != Overlay.NONE) {
            handleOverlayMouseClick(mouseX, mouseY, button);
            if (button == 0) {
                for (GuiButton guiButton : new ArrayList<>(this.overlayButtons)) {
                    if (guiButton.mousePressed(this.mc, mouseX, mouseY)) {
                        guiButton.func_146113_a(this.mc.getSoundHandler());
                        this.actionPerformed(guiButton);
                        return;
                    }
                }
            }
            return;
        }

        if (button == 0) {
            for (GuiButton guiButton : new ArrayList<>(this.mainButtons)) {
                if (guiButton.mousePressed(this.mc, mouseX, mouseY)) {
                    guiButton.func_146113_a(this.mc.getSoundHandler());
                    this.actionPerformed(guiButton);
                    return;
                }
            }
        }

        if (currentState == ScreenState.OWNED_SELF && fortressState == FortressState.BARRACKS && structure != null) {
            int guiLeft = (width - xSize) / 2;
            int guiTop = (height - ySize) / 2;
            int listWidth = 200;
            int listHeight = 120;
            int listX = guiLeft + (xSize - listWidth) / 2;
            int listY = guiTop + 40;

            if (mouseX >= listX && mouseX < listX + listWidth && mouseY >= listY && mouseY < listY + listHeight) {
                int slotIndex = (mouseY - listY) / 15;
                if (slotIndex >= 0 && slotIndex < barracksPlayers.size()) {
                    selectedBarracksPlayerName = barracksPlayers.get(slotIndex).getName();
                    initGui();
                    return;
                }
            }
        }
    }

    private void handleOverlayMouseClick(int mouseX, int mouseY, int button) {
        if (activeOverlay == Overlay.DEPOSIT_PROVISIONS && provisionsAmountField != null) provisionsAmountField.mouseClicked(mouseX, mouseY, button);
        if (activeOverlay == Overlay.INCREASE_CAPACITY && capacityAmountField != null) capacityAmountField.mouseClicked(mouseX, mouseY, button);

        if (activeOverlay == Overlay.ADD_BARRACKS_PLAYER && button == 0 && structure != null) {
            int overlayX = width/2;
            int overlayY = height/2;
            int listX = overlayX - 100, listY = overlayY - 60, listW = 200, listH = 120;
            int entryStartY = listY + 5;
            int entryHeight = 15;

            if (mouseX >= listX && mouseX < listX + listW && mouseY >= listY && mouseY < listY + listH) {
                int clickedIndex = -1;
                for(int i = 0; i < availablePlayersForBarracks.size(); ++i) {
                    int currentEntryY = entryStartY + i * entryHeight;
                    if (mouseY >= currentEntryY -2 && mouseY < currentEntryY + fontRendererObj.FONT_HEIGHT + 2) {
                        clickedIndex = i;
                        break;
                    }
                }

                if(clickedIndex != -1) {
                    String name = availablePlayersForBarracks.get(clickedIndex);
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
            if (activeOverlay == Overlay.DEPOSIT_PROVISIONS && provisionsAmountField != null && provisionsAmountField.isFocused()) {
                if (Character.isDigit(c) || key == 14 || key == 211 || key == 203 || key == 205) {
                    provisionsAmountField.textboxKeyTyped(c, key);
                }
                return;
            }
            if (activeOverlay == Overlay.INCREASE_CAPACITY && capacityAmountField != null && capacityAmountField.isFocused()) {
                if (Character.isDigit(c) || key == 14 || key == 211 || key == 203 || key == 205) {
                    capacityAmountField.textboxKeyTyped(c, key);
                }
                return;
            }

        } else {
            if (key == 1 || key == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
                this.mc.thePlayer.closeScreen();
            }
        }
    }

    private void drawFortressMainScreen(int guiTop) {
        int guiLeft = (width - xSize) / 2;
        int maxFortressHp = 100 + ((structure.level > 0 ? structure.level : 1) - 1) * 10;

        this.drawCenteredString(this.fontRendererObj, "Крепость (Ур. " + structure.level + ") | Прочность: " + structure.destructionCount + " / " + maxFortressHp, width / 2, guiTop + 30, 0xFFD700);

        int barnCap = brain.factions.servers.StructureManager.getGranaryCapacity(structure.barnLevel);
        String provStr = String.format("%.1f", structure.provisions).replace(',', '.');
        this.drawString(this.fontRendererObj, "Амбар (Ур. " + structure.barnLevel + "): " + structure.storedFoodItems + " / " + barnCap + " шт. | " + provStr + " прод.", guiLeft + 20, guiTop + 49, 0xAAAAAA);

        this.drawString(this.fontRendererObj, "Казарма (Ур. " + structure.barracksLevel + "): " + structure.barracksCapacity + " мест", guiLeft + 20, guiTop + 69, 0xAAAAAA);

        long cooldownDays = 11 - structure.workshopLevel;
        if (cooldownDays < 1) cooldownDays = 1;
        long cooldownMs = cooldownDays * 24 * 60 * 60 * 1000L;
        String wsStatus = "Готова";
        if (System.currentTimeMillis() - structure.lastSiegePurchaseTime < cooldownMs) {
            long timeLeft = cooldownMs - (System.currentTimeMillis() - structure.lastSiegePurchaseTime);
            wsStatus = "КД: " + (timeLeft / (24 * 60 * 60 * 1000L)) + "д. " + ((timeLeft / (60 * 60 * 1000L)) % 24) + "ч.";
        }
        this.drawString(this.fontRendererObj, "Мастерская (Ур. " + structure.workshopLevel + "): " + wsStatus, guiLeft + 20, guiTop + 89, 0xAAAAAA);

        double[] healthLevels = {18, 21, 24, 27, 30, 33, 36, 39, 42, 45};
        int levelIndex = Math.max(0, Math.min(9, structure.stableLevel - 1));
        this.drawString(this.fontRendererObj, "Конюшня (Ур. " + structure.stableLevel + "): " + healthLevels[levelIndex] + " ХП", guiLeft + 20, guiTop + 109, 0xAAAAAA);
    }
    private void drawFortressBarracksScreen(int guiLeft, int guiTop, int mouseX, int mouseY) {
        int listWidth = 200;
        int listHeight = 120;
        int listX = guiLeft + (xSize - listWidth) / 2;
        int listY = guiTop + 40;

        this.drawCenteredString(this.fontRendererObj, "Управление гарнизоном", width / 2, guiTop + 25, 0xFFFFFF);
        Gui.drawRect(listX -1, listY -1, listX + listWidth + 1, listY + listHeight + 1, 0xFF000000);
        Gui.drawRect(listX, listY, listX + listWidth, listY + listHeight, 0x80000000);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GuiApi.glScissor(listX, listY, listWidth, listHeight, true);

        int displayY = listY;
        for (int i = 0; i < barracksPlayers.size(); i++) {
            BarracksManager.PlayerProfile player = barracksPlayers.get(i);
            int entryY = displayY + i * 15;

            boolean selected = player.getName().equals(selectedBarracksPlayerName);
            boolean hovered = mouseX >= listX && mouseX < listX + listWidth && mouseY >= entryY && mouseY < entryY + 15;

            int bgColor = selected ? 0xA0CCCCCC : (hovered ? 0x50FFFFFF : 0);
            if (bgColor != 0) Gui.drawRect(listX, entryY, listX + listWidth, entryY + 15, bgColor);

            this.fontRendererObj.drawString(player.getName(), listX + 5, entryY + 3, selected ? 0x000000 : 0xFFFFFF);
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    private void drawFortressSiegeScreen(int guiLeft, int guiTop) {
        this.drawCenteredString(this.fontRendererObj, "Заказ осадных орудий", width / 2, guiTop + 25, 0xFFFFFF);

        long cooldownDays = 11 - structure.workshopLevel;
        if (cooldownDays < 1) cooldownDays = 1;
        long cooldownMs = cooldownDays * 24 * 60 * 60 * 1000L;

        if (System.currentTimeMillis() - structure.lastSiegePurchaseTime < cooldownMs) {
            long timeLeft = cooldownMs - (System.currentTimeMillis() - structure.lastSiegePurchaseTime);
            long daysLeft = timeLeft / (24 * 60 * 60 * 1000L);
            long hoursLeft = (timeLeft / (60 * 60 * 1000L)) % 24;
            this.drawCenteredString(this.fontRendererObj, "Производство занято! КД: §c" + daysLeft + " д. " + hoursLeft + " ч.", width / 2, guiTop + 50, 0xFFFFFF);
        } else {
            this.drawCenteredString(this.fontRendererObj, "Производство: §aГотово к заказам", width / 2, guiTop + 50, 0xFFFFFF);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (provisionsAmountField != null) provisionsAmountField.updateCursorCounter();
        if (capacityAmountField != null) capacityAmountField.updateCursorCounter();
    }
}

