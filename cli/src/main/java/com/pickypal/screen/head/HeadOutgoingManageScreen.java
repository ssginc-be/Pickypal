package com.pickypal.screen.head;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pickypal.dto.auth.LoginResponseDto;
import com.pickypal.dto.order.OrdersViewResponseDto;
import com.pickypal.dto.outgoing.HeadOutgoingItemResponseDto;
import com.pickypal.util.ApiKit;
import com.pickypal.util.ApiResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Scanner;

/**
 * @author Hyun7en
 */

public class HeadOutgoingManageScreen {

    private static Scanner sc = new Scanner(System.in);


    public static void start(LoginResponseDto loginInfo) throws IOException {
        //Runtime.getRuntime().exec("cls"); // for Windows

        boolean typedInvalidMenu = false;
        int currentOption = 0;
        int pageIdx = 0;
        String status = "";
        int orderId = 0;

        while (true) {
            System.out.println("[ [본사] 출고 관리 화면 ]");
            if (loginInfo != null)
                System.out.println("* 로그인 정보: " + loginInfo.getUserName() + " / " + loginInfo.getRole());
            //테이블 조회
            System.out.println("---------------------------------------------------------");
            System.out.printf("%10s%10s", "상품ID", "상품명\n");
            System.out.println("---------------------------------------------------------");
            getByPage(pageIdx, currentOption, status, orderId, loginInfo.getAccessToken());
            System.out.println();
            System.out.println("---------------------------------------------------------");
            System.out.println();

            //메뉴
            System.out.println("0) 지점 발주 관리");
            System.out.println("1) 본사 출고 내역 조회");
            System.out.println("2) [← 뒤로 가기]");

            System.out.println();
            System.out.println("---------------------------------------------------------");

            System.out.print("* * * 조회 옵션 번호를 선택해주세요: ");
            currentOption = sc.nextInt();

            //뒤로 가기
            if (currentOption == 2) {
                return;
            }

            // 테이블 페이지네이션
            System.out.print("* * * 조회할 페이지 번호 >> ");
            pageIdx = sc.nextInt();
            sc.nextLine();

            switch (currentOption) {
                case 0:
                    getByPage(pageIdx, 0, status, orderId, loginInfo.getAccessToken());

                    System.out.println("출고 관리를 하시겠습니까?(Y/N)");
                    String answer = sc.nextLine();
                    if(answer.equalsIgnoreCase("Y")) {
                        System.out.println("출고 관리할 발주 ID를 입력하세요: ");
                        orderId = sc.nextInt();

                        System.out.println("출고 승인 여부를 선택");
                        status = sc.nextLine();
                        getByPage(pageIdx, 2, status, orderId, loginInfo.getAccessToken());
                    }
                    break;
                case 1:
                    getByPage(pageIdx, 1, status, orderId, loginInfo.getAccessToken());
                    break;
            }

        }
    }

    public static void getByPage(int pageIdx, int option, String status, int orderId, String accessToken) throws JsonProcessingException, UnsupportedEncodingException {
        // API 호출
        ApiKit apiKit = new ApiKit();
        String endpoint = "";
        //검색시 value 인코딩 처리
        String encodedStatus = URLEncoder.encode(status, "UTF-8");

        switch (option) {
            case 0:
                endpoint = "http://localhost:8080/orders/head/" + pageIdx;
                break;//지점 발주 조회
            case 1:
                endpoint = "http://localhost:8080/head/outgoing/" + pageIdx;
                break; //본사 출고 내역 조회
            case 2:
                endpoint = "http://localhost:8080/head/outgoing?order_id=" + orderId + "&status=" + encodedStatus;
                break; //본사 출고 승인/거부
        }

        ApiResponse response = apiKit.getRequestWithAuth(endpoint, accessToken);

        //order을 포함하면 발주 조회 그외면 출고 조회
        if (response.getStatusCode() == 200) { // 정상 호출되면

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // LocalDateTime 파싱을 위해 필요
            String jsonStr = response.getJsonStr();
            if (option == 0) {
                // json string을 dto 객체의 List로 파싱
                List<OrdersViewResponseDto> dtoList = mapper.readValue(jsonStr, TypeFactory.defaultInstance().constructCollectionType(List.class, OrdersViewResponseDto.class));
                for (OrdersViewResponseDto dto : dtoList) {
                    System.out.printf("%9s%15s%22s%9s%9s%9s%9s%9s%29s", dto.getOrderId(), dto.getItemId(), dto.getItemName(), dto.getBranchId(), dto.getBranchName(), dto.getQuantity(), dto.getTotalPrice(), dto.getStatus(), dto.getOrderTime());
                    System.out.println();
                }

            }
            else if (option == 1) {
                List<HeadOutgoingItemResponseDto> dtoList = mapper.readValue(jsonStr, TypeFactory.defaultInstance().constructCollectionType(List.class, HeadOutgoingItemResponseDto.class));
                for (HeadOutgoingItemResponseDto dto : dtoList) {
                    System.out.printf("%9s%9s%15s%22s%22s%9s%9s%9s%29s29s", dto.getId(), dto.getOrderId(), dto.getItemId(), dto.getItemName(), dto.getSupplierName(), dto.getStatus(), dto.getQuantity(), dto.getCommitter(), dto.getOutStartTime(), dto.getOutEndTime());
                    System.out.println();
                }

            }
            else if (option == 2) {
                System.out.println("출고 승인 성공");

            }

        } else { // 호출 실패하면
            System.out.println("* * * HeadOutgoingManageScreen: API failed");
        }
    }

}
