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

    // Session-random price ($6.50–$9.50) assigned per enum constant; extra adds 40%
    @Override
    public double priceFor(Size size) {
        double base = option.getPrice();
        double extraAdd = extra ? Math.round(base * 0.40 * 100.0) / 100.0 : 0.0;
        return base + extraAdd;
    }
}
