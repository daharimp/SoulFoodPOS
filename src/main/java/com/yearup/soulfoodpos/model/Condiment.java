package com.yearup.soulfoodpos.model;

import com.yearup.soulfoodpos.model.enums.CondimentOption;
import com.yearup.soulfoodpos.model.enums.Size;

public class Condiment implements Topping {
    private final CondimentOption option;

    public Condiment(CondimentOption option) {
        this.option = option;
    }

    public CondimentOption getOption() {
        return option;
    }

    @Override
    public String getName() {
        return option.getLabel();
    }

    @Override
    public double priceFor(Size size) {
        return 0.0;
    }
}
