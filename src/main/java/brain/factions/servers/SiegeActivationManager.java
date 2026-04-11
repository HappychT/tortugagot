package brain.factions.servers;

import brain.factions.structures.FactionStructureManager;
import brain.factions.structures.FactionStructureSlot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    public void rebuildRaidZones(int dimension) {
        clearRaidZones();
        if (FactionStructureManager.structureSlots == null) {
            return;
        }

        Set<String> added = new HashSet<>();

        if (StructureManager.isRaidTime) {
            for (FactionStructureSlot slot : FactionStructureManager.structureSlots) {
                if (slot == null || slot.category == FactionStructureSlot.StructureCategory.FORTRESS) {
                    continue;
                }
                addRaidZone(dimension, slot.xCoord, slot.yCoord, slot.zCoord);
                if (slot.id != null) {
                    added.add(slot.id);
                }
            }
        }

        if (!StructureManager.activeRaidPoints.isEmpty()) {
            for (String id : StructureManager.activeRaidPoints) {
                if (id == null || added.contains(id)) {
                    continue;
                }
                FactionStructureSlot slot = FactionStructureManager.getStructureById(id);
                if (slot == null || slot.category == FactionStructureSlot.StructureCategory.FORTRESS) {
                    continue;
                }
                addRaidZone(dimension, slot.xCoord, slot.yCoord, slot.zCoord);
            }
        }
    }

    public boolean isSiegeActive(int dimension, double x, double y, double z) {
        int ix = (int) Math.floor(x);
        int iy = (int) Math.floor(y);
        int iz = (int) Math.floor(z);

        for (SiegeZone zone : warZones) {
            if (zone.dimension != dimension) continue;
            if (zone.contains(ix, iy, iz)) return true;
        }

        for (SiegeZone zone : raidZones) {
            if (zone.dimension != dimension) continue;
            if (zone.contains(ix, iy, iz)) return true;
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
