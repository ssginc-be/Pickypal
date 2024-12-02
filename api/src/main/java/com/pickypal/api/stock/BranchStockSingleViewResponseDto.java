package com.pickypal.api.stock;

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
public class BranchStockSingleViewResponseDto {
    private List<BranchStock> content;
}
