package com.thederailingmafia.carwash.notificationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "notifications.queue")
    public void receiveMessage(String message) {
        try {
            Map<String, Object> event = objectMapper.readValue(message, Map.class);
            String eventType = (String) event.get("event");
            switch (eventType) {
                case "order.created":
                    System.out.println("Notification: Order " + event.get("orderId") + " created for customer " + event.get("customerEmail"));
                    break;
                case "order.assigned":
                    System.out.println("Notification: Order " + event.get("orderId") + " assigned to washer " + event.get("washerEmail"));
                    break;
                case "order.updated":
                    System.out.println("Notification: Order " + event.get("orderId") + " status updated to " + event.get("status") + " for customer " + event.get("customerEmail"));
                    break;
                case "order.completed":
                    System.out.println("Notification: Order " + event.get("orderId") + " completed for customer " + event.get("customerEmail"));
                    break;
                case "order.not_completed":
                    System.out.println("Notification: Order " + event.get("orderId") + " not completed for customer " + event.get("customerEmail"));
                    break;
                case "washer.accepted":
                    System.out.println("Notification: Order " + event.get("orderId") + " accepted by washer " + event.get("washerEmail"));
                    break;
                case "washer.rejected":
                    System.out.println("Notification: Order " + event.get("orderId") + " rejected by washer " + event.get("washerEmail"));
                    break;
                case "washer.invoice_generated":
                    System.out.println("Notification: Invoice " + event.get("invoiceId") + " generated for order " + event.get("orderId") + " by washer " + event.get("washerEmail") + ", payment ID: " + event.get("paymentId"));
                    break;
                case "payment.processed":
                    System.out.println("Notification: Payment " + event.get("paymentId") + " processed for order " + event.get("orderId") + ", status: " + event.get("status"));
                    break;
                case "payment.confirmed":
                    System.out.println("Notification: Payment " + event.get("paymentId") + " confirmed for order " + event.get("orderId") + ", amount: " + event.get("amount"));
                    break;
                case "payment.failed":
                    System.out.println("Notification: Payment " + event.get("paymentId") + " failed for order " + event.get("orderId") + ", status: " + event.get("status"));
                    break;
                default:
                    System.out.println("Unknown event: " + eventType);
            }
        } catch (Exception e) {
            System.err.println("Error processing notification: " + e.getMessage());
        }
    }
}