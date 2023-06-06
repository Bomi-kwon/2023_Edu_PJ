package com.koreaIT.project.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koreaIT.project.vo.MessageDTO;
import com.koreaIT.project.vo.SmsRequestDTO;
import com.koreaIT.project.vo.SmsResponseDTO;

@Service
public class MessageService {

	
	// API키는 함수단에서 바로 사용하기보다는 application에 한번에 넣어놓고 사용하는것이 나음.
	@Value("${naver-cloud-sms.accessKey}")
	private String accessKey;
	@Value("${naver-cloud-sms.secretKey}")
	private String secretKey;
	@Value("${naver-cloud-sms.serviceId}")
	private String serviceId;
	@Value("${naver-cloud-sms.senderPhone}")
	private String phone;
	

	// 네이버 공식문서에 나와있는 메시지 형식 만들기
    public String makeSignature(Long time) throws UnsupportedEncodingException, 
    NoSuchAlgorithmException, InvalidKeyException {
    	String space = " ";
    	String newLine = "\n";
    	String method = "POST";
    	String url = "/sms/v2/services/" + this.serviceId + "/messages";
    	String timestamp = time.toString();			// current timestamp (epoch)
    	String accessKey = this.accessKey;			// access key id (from portal or Sub Account)
    	String secretKey = this.secretKey;

    	String message = new StringBuilder()
    		.append(method)
    		.append(space)
    		.append(url)
    		.append(newLine)
    		.append(timestamp)
    		.append(newLine)
    		.append(accessKey)
    		.toString();

    	SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
    	Mac mac = Mac.getInstance("HmacSHA256");
    	mac.init(signingKey);

    	byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
    	String encodeBase64String = Base64.encodeBase64String(rawHmac);

      return encodeBase64String;
    }
    
    
    public SmsResponseDTO sendMessage(MessageDTO messageDto) throws Exception {
    	
    	Long time = System.currentTimeMillis();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("x-ncp-apigw-timestamp", time.toString());
		headers.set("x-ncp-iam-access-key", accessKey);
		headers.set("x-ncp-apigw-signature-v2", makeSignature(time));
		
		List<MessageDTO> messages = new ArrayList<>();
		messages.add(messageDto);
		
		SmsRequestDTO request = SmsRequestDTO.builder()
				.type("SMS")
				.contentType("COMM")
				.countryCode("82")
				.from(phone)
				.content(messageDto.getContent())
				.messages(messages)
				.build();
		
		ObjectMapper objectMapper = new ObjectMapper();
		String body = objectMapper.writeValueAsString(request);
		HttpEntity<String> httpBody = new HttpEntity<>(body, headers);
		
		RestTemplate restTemplate = new RestTemplate();
	    restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
	    SmsResponseDTO response = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"
	    + serviceId +"/messages"), httpBody, SmsResponseDTO.class);
 
    	
    	return response;
    }


}
