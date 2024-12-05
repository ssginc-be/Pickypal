package com.pickypal.api.outgoing;

import com.pickypal.api.order.Orders;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HeadOutgoingItemResponseDto {
    private Long id;
    private Long orderId;
    private String itemId;
    private String itemName;
    private String supplierName;
    private String status;
    private Integer quantity;
    private String committer;
    private LocalDateTime outStartTime;
    private LocalDateTime outEndTime;

    public static HeadOutgoingItemResponseDto of(HeadOutgoingItem ho) {
        Orders order = ho.getOrders();

        return HeadOutgoingItemResponseDto.builder()
                .id(ho.getId())
                .orderId(order.getId())
                .itemId(order.getItem().getId())
                .itemName(order.getItem().getName())
                .supplierName(ho.getSupplier().getName())
                .status(ho.getStatus())
                .quantity(ho.getQuantity())
                .committer(ho.getCommitter().getId())
                .outStartTime(ho.getOutStartTime())
                .outEndTime(ho.getOutEndTime())
                .build();
    }
}
