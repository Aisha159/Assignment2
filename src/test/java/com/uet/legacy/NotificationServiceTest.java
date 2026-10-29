
package com.uet.legacy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceTest {

    @Test
    void testEmailNotification() {
        FakeNotificationService notification =
                new FakeNotificationService();

        notification.sendEmail(
                "student@example.com", "Order confirmed");

        assertEquals("student@example.com", notification.email);
        assertEquals("Order confirmed", notification.emailMessage);
    }

    @Test
    void testSMSNotification() {
        FakeNotificationService notification =
                new FakeNotificationService();

        notification.sendSMS(
                "03001234567", "Order confirmed");

        assertEquals("03001234567", notification.phone);
        assertEquals("Order confirmed", notification.smsMessage);
    }

    static class FakeNotificationService
            implements NotificationService {

        String email;
        String emailMessage;
        String phone;
        String smsMessage;

        @Override
        public void sendEmail(String email, String message) {
            this.email = email;
            this.emailMessage = message;
        }

        @Override
        public void sendSMS(String phone, String message) {
            this.phone = phone;
            this.smsMessage = message;
        }
    }
}
