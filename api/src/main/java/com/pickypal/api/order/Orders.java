package com.pickypal.api.order;

import com.pickypal.api.branch.Branch;
import com.pickypal.api.item.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
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
@DynamicInsert
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="branch_id")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;

    private Integer quantity;

    private Integer price;
    
    @ColumnDefault("'출고대기'")
    private String status;

    @CreationTimestamp
    private LocalDateTime orderTime;

    private LocalDateTime outStartTime;

    private LocalDateTime inEndTime;
}
