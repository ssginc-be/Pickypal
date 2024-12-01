package com.pickypal.api.incoming;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/branch/incoming")
public class BranchIncomingItemController {
    private final BranchIncomingItemService service;

    /* 입고 내역 조회 */
    // @RequestParam은 임시 땜빵이고 원래는 JWT 토큰을 받음
    // 지점의 입고 내역 조회 (최신순 정렬)
    @GetMapping
    public List<BranchIncomingItemResponseDto> getAll(@RequestParam("branch_id") String branchId) {
        return service.getAll(branchId);
    }

    // 상품 id로 조회
    @GetMapping("/item_id")
    public  List<BranchIncomingItemResponseDto> getAllByItemId(@RequestParam("branch_id") String branchId, @RequestParam("item_id") String itemId){
        return service.getAllByItemId(branchId, itemId);
    }

    // 상품명으로 조회
    @GetMapping("/item_name")
    public List<BranchIncomingItemResponseDto> getAllByItemName(@RequestParam("branch_id") String branchId, @RequestParam("item_name") String itemName){
        return service.getAllByItemName(branchId, itemName);
    }

    // 입고 등록일시로 조회
    @GetMapping("/in_time")
    public List<BranchIncomingItemResponseDto> getAllByInTime(@RequestParam("branch_id") String branchId, @RequestParam("date") String date) {
        return service.getAllByInTime(branchId, date);
    }

    /* 입고 내역 등록 */
    // api로 구현하지 않음
    // backdoor api를 통해 배송 완료 처리하면, 시스템 내부적으로 입고 내역 등록 함수를 자동 호출하기 때문
}

