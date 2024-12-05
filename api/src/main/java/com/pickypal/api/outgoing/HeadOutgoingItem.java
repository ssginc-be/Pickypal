package com.pickypal.api.outgoing;

import com.pickypal.api.order.Orders;
import com.pickypal.api.supplier.Supplier;
import com.pickypal.api.user.ServiceUser;
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
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class HeadOutgoingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="outgoing_id")
    private Long id;

    @OneToOne
    @JoinColumn(name="order_id")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name="supplier_id")
    private Supplier supplier;

    @ColumnDefault("'출고중'")
    private String status;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name="user_id")
    private ServiceUser committer;

    @CreationTimestamp
    private LocalDateTime outStartTime;

    private LocalDateTime outEndTime;
}
