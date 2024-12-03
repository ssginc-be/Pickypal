package com.pickypal.screen;

import java.util.Scanner;

/**
 * @author Queue-ri
 */

public class MainScreen {
    
    public int start() {

        boolean typedInvalidMenu = false;
        while (true) {

            System.out.println("[ 메인 화면 ]");
            Scanner sc = new Scanner(System.in);

            System.out.println("1) 로그인");
            System.out.println("2) 회원가입");
            System.out.println();

            if (typedInvalidMenu)
                System.out.print("* * * 해당 메뉴가 없습니다.");

            System.out.println("* * * 메뉴 번호를 입력하세요: ");
            int menuNum = sc.nextInt();

            if (menuNum == 1) {
                return menuNum;
            }
            else if (menuNum == 2) {

            }
            else {
                typedInvalidMenu = true;
            }

        } // end of while
    }
}
