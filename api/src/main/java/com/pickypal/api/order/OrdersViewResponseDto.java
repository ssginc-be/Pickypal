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
public class OrdersViewResponseDto {
    private List<Orders> content;
    private Integer currentPage;
    private Integer totalPageSize;
}
