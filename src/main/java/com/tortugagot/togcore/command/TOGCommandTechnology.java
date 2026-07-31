package com.tortugagot.togcore.command;

import com.tortugagot.togcore.Config;
import com.tortugagot.togcore.technology.TOGTechnology;
import com.tortugagot.togcore.technology.TOGTechnologyPlayerData;
import com.tortugagot.togcore.technology.TOGTechnologyRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TOGCommandTechnology extends CommandBase {
    private static final String[] ROOT_COMMANDS = {"points", "tech", "reset", "openall", "list", "reload"};
    private static final String[] POINT_COMMANDS = {"get", "add", "remove", "set"};
    private static final String[] TECH_COMMANDS = {"open", "close", "list"};

    @Override
    public String getCommandName() {
        return "togtech";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/togtech points <get|add|remove|set> [player] [amount], /togtech tech <open|close|list> [player] [technology], /togtech reset [player], /togtech openall [player], /togtech list, /togtech reload";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        if (args.length == 0) {
            return false;
        }
        if ("points".equalsIgnoreCase(args[0])) {
            return index == 2 && args.length >= 3 && ("get".equalsIgnoreCase(args[1]) || args.length >= 4);
        }
        if ("tech".equalsIgnoreCase(args[0])) {
            return index == 2 && args.length >= 3 && ("list".equalsIgnoreCase(args[1]) || args.length >= 4);
        }
        return index == 1 && ("reset".equalsIgnoreCase(args[0]) || "openall".equalsIgnoreCase(args[0]));
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, ROOT_COMMANDS);
        }
        if (args.length == 2 && "points".equalsIgnoreCase(args[0])) {
            return CommandBase.getListOfStringsMatchingLastWord(args, POINT_COMMANDS);
        }
        if (args.length == 2 && "tech".equalsIgnoreCase(args[0])) {
            return CommandBase.getListOfStringsMatchingLastWord(args, TECH_COMMANDS);
        }
        if (args.length == 3 && "tech".equalsIgnoreCase(args[0]) && ("open".equalsIgnoreCase(args[1]) || "close".equalsIgnoreCase(args[1]))) {
            return CommandBase.getListOfStringsMatchingLastWord(args, combine(MinecraftServer.getServer().getAllUsernames(), getTechnologyIds()));
        }
        if (args.length == 4 && "tech".equalsIgnoreCase(args[0]) && ("open".equalsIgnoreCase(args[1]) || "close".equalsIgnoreCase(args[1]))) {
            return CommandBase.getListOfStringsMatchingLastWord(args, getTechnologyIds());
        }
        if ((args.length == 2 && ("reset".equalsIgnoreCase(args[0]) || "openall".equalsIgnoreCase(args[0]))) || (args.length == 3 && "points".equalsIgnoreCase(args[0])) || (args.length == 3 && "tech".equalsIgnoreCase(args[0]) && "list".equalsIgnoreCase(args[1]))) {
            return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        if (args.length == 4 && "points".equalsIgnoreCase(args[0]) && ("add".equalsIgnoreCase(args[1]) || "remove".equalsIgnoreCase(args[1]) || "set".equalsIgnoreCase(args[1]))) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "1", "10", "100");
        }
        return Collections.emptyList();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            throw new WrongUsageException(getCommandUsage(sender));
        }
        if ("list".equalsIgnoreCase(args[0])) {
            requireLength(args, 1);
            sendAllTechnologyList(sender);
            return;
        }
        if ("reload".equalsIgnoreCase(args[0])) {
            requireLength(args, 1);
            if (!Config.reload()) {
                throw new CommandException("Конфигурация TogCore еще не была загружена.");
            }
            sender.addChatMessage(new ChatComponentText("Конфигурация TogCore перезагружена."));
            return;
        }
        if ("reset".equalsIgnoreCase(args[0])) {
            requireLengthRange(args, 1, 2);
            EntityPlayerMP player = getOptionalPlayer(sender, args, 1);
            TOGTechnologyPlayerData data = getData(player);
            int refund = data.resetTechnologies();
            data.sync();
            sender.addChatMessage(new ChatComponentText("Древо технологий сброшено для " + player.getCommandSenderName() + ". Возвращено очков: " + refund + ". Баланс: " + data.getMasteryPoints() + "."));
            return;
        }
        if ("openall".equalsIgnoreCase(args[0])) {
            requireLengthRange(args, 1, 2);
            EntityPlayerMP player = getOptionalPlayer(sender, args, 1);
            TOGTechnologyPlayerData data = getData(player);
            int opened = data.openAllTechnologies();
            data.sync();
            sender.addChatMessage(new ChatComponentText("Полностью открыто древо для " + player.getCommandSenderName() + ". Новых технологий: " + opened + ". Баланс не изменен: " + data.getMasteryPoints() + "."));
            return;
        }
        if ("points".equalsIgnoreCase(args[0])) {
            processPointsCommand(sender, args);
            return;
        }
        if ("tech".equalsIgnoreCase(args[0])) {
            processTechCommand(sender, args);
            return;
        }
        throw new WrongUsageException(getCommandUsage(sender));
    }

    private void processPointsCommand(ICommandSender sender, String[] args) {
        if (args.length < 2) {
            throw new WrongUsageException(getCommandUsage(sender));
        }
        String action = args[1];
        if ("get".equalsIgnoreCase(action)) {
            requireLengthRange(args, 2, 3);
            EntityPlayerMP player = getOptionalPlayer(sender, args, 2);
            TOGTechnologyPlayerData data = getData(player);
            sender.addChatMessage(new ChatComponentText(player.getCommandSenderName() + ": " + data.getMasteryPoints() + " очков мастерства. Потрачено в древе: " + data.getSpentTechnologyPoints() + "."));
            return;
        }
        if ("add".equalsIgnoreCase(action) || "remove".equalsIgnoreCase(action) || "set".equalsIgnoreCase(action)) {
            requireLengthRange(args, 3, 4);
            EntityPlayerMP player = args.length == 4 ? CommandBase.getPlayer(sender, args[2]) : CommandBase.getCommandSenderAsPlayer(sender);
            int amount = CommandBase.parseIntWithMin(sender, args.length == 4 ? args[3] : args[2], 0);
            TOGTechnologyPlayerData data = getData(player);
            if ("add".equalsIgnoreCase(action)) {
                data.addMasteryPoints(amount);
                data.sync();
                sender.addChatMessage(new ChatComponentText("Добавлено " + amount + " очков мастерства игроку " + player.getCommandSenderName() + ". Баланс: " + data.getMasteryPoints() + "."));
                return;
            }
            if ("remove".equalsIgnoreCase(action)) {
                int removed = data.removeMasteryPoints(amount);
                data.sync();
                sender.addChatMessage(new ChatComponentText("Удалено " + removed + " очков мастерства у игрока " + player.getCommandSenderName() + ". Баланс: " + data.getMasteryPoints() + "."));
                return;
            }
            data.setMasteryPoints(amount);
            data.sync();
            sender.addChatMessage(new ChatComponentText("Баланс очков мастерства игрока " + player.getCommandSenderName() + " установлен: " + data.getMasteryPoints() + "."));
            return;
        }
        throw new WrongUsageException(getCommandUsage(sender));
    }

    private void processTechCommand(ICommandSender sender, String[] args) {
        if (args.length < 2) {
            throw new WrongUsageException(getCommandUsage(sender));
        }
        String action = args[1];
        if ("list".equalsIgnoreCase(action)) {
            requireLengthRange(args, 2, 3);
            EntityPlayerMP player = getOptionalPlayer(sender, args, 2);
            sendPlayerTechnologyList(sender, player);
            return;
        }
        if ("open".equalsIgnoreCase(action) || "close".equalsIgnoreCase(action)) {
            requireLengthRange(args, 3, 4);
            EntityPlayerMP player = args.length == 4 ? CommandBase.getPlayer(sender, args[2]) : CommandBase.getCommandSenderAsPlayer(sender);
            String technologyId = args.length == 4 ? args[3] : args[2];
            TOGTechnology technology = getTechnology(sender, technologyId);
            TOGTechnologyPlayerData data = getData(player);
            if ("open".equalsIgnoreCase(action)) {
                if (!data.openTechnology(technology.getId())) {
                    sender.addChatMessage(new ChatComponentText("Технология уже открыта у игрока " + player.getCommandSenderName() + ": " + technology.getName() + "."));
                    return;
                }
                data.sync();
                sender.addChatMessage(new ChatComponentText("Открыта технология для " + player.getCommandSenderName() + ": " + technology.getName() + ". Баланс не изменен: " + data.getMasteryPoints() + "."));
                return;
            }
            int closed = data.closeTechnologyTree(technology.getId());
            if (closed == 0) {
                sender.addChatMessage(new ChatComponentText("Технология уже закрыта у игрока " + player.getCommandSenderName() + ": " + technology.getName() + "."));
                return;
            }
            data.sync();
            sender.addChatMessage(new ChatComponentText("Закрыта технология для " + player.getCommandSenderName() + ": " + technology.getName() + ". Всего закрыто с зависимыми технологиями: " + closed + ". Баланс не изменен: " + data.getMasteryPoints() + "."));
            return;
        }
        throw new WrongUsageException(getCommandUsage(sender));
    }

    private void sendAllTechnologyList(ICommandSender sender) {
        ArrayList<String> ids = new ArrayList<String>();
        for (TOGTechnology technology : TOGTechnologyRegistry.getAll()) {
            ids.add(technology.getId() + "(" + technology.getBaseCost() + "):" + technology.getName());
        }
        sender.addChatMessage(new ChatComponentText(String.join(", ", ids)));
    }

    private void sendPlayerTechnologyList(ICommandSender sender, EntityPlayerMP player) {
        TOGTechnologyPlayerData data = getData(player);
        Set<String> unlocked = data.getUnlockedTechnologies();
        if (unlocked.isEmpty()) {
            sender.addChatMessage(new ChatComponentText("У игрока " + player.getCommandSenderName() + " нет открытых технологий."));
            return;
        }
        ArrayList<String> names = new ArrayList<String>();
        for (String id : unlocked) {
            TOGTechnology technology = TOGTechnologyRegistry.get(id);
            names.add(technology != null ? technology.getName() + "(" + id + ")" : id);
        }
        sender.addChatMessage(new ChatComponentText("Открытые технологии игрока " + player.getCommandSenderName() + ": " + String.join(", ", names)));
    }

    private TOGTechnology getTechnology(ICommandSender sender, String id) {
        TOGTechnology technology = TOGTechnologyRegistry.get(id);
        if (technology == null) {
            throw new CommandException("Технология не найдена: " + id);
        }
        return technology;
    }

    private EntityPlayerMP getOptionalPlayer(ICommandSender sender, String[] args, int index) {
        return args.length > index ? CommandBase.getPlayer(sender, args[index]) : CommandBase.getCommandSenderAsPlayer(sender);
    }

    private TOGTechnologyPlayerData getData(EntityPlayerMP player) {
        TOGTechnologyPlayerData data = TOGTechnologyPlayerData.get(player);
        if (data == null) {
            TOGTechnologyPlayerData.register(player);
            data = TOGTechnologyPlayerData.get(player);
        }
        return data;
    }

    private void requireLength(String[] args, int length) {
        if (args.length != length) {
            throw new WrongUsageException(getCommandUsage(null));
        }
    }

    private void requireLengthRange(String[] args, int min, int max) {
        if (args.length < min || args.length > max) {
            throw new WrongUsageException(getCommandUsage(null));
        }
    }

    private String[] getTechnologyIds() {
        ArrayList<String> ids = new ArrayList<String>();
        for (TOGTechnology technology : TOGTechnologyRegistry.getAll()) {
            ids.add(technology.getId());
        }
        return ids.toArray(new String[ids.size()]);
    }

    private String[] combine(String[] first, String[] second) {
        String[] result = new String[first.length + second.length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
