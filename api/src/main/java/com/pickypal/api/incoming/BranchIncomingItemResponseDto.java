package com.pickypal.api.incoming;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BranchIncomingItemResponseDto {
    private String id;
    private String supplierId;
    private String supplierName;
    private String itemId;
    private String itemName;
    private Integer quantity;
    private LocalDateTime inTime;

    public static BranchIncomingItemResponseDto of(BranchIncomingItem bi) {
        return BranchIncomingItemResponseDto.builder()
                .id(bi.getId())
                .supplierId(bi.getItem().getSupplier().getId())
                .supplierName(bi.getItem().getSupplier().getName())
                .itemId(bi.getItem().getId())
                .itemName(bi.getItem().getName())
                .quantity(bi.getQuantity())
                .inTime(bi.getInTime())
                .build();
    }
}
