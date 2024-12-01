package com.pickypal.api.supplier;

import com.pickypal.api.branch.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, String> {
}
