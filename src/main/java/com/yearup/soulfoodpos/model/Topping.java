package com.yearup.soulfoodpos.model;

import com.yearup.soulfoodpos.model.enums.Size;

public interface Topping {
    String getName();
    double priceFor(Size size);
    default boolean isExtra() { return false; }
}
