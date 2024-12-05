package com.pickypal.screen;

import com.pickypal.dto.auth.LoginResponseDto;
import com.pickypal.screen.head.HeadOutgoingManageScreen;
import com.pickypal.util.Console;

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
            Console.clear();

            System.out.println("########  ####  ######  ##    ## ##    ## ########     ###    ##");
            System.out.println("##     ##  ##  ##    ## ##   ##   ##  ##  ##     ##   ## ##   ##");
            System.out.println("##     ##  ##  ##       ##  ##     ####   ##     ##  ##   ##  ##");
            System.out.println("########   ##  ##       #####       ##    ########  ##     ## ##");
            System.out.println("##         ##  ##       ##  ##      ##    ##        ######### ##");
            System.out.println("##         ##  ##    ## ##   ##     ##    ##        ##     ## ##");
            System.out.println("##        ####  ######  ##    ##    ##    ##        ##     ## ########");
            System.out.println();
            System.out.println("--------------------------------------------");

            System.out.println("[ 메인 화면 ]");
            if (loginInfo != null) System.out.println("* 로그인 정보: " + loginInfo.getUserName() + " / 권한(" + loginInfo.getRole() + ")");
            System.out.println("--------------------------------------------");

            if (loginInfo != null) System.out.println("0) 로그아웃");
            if (loginInfo == null) System.out.println("1) 로그인");
            if (loginInfo == null) System.out.println("2) 회원가입");
            if (loginInfo != null && loginInfo.getRole().equals("HEAD")) System.out.println("3) [본사] 재고 조회");
            if (loginInfo != null && loginInfo.getRole().equals("BRANCH")) System.out.println("4) [지점] 재고 조회");
            if (loginInfo != null && loginInfo.getRole().equals("BRANCH")) System.out.println("5) [지점] 입고 조회");
            if (loginInfo != null && loginInfo.getRole().equals("BRANCH")) System.out.println("6) [지점] 발주 관리");
            if (loginInfo != null && loginInfo.getRole().equals("HEAD")) System.out.println("7) [본사] 출고 관리");
            if (loginInfo != null) System.out.println("8) [가상] 배송 처리");
            System.out.println();

            if (typedInvalidMenu)
                System.out.print("* * * 해당 메뉴가 없습니다.");

            System.out.print("* * * 메뉴 번호를 입력하세요: ");
            int menuNum = sc.nextInt();

            switch (menuNum) {
                case 0: loginInfo = null; break;
                case 1: typedInvalidMenu = false; loginInfo = LoginScreen.start(); break;
                case 2: typedInvalidMenu = false; SignUpScreen.start(); break;
                case 3: typedInvalidMenu = false; HeadStockScreen.start(loginInfo); break;
                case 4: typedInvalidMenu = false; BranchStockScreen.start(loginInfo); break;
                case 5: typedInvalidMenu = false; BranchIncomingScreen.start(loginInfo); break;
                case 6: typedInvalidMenu = false; BranchOrderFinalScreen.start(loginInfo); break;
                case 7: typedInvalidMenu = false; HeadOutgoingScreen.start(loginInfo); break;
                case 8: typedInvalidMenu = false; BackdoorScreen.start(loginInfo); break;
                default: typedInvalidMenu = true; break;
            }

        } // end of while
    }
}
