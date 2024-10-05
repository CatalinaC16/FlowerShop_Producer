package com.example.flowerShop.utils.reportOrders;

import com.example.flowerShop.dto.order.OrderDTO;
import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.util.List;

public interface Report {
    void generateReport(List<OrderDTO> orders, String filePath) throws IOException, DocumentException;
}

