package com.pickypal.api.incoming;

import com.pickypal.api.branch.Branch;
import com.pickypal.api.order.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchIncomingItemService {
    private final BranchIncomingItemRepository biRepo;

    /* 입고 내역 조회 */
    // 지점의 입고 내역 조회 (최신순 정렬)
    public List<BranchIncomingItemResponseDto> getAll(String branchId) {
        List<BranchIncomingItem> voList = biRepo.findAllByBranchIdOrderByInTimeDesc(branchId);
        List<BranchIncomingItemResponseDto> dtoList = new ArrayList<BranchIncomingItemResponseDto>();

        for (BranchIncomingItem bi : voList) {
            dtoList.add(BranchIncomingItemResponseDto.of(bi));
        }

        return dtoList;
    }

    // 상품 id로 조회
    public List<BranchIncomingItemResponseDto> getAllByItemId(String branchId, String itemId) {
        List<BranchIncomingItem> voList = biRepo.findAllByBranchIdAndItemId(branchId, itemId);
        List<BranchIncomingItemResponseDto> dtoList = new ArrayList<BranchIncomingItemResponseDto>();

        for (BranchIncomingItem bi : voList) {
            dtoList.add(BranchIncomingItemResponseDto.of(bi));
        }

        return dtoList;
    }

    // 상품명으로 조회
    public List<BranchIncomingItemResponseDto> getAllByItemName(String branchId, String itemName) {
        List<BranchIncomingItem> voList = biRepo.findAllByBranchIdAndItemNameLike(branchId, itemName);
        List<BranchIncomingItemResponseDto> dtoList = new ArrayList<BranchIncomingItemResponseDto>();

        for (BranchIncomingItem bi : voList) {
            dtoList.add(BranchIncomingItemResponseDto.of(bi));
        }

        return dtoList;
    }

    // 입고 등록일시로 조회
    public List<BranchIncomingItemResponseDto> getAllByInTime(String branchId, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(date, formatter);
        LocalDate parsedNextDate = parsedDate.plusDays(1);
        String nextDate = parsedNextDate.toString();
        List<BranchIncomingItem> voList = biRepo.findAllByBranchIdAndInTime(branchId, date, nextDate);
        List<BranchIncomingItemResponseDto> dtoList = new ArrayList<BranchIncomingItemResponseDto>();

        for (BranchIncomingItem bi : voList) {
            dtoList.add(BranchIncomingItemResponseDto.of(bi));
        }

        return dtoList;
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

