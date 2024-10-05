package com.example.flowerShop.utils.report;

import com.example.flowerShop.dto.order.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class CsvGenerator implements FileGeneratorStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvGenerator.class);

    @Override
    public void generateFile(List<OrderDTO> orders) {
        SalesCalculator salesCalculator = new SalesCalculator();
        Map<String, Long> monthlySalesReport = salesCalculator.generateMonthlySalesReport(orders);
        Map<String, Map<String, Long>> items = salesCalculator.generateMonthlySalesReportForOrderItems(orders);

        try (FileWriter writer = new FileWriter("sales_report.csv")) {
            writer.append("Produs,Luna,Total\n");
            for (Map.Entry<String, Map<String, Long>> productEntry : items.entrySet()) {
                String productName = productEntry.getKey();
                Map<String, Long> salesByMonth = productEntry.getValue();
                for (Map.Entry<String, Long> entry : salesByMonth.entrySet()) {
                    String monthYear = entry.getKey();
                    Long totalSold = entry.getValue();
                    writer.append(productName).append(",")
                            .append(monthYear).append(",")
                            .append(String.valueOf(totalSold)).append("\n");
                }
            }

            writer.append("\n");
            writer.append("Luna,Total produse vandute\n");

            for (Map.Entry<String, Long> entry : monthlySalesReport.entrySet()) {
                writer.append(entry.getKey()).append(",").append(String.valueOf(entry.getValue())).append("\n");
            }

            writer.append("\nTotal incasari,");
            writer.append(String.valueOf(salesCalculator.getMoney(orders)));

            LOGGER.info("Fisierul CSV a fost salvat.");
        } catch (IOException e) {
            LOGGER.error("Ceva nu a mers bine la salvarea raportului in format CSV", e);
        }
    }
}