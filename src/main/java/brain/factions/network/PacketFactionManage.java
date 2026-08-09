package brain.factions.network;

import brain.factions.Faction;
import brain.factions.servers.BarracksManager;
import brain.factions.servers.CoreFaction;
import brain.factions.servers.ServerTaskExecutor;
import brain.factions.servers.StructureManager;
import brain.factions.structures.FactionStructureManager;
import brain.factions.structures.FactionStructureSlot;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import got.common.faction.GOTFaction;
import got.common.faction.GOTFactionRelations;
import got.common.faction.GOTFactionRelations.Relation;
import got.common.item.other.GOTItemCoin;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static brain.factions.servers.CoreFaction.brainChannel;

public class PacketFactionManage implements IMessage {
    private Action action;
    private String s_data1;
    private String s_data2;
    private long l_data1;

    public PacketFactionManage() {}

    private PacketFactionManage(Action action, String s_data1, String s_data2, long l_data1) {
        this.action = action;
        this.s_data1 = s_data1;
        this.s_data2 = s_data2;
        this.l_data1 = l_data1;
    }

    public static PacketFactionManage treasury(String action, long amount, String goalName) {
        return new PacketFactionManage(Action.TREASURY_ACTION, action, goalName, amount);
    }
    public static PacketFactionManage createTitle(String titleName, long permissions) {
        return new PacketFactionManage(Action.TITLE_CREATE, titleName, "", permissions);
    }
    public static PacketFactionManage politicsAction(String action, String targetFaction) {
        return new PacketFactionManage(Action.POLITICS_ACTION, action, targetFaction, 0L);
    }
    public static PacketFactionManage barracksAction(String action, String playerName, String structureId) {
        return new PacketFactionManage(Action.BARRACKS_ACTION, action + "#" + structureId, playerName, 0L);
    }
    public static PacketFactionManage managePlayer(String targetName, String action, String data) {
        return new PacketFactionManage(Action.PLAYER_ACTION, targetName, action, 0L, data);
    }
    public static PacketFactionManage manageTitleHierarchy(List<String> sortedTitles) {
        return new PacketFactionManage(Action.TITLE_HIERARCHY_EDIT, String.join(",", sortedTitles), "", 0);
    }
    public static PacketFactionManage proposePolitics(Relation type, String targetFaction, long cost) {
        return new PacketFactionManage(Action.POLITICS_PROPOSE, type.codeName(), targetFaction, cost);
    }
    public static PacketFactionManage respondToProposal(String proposingFaction, boolean accept) {
        return new PacketFactionManage(accept ? Action.POLITICS_ACCEPT_PROPOSAL : Action.POLITICS_DECLINE_PROPOSAL, proposingFaction, "", 0L);
    }
    public static PacketFactionManage depositProvisions(int amount, String structureId) {
        return new PacketFactionManage(Action.PROVISIONS_DEPOSIT, structureId, "", amount);
    }
    public static PacketFactionManage closeCollectionGoal(String goalName) {
        return new PacketFactionManage(Action.COLLECTION_GOAL_CLOSE, goalName, "", 0L);
    }
    public static PacketFactionManage increaseBarracksCapacity(String structureId, int amount) {
        return new PacketFactionManage(Action.BARRACKS_INCREASE_CAPACITY, structureId, "", amount);
    }

