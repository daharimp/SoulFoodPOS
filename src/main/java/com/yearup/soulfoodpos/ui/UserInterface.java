package com.yearup.soulfoodpos.ui;

import com.yearup.soulfoodpos.io.ReceiptWriter;
import com.yearup.soulfoodpos.model.*;
import com.yearup.soulfoodpos.model.enums.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class UserInterface {

    private final Scanner scanner = new Scanner(System.in);
    private final String shopName;
    private final ReceiptWriter receiptWriter;
    private Order order;

    public UserInterface(String shopName) {
        this.shopName = shopName;
        this.receiptWriter = new ReceiptWriter(shopName);
    }

    // ---------------- Main loop ----------------

    public void display() {
        while (true) {
            printHeader();
            System.out.println("1) New Order");
            System.out.println("0) Exit");
            String cmd = prompt("> ");
            switch (cmd) {
                case "1" -> orderScreen();
                case "0" -> { System.out.println("Goodbye!"); return; }
                default -> System.out.println("Invalid option. Try again.\n");
            }
        }
    }

    // ---------------- Order screen ----------------

    private void orderScreen() {
        order = new Order();
        while (true) {
            printHeader();
            System.out.println("Current Order (newest first):");
            printItemsNewestFirst();
            System.out.printf("Running total: $%.2f%n%n", order.getTotal());

            System.out.println("1) Add Item");
            System.out.println("2) Add Drink");
            System.out.println("3) Add Main Side");
            System.out.println("4) Checkout");
            System.out.println("0) Cancel Order");
            String cmd = prompt("> ");
            switch (cmd) {
                case "1" -> addItem();
                case "2" -> addDrink();
                case "3" -> addMainSide();
                case "4" -> { if (checkout()) return; }
                case "0" -> { System.out.println("Order cancelled.\n"); return; }
                default -> System.out.println("Invalid option.\n");
            }
        }
    }

    private void printItemsNewestFirst() {
        List<OrderItem> items = new ArrayList<>(order.getItems());
        Collections.reverse(items);
        if (items.isEmpty()) {
            System.out.println("  (empty)\n");
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            OrderItem oi = items.get(i);
            System.out.println("  " + (i + 1) + ") " + firstLine(oi.getDescription())
                    + String.format("  $%.2f", oi.getPrice()));
        }
        System.out.println();
    }

    private String firstLine(String s) {
        int nl = s.indexOf('\n');
        return nl < 0 ? s : s.substring(0, nl);
    }

    // ---------------- Add Item flow ----------------

    private void addItem() {
        PlateType type = chooseEnum("Select plate type", PlateType.values());
        if (type == null) return;
        Size size = chooseSize();
        if (size == null) return;

        SoulFoodPlate plate = new SoulFoodPlate(size, type);

        addToppings(plate, "Meat",            MeatOption.values(),            (opt, extra) -> new Meat(opt, extra),            true,
                opt -> "+$10.00(Sm)/$12.00(Md)/$15.00(Lg)", type.getMeatSlots());
        addToppings(plate, "Premium Topping", PremiumToppingOption.values(),  (opt, extra) -> new PremiumTopping(opt, extra),  true,
                opt -> String.format("+$%.2f", opt.getPrice()), 0);
        addToppings(plate, "Premium Add-Ons", RegularToppingOption.values(),  (opt, extra) -> new RegularTopping(opt),         false, opt -> "(included)", 0);
        addToppings(plate, "Condiment",       CondimentOption.values(),       (opt, extra) -> new Condiment(opt),              false, opt -> "(included)", 0);
        addToppings(plate, "Included Side",   IncludedSideOption.values(),    (opt, extra) -> new IncludedSide(opt),           false, opt -> "(included)", 0);

        String special = prompt("Make it Hot? (y/n): ").trim().toLowerCase();
        plate.setSpecial(special.startsWith("y"));

        order.add(plate);
        System.out.printf("%nAdded plate — subtotal $%.2f%n%n", plate.getPrice());
    }

    @FunctionalInterface
    private interface ToppingFactory<E extends Enum<E>> {
        Topping make(E option, boolean extra);
    }

    private <E extends Enum<E>> void addToppings(SoulFoodPlate plate, String label, E[] options,
                                                 ToppingFactory<E> factory, boolean offerExtra,
                                                 Function<E, String> priceLabel, int maxCount) {
        while (true) {
            long categoryCount = maxCount > 0
                    ? plate.getToppings().stream()
                    .filter(t -> java.util.Arrays.stream(options)
                            .anyMatch(o -> o.toString().equals(t.getName())))
                    .count()
                    : 0;

            if (maxCount > 0 && categoryCount >= maxCount) {
                System.out.println("\n" + maxCount + " " + label.toLowerCase()
                        + "(s) selected — moving on.");
                return;
            }

            System.out.println("\n" + label + " options"
                    + (maxCount > 0 ? " (select " + (maxCount - categoryCount) + " more)" : "") + ":");
            for (int i = 0; i < options.length; i++) {
                E opt = options[i];
                long count = plate.getToppings().stream()
                        .filter(t -> t.getName().equals(opt.toString()))
                        .count();
                String countNote = count > 0 ? " [x" + count + "]" : "";
                System.out.println("  " + (i + 1) + ") " + opt + "  " + priceLabel.apply(opt) + countNote);
            }
            if (maxCount <= 0) {
                System.out.println("  0) Done with " + label.toLowerCase() + "s");
            }
            String input = prompt("> ").trim();
            if (maxCount <= 0 && (input.equals("0") || input.isEmpty())) return;
            try {
                int idx = Integer.parseInt(input) - 1;
                if (idx < 0 || idx >= options.length) {
                    System.out.println("Out of range.");
                    continue;
                }
                E chosen = options[idx];
                long existing = plate.getToppings().stream()
                        .filter(t -> t.getName().equals(chosen.toString()))
                        .count();
                if (existing >= 5) {
                    System.out.println("You can only add " + chosen + " up to 5 times.");
                    continue;
                }
                boolean extra = false;
                if (offerExtra) {
                    extra = prompt("Extra " + label.toLowerCase() + "? (y/n): ")
                            .trim().toLowerCase().startsWith("y");
                }
                plate.addTopping(factory.make(chosen, extra));
            } catch (NumberFormatException e) {
                System.out.println("Enter a number.");
            }
        }
    }

    // ---------------- Add Drink ----------------

    private void addDrink() {
        Size size = chooseSize();
        if (size == null) return;
        DrinkFlavor flavor = chooseEnum("Select drink flavor", DrinkFlavor.values());
        if (flavor == null) return;
        Drink drink = new Drink(size, flavor);
        order.add(drink);
        System.out.printf("%nAdded %s — $%.2f%n%n", drink.getDescription(), drink.getPrice());
    }

    // ---------------- Add Main Side ----------------

    private void addMainSide() {
        MainSideOption option = chooseEnum("Select main side", MainSideOption.values());
        if (option == null) return;
        MainSide side = new MainSide(option);
        order.add(side);
        System.out.printf("%nAdded %s%n%n", side.getDescription());
    }

    // ---------------- Checkout ----------------

    private boolean checkout() {
        if (!order.isValidForCheckout()) {
            System.out.println("\nOrder is empty. Add a plate, drink, or main side first.\n");
            return false;
        }
        if (!order.hasPlates() && !order.hasDrinkOrMainSide()) {
            System.out.println("\nA plate-less order must include a drink or main side.\n");
            return false;
        }

        printHeader();
        System.out.println("ORDER SUMMARY");
        System.out.println("---------------------------------------------");
        List<OrderItem> items = order.getItems();
        for (int i = 0; i < items.size(); i++) {
            OrderItem oi = items.get(i);
            System.out.println("Item " + (i + 1) + ":");
            if (oi instanceof Item plate) {
                System.out.println(plate.getDescription());
                System.out.printf("    Subtotal: $%.2f%n", plate.getPrice());
            } else {
                System.out.println("    " + oi.getDescription());
            }
            System.out.println();
        }
        System.out.printf("TOTAL: $%.2f%n%n", order.getTotal());

        System.out.println("1) Confirm");
        System.out.println("0) Cancel");
        String cmd = prompt("> ");
        if ("1".equals(cmd)) {
            try {
                Path path = receiptWriter.write(order);
                System.out.println("\nReceipt saved: " + path.toAbsolutePath() + "\n");
            } catch (IOException e) {
                System.out.println("Failed to save receipt: " + e.getMessage());
            }
            return true;
        }
        System.out.println("Order cancelled.\n");
        return true;
    }

    // ---------------- Helpers ----------------

    private void printHeader() {
        System.out.println();
        System.out.println("=============================================");
        System.out.println("     " + shopName + " — Point of Sale");
        System.out.println("=============================================");
    }

    private Size chooseSize() {
        return choose("Select size", Size.values(), Size::getLabel);
    }

    private <E extends Enum<E>> E chooseEnum(String label, E[] options) {
        return choose(label, options, Enum::toString);
    }

    private <T> T choose(String label, T[] options, java.util.function.Function<T, String> render) {
        System.out.println("\n" + label + ":");
        for (int i = 0; i < options.length; i++) {
            System.out.println("  " + (i + 1) + ") " + render.apply(options[i]));
        }
        System.out.println("  0) Back");
        while (true) {
            String input = prompt("> ").trim();
            if (input.equals("0")) return null;
            try {
                int idx = Integer.parseInt(input) - 1;
                if (idx >= 0 && idx < options.length) return options[idx];
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Invalid choice.");
        }
    }

    private String prompt(String msg) {
        System.out.print(msg);
        return scanner.nextLine();
    }
}
