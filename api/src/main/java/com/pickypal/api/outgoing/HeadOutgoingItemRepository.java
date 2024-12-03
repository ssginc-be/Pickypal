package com.pickypal.api.outgoing;

import com.pickypal.api.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeadOutgoingItemRepository extends JpaRepository<HeadOutgoingItem, Long> {

    Optional<HeadOutgoingItem> findByOrders(Orders order);

}
