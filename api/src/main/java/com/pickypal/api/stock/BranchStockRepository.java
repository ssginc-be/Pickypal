package com.pickypal.api.stock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BranchStockRepository extends JpaRepository<BranchStock, String> {

    // 특정 지점id에 해당하는 page 단위 전체 재고 조회
    // ex) 1 page = 1~20 row / 2 page = 21~30 row ...
    Page<BranchStock> findPageByBranchId(Pageable pageable, String branchId);

    // 특정 지점id에 해당하는 page 단위 재고 조회: 상품 id로 필터링
    List<BranchStock> findAllByBranchIdAndItemId(String branchId, String itemId);

    // 특정 지점id에 해당하는 page 단위 재고 조회: 상품명으로 필터링
    @Query(value=
            "SELECT * FROM ( " +
            "SELECT bs.*, it.supplier_id, it.name, it.type, it.price, it.tag FROM branch_stock bs " +
            "JOIN item it USING(item_id) " +
            ") res " +
            "WHERE res.branch_id = :branch_id AND res.name LIKE %:item_name% " +
            "ORDER BY stock_id ASC " +
            "LIMIT :sdx, :size;", nativeQuery=true)
    List<BranchStock> findAllByBranchIdAndItemName(@Param("branch_id") String branchId, @Param("item_name") String itemName, @Param("sdx") Integer startIdx, @Param("size") Integer size);

    // 특정 지점id에 해당하는 page 단위 재고 조회: 최종수정일(=최근입고일)로 필터링
    @Query(value=
            "SELECT * FROM branch_stock " +
            "WHERE branch_id = :branch_id AND last_modified_at >= :date AND last_modified_at < :next_date " +
            "ORDER BY stock_id ASC " +
            "LIMIT :sdx, :size;", nativeQuery=true)
    List<BranchStock> findAllByBranchIdAndLastModifiedAt(@Param("branch_id") String branchId, @Param("date") String date, @Param("next_date") String nextDate, @Param("sdx") Integer startIdx, @Param("size") Integer size);

    // 특정 지점id에 해당하는 page 단위 재고 조회: 상품 유형으로 필터링
    @Query(value=
            "SELECT * FROM ( " +
            "SELECT bs.*, it.supplier_id, it.name, it.type, it.price, it.tag FROM branch_stock bs " +
            "JOIN item it USING(item_id) " +
            ") res " +
            "WHERE res.branch_id = :branch_id AND res.type = :type " +
            "ORDER BY stock_id ASC " +
            "LIMIT :sdx, :size;", nativeQuery=true)
    List<BranchStock> findAllByBranchIdAndType(@Param("branch_id") String branchId, @Param("type") String type, @Param("sdx") Integer startIdx, @Param("size") Integer size);

    // 납품업체가 null인 행 제거
    @Modifying
    @Query(value=
            "DELETE branch_stock " +
            "FROM branch_stock " +
            "JOIN item USING(item_id) " +
            "WHERE supplier_id IS NULL;", nativeQuery=true)
    void cleanseNullSupplier();
}
