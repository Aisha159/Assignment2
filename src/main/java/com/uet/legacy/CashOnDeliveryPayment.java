
package com.uet.legacy;

import java.util.Map;

public class CashOnDeliveryPayment implements PaymentStrategy {

    @Override
    public String pay(Map<String, Object> orderData, double payableAmount) {

        if (payableAmount > 25000) {
            System.out.println("Cash on delivery rejected: Amount exceeds limit.");
            return null;
        }

        return "PENDING_CASH";
    }
}
