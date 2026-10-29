
package com.uet.legacy;

import java.util.Map;

public interface PaymentStrategy {

    String pay(Map<String, Object> orderData, double payableAmount);
}
