package com.pickypal.api.item;

import com.pickypal.api.supplier.Supplier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class Item {
    @Id
    @Column(name="item_id")
    private String id;

    @ManyToOne
    @JoinColumn(name="supplier_id")
    private Supplier supplier;

    private String name;

    private String type;

    private Integer price;

    private String tag;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;
}
