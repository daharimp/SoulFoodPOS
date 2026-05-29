package com.yearup.soulfoodpos.model.enums;

public enum Size {
    SMALL("Small", 1),
    MEDIUM("Medium", 2),
    LARGE("Large", 3);

    private final String label;
    // bigger bowl holds more proteins — this drives the meat auto-advance in addToppings
    private final int meatSlots;

    Size(String label, int meatSlots) {
        this.label = label;
        this.meatSlots = meatSlots;
    }

    public String getLabel() {
        return label;
    }

    public int getMeatSlots() {
        return meatSlots;
    }

    @Override
    public String toString() {
        return label;
    }
}
