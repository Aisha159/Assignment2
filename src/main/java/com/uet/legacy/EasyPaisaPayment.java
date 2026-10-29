
package com.uet.legacy;

import java.util.Map;

public class EasyPaisaPayment implements PaymentStrategy {

    @Override
    public String pay(Map<String, Object> orderData, double payableAmount) {

        String mobileNo =
                (String) orderData.getOrDefault("mobileNumber", "");

        if (mobileNo.length() == 11
                && mobileNo.startsWith("03")) {

            System.out.println(
                    "Sending EasyPaisa OTP push to "
                            + mobileNo + " for PKR "
                            + payableAmount);

            return "PAID";

        } else {
            System.out.println(
                    "EasyPaisa failed: Invalid mobile number.");
            return null;
        }
    }
}
