package com.pickypal.api.stock;

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
public class HeadStock {
    @Id
    @Column(name="stock_id")
    private String id;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;

    private Integer stock;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    public static HeadStock of(String id, Item item) {
        return HeadStock.builder()
                .id(id)
                .item(item)
                .stock(99)
                .lastModifiedAt(LocalDateTime.now())
                .build();
    }
}
