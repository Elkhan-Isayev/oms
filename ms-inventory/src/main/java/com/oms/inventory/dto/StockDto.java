package com.oms.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockDto {
    private Long productId;
    private Long warehouseId;
    private Integer quantityAvailable;
}
