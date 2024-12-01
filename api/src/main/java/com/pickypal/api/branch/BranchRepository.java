package com.pickypal.api.branch;

import com.pickypal.api.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, String> {
}
