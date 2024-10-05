package com.example.flowerShop.utils.report;

import com.example.flowerShop.dto.order.OrderDTO;

import java.util.List;

public class ContextReport {

    private FileGeneratorStrategy strategy;

    public ContextReport(FileGeneratorStrategy strategy) {
        this.strategy = strategy;
    }

    public void executeStrategy(List<OrderDTO> orders) {
        strategy.generateFile(orders);
    }
}
