package com.example.flowerShop.config;

import com.example.flowerShop.dto.notification.InvoiceDTO;
import com.example.flowerShop.dto.notification.MessageDTO;
import com.example.flowerShop.dto.notification.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.UUID;

@Component
public class EmailSender {

    @Autowired
    private RabbitSender rabbitSender;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);

    public void sendEmailToUserAsync(InvoiceDTO invoiceDTO) {
        try {
            rabbitSender.send(invoiceDTO);
        } catch (Exception e) {
            LOGGER.error("Eroare la trimiterea asincrona a request-ului: " + e.getMessage());
        }
    }

    public void sendEmailToUserSync(UUID userId, String name, String email, String bodyAction, String body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth("flowerShop" + userId.toString() + userId.toString());
            NotificationDTO notificationRequestDto = new NotificationDTO(userId, name, email, bodyAction, body);
            HttpEntity<NotificationDTO> entity = new HttpEntity<>(notificationRequestDto, headers);
            ResponseEntity<MessageDTO> response = restTemplate.exchange(
                    "http://localhost:8085/send-email",
                    HttpMethod.POST,
                    entity,
                    MessageDTO.class
            );
            this.getResponseStatus(response);
        } catch (RestClientException e) {
            LOGGER.error("Eroare la trimiterea request: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Alta eroare " + e.getMessage());
        }
    }

    private void getResponseStatus(ResponseEntity<MessageDTO> response) {

        if (response.getStatusCode() == HttpStatus.OK) {
            LOGGER.info("Mail-ul a fost trimis: " + response.getBody().getMessage());
        } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            LOGGER.error("Autorizare esuata: Invalid authorization token");
        } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            LOGGER.error("Payload gresit: " + response.getBody().getMessage());
        } else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            LOGGER.error("Eroare interna: " + response.getBody().getMessage());
        } else {
            LOGGER.error("Alta eroare: " + response.getBody().getMessage());
        }
    }
}
