package com.pickypal.api.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Queue-ri
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
    private String userId; // PK
    private String userName;
    private String email;
    private String password;
    private String role;
    private String branchId; // nullable
}
