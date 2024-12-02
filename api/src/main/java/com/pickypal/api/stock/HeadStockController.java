package com.pickypal.api.stock;

import com.pickypal.api.auth.JwtKit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Queue-ri
 */

@RestController
@RequestMapping(value="/head/stock")
@RequiredArgsConstructor
public class HeadStockController {
    private final JwtKit jwtKit;
    private final HeadStockService service;

    // page 단위로 요청자 지점의 재고를 조회하는 api
    // ex) 1 page = 1~20 row / 2 page = 21~30 row ...
    @GetMapping("/{pageIdx}")
    public ResponseEntity<?> getByPage(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("pageIdx") Integer pageIdx) {
        String uid = jwtKit.validate(tokenHeader);
        return service.getByPage(uid, pageIdx);
    }

    // 지점 재고 조회: 상품 id로 필터링
    @GetMapping("/item-id")
    public ResponseEntity<?> getByItemId(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestParam("item_id") String itemId) {
        String uid = jwtKit.validate(tokenHeader);
        return service.getByItemId(uid, itemId);
    }

    // 지점 재고 조회: 상품명으로 필터링
    @GetMapping("/item-name/{pageIdx}")
    public ResponseEntity<?> getByPageWithItemNameFilter(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("pageIdx") Integer pageIdx,
            @RequestParam("item_name") String itemName) {
        String uid = jwtKit.validate(tokenHeader);
        return service.getByPageWithItemNameFilter(uid, pageIdx, itemName);
    }

    // 지점 재고 조회: 최종 수정일(=최근입고일)로 필터링
    @GetMapping("/date/{pageIdx}")
    public ResponseEntity<?> getByPageWithLastModifiedAtFilter(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("pageIdx") Integer pageIdx,
            @RequestParam("date") String date) {
        String uid = jwtKit.validate(tokenHeader);
        return service.getByPageWithLastModifiedAtFilter(uid, pageIdx, date);
    }

    // 지점 재고 조회: 유형(type): 행사상품, PL, FF로 필터링
    @GetMapping("/type/{pageIdx}")
    public ResponseEntity<?> getByPageWithTypeFilter(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("pageIdx") Integer pageIdx,
            @RequestParam("type") String type) {
        String uid = jwtKit.validate(tokenHeader);
        return service.getByPageWithTypeFilter(uid, pageIdx, type);
    }
}
