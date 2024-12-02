package com.pickypal.api.supplier;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/supplier")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    /* 납품업체 조회 */
    //납품업체의 목록 조회
    @GetMapping
    public List<Supplier> getAllSuppliers(){
        return supplierService.getAllSuppliers();
    }

    //납품처명으로 조회
    @GetMapping("/supplier_name")
    public List<Supplier> getSupplierByName(@RequestParam String name){
        return supplierService.getSuppliersByName(name);
    }

    //최종수정일로 조회
    @GetMapping("/supplier_lastModifiedAt")
    public List<Supplier> getSupplierByLastModifiedAt(@RequestParam LocalDateTime lastModifiedAt){
        return supplierService.getSuppliersByLastModifiedAt(lastModifiedAt);
    }

    //거래상태로 조회
    @GetMapping("/supplier_status")
    public List<Supplier> getSupplierByStatus(@RequestParam String status) {
        return supplierService.getSupplierByStatus(status);
    }

    // 납품업체 등록
    @PostMapping
    public Supplier addSupplier(@RequestBody Supplier supplier){
        return supplierService.addSupplier(supplier);
    }

    /* 납품업체 수정 */

    @PutMapping("/{id}")
    public Supplier updateSupplier(
            @PathVariable String id,
            @RequestBody Supplier updatedSupplier) {
        return supplierService.updateById(id, updatedSupplier);
    }


    // 납품업체 비활성화
    @PutMapping("/supplierId/inactiveSupplier")
    public Supplier inactiveSupplier(@PathVariable String supplierId) {
        return supplierService.inactiveSupplier(supplierId);
    }

    // 납품업체 활성화
    @PutMapping("/supplierId/activate")
    public Supplier activateSupplier(@PathVariable String supplierId) {
        return supplierService.activeSupplier(supplierId);
    }
}


