package com.pickypal.screen;

import com.pickypal.util.ApiKit;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Queue-ri
 */

public class SignUpScreen {
    private ApiKit apiKit = new ApiKit();

    public void start() throws IOException {
        Runtime.getRuntime().exec("cls"); // for Windows

        System.out.println("[ 회원가입 화면 ]");
        Scanner sc = new Scanner(System.in);

        System.out.print("* 아이디: ");
        String uid = sc.nextLine();

        System.out.print("* 이름: ");
        String name = sc.nextLine();

        System.out.print("* 이메일: ");
        String email = sc.nextLine();

        System.out.print("* 비밀번호: ");
        String pw = sc.nextLine();

        signUp(uid, name, email, pw);
    }

    public void signUp(String uid, String name, String email, String pw) {
        final String SERVER = "http://localhost:8080";
        apiKit.getRequest(SERVER + "/auth/signup");
    }
}
