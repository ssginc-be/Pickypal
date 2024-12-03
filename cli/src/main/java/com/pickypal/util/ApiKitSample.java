package com.pickypal.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pickypal.dto.stock.HeadStockViewResponseDto;

import java.util.List;

/**
 * @author Queue-ri
 */

public class ApiKitSample {
    
    // ApiKit으로 어떻게 API를 호출하는지 작성한 예제 코드
    // CLI 프로그램과 무관함
    public static void main(String[] args) throws JsonProcessingException {
        ApiKit apiKit = new ApiKit();

        /* 로그인 토큰(auth)을 사용한 HTTP GET 호출
         * --> 객체 리스트 [{}] 형식의 json이 반환될 때 (ex. 본사 재고 조회 api)
         */
        // API 호출
        int pageIdx = 0;
        String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb290Iiwicm9sZSI6IkhFQUQiLCJleHAiOjE3MzU3NDQ0NjN9.4Y6yReg_4HO_66rz5e-7XGrQEux80gxf0RQu9ONG598";
        ApiResponse response = apiKit.getRequestWithAuth("http://localhost:8080/head/stock/" + pageIdx, ACCESS_TOKEN);

        int statusCode = response.getStatusCode();
        System.out.println("* * * ApiKitSample - status code: " + response.getStatusCode());

        if (statusCode == 200) { // 정상 호출되면
            // json string을 dto 객체의 List로 파싱
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // LocalDateTime 파싱을 위해 필요
            String jsonStr = response.getJsonStr();
            List<HeadStockViewResponseDto> dtoList = mapper.readValue(jsonStr, TypeFactory.defaultInstance().constructCollectionType(List.class, HeadStockViewResponseDto.class));

            System.out.println("* * * ApiKitSample - parsed dto list: ");
            for (HeadStockViewResponseDto dto : dtoList) {
                System.out.println(dto.toString()); // 각 column별 값은 dto.getItemName() 처럼 getter로 접근
            }
        }
        else { // 호출 실패하면
            System.out.println("* * * ApiKitSample: 응 아니야~");
        }



        /*  토큰 없이 HTTP POST 호출
         * --> 단일 객체 {} 형식의 json이 반환될 때 (ex. 로그인 api 호출 후 반환되는 형식)
         */
        
    }
}
