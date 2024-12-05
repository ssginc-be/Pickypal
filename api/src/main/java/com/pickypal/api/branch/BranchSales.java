package com.pickypal.api.branch;

import com.pickypal.api.item.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * @author Queue-ri
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class BranchSales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sales_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="branch_id")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;

    @CreationTimestamp
    private LocalDateTime soldAt;
}
