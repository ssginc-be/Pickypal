package com.pickypal.api.backdoor;

import com.pickypal.api.branch.Branch;
import com.pickypal.api.branch.BranchRepository;
import com.pickypal.api.item.Item;
import com.pickypal.api.item.ItemRepository;
import com.pickypal.api.stock.BranchStock;
import com.pickypal.api.stock.BranchStockRepository;
import com.pickypal.api.stock.HeadStock;
import com.pickypal.api.stock.HeadStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Queue-ri
 */

@Service
@RequiredArgsConstructor
public class BackdoorService {
    private final ItemRepository iRepo;
    private final HeadStockRepository hsRepo;
    private final BranchStockRepository bsRepo;
    private final BranchRepository bRepo;

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

    // 출고 승인된 상품을 배송하는 가상 api
    public void shipItem() {

    }

    // 지점의 상품을 판매 처리하는 가상 api
}
