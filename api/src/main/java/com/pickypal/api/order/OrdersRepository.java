package com.pickypal.api.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    // 특정 지점id에 해당하는 page 단위 발주 내역 조회
    // ex) 1 page = 1~20 row / 2 page = 21~30 row ...
    Page<Orders> findPageBy(Pageable pageable);
}
