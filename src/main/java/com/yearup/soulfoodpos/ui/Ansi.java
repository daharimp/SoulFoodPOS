package com.yearup.soulfoodpos.ui;

public final class Ansi {

    public static boolean enabled = true;

    public static final String RESET  = "[0m";
    public static final String BOLD   = "[1m";
    public static final String DIM    = "[2m";
    public static final String YELLOW = "[33m";
    public static final String GREEN  = "[32m";
    public static final String RED    = "[31m";

    private Ansi() {}

    public static String paint(String code, String text) {
        return enabled ? code + text + RESET : text;
    }
}
