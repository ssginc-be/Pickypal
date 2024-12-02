package com.pickypal.api.order;

import com.pickypal.api.auth.JwtKit;
import com.pickypal.api.backdoor.BackdoorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    @GetMapping("/{page}")
    public ResponseEntity<?> getByPage(
            @RequestHeader("Authorization") String tokenHeader,
            @PageableDefault(sort="orderTime", direction=Sort.Direction.DESC, value=20) Pageable pageable) {
        String uid = jwtKit.validate(tokenHeader);
        return service.getByPage(uid, pageable);
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

}
