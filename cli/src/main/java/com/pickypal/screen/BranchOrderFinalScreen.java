package com.pickypal.screen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pickypal.dto.auth.LoginResponseDto;
import com.pickypal.dto.order.OrdersSaveRequestDto;
import com.pickypal.dto.order.OrdersViewResponseDto;
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

public class BranchOrderFinalScreen {

    public static void start(LoginResponseDto loginInfo) throws IOException {
        //Runtime.getRuntime().exec("cls"); // for Windows
        Scanner sc = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        ApiKit apiKit = new ApiKit();

        boolean typedInvalidMenu = false;
        boolean getPageNum = true;
        int currentOption = 0;
        int pageIdx = 0;
        String value = "";

        while(true) {
            Console.clear();
            System.out.println("[ [지점] 발주 관리 화면 ]");
            if (loginInfo != null) System.out.println("* 로그인 정보: " + loginInfo.getUserName() + " / " + loginInfo.getRole());
//            System.out.println("---------------------------------------------------------");
//            System.out.printf("%10s%10s", "상품ID", "상품명\n");
//            System.out.println("---------------------------------------------------------");

            getByPage(pageIdx, currentOption, value, loginInfo.getAccessToken());

            System.out.println("---------------------------------------------------------");

            System.out.println();
            System.out.println("0) 발주 조회");
            System.out.println("1) 발주 등록");
            System.out.println("2) 발주 삭제");
            System.out.println("3) [← 뒤로 가기]");

            System.out.println();
            if (typedInvalidMenu) System.out.println("* * * 잘못 입력했습니다. 옵션 번호를 선택해주세요: ");
            System.out.print("* * * 옵션 번호를 선택해주세요: ");
            currentOption = sc.nextInt();
            sc.nextLine(); // remove line feed

            if (currentOption == 0) {
                typedInvalidMenu = false;
                System.out.print("* * * 유형 선택: \n0) 전체 조회  \n1) 상품ID 조회  \n2) 상품명 조회 \n3) 발주일 조회 \n4) [← 뒤로 가기] \n>> ");
                int selected = sc.nextInt();
                sc.nextLine(); // remove line feed

                switch (selected) {
                    case 0:
                        break;
                    case 1:
                        System.out.print("* * * 조회할 상품ID >> ");
                        value = sc.nextLine();
                        break;
                    case 2:
                        System.out.print("* * * 조회할 상품명 >> ");
                        value = sc.nextLine();
                        break;
                    case 3:
                        System.out.print("* * * 조회할 발주일(####-##-##) >> ");
                        value = sc.nextLine();
                        break;
                    case 4:
                        return;
                }
                
                currentOption = selected; // 다음 조회 옵션을 선택한 번호로 지정 (endpoint 선택)
                getPageNum = true;
            }
            else if (currentOption == 1) {
                typedInvalidMenu = false;
                System.out.print("* * * 등록할 상품ID >> ");
                String itemId = sc.nextLine();
                System.out.print("* * * 등록할 수량 >> ");
                int quantity = sc.nextInt();
                sc.nextLine(); // remove line feed

                OrdersSaveRequestDto dto = new OrdersSaveRequestDto(itemId, quantity);
                ApiResponse response = apiKit.postRequestWithAuth("http://localhost:8080/orders", dto, loginInfo.getAccessToken());

                currentOption = 0; // 등록 후 조회(0)
                getPageNum = false;
            }
            else if (currentOption == 2) {
                typedInvalidMenu = false;
                System.out.print("* * * 삭제할 발주 번호 >> ");
                int orderId = sc.nextInt();

                ApiResponse response = apiKit.deleteRequestWithAuth("http://localhost:8080/orders/" + orderId, loginInfo.getAccessToken());
                System.out.println("* * * 발주번호:" + orderId +"이(가) 삭제되었습니다.");

                currentOption = 0; // 등록 후 조회(0)
                getPageNum = false;
            }
            else if (currentOption == 3) {
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

            String[] columnNames = {"발주ID", "상품ID", "상품명", "납품처ID", "납품처명", "발주 수량", "총 단가", "발주 상태", "발주일"};
            Object[][] data = dtoList.stream()
                    .map(dto -> new Object[] {
                            dto.getOrderId(), dto.getItemId(), dto.getItemName(), dto.getBranchId(), dto.getBranchName(), dto.getQuantity(), dto.getTotalPrice(), dto.getStatus(), dto.getOrderTime()
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
