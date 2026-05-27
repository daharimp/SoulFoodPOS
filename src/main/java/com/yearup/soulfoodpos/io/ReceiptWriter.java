package com.yearup.soulfoodpos.io;

import com.yearup.soulfoodpos.model.Item;
import com.yearup.soulfoodpos.model.Order;
import com.yearup.soulfoodpos.model.OrderItem;

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
    private final String shopName;

    public ReceiptWriter(String shopName) {
        this(shopName, Paths.get("data", "receipts"));
    }

    public ReceiptWriter(String shopName, Path receiptsDir) {
        this.shopName = shopName;
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
        sb.append("=============================================\n");
        sb.append("        ").append(shopName).append("\n");
        sb.append("        ").append(order.getTimestamp().format(DISPLAY_FMT)).append("\n");
        sb.append("=============================================\n\n");

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

        sb.append("---------------------------------------------\n");
        sb.append(String.format("TOTAL: $%.2f\n", order.getTotal()));
        sb.append("=============================================\n");
        sb.append("\n");
        sb.append("   Thank you for shopping at Dorothy's Oven!\n");
        sb.append("\n");
        sb.append("  * Bring this receipt back and receive a  *\n");
        sb.append("  *         FREE large drink!              *\n");
        sb.append("=============================================\n");
        return sb.toString();
    }
}
