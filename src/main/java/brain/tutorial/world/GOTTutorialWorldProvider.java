package brain.tutorial.world;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;

public class GOTTutorialWorldProvider extends WorldProvider {

    @Override
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new net.minecraft.world.biome.WorldChunkManagerHell(net.minecraft.world.biome.BiomeGenBase.plains, 0.0F);
        this.dimensionId = got.common.GOTDimension.TUTORIAL.dimensionID;
    }

    @Override
    public String getDimensionName() {
        return "Tutorial";
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new GOTTutorialChunkProvider(this.worldObj);
    }
    
    @Override
    public boolean canRespawnHere() {
        return false;
    }
    
    @Override
    public boolean isSurfaceWorld() {
        return true;
    }
}
