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
	

	/**
	 * 네이버 공식문서에 나와있는 암호화 형식
	 * 헤더의 내용을 암호화하여 다시 헤더에 넣으라는 함수임
	 * @param time 유닉스 타임(70년 1월 1일 기점으로 몇밀리초 흘렀는지) 받아오기
	 * @return 시간을 변수로 받아 암호화한 문자열
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
    public String makeSignature(Long time) throws UnsupportedEncodingException, 
    NoSuchAlgorithmException, InvalidKeyException {
    	String space = " ";
    	String newLine = "\n";
    	String method = "POST";
    	String url = "/sms/v2/services/" + this.serviceId + "/messages";		// url (include query string)
    	String timestamp = time.toString();			// current timestamp (epoch)
    	String accessKey = this.accessKey;			// access key id (from portal or Sub Account)
    	String secretKey = this.secretKey;

    	
    	// string 객체는 변경 불가능 해서
    	// 하나의 문자열을 다른 문자열과 연결하면 새 문자열이 생성되고 이전 문자열은 garbage collector로 들어간다.
    	// 그래서 많은 문자열을 연결하면 많은 중간 문자열 객체가 생성되어 비효율적인 코드가 생성된다.
    	// 그러나 stringbuilder는 변경가능한 문자열을 만들어줘서 메모리를 덜 잡아먹는다.
    	String message = new StringBuilder()
    		.append(method)
    		.append(space)
    		.append(url)
    		.append(newLine)
    		.append(timestamp)
    		.append(newLine)
    		.append(accessKey)
    		.toString();

    	//1. SecretKeySpec 클래스를 사용한 키 생성 
    	SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
    	// HMAC이란? 해시함수를 이용해서 메시지 인증 코드를 구성하는 방법
    	// HMAC = Hash(Message, Key) + Message
    	
    	//2. 지정된  MAC 알고리즘을 구현하는 Mac 객체를 작성
    	Mac mac = Mac.getInstance("HmacSHA256");
    	// 암호화 해시 함수와 기밀 암호화 키를 수반하는 특정한 유형의 메시지 인증 코드!!
    	// mac은 내가 원하는 사람으로부터 메시지가 왔는지 판단할 수 있게 해줌
    	// 공유된 키로 메시지 해쉬값(Message Authentication Code)을 만들어냄
    	// 메시지 변조와 위장을 막을 수 있음.
    	
    	//3. 키를 사용해 이 Mac 객체를 초기화
    	mac.init(signingKey);

    	//4. 암호화 하려는 데이터의 바이트의 배열을 처리해 MAC 조작을 종료
    	byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
    	
    	//5. Base64 Encode to String
    	String encodeBase64String = Base64.encodeBase64String(rawHmac);

      return encodeBase64String;
    }
    
    
    /**
     * 문자 메시지 전송 기능
     * MessageDTO를 받아 SmsRequestDto로 가공해서 메시지를 보낸뒤
     * 받은 결과 SmsResponseDto를 리턴한다.
     * @param messageDto 받는사람(to)과 내용(content) 정보 포함
     * @return SmsResponseDto로 받아옴. 나중에는 result.jsp 페이지에 이 결과를 그려줄거임.
     * @throws Exception
     * @todo 단체문자 보내기 기능으로 발전시키기!!
     * apache httpclient maven dependancy 추가
     */
    public SmsResponseDTO sendMessage(MessageDTO messageDto) throws Exception {
    	
    	Long time = System.currentTimeMillis();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("x-ncp-apigw-timestamp", time.toString());
		headers.set("x-ncp-iam-access-key", accessKey);
		headers.set("x-ncp-apigw-signature-v2", makeSignature(time));
		
		List<MessageDTO> messages = new ArrayList<>();
		
		// 여기서 messages 리스트에 messageDto를 담는것으로 보아
		// 단체문자 전송도 금방 처리할 수 있을 듯 한데...!!
		messages.add(messageDto);
		
		SmsRequestDTO request = SmsRequestDTO.builder()
				.type("SMS")
				.contentType("COMM")
				.countryCode("82")
				.from(phone)
				.content(messageDto.getContent())
				.messages(messages)
				.build();
		
		// JSON Jackson 라이브러리의 클래스. 자바객체를 JSON 형식으로 변환해줌
		ObjectMapper objectMapper = new ObjectMapper();
		String body = objectMapper.writeValueAsString(request);
		
		// http요청 또는 응답에 해당하는 httpheader와 httpbody를 포함하는 클래스
		HttpEntity<String> httpBody = new HttpEntity<>(body, headers);
		
		// resttemplate은 서버와 서버간 통신을 쉽게 해준다.
		// 스프링에 내장된 http 통신 템플릿이었는데
		// 스프링 5 이후부터는 빠짐 (webclient 사용을 지향)
		RestTemplate restTemplate = new RestTemplate();
		
		// 통신과정을 http client인 HttpComponentsClientHttpRequestFactory에게 위임함.
	    restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
	    
	    // POST요청을 보내고 결과로 객체를 반환받는다.
	    SmsResponseDTO response = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"
	    + serviceId +"/messages"), httpBody, SmsResponseDTO.class);
 
    	
    	return response;
    }


}
