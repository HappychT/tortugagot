package brain.factions.servers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SiegeActivationManager {

    private static final int WAR_RADIUS = 500;
    private static final int RAID_RADIUS = 250;

    private static final SiegeActivationManager INSTANCE = new SiegeActivationManager();

    private final List<SiegeZone> warZones = new CopyOnWriteArrayList<>();
    private final List<SiegeZone> raidZones = new CopyOnWriteArrayList<>();

    public static SiegeActivationManager getInstance() {
        return INSTANCE;
    }

    public void addWarZone(int dimension, int x, int y, int z) {
        warZones.add(new SiegeZone(dimension, x, y, z, WAR_RADIUS));
    }

    public void addRaidZone(int dimension, int x, int y, int z) {
        raidZones.add(new SiegeZone(dimension, x, y, z, RAID_RADIUS));
    }

    public void clearWarZones() {
        warZones.clear();
    }

    public void clearRaidZones() {
        raidZones.clear();
    }

    public boolean isSiegeActive(int dimension, double x, double y, double z) {
        int ix = (int) Math.floor(x);
        int iy = (int) Math.floor(y);
        int iz = (int) Math.floor(z);

        for (SiegeZone zone : warZones) {
            if (zone.dimension != dimension) continue;
            if (zone.contains(ix, iy, iz)) return true;
        }

        if (StructureManager.isRaidTimeFortress) {
            for (SiegeZone zone : raidZones) {
                if (zone.dimension != dimension) continue;
                if (zone.contains(ix, iy, iz)) return true;
            }
        }

        return false;
    }

    private static class SiegeZone {
        final int dimension;
        final int x;
        final int y;
        final int z;
        final int radiusSq;

        SiegeZone(int dimension, int x, int y, int z, int radius) {
            this.dimension = dimension;
            this.x = x;
            this.y = y;
            this.z = z;
            this.radiusSq = radius * radius;
        }

        boolean contains(int px, int py, int pz) {
            int dx = px - x;
            int dy = py - y;
            int dz = pz - z;
            return (dx * dx + dy * dy + dz * dz) <= radiusSq;
        }
    }
}
