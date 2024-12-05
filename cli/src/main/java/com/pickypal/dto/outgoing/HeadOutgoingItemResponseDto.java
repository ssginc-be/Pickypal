package com.pickypal.dto.outgoing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Hyun7en
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
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

}
