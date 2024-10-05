package com.example.flowerShop.utils.report;

import com.example.flowerShop.dto.order.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

@Component
public class TxtGenerator implements FileGeneratorStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(TxtGenerator.class);

    @Override
    public void generateFile(List<OrderDTO> orders) {

        SalesCalculator reportGenerator = new SalesCalculator();
        Map<String, Long> monthlySalesReport = reportGenerator.generateMonthlySalesReport(orders);
        Map<String, Map<String, Long>> items = reportGenerator.generateMonthlySalesReportForOrderItems(orders);

        StringBuilder content = new StringBuilder();
        for (Map.Entry<String, Map<String, Long>> productEntry : items.entrySet()) {
            String productName = productEntry.getKey();
            Map<String, Long> salesByMonth = productEntry.getValue();
            for (Map.Entry<String, Long> entry : salesByMonth.entrySet()) {
                String monthYear = entry.getKey();
                Long totalSold = entry.getValue();
                content.append("Produs: ").append(productName)
                        .append(", Luna: ").append(monthYear)
                        .append(", Total: ").append(totalSold).append("\n");
            }
        }

        content.append("\n");
        for (Map.Entry<String, Long> entry : monthlySalesReport.entrySet()) {
            content.append("Luna: ").append(entry.getKey()).append(", Total produse vandute: ").append(entry.getValue()).append("\n");
        }

        content.append("\nTotal incasari:" + reportGenerator.getMoney(orders));

        File tempFile;
        try {
            tempFile = File.createTempFile("sales_report", ".txt");
            try (FileWriter writer = new FileWriter(tempFile)) {
                writer.write(content.toString());
            }

            File destFile = new File("sales_report.txt");
            Files.copy(tempFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            LOGGER.info("Fisierul TXT a fost generat si salvat in directorul curent.");
        } catch (IOException e) {
            LOGGER.error("Ceva nu a mers bine la salvarea raportului in format txt", e);
        }
    }
}
