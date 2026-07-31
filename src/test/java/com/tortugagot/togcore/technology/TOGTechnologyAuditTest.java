package com.tortugagot.togcore.technology;

import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TOGTechnologyAuditTest {
    public static void main(String[] args) {
        testGraphIntegrity();
        testCostsUnlockingAndReset();
        testMasteryPointOverflow();
        testAdminOpenClose();
        testSavedDataRoundTrip();
        testNotifierMessage();
        System.out.println("TOGTechnologyAuditTest: PASS");
    }

    private static void testGraphIntegrity() {
        Map<String, TOGTechnology> byId = new LinkedHashMap<String, TOGTechnology>();
        for (TOGTechnology technology : TOGTechnologyRegistry.getAll()) {
            assertTrue(technology.getId() != null && technology.getId().length() > 0, "technology id is empty");
            assertTrue(technology.getName() != null && technology.getName().length() > 0, "technology name is empty for " + technology.getId());
            assertTrue(technology.getDescription() != null && technology.getDescription().length() > 0, "technology description is empty for " + technology.getId());
            assertTrue(technology.getUnlocks() != null && technology.getUnlocks().length() > 0, "technology unlock text is empty for " + technology.getId());
            assertTrue(technology.getBaseCost() >= 0, "technology base cost is negative for " + technology.getId());
            assertTrue(technology.getBranchMask() != 0, "technology branch mask is empty for " + technology.getId());
            assertTrue(!byId.containsKey(technology.getId()), "duplicate technology id: " + technology.getId());
            byId.put(technology.getId(), technology);
        }
        assertTrue(byId.size() >= 30, "technology tree unexpectedly small: " + byId.size());
        assertTrue(byId.containsKey("strong_hands"), "missing strong_hands");
        assertTrue(byId.containsKey("keen_eye"), "missing keen_eye");

        for (TOGTechnology technology : byId.values()) {
            for (String prerequisiteId : technology.getPrerequisites()) {
                assertTrue(byId.containsKey(prerequisiteId), "missing prerequisite " + prerequisiteId + " for " + technology.getId());
                assertTrue(!technology.getId().equals(prerequisiteId), "self prerequisite for " + technology.getId());
            }
            assertNoCycle(technology.getId(), byId, new LinkedHashSet<String>());
        }

        Set<String> reachable = new HashSet<String>();
        for (TOGTechnology technology : byId.values()) {
            if (technology.getPrerequisites().isEmpty()) {
                markReachable(technology.getId(), byId, reachable);
            }
        }
        assertTrue(reachable.containsAll(byId.keySet()), "unreachable technologies: " + difference(byId.keySet(), reachable));

        assertTrue(byId.get("strong_hands").hasBranch(TOGTechnologyBranch.CRAFTSMAN), "strong_hands must be craftsman");
        assertTrue(byId.get("strong_hands").hasBranch(TOGTechnologyBranch.WARRIOR), "strong_hands must be warrior");
        assertTrue(byId.get("keen_eye").hasBranch(TOGTechnologyBranch.WARRIOR), "keen_eye must be warrior");
        assertTrue(byId.get("keen_eye").hasBranch(TOGTechnologyBranch.GATHERER), "keen_eye must be gatherer");
    }

    private static void testCostsUnlockingAndReset() {
        TOGTechnologyPlayerData data = new TOGTechnologyPlayerData(null);
        data.setMasteryPoints(100);

        assertContains(data.getBlockedReason("craftsman_i"), "предыдущая технология", "craftsman_i must require strong_hands");
        assertTrue(data.unlock("strong_hands"), "strong_hands should unlock");
        assertEquals(88, data.getMasteryPoints(), "strong_hands should cost 12 as first unlock");
        assertEquals(12, data.getSpentTechnologyPoints(), "spent after first unlock");
        assertTrue(!data.unlock("strong_hands"), "repeat unlock should fail");
        assertEquals(88, data.getMasteryPoints(), "repeat unlock must not charge points");

        assertTrue(data.unlock("craftsman_i"), "craftsman_i should unlock after strong_hands");
        assertEquals(64, data.getMasteryPoints(), "craftsman_i should cost 24 as second unlock");
        assertEquals(36, data.getSpentTechnologyPoints(), "spent after two unlocks");

        int refund = data.resetTechnologies();
        assertEquals(36, refund, "reset refund");
        assertEquals(100, data.getMasteryPoints(), "reset should restore spent points");
        assertTrue(data.getUnlockedTechnologies().isEmpty(), "reset should clear unlocked technologies");
        assertEquals(0, data.resetTechnologies(), "second reset should not refund twice");
        assertEquals(100, data.getMasteryPoints(), "second reset must not duplicate points");
    }

    private static void testAdminOpenClose() {
        TOGTechnologyPlayerData data = new TOGTechnologyPlayerData(null);
        data.setMasteryPoints(0);
        assertTrue(data.openTechnology("strong_hands"), "admin open strong_hands");
        assertTrue(data.openTechnology("craftsman_i"), "admin open craftsman_i");
        assertTrue(data.hasUnlocked("craftsman_i"), "craftsman_i should be admin-opened");
        assertEquals(0, data.getSpentTechnologyPoints(), "admin open should not mark spent points");
        int closed = data.closeTechnologyTree("strong_hands");
        assertEquals(2, closed, "closing strong_hands should close dependent craftsman_i");
        assertTrue(!data.hasUnlocked("strong_hands"), "strong_hands should be closed");
        assertTrue(!data.hasUnlocked("craftsman_i"), "dependent craftsman_i should be closed");
    }

    private static void testMasteryPointOverflow() {
        TOGTechnologyPlayerData data = new TOGTechnologyPlayerData(null);
        data.setMasteryPoints(Integer.MAX_VALUE - 5);
        data.addMasteryPoints(10);
        assertEquals(Integer.MAX_VALUE, data.getMasteryPoints(), "mastery point add should saturate instead of overflowing");
        data.addMasteryPoints(-100);
        assertEquals(Integer.MAX_VALUE, data.getMasteryPoints(), "negative add should not reduce points");
    }

    private static void testSavedDataRoundTrip() {
        UUID playerUuid = UUID.fromString("11111111-1111-1111-1111-111111111111");
        Set<String> unlocked = new LinkedHashSet<String>();
        unlocked.add("strong_hands");
        unlocked.add("craftsman_i");
        Map<String, Integer> spent = new HashMap<String, Integer>();
        spent.put("strong_hands", 12);
        spent.put("craftsman_i", 24);

        TOGTechnologySavedData saved = new TOGTechnologySavedData("test");
        saved.savePlayer(playerUuid, 64, unlocked, spent);

        NBTTagCompound nbt = new NBTTagCompound();
        saved.writeToNBT(nbt);

        TOGTechnologySavedData loaded = new TOGTechnologySavedData("test");
        loaded.readFromNBT(nbt);

        TOGTechnologyPlayerData playerData = new TOGTechnologyPlayerData(null);
        assertTrue(loaded.loadPlayer(playerUuid, playerData), "saved data should load by UUID");
        assertEquals(64, playerData.getMasteryPoints(), "loaded mastery points");
        assertEquals(36, playerData.getSpentTechnologyPoints(), "loaded spent points");
        assertTrue(playerData.hasUnlocked("strong_hands"), "loaded strong_hands");
        assertTrue(playerData.hasUnlocked("craftsman_i"), "loaded craftsman_i");
        assertTrue(!loaded.hasTechnology(UUID.fromString("22222222-2222-2222-2222-222222222222"), "strong_hands"), "other UUID must not inherit technology");
    }

    private static void testNotifierMessage() {
        String message = TOGTechnologyNotifier.buildMessage("создать сталь", "не открыта технология", TOGTechnologyLocks.STEEL);
        assertContains(message, "Не удалось: создать сталь", "notification action");
        assertContains(message, "Причина: не открыта технология", "notification reason");
        assertContains(message, "Ремесленник I", "notification technology name");
    }

    private static void assertNoCycle(String id, Map<String, TOGTechnology> byId, Set<String> visiting) {
        assertTrue(visiting.add(id), "cycle detected: " + visiting + " -> " + id);
        for (String prerequisiteId : byId.get(id).getPrerequisites()) {
            assertNoCycle(prerequisiteId, byId, visiting);
        }
        visiting.remove(id);
    }

    private static void markReachable(String id, Map<String, TOGTechnology> byId, Set<String> reachable) {
        if (!reachable.add(id)) {
            return;
        }
        for (TOGTechnology technology : byId.values()) {
            if (technology.getPrerequisites().contains(id)) {
                markReachable(technology.getId(), byId, reachable);
            }
        }
    }

    private static Set<String> difference(Set<String> left, Set<String> right) {
        Set<String> result = new LinkedHashSet<String>(left);
        result.removeAll(right);
        return result;
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message + ": expected " + expected + ", got " + actual);
        }
    }

    private static void assertContains(String value, String expectedPart, String message) {
        assertTrue(value != null && value.contains(expectedPart), message + ": expected substring '" + expectedPart + "' in '" + value + "'");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
