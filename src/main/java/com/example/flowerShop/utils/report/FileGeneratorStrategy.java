package com.example.flowerShop.utils.report;

import com.example.flowerShop.dto.order.OrderDTO;

import java.util.List;

public interface FileGeneratorStrategy {

    void generateFile(List<OrderDTO> orders);
}
