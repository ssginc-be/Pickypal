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

    /* 출고 승인된 상품을 배송하는 가상 api */
    // 1. 발주 id에 해당하는 발주의 status를 '입고완료'로 변경
    // 2. 발주 id에 해당하는 발주의 inEndTime을 now()로 변경
    // 3. 발주 id에 해당하는 본사 출고내역의 status를 '출고완료'로 변경
    // 4. 발주 id에 해당하는 본사 출고내역의 outEndTime을 now()로 변경
    // 5. 발주 id에 해당하는 지점의 입고내역 등록
    // 6. 발주 id에 해당하는 지점의 해당 상품 재고를 추가
    @GetMapping("/ship")
    public void shipItem(@RequestParam("order_id") Long oid) {
        service.shipItem(oid);
    }

    // 납품업체 테이블에 정보가 없는 상품 row 모두 제거
    // DeleteMapping이 맞는데 테스트용이라서 Get으로 구현함
    @GetMapping("cleanse")
    public void cleanseStockData() {
        service.cleanseStockData();
    }
}
