package com.yearup.soulfoodpos.model.enums;

public enum RegularToppingOption {
    CANDIED_YAMS("Candied Yams"),
    CABBAGE("Cabbage"),
    BLACK_EYED_PEAS("Black-Eyed Peas"),
    POTATO_SALAD("Potato Salad"),
    COLESLAW("Coleslaw"),
    CORNBREAD("Cornbread"),
    PICKLED_OKRA("Pickled Okra"),
    ONIONS("Onions"),
    PICKLES("Pickles");

    private final String label;

    RegularToppingOption(String label) {
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