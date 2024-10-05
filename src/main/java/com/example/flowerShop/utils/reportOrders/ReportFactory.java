package com.example.flowerShop.utils.reportOrders;

public class ReportFactory {
    public static Report createReport(String reportType) {
        switch (reportType.toLowerCase()) {
            case "pdf":
                return new PdfReport();
            case "txt":
                return new TxtReport();
            case "csv":
                return new CsvReport();
            default:
                throw new IllegalArgumentException("Unknown report type: " + reportType);
        }
    }
}