    private PacketFactionManage(Action action, String s_data1, String s_data2, long l_data1, String s_data3) {
        this(action, s_data1, s_data2, l_data1);
        if (action == Action.PLAYER_ACTION) {
            this.s_data2 = this.s_data2 + "#" + s_data3;
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        action = Action.values()[buf.readInt()];
        s_data1 = ByteBufUtils.readUTF8String(buf);
        s_data2 = ByteBufUtils.readUTF8String(buf);
        l_data1 = buf.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(action.ordinal());
        ByteBufUtils.writeUTF8String(buf, s_data1);
        ByteBufUtils.writeUTF8String(buf, s_data2);
        buf.writeLong(l_data1);
    }

    public enum Action {
        TREASURY_ACTION, TITLE_CREATE, PLAYER_ACTION, POLITICS_ACTION, BARRACKS_ACTION,
        TITLE_HIERARCHY_EDIT, POLITICS_PROPOSE, POLITICS_ACCEPT_PROPOSAL, POLITICS_DECLINE_PROPOSAL,
        PROVISIONS_DEPOSIT, COLLECTION_GOAL_CLOSE, BARRACKS_INCREASE_CAPACITY
    }

    public static class Handler implements IMessageHandler<PacketFactionManage, IMessage> {
        @Override
        public IMessage onMessage(PacketFactionManage message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            Faction faction = PacketMessage.getCurrentFaction(player.getCommandSenderName());

            if (faction == null) return null;

            switch (message.action) {
                case TREASURY_ACTION:
                    handleTreasury(player, faction, message);
                    break;
                case TITLE_CREATE:
                    handleTitleCreate(player, faction, message);
                    break;
                case PLAYER_ACTION:
                    handlePlayerAction(player, faction, message);
                    break;
                case POLITICS_ACTION:
                    handlePoliticsAction(player, faction, message);
                    break;
                case BARRACKS_ACTION:
                    handleBarracksAction(player, faction, message);
                    break;
                case TITLE_HIERARCHY_EDIT:
                    handleTitleHierarchyEdit(player, faction, message);
                    break;
                case POLITICS_PROPOSE:
                    handlePoliticsPropose(player, faction, message);
                    break;
                case POLITICS_ACCEPT_PROPOSAL:
                    handlePoliticsAccept(player, faction, message);
                    break;
                case POLITICS_DECLINE_PROPOSAL:
                    handlePoliticsDecline(player, faction, message);
                    break;
                case PROVISIONS_DEPOSIT:
                    handleProvisionsDeposit(player, faction, message);
                    break;
                case COLLECTION_GOAL_CLOSE:
                    handleCloseCollectionGoal(player, faction, message);
                    break;
                case BARRACKS_INCREASE_CAPACITY:
                    handleIncreaseBarracksCapacity(player, faction, message);
                    break;

            }
            return null;
        }

        private void handleIncreaseBarracksCapacity(EntityPlayerMP player, Faction faction, PacketFactionManage message) {
            String structureId = message.s_data1;
            int amount = (int) message.l_data1;

            FactionStructureSlot slot = FactionStructureManager.getStructureById(structureId);
            if (slot == null || !faction.getID().equals(slot.ownerFactionID) || slot.category != FactionStructureSlot.StructureCategory.FORTRESS) {
                return;
            }

            if (!faction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_MANAGE_BARRACKS)) {
                player.addChatMessage(new ChatComponentText("§cУ вас нет прав на управление казармой."));
                return;
            }

            if (amount <= 0) return;

            long cost = (long) amount * StructureManager.config.provisionCostPerBarracksSlot;

            if (slot.provisions >= cost) {
                slot.provisions -= cost;
                slot.barracksCapacity += amount;
                player.addChatMessage(new ChatComponentText("§aВместимость казармы увеличена на " + amount + "!"));
                FactionStructureManager.saveStructureOwnership();
                CoreFaction.sendAllGui();
                CoreFaction.brainChannel.sendTo(new PacketStructureGUISync(slot.xCoord, slot.yCoord, slot.zCoord, slot), player);
            } else {
                player.addChatMessage(new ChatComponentText("§cНедостаточно продовольствия в крепости. Требуется: " + cost));
            }
        }

        private void handleProvisionsDeposit(EntityPlayerMP player, Faction faction, PacketFactionManage message) {
            String structureId = message.s_data1;
            FactionStructureSlot slot = FactionStructureManager.getStructureById(structureId);
            if (slot == null || !faction.getID().equals(slot.ownerFactionID)) return;

            int amountToDeposit = (int) message.l_data1;
            if (amountToDeposit <= 0) return;

            boolean isBarn = slot.category == FactionStructureSlot.StructureCategory.BARN;
            boolean isFortressBarn = slot.category == FactionStructureSlot.StructureCategory.FORTRESS;

            int itemsInInventory = 0;
            for (ItemStack stack : player.inventory.mainInventory) {
                if (stack != null && brain.factions.servers.ItemToProvisionMap.getProvisionValue(stack) > 0) {
                    itemsInInventory += stack.stackSize;
                }
            }

            if (itemsInInventory < amountToDeposit) {
                player.addChatMessage(new ChatComponentText("§cУ вас недостаточно еды для внесения " + amountToDeposit + " предметов."));
                return;
            }

            if (isBarn || isFortressBarn) {
                int capacity = isFortressBarn ? StructureManager.getGranaryCapacity(slot.barnLevel) : StructureManager.getGranaryCapacity(slot.level);
                if (slot.storedFoodItems + amountToDeposit > capacity) {
                    player.addChatMessage(new ChatComponentText("§cАмбар не может вместить столько еды! Свободно: " + (capacity - slot.storedFoodItems) + " шт."));
                    return;
                }
            }

            int itemsLeftToDeposit = amountToDeposit;
            float addedProvisions = 0;

            for (int i = 0; i < player.inventory.mainInventory.length; i++) {
                ItemStack stack = player.inventory.mainInventory[i];
                if (stack != null && brain.factions.servers.ItemToProvisionMap.getProvisionValue(stack) > 0) {
                    int itemsToRemove = Math.min(stack.stackSize, itemsLeftToDeposit);

                    float provisionPerItem = brain.factions.servers.ItemToProvisionMap.getProvisionValue(stack);
                    addedProvisions += provisionPerItem * itemsToRemove;

                    itemsLeftToDeposit -= itemsToRemove;
                    stack.stackSize -= itemsToRemove;

                    if (stack.stackSize <= 0) {
                        player.inventory.mainInventory[i] = null;
                    }
                    if (itemsLeftToDeposit <= 0) break;
                }
            }
            player.inventory.markDirty();

            slot.provisions += addedProvisions;

            if (isBarn || isFortressBarn) {
                slot.storedFoodItems += amountToDeposit;
                player.addChatMessage(new ChatComponentText("§aВы внесли " + amountToDeposit + " предметов еды в амбар."));
            } else {
                player.addChatMessage(new ChatComponentText("§aВы внесли продовольствие."));
            }

            FactionStructureManager.saveStructureOwnership();
            CoreFaction.sendAllGui();
            CoreFaction.brainChannel.sendTo(new PacketStructureGUISync(slot.xCoord, slot.yCoord, slot.zCoord, slot), player);
        }

        private void handleBarracksAction(EntityPlayerMP player, Faction faction, PacketFactionManage message) {
            String[] actionParts = message.s_data1.split("#");
            String action = actionParts[0];
            String structureId = actionParts.length > 1 ? actionParts[1] : "";
            String playerName = message.s_data2;

            FactionStructureSlot slot = FactionStructureManager.getStructureById(structureId);
            if (slot == null || !faction.getID().equals(slot.ownerFactionID) || slot.category != FactionStructureSlot.StructureCategory.FORTRESS) {
                player.addChatMessage(new ChatComponentText("§cНеверная структура для управления казармой."));
                return;
            }

            if (!faction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_MANAGE_BARRACKS)) {
                player.addChatMessage(new ChatComponentText("§cУ вас нет прав на управление казармой."));
                return;
            }

            switch (action) {
                case "add_player":
                    if (BarracksManager.getBarracksPlayers(structureId).size() >= slot.barracksCapacity) {
                        player.addChatMessage(new ChatComponentText("§cВместимость казармы полная (Лимит: " + slot.barracksCapacity + ")!"));
                        return;
                    }

                    int currentPlayers = BarracksManager.getBarracksPlayers(structureId).size();
                    long cost = (long) (100 * Math.pow(1.5, currentPlayers));

                    if (slot.provisions < cost) {
                        player.addChatMessage(new ChatComponentText("§cНедостаточно продовольствия! Требуется: " + cost));
                        return;
                    }

                    slot.provisions -= cost;
                    BarracksManager.addPlayer(structureId, playerName);
                    player.addChatMessage(new ChatComponentText("§aИгрок " + playerName + " добавлен в казарму за " + cost + " ед. продовольствия."));

                    FactionStructureManager.saveStructureOwnership();
                    break;
                case "remove_player":
                    BarracksManager.removePlayer(structureId, playerName);
                    player.addChatMessage(new ChatComponentText("§aИгрок " + playerName + " удален из казармы."));
                    break;
            }
            CoreFaction.sendAllGui();
            CoreFaction.brainChannel.sendTo(new PacketStructureGUISync(slot.xCoord, slot.yCoord, slot.zCoord, slot), player);
        }

