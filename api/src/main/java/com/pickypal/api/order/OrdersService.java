package com.pickypal.api.order;

import com.pickypal.api.branch.BranchRepository;
import com.pickypal.api.item.Item;
import com.pickypal.api.item.ItemRepository;
import com.pickypal.api.stock.HeadStock;
import com.pickypal.api.stock.HeadStockPageViewResponseDto;
import com.pickypal.api.user.ServiceUser;
import com.pickypal.api.user.ServiceUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    /* 지점의 발주 조회 */
    // page 단위로 요청자 지점의 발주 내역을 조회하는 api
    // ex) 1 page = 1~20 row / 2 page = 21~30 row ...
    public ResponseEntity<?> getByPage(String uid, Integer pageIdx) {
        // Pageable 설정
        Pageable pageable = PageRequest.of(pageIdx, 20, Sort.by(Sort.Direction.DESC, "orderTime"));

        // 나머지 테이블 조회
        ServiceUser user = uRepo.findById(uid).get();
        String branchId = user.getBranch().getId();
        Page<Orders> pageData = oRepo.findPageByBranchId(pageable, branchId);

        // client에서 파싱 용이하도록 필요한 정보만 선별하여 dto로 변환
        List<OrdersViewResponseDto> dtoList = new ArrayList<>();
        List<Orders> entityList = pageData.getContent();

        for (Orders entity : entityList) {
            Item item = entity.getItem();
            OrdersViewResponseDto dto = new OrdersViewResponseDto(
                    entity.getId(),
                    item.getId(),
                    item.getName(),
                    entity.getBranch().getId(),
                    entity.getBranch().getName(),
                    entity.getQuantity(),
                    entity.getTotalPrice(),
                    entity.getStatus(),
                    entity.getOrderTime(),
                    pageData.getNumber()
            );
            dtoList.add(dto);
        }

        // logging
        log.info("* * * OrdersService: orders read info");
        log.info("request from: {}", branchId);
        log.info("page: {}", pageable.getPageNumber());

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(dtoList);
    }

    // 발주 조회: 상품 id로 필터링
    public ResponseEntity<?> getByPageWithItemIdFilter(String uid, Integer pageIdx, String itemId) {
        // Pageable 설정
        Pageable pageable = PageRequest.of(pageIdx, 20, Sort.by(Sort.Direction.DESC, "orderTime"));

        // 나머지 테이블 조회
        ServiceUser user = uRepo.findById(uid).get();
        String branchId = user.getBranch().getId();
        Page<Orders> pageData = oRepo.findAllByBranchIdAndItemId(pageable, branchId, itemId);

        // client에서 파싱 용이하도록 필요한 정보만 선별하여 dto로 변환
        List<OrdersViewResponseDto> dtoList = new ArrayList<>();
        List<Orders> entityList = pageData.getContent();

        for (Orders entity : entityList) {
            Item item = entity.getItem();
            OrdersViewResponseDto dto = new OrdersViewResponseDto(
                    entity.getId(),
                    item.getId(),
                    item.getName(),
                    entity.getBranch().getId(),
                    entity.getBranch().getName(),
                    entity.getQuantity(),
                    entity.getTotalPrice(),
                    entity.getStatus(),
                    entity.getOrderTime(),
                    pageData.getNumber()
            );
            dtoList.add(dto);
        }

        // logging
        log.info("* * * OrdersService: orders read info");
        log.info("request from: {}", branchId);
        log.info("page: {}", pageable.getPageNumber());

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(dtoList);
    }

    // 발주 조회: 상품명으로 필터링
    public ResponseEntity<?> getByPageWithItemNameFilter(String uid, Integer pageIdx, String itemName) {
        // Pageable 설정
        // Pageable pageable = PageRequest.of(pageIdx, 20, Sort.by(Sort.Direction.DESC, "order_time"));
        final int PAGE_SIZE = 20;
        // 0 -->  0 ~ 19
        // 1 --> 20 ~ 39
        // 2 --> 40 ~ 59
        int startIdx = pageIdx * PAGE_SIZE;
        //int endIdx = (pageIdx + 1) * pageSize - 1;

        // 나머지 테이블 조회
        ServiceUser user = uRepo.findById(uid).get();
        String branchId = user.getBranch().getId();
        List<Orders> pageData = oRepo.findAllByBranchIdAndItemName(branchId, itemName, startIdx, PAGE_SIZE);

        // client에서 파싱 용이하도록 필요한 정보만 선별하여 dto로 변환
        List<OrdersViewResponseDto> dtoList = new ArrayList<>();

        for (Orders entity : pageData) {
            Item item = entity.getItem();
            OrdersViewResponseDto dto = new OrdersViewResponseDto(
                    entity.getId(),
                    item.getId(),
                    item.getName(),
                    entity.getBranch().getId(),
                    entity.getBranch().getName(),
                    entity.getQuantity(),
                    entity.getTotalPrice(),
                    entity.getStatus(),
                    entity.getOrderTime(),
                    pageIdx
            );
            dtoList.add(dto);
        }

        // logging
        log.info("* * * OrdersService: orders read info");
        log.info("request from: {}", branchId);
        log.info("page: {}", pageIdx);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(dtoList);
    }

    // 발주 조회: 발주일로 필터링
    public ResponseEntity<?> getByPageWithOrderTimeFilter(String uid, Integer pageIdx, String date) {
        // Pageable 설정
        // Pageable pageable = PageRequest.of(pageIdx, 20, Sort.by(Sort.Direction.DESC, "orderTime"));
        // paging 설정
        final int PAGE_SIZE = 20;
        // 0 -->  0 ~ 19
        // 1 --> 20 ~ 39
        // 2 --> 40 ~ 59
        int startIdx = pageIdx * PAGE_SIZE;
        //int endIdx = (pageIdx + 1) * pageSize - 1;

        // nextDate 계산
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(date, formatter);
        LocalDate parsedNextDate = parsedDate.plusDays(1);
        String nextDate = parsedNextDate.toString();

        // 나머지 테이블 조회
        ServiceUser user = uRepo.findById(uid).get();
        String branchId = user.getBranch().getId();
        List<Orders> pageData = oRepo.findAllByBranchIdAndOrderTime(branchId, date, nextDate, startIdx, PAGE_SIZE);

        // client에서 파싱 용이하도록 필요한 정보만 선별하여 dto로 변환
        List<OrdersViewResponseDto> dtoList = new ArrayList<>();

        for (Orders entity : pageData) {
            Item item = entity.getItem();
            OrdersViewResponseDto dto = new OrdersViewResponseDto(
                    entity.getId(),
                    item.getId(),
                    item.getName(),
                    entity.getBranch().getId(),
                    entity.getBranch().getName(),
                    entity.getQuantity(),
                    entity.getTotalPrice(),
                    entity.getStatus(),
                    entity.getOrderTime(),
                    pageIdx
            );
            dtoList.add(dto);
        }

        // logging
        log.info("* * * OrdersService: orders read info");
        log.info("request from: {}", branchId);
        log.info("page: {}", pageIdx);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(dtoList);
    }

    // 발주 등록
    @Transactional
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
        orders.setTotalPrice(dto.getQuantity() * item.getPrice());

        oRepo.save(orders);
        
        // 발주 정상 등록 - 200 반환
        log.info("* * * OrdersService: order save done");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 발주 삭제
    @Transactional
    public ResponseEntity<?> delete(String uid, Long orderId) {
        ServiceUser user = uRepo.findById(uid).get();
        String branchId = user.getBranch().getId();

        oRepo.deleteByIdAndBranchId(orderId, branchId);

        // 발주 정상 삭제 - 200 반환
        log.info("* * * OrdersService: order delete done");
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /* 본사의 발주 조회 */
    // '출고대기' 상태만 필터링하여 조회
    public ResponseEntity<?> getWaitingOrders(String uid, Integer pageIdx) {
        // Pageable 설정
        Pageable pageable = PageRequest.of(pageIdx, 20, Sort.by(Sort.Direction.DESC, "orderTime"));

        Page<Orders> pageData = oRepo.findPageByStatus(pageable, "출고대기");

        // client에서 파싱 용이하도록 필요한 정보만 선별하여 dto로 변환
        List<OrdersViewResponseDto> dtoList = new ArrayList<>();
        List<Orders> entityList = pageData.getContent();

        for (Orders entity : entityList) {
            Item item = entity.getItem();
            OrdersViewResponseDto dto = new OrdersViewResponseDto(
                    entity.getId(),
                    item.getId(),
                    item.getName(),
                    entity.getBranch().getId(),
                    entity.getBranch().getName(),
                    entity.getQuantity(),
                    entity.getTotalPrice(),
                    entity.getStatus(),
                    entity.getOrderTime(),
                    pageData.getNumber()
            );
            dtoList.add(dto);
        }

        // logging
        log.info("* * * OrdersService: [HEAD] orders read info");
        log.info("request from: HEAD");
        log.info("page: {}", pageable.getPageNumber());

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(dtoList);
    }
}
