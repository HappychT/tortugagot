package brain.factions.arenas;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class ArenaRegion {
    private final String name;
    private final int dimensionId;
    private final int minX, minY, minZ;
    private final int maxX, maxY, maxZ;

    public ArenaRegion(String name, int dim, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.name = name;
        this.dimensionId = dim;

        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
    }

    public String getName() {
        return name;
    }

    public int getDimensionId() { return dimensionId; }
    public int getMinX() { return minX; }
    public int getMinY() { return minY; }
    public int getMinZ() { return minZ; }
    public int getMaxX() { return maxX; }
    public int getMaxY() { return maxY; }
    public int getMaxZ() { return maxZ; }


    public boolean isPlayerInside(EntityPlayer player) {
        if (player.dimension != this.dimensionId) {
            return false;
        }

        int playerX = MathHelper.floor_double(player.posX);
        int playerY = MathHelper.floor_double(player.posY);
        int playerZ = MathHelper.floor_double(player.posZ);

        return playerX >= minX && playerX <= maxX &&
                playerY >= minY && playerY <= maxY &&
                playerZ >= minZ && playerZ <= maxZ;
    }
}