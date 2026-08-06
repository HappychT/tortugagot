package com.tortugagot.togcore.technology;

public final class TOGTechnologyRules {
    public static final double STAMINA_CHOICE_FACTOR = 1.5D;
    public static final String FIRST_ATTACK_REDUCTION_SUFFIX = "_stamina_choice_i";
    public static final String FIRST_BLOCK_EXHAUSTION_SUFFIX = "_stamina_block_drain_i";
    public static final String SECOND_BLOCK_REDUCTION_SUFFIX = "_stamina_choice_ii";
    public static final String SECOND_ATTACK_REDUCTION_SUFFIX = "_stamina_attack_choice_ii";

    private static final String FINAL_DAMAGE_II_SUFFIX = "_final_damage_ii";
    private static final String FINAL_DAMAGE_III_SUFFIX = "_final_damage_iii";

    private TOGTechnologyRules() {
    }

    public static String firstAttackReductionId(String prefix) {
        return prefix + FIRST_ATTACK_REDUCTION_SUFFIX;
    }

    public static String firstBlockExhaustionId(String prefix) {
        return prefix + FIRST_BLOCK_EXHAUSTION_SUFFIX;
    }

    public static String secondBlockReductionId(String prefix) {
        return prefix + SECOND_BLOCK_REDUCTION_SUFFIX;
    }

    public static String secondAttackReductionId(String prefix) {
        return prefix + SECOND_ATTACK_REDUCTION_SUFFIX;
    }

    public static boolean isWarriorStaminaChoice(String technologyId) {
        return getExclusiveStaminaChoiceId(technologyId) != null;
    }

    public static String getExclusiveStaminaChoiceId(String technologyId) {
        String prefix = prefixBeforeSuffix(technologyId, FIRST_ATTACK_REDUCTION_SUFFIX);
        if (prefix != null) {
            return firstBlockExhaustionId(prefix);
        }
        prefix = prefixBeforeSuffix(technologyId, FIRST_BLOCK_EXHAUSTION_SUFFIX);
        if (prefix != null) {
            return firstAttackReductionId(prefix);
        }
        prefix = prefixBeforeSuffix(technologyId, SECOND_BLOCK_REDUCTION_SUFFIX);
        if (prefix != null) {
            return secondAttackReductionId(prefix);
        }
        prefix = prefixBeforeSuffix(technologyId, SECOND_ATTACK_REDUCTION_SUFFIX);
        if (prefix != null) {
            return secondBlockReductionId(prefix);
        }
        return null;
    }

    public static String[] getRequiredStaminaChoiceGroup(String technologyId) {
        String prefix = prefixBeforeSuffix(technologyId, FINAL_DAMAGE_II_SUFFIX);
        if (prefix != null) {
            return new String[]{firstAttackReductionId(prefix), firstBlockExhaustionId(prefix)};
        }
        prefix = prefixBeforeSuffix(technologyId, FINAL_DAMAGE_III_SUFFIX);
        if (prefix != null) {
            return new String[]{secondBlockReductionId(prefix), secondAttackReductionId(prefix)};
        }
        return null;
    }

    public static String getRequiredStaminaChoiceName(String technologyId) {
        if (prefixBeforeSuffix(technologyId, FINAL_DAMAGE_II_SUFFIX) != null) {
            return "первого выбора стамины";
        }
        if (prefixBeforeSuffix(technologyId, FINAL_DAMAGE_III_SUFFIX) != null) {
            return "второго выбора стамины";
        }
        return "выбора стамины";
    }

    public static boolean isRequiredStaminaChoiceFor(String prerequisiteId, String technologyId) {
        String[] group = getRequiredStaminaChoiceGroup(technologyId);
        return group != null && (group[0].equals(prerequisiteId) || group[1].equals(prerequisiteId));
    }

    private static String prefixBeforeSuffix(String value, String suffix) {
        if (value == null || !value.endsWith(suffix)) {
            return null;
        }
        String prefix = value.substring(0, value.length() - suffix.length());
        return prefix.length() > 0 ? prefix : null;
    }
}
