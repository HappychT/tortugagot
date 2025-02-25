package got.common.command;

import got.common.GOTTime;
import got.common.world.GOTWorldInfo;
import got.common.world.GOTWorldProvider;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GOTCommandWeather extends CommandBase {
    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "clear", "rain");
        }
        return Collections.emptyList();
    }

    @Override
    public String getCommandName() {
        return "got_weather";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "got.command.weather.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length >= 1 && args.length <= 2) {
            int i = (300 + (new Random()).nextInt(600)) * 20;

            if (args.length >= 2)
            {
                i = parseIntBounded(sender, args[1], 1, 1000000) * 20;
            }

            WorldServer world = MinecraftServer.getServer().worldServerForDimension(99);
            WorldInfo worldinfo = world.getWorldInfo();
            if ("clear".equals(args[0])) {
                        worldinfo.setRainTime(0);
                        worldinfo.setThunderTime(0);
                        worldinfo.setRaining(false);
                        worldinfo.setThundering(false);
                        func_152373_a(sender, this, "got.command.weather.clear", new Object[0]);
                        return;
            }

            else if ("rain".equals(args[0])) {
                        worldinfo.setRainTime((i));
                        worldinfo.setRaining(true);
                        worldinfo.setThundering(false);
                        func_152373_a(sender, this, "got.command.weather.rain", new Object[0]);
                        return;
            }

            else {
                    if (!"thunder".equalsIgnoreCase(args[0]))
                    {
                        throw new WrongUsageException("got.command.weather.usage", new Object[0]);
                    }

                    worldinfo.setRainTime(i);
                    worldinfo.setThunderTime(i);
                    worldinfo.setRaining(true);
                    worldinfo.setThundering(true);
                    func_152373_a(sender, this, "got.command.weather.thunder", new Object[0]);
            }
        }
        throw new WrongUsageException(getCommandUsage(sender));
    }
}

