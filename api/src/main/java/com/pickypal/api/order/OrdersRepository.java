package com.pickypal.api.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    // 특정 지점id에 해당하는 page 단위 발주 내역 조회
    // ex) 1 page = 1~20 row / 2 page = 21~30 row ...
    Page<Orders> findPageBy(Pageable pageable);

    // 특정 지점id에 해당하는 발주 내역 삭제
    @Modifying
    @Query(value=
            "DELETE FROM orders " +
            "WHERE order_id = :order_id AND branch_id = :branch_id;", nativeQuery=true)
    void deleteByIdAndBranchId(@Param("order_id") Long orderId, @Param("branch_id") String branchId);
}
