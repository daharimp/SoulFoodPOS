package com.yearup.soulfoodpos.io;

import com.yearup.soulfoodpos.model.Item;
import com.yearup.soulfoodpos.model.Order;
import com.yearup.soulfoodpos.model.OrderItem;
import com.yearup.soulfoodpos.model.ShopInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReceiptWriter {

    private static final DateTimeFormatter FILE_FMT =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
    private static final DateTimeFormatter DISPLAY_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path receiptsDir;
    private final ShopInfo shop;

    public ReceiptWriter(ShopInfo shop) {
        this(shop, Paths.get("data", "receipts"));
    }

    public ReceiptWriter(ShopInfo shop, Path receiptsDir) {
        this.shop = shop;
        this.receiptsDir = receiptsDir;
    }

    public Path write(Order order) throws IOException {
        Files.createDirectories(receiptsDir);
        String fileName = order.getTimestamp().format(FILE_FMT) + ".txt";
        Path target = receiptsDir.resolve(fileName);
        Files.writeString(target, render(order));
        return target;
    }

    private String render(Order order) {
        StringBuilder sb = new StringBuilder();

        sb.append("  ╔══════════════════════════════════════════════════════╗\n");
        sb.append("  ║                                                    ║\n");
        sb.append("  ║          D O R O T H Y ' S   O V E N               ║\n");
        sb.append("  ║          ──── Soul Food Kitchen ────              ║\n");
        sb.append("  ║                                                    ║\n");
        sb.append("  ╚══════════════════════════════════════════════════════╝\n");
        sb.append("                  ").append(shop.addressLine1()).append("\n");
        sb.append("               ").append(shop.cityStateZip()).append("\n");
        sb.append("                    ").append(shop.phone()).append("\n");
        sb.append("            ").append(shop.hours()).append("\n");
        sb.append("               \"").append(shop.tagline()).append("\"\n");
        sb.append("\n");
        sb.append("  Order: ").append(order.getTimestamp().format(DISPLAY_FMT)).append("\n");
        sb.append("  ────────────────────────────────────────────────────────\n\n");

        List<OrderItem> items = order.getItems();
        for (int i = 0; i < items.size(); i++) {
            OrderItem oi = items.get(i);
            sb.append("Item ").append(i + 1).append(":\n");
            if (oi instanceof Item plate) {
                sb.append(plate.getDescription()).append("\n");
                sb.append(String.format("    Subtotal: $%.2f\n", plate.getPrice()));
            } else {
                sb.append("    ").append(oi.getDescription()).append("\n");
            }
            sb.append("\n");
        }

        sb.append("  ────────────────────────────────────────────────────────\n");
        sb.append(String.format("  TOTAL: $%.2f\n", order.getTotal()));
        sb.append("  ════════════════════════════════════════════════════════\n");
        sb.append("\n");
        sb.append("   Thank you for shopping at ").append(shop.name()).append("!\n");
        sb.append("\n");
        sb.append("  * Bring this receipt back and receive a *\n");
        sb.append("  *         FREE large drink!             *\n");
        sb.append("  ════════════════════════════════════════════════════════\n");
        return sb.toString();
    }
}
