package com.pickypal.screen;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Queue-ri
 */

public class LoginScreen {

    public void start() throws IOException {
        Runtime.getRuntime().exec("cls"); // for Windows

        System.out.println("[ 로그인 화면 ]");
        Scanner sc = new Scanner(System.in);

        System.out.print("* 아이디: ");
        String uid = sc.nextLine();

        System.out.print("* 비밀번호: ");
        String pw = sc.nextLine();
    }
}
