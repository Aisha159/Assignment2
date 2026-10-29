
package com.uet.legacy;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OrderBillingEngineTest {

    private OrderBillingEngine createEngine() {
        OrderBillingEngine engine = new OrderBillingEngine();
        engine.setLogFilePath("test_order_audit.log");
        return engine;
    }

    private Map<String, Object> createOrder(
            String email, double price, int quantity) {

        Map<String, Object> item = new HashMap<>();
        item.put("price", price);
        item.put("quantity", quantity);

        Map<String, Object> order = new HashMap<>();
        order.put("customerEmail", email);
        order.put("items", List.of(item));
        order.put("paymentMethod", "CASH_ON_DELIVERY");

        return order;
    }

    // 1. Empty order should be rejected
    @Test
    void emptyOrderShouldBeRejected() {
        assertFalse(createEngine().processOrder(new HashMap<>()));
    }

    // 2. Null order should be rejected
    @Test
    void nullOrderShouldBeRejected() {
        assertFalse(createEngine().processOrder(null));
    }

    // 3. Invalid email should be rejected
    @Test
    void invalidEmailShouldBeRejected() {
        assertFalse(createEngine().processOrder(
                createOrder("invalid-email", 1000, 1)));
    }

    // 4. Missing email should be rejected
    @Test
    void missingEmailShouldBeRejected() {
        Map<String, Object> order = createOrder("test@example.com", 1000, 1);
        order.remove("customerEmail");

        assertFalse(createEngine().processOrder(order));
    }

    // 5. Empty cart should be rejected
    @Test
    void emptyCartShouldBeRejected() {
        Map<String, Object> order = createOrder("test@example.com", 1000, 1);
        order.put("items", List.of());

        assertFalse(createEngine().processOrder(order));
    }

    // 6. Missing price should be rejected
    @Test
    void missingPriceShouldBeRejected() {
        Map<String, Object> item = new HashMap<>();
        item.put("quantity", 1);

        Map<String, Object> order = createOrder("test@example.com", 1000, 1);
        order.put("items", List.of(item));

        assertFalse(createEngine().processOrder(order));
    }

    // 7. Missing quantity should be rejected
    @Test
    void missingQuantityShouldBeRejected() {
        Map<String, Object> item = new HashMap<>();
        item.put("price", 1000);

        Map<String, Object> order = createOrder("test@example.com", 1000, 1);
        order.put("items", List.of(item));

        assertFalse(createEngine().processOrder(order));
    }

    // 8. Negative price should be rejected
    @Test
    void negativePriceShouldBeRejected() {
        assertFalse(createEngine().processOrder(
                createOrder("test@example.com", -100, 1)));
    }

    // 9. Zero price should be rejected
    @Test
    void zeroPriceShouldBeRejected() {
        assertFalse(createEngine().processOrder(
                createOrder("test@example.com", 0, 1)));
    }

    // 10. Negative quantity should be rejected
    @Test
    void negativeQuantityShouldBeRejected() {
        assertFalse(createEngine().processOrder(
                createOrder("test@example.com", 1000, -1)));
    }

    // 11. Zero quantity should be rejected
    @Test
    void zeroQuantityShouldBeRejected() {
        assertFalse(createEngine().processOrder(
                createOrder("test@example.com", 1000, 0)));
    }

    // 12. Regular order should be accepted
    @Test
    void regularOrderShouldBeAccepted() {
        assertTrue(createEngine().processOrder(
                createOrder("test@example.com", 1000, 1)));
    }

    // 13. VIP order should be accepted
    @Test
    void vipOrderShouldBeAccepted() {
        Map<String, Object> order = createOrder("vip@example.com", 1000, 1);
        order.put("customerType", "VIP");

        assertTrue(createEngine().processOrder(order));
    }

    // 14. VIP order above 5000 should be accepted
    @Test
    void vipOrderAbove5000ShouldBeAccepted() {
        Map<String, Object> order = createOrder("vip@example.com", 6000, 1);
        order.put("customerType", "VIP");

        assertTrue(createEngine().processOrder(order));
    }

    // 15. Student order should be accepted
    @Test
    void studentOrderShouldBeAccepted() {
        Map<String, Object> order = createOrder("student@example.com", 1000, 1);
        order.put("customerType", "STUDENT");

        assertTrue(createEngine().processOrder(order));
    }

    // 16. Corporate order should be accepted
    @Test
    void corporateOrderShouldBeAccepted() {
        Map<String, Object> order = createOrder("company@example.com", 1000, 1);
        order.put("customerType", "CORPORATE");

        assertTrue(createEngine().processOrder(order));
    }

    // 17. Valid credit card should be accepted
    @Test
    void validCreditCardShouldBeAccepted() {
        Map<String, Object> order = createOrder("test@example.com", 1000, 1);
        order.put("paymentMethod", "CREDIT_CARD");
        order.put("cardNumber", "1234567812345678");

        assertTrue(createEngine().processOrder(order));
    }

    // 18. Invalid credit card should be rejected
    @Test
    void invalidCreditCardShouldBeRejected() {
        Map<String, Object> order = createOrder("test@example.com", 1000, 1);
        order.put("paymentMethod", "CREDIT_CARD");
        order.put("cardNumber", "1234");

        assertFalse(createEngine().processOrder(order));
    }

    // 19. Valid EasyPaisa should be accepted
    @Test
    void validEasyPaisaShouldBeAccepted() {
        Map<String, Object> order = createOrder("test@example.com", 1000, 1);
        order.put("paymentMethod", "EASYPAISA");
        order.put("mobileNumber", "03123456789");

        assertTrue(createEngine().processOrder(order));
    }

    // 20. Invalid EasyPaisa number should be rejected
    @Test
    void invalidEasyPaisaShouldBeRejected() {
        Map<String, Object> order = createOrder("test@example.com", 1000, 1);
        order.put("paymentMethod", "EASYPAISA");
        order.put("mobileNumber", "12345");

        assertFalse(createEngine().processOrder(order));
    }

    // 21. Cash on delivery for a small order should be accepted
    @Test
    void cashOnDeliverySmallOrderShouldBeAccepted() {
        Map<String, Object> order = createOrder("test@example.com", 1000, 1);
        order.put("paymentMethod", "CASH_ON_DELIVERY");

        assertTrue(createEngine().processOrder(order));
    }

    // 22. Cash on delivery above the limit should be rejected
    @Test
    void cashOnDeliveryAbove25000ShouldBeRejected() {
        Map<String, Object> order = createOrder("test@example.com", 30000, 1);
        order.put("paymentMethod", "CASH_ON_DELIVERY");

        assertFalse(createEngine().processOrder(order));
    }

    // 23. Unsupported payment method should be rejected
    @Test
    void unsupportedPaymentMethodShouldBeRejected() {
        Map<String, Object> order = createOrder("test@example.com", 1000, 1);
        order.put("paymentMethod", "BITCOIN");

        assertFalse(createEngine().processOrder(order));
    }

    // 24. Missing payment method should be rejected
    @Test
    void defaultPaymentMethodShouldBeRejected() {
        Map<String, Object> order = createOrder("test@example.com", 1000, 1);
        order.remove("paymentMethod");

        assertFalse(createEngine().processOrder(order));
    }

    // 25. Order with default country should be accepted
    @Test
    void defaultCountryOrderShouldBeAccepted() {
        assertTrue(createEngine().processOrder(
                createOrder("test@example.com", 1000, 1)));
    }

    // 26. Order from the US should be accepted
    @Test
    void usCountryOrderShouldBeAccepted() {
        Map<String, Object> order = createOrder("test@example.com", 1000, 1);
        order.put("country", "US");

        assertTrue(createEngine().processOrder(order));
    }

    // 27. Order from the UK should be accepted
    @Test
    void ukCountryOrderShouldBeAccepted() {
        Map<String, Object> order = createOrder("test@example.com", 1000, 1);
        order.put("country", "UK");

        assertTrue(createEngine().processOrder(order));
    }

    // 28. Order with multiple items should be accepted
    @Test

void multipleItemsOrderShouldBeAccepted() {
    OrderBillingEngine engine = createEngine();

    Map<String, Object> order = createOrder(
            "test@example.com", 1000, 1
    );

    Map<String, Object> item1 = new HashMap<>();
    item1.put("price", 1000.0);
    item1.put("quantity", 1);

    Map<String, Object> item2 = new HashMap<>();
    item2.put("price", 2000.0);
    item2.put("quantity", 2);

    order.put("items", List.of(item1, item2));

    assertTrue(engine.processOrder(order));
}


    // 29. Order should be accepted for notification processing
    @Test
    void notificationOrderShouldBeAccepted() {
        assertTrue(createEngine().processOrder(
                createOrder("test@example.com", 1000, 1)));
    }
}
