package com.pickypal.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author Queue-ri
 */

public class ApiKit {

    // Http GET request to backend server
    // **no auth header**
    public ApiResponse getRequest(String endpoint) {
        ApiResponse res = new ApiResponse();

        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet getRequest = new HttpGet(endpoint); //GET 메소드 URL 생성

            CloseableHttpResponse response = client.execute(getRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            res.setStatusCode(statusCode);

            // Response 출력
            String body;
            if (statusCode == 200) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                body = handler.handleResponse(response);
                System.out.println("* * * ApiKit(GET): 200 OK");
                System.out.println(body);
            } else {
                System.out.println("* * * ApiKit(GET): Server returned " + statusCode);
                body = null;
            }
            res.setJsonStr(body);
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    // Http GET request **with auth header** to backend server
    // 모든 조회 용도: access token 필요
    public ApiResponse getRequestWithAuth(String endpoint, String ACCESS_TOKEN) {
        ApiResponse res = new ApiResponse();

        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet getRequest = new HttpGet(endpoint); //GET 메소드 URL 생성
            getRequest.addHeader("Authorization", "Bearer " + ACCESS_TOKEN);

            CloseableHttpResponse response = client.execute(getRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            res.setStatusCode(statusCode);

            // response 출력
            String body;
            if (statusCode == 200) {
                // response body를 UTF-8로 파싱
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                body = bufferedReader.readLine();
                bufferedReader.close();
                
                System.out.println("* * * ApiKit(auth GET): 200 OK");
                System.out.println(body);

            } else {
                System.out.println("* * * ApiKit(auth GET): Server returned " + statusCode);
                body = null;
            }
            res.setJsonStr(body);
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    // Http POST request to backend server
    // **no auth header: 로그인, 회원가입 용도**
    public ApiResponse postRequest(String endpoint, Object dto) {
        ApiResponse res = new ApiResponse();
        ObjectMapper mapper = new ObjectMapper();

        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost postRequest = new HttpPost(endpoint); // POST 메소드 URL 생성
            postRequest.setHeader("Content-Type", "application/json");
            // add body
            String json = mapper.writeValueAsString(dto);
            HttpEntity entity = new ByteArrayEntity(json.getBytes("UTF-8"));
            postRequest.setEntity(entity);

            CloseableHttpResponse response = client.execute(postRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            res.setStatusCode(statusCode);

            // Response 출력
            String body;
            if (statusCode == 200) {
                // response body를 UTF-8로 파싱
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                body = bufferedReader.readLine();
                bufferedReader.close();

                System.out.println("* * * ApiKit(POST): 200 OK");
                System.out.println(body);
            } else {
                System.out.println("* * * ApiKit(POST): Server returned " + statusCode);
                body = null;
            }
            res.setJsonStr(body);
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    // Http POST request **with auth header** to backend server
    // 모든 등록 용도: access token 필요
    public ApiResponse postRequestWithAuth(String endpoint) {
        String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb290Iiwicm9sZSI6IkhFQUQiLCJleHAiOjE3MzU3NDQ0NjN9.4Y6yReg_4HO_66rz5e-7XGrQEux80gxf0RQu9ONG598";
        ApiResponse res = new ApiResponse();

        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost postRequest = new HttpPost(endpoint); // POST 메소드 URL 생성
            postRequest.addHeader("Authorization", ACCESS_TOKEN); // token 이용시

            CloseableHttpResponse response = client.execute(postRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            res.setStatusCode(statusCode);

            // Response 출력
            String body;
            if (statusCode == 200) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                body = handler.handleResponse(response);
                System.out.println("* * * ApiKit(auth POST): 200 OK");
                System.out.println(body);
            } else {
                System.out.println("* * * ApiKit(auth POST): Server returned " + statusCode);
                body = null;
            }
            res.setJsonStr(body);
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    // Http DELETE request **with auth header** to backend server
    // 모든 삭제 용도: access token 필요
    public void deleteRequestWithAuth(String endpoint) {

    }
}
