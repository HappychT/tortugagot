package com.tortugagot.togcore.technology;

import com.tortugagot.togcore.recipe.TOGRecipeBallistaBolt;
import got.common.GOTConfig;
import got.common.database.GOTMaterial;
import got.common.database.GOTRegistry;
import got.common.enchant.GOTEnchantment;
import got.common.enchant.GOTEnchantmentHelper;
import got.common.item.other.GOTItemGem;
import got.common.item.tool.GOTItemAxe;
import got.common.item.tool.GOTItemPickaxe;
import got.common.item.weapon.*;
import got.common.item.weapon.GOTItemSarbacane;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import noname.weapons.RegItem;
import noname.weapons.entity.EntityBalista;
import noname.weapons.entity.EntityBatteringRam;
import noname.weapons.entity.EntityCatapult;
import noname.weapons.entity.EntityTribushet;
import noname.weapons.item.ItemBalistaBolt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class TOGTechnologyAuditTest {
    public static void main(String[] args) {
        testGraphIntegrity();
        testTechnologyTreeLayoutSpec();
        testTechnologyTreeConnectionLayoutSpec();
        testBlacksmithBranchSpec();
        testMinerBranchSpec();
        testEngineerBranchSpec();
        testCookingBranchSpec();
        testBrewingBranchSpec();
        testWarriorBranchSpec();
        testSmithingTechnologyEffects();
        testMiningTechnologyEffects();
        testEngineeringTechnologyEffects();
        testCookingTechnologyEffects();
        testBrewingTechnologyEffects();
        testWarriorTechnologyEffects();
        testStaminaChoiceUnlocking();
        testLegendarySmithChanceConfig();
        testLegendarySmithEnchantments();
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
        assertTrue(byId.get("experienced_smith").getPrerequisites().contains("craftsman_vii"), "experienced_smith must require craftsman_vii");
        assertTrue(byId.get("master_smith").getPrerequisites().contains("experienced_smith"), "master_smith must require experienced_smith");
        assertTrue(byId.get("legendary_smith").getPrerequisites().contains("master_smith"), "legendary_smith must require master_smith");
        assertTrue(byId.get("legendary_smith").hasBranch(TOGTechnologyBranch.CRAFTSMAN), "legendary_smith must be craftsman");
        assertTrue(byId.get("experienced_miner").getPrerequisites().contains("craftsman_vii"), "experienced_miner must require craftsman_vii");
        assertTrue(byId.get("master_miner").getPrerequisites().contains("experienced_miner"), "master_miner must require experienced_miner");
        assertTrue(byId.get("legendary_miner").getPrerequisites().contains("master_miner"), "legendary_miner must require master_miner");
        assertTrue(byId.get("legendary_miner").hasBranch(TOGTechnologyBranch.MINER), "legendary_miner must be miner");
        assertTrue(byId.get("apprentice_engineer").getPrerequisites().contains("craftsman_vii"), "apprentice_engineer must require craftsman_vii");
        assertTrue(byId.get("engineer").getPrerequisites().contains("apprentice_engineer"), "engineer must require apprentice_engineer");
        assertTrue(byId.get("master_engineer").getPrerequisites().contains("engineer"), "master_engineer must require engineer");
        assertTrue(byId.get("master_engineer").hasBranch(TOGTechnologyBranch.ENGINEER), "master_engineer must be engineer");
        assertTrue(byId.get("cooking_i").getPrerequisites().contains("hunter_iii"), "cooking_i must require hunter_iii");
        assertTrue(byId.get("cooking_i").getPrerequisites().contains("farmer_iv"), "cooking_i must require farmer_iv");
        assertTrue(byId.get("cooking_ii").getPrerequisites().contains("cooking_i"), "cooking_ii must require cooking_i");
        assertTrue(byId.get("cooking_iii").getPrerequisites().contains("cooking_ii"), "cooking_iii must require cooking_ii");
        assertTrue(byId.get("cooking_iii").hasBranch(TOGTechnologyBranch.COOKING), "cooking_iii must be cooking");
    }

    private static void testTechnologyTreeLayoutSpec() {
        int visualNodeSize = 16;
        int nodeBorder = 1;
        int treeNodeXOffset = 28;
        int treeNodeYOffset = 18;
        int treeXScaleNumerator = 3;
        int treeXScaleDenominator = 2;
        int treeYScaleNumerator = 2;
        int treeYScaleDenominator = 1;
        List<TOGTechnology> technologies = TOGTechnologyRegistry.getAll();
        for (int i = 0; i < technologies.size(); i++) {
            TOGTechnology left = technologies.get(i);
            int leftX = treeNodeXOffset + left.getX() - nodeBorder;
            int leftY = treeNodeYOffset + left.getY() - nodeBorder;
            assertTrue(leftX >= 0, left.getId() + " layout must stay inside left edge");
            assertTrue(leftY >= 0, left.getId() + " layout must stay inside top edge");
            for (int j = i + 1; j < technologies.size(); j++) {
                TOGTechnology right = technologies.get(j);
                int leftScaledX = treeNodeXOffset + left.getX() * treeXScaleNumerator / treeXScaleDenominator - nodeBorder;
                int leftScaledY = treeNodeYOffset + left.getY() * treeYScaleNumerator / treeYScaleDenominator - nodeBorder;
                int rightScaledX = treeNodeXOffset + right.getX() * treeXScaleNumerator / treeXScaleDenominator - nodeBorder;
                int rightScaledY = treeNodeYOffset + right.getY() * treeYScaleNumerator / treeYScaleDenominator - nodeBorder;
                boolean separated = leftScaledX + visualNodeSize <= rightScaledX
                        || rightScaledX + visualNodeSize <= leftScaledX
                        || leftScaledY + visualNodeSize <= rightScaledY
                        || rightScaledY + visualNodeSize <= leftScaledY;
                assertTrue(separated, "technology icons must not overlap: " + left.getId() + " and " + right.getId());
            }
        }
    }

    private static void testTechnologyTreeConnectionLayoutSpec() {
        int visualNodeSize = 16;
        int nodeBorder = 1;
        int nodeSize = 14;
        int treeNodeXOffset = 28;
        int treeNodeYOffset = 18;
        int treeXScaleNumerator = 3;
        int treeXScaleDenominator = 2;
        int treeYScaleNumerator = 2;
        int treeYScaleDenominator = 1;
        int connectionNodeGap = 2;
        int connectionBusOffset = 10;
        int lineNodeClearance = 2;

        List<TOGTechnology> technologies = TOGTechnologyRegistry.getAll();
        for (TOGTechnology technology : technologies) {
            for (String prerequisiteId : technology.getPrerequisites()) {
                TOGTechnology prerequisite = requireTechnology(prerequisiteId);
                assertConnectionAvoidsOtherNodes(prerequisite, technology, technologies, treeNodeXOffset, treeNodeYOffset, treeXScaleNumerator, treeXScaleDenominator, treeYScaleNumerator, treeYScaleDenominator, nodeSize, visualNodeSize, nodeBorder, connectionNodeGap, connectionBusOffset, lineNodeClearance);
            }
            String[] requiredChoiceGroup = TOGTechnologyRules.getRequiredStaminaChoiceGroup(technology.getId());
            if (requiredChoiceGroup != null) {
                for (String choiceId : requiredChoiceGroup) {
                    TOGTechnology choice = requireTechnology(choiceId);
                    assertConnectionAvoidsOtherNodes(choice, technology, technologies, treeNodeXOffset, treeNodeYOffset, treeXScaleNumerator, treeXScaleDenominator, treeYScaleNumerator, treeYScaleDenominator, nodeSize, visualNodeSize, nodeBorder, connectionNodeGap, connectionBusOffset, lineNodeClearance);
                }
            }
        }
    }

    private static void assertConnectionAvoidsOtherNodes(TOGTechnology start, TOGTechnology end, List<TOGTechnology> technologies, int treeNodeXOffset, int treeNodeYOffset, int treeXScaleNumerator, int treeXScaleDenominator, int treeYScaleNumerator, int treeYScaleDenominator, int nodeSize, int visualNodeSize, int nodeBorder, int connectionNodeGap, int connectionBusOffset, int lineNodeClearance) {
        int startY = scaledNodeY(start, treeNodeYOffset, treeYScaleNumerator, treeYScaleDenominator) + nodeSize / 2;
        int endY = scaledNodeY(end, treeNodeYOffset, treeYScaleNumerator, treeYScaleDenominator) + nodeSize / 2;
        int startLeft = scaledNodeX(start, treeNodeXOffset, treeXScaleNumerator, treeXScaleDenominator);
        int startRight = startLeft + nodeSize;
        int endLeft = scaledNodeX(end, treeNodeXOffset, treeXScaleNumerator, treeXScaleDenominator);
        int endRight = endLeft + nodeSize;
        int startX;
        int endX;
        int busXOffset;

        if (endLeft > startRight) {
            startX = startRight + connectionNodeGap;
            endX = endLeft - connectionNodeGap;
            busXOffset = connectionBusOffset;
        } else if (endRight < startLeft) {
            startX = startLeft - connectionNodeGap;
            endX = endRight + connectionNodeGap;
            busXOffset = -connectionBusOffset;
        } else {
            startX = startLeft - connectionNodeGap;
            endX = endLeft - connectionNodeGap;
            busXOffset = -connectionBusOffset;
        }

        if (startY == endY) {
            assertSegmentAvoidsOtherNodes(start, end, technologies, startX, startY, endX, endY, treeNodeXOffset, treeNodeYOffset, treeXScaleNumerator, treeXScaleDenominator, treeYScaleNumerator, treeYScaleDenominator, visualNodeSize, nodeBorder, lineNodeClearance);
            return;
        }

        int busX = startX + busXOffset;
        assertSegmentAvoidsOtherNodes(start, end, technologies, startX, startY, busX, startY, treeNodeXOffset, treeNodeYOffset, treeXScaleNumerator, treeXScaleDenominator, treeYScaleNumerator, treeYScaleDenominator, visualNodeSize, nodeBorder, lineNodeClearance);
        assertSegmentAvoidsOtherNodes(start, end, technologies, busX, startY, busX, endY, treeNodeXOffset, treeNodeYOffset, treeXScaleNumerator, treeXScaleDenominator, treeYScaleNumerator, treeYScaleDenominator, visualNodeSize, nodeBorder, lineNodeClearance);
        assertSegmentAvoidsOtherNodes(start, end, technologies, busX, endY, endX, endY, treeNodeXOffset, treeNodeYOffset, treeXScaleNumerator, treeXScaleDenominator, treeYScaleNumerator, treeYScaleDenominator, visualNodeSize, nodeBorder, lineNodeClearance);
    }

    private static void assertSegmentAvoidsOtherNodes(TOGTechnology start, TOGTechnology end, List<TOGTechnology> technologies, int x1, int y1, int x2, int y2, int treeNodeXOffset, int treeNodeYOffset, int treeXScaleNumerator, int treeXScaleDenominator, int treeYScaleNumerator, int treeYScaleDenominator, int visualNodeSize, int nodeBorder, int lineNodeClearance) {
        for (TOGTechnology technology : technologies) {
            if (technology == start || technology == end) {
                continue;
            }
            int left = scaledNodeX(technology, treeNodeXOffset, treeXScaleNumerator, treeXScaleDenominator) - nodeBorder - lineNodeClearance;
            int top = scaledNodeY(technology, treeNodeYOffset, treeYScaleNumerator, treeYScaleDenominator) - nodeBorder - lineNodeClearance;
            int right = left + visualNodeSize + lineNodeClearance * 2;
            int bottom = top + visualNodeSize + lineNodeClearance * 2;
            assertTrue(!segmentIntersectsRect(x1, y1, x2, y2, left, top, right, bottom), "connection " + start.getId() + " -> " + end.getId() + " must keep clear of " + technology.getId());
        }
    }

    private static int scaledNodeX(TOGTechnology technology, int offset, int numerator, int denominator) {
        return offset + technology.getX() * numerator / denominator;
    }

    private static int scaledNodeY(TOGTechnology technology, int offset, int numerator, int denominator) {
        return offset + technology.getY() * numerator / denominator;
    }

    private static boolean segmentIntersectsRect(int x1, int y1, int x2, int y2, int left, int top, int right, int bottom) {
        if (x1 == x2) {
            int segmentTop = Math.min(y1, y2);
            int segmentBottom = Math.max(y1, y2);
            return x1 >= left && x1 <= right && segmentBottom >= top && segmentTop <= bottom;
        }
        if (y1 == y2) {
            int segmentLeft = Math.min(x1, x2);
            int segmentRight = Math.max(x1, x2);
            return y1 >= top && y1 <= bottom && segmentRight >= left && segmentLeft <= right;
        }
        return false;
    }

    private static void testBlacksmithBranchSpec() {
        TOGTechnology craftsmanVII = requireTechnology("craftsman_vii");
        TOGTechnology experienced = requireTechnology("experienced_smith");
        TOGTechnology master = requireTechnology("master_smith");
        TOGTechnology legendary = requireTechnology("legendary_smith");

        assertEquals(80, experienced.getCost(), "experienced_smith base cost");
        assertEquals(100, master.getCost(), "master_smith base cost");
        assertEquals(150, legendary.getCost(), "legendary_smith base cost");
        assertTrue(experienced.getX() > craftsmanVII.getX(), "experienced_smith must be placed after craftsman_vii");
        assertTrue(master.getX() > experienced.getX(), "master_smith must be placed after experienced_smith");
        assertTrue(legendary.getX() > master.getX(), "legendary_smith must be placed after master_smith");
        assertTrue(experienced.getPrerequisites().contains(craftsmanVII.getId()), "experienced_smith prerequisite");
        assertTrue(master.getPrerequisites().contains(experienced.getId()), "master_smith prerequisite");
        assertTrue(legendary.getPrerequisites().contains(master.getId()), "legendary_smith prerequisite");
        assertContains(legendary.getDescription(), "оружия, брони и инструментов", "legendary_smith description must include all valyrian smithing items");
    }

    private static void testMinerBranchSpec() {
        TOGTechnology craftsmanVII = requireTechnology("craftsman_vii");
        TOGTechnology experienced = requireTechnology("experienced_miner");
        TOGTechnology master = requireTechnology("master_miner");
        TOGTechnology legendary = requireTechnology("legendary_miner");

        assertEquals(50, experienced.getCost(), "experienced_miner base cost");
        assertEquals(80, master.getCost(), "master_miner base cost");
        assertEquals(150, legendary.getCost(), "legendary_miner base cost");
        assertTrue(experienced.getX() > craftsmanVII.getX(), "experienced_miner must be placed after craftsman_vii");
        assertTrue(master.getX() > experienced.getX(), "master_miner must be placed after experienced_miner");
        assertTrue(legendary.getX() > master.getX(), "legendary_miner must be placed after master_miner");
        assertTrue(experienced.getPrerequisites().contains(craftsmanVII.getId()), "experienced_miner prerequisite");
        assertTrue(master.getPrerequisites().contains(experienced.getId()), "master_miner prerequisite");
        assertTrue(legendary.getPrerequisites().contains(master.getId()), "legendary_miner prerequisite");
    }

    private static void testEngineerBranchSpec() {
        TOGTechnology craftsmanVII = requireTechnology("craftsman_vii");
        TOGTechnology apprentice = requireTechnology("apprentice_engineer");
        TOGTechnology engineer = requireTechnology("engineer");
        TOGTechnology master = requireTechnology("master_engineer");

        assertEquals(20, apprentice.getCost(), "apprentice_engineer base cost");
        assertEquals(80, engineer.getCost(), "engineer base cost");
        assertEquals(200, master.getCost(), "master_engineer base cost");
        assertTrue(apprentice.getX() > craftsmanVII.getX(), "apprentice_engineer must be placed after craftsman_vii");
        assertTrue(engineer.getX() > apprentice.getX(), "engineer must be placed after apprentice_engineer");
        assertTrue(master.getX() > engineer.getX(), "master_engineer must be placed after engineer");
        assertTrue(apprentice.getPrerequisites().contains(craftsmanVII.getId()), "apprentice_engineer prerequisite");
        assertTrue(engineer.getPrerequisites().contains(apprentice.getId()), "engineer prerequisite");
        assertTrue(master.getPrerequisites().contains(engineer.getId()), "master_engineer prerequisite");
        assertContains(engineer.getDescription(), "Таран доступен изначально", "engineer description must explain ram access");
        assertTrue(!engineer.getDescription().contains("кроме тарана"), "engineer description must not imply ram is locked");
        assertContains(engineer.getLockedEffect(), "Баллисту, катапульту и требушет", "engineer locked effect must only list restricted siege weapons");
    }

    private static void testCookingBranchSpec() {
        TOGTechnology hunterIII = requireTechnology("hunter_iii");
        TOGTechnology farmerIV = requireTechnology("farmer_iv");
        TOGTechnology cookingI = requireTechnology("cooking_i");
        TOGTechnology cookingII = requireTechnology("cooking_ii");
        TOGTechnology cookingIII = requireTechnology("cooking_iii");

        assertEquals(40, cookingI.getCost(), "cooking_i base cost");
        assertEquals(40, cookingII.getCost(), "cooking_ii base cost");
        assertEquals(40, cookingIII.getCost(), "cooking_iii base cost");
        assertTrue(cookingI.getX() > hunterIII.getX(), "cooking_i must be placed after hunter_iii");
        assertTrue(cookingI.getX() > farmerIV.getX(), "cooking_i must be placed after farmer_iv");
        assertTrue(cookingII.getX() > cookingI.getX(), "cooking_ii must be placed after cooking_i");
        assertTrue(cookingIII.getX() > cookingII.getX(), "cooking_iii must be placed after cooking_ii");
        assertTrue(cookingI.getPrerequisites().contains(hunterIII.getId()), "cooking_i hunter prerequisite");
        assertTrue(cookingI.getPrerequisites().contains(farmerIV.getId()), "cooking_i farmer prerequisite");
        assertTrue(cookingII.getPrerequisites().contains(cookingI.getId()), "cooking_ii prerequisite");
        assertTrue(cookingIII.getPrerequisites().contains(cookingII.getId()), "cooking_iii prerequisite");
    }

    private static void testBrewingBranchSpec() {
        TOGTechnology cookingI = requireTechnology("cooking_i");
        TOGTechnology gathererIII = requireTechnology("gatherer_iii");
        TOGTechnology brewing = requireTechnology("brewing");

        assertEquals(20, brewing.getCost(), "brewing base cost");
        assertTrue(brewing.getX() > cookingI.getX(), "brewing must be placed after cooking_i");
        assertTrue(brewing.getX() > gathererIII.getX(), "brewing must be placed after gatherer_iii");
        assertTrue(brewing.getY() < cookingI.getY(), "brewing must be placed above cooking_i");
        assertTrue(brewing.getPrerequisites().contains(cookingI.getId()), "brewing cooking prerequisite");
        assertTrue(brewing.getPrerequisites().contains(gathererIII.getId()), "brewing gatherer prerequisite");
        assertTrue(brewing.hasBranch(TOGTechnologyBranch.COOKING), "brewing must be in cooking branch");
    }

    private static void testWarriorBranchSpec() {
        assertWarriorPath("sword", "sword_mastery");
        assertWarriorPath("claymore", "claymore_mastery");
        assertWarriorPath("two_handed_sword", "two_handed_sword_mastery");
        assertWarriorPath("dagger", "dagger_mastery");
        assertWarriorPath("spear", "spear_mastery");
        assertWarriorPath("pike", "pike_mastery");
        assertWarriorPath("glaive", "glaive_mastery");
        assertWarriorPath("shield", "shield_mastery");
        assertWarriorPath("axe", "axe_mastery");
        assertWarriorPath("hammer", "hammer_mastery");
        assertBowPath();
        assertCrossbowPath();

        TOGTechnology axeMastery = requireTechnology("axe_mastery");
        TOGTechnology bowMastery = requireTechnology("bow_mastery");
        TOGTechnology crossbowMastery = requireTechnology("crossbow_mastery");
        assertTrue(bowMastery.getY() < axeMastery.getY(), "bow branch must be above axe lane");
        assertTrue(crossbowMastery.getY() < axeMastery.getY(), "crossbow branch must be above axe lane");
        assertTrue(crossbowMastery.getY() > bowMastery.getY(), "crossbow branch must stay below bow lane");
    }

    private static void assertWarriorPath(String prefix, String masteryId) {
        TOGTechnology mastery = requireTechnology(masteryId);
        TOGTechnology finalI = requireTechnology(prefix + "_final_damage_i");
        TOGTechnology staminaAttackI = requireTechnology(TOGTechnologyRules.firstAttackReductionId(prefix));
        TOGTechnology staminaBlockDrainI = requireTechnology(TOGTechnologyRules.firstBlockExhaustionId(prefix));
        TOGTechnology finalII = requireTechnology(prefix + "_final_damage_ii");
        TOGTechnology staminaBlockReductionII = requireTechnology(TOGTechnologyRules.secondBlockReductionId(prefix));
        TOGTechnology staminaAttackII = requireTechnology(TOGTechnologyRules.secondAttackReductionId(prefix));
        TOGTechnology finalIII = requireTechnology(prefix + "_final_damage_iii");
        TOGTechnology passive = requireTechnology(prefix + "_passive");

        assertEquals(150, finalI.getCost(), prefix + " final damage I cost");
        assertEquals(150, staminaAttackI.getCost(), prefix + " first attack stamina choice cost");
        assertEquals(150, staminaBlockDrainI.getCost(), prefix + " first block drain stamina choice cost");
        assertEquals(200, finalII.getCost(), prefix + " final damage II cost");
        assertEquals(200, staminaBlockReductionII.getCost(), prefix + " second block stamina choice cost");
        assertEquals(200, staminaAttackII.getCost(), prefix + " second attack stamina choice cost");
        assertEquals(250, finalIII.getCost(), prefix + " final damage III cost");
        assertEquals(800, passive.getCost(), prefix + " passive cost");
        assertTrue(finalI.getX() > mastery.getX(), prefix + " final damage I must be after mastery");
        assertTrue(staminaAttackI.getX() > finalI.getX(), prefix + " first stamina attack choice must be after final I");
        assertTrue(staminaBlockDrainI.getX() > finalI.getX(), prefix + " first stamina block drain choice must be after final I");
        assertTrue(finalII.getX() > staminaBlockDrainI.getX(), prefix + " final II must be after first stamina choices");
        assertTrue(staminaBlockReductionII.getX() > finalII.getX(), prefix + " second stamina block choice must be after final II");
        assertTrue(staminaAttackII.getX() > finalII.getX(), prefix + " second stamina attack choice must be after final II");
        assertTrue(finalIII.getX() > staminaAttackII.getX(), prefix + " final III must be after second stamina choices");
        assertTrue(passive.getX() > finalIII.getX(), prefix + " passive must be after final III");
        assertTrue(finalI.getPrerequisites().contains(masteryId), prefix + " final I prerequisite");
        assertTrue(staminaAttackI.getPrerequisites().contains(finalI.getId()), prefix + " first attack stamina choice prerequisite");
        assertTrue(staminaBlockDrainI.getPrerequisites().contains(finalI.getId()), prefix + " first block drain stamina choice prerequisite");
        assertTrue(finalII.getPrerequisites().contains(finalI.getId()), prefix + " final II base prerequisite");
        assertStaminaChoiceGroup(TOGTechnologyRules.getRequiredStaminaChoiceGroup(finalII.getId()), staminaAttackI.getId(), staminaBlockDrainI.getId(), prefix + " final II choice gate");
        assertTrue(staminaBlockReductionII.getPrerequisites().contains(finalII.getId()), prefix + " second block stamina choice prerequisite");
        assertTrue(staminaAttackII.getPrerequisites().contains(finalII.getId()), prefix + " second attack stamina choice prerequisite");
        assertTrue(finalIII.getPrerequisites().contains(finalII.getId()), prefix + " final III base prerequisite");
        assertStaminaChoiceGroup(TOGTechnologyRules.getRequiredStaminaChoiceGroup(finalIII.getId()), staminaBlockReductionII.getId(), staminaAttackII.getId(), prefix + " final III choice gate");
        assertTrue(passive.getPrerequisites().contains(finalIII.getId()), prefix + " passive prerequisite");
        assertEquals(staminaBlockDrainI.getId(), TOGTechnologyRules.getExclusiveStaminaChoiceId(staminaAttackI.getId()), prefix + " first choices must exclude each other");
        assertEquals(staminaAttackI.getId(), TOGTechnologyRules.getExclusiveStaminaChoiceId(staminaBlockDrainI.getId()), prefix + " first choices must exclude each other reverse");
        assertEquals(staminaAttackII.getId(), TOGTechnologyRules.getExclusiveStaminaChoiceId(staminaBlockReductionII.getId()), prefix + " second choices must exclude each other");
        assertEquals(staminaBlockReductionII.getId(), TOGTechnologyRules.getExclusiveStaminaChoiceId(staminaAttackII.getId()), prefix + " second choices must exclude each other reverse");
        assertContains(staminaAttackI.getDescription(), "уменьшает количество стамины в 1.5", prefix + " first attack choice description");
        assertContains(staminaBlockDrainI.getDescription(), "увеличивает количество стамины в 1.5", prefix + " first block drain choice description");
        assertContains(staminaBlockReductionII.getDescription(), "блокировании входящих атак", prefix + " second block choice description");
        assertContains(staminaAttackII.getDescription(), "суммируется", prefix + " second attack choice stacking description");
        if ("shield".equals(prefix)) {
            assertContains(finalI.getDescription(), "копий и пик со щитами", "shield final damage must be limited to shield spear/pike");
            assertContains(finalI.getLockedEffect(), "ветки копий и пик без щита не усиливают варианты со щитами", "shield final damage locked text");
        }
        assertPassiveText(passive, prefix);
    }

    private static void assertBowPath() {
        TOGTechnology mastery = requireTechnology("bow_mastery");
        TOGTechnology saveI = requireTechnology(TOGTechnologyLocks.BOW_ARROW_SAVE_I);
        TOGTechnology stamina = requireTechnology(TOGTechnologyLocks.BOW_STAMINA);
        TOGTechnology saveII = requireTechnology(TOGTechnologyLocks.BOW_ARROW_SAVE_II);
        TOGTechnology passive = requireTechnology(TOGTechnologyLocks.BOW_PASSIVE);

        assertEquals(300, saveI.getCost(), "bow arrow save I cost");
        assertEquals(500, stamina.getCost(), "bow stamina cost");
        assertEquals(300, saveII.getCost(), "bow arrow save II cost");
        assertEquals(800, passive.getCost(), "bow passive cost");
        assertTrue(saveI.getX() > mastery.getX(), "bow save I must be after mastery");
        assertTrue(stamina.getX() > saveI.getX(), "bow stamina must be after save I");
        assertTrue(saveII.getX() > stamina.getX(), "bow save II must be after stamina");
        assertTrue(passive.getX() > saveII.getX(), "bow passive must be after save II");
        assertTrue(saveI.getPrerequisites().contains(mastery.getId()), "bow save I prerequisite");
        assertTrue(stamina.getPrerequisites().contains(saveI.getId()), "bow stamina prerequisite");
        assertTrue(saveII.getPrerequisites().contains(stamina.getId()), "bow save II prerequisite");
        assertTrue(passive.getPrerequisites().contains(saveII.getId()), "bow passive prerequisite");
        assertPassiveText(passive, "bow");
    }

    private static void assertCrossbowPath() {
        TOGTechnology mastery = requireTechnology("crossbow_mastery");
        TOGTechnology saveI = requireTechnology(TOGTechnologyLocks.CROSSBOW_BOLT_SAVE_I);
        TOGTechnology stamina = requireTechnology(TOGTechnologyLocks.CROSSBOW_STAMINA);
        TOGTechnology saveII = requireTechnology(TOGTechnologyLocks.CROSSBOW_BOLT_SAVE_II);
        TOGTechnology passive = requireTechnology(TOGTechnologyLocks.CROSSBOW_PASSIVE);

        assertEquals(300, saveI.getCost(), "crossbow bolt save I cost");
        assertEquals(500, stamina.getCost(), "crossbow stamina cost");
        assertEquals(300, saveII.getCost(), "crossbow bolt save II cost");
        assertEquals(800, passive.getCost(), "crossbow passive cost");
        assertTrue(saveI.getX() > mastery.getX(), "crossbow save I must be after mastery");
        assertTrue(stamina.getX() > saveI.getX(), "crossbow stamina must be after save I");
        assertTrue(saveII.getX() > stamina.getX(), "crossbow save II must be after stamina");
        assertTrue(passive.getX() > saveII.getX(), "crossbow passive must be after save II");
        assertTrue(saveI.getPrerequisites().contains(mastery.getId()), "crossbow save I prerequisite");
        assertTrue(stamina.getPrerequisites().contains(saveI.getId()), "crossbow stamina prerequisite");
        assertTrue(saveII.getPrerequisites().contains(stamina.getId()), "crossbow save II prerequisite");
        assertTrue(passive.getPrerequisites().contains(saveII.getId()), "crossbow passive prerequisite");
        assertPassiveText(passive, "crossbow");
    }

    private static void assertPassiveText(TOGTechnology passive, String prefix) {
        assertTrue(passive.getDescription() != null && passive.getDescription().length() > 20, prefix + " passive description must be explicit");
        assertTrue(passive.getUnlocks() != null && passive.getUnlocks().length() > 10, prefix + " passive unlock text must be explicit");
        assertTrue(passive.getLockedEffect() != null && passive.getLockedEffect().length() > 10, prefix + " passive locked text must be explicit");
        assertTrue(!passive.getDescription().contains("открывает пассивную способность"), prefix + " passive description must not use placeholder text");
        if ("dagger".equals(prefix)) {
            assertTrue(passive.getName().contains("\u0423\u0434\u0430\u0440 \u0432 \u0441\u043F\u0438\u043D\u0443"), "dagger passive name must describe backstab effect");
            assertTrue(passive.getDescription().contains("\u043A\u0430\u0436\u0434\u0430\u044F \u0432\u0442\u043E\u0440\u0430\u044F \u0443\u0434\u0430\u0447\u043D\u0430\u044F \u0430\u0442\u0430\u043A\u0430 \u0432 \u0441\u043F\u0438\u043D\u0443"), "dagger passive description must describe every second successful back attack");
            assertTrue(passive.getUnlocks().contains("+2"), "dagger passive unlock text must mention pure damage amount");
        }
    }

    private static void assertStaminaChoiceGroup(String[] actual, String expectedFirst, String expectedSecond, String message) {
        assertTrue(actual != null && actual.length == 2, message + " must have two alternatives");
        assertEquals(expectedFirst, actual[0], message + " first alternative");
        assertEquals(expectedSecond, actual[1], message + " second alternative");
    }

    private static void testSmithingTechnologyEffects() {
        assertEquals(20, TOGSmithingTechnology.applyExperiencedSmithReforgeDiscount(false, 20), "reforge cost without experienced smith");
        assertEquals(10, TOGSmithingTechnology.applyExperiencedSmithReforgeDiscount(true, 20), "even reforge cost with experienced smith");
        assertEquals(11, TOGSmithingTechnology.applyExperiencedSmithReforgeDiscount(true, 21), "odd reforge cost must round up");
        assertEquals(0, TOGSmithingTechnology.applyExperiencedSmithReforgeDiscount(true, 0), "zero reforge cost stays zero");

        assertEquals(20, TOGSmithingTechnology.applyMasterSmithTemplateDiscount(false, 20), "template cost without master smith");
        assertEquals(10, TOGSmithingTechnology.applyMasterSmithTemplateDiscount(true, 20), "template cost with master smith");
        assertEquals(11, TOGSmithingTechnology.applyMasterSmithTemplateDiscount(true, 21), "template cost must round up");

        ItemStack valyrianSword = new ItemStack(new ItemSword(GOTMaterial.VALYRIAN_TOOL));
        ItemStack valyrianBow = new ItemStack(new GOTItemBow(GOTMaterial.VALYRIAN_TOOL));
        ItemStack valyrianCrossbow = new ItemStack(new GOTItemCrossbow(GOTMaterial.VALYRIAN_TOOL));
        ItemStack valyrianPickaxe = new ItemStack(new GOTItemPickaxe(GOTMaterial.VALYRIAN_TOOL2));
        ItemStack valyrianArmor = new ItemStack(new ItemArmor(GOTMaterial.VALYRIAN, 0, 1));
        ItemStack valyrianChainArmor = new ItemStack(new ItemArmor(GOTMaterial.VALYRIAN_CHAINMAIL, 0, 1));
        ItemStack ironSword = new ItemStack(new ItemSword(Item.ToolMaterial.IRON));
        ItemStack ironArmor = new ItemStack(new ItemArmor(ItemArmor.ArmorMaterial.IRON, 0, 1));
        assertTrue(TOGSmithingTechnology.isValyrianWeapon(valyrianSword), "valyrian sword must be detected");
        assertTrue(TOGSmithingTechnology.isValyrianWeapon(valyrianBow), "valyrian bow must be detected");
        assertTrue(TOGSmithingTechnology.isValyrianWeapon(valyrianCrossbow), "valyrian crossbow must be detected");
        assertTrue(TOGSmithingTechnology.isValyrianToolOrWeapon(valyrianPickaxe), "valyrian pickaxe must be detected");
        assertTrue(TOGSmithingTechnology.isValyrianArmor(valyrianArmor), "valyrian armor must be detected");
        assertTrue(TOGSmithingTechnology.isValyrianArmor(valyrianChainArmor), "valyrian chain armor must be detected");
        assertTrue(TOGSmithingTechnology.isValyrianSmithingItem(valyrianPickaxe), "valyrian tool must require legendary smith");
        assertTrue(TOGSmithingTechnology.isValyrianSmithingItem(valyrianArmor), "valyrian armor must require legendary smith");
        assertTrue(!TOGSmithingTechnology.isValyrianWeapon(ironSword), "ordinary sword must not be detected as valyrian");
        assertTrue(!TOGSmithingTechnology.isValyrianSmithingItem(ironArmor), "ordinary armor must not be detected as valyrian");
        assertTrue(!TOGSmithingTechnology.canUseValyrianWeapon(false, valyrianSword), "valyrian weapon must be blocked before legendary smith");
        assertTrue(!TOGSmithingTechnology.canUseValyrianWeapon(false, valyrianCrossbow), "valyrian crossbow must be blocked before legendary smith");
        assertTrue(!TOGSmithingTechnology.canUseValyrianSmithingItem(false, valyrianPickaxe), "valyrian tool must be blocked before legendary smith");
        assertTrue(!TOGSmithingTechnology.canUseValyrianSmithingItem(false, valyrianArmor), "valyrian armor must be blocked before legendary smith");
        assertTrue(!TOGSmithingTechnology.canUseValyrianWeapon(false, valyrianArmor), "legacy valyrian check must also block armor before legendary smith");
        assertTrue(TOGSmithingTechnology.canUseValyrianWeapon(true, valyrianSword), "valyrian weapon must be allowed after legendary smith");
        assertTrue(TOGSmithingTechnology.canUseValyrianSmithingItem(true, valyrianPickaxe), "valyrian tool must be allowed after legendary smith");
        assertTrue(TOGSmithingTechnology.canUseValyrianSmithingItem(true, valyrianArmor), "valyrian armor must be allowed after legendary smith");
        assertTrue(TOGSmithingTechnology.canUseValyrianWeapon(false, ironSword), "ordinary weapon must not require legendary smith");
        assertTrue(TOGSmithingTechnology.canUseValyrianSmithingItem(false, ironArmor), "ordinary armor must not require legendary smith");
    }

    private static void testMiningTechnologyEffects() {
        ItemStack pickaxe = new ItemStack(new GOTItemPickaxe(Item.ToolMaterial.IRON));
        GOTEnchantmentHelper.setHasEnchant(pickaxe, GOTEnchantment.looting2);

        assertEquals(0, TOGMiningTechnology.getEffectiveMiningLuck(false, false, pickaxe), "mining luck must be gated by mining technologies");
        assertEquals(2, TOGMiningTechnology.getEffectiveMiningLuck(true, false, pickaxe), "experienced miner must enable only mod luck for gems");
        assertEquals(3, TOGMiningTechnology.getEffectiveMiningLuck(true, true, pickaxe), "legendary miner must add +1 to mod luck");
        assertEquals(1, TOGMiningTechnology.getEffectiveMiningLuck(false, true, null), "legendary miner must grant +1 luck without tool modifiers");
        assertEquals(0, TOGMiningTechnology.getEffectiveMiningLuck(true, false, new ItemStack(new Item())), "ordinary Minecraft fortune must not be used as mining luck source");
        assertEquals(2, TOGMiningTechnology.getHalfKeptAmount(4, new FixedRandom(new boolean[] { true, false, true, false }, null)), "gems without experienced miner must be kept by per-item 50 percent rolls");
        assertEquals(0, TOGMiningTechnology.getHalfKeptAmount(3, new FixedRandom(new boolean[] { false, false, false }, null)), "failed gem rolls without experienced miner must destroy the drops");
        assertEquals(3, TOGMiningTechnology.applyModLuckAmount(3, 0, new FixedRandom(null, null)), "mod luck disabled must not change ore amount");
        assertEquals(8, TOGMiningTechnology.applyModLuckAmount(2, 3, new FixedRandom(null, new int[] { 4 })), "legendary miner must apply mod luck to ore amount before smelting");
        assertTrue(TOGMiningTechnology.isGemDrop(new ItemStack(new GOTItemGem())), "mod gem item must be treated as a gem");
        assertVanillaGemDropIfAvailable(Items.diamond, 0, "diamond must be treated as a gem");
        assertVanillaGemDropIfAvailable(Items.emerald, 0, "emerald must be treated as a gem");
        assertVanillaGemDropIfAvailable(Items.dye, 4, "lapis lazuli dye must be treated as a gem");
        if (Items.coal != null) {
            assertTrue(!TOGMiningTechnology.isGemDrop(new ItemStack(Items.coal)), "coal must not be treated as a gem");
            assertTrue(TOGMiningTechnology.isCoalDrop(new ItemStack(Items.coal)), "coal must be detected as a coal drop");
        }
        assertTrue(!TOGMiningTechnology.canAutoSmeltDrop(new ItemStack(new GOTItemGem()), new ItemStack(new Item())), "gems must not auto-smelt");
        if (Items.coal != null) {
            assertTrue(!TOGMiningTechnology.canAutoSmeltDrop(new ItemStack(Items.coal), new ItemStack(new Item())), "coal must not auto-smelt");
        }
        Item rawOre = new Item();
        Item ingot = new Item();
        assertTrue(TOGMiningTechnology.canAutoSmeltDrop(new ItemStack(rawOre), new ItemStack(ingot)), "raw ore with smelting result must be auto-smeltable");
        assertTrue(!TOGMiningTechnology.canAutoSmeltDrop(new ItemStack(ingot), new ItemStack(ingot)), "ready material must not auto-smelt into itself");
        ItemStack smelted = TOGMiningTechnology.getAutoSmeltedDrop(new ItemStack(rawOre, 4), new ItemStack(ingot, 2));
        assertTrue(smelted.getItem() == ingot, "auto-smelt must replace ore with smelting result");
        assertEquals(8, smelted.stackSize, "auto-smelt must preserve the whole mined amount");
        ItemStack noRecipe = TOGMiningTechnology.getAutoSmeltedDrop(new ItemStack(rawOre, 4), null);
        assertTrue(noRecipe.getItem() == rawOre, "drop without smelting result must stay unchanged");
        assertEquals(4, noRecipe.stackSize, "drop without smelting result must keep stack size");
        ItemStack gemDrop = TOGMiningTechnology.getAutoSmeltedDrop(new ItemStack(new GOTItemGem(), 3), new ItemStack(ingot));
        assertTrue(gemDrop.getItem() instanceof GOTItemGem, "gem drop must stay unchanged even if a smelting result exists");
        assertEquals(3, gemDrop.stackSize, "gem drop must keep stack size");
    }

    private static void testEngineeringTechnologyEffects() {
        assertTrue(!TOGEngineeringTechnology.canUseRepairKit(null), "repair kit must be blocked without apprentice engineer");
        assertTrue(!TOGEngineeringTechnology.canUseSiegeCrossbow(null), "siege crossbow must be blocked without master engineer");
        assertEquals(500, TOGEngineeringTechnology.getReloadTime(500.0F, false), "standard reload without master engineer");
        assertEquals(375, TOGEngineeringTechnology.getReloadTime(500.0F, true), "master engineer reload must be 75 percent");
        assertEquals(8, TOGEngineeringTechnology.getReloadTime(10.0F, true), "fractional master engineer reload must round up");
        assertEquals(1, TOGEngineeringTechnology.getReloadTime(1.0F, true), "reload time must stay at least one tick");
        assertEquals(0, TOGEngineeringTechnology.getReloadTime(0.0F, true), "zero reload time stays zero");

        assertTrue(TOGEngineeringTechnology.isRestrictedSiegeWeaponClass(EntityBalista.class), "balista must require engineer");
        assertTrue(TOGEngineeringTechnology.isRestrictedSiegeWeaponClass(EntityCatapult.class), "catapult must require engineer");
        assertTrue(TOGEngineeringTechnology.isRestrictedSiegeWeaponClass(EntityTribushet.class), "tribushet must require engineer");
        assertTrue(!TOGEngineeringTechnology.isRestrictedSiegeWeaponClass(EntityBatteringRam.class), "battering ram must stay available without engineer");
        assertTrue(!TOGEngineeringTechnology.isRestrictedSiegeWeaponClass(null), "null entity class must not be restricted");
        assertTrue(TOGEngineeringTechnology.isSiegeCrossbowItem(new GOTItemSiegeCrossbow()), "siege crossbow item must require master engineer");
        assertTrue(!TOGEngineeringTechnology.isSiegeCrossbowItem(new GOTItemCrossbow(Item.ToolMaterial.IRON)), "ordinary crossbow must not require master engineer");

        Item steel = new Item();
        Item stick = new Item();
        ItemStack[] ballistaRecipe = new ItemStack[9];
        ballistaRecipe[1] = new ItemStack(steel);
        ballistaRecipe[2] = new ItemStack(steel);
        ballistaRecipe[4] = new ItemStack(stick);
        ballistaRecipe[5] = new ItemStack(steel);
        ballistaRecipe[6] = new ItemStack(stick);
        assertTrue(TOGRecipeBallistaBolt.matchesBallistaBoltPattern(ballistaRecipe, steel, stick), "ballista bolt recipe pattern must match");

        ballistaRecipe[1] = null;
        assertTrue(!TOGRecipeBallistaBolt.matchesBallistaBoltPattern(ballistaRecipe, steel, stick), "ballista bolt recipe must require steel in the top middle slot");
        ballistaRecipe[1] = new ItemStack(steel);
        ballistaRecipe[0] = new ItemStack(stick);
        assertTrue(!TOGRecipeBallistaBolt.matchesBallistaBoltPattern(ballistaRecipe, steel, stick), "ballista bolt recipe must reject extra ingredients");

        ItemBalistaBolt previousBolt = RegItem.balistaBolt;
        try {
            RegItem.balistaBolt = new ItemBalistaBolt();
            InventoryBasic inventory = new InventoryBasic("ballista", false, 9);
            inventory.setInventorySlotContents(1, new ItemStack(steel));
            inventory.setInventorySlotContents(2, new ItemStack(steel));
            inventory.setInventorySlotContents(5, new ItemStack(steel));
            TOGRecipeBallistaBolt.consumeExtraSteel(new ItemStack(RegItem.balistaBolt), inventory);
            assertEquals(1, inventory.getStackInSlot(1).stackSize, "ballista bolt craft must not consume extra steel from top middle slot");
            assertEquals(1, inventory.getStackInSlot(2).stackSize, "ballista bolt craft must not consume extra steel from top right slot");
            assertEquals(1, inventory.getStackInSlot(5).stackSize, "ballista bolt craft must not consume extra steel from middle right slot");
        } finally {
            RegItem.balistaBolt = previousBolt;
        }
    }

    private static void testCookingTechnologyEffects() {
        Item previousRhinoCooked = GOTRegistry.rhinoCooked;
        Item previousMelonSoup = GOTRegistry.melonSoup;
        Item previousShishKebab = GOTRegistry.shishKebab;
        Item previousElephantCooked = GOTRegistry.elephantCooked;
        Item previousMarzipan = GOTRegistry.marzipan;
        Item previousPancakeMapleSyrup = GOTRegistry.pancakeMapleSyrup;
        Item previousMarzipanChocolate = GOTRegistry.marzipanChocolate;
        Item previousGingerbread = GOTRegistry.gingerbread;
        Item previousWalrusLardCooked = GOTRegistry.walrusLardCooked;
        try {
            GOTRegistry.rhinoCooked = new Item();
            GOTRegistry.melonSoup = new Item();
            GOTRegistry.shishKebab = new Item();
            GOTRegistry.elephantCooked = new Item();
            GOTRegistry.marzipan = new Item();
            GOTRegistry.pancakeMapleSyrup = new Item();
            GOTRegistry.marzipanChocolate = new Item();
            GOTRegistry.gingerbread = new Item();
            GOTRegistry.walrusLardCooked = new Item();

            assertEquals(TOGTechnologyLocks.COOKING_I, TOGCookingTechnology.getRequiredTechnologyForResult(new ItemStack(GOTRegistry.rhinoCooked)), "rhino cooked must require cooking I");
            assertEquals(TOGTechnologyLocks.COOKING_I, TOGCookingTechnology.getRequiredTechnologyForResult(new ItemStack(GOTRegistry.melonSoup)), "melon soup must require cooking I");
            assertEquals(TOGTechnologyLocks.COOKING_I, TOGCookingTechnology.getRequiredTechnologyForResult(new ItemStack(GOTRegistry.shishKebab)), "shish kebab must require cooking I");
            assertEquals(TOGTechnologyLocks.COOKING_II, TOGCookingTechnology.getRequiredTechnologyForResult(new ItemStack(GOTRegistry.elephantCooked)), "mammoth meat item must require cooking II");
            assertEquals(TOGTechnologyLocks.COOKING_II, TOGCookingTechnology.getRequiredTechnologyForResult(new ItemStack(GOTRegistry.marzipan)), "marzipan must require cooking II");
            assertEquals(TOGTechnologyLocks.COOKING_II, TOGCookingTechnology.getRequiredTechnologyForResult(new ItemStack(GOTRegistry.pancakeMapleSyrup)), "maple pancake must require cooking II");
            assertEquals(TOGTechnologyLocks.COOKING_III, TOGCookingTechnology.getRequiredTechnologyForResult(new ItemStack(GOTRegistry.marzipanChocolate)), "chocolate marzipan must require cooking III");
            assertEquals(TOGTechnologyLocks.COOKING_III, TOGCookingTechnology.getRequiredTechnologyForResult(new ItemStack(GOTRegistry.gingerbread)), "gingerbread must require cooking III");
            assertEquals(TOGTechnologyLocks.COOKING_III, TOGCookingTechnology.getRequiredTechnologyForResult(new ItemStack(GOTRegistry.walrusLardCooked)), "cooked walrus lard must require cooking III");
            assertTrue(!TOGCookingTechnology.canReceiveCookingResult(null, new ItemStack(GOTRegistry.rhinoCooked)), "restricted cooking result must be blocked without technology");
            assertTrue(TOGCookingTechnology.canReceiveCookingResult(null, new ItemStack(new Item())), "unlisted cooking result must be allowed");
            assertTrue(TOGCookingTechnology.getRequiredTechnologyForResult(new ItemStack(new Item())) == null, "unlisted food must not require cooking technology");
        } finally {
            GOTRegistry.rhinoCooked = previousRhinoCooked;
            GOTRegistry.melonSoup = previousMelonSoup;
            GOTRegistry.shishKebab = previousShishKebab;
            GOTRegistry.elephantCooked = previousElephantCooked;
            GOTRegistry.marzipan = previousMarzipan;
            GOTRegistry.pancakeMapleSyrup = previousPancakeMapleSyrup;
            GOTRegistry.marzipanChocolate = previousMarzipanChocolate;
            GOTRegistry.gingerbread = previousGingerbread;
            GOTRegistry.walrusLardCooked = previousWalrusLardCooked;
        }
    }

    private static void testBrewingTechnologyEffects() {
        Item previousSourMilk = GOTRegistry.mugSourMilk;
        Item previousMapleBeer = GOTRegistry.mugMapleBeer;
        Item previousKvass = GOTRegistry.mugPlumKvass;
        Item previousWhisky = GOTRegistry.mugWhisky;
        Item previousEthanol = GOTRegistry.mugEthanol;
        try {
            GOTRegistry.mugSourMilk = new Item();
            GOTRegistry.mugMapleBeer = new Item();
            GOTRegistry.mugPlumKvass = new Item();
            GOTRegistry.mugWhisky = new Item();
            GOTRegistry.mugEthanol = new Item();

            assertTrue(TOGBrewingTechnology.isRestrictedDrink(new ItemStack(GOTRegistry.mugSourMilk)), "sour milk must require brewing");
            assertTrue(TOGBrewingTechnology.isRestrictedDrink(new ItemStack(GOTRegistry.mugMapleBeer)), "maple beer must require brewing");
            assertTrue(TOGBrewingTechnology.isRestrictedDrink(new ItemStack(GOTRegistry.mugPlumKvass)), "kvass must require brewing");
            assertTrue(TOGBrewingTechnology.isRestrictedDrink(new ItemStack(GOTRegistry.mugWhisky)), "whisky must require brewing");
            assertTrue(TOGBrewingTechnology.isRestrictedDrink(new ItemStack(GOTRegistry.mugEthanol)), "ethanol must require brewing");
            assertTrue(!TOGBrewingTechnology.isRestrictedDrink(new ItemStack(new Item())), "unlisted drink must not require brewing");
            assertTrue(TOGBrewingTechnology.canBrew(null, null), "empty brewing result must not be blocked");
            assertTrue(!TOGBrewingTechnology.canBrew(null, new ItemStack(GOTRegistry.mugWhisky)), "restricted drink must be blocked without player technology");
            assertTrue(TOGBrewingTechnology.canBrew(null, new ItemStack(new Item())), "unlisted drink must not be blocked");
            assertEquals("\u0412\u044B \u043D\u0435 \u043C\u043E\u0436\u0435\u0442\u0435 \u043F\u0440\u0438\u0433\u043E\u0442\u043E\u0432\u0438\u0442\u044C \u044D\u0442\u043E\u0442 \u043D\u0430\u043F\u0438\u0442\u043E\u043A. \u0421\u043D\u0430\u0447\u0430\u043B\u0430 \u043E\u0442\u043A\u0440\u043E\u0439\u0442\u0435 \u0442\u0435\u0445\u043D\u043E\u043B\u043E\u0433\u0438\u044E \u201C\u041F\u0438\u0432\u043E\u0432\u0430\u0440\u0435\u043D\u0438\u0435\u201D", TOGBrewingTechnology.BLOCKED_MESSAGE, "brewing blocked message");
        } finally {
            GOTRegistry.mugSourMilk = previousSourMilk;
            GOTRegistry.mugMapleBeer = previousMapleBeer;
            GOTRegistry.mugPlumKvass = previousKvass;
            GOTRegistry.mugWhisky = previousWhisky;
            GOTRegistry.mugEthanol = previousEthanol;
        }
    }

    private static void testWarriorTechnologyEffects() {
        assertEquals("axe_final_damage_i", TOGWarriorTechnology.finalDamageTechnologyId(TOGWarriorTechnology.WeaponCategory.AXE, 1), "axe final I id");
        assertEquals("axe_final_damage_ii", TOGWarriorTechnology.finalDamageTechnologyId(TOGWarriorTechnology.WeaponCategory.AXE, 2), "axe final II id");
        assertEquals("axe_final_damage_iii", TOGWarriorTechnology.finalDamageTechnologyId(TOGWarriorTechnology.WeaponCategory.AXE, 3), "axe final III id");
        assertEquals(null, TOGWarriorTechnology.finalDamageTechnologyId(TOGWarriorTechnology.WeaponCategory.AXE, 4), "invalid final damage level must be rejected");
        assertEquals(null, TOGWarriorTechnology.finalDamageTechnologyId(TOGWarriorTechnology.WeaponCategory.BOW, 1), "bow must use its custom branch instead of final damage");
        assertEquals(null, TOGWarriorTechnology.finalDamageTechnologyId(TOGWarriorTechnology.WeaponCategory.CROSSBOW, 1), "crossbow must use its custom branch instead of final damage");
        assertEquals("axe_passive", TOGWarriorTechnology.passiveTechnologyId(TOGWarriorTechnology.WeaponCategory.AXE), "axe passive id");
        assertEquals(null, TOGWarriorTechnology.passiveTechnologyId(null), "null passive category must be rejected");
        assertEquals(5, TOGWarriorTechnology.passiveCounterThreshold(TOGWarriorTechnology.WeaponCategory.SWORD), "sword passive counter threshold");
        assertEquals(3, TOGWarriorTechnology.passiveCounterThreshold(TOGWarriorTechnology.WeaponCategory.CLAYMORE), "claymore passive counter threshold");
        assertEquals(3, TOGWarriorTechnology.passiveCounterThreshold(TOGWarriorTechnology.WeaponCategory.TWO_HANDED_SWORD), "two handed sword passive counter threshold");
        assertEquals(2, TOGWarriorTechnology.passiveCounterThreshold(TOGWarriorTechnology.WeaponCategory.DAGGER), "dagger passive counter threshold");
        assertEquals(2, TOGWarriorTechnology.passiveCounterThreshold(TOGWarriorTechnology.WeaponCategory.SPEAR), "spear passive counter threshold");
        assertEquals(3, TOGWarriorTechnology.passiveCounterThreshold(TOGWarriorTechnology.WeaponCategory.AXE), "axe passive counter threshold");
        assertEquals(3, TOGWarriorTechnology.passiveCounterThreshold(TOGWarriorTechnology.WeaponCategory.BOW), "bow passive counter threshold");
        assertEquals(5, TOGWarriorTechnology.passiveCounterThreshold(TOGWarriorTechnology.WeaponCategory.CROSSBOW), "crossbow passive counter threshold");
        assertEquals(0, TOGWarriorTechnology.passiveCounterThreshold(TOGWarriorTechnology.WeaponCategory.PIKE), "pike passive has no buildup counter");
        assertEquals(2, TOGWarriorTechnology.passiveCounterThreshold(TOGWarriorTechnology.WeaponCategory.HAMMER), "hammer passive must trigger every second hit");
        assertEquals("Фехтовальщик", TOGWarriorTechnology.passiveCounterLabel(TOGWarriorTechnology.WeaponCategory.SWORD), "sword passive counter label");
        assertEquals("\u0423\u0434\u0430\u0440 \u0432 \u0441\u043F\u0438\u043D\u0443", TOGWarriorTechnology.passiveCounterLabel(TOGWarriorTechnology.WeaponCategory.DAGGER), "dagger passive counter label");
        assertEquals("Тройной залп", TOGWarriorTechnology.passiveCounterLabel(TOGWarriorTechnology.WeaponCategory.CROSSBOW), "crossbow passive counter label");
        assertEquals("\u041E\u0433\u043B\u0443\u0448\u0435\u043D\u0438\u0435", TOGWarriorTechnology.passiveCounterLabel(TOGWarriorTechnology.WeaponCategory.HAMMER), "hammer passive counter label");
        assertTrue(TOGWarriorTechnology.isBackstabPosition(0.0D, -1.0D, 0.0D, 0.0D, 0.0F), "attacker behind target facing south");
        assertTrue(!TOGWarriorTechnology.isBackstabPosition(0.0D, 1.0D, 0.0D, 0.0D, 0.0F), "attacker in front must not be backstab");
        assertTrue(!TOGWarriorTechnology.isBackstabPosition(1.0D, 0.0D, 0.0D, 0.0D, 0.0F), "side hit must not be backstab");
        assertTrue(TOGWarriorTechnology.isBackstabPosition(1.0D, 0.0D, 0.0D, 0.0D, 90.0F), "attacker behind target facing west");
        assertDoubleEquals(0.0D, TOGWarriorTechnology.getBowArrowSaveChance(false, false), "bow no arrow save chance");
        assertDoubleEquals(0.05D, TOGWarriorTechnology.getBowArrowSaveChance(true, false), "bow first arrow save chance");
        assertDoubleEquals(0.10D, TOGWarriorTechnology.getBowArrowSaveChance(true, true), "bow full arrow save chance");
        assertDoubleEquals(0.0D, TOGWarriorTechnology.getCrossbowBoltSaveChance(false, false), "crossbow no bolt save chance");
        assertDoubleEquals(0.10D, TOGWarriorTechnology.getCrossbowBoltSaveChance(true, false), "crossbow first bolt save chance");
        assertDoubleEquals(0.20D, TOGWarriorTechnology.getCrossbowBoltSaveChance(true, true), "crossbow full bolt save chance");
        assertDoubleEquals(3.0D, TOGWarriorTechnology.getRangedShotStaminaCostForNodes(false, 3.0D), "ranged shot stamina cost without pressure");
        assertDoubleEquals(1.5D, TOGWarriorTechnology.getRangedShotStaminaCostForNodes(true, 3.0D), "stamina pressure must reduce owner ranged shot cost");
        assertDoubleEquals(1.0D, TOGWarriorTechnology.getRangedTargetStaminaDrainForNodes(false, 1.0D), "ranged target stamina drain without pressure");
        assertDoubleEquals(2.0D, TOGWarriorTechnology.getRangedTargetStaminaDrainForNodes(true, 1.0D), "stamina pressure must increase target ranged stamina drain");
        assertDoubleEquals(6.0D, TOGWarriorTechnology.getAttackStaminaCostForNodes(false, false, 6.0D), "attack stamina cost without choices");
        assertDoubleEquals(4.0D, TOGWarriorTechnology.getAttackStaminaCostForNodes(true, false, 6.0D), "first attack stamina choice must divide cost by 1.5");
        assertDoubleEquals(4.0D, TOGWarriorTechnology.getAttackStaminaCostForNodes(false, true, 6.0D), "second attack stamina choice must divide cost by 1.5");
        assertDoubleEquals(6.0D / 2.25D, TOGWarriorTechnology.getAttackStaminaCostForNodes(true, true, 6.0D), "attack stamina choices must stack");
        assertDoubleEquals(6.0D, TOGWarriorTechnology.getBlockingStaminaCostForNodes(false, 6.0D), "blocking stamina cost without choice");
        assertDoubleEquals(4.0D, TOGWarriorTechnology.getBlockingStaminaCostForNodes(true, 6.0D), "blocking stamina choice must divide cost by 1.5");
        assertDoubleEquals(9.0D, TOGWarriorTechnology.getBlockingTargetStaminaDrainForNodes(true, false, 6.0D), "block exhaustion choice must multiply blocker drain by 1.5");
        assertDoubleEquals(4.0D, TOGWarriorTechnology.getBlockingTargetStaminaDrainForNodes(false, true, 6.0D), "block reduction choice must reduce blocker drain by 1.5");
        assertDoubleEquals(6.0D, TOGWarriorTechnology.getBlockingTargetStaminaDrainForNodes(true, true, 6.0D), "opposing block stamina choices must combine multiplicatively");
        assertEquals(0, TOGWarriorTechnology.getFinalDamageBonusForNodes(0, true), "no final nodes against player");
        assertEquals(0, TOGWarriorTechnology.getFinalDamageBonusForNodes(-1, true), "negative final nodes must clamp to zero");
        assertEquals(1, TOGWarriorTechnology.getFinalDamageBonusForNodes(1, true), "one final node against player");
        assertEquals(3, TOGWarriorTechnology.getFinalDamageBonusForNodes(3, true), "three final nodes against player");
        assertEquals(2, TOGWarriorTechnology.getFinalDamageBonusForNodes(1, false), "one final node against NPC");
        assertEquals(6, TOGWarriorTechnology.getFinalDamageBonusForNodes(3, false), "three final nodes against NPC");
        assertEquals(6, TOGWarriorTechnology.getFinalDamageBonusForNodes(99, false), "final nodes must clamp to three");

        assertEquals(TOGWarriorTechnology.WeaponCategory.SWORD, TOGWarriorTechnology.getWeaponCategory(new ItemStack(new GOTItemSword(Item.ToolMaterial.IRON))), "sword category");
        assertEquals(TOGWarriorTechnology.WeaponCategory.CLAYMORE, TOGWarriorTechnology.getWeaponCategory(new ItemStack(new GOTItemArrynClaymore(Item.ToolMaterial.IRON))), "claymore category");
        assertEquals(TOGWarriorTechnology.WeaponCategory.TWO_HANDED_SWORD, TOGWarriorTechnology.getWeaponCategory(new ItemStack(new GOTItemGreatsword(Item.ToolMaterial.IRON))), "greatsword category");
        assertEquals(TOGWarriorTechnology.WeaponCategory.DAGGER, TOGWarriorTechnology.getWeaponCategory(new ItemStack(new GOTItemDagger(Item.ToolMaterial.IRON))), "dagger category");
        ItemStack spear = new ItemStack(new GOTItemSpear(Item.ToolMaterial.IRON));
        ItemStack pike = new ItemStack(new GOTItemPike(Item.ToolMaterial.IRON));
        ItemStack shieldSpear = new ItemStack(new GOTItemShieldSpear(Item.ToolMaterial.IRON));
        ItemStack shieldPike = new ItemStack(new GOTItemShieldPike(Item.ToolMaterial.IRON));
        assertEquals(TOGWarriorTechnology.WeaponCategory.SPEAR, TOGWarriorTechnology.getWeaponCategory(spear), "spear category");
        assertEquals(TOGWarriorTechnology.WeaponCategory.PIKE, TOGWarriorTechnology.getWeaponCategory(pike), "pike category");
        assertEquals(TOGWarriorTechnology.WeaponCategory.SPEAR, TOGWarriorTechnology.getWeaponCategory(shieldSpear), "shield spear must keep spear mastery category");
        assertEquals(TOGWarriorTechnology.WeaponCategory.PIKE, TOGWarriorTechnology.getWeaponCategory(shieldPike), "shield pike must keep pike mastery category");
        assertEquals(TOGWarriorTechnology.WeaponCategory.SPEAR, TOGWarriorTechnology.getFinalDamageCategory(spear), "plain spear must use spear final damage branch");
        assertEquals(TOGWarriorTechnology.WeaponCategory.PIKE, TOGWarriorTechnology.getFinalDamageCategory(pike), "plain pike must use pike final damage branch");
        assertEquals(TOGWarriorTechnology.WeaponCategory.SHIELD, TOGWarriorTechnology.getFinalDamageCategory(shieldSpear), "shield spear must use shield final damage branch");
        assertEquals(TOGWarriorTechnology.WeaponCategory.SHIELD, TOGWarriorTechnology.getFinalDamageCategory(shieldPike), "shield pike must use shield final damage branch");
        assertEquals(TOGWarriorTechnology.WeaponCategory.AXE, TOGWarriorTechnology.getWeaponCategory(new ItemStack(new GOTItemBattleaxe(Item.ToolMaterial.IRON))), "battleaxe category");
        assertEquals(TOGWarriorTechnology.WeaponCategory.HAMMER, TOGWarriorTechnology.getWeaponCategory(new ItemStack(new GOTItemHammer(Item.ToolMaterial.IRON))), "hammer category");
        assertEquals(TOGWarriorTechnology.WeaponCategory.BOW, TOGWarriorTechnology.getWeaponCategory(new ItemStack(new ItemBow())), "bow category");
        assertEquals(TOGWarriorTechnology.WeaponCategory.CROSSBOW, TOGWarriorTechnology.getWeaponCategory(new ItemStack(new GOTItemCrossbow(Item.ToolMaterial.IRON))), "crossbow category");
        assertEquals(null, TOGWarriorTechnology.getWeaponCategory(new ItemStack(new GOTItemSarbacane(Item.ToolMaterial.WOOD))), "sarbacane must not use bow/crossbow warrior branch");
        ItemStack toolAxe = new ItemStack(new GOTItemAxe(Item.ToolMaterial.IRON));
        ItemStack toolPickaxe = new ItemStack(new GOTItemPickaxe(Item.ToolMaterial.IRON));
        assertEquals(null, TOGWarriorTechnology.getWeaponCategory(toolAxe), "tool axe must not use axe warrior branch");
        assertEquals(null, TOGWarriorTechnology.getWeaponCategory(toolPickaxe), "pickaxe must not use axe warrior branch");
        assertEquals(null, TOGWeaponTechnology.getRequiredTechnology(toolAxe), "tool axe must not require axe mastery");
        assertEquals(null, TOGWeaponTechnology.getRequiredTechnology(toolPickaxe), "pickaxe must not require axe mastery");
        assertTrue(!TOGWeaponTechnology.isTrackedWeapon(toolAxe), "tool axe must not be tracked as weapon");
        assertTrue(!TOGWeaponTechnology.isTrackedWeapon(toolPickaxe), "pickaxe must not be tracked as weapon");
        assertTrue(!TOGWeaponTechnology.isMeleeWeapon(toolAxe), "tool axe must not count as melee warrior weapon");
        assertTrue(!TOGWeaponTechnology.isMeleeWeapon(toolPickaxe), "pickaxe must not count as melee warrior weapon");

        TestProjectile bowProjectile = new TestProjectile();
        TOGWarriorTechnology.markProjectileWeapon(bowProjectile, new ItemStack(new ItemBow()));
        assertEquals(TOGWarriorTechnology.WeaponCategory.BOW, TOGWarriorTechnology.getProjectileWeaponCategory(bowProjectile), "bow projectile category must be stored on projectile");
        TestProjectile crossbowProjectile = new TestProjectile();
        TOGWarriorTechnology.markProjectileWeapon(crossbowProjectile, new ItemStack(new GOTItemCrossbow(Item.ToolMaterial.IRON)));
        assertEquals(TOGWarriorTechnology.WeaponCategory.CROSSBOW, TOGWarriorTechnology.getProjectileWeaponCategory(crossbowProjectile), "crossbow projectile category must be stored on projectile");
        assertEquals(null, TOGWarriorTechnology.getProjectileWeaponCategory(new TestProjectile()), "unmarked projectile must not have a weapon category");
    }

    private static void testStaminaChoiceUnlocking() {
        TOGTechnologyPlayerData data = new TOGTechnologyPlayerData(null);
        data.setMasteryPoints(10000);
        assertTrue(data.openTechnology("strong_hands"), "admin open strong_hands for stamina choice test");
        assertTrue(data.openTechnology("sword_mastery"), "admin open sword mastery for stamina choice test");
        assertTrue(data.openTechnology("sword_final_damage_i"), "admin open sword final I for stamina choice test");

        assertContains(data.getBlockedReason("sword_final_damage_ii"), "первого выбора стамины", "final II must require one first stamina choice");
        assertTrue(data.unlock(TOGTechnologyRules.firstAttackReductionId("sword")), "first attack stamina choice should unlock");
        assertContains(data.getBlockedReason(TOGTechnologyRules.firstBlockExhaustionId("sword")), "альтернативный узел стамины", "first alternative must be blocked after choice");
        assertEquals(null, data.getBlockedReason("sword_final_damage_ii"), "final II must unlock after one first stamina choice");
        assertTrue(data.unlock("sword_final_damage_ii"), "final II should unlock after first stamina choice");

        assertContains(data.getBlockedReason("sword_final_damage_iii"), "второго выбора стамины", "final III must require one second stamina choice");
        assertTrue(data.unlock(TOGTechnologyRules.secondAttackReductionId("sword")), "second attack stamina choice should unlock");
        assertContains(data.getBlockedReason(TOGTechnologyRules.secondBlockReductionId("sword")), "альтернативный узел стамины", "second alternative must be blocked after choice");
        assertEquals(null, data.getBlockedReason("sword_final_damage_iii"), "final III must unlock after one second stamina choice");
    }

    private static void testLegendarySmithChanceConfig() {
        GOTConfig.config = null;
        assertEquals(7, GOTConfig.getLegendarySmithChanceKeys().length, "legendary smith chance key count");
        assertTrue(GOTConfig.isLegendarySmithChanceKey("swift"), "swift chance key");
        assertTrue(GOTConfig.isLegendarySmithChanceKey("extended"), "extended chance key");
        assertTrue(GOTConfig.isLegendarySmithChanceKey("mighty"), "mighty chance key");
        assertTrue(GOTConfig.isLegendarySmithChanceKey("armor_protection"), "armor protection chance key");
        assertTrue(GOTConfig.isLegendarySmithChanceKey("fall_protection"), "fall protection chance key");
        assertTrue(GOTConfig.isLegendarySmithChanceKey("projectile_protection"), "projectile protection chance key");
        assertTrue(GOTConfig.isLegendarySmithChanceKey("powerful_ranged"), "powerful ranged chance key");

        GOTConfig.legendarySmithMeleeSpeedChance = 2.5D;
        GOTConfig.legendarySmithMeleeReachChance = 2.0D;
        GOTConfig.legendarySmithMightyChance = 0.35D;
        GOTConfig.legendarySmithRangedDamageChance = 2.5D;
        ItemStack sword = new ItemStack(new ItemSword(Item.ToolMaterial.IRON));
        ItemStack bow = new ItemStack(new ItemBow());
        assertDoubleEquals(2.5D, GOTEnchantmentHelper.getLegendarySmithChance(GOTEnchantment.meleeSpeed2, sword), "swift chance mapping");
        assertDoubleEquals(2.0D, GOTEnchantmentHelper.getLegendarySmithChance(GOTEnchantment.meleeReach2, sword), "extended chance mapping");
        assertDoubleEquals(0.35D, GOTEnchantmentHelper.getLegendarySmithChance(GOTEnchantment.strong3, sword), "mighty chance mapping");
        assertTrue(GOTEnchantmentHelper.getLegendarySmithChance(GOTEnchantment.strong3, sword) < GOTEnchantmentHelper.getLegendarySmithChance(GOTEnchantment.meleeSpeed2, sword), "mighty chance must stay much lower than swift");
        assertDoubleEquals(2.5D, GOTEnchantmentHelper.getLegendarySmithChance(GOTEnchantment.rangedStrong2, bow), "powerful ranged chance mapping");
        assertDoubleEquals(0.0D, GOTEnchantmentHelper.getLegendarySmithChance(GOTEnchantment.rangedStrong2, sword), "powerful ranged chance must not apply to melee weapons");

        assertDoubleEquals(100.0D, GOTConfig.setLegendarySmithChance("mighty", 150.0D), "chance must clamp above 100");
        assertDoubleEquals(100.0D, GOTConfig.getLegendarySmithChance("mighty"), "clamped high chance must be applied");
        assertDoubleEquals(0.0D, GOTConfig.setLegendarySmithChance("mighty", -1.0D), "chance must clamp below 0");
        assertDoubleEquals(0.0D, GOTConfig.getLegendarySmithChance("mighty"), "clamped low chance must be applied");
        assertTrue(Double.isNaN(GOTConfig.setLegendarySmithChance("unknown", 1.0D)), "unknown chance key must be rejected");
    }

    private static void testLegendarySmithEnchantments() {
        setLegendarySmithChances(100.0D);

        ItemStack ordinarySword = new ItemStack(new ItemSword(Item.ToolMaterial.IRON));
        GOTEnchantmentHelper.applyRandomEnchantments(ordinarySword, new Random(1L), false, true, false, false);
        assertNoLegendarySmithModifiers(ordinarySword, "legendary modifiers must not roll without legendary smith");

        ItemStack legendarySword = new ItemStack(new ItemSword(Item.ToolMaterial.IRON));
        GOTEnchantmentHelper.applyRandomEnchantments(legendarySword, new Random(2L), false, true, false, true);
        assertTrue(GOTEnchantmentHelper.hasEnchant(legendarySword, GOTEnchantment.meleeSpeed2), "legendary sword must roll swift at 100 percent");
        assertTrue(GOTEnchantmentHelper.hasEnchant(legendarySword, GOTEnchantment.meleeReach2), "legendary sword must roll extended at 100 percent");
        assertTrue(GOTEnchantmentHelper.hasEnchant(legendarySword, GOTEnchantment.strong3), "legendary sword must roll mighty at 100 percent");
        assertFloatEquals(1.2F, GOTEnchantmentHelper.calcMeleeSpeedFactor(legendarySword), "swift speed factor");
        assertFloatEquals(1.2F, GOTEnchantmentHelper.calcMeleeReachFactor(legendarySword), "extended reach factor");
        assertFloatEquals(2.0F, GOTEnchantmentHelper.calcBaseMeleeDamageBoost(legendarySword), "mighty damage boost");

        ItemStack boots = new ItemStack(new ItemArmor(ItemArmor.ArmorMaterial.IRON, 0, 3));
        GOTEnchantmentHelper.applyRandomEnchantments(boots, new Random(3L), false, true, false, true);
        assertTrue(GOTEnchantmentHelper.hasEnchant(boots, GOTEnchantment.protect1), "legendary armor must roll protection at 100 percent");
        assertTrue(GOTEnchantmentHelper.hasEnchant(boots, GOTEnchantment.protectFall2), "legendary boots must roll padded at 100 percent");
        assertTrue(GOTEnchantmentHelper.hasEnchant(boots, GOTEnchantment.protectRanged2), "legendary armor must roll deflecting at 100 percent");
        assertEquals(1, GOTEnchantmentHelper.calcCommonArmorProtection(boots), "protection armor value");
        assertEquals(6, GOTEnchantment.protectFall2 instanceof got.common.enchant.GOTEnchantmentProtectionFall ? ((got.common.enchant.GOTEnchantmentProtectionFall) GOTEnchantment.protectFall2).calcIntProtection() : -1, "padded fall protection value");
        assertEquals(2, GOTEnchantment.protectRanged2 instanceof got.common.enchant.GOTEnchantmentProtectionRanged ? ((got.common.enchant.GOTEnchantmentProtectionRanged) GOTEnchantment.protectRanged2).calcIntProtection() : -1, "deflecting projectile protection value");

        ItemStack bow = new ItemStack(new ItemBow());
        GOTEnchantmentHelper.applyRandomEnchantments(bow, new Random(4L), false, true, false, true);
        assertTrue(GOTEnchantmentHelper.hasEnchant(bow, GOTEnchantment.rangedStrong2), "legendary bow must roll powerful at 100 percent");
        assertTrue(GOTEnchantment.rangedStrong2.applyToProjectile(), "powerful ranged modifier must be transferred to projectile");
        assertTrue(GOTEnchantmentHelper.shouldApplyProjectileEnchantment(bow, GOTEnchantment.rangedStrong2), "powerful must apply to bow projectile");
        assertFloatEquals(1.0F, GOTEnchantmentHelper.calcRangedLaunchDamageFactor(bow), "powerful must not change launch damage");
        TestProjectile projectile = new TestProjectile();
        GOTEnchantmentHelper.setProjectileEnchantment(projectile, GOTEnchantment.rangedStrong2);
        assertFloatEquals(1.2F, GOTEnchantmentHelper.calcProjectileRangedDamageFactor(projectile), "powerful must change projectile damage on hit");

        ItemStack crossbow = new ItemStack(new GOTItemCrossbow(Item.ToolMaterial.IRON));
        assertTrue(GOTEnchantment.rangedStrong2.canApply(crossbow, false), "powerful must apply to crossbows");
        assertTrue(!GOTEnchantment.rangedStrong2.canApply(legendarySword, false), "powerful must not apply to melee weapons");
        assertTrue(!GOTEnchantment.meleeSpeed2.canApply(boots, false), "swift must not apply to armor");
        assertTrue(!GOTEnchantment.protect1.canApply(legendarySword, false), "armor protection must not apply to weapons");

        ItemStack sarbacane = new ItemStack(new GOTItemSarbacane(Item.ToolMaterial.WOOD));
        GOTEnchantmentHelper.setHasEnchant(sarbacane, GOTEnchantment.rangedStrong2);
        assertTrue(!GOTEnchantment.rangedStrong2.canApply(sarbacane, false), "powerful must not apply to sarbacane");
        assertTrue(!GOTEnchantmentHelper.shouldApplyProjectileEnchantment(sarbacane, GOTEnchantment.rangedStrong2), "powerful must not transfer from sarbacane");

        ItemStack copy = legendarySword.copy();
        assertTrue(GOTEnchantmentHelper.hasEnchant(copy, GOTEnchantment.strong3), "legendary modifier must survive item transfer/copy");
        assertTrue(copy.hasTagCompound() && copy.getTagCompound().hasKey("GOTEnch"), "legendary modifier must be stored in item NBT");
        if (Items.iron_sword != null) {
            ItemStack persistentSword = new ItemStack(Items.iron_sword);
            GOTEnchantmentHelper.setHasEnchant(persistentSword, GOTEnchantment.strong3);
            NBTTagCompound saved = new NBTTagCompound();
            persistentSword.writeToNBT(saved);
            ItemStack loaded = ItemStack.loadItemStackFromNBT(saved);
            assertTrue(GOTEnchantmentHelper.hasEnchant(loaded, GOTEnchantment.strong3), "legendary modifier must survive NBT save/load");
        }

        for (int i = 0; i < 250; i++) {
            ItemStack sword = new ItemStack(new ItemSword(Item.ToolMaterial.IRON));
            GOTEnchantmentHelper.applyRandomEnchantments(sword, new Random(1000L + i), false, true, true, false);
            for (GOTEnchantment enchantment : GOTEnchantmentHelper.getEnchantList(sword)) {
                assertTrue(enchantment.isBeneficial(), "master smith reforge must not roll negative modifiers: " + enchantment.enchantName);
            }
        }
    }

    private static void testCostsUnlockingAndReset() {
        TOGTechnologyPlayerData data = new TOGTechnologyPlayerData(null);
        data.setMasteryPoints(100);

        assertContains(data.getBlockedReason("craftsman_i"), "предыдущая технология", "craftsman_i must require strong_hands");
        assertTrue(data.unlock("strong_hands"), "strong_hands should unlock");
        assertEquals(85, data.getMasteryPoints(), "strong_hands should cost 15 as first unlock");
        assertEquals(15, data.getSpentTechnologyPoints(), "spent after first unlock");
        assertTrue(!data.unlock("strong_hands"), "repeat unlock should fail");
        assertEquals(85, data.getMasteryPoints(), "repeat unlock must not charge points");

        assertTrue(data.unlock("craftsman_i"), "craftsman_i should unlock after strong_hands");
        assertEquals(55, data.getMasteryPoints(), "craftsman_i should cost 30 as second unlock");
        assertEquals(45, data.getSpentTechnologyPoints(), "spent after two unlocks");

        int refund = data.resetTechnologies();
        assertEquals(45, refund, "reset refund");
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

    private static TOGTechnology requireTechnology(String id) {
        TOGTechnology technology = TOGTechnologyRegistry.get(id);
        assertTrue(technology != null, "missing technology: " + id);
        return technology;
    }

    private static void setLegendarySmithChances(double chance) {
        GOTConfig.legendarySmithMeleeSpeedChance = chance;
        GOTConfig.legendarySmithMeleeReachChance = chance;
        GOTConfig.legendarySmithMightyChance = chance;
        GOTConfig.legendarySmithArmorProtectionChance = chance;
        GOTConfig.legendarySmithFallProtectionChance = chance;
        GOTConfig.legendarySmithProjectileProtectionChance = chance;
        GOTConfig.legendarySmithRangedDamageChance = chance;
    }

    private static void assertNoLegendarySmithModifiers(ItemStack itemstack, String message) {
        for (GOTEnchantment enchantment : GOTEnchantmentHelper.getEnchantList(itemstack)) {
            assertTrue(!GOTEnchantmentHelper.isLegendarySmithModifier(enchantment), message + ": " + enchantment.enchantName);
        }
    }

    private static void assertVanillaGemDropIfAvailable(Item item, int meta, String message) {
        if (item != null) {
            assertTrue(TOGMiningTechnology.isGemDrop(new ItemStack(item, 1, meta)), message);
        }
    }

    private static class TestProjectile extends Entity {
        private TestProjectile() {
            super((World) null);
        }

        @Override
        protected void entityInit() {
        }

        @Override
        protected void readEntityFromNBT(NBTTagCompound nbt) {
        }

        @Override
        protected void writeEntityToNBT(NBTTagCompound nbt) {
        }
    }

    private static class FixedRandom extends Random {
        private final boolean[] booleans;
        private final int[] ints;
        private int booleanIndex;
        private int intIndex;

        private FixedRandom(boolean[] booleans, int[] ints) {
            this.booleans = booleans != null ? booleans : new boolean[0];
            this.ints = ints != null ? ints : new int[0];
        }

        @Override
        public boolean nextBoolean() {
            if (booleanIndex >= booleans.length) {
                throw new AssertionError("missing fixed boolean");
            }
            return booleans[booleanIndex++];
        }

        @Override
        public int nextInt(int bound) {
            if (intIndex >= ints.length) {
                throw new AssertionError("missing fixed int");
            }
            int value = ints[intIndex++];
            if (value < 0 || value >= bound) {
                throw new AssertionError("fixed int " + value + " is out of bound " + bound);
            }
            return value;
        }
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message + ": expected " + expected + ", got " + actual);
        }
    }

    private static void assertEquals(String expected, String actual, String message) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(message + ": expected " + expected + ", got " + actual);
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(message + ": expected " + expected + ", got " + actual);
        }
    }

    private static void assertDoubleEquals(double expected, double actual, String message) {
        if (Math.abs(expected - actual) > 0.0001D) {
            throw new AssertionError(message + ": expected " + expected + ", got " + actual);
        }
    }

    private static void assertFloatEquals(float expected, float actual, String message) {
        if (Math.abs(expected - actual) > 0.0001F) {
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
