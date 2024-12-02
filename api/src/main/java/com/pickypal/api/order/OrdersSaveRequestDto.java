package com.pickypal.api.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Queue-ri
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersSaveRequestDto { // 발주 등록 요청 dto
    private String itemId;
    private Integer quantity;
    // 나머지 발주 column은 backend가 item 테이블을 조회하여 알아서 기입
}
