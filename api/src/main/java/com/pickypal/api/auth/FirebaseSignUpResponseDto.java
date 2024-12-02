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
public class FirebaseSignUpResponseDto {
    private String kind;
    private String idToken;
    private String email;
    private String refreshToken;
    private String expiresIn;
    private String localId;
}
