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
public class FirebaseLoginRequestDto {
    private String email;
    private String password;
    private Boolean returnSecureToken;
}
