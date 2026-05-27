package com.yearup.soulfoodpos.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final List<OrderItem> items = new ArrayList<>();

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void add(OrderItem item) {
        items.add(item);
    }

    public void remove(OrderItem item) {
        items.remove(item);
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getTotal() {
        return items.stream().mapToDouble(OrderItem::getPrice).sum();
    }

    public boolean hasItems() {
        return !items.isEmpty();
    }

    public boolean hasPlates() {
        return items.stream().anyMatch(i -> i instanceof Item);
    }

    public boolean hasDrinkOrMainSide() {
        return items.stream().anyMatch(i -> i instanceof Drink || i instanceof MainSide);
    }

    /** Spec rule: zero plates is allowed ONLY if there is at least one drink or main side. */
    public boolean isValidForCheckout() {
        if (!hasItems()) return false;
        return hasPlates() || hasDrinkOrMainSide();
    }
}
