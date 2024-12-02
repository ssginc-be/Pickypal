package com.pickypal.api.stock;

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
import java.util.List;

/**
 * @author Queue-ri
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class BranchStockService {
    private final ServiceUserRepository uRepo;
    private final BranchStockRepository bsRepo;

    // page 단위로 요청자 지점의 재고를 조회하는 api
    // ex) 1 page = 1~20 row / 2 page = 21~30 row ...
    public ResponseEntity<?> getByPage(String uid, Integer pageIdx) {
        // Pageable 설정
        Pageable pageable = PageRequest.of(pageIdx, 20, Sort.by(Sort.Direction.ASC, "id"));

        // 나머지 테이블 조회
        ServiceUser user = uRepo.findById(uid).get();
        String branchId = user.getBranch().getId();
        Page<BranchStock> pageData = bsRepo.findPageByBranchId(pageable, branchId);

        // client에서 파싱 용이하도록 필요한 정보만 선별하여 dto로 변환
        BranchStockPageViewResponseDto dto = new BranchStockPageViewResponseDto(
                pageData.getContent(), pageData.getNumber()
        );

        // logging
        log.info("* * * BranchStockService: branch stock read info");
        log.info("request from: {}", branchId);
        log.info("page: {}", pageable.getPageNumber());

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(dto);
    }

    // 지점 재고 조회: 상품 id로 필터링
    public ResponseEntity<?> getByItemId(String uid, String itemId) {

        // 나머지 테이블 조회
        ServiceUser user = uRepo.findById(uid).get();
        String branchId = user.getBranch().getId();
        // 상품 id 당 재고도 한 row라서 order by나 pagination은 필요없음
        // 1개의 row가 검색되든, 검색되지 않든 리스트로 반환
        List<BranchStock> bsList = bsRepo.findAllByBranchIdAndItemId(branchId, itemId);


        // client에서 파싱 용이하도록 필요한 정보만 선별하여 dto로 변환
        BranchStockSingleViewResponseDto dto = new BranchStockSingleViewResponseDto(
                bsList
        );

        // logging
        log.info("* * * BranchStockService: branch stock read info");
        log.info("request from: {}", branchId);
        log.info("requested item_id: {}", itemId);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(dto);
    }

    // 지점 재고 조회: 상품명으로 필터링
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
        List<BranchStock> pageData = bsRepo.findAllByBranchIdAndItemName(branchId, itemName, startIdx, PAGE_SIZE);

        // client에서 파싱 용이하도록 필요한 정보만 선별하여 dto로 변환
        BranchStockPageViewResponseDto dto = new BranchStockPageViewResponseDto(
                pageData, pageIdx
        );

        // logging
        log.info("* * * BranchStockService: branch stock read info");
        log.info("request from: {}", branchId);
        log.info("page: {}", pageIdx);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(dto);
    }

    // 지점 재고 조회: 최종 수정일(=최근입고일)로 필터링
    public ResponseEntity<?> getByPageWithLastModifiedAtFilter(String uid, Integer pageIdx, String date) {
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
        List<BranchStock> pageData = bsRepo.findAllByBranchIdAndLastModifiedAt(branchId, date, nextDate, startIdx, PAGE_SIZE);

        // client에서 파싱 용이하도록 필요한 정보만 선별하여 dto로 변환
        BranchStockPageViewResponseDto dto = new BranchStockPageViewResponseDto(
                pageData, pageIdx
        );

        // logging
        log.info("* * * BranchStockService: branch stock read info");
        log.info("request from: {}", branchId);
        log.info("page: {}", pageIdx);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(dto);
    }

    // 지점 재고 조회: 유형(type): 행사상품, PL, FF로 필터링
    public ResponseEntity<?> getByPageWithTypeFilter(String uid, Integer pageIdx, String type) {
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
        List<BranchStock> pageData = bsRepo.findAllByBranchIdAndType(branchId, type, startIdx, PAGE_SIZE);

        // client에서 파싱 용이하도록 필요한 정보만 선별하여 dto로 변환
        BranchStockPageViewResponseDto dto = new BranchStockPageViewResponseDto(
                pageData, pageIdx
        );

        // logging
        log.info("* * * BranchStockService: branch stock read info");
        log.info("request from: {}", branchId);
        log.info("requested type: {}", type);
        log.info("page: {}", pageIdx);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(dto);
    }

}
