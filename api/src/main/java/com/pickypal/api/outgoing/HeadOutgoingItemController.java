package com.pickypal.api.outgoing;

import com.pickypal.api.auth.JwtKit;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Queue-ri
 */

@RestController
@RequestMapping(value="/head/outgoing")
@RequiredArgsConstructor
public class HeadOutgoingItemController {
    private final JwtKit jwtKit;
    private final HeadOutgoingItemService service;

    // 본사에서 발주를 [ 출고승인 / 출고거절 ] 하는 api
    // 출고승인시 내부적으로 출고내역을 등록함
    // refactor 필수
    @GetMapping
    public void setStatus(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestParam("order_id") Long oid,
            @RequestParam("status") String status) {
        String uid = jwtKit.validate(tokenHeader);
        service.setStatus(uid, oid, status);
    }

}
