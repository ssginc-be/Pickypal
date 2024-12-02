package com.pickypal.api.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickypal.api.branch.Branch;
import com.pickypal.api.branch.BranchRepository;
import com.pickypal.api.user.ServiceUser;
import com.pickypal.api.user.ServiceUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * @author Queue-ri
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${secret.firebase.api-key}")
    private String FIREBASE_API_KEY;

    private final ServiceUserRepository suRepo;
    private final BranchRepository bRepo;
    private final JwtKit jwtKit;

    // 로그인 api
    public ResponseEntity<?> login(LoginRequestDto dto) throws JsonProcessingException, URISyntaxException {
        ServiceUser user;
        try {
            user = suRepo.findById(dto.getUserId()).get();
        } catch (Exception e) {
            log.error("* * * AuthController: login failed - could not find user");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Firebase REST API 호출
        URI url = new URI("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + FIREBASE_API_KEY);
        RestTemplate restTemplate = new RestTemplate();
        // header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // request body
        FirebaseLoginRequestDto fbDto = new FirebaseLoginRequestDto(user.getEmail(), dto.getPassword(), true);
        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(fbDto);
        // request & get response from Firebase
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        FirebaseLoginResponseDto responseDto = mapper.readValue(response.getBody(), FirebaseLoginResponseDto.class);

        log.info("* * * AuthController: login done");
        log.info("idToken: " + responseDto.getIdToken());
        log.info("email: " + responseDto.getEmail());
        log.info("refreshToken: " + responseDto.getRefreshToken());
        log.info("expiresIn: " + responseDto.getExpiresIn());
        log.info("localId: " + responseDto.getLocalId());
        log.info("registered: " + responseDto.getRegistered());

        String accessToken = jwtKit.generateAccessToken(user.getId(), user.getRole());

        return ResponseEntity
                .status(HttpStatus.ACCEPTED.value())
                .body(new LoginResponseDto(user.getName(), accessToken));
    }

    // 회원가입 api
    public ResponseEntity<?> signup(SignUpRequestDto dto) throws JsonProcessingException, URISyntaxException {
        // 기존 사용자와 id 겹치는지 확인
        Optional<ServiceUser> optUser = suRepo.findById(dto.getUserId());
        if (optUser.isPresent()) {
            log.error("* * * AuthService: signup failed - Same user id exists");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // 기존 사용자와 email 겹치는지 확인
        optUser = suRepo.findByEmail(dto.getEmail());
        if (optUser.isPresent()) {
            log.error("* * * AuthService: signup failed - Same email exists");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Firebase REST API 호출
        URI url = new URI("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + FIREBASE_API_KEY);
        RestTemplate restTemplate = new RestTemplate();
        // header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // request body
        FirebaseSignUpRequestDto fbDto = new FirebaseSignUpRequestDto(dto.getEmail(), dto.getPassword(), true);
        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(fbDto);
        // request & get response from Firebase
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        FirebaseSignUpResponseDto responseDto = mapper.readValue(response.getBody(), FirebaseSignUpResponseDto.class);

        log.info("* * * AuthController: firebase signup done");
        log.info("kind: " + responseDto.getKind());
        log.info("idToken: " + responseDto.getIdToken());
        log.info("email: " + responseDto.getEmail());
        log.info("refreshToken: " + responseDto.getRefreshToken());
        log.info("expiresIn: " + responseDto.getExpiresIn());
        log.info("localId: " + responseDto.getLocalId());

        // MySQL에 사용자 정보 저장
        ServiceUser user = new ServiceUser();
        user.setId(dto.getUserId());
        user.setName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());

        Branch branch;
        if (dto.getRole().equals("BRANCH")) {
            try {
                branch = bRepo.findById(dto.getBranchId()).get();
            } catch (Exception e) {
                log.error("* * * AuthService: signup failed - branch id not found");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        else if (dto.getRole().equals("HEAD")) {
            branch = null;
        }
        else {
            log.error("* * * AuthService: signup failed - invalid role value");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        user.setBranch(branch);

        suRepo.save(user);
        log.info("* * * AuthController: backend signup done");

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
