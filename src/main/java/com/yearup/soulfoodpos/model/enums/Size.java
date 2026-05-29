package com.yearup.soulfoodpos.model.enums;

public enum Size {
    SMALL("Small (2 lb)"),
    MEDIUM("Medium (4 lb)"),
    LARGE("Large (8 lb)");

    private final String label;

    Size(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}

