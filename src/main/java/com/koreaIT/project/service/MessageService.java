package com.koreaIT.project.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

	private static final String NAVER_SENS_API_URL = "https://sens.apigw.ntruss.com/sms/v2/services/{serviceId}/messages";
    private static final String NAVER_SENS_API_ACCESS_KEY = "OxAIgEdXyNQXm36Tas3j";
    private static final String NAVER_SENS_API_SECRET_KEY = "mncOGNerievlx9JfvdsJlpDK0YOVjPsD07F3vmUR";
    private static final String NAVER_SENS_API_SERVICE_ID = "ncp:sms:kr:309259979584:spongeedu";

    public void sendMessage(String phoneNumber, String message) throws Exception {
        String url = NAVER_SENS_API_URL.replace("{serviceId}", NAVER_SENS_API_SERVICE_ID);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        httpPost.setHeader("X-NCP-auth-key", NAVER_SENS_API_ACCESS_KEY);
        httpPost.setHeader("X-NCP-service-secret", NAVER_SENS_API_SECRET_KEY);

        String requestBody = "{\"type\":\"SMS\","
        		+ "\"contentType\":\"COMM\","
        		+ "\"countryCode\":\"82\","
        		+ "\"from\":\"01000000000\","
        		+ "\"content\":\"" + message + "\","
        				+ "\"messages\":[{\"to\":\"" + phoneNumber + "\"}]}";
        HttpEntity entity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);

        HttpResponse response = client.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == 202) {
            System.out.println("메시지가 성공적으로 전송되었습니다.");
        } else {
            System.out.println("메시지 전송에 실패했습니다. 상태 코드: " + statusCode);
        }
    }


}
