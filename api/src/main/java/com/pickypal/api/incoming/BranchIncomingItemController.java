package com.pickypal.api.incoming;

import com.pickypal.api.auth.JwtKit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/branch/incoming")
public class BranchIncomingItemController {
    private final JwtKit jwtKit;
    private final BranchIncomingItemService service;

    /* 입고 내역 조회 */
    // @RequestParam은 임시 땜빵이고 원래는 JWT 토큰을 받음
    // 지점의 입고 내역 조회 (최신순 정렬)
    @GetMapping("/{pageIdx}")
    public ResponseEntity<?> getByPage(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("pageIdx") Integer pageIdx) {
        String uid = jwtKit.validate(tokenHeader);
        return service.getByPage(uid, pageIdx);
    }

    // 상품 id로 조회
    @GetMapping("/item-id/{pageIdx}")
    public ResponseEntity<?> getByPageWithItemIdFilter(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("pageIdx") Integer pageIdx,
            @RequestParam("item_id") String itemId) {
        String uid = jwtKit.validate(tokenHeader);
        return service.getByPageWithItemIdFilter(uid, pageIdx, itemId);
    }

    // 상품명으로 조회
    @GetMapping("/item-name/{pageIdx}")
    public ResponseEntity<?> getByPageWithItemNameFilter(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("pageIdx") Integer pageIdx,
            @RequestParam("item_name") String itemName) {
        String uid = jwtKit.validate(tokenHeader);
        return service.getByPageWithItemNameFilter(uid, pageIdx, itemName);
    }

    // 입고 등록일시로 조회
    @GetMapping("/date/{pageIdx}")
    public ResponseEntity<?> getByPageWithInTimeFilter(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("pageIdx") Integer pageIdx,
            @RequestParam("date") String date) {
        String uid = jwtKit.validate(tokenHeader);
        return service.getByPageWithInTimeFilter(uid, pageIdx, date);
    }

    /* 입고 내역 등록 */
    // api로 구현하지 않음
    // backdoor api를 통해 배송 완료 처리하면, 시스템 내부적으로 입고 내역 등록 함수를 자동 호출하기 때문
}

