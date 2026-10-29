
package com.uet.legacy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileOrderRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    void testSaveWritesOrderDetailsToFile() throws Exception {

        Path logFile = tempDir.resolve("test-orders.log");

        FileOrderRepository repository = new FileOrderRepository();
        repository.setFilePath(logFile.toString());

        Map<String, Object> orderData = new HashMap<>();
        orderData.put("customerEmail", "student@example.com");

        repository.save(orderData, 1500.0, "PAID");

        assertTrue(
                Files.exists(logFile),
                "The log file should be created"
        );

        String content = Files.readString(logFile);

        assertAll(
                () -> assertTrue(
                        content.contains("student@example.com"),
                        "The file should contain the email"
                ),
                () -> assertTrue(
                        content.contains("1500.0"),
                        "The file should contain the payable amount"
                ),
                () -> assertTrue(
                        content.contains("PAID"),
                        "The file should contain the payment status"
                )
        );
    }
}
