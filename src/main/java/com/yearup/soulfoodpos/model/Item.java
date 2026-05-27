package com.yearup.soulfoodpos.model;

import com.yearup.soulfoodpos.model.enums.PlateType;
import com.yearup.soulfoodpos.model.enums.Size;

import java.util.ArrayList;
import java.util.List;

public abstract class Item implements OrderItem {
    protected final Size size;
    protected final PlateType type;
    protected final List<Topping> toppings = new ArrayList<>();
    protected boolean special;

    protected Item(Size size, PlateType type) {
        this.size = size;
        this.type = type;
    }

    public Size getSize() { return size; }
    public PlateType getType() { return type; }
    public List<Topping> getToppings() { return toppings; }
    public boolean isSpecial() { return special; }
    public void setSpecial(boolean special) { this.special = special; }

    public void addTopping(Topping t) {
        toppings.add(t);
    }
}
