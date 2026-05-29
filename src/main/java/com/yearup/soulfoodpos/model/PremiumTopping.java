package com.yearup.soulfoodpos.model;

import com.yearup.soulfoodpos.model.enums.PremiumToppingOption;
import com.yearup.soulfoodpos.model.enums.Size; // kept: required by Topping interface signature

public class PremiumTopping implements Topping {
    private final PremiumToppingOption option;
    private final boolean extra;

    public PremiumTopping(PremiumToppingOption option, boolean extra) {
        this.option = option;
        this.extra = extra;
    }

    public PremiumToppingOption getOption() {
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

    // Capstone price sheet: $0.75 (Sm) / $1.50 (Md) / $2.25 (Lg);
    // an extra portion adds the "extra cheese" surcharge $0.30 / $0.60 / $0.90
    @Override
    public double priceFor(Size size) {
        double base = switch (size) {
            case SMALL -> 0.75;
            case MEDIUM -> 1.50;
            case LARGE -> 2.25;
        };
        double extraAdd = extra ? switch (size) {
            case SMALL -> 0.30;
            case MEDIUM -> 0.60;
            case LARGE -> 0.90;
        } : 0.0;
        return base + extraAdd;
    }
}
