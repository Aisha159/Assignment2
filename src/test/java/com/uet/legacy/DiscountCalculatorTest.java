
package com.uet.legacy;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiscountCalculatorTest {

    private final DiscountCalculator calculator = new DiscountCalculator();

    private List<Map<String, Object>> items(int count) {
        return new ArrayList<>(
                java.util.Collections.nCopies(count, Map.of()));
    }

    @Test
    void vipAbove5000Gets15PercentDiscount() {
        assertEquals(900.0,
                calculator.calculateDiscount(6000, "VIP", items(1)),
                0.001);
    }

    @Test
    void vipAt5000Gets10PercentDiscount() {
        assertEquals(500.0,
                calculator.calculateDiscount(5000, "VIP", items(1)),
                0.001);
    }

    @Test
    void studentGets8PercentDiscount() {
        assertEquals(160.0,
                calculator.calculateDiscount(2000, "STUDENT", items(1)),
                0.001);
    }

    @Test
    void corporateWithFewerThan10ItemsGets5PercentDiscount() {
        assertEquals(150.0,
                calculator.calculateDiscount(3000, "CORPORATE", items(9)),
                0.001);
    }

    @Test
    void corporateWith10ItemsGets20PercentDiscount() {
        assertEquals(600.0,
                calculator.calculateDiscount(3000, "CORPORATE", items(10)),
                0.001);
    }

    @Test
    void regularAbove10000GetsFlat500Discount() {
        assertEquals(500.0,
                calculator.calculateDiscount(12000, "REGULAR", items(1)),
                0.001);
    }

    @Test
    void regularAt10000GetsNoDiscount() {
        assertEquals(0.0,
                calculator.calculateDiscount(10000, "REGULAR", items(1)),
                0.001);
    }
}
