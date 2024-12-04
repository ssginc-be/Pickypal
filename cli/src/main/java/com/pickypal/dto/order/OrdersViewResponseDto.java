package com.pickypal.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersViewResponseDto {
    private Long orderId; // UI에 보이지 않지만 내부적으로 사용할 수 있음
    private String itemId;
    private String itemName;
    private String branchId;
    private String branchName;
    private Integer quantity; // 발주 수량
    private Integer totalPrice; // 총 단가
    private String status; // 출고대기, 출고승인, 출고완료
    private LocalDateTime orderTime;
    private Integer currentPage;
}
