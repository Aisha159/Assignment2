
package com.uet.legacy;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class FileOrderRepository implements OrderRepository {

    private String filePath = "order_audit.log";

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void save(
            Map<String, Object> orderData,
            double payableAmount,
            String paymentStatus) {

        String email = (String) orderData.get("customerEmail");

        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(
                    LocalDateTime.now()
                    + " | Order for: " + email
                    + " | Payable: PKR " + payableAmount
                    + " | Status: " + paymentStatus
                    + System.lineSeparator());
        } catch (IOException e) {
            System.out.println(
                    "Failed to write order log: " + e.getMessage());
        }
    }
}
