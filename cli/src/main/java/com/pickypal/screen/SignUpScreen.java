package com.pickypal.screen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickypal.dto.auth.LoginRequestDto;
import com.pickypal.dto.auth.LoginResponseDto;
import com.pickypal.dto.auth.SignUpRequestDto;
import com.pickypal.util.ApiKit;
import com.pickypal.util.ApiResponse;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Queue-ri
 */

public class SignUpScreen {

    public static void start() throws IOException {
        //Runtime.getRuntime().exec("cls"); // for Windows

        System.out.println("[ 회원가입 화면 ]");
        Scanner sc = new Scanner(System.in);

        System.out.print("* 아이디: ");
        String uid = sc.nextLine();

        System.out.print("* 이름: ");
        String name = sc.nextLine();

        System.out.print("* 이메일: ");
        String email = sc.nextLine();

        String pw = "";
        boolean invalidPw = true;
        while(invalidPw) {
            System.out.print("* 비밀번호: ");
            String input = sc.nextLine();
            if (input.length() > 5) {
                invalidPw = false;
                pw = input;
            }
            else {
                System.out.println("* * * 패스워드는 6자리 이상이어야 합니다.");
            }
        }

        System.out.print("* * * 지점주로 가입하시겠습니까? (y/n): ");
        String option = sc.nextLine().toLowerCase();

        String branchId, role;
        if (option.equals("y")) {
            System.out.print("* 지점 코드: ");
            branchId = sc.nextLine();
            role = "BRANCH";
        }
        else {
            branchId = null;
            role = "HEAD";
        }

        signUp(uid, name, email, pw, role, branchId);
    }

    public static void signUp(String uid, String name, String email, String pw, String role, String bid) {
        ApiKit apiKit = new ApiKit();
        SignUpRequestDto reqDto = new SignUpRequestDto(uid, name, email, pw, role, bid);
        ApiResponse response = apiKit.postRequest("http://localhost:8080/auth/signup", reqDto);
    }
}
