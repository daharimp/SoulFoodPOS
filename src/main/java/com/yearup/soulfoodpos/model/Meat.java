package com.yearup.soulfoodpos.model;

import com.yearup.soulfoodpos.model.enums.MeatOption;
import com.yearup.soulfoodpos.model.enums.Size;

public class Meat implements Topping {
    private final MeatOption option;
    private final boolean extra;

    public Meat(MeatOption option, boolean extra) {
        this.option = option;
        this.extra = extra;
    }

    public MeatOption getOption() {
        return option;
    }

    @Override
    public String getName() {
        return option.getLabel();
    }

    @Override
    public boolean isExtra() {
        return extra;
    }

    // Capstone price sheet: $1.00 (Sm) / $2.00 (Md) / $3.00 (Lg) — an extra portion adds $0.50 / $1.00 / $1.50
    @Override
    public double priceFor(Size size) {
        double base = switch (size) {
            case SMALL -> 1.00;
            case MEDIUM -> 2.00;
            case LARGE -> 3.00;
        };
        double extraAdd = extra ? switch (size) {
            case SMALL -> 0.50;
            case MEDIUM -> 1.00;
            case LARGE -> 1.50;
        } : 0.0;
        return base + extraAdd;
    }
}
