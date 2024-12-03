package com.pickypal.api.supplier;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, String> {
    //납품처명으로 조회
    List<Supplier> findAllByNameContains(String name);

    //최종 수정일로 조회
    @Query(value =
            "SELECT * FROM Supplier s WHERE s.lastModifiedAt >= :startDate AND s.lastModifiedAt < :endDate;"
            , nativeQuery=true)
    List<Supplier> findAllByLastModifiedOnDate(@Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);

    // 거래 상태로 조회
    List<Supplier> findAllByStatus(String status);
}

