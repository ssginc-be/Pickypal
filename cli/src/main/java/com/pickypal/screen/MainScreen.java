package com.pickypal.screen;

import com.pickypal.dto.auth.LoginResponseDto;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Queue-ri
 */

public class MainScreen {
    
    public int start() throws IOException {

        boolean typedInvalidMenu = false;
        LoginResponseDto loginInfo = null;
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("[ 메인 화면 ]");
            if (loginInfo != null) System.out.println("* 로그인 정보: " + loginInfo.getUserName() + " / " + loginInfo.getRole());
            System.out.println("-----------------------------");

            if (loginInfo != null) System.out.println("0) 로그아웃");
            if (loginInfo == null) System.out.println("1) 로그인");
            if (loginInfo == null) System.out.println("2) 회원가입");
            if (loginInfo != null && loginInfo.getRole().equals("HEAD")) System.out.println("3) [본사] 재고 조회");
            if (loginInfo != null && loginInfo.getRole().equals("BRANCH")) System.out.println("4) [지점] 재고 조회");
            System.out.println();

            if (typedInvalidMenu)
                System.out.print("* * * 해당 메뉴가 없습니다.");

            System.out.print("* * * 메뉴 번호를 입력하세요: ");
            int menuNum = sc.nextInt();

            switch (menuNum) {
                case 0: break;
                case 1: loginInfo = LoginScreen.start(); break;
                case 2: SignUpScreen.start(); break;
                case 3: HeadStockScreen.start(loginInfo); break;
                default: typedInvalidMenu = true; break;
            }

        } // end of while
    }
}
