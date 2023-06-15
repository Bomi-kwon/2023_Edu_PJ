package com.koreaIT.project.vo;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.koreaIT.project.service.MemberService;
import com.koreaIT.project.util.Util;

import lombok.Getter;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Rq {
	
	@Getter
	private int loginedMemberId;
	@Getter
	private Member loginedMember;
	@Getter
	private int loginedMemberImageId;

	private HttpServletRequest req;
	private HttpServletResponse resp;
	private HttpSession httpSession;
	public Rq(HttpServletRequest req, HttpServletResponse resp, MemberService memberService) {
	    this.req = req;
	    this.resp = resp;
	    this.httpSession = req.getSession();

	    int loginedMemberId = 0;
	    Member loginedMember = null;
	    int loginedMemberImageId = 0;

	    if(httpSession.getAttribute("loginedMemberId") != null) {
	    	loginedMemberId = (int)httpSession.getAttribute("loginedMemberId");
	    	loginedMember = memberService.getMemberById(loginedMemberId);
	    	loginedMemberImageId = memberService.getImageByMemberId(loginedMemberId);
	    }

	    this.loginedMemberId = loginedMemberId;
	    this.loginedMember = loginedMember;
	    this.loginedMemberImageId = loginedMemberImageId;

	    this.req.setAttribute("rq", this);
	}

	public void login(Member member) {
	    httpSession.setAttribute("loginedMemberId", member.getId());
	}

	public void logout() {
	    httpSession.removeAttribute("loginedMemberId");
	}

	
	//initRq() 속에는 함수 내용을 안 써줄 것임.
	//함수 내용이 없어도 그냥 rq를 한번 써주기만 하면 스프링부트가 rq 객체 생성하기 때문. 
	//그럼 그 뒤로는 그 생성한 객체를 가지고 여기저기서 rq속 함수를 쓸 수가 있는것임.
	public void initRq() {
		
	}

	
	//@ResponseBody 없고 리턴해야되는 값 없이 오류메시지만 alert 해줄때
	public void jsPrintHistoryBack(String msg) {
		resp.setContentType("text/html; charset=UTF-8;");
		
		try {
				if(msg == null) {
				msg = "";
			}
				resp.getWriter().append(Util.f("""
							<script>
								const msg = '%s'.trim();
								if (msg.length > 0) {
									alert(msg);
								}
								history.back();
							</script>
						""", msg));
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	//@ResponseBody 없고 리턴해야되는 값 있고 common.jsp로 잠깐 창 열어서 오류메세지 보여준뒤 뒤로가기 할 때
	public String jsReturnOnView(String msg, boolean isHistoryBack) {
		
		req.setAttribute("msg", msg);
		req.setAttribute("isHistoryBack", isHistoryBack);
		
		return "project/common/js";
	}
	
	
	
}
