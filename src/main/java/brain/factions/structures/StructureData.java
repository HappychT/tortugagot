package brain.factions.structures;

import java.util.List;

public class StructureData {
    private String name;
    private List<BlockData> blocks;
    private int width;
    private int depth;

    public StructureData(String name, List<BlockData> blocks, int width, int depth) {
        this.name = name;
        this.blocks = blocks;
        this.width = width;
        this.depth = depth;
    }

    public String getName() { return name; }
    public List<BlockData> getBlocks() { return blocks; }
    public int getWidth() { return width; }
    public int getDepth() { return depth; }
}