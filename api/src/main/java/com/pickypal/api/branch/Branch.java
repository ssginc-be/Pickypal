package com.pickypal.api.branch;

import com.pickypal.api.item.Item;
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
public class Branch {
    @Id
    @Column(name="branch_id")
    private String id;

    private String name;

    private String address;

    @Column(columnDefinition="VARCHAR(15)")
    private String tel;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;
}
