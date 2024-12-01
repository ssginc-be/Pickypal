package com.pickypal.api.supplier;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
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
public class Supplier {
    @Id
    @Column(name="supplier_id")
    private String id;

    private String name;

    @ColumnDefault("'ACTIVE'")
    private String status;

    private String address;

    @Column(columnDefinition="VARCHAR(15)")
    private String tel;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;
}
