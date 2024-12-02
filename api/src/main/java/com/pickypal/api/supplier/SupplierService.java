package com.pickypal.api.supplier;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;

    /* 납품업체 조회 */
    //납품업체의 목록 조회
    public List<Supplier> getAllSuppliers() {
        List<Supplier> list = supplierRepository.findAll();
        System.out.println(list);
        return list;
    }

    //납품처명으로 조회
    public List<Supplier> getSuppliersByName(String name) {
        return supplierRepository.findByName(name);
    }

    //최종수정일로 조회
    public List<Supplier> getSuppliersByLastModifiedAt(LocalDateTime LastModifiedAt) {
        return supplierRepository.findByLastModifiedOnDate(LastModifiedAt);
    }

    //거래상태로 조회
    public List<Supplier> getSupplierByStatus(String status) {
        return supplierRepository.findByStatus(status);
    }

    /* 납품업체 등록 */
    public Supplier addSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    /* 납품업체 수정 */
    public Supplier updateById(String supplierId, Supplier UpdatedSupplier) {
        Supplier existingSupplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID: " + supplierId));

        // 업데이트: null 값이 아닌 필드만 수정
        if (UpdatedSupplier.getName() != null) {
            existingSupplier.setName(UpdatedSupplier.getName());
        }
        if (UpdatedSupplier.getStatus() != null) {
            existingSupplier.setStatus(UpdatedSupplier.getStatus());
        }
        if (UpdatedSupplier.getAddress() != null) {
            existingSupplier.setAddress(UpdatedSupplier.getAddress());
        }
        if (UpdatedSupplier.getTel() != null) {
            existingSupplier.setTel(UpdatedSupplier.getTel());
        }

        // 수정된 엔티티 저장 후 반환
        return supplierRepository.save(existingSupplier);
    }


    //납품업체 비활성화(inactive)
    public Supplier inactiveSupplier(String supplierId) {
        Supplier existingSupplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID"));
        existingSupplier.setStatus("INACTIVE");
        return supplierRepository.save(existingSupplier);
    }

    //납품업체 활성화(active)
    public Supplier activeSupplier(String supplierId) {
        Supplier existingSupplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID"));
        existingSupplier.setStatus("Active");
        return supplierRepository.save(existingSupplier);

    }
}

