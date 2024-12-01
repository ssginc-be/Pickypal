package com.pickypal.api.backdoor;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Queue-ri
 */

@RestController
@RequestMapping(value="/backdoor")
@RequiredArgsConstructor
public class BackdoorController {
    private final BackdoorService service;

    // 본사의 재고를 모든 상품에 대하여 99개로 세팅하는 가상 api
    @GetMapping("/head/full-stock")
    public void setHeadStock99() {
        service.setHeadStock99();
    }

    // 특정 지점의 재고를 모든 상품에 대하여 0~20개 사이로 세팅하는 가상 api
    @GetMapping("/branch/random-stock")
    public void setBranchStockBetween0And20(@RequestParam("branch_id") String branchId) {
        service.setBranchStockBetween0And20(branchId);
    }

    // 출고 승인된 상품을 배송하는 가상 api
    @GetMapping("/out")
    public void shipItem() {
        service.shipItem();
    }

    // 지점의 상품을 판매 처리하는 가상 api
}
