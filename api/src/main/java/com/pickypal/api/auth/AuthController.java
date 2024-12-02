package com.pickypal.api.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

/**
 * @author Queue-ri
 */

@RestController
@RequestMapping(value="/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    // 로그인 api
    // client로부터 이메일, 비밀번호를 받아 Firebase에 검증받은 후 JWT 토큰 반환
    // RT 발급은 개발 기간 상 생략되어 있음
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) throws JsonProcessingException, URISyntaxException {
        return service.login(dto);
    }

    // 로그아웃 api
    // 요청자의 AT를 만료시킴
    @GetMapping("/logout")
    public void logout(@RequestParam("token") String token) {
        //service.logout(token);
    }

    // 회원가입 api
    // Firebase에 등록함
    @PostMapping("/signup")
    public void signup(@RequestBody SignUpRequestDto dto) throws JsonProcessingException, URISyntaxException {
        service.signup(dto);
    }
}
