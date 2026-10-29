
package com.uet.legacy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaxCalculatorTest {

    private final TaxCalculator calculator = new TaxCalculator();

    @Test
    void pakistanTaxIs17Percent() {
        assertEquals(170.0, calculator.calculateTax(1000, "PK"), 0.001);
    }

    @Test
    void usTaxIs8Percent() {
        assertEquals(80.0, calculator.calculateTax(1000, "US"), 0.001);
    }

    @Test
    void ukTaxIs20Percent() {
        assertEquals(200.0, calculator.calculateTax(1000, "UK"), 0.001);
    }

    @Test
    void unknownCountryUsesDefault15PercentTax() {
        assertEquals(150.0, calculator.calculateTax(1000, "CA"), 0.001);
    }

    @Test
    void zeroAmountHasZeroTax() {
        assertEquals(0.0, calculator.calculateTax(0, "PK"), 0.001);
    }
}
