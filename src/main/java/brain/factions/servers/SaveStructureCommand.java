package brain.factions.servers;

import brain.factions.structures.BlockData;
import brain.factions.structures.StructureData;
import com.google.gson.Gson;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SaveStructureCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "save-structure";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/save-structure <name> <x1> <y1> <z1> <x2> <y2> <z2>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 7) {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            return;
        }

        String name = args[0];
        int x1 = parseInt(sender, args[1]);
        int y1 = parseInt(sender, args[2]);
        int z1 = parseInt(sender, args[3]);
        int x2 = parseInt(sender, args[4]);
        int y2 = parseInt(sender, args[5]);
        int z2 = parseInt(sender, args[6]);

        World world = sender.getEntityWorld();
        List<BlockData> blocks = new ArrayList<>();
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);
        int maxZ = Math.max(z1, z2);
        int originX = minX;
        int originY = minY;
        int originZ = minZ;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlock(x, y, z);
                    if (block != Blocks.air) {
                        int metadata = world.getBlockMetadata(x, y, z);
                        TileEntity te = world.getTileEntity(x, y, z);
                        String nbtString = null;
                        if (te != null) {
                            NBTTagCompound nbt = new NBTTagCompound();
                            te.writeToNBT(nbt);
                            nbtString = nbt.toString();
                        }
                        blocks.add(new BlockData(Block.blockRegistry.getNameForObject(block), metadata, x - originX, y - originY, z - originZ, nbtString));
                    }
                }
            }
        }

        if (blocks.isEmpty()) {
            sender.addChatMessage(new ChatComponentText("§eВыделенная область не содержит блоков."));
            return;
        }

        int width = maxX - minX;
        int depth = maxZ - minZ;

        try {
            File structuresFolder = new File(CoreFaction.configFolder, "structures");
            if (!structuresFolder.exists()) structuresFolder.mkdirs();
            File structureFile = new File(structuresFolder, name + ".json");
            FileWriter writer = new FileWriter(structureFile);
            new Gson().toJson(new StructureData(name, blocks, width, depth), writer);
            writer.close();
            sender.addChatMessage(new ChatComponentText("§aСтруктура '" + name + ".json' успешно сохранена с размерами: " + width + "x" + depth));
        } catch (IOException e) {
            sender.addChatMessage(new ChatComponentText("§cОшибка при сохранении структуры."));
            e.printStackTrace();
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender.canCommandSenderUseCommand(4, getCommandName());
    }
}