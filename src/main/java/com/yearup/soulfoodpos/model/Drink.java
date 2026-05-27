package com.yearup.soulfoodpos.model;

import com.yearup.soulfoodpos.model.enums.DrinkFlavor;
import com.yearup.soulfoodpos.model.enums.Size;

public class Drink implements OrderItem {
    private final Size size;
    private final DrinkFlavor flavor;

    public Drink(Size size, DrinkFlavor flavor) {
        this.size = size;
        this.flavor = flavor;
    }

    public Size getSize() { return size; }
    public DrinkFlavor getFlavor() { return flavor; }

    // Per spec: Small $2.00 / Medium $2.50 / Large $3.00
    @Override
    public double getPrice() {
        return switch (size) {
            case SMALL -> 2.00;
            case MEDIUM -> 2.50;
            case LARGE -> 3.00;
        };
    }

    @Override
    public String getDescription() {
        return String.format("%s %s — $%.2f", size.getLabel(), flavor.getLabel(), getPrice());
    }
}
