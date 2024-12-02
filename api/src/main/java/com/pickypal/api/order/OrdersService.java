package com.pickypal.api.order;

import com.pickypal.api.auth.LoginResponseDto;
import com.pickypal.api.branch.Branch;
import com.pickypal.api.branch.BranchRepository;
import com.pickypal.api.item.Item;
import com.pickypal.api.item.ItemRepository;
import com.pickypal.api.stock.BranchStock;
import com.pickypal.api.stock.BranchStockRepository;
import com.pickypal.api.stock.HeadStock;
import com.pickypal.api.stock.HeadStockRepository;
import com.pickypal.api.user.ServiceUser;
import com.pickypal.api.user.ServiceUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Queue-ri
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository oRepo;
    private final ServiceUserRepository uRepo;
    private final ItemRepository iRepo;
    private final BranchRepository bRepo;

    // page 단위로 요청자 지점의 발주 내역을 조회하는 api
    // ex) 1 page = 1~20 row / 2 page = 21~30 row ...
    public ResponseEntity<?> getByPage(String uid, Pageable pageable) {
        ServiceUser user = uRepo.findById(uid).get();
        String branchId = user.getBranch().getId();
        Page<Orders> pageData = oRepo.findPageBy(pageable);

        // client에서 파싱 용이하도록 필요한 정보만 선별하여 dto로 변환
        OrdersViewResponseDto dto = new OrdersViewResponseDto(
                pageData.getContent(), pageData.getNumber(), pageData.getTotalPages()
        );

        // logging
        log.info("* * * OrdersService: orders read info");
        log.info("request from: {}", branchId);
        log.info("page: {}", pageable.getPageNumber());

        return ResponseEntity
                .status(HttpStatus.ACCEPTED.value())
                .body(dto);
    }

    // 발주 등록
    public ResponseEntity<?> save(String uid, OrdersSaveRequestDto dto) {
        String itemId = dto.getItemId();
        Item item;
        // client에서 이상한 item id를 줬을 경우 예외 처리 - 400 반환
        try {
            item = iRepo.findById(itemId).get();
        } catch (Exception e) {
            log.error("* * * OrdersService: order save failed - could not find requested item id");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Orders orders = new Orders();
        ServiceUser user = uRepo.findById(uid).get();
        orders.setBranch(bRepo.findById(user.getBranch().getId()).get());
        orders.setItem(item);
        orders.setQuantity(dto.getQuantity());
        orders.setPrice(dto.getQuantity() * item.getPrice());

        oRepo.save(orders);
        
        // 발주 정상 등록 - 200 반환
        log.info("* * * OrdersService: order save done");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
