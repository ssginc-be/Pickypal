package com.pickypal.api.incoming;

import com.pickypal.api.branch.Branch;
import com.pickypal.api.item.Item;
import com.pickypal.api.order.Orders;
import com.pickypal.api.order.OrdersViewResponseDto;
import com.pickypal.api.user.ServiceUser;
import com.pickypal.api.user.ServiceUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BranchIncomingItemService {
    private final ServiceUserRepository uRepo;
    private final BranchIncomingItemRepository biRepo;

    /* 입고 내역 조회 */
    // 지점의 입고 내역 조회 (최신순 정렬)
    public ResponseEntity<?> getByPage(String uid, Integer pageIdx) {
        // Pageable 설정
        Pageable pageable = PageRequest.of(pageIdx, 20, Sort.by(Sort.Direction.DESC, "inTime"));

        // 나머지 테이블 조회
        ServiceUser user = uRepo.findById(uid).get();
        String branchId = user.getBranch().getId();
        Page<BranchIncomingItem> pageData = biRepo.findPageByBranchId(pageable, branchId);

        // client에서 파싱 용이하도록 필요한 정보만 선별하여 dto로 변환
        List<BranchIncomingItemResponseDto> dtoList = new ArrayList<>();
        List<BranchIncomingItem> entityList = pageData.getContent();

        for (BranchIncomingItem entity : entityList) {
            Item item = entity.getItem();
            BranchIncomingItemResponseDto dto = new BranchIncomingItemResponseDto(
                    entity.getId(),
                    item.getSupplier().getId(),
                    item.getSupplier().getName(),
                    item.getId(),
                    item.getName(),
                    entity.getQuantity(),
                    entity.getInTime()
            );
            dtoList.add(dto);
        }

        // logging
        log.info("* * * BranchIncomingItemService: branch incoming read info");
        log.info("request from: {}", branchId);
        log.info("page: {}", pageable.getPageNumber());

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(dtoList);
    }

    // 상품 id로 조회
    public ResponseEntity<?> getByPageWithItemIdFilter(String uid, Integer pageIdx, String itemId) {
        // Pageable 설정
        Pageable pageable = PageRequest.of(pageIdx, 20, Sort.by(Sort.Direction.DESC, "inTime"));

        // 나머지 테이블 조회
        ServiceUser user = uRepo.findById(uid).get();
        String branchId = user.getBranch().getId();
        Page<BranchIncomingItem> pageData = biRepo.findAllByBranchIdAndItemId(pageable, branchId, itemId);

        // client에서 파싱 용이하도록 필요한 정보만 선별하여 dto로 변환
        List<BranchIncomingItemResponseDto> dtoList = new ArrayList<>();
        List<BranchIncomingItem> entityList = pageData.getContent();

        for (BranchIncomingItem entity : entityList) {
            Item item = entity.getItem();
            BranchIncomingItemResponseDto dto = new BranchIncomingItemResponseDto(
                    entity.getId(),
                    item.getSupplier().getId(),
                    item.getSupplier().getName(),
                    item.getId(),
                    item.getName(),
                    entity.getQuantity(),
                    entity.getInTime()
            );
            dtoList.add(dto);
        }

        // logging
        log.info("* * * BranchIncomingItemService: branch incoming read info");
        log.info("request from: {}", branchId);
        log.info("page: {}", pageable.getPageNumber());

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(dtoList);
    }

    // 상품명으로 조회
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
        List<BranchIncomingItem> pageData = biRepo.findAllByBranchIdAndItemName(branchId, itemName, startIdx, PAGE_SIZE);

        // client에서 파싱 용이하도록 필요한 정보만 선별하여 dto로 변환
        List<BranchIncomingItemResponseDto> dtoList = new ArrayList<>();

        for (BranchIncomingItem entity : pageData) {
            Item item = entity.getItem();
            BranchIncomingItemResponseDto dto = new BranchIncomingItemResponseDto(
                    entity.getId(),
                    item.getSupplier().getId(),
                    item.getSupplier().getName(),
                    item.getId(),
                    item.getName(),
                    entity.getQuantity(),
                    entity.getInTime()
            );
            dtoList.add(dto);
        }

        // logging
        log.info("* * * BranchIncomingItemService: branch incoming read info");
        log.info("request from: {}", branchId);
        log.info("page: {}", pageIdx);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(dtoList);
    }

    // 입고 등록일시로 조회
    public ResponseEntity<?> getByPageWithInTimeFilter(String uid, Integer pageIdx, String date) {
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
        List<BranchIncomingItem> pageData = biRepo.findAllByBranchIdAndInTime(branchId, date, nextDate, startIdx, PAGE_SIZE);

        // client에서 파싱 용이하도록 필요한 정보만 선별하여 dto로 변환
        List<BranchIncomingItemResponseDto> dtoList = new ArrayList<>();

        for (BranchIncomingItem entity : pageData) {
            Item item = entity.getItem();
            BranchIncomingItemResponseDto dto = new BranchIncomingItemResponseDto(
                    entity.getId(),
                    item.getSupplier().getId(),
                    item.getSupplier().getName(),
                    item.getId(),
                    item.getName(),
                    entity.getQuantity(),
                    entity.getInTime()
            );
            dtoList.add(dto);
        }

        // logging
        log.info("* * * BranchIncomingItemService: branch incoming read info");
        log.info("request from: {}", branchId);
        log.info("page: {}", pageIdx);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(dtoList);
    }

    /* 입고 내역 등록 */
    // api로 구현하지 않음
    // backdoor api를 통해 배송 완료 처리하면, 시스템 내부적으로 입고 내역 등록 함수를 자동 호출하기 때문
    public void saveIncomingLog(Orders order) {
        Branch branch = order.getBranch();

        BranchIncomingItem bi = new BranchIncomingItem();
        bi.setId(branch.getId() + "_" + order.getId());
        bi.setBranch(branch);
        bi.setItem(order.getItem());
        bi.setQuantity(order.getQuantity());

        biRepo.save(bi);
    }
}

