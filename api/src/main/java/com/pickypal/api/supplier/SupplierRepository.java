package com.pickypal.api.supplier;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, String> {

    Optional<Supplier> findById(String id);

    //납품업체명으로 조회
    List<Supplier> findByName(String name);

    //최종 수정일로 조회
    @Query(value =
            "SELECT s FROM Supplier s WHERE s.lastModifiedAt >= :date AND s.lastModifiedAt < :next_date")
    List<Supplier> findByLastModifiedOnDate (@Param("date") LocalDateTime date
    );

    // 거래 상태로 조회
    List<Supplier> findByStatus(String status);

}

