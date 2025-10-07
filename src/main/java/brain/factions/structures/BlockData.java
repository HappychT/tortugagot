package brain.factions.structures;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;

public class BlockData {
    private String id;
    private int metadata;
    private int x;
    private int y;
    private int z;
    private String nbt;

    public BlockData(String id, int metadata, int x, int y, int z, String nbt) {
        this.id = id;
        this.metadata = metadata;
        this.x = x;
        this.y = y;
        this.z = z;
        this.nbt = nbt;
    }

    public String getId() { return id; }
    public int getMetadata() { return metadata; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }

    public NBTTagCompound getNbt() {
        if (this.nbt == null || this.nbt.isEmpty()) {
            return null;
        }
        try {
            return (NBTTagCompound) JsonToNBT.func_150315_a(this.nbt);
        } catch (NBTException e) {
            System.err.println("Failed to parse NBT string: " + this.nbt);
            e.printStackTrace();
            return null;
        }
    }
}