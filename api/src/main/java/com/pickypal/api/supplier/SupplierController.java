package com.pickypal.api.supplier;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/supplier")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    /* 납품업체 조회 */
    //납품업체의 목록 조회
    @GetMapping
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    //납품처명으로 조회
    @GetMapping("/name")
    public List<Supplier> getSupplierByName(@RequestParam("name") String name) {
        return supplierService.getSuppliersByName(name);
    }

    //최종수정일로 조회
    @GetMapping("/lastModifiedAt")
    public List<Supplier> getSupplierByLastModifiedAt(@RequestParam("lastModifiedAt") String lastModifiedAt) {
        return supplierService.getSuppliersByLastModifiedAt(lastModifiedAt);
    }

    //거래상태로 조회
    @GetMapping("/status")
    public List<Supplier> getSupplierByStatus(@RequestParam("status") String status) {
        return supplierService.getSupplierByStatus(status);
    }
}


