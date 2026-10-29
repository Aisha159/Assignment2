
package com.uet.legacy;

import java.util.Map;

public class CreditCardPayment implements PaymentStrategy {

    @Override
    public String pay(Map<String, Object> orderData, double payableAmount) {

        String cardNumber =
                (String) orderData.getOrDefault("cardNumber", "");

        if (cardNumber.length() == 16
                && cardNumber.matches("\\d+")) {

            System.out.println(
                    "Processing credit card payment of PKR "
                            + payableAmount);

            return "PAID";

        } else {
            System.out.println("Card declined: Must be 16 digits.");
            return null;
        }
    }
}
