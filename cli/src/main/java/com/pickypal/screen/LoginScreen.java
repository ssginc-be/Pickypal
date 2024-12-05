package com.pickypal.screen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickypal.dto.auth.LoginRequestDto;
import com.pickypal.dto.auth.LoginResponseDto;
import com.pickypal.util.ApiKit;
import com.pickypal.util.ApiResponse;
import com.pickypal.util.Console;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Queue-ri
 */

public class LoginScreen {

    public static LoginResponseDto start() throws IOException {
        //Runtime.getRuntime().exec("cls"); // for Windows
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
        System.out.println("[ 로그인 화면 ]");
        System.out.println("--------------------------------------------");
        Scanner sc = new Scanner(System.in);

        System.out.print("* 아이디: ");
        String uid = sc.nextLine();

        System.out.print("* 비밀번호: ");
        String pw = sc.nextLine();

        return login(uid, pw);
    }

    public static LoginResponseDto login(String uid, String pw) throws JsonProcessingException {
        ApiKit apiKit = new ApiKit();
        LoginRequestDto reqDto = new LoginRequestDto(uid, pw);
        System.out.println("--------------------------------------------");
        System.out.println("* * * 로그인 중입니다. 잠시만 기다려주세요.");
        ApiResponse response = apiKit.postRequest("http://localhost:8080/auth/login", reqDto);

        ObjectMapper mapper = new ObjectMapper();
        LoginResponseDto resDto = mapper.readValue(response.getJsonStr(), LoginResponseDto.class);

        return resDto;
    }
}
