package com.example.flowerShop.config;

import com.example.flowerShop.dto.notification.NotificationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSender.class);

    public void uploadFile(String url, MultipartFile file, NotificationDTO notificationDTO, String authorizationToken) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Authorization", authorizationToken);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(notificationDTO);
        HttpEntity entity = MultipartEntityBuilder.create().addBinaryBody("file", file.getInputStream(), ContentType.APPLICATION_OCTET_STREAM, file.getOriginalFilename()).addTextBody("requestDto", jsonPayload, ContentType.APPLICATION_JSON).build();
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost);
        LOGGER.info("Raspuns de la server: " + response.getStatusLine().getStatusCode());
        String responseBody = EntityUtils.toString(response.getEntity());
        LOGGER.info("Raspuns body: " + responseBody);
    }

    public void sendReportOnEmail(String nameFile, UUID userId, String name, String email, String bodyAction, String body) {
        String microserviceA3Url = "http://localhost:8085/send-email/report";
        try {
            Path path = Paths.get(nameFile);
            String originalFileName = nameFile;
            String contentType = "text/plain";
            byte[] content = null;
            try {
                content = Files.readAllBytes(path);
            } catch (IOException e) {
                LOGGER.error("Eroare la citirea fisierului: " + e.getMessage());
            }
            NotificationDTO notificationRequestDto = new NotificationDTO(userId, name, email, bodyAction, body);
            MultipartFile result = new MockMultipartFile(nameFile, originalFileName, contentType, content);
            String authorizationToken = "Bearer flowerShop" + userId.toString() + userId.toString();
            this.uploadFile(microserviceA3Url, result, notificationRequestDto, authorizationToken);
        } catch (IOException e) {
            LOGGER.error("Eroare la incarcarea fisierului: " + e.getMessage());
        }
    }
}
