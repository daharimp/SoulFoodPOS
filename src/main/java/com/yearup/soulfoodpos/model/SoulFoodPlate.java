package com.yearup.soulfoodpos.model;

import com.yearup.soulfoodpos.model.enums.PlateType;
import com.yearup.soulfoodpos.model.enums.Size;

public class SoulFoodPlate extends Item {

    public SoulFoodPlate(Size size, PlateType type) {
        super(size, type);
    }

    private double basePlatePrice() {
        // Capstone price sheet: every rice base is the same price, set by size
        return switch (size) {
            case SMALL  -> 3.50;
            case MEDIUM -> 9.00;
            case LARGE  -> 8.50;
        };
    }

    @Override
    public double getPrice() {
        double total = basePlatePrice();
        for (Topping t : toppings) {
            total += t.priceFor(size);
        }
        return total;
    }

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(size.getLabel()).append(" ").append(type.getLabel());
        if (special) sb.append(" [Make it Hot]");
        sb.append("\n");
        for (Topping t : toppings) {
            sb.append("    - ").append(t.getName());
            if (t.isExtra()) sb.append(" (extra)");
            double p = t.priceFor(size);
            if (p > 0) sb.append(String.format("  +$%.2f", p));
            sb.append("\n");
        }
        return sb.toString().stripTrailing();
    }
}
