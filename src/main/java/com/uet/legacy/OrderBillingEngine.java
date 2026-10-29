
package com.uet.legacy;

import java.util.List;
import java.util.Map;

public class OrderBillingEngine {

   private String logFilePath = "order_audit.log";
private final NotificationService notificationService;
private final OrderRepository orderRepository;

public OrderBillingEngine() {
    this(new FileOrderRepository(), new ConsoleNotificationService());
}

public OrderBillingEngine(OrderRepository orderRepository) {
    this(orderRepository, new ConsoleNotificationService());
}

public OrderBillingEngine(
        OrderRepository orderRepository,
        NotificationService notificationService) {

    this.orderRepository = orderRepository;
    this.notificationService = notificationService;

    if (orderRepository instanceof FileOrderRepository) {
        ((FileOrderRepository) orderRepository).setFilePath(logFilePath);
    }
}


    public boolean processOrder(Map<String, Object> orderData) {

        if (orderData == null || orderData.isEmpty()) {
            System.out.println("Error: Order data is empty!");
            return false;
        }

        String email = (String) orderData.get("customerEmail");

        if (email == null || !email.contains("@")) {
            System.out.println("Error: Invalid customer email!");
            return false;
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items =
                (List<Map<String, Object>>) orderData.get("items");

        if (items == null || items.isEmpty()) {
            System.out.println("Error: Cart has no items!");
            return false;
        }

        double rawTotal = 0.0;

        for (Map<String, Object> item : items) {
            if (item == null
                    || !item.containsKey("price")
                    || !item.containsKey("quantity")
                    || item.get("price") == null
                    || item.get("quantity") == null) {
                System.out.println(
                        "Error: Missing item price or quantity!");
                return false;
            }

            if (!(item.get("price") instanceof Number)
                    || !(item.get("quantity") instanceof Number)) {
                System.out.println(
                        "Error: Invalid price or quantity for item!");
                return false;
            }

            double price = ((Number) item.get("price")).doubleValue();
            int quantity = ((Number) item.get("quantity")).intValue();

            if (price <= 0 || quantity <= 0) {
                System.out.println(
                        "Error: Invalid price or quantity for item!");
                return false;
            }

            rawTotal += price * quantity;
        }

        String customerType =
                (String) orderData.getOrDefault("customerType", "REGULAR");

        DiscountCalculator discountCalculator = new DiscountCalculator();
        double discount = discountCalculator.calculateDiscount(
                rawTotal, customerType, items);

        double netAmount = rawTotal - discount;

        String country = (String) orderData.getOrDefault("country", "PK");

        TaxCalculator taxCalculator = new TaxCalculator();
        double tax = taxCalculator.calculateTax(netAmount, country);
        double payableAmount = netAmount + tax;

        String paymentMethod =
                (String) orderData.getOrDefault("paymentMethod", "CASH");

        PaymentStrategy paymentStrategy;

        switch (paymentMethod) {
            case "CREDIT_CARD":
                paymentStrategy = new CreditCardPayment();
                break;

            case "EASYPAISA":
                paymentStrategy = new EasyPaisaPayment();
                break;

            case "CASH_ON_DELIVERY":
                paymentStrategy = new CashOnDeliveryPayment();
                break;

            default:
                System.out.println(
                        "Error: Unsupported payment method: "
                                + paymentMethod);
                return false;
        }

        String paymentStatus =
                paymentStrategy.pay(orderData, payableAmount);

        if (paymentStatus == null) {
            return false;
        }

        // Save order through the repository.
      orderRepository.save(orderData, payableAmount, paymentStatus);

        String notifyChannel =
                (String) orderData.getOrDefault("notifyChannel", "EMAIL");

        if (notifyChannel.equals("EMAIL")) {
            notificationService.sendEmail(
                    email,
                    "Your order of PKR "
                            + String.format("%.2f", payableAmount)
                            + " is confirmed!");
        } else if (notifyChannel.equals("SMS")) {
            String phone =
                    (String) orderData.getOrDefault(
                            "phone", "0300-0000000");

            notificationService.sendSMS(
                    phone,
                    "Order confirmed! Paid: PKR "
                            + String.format("%.2f", payableAmount));
        }

        return true;
    }

    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;

        if (orderRepository instanceof FileOrderRepository) {
            ((FileOrderRepository) orderRepository)
                    .setFilePath(logFilePath);
        }
    }

    public String getLogFilePath() {
        return logFilePath;
    }
}
