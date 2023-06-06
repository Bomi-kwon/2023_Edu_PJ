package com.koreaIT.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.koreaIT.project.service.MessageService;
import com.koreaIT.project.vo.MessageDTO;
import com.koreaIT.project.vo.SmsResponseDTO;

@Controller
public class ProjectMessageController {
	
	private MessageService messageService;

	@Autowired
	public ProjectMessageController(MessageService messageService) {
		this.messageService = messageService;
	}
	
	// requestmapping은 post방식과 get방식 둘다 받을 수 있음.
	// 원래는 post인지 get인지 적어줘야되는데 딱히 안 적어줘도됨.
	// 일반적인 요청이면 그냥 파라미터 받아오면 되는데
	// json 요청이면 requestbody 어노테이션을 써주는게 좋음
	// 왜냐면 요청의 바디내용을 통째로 자바객체로 변환시키기 때문
	// 근데 get방식 요청은 requestbody 어노테이션으로 받을수 없고 @pathvariable, @requestparam 이런거 써야됨
	@RequestMapping("/project/message/test")
    public String test() {
		return "project/message/test";
    }
	
	@PostMapping("/project/message/sendmessage")
    public String sendMessage(MessageDTO messageDto, Model model) throws Exception {
		
		// jsp에서 적은 to와 content를 담은 MessageDTO를
		// 서비스로 보내서 SmsRequestDTO 요청 JSON으로 가공해서 보내고
		// 받아온 결과를 SmsResponseDTO response에 넣겠다는 소리임!! 그래서 vo 클래스 3개 필요
		SmsResponseDTO response = messageService.sendMessage(messageDto);
		
		model.addAttribute("response", response);
		
		return "project/message/result";
	}
	
	
}
