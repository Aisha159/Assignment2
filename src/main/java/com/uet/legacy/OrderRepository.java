
package com.uet.legacy;

import java.util.Map;

public interface OrderRepository {

    void save(
            Map<String, Object> orderData,
            double payableAmount,
            String paymentStatus);
}