        private void handleCloseCollectionGoal(EntityPlayerMP player, Faction faction, PacketFactionManage message) {
            if (!faction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_MANAGE_TREASURY)) {
                player.addChatMessage(new ChatComponentText("§cУ вас нет прав на управление сборами."));
                return;
            }

            String goalName = message.s_data1;
            Optional<brain.factions.servers.CollectionGoal> goalOpt = faction.getCollectionGoals().stream()
                    .filter(g -> g.getName().equals(goalName))
                    .findFirst();

            if (goalOpt.isPresent()) {
                brain.factions.servers.CollectionGoal goal = goalOpt.get();
                if (goal.isComplete()) {
                    faction.setTreasury(faction.getTreasury() + goal.getCurrentAmount());
                    faction.logTreasuryTransaction("Завершение сбора: " + goal.getName(), goal.getCurrentAmount());
                    faction.getCollectionGoals().remove(goal);
                    player.addChatMessage(new ChatComponentText("§aСбор '" + goalName + "' завершен, средства переведены в казну."));
                    CoreFaction.saveFactions();
                    CoreFaction.sendAllGui();
                } else {
                    player.addChatMessage(new ChatComponentText("§cЭтот сбор еще не завершен."));
                }
            }
        }


        private void handlePoliticsPropose(EntityPlayerMP player, Faction faction, PacketFactionManage message) {
            if (!faction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_USE_TREASURY)) {
                player.addChatMessage(new ChatComponentText("§cУ вас нет прав на использование казны."));
                return;
            }

            String targetFactionId = message.s_data2;
            Relation type = Relation.forName(message.s_data1);
            long cost = message.l_data1;

            Faction targetFaction = PacketMessage.getFaction(targetFactionId);
            if (targetFaction == null) {
                player.addChatMessage(new ChatComponentText("§cУказанная фракция не найдена."));
                return;
            }

            if (faction.getTreasury() < cost) {
                player.addChatMessage(new ChatComponentText("§cВ казне недостаточно средств для предложения."));
                return;
            }

            Faction.Proposal newProposal = new Faction.Proposal(faction.getID(), type, cost);
            targetFaction.getProposals().put(faction.getID(), newProposal);

            player.addChatMessage(new ChatComponentText("§aПредложение о " + type.getDisplayName() + " фракции " + targetFaction.getID() + " было отправлено."));
            EntityPlayerMP targetLeader = PacketMessage.getPlayer(targetFaction.getLeaderName());
            if (targetLeader != null) {
                targetLeader.addChatMessage(new ChatComponentText("§aФракция " + faction.getID() + " отправила вам предложение о " + type.getDisplayName() + " на сумму " + cost + " монет."));
            }
            CoreFaction.saveFactions();
            CoreFaction.sendAllGui();
        }

        private void handlePoliticsAccept(EntityPlayerMP player, Faction faction, PacketFactionManage message) {
            if (!faction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_MANAGE_TREASURY)) {
                player.addChatMessage(new ChatComponentText("§cУ вас нет прав на принятие дипломатических решений."));
                return;
            }
            String proposingFactionId = message.s_data1;
            Faction proposingFaction = PacketMessage.getFaction(proposingFactionId);

            if (proposingFaction != null) {
                Faction.Proposal proposal = faction.getProposals().get(proposingFactionId);
                if (proposal != null) {
                    if (proposingFaction.getTreasury() >= proposal.getCost()) {
                        proposingFaction.setTreasury(proposingFaction.getTreasury() - proposal.getCost());
                        faction.setTreasury(faction.getTreasury() + proposal.getCost());
                        GOTFactionRelations.overrideRelations(GOTFaction.forName(faction.getID()), GOTFaction.forName(proposingFaction.getID()), proposal.getRelationType());
                        faction.getProposals().remove(proposingFactionId);

                        player.addChatMessage(new ChatComponentText("§aВы приняли предложение фракции " + proposingFactionId + " о " + proposal.getRelationType().getDisplayName() + "."));
                        EntityPlayerMP proposingLeader = PacketMessage.getPlayer(proposingFaction.getLeaderName());
                        if (proposingLeader != null) {
                            proposingLeader.addChatMessage(new ChatComponentText("§aФракция " + faction.getID() + " приняла ваше предложение о " + proposal.getRelationType().getDisplayName() + "."));
                        }
                    } else {
                        player.addChatMessage(new ChatComponentText("§cУ фракции " + proposingFactionId + " недостаточно средств для оплаты предложения. Предложение отклонено."));
                        faction.getProposals().remove(proposingFactionId);
                    }
                }
            }
            CoreFaction.saveFactions();
            CoreFaction.sendAllGui();
        }

        private void handlePoliticsDecline(EntityPlayerMP player, Faction faction, PacketFactionManage message) {
            String proposingFactionId = message.s_data1;
            Faction proposingFaction = PacketMessage.getFaction(proposingFactionId);

            if (proposingFaction != null) {
                Faction.Proposal proposal = faction.getProposals().remove(proposingFactionId);
                if (proposal != null) {
                    player.addChatMessage(new ChatComponentText("§cВы отклонили предложение фракции " + proposingFactionId + " о " + proposal.getRelationType().getDisplayName() + "."));
                    EntityPlayerMP proposingLeader = PacketMessage.getPlayer(proposingFaction.getLeaderName());
                    if (proposingLeader != null) {
                        proposingLeader.addChatMessage(new ChatComponentText("§cФракция " + faction.getID() + " отклонила ваше предложение о " + proposal.getRelationType().getDisplayName() + "."));
                    }
                }
            }
            CoreFaction.saveFactions();
            CoreFaction.sendAllGui();
        }

        private void handleTitleHierarchyEdit(EntityPlayerMP player, Faction faction, PacketFactionManage message) {
            got.rome.ExtendedPlayer ext = got.rome.ExtendedPlayer.get(player);
            boolean isTutorial = ext != null && ext.isTutorialActive();
            if (!faction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_MANAGE_HIERARCHY) && !isTutorial) {
                player.addChatMessage(new ChatComponentText("§cУ вас нет прав на изменение иерархии титулов."));
                return;
            }
            String[] sortedTitles = message.s_data1.split(",");
            for (int i = 0; i < sortedTitles.length; i++) {
                String titleName = sortedTitles[i];
                Faction.Title title = faction.getTitles().get(titleName);
                if (title != null) {
                    title.setHierarchy(i);
                }
            }
            player.addChatMessage(new ChatComponentText("§aИерархия титулов успешно сохранена."));
            CoreFaction.saveFactions();
            CoreFaction.sendAllGui();
        }

        private int getInventoryValue(EntityPlayerMP player) {
            int value = 0;
            IInventory inv = player.inventory;
            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                ItemStack itemstack = inv.getStackInSlot(i);
                value += getStackValue(itemstack);
            }
            return value;
        }

        private int getStackValue(ItemStack itemstack) {
            if (itemstack != null && itemstack.getItem() instanceof GOTItemCoin) {
                return GOTItemCoin.values[itemstack.getItemDamage()] * itemstack.stackSize;
            }
            return 0;
        }

        private void handleTreasury(EntityPlayerMP player, Faction faction, PacketFactionManage message) {
            long amount = message.l_data1;
            String goalName = message.s_data2;
            String playerName = player.getCommandSenderName();

            if (message.s_data1.equals("withdraw")) {
                if (!faction.playerHasPermission(playerName, Faction.Permission.CAN_USE_TREASURY)) {
                    player.addChatMessage(new ChatComponentText("§cУ вас нет прав на снятие средств."));
                    return;
                }
                if (goalName.isEmpty()) {
                    if (faction.getTreasury() >= amount) {
                        faction.setTreasury(faction.getTreasury() - amount);
                        faction.logTreasuryTransaction(playerName, -amount);
                        GOTItemCoin.giveCoins((int) amount, player);
                        player.addChatMessage(new ChatComponentText("§aВы сняли " + amount + " из казны."));
                    } else {
                        player.addChatMessage(new ChatComponentText("§cВ казне недостаточно средств."));
                    }
                } else {
                    faction.getCollectionGoals().stream()
                            .filter(g -> g.getName().equals(goalName))
                            .findFirst()
                            .ifPresent(g -> {
                                if (g.getCurrentAmount() >= amount) {
                                    g.addAmount(-amount);
                                    g.logTransaction(playerName, -amount);
                                    GOTItemCoin.giveCoins((int) amount, player);
                                    player.addChatMessage(new ChatComponentText("§aВы сняли " + amount + " из сбора '" + g.getName() + "'."));
                                } else {
                                    player.addChatMessage(new ChatComponentText("§cВ этом сборе недостаточно средств."));
                                }
                            });
                }
            } else if (message.s_data1.equals("deposit")) {
                int playerCoins = getInventoryValue(player);
                if (playerCoins >= amount) {

                    Optional<brain.factions.servers.CollectionGoal> goalOpt = faction.getCollectionGoals().stream()
                            .filter(g -> g.getName().equals(goalName)).findFirst();
                    if (goalOpt.isPresent() && goalOpt.get().isComplete()) {
                        player.addChatMessage(new ChatComponentText("§cЭтот сбор уже завершен."));
                        return;
                    }

                    GOTItemCoin.takeCoins((int) amount, player);
                    if (goalName.isEmpty()) {
                        faction.setTreasury(faction.getTreasury() + amount);
                        faction.logTreasuryTransaction(playerName, amount);
                        player.addChatMessage(new ChatComponentText("§aВы пожертвовали " + amount + " в казну."));
                    } else {
                        goalOpt.ifPresent(g -> {
                            g.addAmount(amount);
                            g.logTransaction(playerName, amount);
                            player.addChatMessage(new ChatComponentText("§aВы пожертвовали " + amount + " в сбор '" + g.getName() + "'."));
                        });
                    }
                } else {
                    player.addChatMessage(new ChatComponentText("§cУ вас недостаточно монет в инвентаре."));
                }
            }
            CoreFaction.saveFactions();
            CoreFaction.sendAllGui();
        }

        private void handlePoliticsAction(final EntityPlayerMP player, final Faction faction, final PacketFactionManage message) {
            if (!faction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_USE_TREASURY)) {
                player.addChatMessage(new ChatComponentText("§cУ вас нет прав на использование казны."));
                return;
            }

            ServerTaskExecutor.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    String action = message.s_data1;
                    String targetFactionName = message.s_data2;
                    long cost = 0;

                    switch (action) {
                        case "declare_war":
                            cost = 10000;
                            if (faction.getTreasury() >= cost) {
                                faction.setTreasury(faction.getTreasury() - cost);

                                // Выполнение команды от имени консоли сервера
                                String command = "facRelations set " + faction.getID() + " " + targetFactionName + " MORTAL_ENEMY";
                                MinecraftServer.getServer().getCommandManager().executeCommand(MinecraftServer.getServer(), command);

                                player.addChatMessage(new ChatComponentText("§aВы объявили войну фракции " + targetFactionName));

                                faction.setInWarState(true);
                                faction.setUnpaidWarTaxDays(0);
                                faction.setWarTaxOwed(0);

                                // Обязательное сохранение данных!
                                CoreFaction.saveFactions();
                                CoreFaction.sendAllGui();
                            } else {
                                player.addChatMessage(new ChatComponentText("§cНедостаточно средств в казне."));
                            }
                            break;

                        case "declare_hostility":
                            cost = 5000;
                            if (faction.getTreasury() >= cost) {
                                faction.setTreasury(faction.getTreasury() - cost);

                                // Выполнение команды от имени консоли сервера
                                String command = "facRelations set " + faction.getID() + " " + targetFactionName + " ENEMY";
                                MinecraftServer.getServer().getCommandManager().executeCommand(MinecraftServer.getServer(), command);

                                player.addChatMessage(new ChatComponentText("§aВы объявили вражду фракции " + targetFactionName));

                                // Обязательное сохранение данных!
                                CoreFaction.saveFactions();
                                CoreFaction.sendAllGui();
                            } else {
                                player.addChatMessage(new ChatComponentText("§cНедостаточно средств в казне."));
                            }
                            break;
                    }
                }
            });
        }

        private void handleTitleCreate(EntityPlayerMP player, Faction faction, PacketFactionManage message) {
            got.rome.ExtendedPlayer ext = got.rome.ExtendedPlayer.get(player);
            boolean isTutorial = ext != null && ext.isTutorialActive();
            if (!faction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_CREATE_TITLES) && !isTutorial) {
                player.addChatMessage(new ChatComponentText("§cУ вас нет прав на создание титулов."));
                return;
            }
            String titleName = message.s_data1;
            if (faction.getTitles().containsKey(titleName)) {
                player.addChatMessage(new ChatComponentText("§cТитул с таким именем уже существует."));
                return;
            }
            int hierarchy = faction.getTitles().size() + 1;
            faction.getTitles().put(titleName, new Faction.Title(titleName, hierarchy, message.l_data1));
            player.addChatMessage(new ChatComponentText("§aТитул '" + titleName + "' создан."));
            CoreFaction.saveFactions();
            CoreFaction.sendAllGui();
        }

        private void handlePlayerAction(EntityPlayerMP player, Faction faction, PacketFactionManage message) {
            String targetName = message.s_data1;
            String[] actionData = message.s_data2.split("#");
            String action = actionData[0];

            if (action.equals("kick")) {
                if (targetName.equals(player.getCommandSenderName())) {
                    player.addChatMessage(new ChatComponentText("§cВы не можете выгнать самого себя!"));
                    return;
                }
                if (targetName.equals(faction.getLeaderName())) {
                    player.addChatMessage(new ChatComponentText("§cЛидера нельзя выгнать из фракции!"));
                    return;
                }
                
                got.rome.ExtendedPlayer ext = got.rome.ExtendedPlayer.get(player);
                if (ext != null && ext.isTutorialActive()) {
                    player.addChatMessage(new ChatComponentText("§cВы не можете выгонять игроков во время обучения."));
                    return;
                }

                if (!faction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_KICK_MEMBERS)) {
                    player.addChatMessage(new ChatComponentText("§cУ вас нет прав выгонять игроков."));
                    return;
                }

                Faction.PlayerData pData = faction.getPlayers().get(targetName);
                if (pData != null) {
                    Faction.Title playerTitle = faction.getTitles().get(faction.getPlayers().get(player.getCommandSenderName()).getTitle());
                    Faction.Title targetTitle = faction.getTitles().get(pData.getTitle());

                    if (playerTitle != null && targetTitle != null && playerTitle.getHierarchy() >= targetTitle.getHierarchy()) {
                        player.addChatMessage(new ChatComponentText("§cВы не можете выгнать игрока с равным или более высоким титулом."));
                        return;
                    }
                }

                faction.getPlayers().remove(targetName);
                CoreFaction.updatePrefix(targetName);

                if (faction.getAssistantName().equals(targetName)) {
                    faction.setAssistantName("");
                }

                EntityPlayerMP targetPlayer = PacketMessage.getPlayer(targetName);
                if (targetPlayer == null)
                    targetPlayer = MinecraftServer.getServer().getConfigurationManager().func_152612_a(targetName);

                if (targetPlayer != null) {
                    got.common.GOTPlayerData pd = got.common.GOTLevelData.getData(targetPlayer);
                    if (pd != null) pd.revokePledgeFaction(targetPlayer, true);
                } else {
                    java.util.UUID targetUUID = null;
                    for (java.util.Map.Entry<java.util.UUID, String> entry : net.minecraftforge.common.UsernameCache.getMap().entrySet()) {
                        if (targetName.contains(entry.getValue())) {
                            targetUUID = entry.getKey();
                            break;
                        }
                    }
                    if (targetUUID != null) {
                        got.common.GOTPlayerData pd = got.common.GOTLevelData.getData(targetUUID);
                        if (pd != null) pd.revokePledgeFaction(null, false);
                    }
                }

                player.addChatMessage(new ChatComponentText("§aИгрок " + targetName + " был изгнан из фракции."));
            } else if (action.equals("setTitle")) {
                got.rome.ExtendedPlayer ext = got.rome.ExtendedPlayer.get(player);
                boolean isTutorial = ext != null && ext.isTutorialActive();
                if (!faction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_CREATE_TITLES) && !isTutorial) {
                    player.addChatMessage(new ChatComponentText("§cУ вас нет прав назначать титулы."));
                    return;
                }
                if (isTutorial && !targetName.equals(player.getCommandSenderName())) {
                    player.addChatMessage(new ChatComponentText("§cВы не можете изменять титулы других игроков во время обучения."));
                    return;
                }
                
                String titleName = actionData[1];
                Faction.PlayerData pData = faction.getPlayers().get(targetName);
                if (pData != null && faction.getTitles().containsKey(titleName)) {
                    pData.setTitle(titleName);
                    player.addChatMessage(new ChatComponentText("§aИгроку " + targetName + " назначен титул '" + titleName + "'."));
                    CoreFaction.updatePrefix(targetName);
                } else {
                    player.addChatMessage(new ChatComponentText("§cНе удалось найти игрока или титул."));
                }
            }
            CoreFaction.saveFactions();
            CoreFaction.sendAllGui();
        }
    }
}