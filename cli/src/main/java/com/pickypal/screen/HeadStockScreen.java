package com.pickypal.screen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pickypal.dto.auth.LoginResponseDto;
import com.pickypal.dto.auth.SignUpRequestDto;
import com.pickypal.dto.stock.HeadStockViewResponseDto;
import com.pickypal.util.ApiKit;
import com.pickypal.util.ApiResponse;
import com.pickypal.util.Console;
import dnl.utils.text.table.TextTable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * @author Queue-ri
 */

public class HeadStockScreen {

    public static void start(LoginResponseDto loginInfo) throws IOException {
        //Runtime.getRuntime().exec("cls"); // for Windows
        Scanner sc = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));

        boolean typedInvalidMenu = false;
        int currentOption = 0;
        int pageIdx = 0;
        String value = "";

        while(true) {
            Console.clear();
            System.out.println("[ [본사] 재고 조회 화면 ]");
            if (loginInfo != null) System.out.println("* 로그인 정보: " + loginInfo.getUserName() + " / " + loginInfo.getRole());
//            System.out.println("---------------------------------------------------------");
//            System.out.printf("%10s%10s", "상품ID", "상품명\n");
//            System.out.println("---------------------------------------------------------");

            getByPage(pageIdx, currentOption, value, loginInfo.getAccessToken());

            System.out.println("---------------------------------------------------------");

            System.out.println();
            System.out.println("0) 전체 조회");
            System.out.println("1) 상품 id 조회");
            System.out.println("2) 상품명 조회");
            System.out.println("3) 상품 유형 조회");
            System.out.println("4) 최근 입고일 조회");
            System.out.println("5) [← 뒤로 가기]");

            System.out.println();
            if (typedInvalidMenu) System.out.println("* * * 잘못 입력했습니다. 옵션 번호를 선택해주세요: ");
            System.out.print("* * * 조회 옵션 번호를 선택해주세요: ");
            currentOption = sc.nextInt();

            if (currentOption == 0) {
                typedInvalidMenu = false;
            }
            if (currentOption == 3) {
                typedInvalidMenu = false;
                System.out.print("* * * 유형 선택: 0)행사 상품  1)차별화 상품  2)Fresh Food >> ");
                int selected = sc.nextInt();
                switch (selected) {
                    case 0: value = "행사 상품"; break;
                    case 1: value = "차별화 상품"; break;
                    case 2: value = "Fresh Food"; break;
                }
            }
            else if (currentOption == 5) {
                return;
            }
            else if (currentOption != 0) {
                typedInvalidMenu = false;
                sc.nextLine(); // remove line feed
                System.out.print("* * * 검색할 값 >> ");
                value = sc.nextLine();
            }

            if (currentOption < 0 || 4 < currentOption) { // 잘못된 옵션 값 입력
                typedInvalidMenu = true;
            }

            System.out.print("* * * 조회할 페이지 번호 >> ");
            pageIdx = sc.nextInt();
        }

    }

    public static void getByPage(int pageIdx, int option, String value, String accessToken) throws JsonProcessingException, UnsupportedEncodingException {
        // API 호출
        ApiKit apiKit = new ApiKit();
        String endpoint = "";
        String encodedValue = URLEncoder.encode(value, "UTF-8");

        switch (option) {
            case 0: endpoint = "http://localhost:8080/head/stock/" + pageIdx; break;
            case 1: endpoint = "http://localhost:8080/head/stock/item-id?item_id=" + encodedValue; break;
            case 2: endpoint = "http://localhost:8080/head/stock/item-name/" + pageIdx + "?item_name=" + encodedValue; break;
            case 3: endpoint = "http://localhost:8080/head/stock/type/" + pageIdx + "?type=" + encodedValue; break;
            case 4: endpoint = "http://localhost:8080/head/stock/date/" + pageIdx + "?date=" + encodedValue; break;
        }

        ApiResponse response = apiKit.getRequestWithAuth(endpoint, accessToken);

        if (response.getStatusCode() == 200) { // 정상 호출되면
            // json string을 dto 객체의 List로 파싱
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // LocalDateTime 파싱을 위해 필요
            String jsonStr = response.getJsonStr();
            List<HeadStockViewResponseDto> dtoList = mapper.readValue(jsonStr, TypeFactory.defaultInstance().constructCollectionType(List.class, HeadStockViewResponseDto.class));

            String[] columnNames = {"재고ID", "상품ID", "상품명", "상품 유형", "상품 태그", "납품처명", "가격", "재고", "최근 입고일"};
            Object[][] data = dtoList.stream()
                    .map(dto -> new Object[] {
                            dto.getStockId(), dto.getItemId(), dto.getItemName(), dto.getType(), dto.getTag(), dto.getSupplierName(), dto.getPrice(), dto.getStock(), dto.getIncomingTime()
                    }).toArray(Object[][]::new);

            TextTable tt = new TextTable(columnNames, data);
            tt.printTable();

//            for (HeadStockViewResponseDto dto : dtoList) {
//                System.out.printf("%9s%15s%22s%9s%9s%9s%9s%9s%29s", dto.getStockId(), dto.getItemId(), dto.getItemName(), dto.getType(), dto.getTag(), dto.getSupplierName(), dto.getPrice(), dto.getStock(), dto.getIncomingTime());
//                System.out.println();
//            }
        }
        else { // 호출 실패하면
            System.out.println("* * * HeadStockScreen: API failed");
        }
    }
}
