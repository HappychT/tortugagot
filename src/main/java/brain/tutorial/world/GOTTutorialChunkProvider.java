package brain.tutorial.world;

import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.World;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import java.util.List;

public class GOTTutorialChunkProvider implements IChunkProvider {
    private World worldObj;

    public GOTTutorialChunkProvider(World world) {
        this.worldObj = world;
    }

    @Override
    public boolean chunkExists(int x, int z) { 
        return true; 
    }

    @Override
    public Chunk provideChunk(int x, int z) {
        Chunk chunk = new Chunk(worldObj, new net.minecraft.block.Block[65536], x, z);
        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public Chunk loadChunk(int x, int z) { 
        return provideChunk(x, z); 
    }

    @Override
    public void populate(IChunkProvider p, int x, int z) {
        // Empty
    }

    @Override
    public boolean saveChunks(boolean all, IProgressUpdate progress) { 
        return true; 
    }

    @Override
    public boolean unloadQueuedChunks() { 
        return false; 
    }

    @Override
    public boolean canSave() { 
        return true; 
    }

    @Override
    public String makeString() { 
        return "TutorialLevelSource"; 
    }

    @Override
    public List getPossibleCreatures(EnumCreatureType type, int x, int y, int z) { 
        return null; 
    }

    @Override
    public net.minecraft.world.ChunkPosition func_147416_a(World world, String type, int x, int y, int z) { 
        return null; 
    }

    @Override
    public int getLoadedChunkCount() { 
        return 0; 
    }

    @Override
    public void recreateStructures(int x, int z) {
        // Empty
    }

    @Override
    public void saveExtraData() {
        // Empty
    }
}
