package com.example.flowerShop.utils.report;

import com.example.flowerShop.dto.order.OrderDTO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class PdfGenerator implements FileGeneratorStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfGenerator.class);

    @Override
    public void generateFile(List<OrderDTO> orders) {
        SalesCalculator salesCalculator = new SalesCalculator();
        Map<String, Long> monthlySalesReport = salesCalculator.generateMonthlySalesReport(orders);
        Map<String, Map<String, Long>> items = salesCalculator.generateMonthlySalesReportForOrderItems(orders);

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("sales_report.pdf"));
            document.open();

            for (Map.Entry<String, Map<String, Long>> productEntry : items.entrySet()) {
                String productName = productEntry.getKey();
                Map<String, Long> salesByMonth = productEntry.getValue();
                for (Map.Entry<String, Long> entry : salesByMonth.entrySet()) {
                    String monthYear = entry.getKey();
                    Long totalSold = entry.getValue();
                    document.add(new Paragraph("Produs: " + productName + ", Luna: " + monthYear + ", Total: " + totalSold));
                }
            }

            document.add(new Paragraph("\n"));
            for (Map.Entry<String, Long> entry : monthlySalesReport.entrySet()) {
                document.add(new Paragraph("Luna: " + entry.getKey() + ", Total produse vandute: " + entry.getValue()));
            }

            document.add(new Paragraph("\nTotal incasari:" + salesCalculator.getMoney(orders)));
            document.close();

            LOGGER.info("Fisierul PDF a fost salvat.");
        } catch (IOException | DocumentException e) {
            LOGGER.error("Ceva nu a mers bine la generarea sau salvarea raportului in format PDF", e);
        }
    }
}
