package com.tortugagot.togcore.technology;

import com.tortugagot.togcore.network.TOGPacketHandler;
import com.tortugagot.togcore.network.TOGPacketPassiveCounter;
import got.common.database.GOTEffects;
import got.common.item.weapon.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class TOGWarriorTechnology {
    public static final String PROJECTILE_CATEGORY_KEY = "TOGWarriorWeaponCategory";
    private static final String CROSSBOW_RELOAD_COMBO_KEY = "TOGCrossbowReloadCombo";
    private static final String CROSSBOW_LAST_SHOT_TIME_KEY = "TOGCrossbowLastShotTime";
    private static final String CROSSBOW_TRIPLE_LOADED_KEY = "TOGCrossbowTripleLoaded";
    private static final double BOW_ARROW_SAVE_STEP = 0.05D;
    private static final double CROSSBOW_BOLT_SAVE_STEP = 0.10D;
    private static final int BOW_SLOW_HIT_THRESHOLD = 3;
    private static final int BOW_SLOW_DURATION_TICKS = 60;
    private static final int BOW_SLOW_AMPLIFIER = 3;
    private static final int CROSSBOW_RELOAD_WINDOW_TICKS = 100;
    private static final int CROSSBOW_TRIPLE_RELOAD_THRESHOLD = 5;
    private static final int GLAIVE_REDIRECTION_WINDOW_TICKS = 20;
    private static final Map<UUID, Map<String, Integer>> COMBO_COUNTS = new HashMap<UUID, Map<String, Integer>>();
    private static final Map<UUID, UUID> BOW_SLOW_LAST_TARGETS = new HashMap<UUID, UUID>();
    private static final Map<UUID, Integer> BOW_SLOW_HIT_COUNTS = new HashMap<UUID, Integer>();
    private static final Set<UUID> CLEAVER_READY = new HashSet<UUID>();
    private static final Map<UUID, Long> GLAIVE_REDIRECTION_READY_UNTIL = new HashMap<UUID, Long>();
    private static final Map<UUID, Long> SHIELD_WALL_SHIFT_STARTED = new HashMap<UUID, Long>();
    private static final Set<UUID> SHIELD_WALL_READY = new HashSet<UUID>();
    private static final Map<UUID, Integer> SUPPRESSED_KNOCKBACK_TICKS = new HashMap<UUID, Integer>();
    private static boolean applyingPureDamage;

    private TOGWarriorTechnology() {
    }

    public enum WeaponCategory {
        SWORD("sword"),
        CLAYMORE("claymore"),
        TWO_HANDED_SWORD("two_handed_sword"),
        DAGGER("dagger"),
        SPEAR("spear"),
        PIKE("pike"),
        GLAIVE("glaive"),
        SHIELD("shield"),
        AXE("axe"),
        HAMMER("hammer"),
        BOW("bow"),
        CROSSBOW("crossbow");

        private final String prefix;

        WeaponCategory(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    public static class CombatAdjustment {
        private float amount;
        private boolean ignoreBlock;
        private boolean skipBlockVulnerability;
        private boolean suppressKnockback;

        private CombatAdjustment(float amount) {
            this.amount = amount;
        }

        public float getAmount() {
            return amount;
        }

        public boolean ignoresBlock() {
            return ignoreBlock;
        }

        public boolean skipsBlockVulnerability() {
            return skipBlockVulnerability;
        }

        public boolean suppressesKnockback() {
            return suppressKnockback;
        }
    }

    public static void resetRuntimeState() {
        COMBO_COUNTS.clear();
        BOW_SLOW_LAST_TARGETS.clear();
        BOW_SLOW_HIT_COUNTS.clear();
        CLEAVER_READY.clear();
        GLAIVE_REDIRECTION_READY_UNTIL.clear();
        SHIELD_WALL_SHIFT_STARTED.clear();
        SHIELD_WALL_READY.clear();
        SUPPRESSED_KNOCKBACK_TICKS.clear();
        applyingPureDamage = false;
    }

    public static boolean isApplyingPureDamage() {
        return applyingPureDamage;
    }

    public static String finalDamageTechnologyId(WeaponCategory category, int level) {
        if (category == null || category == WeaponCategory.BOW || category == WeaponCategory.CROSSBOW || level < 1 || level > 3) {
            return null;
        }
        return category.getPrefix() + "_final_damage_" + romanLevel(level);
    }

    public static String passiveTechnologyId(WeaponCategory category) {
        return category == null ? null : category.getPrefix() + "_passive";
    }

    static int passiveCounterThreshold(WeaponCategory category) {
        if (category == WeaponCategory.SWORD) {
            return 5;
        }
        if (category == WeaponCategory.CLAYMORE || category == WeaponCategory.TWO_HANDED_SWORD || category == WeaponCategory.AXE || category == WeaponCategory.BOW) {
            return 3;
        }
        if (category == WeaponCategory.DAGGER || category == WeaponCategory.SPEAR) {
            return 2;
        }
        if (category == WeaponCategory.CROSSBOW) {
            return 5;
        }
        return 0;
    }

    static String passiveCounterLabel(WeaponCategory category) {
        if (category == WeaponCategory.SWORD) {
            return "\u0424\u0435\u0445\u0442\u043E\u0432\u0430\u043B\u044C\u0449\u0438\u043A";
        }
        if (category == WeaponCategory.CLAYMORE) {
            return "\u0423\u0434\u0430\u0440 \u0433\u0430\u0440\u0434\u043E\u0439";
        }
        if (category == WeaponCategory.TWO_HANDED_SWORD) {
            return "\u0420\u0430\u0441\u0441\u0435\u043A\u0430\u0442\u0435\u043B\u044C";
        }
        if (category == WeaponCategory.DAGGER) {
            return "\u0423\u0434\u0430\u0440 \u0432 \u0441\u043F\u0438\u043D\u0443";
        }
        if (category == WeaponCategory.SPEAR) {
            return "\u041F\u0440\u043E\u043D\u0437\u0430\u044E\u0449\u0438\u0439";
        }
        if (category == WeaponCategory.AXE) {
            return "\u0416\u0430\u0436\u0434\u0430 \u043A\u0440\u043E\u0432\u0438";
        }
        if (category == WeaponCategory.BOW) {
            return "\u0417\u0430\u043C\u0435\u0434\u043B\u044F\u044E\u0449\u0430\u044F \u0441\u0435\u0440\u0438\u044F";
        }
        if (category == WeaponCategory.CROSSBOW) {
            return "\u0422\u0440\u043E\u0439\u043D\u043E\u0439 \u0437\u0430\u043B\u043F";
        }
        return "";
    }

    public static double getBowArrowSaveChance(EntityPlayer player) {
        return getBowArrowSaveChance(TOGTechnologyLocks.has(player, TOGTechnologyLocks.BOW_ARROW_SAVE_I), TOGTechnologyLocks.has(player, TOGTechnologyLocks.BOW_ARROW_SAVE_II));
    }

    static double getBowArrowSaveChance(boolean firstLevel, boolean secondLevel) {
        double chance = 0.0D;
        if (firstLevel) {
            chance += BOW_ARROW_SAVE_STEP;
        }
        if (secondLevel) {
            chance += BOW_ARROW_SAVE_STEP;
        }
        return chance;
    }

    public static double getCrossbowBoltSaveChance(EntityPlayer player) {
        return getCrossbowBoltSaveChance(TOGTechnologyLocks.has(player, TOGTechnologyLocks.CROSSBOW_BOLT_SAVE_I), TOGTechnologyLocks.has(player, TOGTechnologyLocks.CROSSBOW_BOLT_SAVE_II));
    }

    static double getCrossbowBoltSaveChance(boolean firstLevel, boolean secondLevel) {
        double chance = 0.0D;
        if (firstLevel) {
            chance += CROSSBOW_BOLT_SAVE_STEP;
        }
        if (secondLevel) {
            chance += CROSSBOW_BOLT_SAVE_STEP;
        }
        return chance;
    }

    public static boolean rollBowArrowPreserved(EntityPlayer player) {
        return rollChance(player, getBowArrowSaveChance(player));
    }

    public static boolean rollCrossbowBoltPreserved(EntityPlayer player) {
        return rollChance(player, getCrossbowBoltSaveChance(player));
    }

    public static double getAttackStaminaCost(EntityPlayer player, ItemStack weapon, double baseCost) {
        WeaponCategory category = getWeaponCategory(weapon);
        if (category == null) {
            return baseCost;
        }
        return getAttackStaminaCostForNodes(
                TOGTechnologyLocks.has(player, TOGTechnologyRules.firstAttackReductionId(category.getPrefix())),
                TOGTechnologyLocks.has(player, TOGTechnologyRules.secondAttackReductionId(category.getPrefix())),
                baseCost);
    }

    static double getAttackStaminaCostForNodes(boolean firstAttackReduction, boolean secondAttackReduction, double baseCost) {
        double cost = baseCost;
        if (firstAttackReduction) {
            cost /= TOGTechnologyRules.STAMINA_CHOICE_FACTOR;
        }
        if (secondAttackReduction) {
            cost /= TOGTechnologyRules.STAMINA_CHOICE_FACTOR;
        }
        return cost;
    }

    public static double getBlockingTargetStaminaDrain(EntityPlayer attacker, EntityPlayer blocker, DamageSource source, double baseDrain) {
        double drain = getBlockingStaminaCost(blocker, blocker != null ? blocker.getHeldItem() : null, baseDrain);
        WeaponCategory attackCategory = getDamageCategory(attacker, source);
        if (attackCategory != null && TOGTechnologyLocks.has(attacker, TOGTechnologyRules.firstBlockExhaustionId(attackCategory.getPrefix()))) {
            drain *= TOGTechnologyRules.STAMINA_CHOICE_FACTOR;
        }
        return drain;
    }

    public static double getBlockingStaminaCost(EntityPlayer blocker, ItemStack blockingWeapon, double baseCost) {
        WeaponCategory category = getWeaponCategory(blockingWeapon);
        if (category == null) {
            return baseCost;
        }
        return getBlockingStaminaCostForNodes(TOGTechnologyLocks.has(blocker, TOGTechnologyRules.secondBlockReductionId(category.getPrefix())), baseCost);
    }

    static double getBlockingStaminaCostForNodes(boolean blockReduction, double baseCost) {
        return blockReduction ? baseCost / TOGTechnologyRules.STAMINA_CHOICE_FACTOR : baseCost;
    }

    static double getBlockingTargetStaminaDrainForNodes(boolean attackerBlockExhaustion, boolean blockerBlockReduction, double baseDrain) {
        double drain = getBlockingStaminaCostForNodes(blockerBlockReduction, baseDrain);
        return attackerBlockExhaustion ? drain * TOGTechnologyRules.STAMINA_CHOICE_FACTOR : drain;
    }

    public static double getBowShotStaminaCost(EntityPlayer player, double baseCost) {
        return TOGTechnologyLocks.has(player, TOGTechnologyLocks.BOW_STAMINA) ? baseCost * 2.0D : baseCost;
    }

    public static double getCrossbowShotStaminaCost(EntityPlayer player, double baseCost) {
        return TOGTechnologyLocks.has(player, TOGTechnologyLocks.CROSSBOW_STAMINA) ? baseCost * 2.0D : baseCost;
    }

    public static double getRangedTargetStaminaDrain(EntityPlayer attacker, DamageSource source, double baseDrain) {
        WeaponCategory category = getDamageCategory(attacker, source);
        if (category == WeaponCategory.BOW && TOGTechnologyLocks.has(attacker, TOGTechnologyLocks.BOW_STAMINA)) {
            return baseDrain * 2.0D;
        }
        if (category == WeaponCategory.CROSSBOW && TOGTechnologyLocks.has(attacker, TOGTechnologyLocks.CROSSBOW_STAMINA)) {
            return baseDrain * 2.0D;
        }
        return baseDrain;
    }

    public static int getFinalDamageBonusForNodes(int unlockedNodes, boolean targetIsPlayer) {
        int nodes = Math.max(0, Math.min(3, unlockedNodes));
        return nodes * (targetIsPlayer ? 1 : 2);
    }

    public static int getUnlockedFinalDamageNodes(EntityPlayer player, WeaponCategory category) {
        if (player == null || category == null) {
            return 0;
        }
        int unlocked = 0;
        for (int level = 1; level <= 3; level++) {
            if (TOGTechnologyLocks.has(player, finalDamageTechnologyId(category, level))) {
                unlocked++;
            }
        }
        return unlocked;
    }

    public static int getFinalDamageBonus(EntityPlayer attacker, EntityLivingBase target, WeaponCategory category) {
        return getFinalDamageBonusForNodes(getUnlockedFinalDamageNodes(attacker, category), target instanceof EntityPlayer);
    }

    static WeaponCategory getFinalDamageCategory(ItemStack weapon) {
        return isShieldWeapon(weapon) ? WeaponCategory.SHIELD : getWeaponCategory(weapon);
    }

    public static CombatAdjustment preparePlayerHit(EntityPlayer attacker, EntityPlayer target, DamageSource source, float amount, boolean blocked) {
        CombatAdjustment adjustment = new CombatAdjustment(amount);
        if (attacker == null || target == null || source == null || applyingPureDamage) {
            return adjustment;
        }
        WeaponCategory category = getDamageCategory(attacker, source);
        adjustment.amount += getFinalDamageBonus(attacker, target, getFinalDamageCategory(attacker, source));
        applyPreDefensePassive(attacker, source, category, blocked, adjustment);
        return adjustment;
    }

    public static CombatAdjustment prepareNonPlayerHit(EntityPlayer attacker, EntityLivingBase target, DamageSource source, float amount) {
        CombatAdjustment adjustment = new CombatAdjustment(amount);
        if (attacker == null || target == null || source == null || applyingPureDamage) {
            return adjustment;
        }
        WeaponCategory category = getDamageCategory(attacker, source);
        adjustment.amount += getFinalDamageBonus(attacker, target, getFinalDamageCategory(attacker, source));
        applyPreDefensePassive(attacker, source, category, false, adjustment);
        return adjustment;
    }

    public static void afterResolvedHit(EntityPlayer attacker, EntityLivingBase target, DamageSource source, boolean blockedHitBypassed) {
        if (attacker == null || target == null || source == null || applyingPureDamage || source.isProjectile()) {
            return;
        }
        WeaponCategory category = getWeaponCategory(attacker.getHeldItem());
        if (category == null) {
            return;
        }
        if (category == WeaponCategory.GLAIVE && hasPassive(attacker, WeaponCategory.GLAIVE)) {
            applyGlaiveRedirection(attacker, target);
            return;
        }
        if (category == WeaponCategory.SWORD && hasPassive(attacker, WeaponCategory.SWORD) && target instanceof EntityPlayer) {
            if (incrementCombo(attacker, "sword_fencer", passiveCounterThreshold(category), category, false)) {
                switchHotbarSlot((EntityPlayer) target);
            }
            return;
        }
        if (category == WeaponCategory.CLAYMORE && hasPassive(attacker, WeaponCategory.CLAYMORE)) {
            if (incrementCombo(attacker, "claymore_guard", passiveCounterThreshold(category), category, false)) {
                target.addPotionEffect(new PotionEffect(GOTEffects.inversion.id, 20, 0));
                applyPureDamage(attacker, target, 2.0F);
            }
            return;
        }
        if (category == WeaponCategory.TWO_HANDED_SWORD && hasPassive(attacker, WeaponCategory.TWO_HANDED_SWORD)) {
            if (!blockedHitBypassed && incrementCombo(attacker, "two_handed_cleaver", passiveCounterThreshold(category), category, true)) {
                CLEAVER_READY.add(attacker.getUniqueID());
            }
            return;
        }
        if (category == WeaponCategory.DAGGER && hasPassive(attacker, WeaponCategory.DAGGER)) {
            applyDaggerBackstab(attacker, target);
            return;
        }
        if (category == WeaponCategory.AXE && hasPassive(attacker, WeaponCategory.AXE)) {
            applyAxeBleeding(attacker, target);
            return;
        }
        if (category == WeaponCategory.HAMMER && hasPassive(attacker, WeaponCategory.HAMMER)) {
            applyHammerStun(target, isTwoHandedHammer(attacker.getHeldItem()));
        }
    }

    public static void afterProjectileHit(EntityPlayer attacker, EntityLivingBase target, DamageSource source) {
        if (attacker == null || target == null || source == null || !source.isProjectile() || applyingPureDamage) {
            return;
        }
        WeaponCategory category = getDamageCategory(attacker, source);
        if (category == WeaponCategory.BOW && TOGTechnologyLocks.has(attacker, TOGTechnologyLocks.BOW_PASSIVE)) {
            applyBowSlowCombo(attacker, target);
        }
    }

    public static void recordSuccessfulBlock(EntityPlayer player) {
        if (player == null || player.worldObj == null || player.worldObj.isRemote || applyingPureDamage) {
            return;
        }
        if (getWeaponCategory(player.getHeldItem()) != WeaponCategory.GLAIVE || !hasPassive(player, WeaponCategory.GLAIVE)) {
            return;
        }
        GLAIVE_REDIRECTION_READY_UNTIL.put(player.getUniqueID(), player.worldObj.getTotalWorldTime() + GLAIVE_REDIRECTION_WINDOW_TICKS);
    }

    public static void updateShieldWall(EntityPlayer player) {
        if (player == null || player.worldObj == null || player.worldObj.isRemote) {
            return;
        }
        UUID playerId = player.getUniqueID();
        if (!player.isSneaking()) {
            SHIELD_WALL_SHIFT_STARTED.remove(playerId);
            SHIELD_WALL_READY.remove(playerId);
            return;
        }
        if (!hasPassive(player, WeaponCategory.SHIELD)) {
            SHIELD_WALL_SHIFT_STARTED.remove(playerId);
            SHIELD_WALL_READY.remove(playerId);
            return;
        }
        Long started = SHIELD_WALL_SHIFT_STARTED.get(playerId);
        long now = player.worldObj.getTotalWorldTime();
        if (started == null) {
            SHIELD_WALL_SHIFT_STARTED.put(playerId, now);
            return;
        }
        if (now - started.longValue() >= 20L) {
            SHIELD_WALL_READY.add(playerId);
        }
    }

    public static void applyShieldWallIfReady(EntityPlayer attacker, EntityPlayer target, boolean targetWasBlocking) {
        if (attacker == null || target == null || !targetWasBlocking || applyingPureDamage) {
            return;
        }
        ItemStack weapon = attacker.getHeldItem();
        if (!isShieldWeapon(weapon) || !SHIELD_WALL_READY.contains(attacker.getUniqueID())) {
            return;
        }
        applyPureDamage(attacker, target, isShieldPike(weapon) ? 1.0F : 0.5F);
        applyExtraKnockback(attacker, target);
    }

    public static void suppressKnockback(EntityLivingBase target) {
        if (target != null) {
            SUPPRESSED_KNOCKBACK_TICKS.put(target.getUniqueID(), 2);
        }
    }

    public static void updateSuppressedKnockback(EntityLivingBase target) {
        if (target == null || target.worldObj == null || target.worldObj.isRemote) {
            return;
        }
        UUID targetId = target.getUniqueID();
        Integer ticks = SUPPRESSED_KNOCKBACK_TICKS.get(targetId);
        if (ticks == null) {
            return;
        }
        target.motionX = 0.0D;
        target.motionZ = 0.0D;
        target.velocityChanged = true;
        if (ticks.intValue() <= 1) {
            SUPPRESSED_KNOCKBACK_TICKS.remove(targetId);
        } else {
            SUPPRESSED_KNOCKBACK_TICKS.put(targetId, ticks.intValue() - 1);
        }
    }

    public static boolean isStunned(EntityPlayer player) {
        return player != null && player.isPotionActive(GOTEffects.staminaLock.id);
    }

    public static void markProjectileWeapon(Entity projectile, ItemStack weapon) {
        WeaponCategory category = getWeaponCategory(weapon);
        if (projectile != null && (category == WeaponCategory.BOW || category == WeaponCategory.CROSSBOW)) {
            projectile.getEntityData().setString(PROJECTILE_CATEGORY_KEY, category.getPrefix());
        }
    }

    public static WeaponCategory getProjectileWeaponCategory(Entity projectile) {
        if (projectile == null || !projectile.getEntityData().hasKey(PROJECTILE_CATEGORY_KEY)) {
            return null;
        }
        return fromPrefix(projectile.getEntityData().getString(PROJECTILE_CATEGORY_KEY));
    }

    public static void markCrossbowShot(ItemStack crossbow, EntityPlayer player) {
        if (!isOrdinaryCrossbow(crossbow) || player == null || player.worldObj == null || player.worldObj.isRemote) {
            return;
        }
        NBTTagCompound nbt = getOrCreateTag(crossbow);
        nbt.setLong(CROSSBOW_LAST_SHOT_TIME_KEY, player.worldObj.getTotalWorldTime());
    }

    public static void recordCrossbowReload(ItemStack crossbow, EntityPlayer player) {
        if (!isOrdinaryCrossbow(crossbow) || player == null || player.worldObj == null || player.worldObj.isRemote) {
            return;
        }
        NBTTagCompound nbt = getOrCreateTag(crossbow);
        if (!TOGTechnologyLocks.has(player, TOGTechnologyLocks.CROSSBOW_PASSIVE)) {
            nbt.setInteger(CROSSBOW_RELOAD_COMBO_KEY, 0);
            nbt.removeTag(CROSSBOW_TRIPLE_LOADED_KEY);
            clearPassiveCounter(player);
            return;
        }
        long lastShotTime = nbt.getLong(CROSSBOW_LAST_SHOT_TIME_KEY);
        long now = player.worldObj.getTotalWorldTime();
        if (lastShotTime <= 0L || now - lastShotTime > CROSSBOW_RELOAD_WINDOW_TICKS) {
            nbt.setInteger(CROSSBOW_RELOAD_COMBO_KEY, 0);
            nbt.removeTag(CROSSBOW_TRIPLE_LOADED_KEY);
            clearPassiveCounter(player);
            return;
        }
        int reloads = nbt.getInteger(CROSSBOW_RELOAD_COMBO_KEY) + 1;
        if (reloads >= CROSSBOW_TRIPLE_RELOAD_THRESHOLD) {
            nbt.setInteger(CROSSBOW_RELOAD_COMBO_KEY, 0);
            nbt.setBoolean(CROSSBOW_TRIPLE_LOADED_KEY, true);
            syncPassiveCounter(player, WeaponCategory.CROSSBOW, CROSSBOW_TRIPLE_RELOAD_THRESHOLD, CROSSBOW_TRIPLE_RELOAD_THRESHOLD, true);
        } else {
            nbt.setInteger(CROSSBOW_RELOAD_COMBO_KEY, reloads);
            nbt.removeTag(CROSSBOW_TRIPLE_LOADED_KEY);
            syncPassiveCounter(player, WeaponCategory.CROSSBOW, reloads, CROSSBOW_TRIPLE_RELOAD_THRESHOLD, false);
        }
    }

    public static boolean isCrossbowTripleShotLoaded(ItemStack crossbow) {
        return isOrdinaryCrossbow(crossbow) && crossbow.hasTagCompound() && crossbow.getTagCompound().getBoolean(CROSSBOW_TRIPLE_LOADED_KEY);
    }

    public static void clearCrossbowTripleShot(ItemStack crossbow) {
        clearCrossbowTripleShot(crossbow, null);
    }

    public static void clearCrossbowTripleShot(ItemStack crossbow, EntityPlayer player) {
        boolean wasTripleShotLoaded = isCrossbowTripleShotLoaded(crossbow);
        if (crossbow != null && crossbow.hasTagCompound()) {
            crossbow.getTagCompound().removeTag(CROSSBOW_TRIPLE_LOADED_KEY);
        }
        if (wasTripleShotLoaded && player != null) {
            syncPassiveCounter(player, WeaponCategory.CROSSBOW, CROSSBOW_TRIPLE_RELOAD_THRESHOLD, CROSSBOW_TRIPLE_RELOAD_THRESHOLD, false);
        }
    }

    public static WeaponCategory getWeaponCategory(ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return null;
        }
        Item item = stack.getItem();
        if (item instanceof GOTItemCrossbow) {
            return WeaponCategory.CROSSBOW;
        }
        if (item instanceof ItemBow || item == Items.bow) {
            return WeaponCategory.BOW;
        }
        if (isShieldSpear(stack)) {
            return WeaponCategory.SPEAR;
        }
        if (isShieldPike(stack)) {
            return WeaponCategory.PIKE;
        }
        String className = item.getClass().getSimpleName().toLowerCase();
        if (item instanceof GOTItemArrynClaymore || className.contains("claymore")) {
            return WeaponCategory.CLAYMORE;
        }
        if (item instanceof GOTItemLongsword || item instanceof GOTItemGreatsword) {
            return WeaponCategory.TWO_HANDED_SWORD;
        }
        if (isAxeWeapon(item, className)) {
            return WeaponCategory.AXE;
        }
        if (item instanceof GOTItemHammer || className.contains("hammer")) {
            return WeaponCategory.HAMMER;
        }
        if (item instanceof GOTItemSpear || className.contains("spear") || className.contains("trident")) {
            return WeaponCategory.SPEAR;
        }
        if (item instanceof GOTItemPike || item instanceof GOTItemReachPike || item instanceof GOTItemPolearmLong || className.contains("pike")) {
            return WeaponCategory.PIKE;
        }
        if (item instanceof GOTItemPolearm) {
            return WeaponCategory.GLAIVE;
        }
        if (item instanceof GOTItemDagger || className.contains("dagger")) {
            return WeaponCategory.DAGGER;
        }
        if (item instanceof GOTItemSword) {
            return WeaponCategory.SWORD;
        }
        return null;
    }

    private static boolean isAxeWeapon(Item item, String className) {
        return item instanceof GOTItemBattleaxe
                || item instanceof GOTItemPoleaxe
                || item instanceof GOTItemThrowingAxe
                || className.contains("battleaxe")
                || className.contains("poleaxe")
                || className.contains("throwingaxe")
                || className.contains("ironbornaxe")
                || className.contains("celtigaraxe")
                || className.contains("areohotahaxe")
                || className.contains("victarionaxe")
                || className.contains("tyrionaxe");
    }

    private static void applyPreDefensePassive(EntityPlayer attacker, DamageSource source, WeaponCategory category, boolean blocked, CombatAdjustment adjustment) {
        if (source.isProjectile() || category == null) {
            return;
        }
        ItemStack weapon = attacker.getHeldItem();
        if (category == WeaponCategory.TWO_HANDED_SWORD && hasPassive(attacker, WeaponCategory.TWO_HANDED_SWORD) && CLEAVER_READY.remove(attacker.getUniqueID())) {
            adjustment.ignoreBlock = true;
            adjustment.skipBlockVulnerability = true;
            syncPassiveCounter(attacker, category, passiveCounterThreshold(category), passiveCounterThreshold(category), false);
            return;
        }
        if (category == WeaponCategory.SPEAR && hasPassive(attacker, WeaponCategory.SPEAR) && !isShieldWeapon(weapon)) {
            if (incrementCombo(attacker, "spear_piercing", passiveCounterThreshold(category), category, false)) {
                adjustment.amount *= 0.7F;
                adjustment.ignoreBlock = true;
                adjustment.skipBlockVulnerability = true;
            }
            return;
        }
        if (category == WeaponCategory.PIKE && hasPassive(attacker, WeaponCategory.PIKE) && !isShieldWeapon(weapon)) {
            adjustment.amount *= 0.7F;
            adjustment.ignoreBlock = true;
            adjustment.skipBlockVulnerability = true;
            adjustment.suppressKnockback = true;
        }
    }

    private static WeaponCategory getDamageCategory(EntityPlayer attacker, DamageSource source) {
        WeaponCategory projectileCategory = getProjectileWeaponCategory(source.getSourceOfDamage());
        if (projectileCategory != null) {
            return projectileCategory;
        }
        return attacker != null ? getWeaponCategory(attacker.getHeldItem()) : null;
    }

    private static WeaponCategory getFinalDamageCategory(EntityPlayer attacker, DamageSource source) {
        WeaponCategory projectileCategory = getProjectileWeaponCategory(source.getSourceOfDamage());
        if (projectileCategory != null) {
            return projectileCategory;
        }
        return attacker != null ? getFinalDamageCategory(attacker.getHeldItem()) : null;
    }

    private static boolean rollChance(EntityPlayer player, double chance) {
        return player != null && player.worldObj != null && chance > 0.0D && player.worldObj.rand.nextDouble() < chance;
    }

    private static void applyDaggerBackstab(EntityPlayer attacker, EntityLivingBase target) {
        if (isBackstab(attacker, target) && incrementCombo(attacker, "dagger_backstab", passiveCounterThreshold(WeaponCategory.DAGGER), WeaponCategory.DAGGER, false)) {
            applyPureDamage(attacker, target, 2.0F);
        }
    }

    static boolean isBackstab(Entity attacker, EntityLivingBase target) {
        return attacker != null && target != null && isBackstabPosition(attacker.posX, attacker.posZ, target.posX, target.posZ, target.rotationYaw);
    }

    static boolean isBackstabPosition(double attackerX, double attackerZ, double targetX, double targetZ, float targetYaw) {
        double dx = attackerX - targetX;
        double dz = attackerZ - targetZ;
        double len = Math.sqrt(dx * dx + dz * dz);
        if (len < 0.0001D) {
            return false;
        }
        dx /= len;
        dz /= len;
        double yaw = Math.toRadians(targetYaw);
        double lookX = -Math.sin(yaw);
        double lookZ = Math.cos(yaw);
        double dot = dx * lookX + dz * lookZ;
        return dot <= -0.5D;
    }

    private static void applyGlaiveRedirection(EntityPlayer attacker, EntityLivingBase target) {
        if (attacker.worldObj == null) {
            return;
        }
        UUID attackerId = attacker.getUniqueID();
        Long readyUntil = GLAIVE_REDIRECTION_READY_UNTIL.get(attackerId);
        if (readyUntil == null) {
            return;
        }
        if (attacker.worldObj.getTotalWorldTime() > readyUntil.longValue()) {
            GLAIVE_REDIRECTION_READY_UNTIL.remove(attackerId);
            return;
        }
        GLAIVE_REDIRECTION_READY_UNTIL.remove(attackerId);
        applyPureDamage(attacker, target, 2.0F);
    }

    private static void applyBowSlowCombo(EntityPlayer attacker, EntityLivingBase target) {
        UUID attackerId = attacker.getUniqueID();
        UUID targetId = target.getUniqueID();
        UUID lastTargetId = BOW_SLOW_LAST_TARGETS.get(attackerId);
        int count = targetId.equals(lastTargetId) && BOW_SLOW_HIT_COUNTS.containsKey(attackerId) ? BOW_SLOW_HIT_COUNTS.get(attackerId).intValue() : 0;
        count++;
        BOW_SLOW_LAST_TARGETS.put(attackerId, targetId);
        if (count >= BOW_SLOW_HIT_THRESHOLD) {
            BOW_SLOW_HIT_COUNTS.put(attackerId, 0);
            syncPassiveCounter(attacker, WeaponCategory.BOW, BOW_SLOW_HIT_THRESHOLD, BOW_SLOW_HIT_THRESHOLD, false);
            target.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, BOW_SLOW_DURATION_TICKS, BOW_SLOW_AMPLIFIER));
        } else {
            BOW_SLOW_HIT_COUNTS.put(attackerId, count);
            syncPassiveCounter(attacker, WeaponCategory.BOW, count, BOW_SLOW_HIT_THRESHOLD, false);
        }
    }

    private static boolean hasPassive(EntityPlayer player, WeaponCategory category) {
        return TOGTechnologyLocks.has(player, passiveTechnologyId(category));
    }

    private static boolean isOrdinaryCrossbow(ItemStack stack) {
        return stack != null && stack.getItem() instanceof GOTItemCrossbow && !(stack.getItem() instanceof GOTItemSiegeCrossbow);
    }

    private static NBTTagCompound getOrCreateTag(ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) {
            nbt = new NBTTagCompound();
            stack.setTagCompound(nbt);
        }
        return nbt;
    }

    private static boolean incrementCombo(EntityPlayer player, String key, int threshold, WeaponCategory category, boolean readyWhenTriggered) {
        if (threshold <= 1) {
            return false;
        }
        UUID playerId = player.getUniqueID();
        Map<String, Integer> counters = COMBO_COUNTS.get(playerId);
        if (counters == null) {
            counters = new HashMap<String, Integer>();
            COMBO_COUNTS.put(playerId, counters);
        }
        int count = counters.containsKey(key) ? counters.get(key).intValue() : 0;
        count++;
        if (count >= threshold) {
            counters.put(key, 0);
            syncPassiveCounter(player, category, threshold, threshold, readyWhenTriggered);
            return true;
        }
        counters.put(key, count);
        syncPassiveCounter(player, category, count, threshold, false);
        return false;
    }

    private static void syncPassiveCounter(EntityPlayer player, WeaponCategory category, int count, int threshold, boolean ready) {
        if (!(player instanceof EntityPlayerMP) || threshold <= 1 || TOGPacketHandler.networkWrapper == null) {
            return;
        }
        String label = passiveCounterLabel(category);
        if (label.length() == 0) {
            return;
        }
        TOGPacketHandler.networkWrapper.sendTo(new TOGPacketPassiveCounter(label, count, threshold, ready), (EntityPlayerMP) player);
    }

    private static void clearPassiveCounter(EntityPlayer player) {
        if (player instanceof EntityPlayerMP && TOGPacketHandler.networkWrapper != null) {
            TOGPacketHandler.networkWrapper.sendTo(new TOGPacketPassiveCounter("", 0, 0, false), (EntityPlayerMP) player);
        }
    }

    private static void switchHotbarSlot(EntityPlayer target) {
        int current = target.inventory.currentItem;
        int next = (current + 1) % 9;
        target.inventory.currentItem = next;
        target.inventory.markDirty();
        target.inventoryContainer.detectAndSendChanges();
        if (target instanceof EntityPlayerMP) {
            ((EntityPlayerMP) target).playerNetServerHandler.sendPacket(new S09PacketHeldItemChange(next));
        }
    }

    private static void applyAxeBleeding(EntityPlayer attacker, EntityLivingBase target) {
        boolean twoHanded = isTwoHandedAxe(attacker.getHeldItem());
        PotionEffect active = target.getActivePotionEffect(GOTEffects.bleeding);
        if (active != null) {
            int extension = twoHanded ? 20 : 10;
            target.addPotionEffect(new PotionEffect(GOTEffects.bleeding.id, active.getDuration() + extension, active.getAmplifier()));
            return;
        }
        if (incrementCombo(attacker, "axe_bloodthirst", passiveCounterThreshold(WeaponCategory.AXE), WeaponCategory.AXE, false)) {
            target.addPotionEffect(new PotionEffect(GOTEffects.bleeding.id, twoHanded ? 60 : 40, 0));
        }
    }

    private static void applyHammerStun(EntityLivingBase target, boolean twoHanded) {
        int duration = twoHanded ? 20 : 12;
        target.addPotionEffect(new PotionEffect(GOTEffects.staminaLock.id, duration, 0));
        target.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, duration, 127));
        target.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, duration, 127));
        target.addPotionEffect(new PotionEffect(Potion.jump.id, duration, 128));
        if (target instanceof EntityPlayer) {
            ((EntityPlayer) target).setSprinting(false);
            ((EntityPlayer) target).clearItemInUse();
        }
    }

    private static void applyPureDamage(EntityPlayer attacker, EntityLivingBase target, float amount) {
        if (amount <= 0.0F) {
            return;
        }
        applyingPureDamage = true;
        try {
            target.attackEntityFrom(DamageSource.causePlayerDamage(attacker).setDamageBypassesArmor(), amount);
        } finally {
            applyingPureDamage = false;
        }
    }

    private static void applyExtraKnockback(EntityPlayer attacker, EntityLivingBase target) {
        double dx = target.posX - attacker.posX;
        double dz = target.posZ - attacker.posZ;
        double len = Math.sqrt(dx * dx + dz * dz);
        if (len < 0.0001D) {
            dx = -Math.sin(Math.toRadians(attacker.rotationYaw));
            dz = Math.cos(Math.toRadians(attacker.rotationYaw));
            len = Math.sqrt(dx * dx + dz * dz);
        }
        target.addVelocity(dx / len * 0.4D, 0.1D, dz / len * 0.4D);
        target.velocityChanged = true;
    }

    private static boolean isTwoHandedAxe(ItemStack stack) {
        return stack != null && stack.getItem() instanceof GOTItemBattleaxe;
    }

    private static boolean isTwoHandedHammer(ItemStack stack) {
        return stack != null && stack.getItem() instanceof GOTItemHammer && !(stack.getItem().getClass().equals(GOTItemHammer.class));
    }

    private static boolean isShieldWeapon(ItemStack stack) {
        return isShieldSpear(stack) || isShieldPike(stack);
    }

    private static boolean isShieldSpear(ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return false;
        }
        String className = stack.getItem().getClass().getSimpleName().toLowerCase();
        return stack.getItem() instanceof GOTItemShieldSpear || className.contains("shieldspear") || className.contains("shieldriverlandstrident");
    }

    private static boolean isShieldPike(ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return false;
        }
        String className = stack.getItem().getClass().getSimpleName().toLowerCase();
        return stack.getItem() instanceof GOTItemShieldPike || className.contains("shieldpike") || className.contains("shieldreachpike") || className.contains("shieldhalbert");
    }

    private static WeaponCategory fromPrefix(String prefix) {
        for (WeaponCategory category : WeaponCategory.values()) {
            if (category.getPrefix().equals(prefix)) {
                return category;
            }
        }
        return null;
    }

    private static String romanLevel(int level) {
        if (level == 1) {
            return "i";
        }
        if (level == 2) {
            return "ii";
        }
        return "iii";
    }
}
