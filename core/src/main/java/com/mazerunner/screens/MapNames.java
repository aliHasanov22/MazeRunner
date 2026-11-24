package com.mazerunner.screens;

public final class MapNames {

    private MapNames() {
    }

    public static String readable(String fileName) {
        String[] words = fileName.replace('_', '-').split("-");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }
            if (result.length() > 0) {
                result.append(' ');
            }
            result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return result.toString();
    }
}
