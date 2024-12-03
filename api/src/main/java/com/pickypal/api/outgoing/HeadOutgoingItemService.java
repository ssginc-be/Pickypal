package com.pickypal.api.outgoing;

import com.pickypal.api.item.Item;
import com.pickypal.api.order.Orders;
import com.pickypal.api.order.OrdersRepository;
import com.pickypal.api.order.OrdersViewResponseDto;
import com.pickypal.api.stock.HeadStock;
import com.pickypal.api.stock.HeadStockRepository;
import com.pickypal.api.user.ServiceUser;
import com.pickypal.api.user.ServiceUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Queue-ri
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class HeadOutgoingItemService {
    private final OrdersRepository oRepo;
    private final HeadOutgoingItemRepository hoRepo;
    private final ServiceUserRepository uRepo;
    private final HeadStockRepository hsRepo;

    // 본사에서 발주를 [ 출고승인 / 출고거절 ] 하는 api
    // 출고승인시 내부적으로 출고내역을 등록함
    // refactor 필수
    @Transactional
    public void setStatus(String uid, Long oid, String status) {
        // 해당 발주의 출고승인/출고거절 상태 업데이트
        Orders order = oRepo.findById(oid).get();
        order.setStatus(status);

        // 출고승인이면: 출고승인일 기록
        if (status.equals("출고승인")) {
            order.setOutStartTime(LocalDateTime.now());
        }

        oRepo.save(order);

        if (status.equals("출고승인")) {
            // 출고승인이면: 출고 내역 등록
            HeadOutgoingItem ho = new HeadOutgoingItem();
            ho.setOrders(order);
            ho.setSupplier(order.getItem().getSupplier());
            ho.setQuantity(order.getQuantity());

            ServiceUser user = uRepo.findById(uid).get();
            ho.setCommitter(user);

            hoRepo.save(ho);

            // 출고승인이면: 본사 재고에서 출고된 상품의 재고 차감
            List<HeadStock> hsList = hsRepo.findAllByItemId(order.getItem().getId());
            HeadStock hs = hsList.get(0);
            Integer stock = hs.getStock();
            hs.setStock(stock - order.getQuantity());

            hsRepo.save(hs);
        }
    }

    // 본사의 출고 내역 조회 api: page size 20
    public ResponseEntity<?> getByPage(String uid, Integer pageIdx) {
        // Pageable 설정
        Pageable pageable = PageRequest.of(pageIdx, 20, Sort.by(Sort.Direction.ASC, "id"));

        // 테이블 조회
        Page<HeadOutgoingItem> pageData = hoRepo.findPageBy(pageable);

        // client에서 파싱 용이하도록 필요한 정보만 선별하여 dto로 변환
        List<HeadOutgoingItemResponseDto> dtoList = new ArrayList<>();
        List<HeadOutgoingItem> entityList = pageData.getContent();

        for (HeadOutgoingItem entity : entityList) {
            dtoList.add(HeadOutgoingItemResponseDto.of(entity));
        }

        // logging
        log.info("* * * HeadOutgoingItemService: head outgoing item read info");
        log.info("request from: {}", "HEAD");
        log.info("page: {}", pageable.getPageNumber());

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(dtoList);
    }

}
