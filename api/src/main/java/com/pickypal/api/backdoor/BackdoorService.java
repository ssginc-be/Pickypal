package com.pickypal.api.backdoor;

import com.pickypal.api.branch.Branch;
import com.pickypal.api.branch.BranchRepository;
import com.pickypal.api.incoming.BranchIncomingItem;
import com.pickypal.api.incoming.BranchIncomingItemRepository;
import com.pickypal.api.item.Item;
import com.pickypal.api.item.ItemRepository;
import com.pickypal.api.order.Orders;
import com.pickypal.api.order.OrdersRepository;
import com.pickypal.api.outgoing.HeadOutgoingItem;
import com.pickypal.api.outgoing.HeadOutgoingItemRepository;
import com.pickypal.api.stock.BranchStock;
import com.pickypal.api.stock.BranchStockRepository;
import com.pickypal.api.stock.HeadStock;
import com.pickypal.api.stock.HeadStockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class BackdoorService {
    private final ItemRepository iRepo;
    private final HeadStockRepository hsRepo;
    private final HeadOutgoingItemRepository hoRepo;
    private final BranchStockRepository bsRepo;
    private final BranchIncomingItemRepository biRepo;
    private final BranchRepository bRepo;
    private final OrdersRepository oRepo;

    // 본사의 재고를 모든 상품에 대하여 99개로 세팅하는 가상 api
    public void setHeadStock99() {
        List<Item> itemList = iRepo.findAll();
        List<HeadStock> hsList = new ArrayList<HeadStock>();

        Long stockNo = 1L;
        for (Item item : itemList) {
            String id = "HS" + String.format("%05d", stockNo);
            hsList.add(HeadStock.of(id, item));
            ++stockNo;
        }

        hsRepo.saveAll(hsList);
    }

    // 특정 지점의 재고를 모든 상품에 대하여 0~20개 사이로 세팅하는 가상 api
    public void setBranchStockBetween0And20(String branchId) {
        List<Item> itemList = iRepo.findAll();
        List<BranchStock> bsList = new ArrayList<BranchStock>();

        Long stockNo = 1L;
        for (Item item : itemList) {
            String id = branchId + "_" + String.format("%05d", stockNo);
            Branch branch = bRepo.findById(branchId).get();
            Integer stock = (int)(Math.random() * 21); // random generate 0~20

            bsList.add(BranchStock.of(id, item, branch, stock));
            ++stockNo;
        }

        bsRepo.saveAll(bsList);
    }

    /* 출고 승인된 상품을 배송하는 가상 api */
    // 1. 발주 id에 해당하는 발주의 status를 '입고완료'로 변경
    // 2. 발주 id에 해당하는 발주의 inEndTime을 now()로 변경
    // 3. 발주 id에 해당하는 본사 출고내역의 status를 '출고완료'로 변경
    // 4. 발주 id에 해당하는 본사 출고내역의 outEndTime을 now()로 변경
    // 5. 발주 id에 해당하는 지점의 입고내역 등록
    // 6. 발주 id에 해당하는 지점의 해당 상품 재고를 추가
    @Transactional
    public void shipItem(Long oid) {
        // 1. 발주 id에 해당하는 발주의 status를 '입고완료'로 변경
        Orders order = oRepo.findById(oid).get();
        order.setStatus("입고완료");

        // 2. 발주 id에 해당하는 발주의 inEndTime을 now()로 변경
        order.setInEndTime(LocalDateTime.now());
        oRepo.save(order);

        // 3. 발주 id에 해당하는 본사 출고내역의 status를 '출고완료'로 변경
        HeadOutgoingItem ho = hoRepo.findByOrders(order).get();
        ho.setStatus("출고완료");

        // 4. 발주 id에 해당하는 본사 출고내역의 outEndTime을 now()로 변경
        ho.setOutEndTime(LocalDateTime.now());
        hoRepo.save(ho);

        // 5. 발주 id에 해당하는 지점의 입고내역 등록
        BranchIncomingItem bi = new BranchIncomingItem();
        bi.setId(order.getBranch().getId() + "_" + order.getId()); // 지점id_발주id 형식
        bi.setBranch(order.getBranch());
        bi.setItem(order.getItem());
        bi.setQuantity(order.getQuantity());
        bi.setInTime(LocalDateTime.now());
        biRepo.save(bi);

        // 6. 발주 id에 해당하는 지점의 해당 상품 재고를 추가
        List<BranchStock> bsList = bsRepo.findAllByBranchIdAndItemId(order.getBranch().getId(), order.getItem().getId());
        
        if (bsList.isEmpty()) { // 최초 row 추가
            Branch b = order.getBranch();
            Item i = order.getItem();
            BranchStock bs = new BranchStock();
            bs.setId(b.getId() + "_" + i.getId());
            bs.setBranch(b);
            bs.setItem(i);
            bs.setStock(order.getQuantity());
            bsRepo.save(bs);
        }
        else { // 이미 row 있어서 조회됐으면 ++
            BranchStock bs = bsList.get(0);
            Integer stock = bs.getStock();
            bs.setStock(stock + order.getQuantity());
            bsRepo.save(bs);
        }

        log.info("* * * BackdoorService: order ship done.");
        log.info("requested order id: {}", oid);
    }

    // 납품업체 테이블에 정보가 없는 상품 row 모두 제거
    // DeleteMapping이 맞는데 테스트용이라서 Get으로 구현함
    @Transactional
    public void cleanseStockData() {
        hsRepo.cleanseNullSupplier();
        bsRepo.cleanseNullSupplier();
        log.info("* * * BackdoorService: Stock tables cleanse done.");
    }

    // 지점의 상품을 판매 처리하는 가상 api
}
