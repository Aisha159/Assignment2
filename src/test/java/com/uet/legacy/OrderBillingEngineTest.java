
package com.uet.legacy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderBillingEngineTest {

    private OrderBillingEngine engine;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        engine = new OrderBillingEngine();
        engine.setLogFilePath(tempDir.resolve("test.log").toString());
    }

    private Map<String, Object> createOrder(
            String customerType,
            String country,
            String paymentMethod,
            double price,
            int quantity) {

        Map<String, Object> item = new HashMap<>();
        item.put("price", price);
        item.put("quantity", quantity);

        List<Map<String, Object>> items = new ArrayList<>();
        items.add(item);

        Map<String, Object> order = new HashMap<>();
        order.put("customerEmail", "student@example.com");
        order.put("customerType", customerType);
        order.put("country", country);
        order.put("paymentMethod", paymentMethod);
        order.put("items", items);
        order.put("cardNumber", "1234567812345678");
        order.put("mobileNumber", "03001234567");

        return order;
    }

    private Map<String, Object> standardOrder() {
        return createOrder(
                "REGULAR", "PK", "CASH_ON_DELIVERY", 1000.0, 1);
    }

    @Test
    void standardOrderShouldBeAccepted() {
        assertTrue(engine.processOrder(standardOrder()));
    }

    @Test
    void vipOrderShouldBeAccepted() {
        assertTrue(engine.processOrder(createOrder(
                "VIP", "PK", "CASH_ON_DELIVERY", 6000.0, 1)));
    }

    @Test
    void vipOrderBelow5000ShouldBeAccepted() {
        assertTrue(engine.processOrder(createOrder(
                "VIP", "PK", "CASH_ON_DELIVERY", 4000.0, 1)));
    }

    @Test
    void studentOrderShouldBeAccepted() {
        assertTrue(engine.processOrder(createOrder(
                "STUDENT", "PK", "CASH_ON_DELIVERY", 2000.0, 1)));
    }

    @Test
    void corporateOrderShouldBeAccepted() {
        assertTrue(engine.processOrder(createOrder(
                "CORPORATE", "PK", "CASH_ON_DELIVERY", 3000.0, 1)));
    }

    @Test
    void regularLargeOrderShouldBeAccepted() {
        assertTrue(engine.processOrder(createOrder(
                "REGULAR", "PK", "CASH_ON_DELIVERY", 12000.0, 1)));
    }

    @Test
    void ukCountryCodeShouldBeAccepted() {
        assertTrue(engine.processOrder(createOrder(
                "REGULAR", "UK", "CASH_ON_DELIVERY", 1000.0, 1)));
    }

    @Test
    void usCountryCodeShouldBeAccepted() {
        assertTrue(engine.processOrder(createOrder(
                "REGULAR", "US", "CASH_ON_DELIVERY", 1000.0, 1)));
    }

    @Test
    void defaultCountryShouldBeAccepted() {
        Map<String, Object> order = standardOrder();
        order.remove("country");

        assertTrue(engine.processOrder(order));
    }

    @Test
    void creditCardPaymentShouldBeAccepted() {
        assertTrue(engine.processOrder(createOrder(
                "REGULAR", "PK", "CREDIT_CARD", 1000.0, 1)));
    }

    @Test
    void easyPaisaPaymentShouldBeAccepted() {
        assertTrue(engine.processOrder(createOrder(
                "REGULAR", "PK", "EASYPAISA", 1000.0, 1)));
    }

    @Test
    void cashOnDeliveryShouldBeAccepted() {
        assertTrue(engine.processOrder(standardOrder()));
    }

    @Test
    void defaultPaymentMethodShouldBeRejected() {
        Map<String, Object> order = standardOrder();
        order.remove("paymentMethod");

        assertFalse(engine.processOrder(order));
    }

    @Test
    void unsupportedPaymentMethodShouldBeRejected() {
        Map<String, Object> order = standardOrder();
        order.put("paymentMethod", "CASH");

        assertFalse(engine.processOrder(order));
    }

    @Test
    void invalidCreditCardShouldBeRejected() {
        Map<String, Object> order = standardOrder();
        order.put("paymentMethod", "CREDIT_CARD");
        order.put("cardNumber", "1234");

        assertFalse(engine.processOrder(order));
    }

    @Test
    void nondigitCreditCardShouldBeRejected() {
        Map<String, Object> order = standardOrder();
        order.put("paymentMethod", "CREDIT_CARD");
        order.put("cardNumber", "123456781234abcd");

        assertFalse(engine.processOrder(order));
    }

    @Test
    void invalidEasyPaisaNumberShouldBeRejected() {
        Map<String, Object> order = standardOrder();
        order.put("paymentMethod", "EASYPAISA");
        order.put("mobileNumber", "12345");

        assertFalse(engine.processOrder(order));
    }

    @Test
    void nullOrderShouldBeRejected() {
        assertFalse(engine.processOrder(null));
    }

    @Test
    void emptyOrderShouldBeRejected() {
        assertFalse(engine.processOrder(new HashMap<>()));
    }

    @Test
    void invalidEmailShouldBeRejected() {
        Map<String, Object> order = standardOrder();
        order.put("customerEmail", "invalid-email");

        assertFalse(engine.processOrder(order));
    }

    @Test
    void missingEmailShouldBeRejected() {
        Map<String, Object> order = standardOrder();
        order.remove("customerEmail");

        assertFalse(engine.processOrder(order));
    }

    @Test
    void emptyCartShouldBeRejected() {
        Map<String, Object> order = standardOrder();
        order.put("items", new ArrayList<>());

        assertFalse(engine.processOrder(order));
    }

    @Test
    void missingItemsShouldBeRejected() {
        Map<String, Object> order = standardOrder();
        order.remove("items");

        assertFalse(engine.processOrder(order));
    }

    @Test
    void negativePriceShouldBeRejected() {
        assertFalse(engine.processOrder(createOrder(
                "REGULAR", "PK", "CASH_ON_DELIVERY", -100.0, 1)));
    }

    @Test
    void zeroPriceShouldBeRejected() {
        assertFalse(engine.processOrder(createOrder(
                "REGULAR", "PK", "CASH_ON_DELIVERY", 0.0, 1)));
    }

    @Test
    void zeroQuantityShouldBeRejected() {
        assertFalse(engine.processOrder(createOrder(
                "REGULAR", "PK", "CASH_ON_DELIVERY", 100.0, 0)));
    }

    @Test
    void missingPriceShouldBeRejected() {
        Map<String, Object> order = standardOrder();

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items =
                (List<Map<String, Object>>) order.get("items");

        items.get(0).remove("price");

        assertFalse(engine.processOrder(order));
    }

    @Test
    void multipleItemsOrderShouldBeAccepted() {
        Map<String, Object> order = standardOrder();

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items =
                (List<Map<String, Object>>) order.get("items");

        Map<String, Object> secondItem = new HashMap<>();
        secondItem.put("price", 500.0);
        secondItem.put("quantity", 2);
        items.add(secondItem);

        assertTrue(engine.processOrder(order));
    }
}
