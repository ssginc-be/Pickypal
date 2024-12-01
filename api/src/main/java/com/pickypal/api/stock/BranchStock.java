package com.pickypal.api.stock;

import com.pickypal.api.branch.Branch;
import com.pickypal.api.item.Item;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * @author Queue-ri
 */

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchStock {
    @Id
    @Column(name="stock_id")
    private String id;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name="branch_id")
    private Branch branch;

    private Integer stock;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    public static BranchStock of(String id, Item item, Branch branch, Integer stock) {
        return BranchStock.builder()
                .id(id)
                .item(item)
                .branch(branch)
                .stock(stock)
                .lastModifiedAt(LocalDateTime.now())
                .build();
    }
}
