
package com.uet.legacy;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PaymentStrategyTest {

    @Test
    void validCreditCardShouldBePaid() {
        Map<String, Object> order = new HashMap<>();
        order.put("cardNumber", "1234567812345678");

        assertEquals("PAID",
                new CreditCardPayment().pay(order, 1000));
    }

    @Test
    void invalidCreditCardShouldBeRejected() {
        Map<String, Object> order = new HashMap<>();
        order.put("cardNumber", "1234");

        assertNull(new CreditCardPayment().pay(order, 1000));
    }

    @Test
    void validEasyPaisaNumberShouldBePaid() {
        Map<String, Object> order = new HashMap<>();
        order.put("mobileNumber", "03001234567");

        assertEquals("PAID",
                new EasyPaisaPayment().pay(order, 1000));
    }

    @Test
    void invalidEasyPaisaNumberShouldBeRejected() {
        Map<String, Object> order = new HashMap<>();
        order.put("mobileNumber", "12345");

        assertNull(new EasyPaisaPayment().pay(order, 1000));
    }

    @Test
    void cashOnDeliveryWithinLimitShouldBePending() {
        assertEquals("PENDING_CASH",
                new CashOnDeliveryPayment().pay(new HashMap<>(), 25000));
    }

    @Test
    void cashOnDeliveryAboveLimitShouldBeRejected() {
        assertNull(new CashOnDeliveryPayment().pay(new HashMap<>(), 25001));
    }
}
