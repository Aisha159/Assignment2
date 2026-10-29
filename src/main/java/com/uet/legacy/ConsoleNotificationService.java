
package com.uet.legacy;

public class ConsoleNotificationService implements NotificationService {

    @Override
    public void sendEmail(String email, String message) {
        System.out.println(
                "[EMAIL DISPATCHED] To: " + email + " | " + message);
    }

    @Override
    public void sendSMS(String phone, String message) {
        System.out.println(
                "[SMS DISPATCHED] To: " + phone + " | " + message);
    }
}
