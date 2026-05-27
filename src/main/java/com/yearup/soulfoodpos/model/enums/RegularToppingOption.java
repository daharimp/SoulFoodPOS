package com.yearup.soulfoodpos.model;

import com.yearup.soulfoodpos.model.enums.RegularToppingOption;
import com.yearup.soulfoodpos.model.enums.Size;

public class RegularTopping implements Topping {
    private final RegularToppingOption option;

    public RegularTopping(RegularToppingOption option) {
        this.option = option;
    }

    public RegularToppingOption getOption() {
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
