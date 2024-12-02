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

    // 특정 지점id에 해당하는 page 단위 발주 내역 조회: 상품 id로 필터링
    Page<Orders> findAllByBranchIdAndItemId(Pageable pageable, String branchId, String itemId);

    // 특정 지점id에 해당하는 page 단위 발주 내역 조회: 상품명으로 필터링
    @Query(value=
            "SELECT * FROM ( " +
            "SELECT * FROM orders o " +
            "JOIN item it USING(item_id) " +
            ") res " +
            "WHERE res.branch_id = :branch_id AND res.name LIKE %:item_name% " +
            "ORDER BY order_time DESC " +
            "LIMIT :sdx, :size;", nativeQuery=true)
    List<Orders> findAllByBranchIdAndItemName(@Param("branch_id") String branchId, @Param("item_name") String itemName, @Param("sdx") Integer startIdx, @Param("size") Integer size);

    // 특정 지점id에 해당하는 page 단위 발주 내역 조회: 발주일로 필터링
    @Query(value=
            "SELECT * FROM orders " +
            "WHERE branch_id = :branch_id AND order_time >= :date AND order_time < :next_date " +
            "ORDER BY order_time DESC " +
            "LIMIT :sdx, :size;", nativeQuery=true)
    List<Orders> findAllByBranchIdAndOrderTime(@Param("branch_id") String branchId, @Param("date") String date, @Param("next_date") String nextDate, @Param("sdx") Integer startIdx, @Param("size") Integer size);

    // 특정 지점id에 해당하는 발주 내역 삭제
    // native query 상에서 pagination 처리
    @Modifying
    @Query(value=
            "DELETE FROM orders " +
            "WHERE order_id = :order_id AND branch_id = :branch_id;", nativeQuery=true)
    void deleteByIdAndBranchId(@Param("order_id") Long orderId, @Param("branch_id") String branchId);
}
