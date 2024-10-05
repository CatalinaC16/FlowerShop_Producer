package com.example.flowerShop.config;

import com.example.flowerShop.dto.notification.InvoiceDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(InvoiceDTO invoiceDTO) {
        rabbitTemplate.convertAndSend(AMQPConfig.EXCHANGE_NAME, AMQPConfig.ROUTING_KEY, invoiceDTO);
    }
}