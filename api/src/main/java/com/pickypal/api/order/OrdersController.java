package com.pickypal.api.order;

import com.pickypal.api.auth.JwtKit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Queue-ri
 */

@RestController
@RequestMapping(value="/orders")
@RequiredArgsConstructor
public class OrdersController {
    private final JwtKit jwtKit;
    private final OrdersService service;

    // page 단위로 요청자 지점의 발주 내역을 조회하는 api
    // ex) 1 page = 1~20 row / 2 page = 21~30 row ...
    @GetMapping("/{pageIdx}")
    public ResponseEntity<?> getByPage(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("pageIdx") Integer pageIdx) {
        String uid = jwtKit.validate(tokenHeader);
        return service.getByPage(uid, pageIdx);
    }

    // 발주 조회: 상품 id로 필터링
    @GetMapping("/item-id/{pageIdx}")
    public ResponseEntity<?> getByPageWithItemIdFilter(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("pageIdx") Integer pageIdx,
            @RequestParam("item_id") String itemId) {
        String uid = jwtKit.validate(tokenHeader);
        return service.getByPageWithItemIdFilter(uid, pageIdx, itemId);
    }

    // 발주 조회: 상품명으로 필터링
    @GetMapping("/item-name/{pageIdx}")
    public ResponseEntity<?> getByPageWithItemNameFilter(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("pageIdx") Integer pageIdx,
            @RequestParam("item_name") String itemName) {
        String uid = jwtKit.validate(tokenHeader);
        return service.getByPageWithItemNameFilter(uid, pageIdx, itemName);
    }

    // 발주 조회: 발주일로 필터링
    @GetMapping("/date/{pageIdx}")
    public ResponseEntity<?> getByPageWithOrderTimeFilter(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("pageIdx") Integer pageIdx,
            @RequestParam("date") String date) {
        String uid = jwtKit.validate(tokenHeader);
        return service.getByPageWithOrderTimeFilter(uid, pageIdx, date);
    }

    // 발주 등록
    @PostMapping
    public void save(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestBody OrdersSaveRequestDto dto) {
        String uid = jwtKit.validate(tokenHeader);
        service.save(uid, dto);
    }

    // 발주 삭제
    @DeleteMapping("/{oid}")
    public void delete(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("oid") Long orderId) {
        String uid = jwtKit.validate(tokenHeader);
        service.delete(uid, orderId);
    }

    // 본사: '출고대기' 상태의 발주 조회
    @GetMapping("/head/{pageIdx}")
    public ResponseEntity<?> getWaitingOrders(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("pageIdx") Integer pageIdx) {
        String uid = jwtKit.validate(tokenHeader);
        return service.getWaitingOrders(uid, pageIdx);
    }

}
