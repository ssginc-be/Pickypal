package com.pickypal.api.stock;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchStockRepository extends JpaRepository<BranchStock, String> {
}
