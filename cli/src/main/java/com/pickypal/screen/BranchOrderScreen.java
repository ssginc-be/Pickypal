package com.pickypal.screen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pickypal.dto.auth.LoginResponseDto;
import com.pickypal.dto.order.OrdersSaveRequestDto;
import com.pickypal.dto.order.OrdersViewResponseDto;
import com.pickypal.util.ApiKit;
import com.pickypal.util.ApiResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Scanner;

/**
 * @author Queue-ri
 */

public class BranchOrderScreen {
    static Scanner sc = new Scanner(System.in);
    static LoginResponseDto loginInfoAccessToken;

    public static void start(LoginResponseDto loginInfo) throws IOException {
        //Runtime.getRuntime().exec("cls"); // for Windows
        loginInfoAccessToken = loginInfo;
        boolean typedInvalidMenu = false;
        int currentOption = 0;
        int pageIdx = 0;
        String value = "";

        while(true) {
            System.out.println("[ [본사] 재고 조회 화면 ]");
            if (loginInfo != null) System.out.println("* 로그인 정보: " + loginInfo.getUserName() + " / " + loginInfo.getRole());
            System.out.println("---------------------------------------------------------");
            System.out.printf("%10s%10s", "상품ID", "상품명\n");
            System.out.println("---------------------------------------------------------");

            getByPage(pageIdx, currentOption, value, loginInfo.getAccessToken());

            System.out.println("---------------------------------------------------------");

            System.out.println();
            System.out.println("0) 발주 조회");
            System.out.println("1) 발주 등록");
            System.out.println("2) 발주 삭제");

            System.out.println();
            System.out.print("* * * 조회 옵션 번호를 선택해주세요: ");
            currentOption = sc.nextInt();

            if (currentOption == 0) {
                Loop: while(true) {
                    System.out.print("* * * 유형 선택: \n0) 전체 조회  \n1) 상품ID 조회  \n2) 상품명 조회 \n3) 발주일 조회 \n4) [← 뒤로 가기] >> ");
                    int selected = sc.nextInt();
                    switch (selected) {
                        case 0: overall(); break;
                        case 1: itemId(); break;
                        case 2: itemName(); break;
                        case 3: orderDate(); break;
                        case 4: break Loop;
                    }
                }
            }
            else if (currentOption == 1) { // 발주 등록
                System.out.print("* * * 등록할 상품ID >> ");
                String itemId = sc.nextLine();
                itemId = sc.nextLine();
                System.out.print("* * * 등록할 수량 >> ");
                int quantity = sc.nextInt();

                OrdersSaveRequestDto dto = new OrdersSaveRequestDto(itemId, quantity);
                orderRegist(dto);
            }
            else if (currentOption == 2) { // 발주 삭제
                System.out.print("* * * 삭제할 발주 번호 >> ");
                int orderId = sc.nextInt();

                ApiKit apiKit = new ApiKit();
                ApiResponse response = apiKit.deleteRequestWithAuth("http://localhost:8080/orders/" + orderId, loginInfo.getAccessToken());
                System.out.println("발주번호:" + orderId +" 삭제 되었습니다.");
            }
        }
    }

    public static void overall() throws UnsupportedEncodingException, JsonProcessingException {
        System.out.print("* * * 조회할 페이지 번호 >> ");
        int pageIdx = sc.nextInt();
        getByPage(pageIdx, 0,"", loginInfoAccessToken.getAccessToken());
    }

    public static void itemId() throws UnsupportedEncodingException, JsonProcessingException {
        System.out.print("* * * 조회할 상품ID >> ");
        String id = sc.nextLine();
        id = sc.nextLine();
        getByPage(0, 1, id, loginInfoAccessToken.getAccessToken());
    }

    public static void itemName() throws UnsupportedEncodingException, JsonProcessingException {
        System.out.print("* * * 조회할 상품명 >> ");
        String name = sc.nextLine();
        name = sc.nextLine();
        getByPage(0, 2, name, loginInfoAccessToken.getAccessToken());
    }

    public static void orderDate() throws UnsupportedEncodingException, JsonProcessingException {
        System.out.print("* * * 조회할 발주일(####-##-##) >> ");
        String order = sc.nextLine();
        order = sc.nextLine();
        getByPage(0, 3, order, loginInfoAccessToken.getAccessToken());
    }

    public static void orderRegist(OrdersSaveRequestDto dto) {
        ApiKit apiKit = new ApiKit();
        ApiResponse response = apiKit.postRequestWithAuth("http://localhost:8080/orders", dto, loginInfoAccessToken.getAccessToken());
        System.out.println(response.getStatusCode());
    }

    public static void getByPage(int pageIdx, int option, String value, String accessToken) throws JsonProcessingException, UnsupportedEncodingException {
        // API 호출
        ApiKit apiKit = new ApiKit();
        String endpoint = "";
        String encodedValue = URLEncoder.encode(value, "UTF-8");

        switch (option) {
            case 0: endpoint = "http://localhost:8080/orders/" + pageIdx; break;
            case 1: endpoint = "http://localhost:8080/orders/item-id/" + pageIdx + "?item_id=" + encodedValue; break;
            case 2: endpoint = "http://localhost:8080/orders/item-name/" + pageIdx + "?item_name=" + encodedValue; break;
            case 3: endpoint = "http://localhost:8080/orders/date/" + pageIdx + "?date=" + encodedValue; break;
        }

        ApiResponse response = apiKit.getRequestWithAuth(endpoint, accessToken);

        if (response.getStatusCode() == 200) { // 정상 호출되면
            // json string을 dto 객체의 List로 파싱
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // LocalDateTime 파싱을 위해 필요
            String jsonStr = response.getJsonStr();
            List<OrdersViewResponseDto> dtoList = mapper.readValue(jsonStr, TypeFactory.defaultInstance().constructCollectionType(List.class, OrdersViewResponseDto.class));
            for (OrdersViewResponseDto dto : dtoList) {
                System.out.printf("%9s%15s%22s%9s%9s%9s%9s%9s%29s", dto.getOrderId(), dto.getItemId(), dto.getItemName(), dto.getBranchId(), dto.getBranchName(), dto.getQuantity(), dto.getTotalPrice(), dto.getStatus(), dto.getOrderTime(), dto.getCurrentPage());
                System.out.println();
            }
        }
        else { // 호출 실패하면
            System.out.println("* * * HeadStockScreen: API failed");
        }
    }
}
