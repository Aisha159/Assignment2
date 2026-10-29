
package com.uet.legacy;

public interface NotificationService {

    void sendEmail(String email, String message);

    void sendSMS(String phone, String message);
}
