package com.pickypal.api.supplier;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractAuditable_.lastModifiedDate;


@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;

    /* 납품업체 조회 */
    //납품업체의 목록 조회
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    //납품처명으로 조회
    public List<Supplier> getSuppliersByName(String name) {
        return supplierRepository.findAllByNameContains(name);
    }

    //최종수정일로 조회
    public List<Supplier> getSuppliersByLastModifiedAt(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(date, formatter);
        LocalDateTime startOfDay = parsedDate.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return supplierRepository.findAllByLastModifiedOnDate(startOfDay, endOfDay);
    }

    //거래상태로 조회
    public List<Supplier> getSupplierByStatus(String status) {
        return supplierRepository.findAllByStatus(status.toUpperCase());
    }
}


