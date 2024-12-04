package com.pickypal.screen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pickypal.dto.auth.LoginResponseDto;
import com.pickypal.dto.incoming.BranchIncomingItemResponseDto;
import com.pickypal.dto.stock.HeadStockViewResponseDto;
import com.pickypal.util.ApiKit;
import com.pickypal.util.ApiResponse;
import com.pickypal.util.Console;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Scanner;

/**
 * @author Queue-ri
 */

public class BranchIncomingScreen {

    public static void start(LoginResponseDto loginInfo) throws IOException {
        //Runtime.getRuntime().exec("cls"); // for Windows
        Scanner sc = new Scanner(System.in);

        boolean typedInvalidMenu = false;
        int currentOption = 0;
        int pageIdx = 0;
        String value = "";

        while(true) {
            Console.clear();
            System.out.println("[ [지점] 입고 조회 화면 ]");
            if (loginInfo != null) System.out.println("* 로그인 정보: " + loginInfo.getUserName() + " / " + loginInfo.getRole());
            System.out.println("|---------------------------------------------------------");
            System.out.printf("|%9s|%15s|%22s|", "입고ID", "상품ID", "상품명\n");
            System.out.println("|---------------------------------------------------------");

            getByPage(pageIdx, currentOption, value, loginInfo.getAccessToken());

            System.out.println("|---------------------------------------------------------");

            System.out.println();
            System.out.println("0) 전체 조회");
            System.out.println("1) 상품 id 조회");
            System.out.println("2) 상품명 조회");
            System.out.println("3) 입고 등록일 조회");
            System.out.println("4) [← 뒤로 가기]");

            System.out.println();
            if (typedInvalidMenu) System.out.println("* * * 잘못 입력했습니다. 옵션 번호를 선택해주세요: ");
            else System.out.print("* * * 옵션 번호를 선택해주세요: ");
            currentOption = sc.nextInt();

            if (currentOption == 0) {
                typedInvalidMenu = false;
                System.out.print("* * * 조회할 페이지 번호 >> ");
                pageIdx = sc.nextInt();
            }
            else if (currentOption == 1 || currentOption == 2 || currentOption == 3) {
                typedInvalidMenu = false;
                sc.nextLine(); // remove line feed
                System.out.print("* * * 검색할 값 >> ");
                value = sc.nextLine();
                System.out.print("* * * 조회할 페이지 번호 >> ");
                pageIdx = sc.nextInt();
            }
            else if (currentOption == 4) {
                return;
            }
            else { // 잘못된 옵션 값 입력
                typedInvalidMenu = true;
            }

        } // end of while

    }

    public static void getByPage(int pageIdx, int option, String value, String accessToken) throws JsonProcessingException, UnsupportedEncodingException {
        // API 호출
        ApiKit apiKit = new ApiKit();
        String endpoint = "";
        String encodedValue = URLEncoder.encode(value, "UTF-8");

        switch (option) {
            case 0: endpoint = "http://localhost:8080/branch/incoming/" + pageIdx; break;
            case 1: endpoint = "http://localhost:8080/branch/incoming/item-id/" + pageIdx + "?item_id=" + encodedValue; break;
            case 2: endpoint = "http://localhost:8080/branch/incoming/item-name/" + pageIdx + "?item_name=" + encodedValue; break;
            case 3: endpoint = "http://localhost:8080/branch/incoming/date/" + pageIdx + "?date=" + encodedValue; break;
        }

        ApiResponse response = apiKit.getRequestWithAuth(endpoint, accessToken);

        if (response.getStatusCode() == 200) { // 정상 호출되면
            // json string을 dto 객체의 List로 파싱
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // LocalDateTime 파싱을 위해 필요
            String jsonStr = response.getJsonStr();
            List<BranchIncomingItemResponseDto> dtoList = mapper.readValue(jsonStr, TypeFactory.defaultInstance().constructCollectionType(List.class, BranchIncomingItemResponseDto.class));
            for (BranchIncomingItemResponseDto dto : dtoList) {
                System.out.printf("|%9s|%15s|%22s|%9s%9s%9s%9s", dto.getId(), dto.getItemId(), dto.getItemName(), dto.getSupplierId(), dto.getSupplierName(), dto.getQuantity(), dto.getInTime());
                System.out.println();
            }
        }
        else { // 호출 실패하면
            System.out.println("* * * BranchIncomingScreen: API failed");
        }
    }
}
