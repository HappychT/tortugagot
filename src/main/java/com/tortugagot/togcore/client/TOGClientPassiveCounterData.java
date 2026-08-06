package com.tortugagot.togcore.client;

public final class TOGClientPassiveCounterData {
    private static final int NORMAL_DISPLAY_TICKS = 60;
    private static final int READY_DISPLAY_TICKS = 100;
    private static String label = "";
    private static int count;
    private static int threshold;
    private static int ticksRemaining;
    private static boolean ready;

    private TOGClientPassiveCounterData() {
    }

    public static void setCounter(String newLabel, int newCount, int newThreshold, boolean newReady) {
        if (newThreshold <= 1 || newLabel == null || newLabel.length() == 0) {
            clear();
            return;
        }
        label = newLabel;
        threshold = newThreshold;
        count = Math.max(0, Math.min(newCount, threshold));
        ready = newReady;
        ticksRemaining = ready ? READY_DISPLAY_TICKS : NORMAL_DISPLAY_TICKS;
    }

    public static void tick() {
        if (ticksRemaining > 0) {
            ticksRemaining--;
        }
        if (ticksRemaining <= 0) {
            clear();
        }
    }

    public static boolean isVisible() {
        return ticksRemaining > 0 && threshold > 1 && label.length() > 0;
    }

    public static String getDisplayText() {
        if (!isVisible()) {
            return "";
        }
        if (ready) {
            return label + ": \u0433\u043E\u0442\u043E\u0432\u043E";
        }
        return label + ": " + count + " / " + threshold;
    }

    public static int getColor() {
        return ready || count >= threshold ? 0xFFD34D : 0xFFFFFF;
    }

    public static void clear() {
        label = "";
        count = 0;
        threshold = 0;
        ticksRemaining = 0;
        ready = false;
    }
}
