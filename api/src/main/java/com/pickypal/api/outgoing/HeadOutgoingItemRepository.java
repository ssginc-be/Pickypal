package com.pickypal.api.outgoing;

import com.pickypal.api.order.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeadOutgoingItemRepository extends JpaRepository<HeadOutgoingItem, Long> {

    // 본사: 발주로 출고 내역 조회 (발주ID로 조회)
    Optional<HeadOutgoingItem> findByOrders(Orders order);

    // 본사: page size 20으로 출고 내역 조회
    Page<HeadOutgoingItem> findPageBy(Pageable pageable);

}
