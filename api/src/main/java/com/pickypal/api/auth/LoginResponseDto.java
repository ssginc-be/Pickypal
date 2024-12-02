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
public class LoginResponseDto {
    private String userName;
    private String accessToken; // JWT
}
