package com.pickypal.dto.incoming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchIncomingItemResponseDto {
    private String id;
    private String supplierId;
    private String supplierName;
    private String itemId;
    private String itemName;
    private Integer quantity;
    private LocalDateTime inTime;
}
