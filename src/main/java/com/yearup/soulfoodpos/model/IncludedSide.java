package com.yearup.soulfoodpos.model;

import com.yearup.soulfoodpos.model.enums.IncludedSideOption;
import com.yearup.soulfoodpos.model.enums.Size;

public class IncludedSide implements Topping {
    private final IncludedSideOption option;

    public IncludedSide(IncludedSideOption option) {
        this.option = option;
    }

    public IncludedSideOption getOption() {
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
