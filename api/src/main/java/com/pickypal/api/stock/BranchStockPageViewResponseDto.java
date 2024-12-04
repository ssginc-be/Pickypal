package com.pickypal.api.stock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Queue-ri
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchStockPageViewResponseDto {
    private String stockId; // UI에 보이지 않지만 내부적으로 사용할 수 있음
    private String itemId;
    private String itemName;
    private String type;
    private String tag;
    private String supplierName;
    private Integer price;
    private Integer stock; // 재고 수량
    private LocalDateTime incomingTime;
    private Integer currentPage;
}
