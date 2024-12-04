package com.pickypal.screen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pickypal.dto.auth.LoginResponseDto;
import com.pickypal.dto.order.OrdersSaveRequestDto;
import com.pickypal.dto.order.OrdersViewResponseDto;
import com.pickypal.dto.outgoing.HeadOutgoingItemResponseDto;
import com.pickypal.util.ApiKit;
import com.pickypal.util.ApiResponse;
import com.pickypal.util.Console;
import dnl.utils.text.table.TextTable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Queue-ri
 */

public class HeadOutgoingScreen {

    public static void start(LoginResponseDto loginInfo) throws IOException {
        //Runtime.getRuntime().exec("cls"); // for Windows
        Scanner sc = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));

        boolean typedInvalidMenu = false;
        boolean getPageNum = true;
        int currentOption = 0;
        int pageIdx = 0;

        while(true) {
            Console.clear();
            System.out.println("[ [본사] 출고 관리 화면 ]");
            if (loginInfo != null) System.out.println("* 로그인 정보: " + loginInfo.getUserName() + " / " + loginInfo.getRole());
//            System.out.println("---------------------------------------------------------");
//            System.out.printf("%10s%10s", "상품ID", "상품명\n");
//            System.out.println("---------------------------------------------------------");

            getByPage(pageIdx, loginInfo.getAccessToken());

            System.out.println("---------------------------------------------------------");

            System.out.println();
            System.out.println("0) 지점 발주 관리");
            System.out.println("1) 본사 출고 내역 조회");
            System.out.println("2) [← 뒤로 가기]");

            System.out.println();
            if (typedInvalidMenu) System.out.println("* * * 잘못 입력했습니다. 옵션 번호를 선택해주세요: ");
            System.out.print("* * * 옵션 번호를 선택해주세요: ");
            currentOption = sc.nextInt();
            sc.nextLine(); // remove line feed

            if (currentOption == 0) {
                typedInvalidMenu = false;
                // 발주 내역 조회(0) - 서브 screen으로 분리
                HeadOutgoingSubScreen.start(loginInfo);
                // end of sub screen
                getPageNum = false;
            }
            else if (currentOption == 1) {
                typedInvalidMenu = false;
                // 출고 내역 조회(1) - 출고 관리 페이지이므로 출고 내역 조회를 기본으로 함
                getPageNum = true; // 조회이므로 페이지 번호를 받음
            }
            else if (currentOption == 2) {
                return;
            }
            else { // 잘못된 옵션 값 입력
                typedInvalidMenu = true;
                getPageNum = false;
            }

            // true 일때만 페이지 번호 받아서 다음 조회에 반영하기
            if (getPageNum) {
                System.out.print("* * * 조회할 페이지 번호 >> ");
                pageIdx = sc.nextInt();
            }
        }

    }

    public static void getByPage(int pageIdx, String accessToken) throws JsonProcessingException, UnsupportedEncodingException {
        // API 호출
        ApiKit apiKit = new ApiKit();
        String endpoint = "http://localhost:8080/head/outgoing/" + pageIdx;

        ApiResponse response = apiKit.getRequestWithAuth(endpoint, accessToken);

        if (response.getStatusCode() == 200) { // 정상 호출되면
            // json string을 dto 객체의 List로 파싱
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // LocalDateTime 파싱을 위해 필요
            String jsonStr = response.getJsonStr();

            List<HeadOutgoingItemResponseDto> dtoList = mapper.readValue(jsonStr, TypeFactory.defaultInstance().constructCollectionType(List.class, HeadOutgoingItemResponseDto.class));
            String[] columnNames = new String[]{"출고ID", "발주ID", "상품ID", "상품명", "납품처명", "출고 상태", "수량", "출고 승인자", "출고일시", "출고완료일시"};
            Object[][] data = dtoList.stream()
                    .map(dto -> new Object[] {
                            dto.getId(), dto.getOrderId(), dto.getItemId(), dto.getItemName(), dto.getSupplierName(), dto.getStatus(), dto.getQuantity(), dto.getCommitter(), dto.getOutStartTime(), dto.getOutEndTime()
                    }).toArray(Object[][]::new);

            TextTable tt = new TextTable(columnNames, data);
            tt.printTable();

        }
        else { // 호출 실패하면
            System.out.println("* * * HeadStockScreen: API failed");
        }
    }

}
