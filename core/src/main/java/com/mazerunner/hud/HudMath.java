package com.mazerunner.hud;

import com.mazerunner.map.GridPosition;

public final class HudMath {

    private HudMath() {
    }

    public static String formatTime(float seconds) {
        int total = (int) Math.max(0, seconds);
        int minutes = total / 60;
        int rest = total % 60;
        return String.format("%02d:%02d", minutes, rest);
    }

    public static float angleBetween(GridPosition from, GridPosition to) {
        double radians = Math.atan2(to.y() - from.y(), to.x() - from.x());
        return (float) Math.toDegrees(radians);
    }
}
