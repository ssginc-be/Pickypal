package com.pickypal.api.user;

import com.pickypal.api.branch.Branch;
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
public class ServiceUser {
    @Id
    @Column(name="user_id")
    private String id;

    @Column(name="user_name")
    private String name;

    private String email;

    @Column(columnDefinition="VARCHAR(10)")
    private String role;

    @OneToOne
    @JoinColumn(name="branch_id")
    private Branch branch; // nullable if role is 'HEAD'

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;
}
