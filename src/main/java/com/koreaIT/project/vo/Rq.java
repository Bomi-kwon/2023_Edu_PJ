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

	private HttpServletRequest req;
	private HttpServletResponse resp;
	private HttpSession httpSession;
	public Rq(HttpServletRequest req, HttpServletResponse resp, MemberService memberService) {
	    this.req = req;
	    this.resp = resp;
	    this.httpSession = req.getSession();

	    int loginedMemberId = 0;
	    Member loginedMember = null;

	    if(httpSession.getAttribute("loginedMemberId") != null) {
	    	loginedMemberId = (int)httpSession.getAttribute("loginedMemberId");
	    	loginedMember = memberService.getMemberById(loginedMemberId);
	    }

	    this.loginedMemberId = loginedMemberId;
	    this.loginedMember = loginedMember;

	    this.req.setAttribute("rq", this);
	}

	public void login(Member member) {
	    httpSession.setAttribute("loginedMemberId", member.getId());
	}

	public void logout() {
	    httpSession.removeAttribute("loginedMemberId");
	}

	public void initRq() {
		
	}

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
	
	public String jsReturnOnView(String msg, boolean isHistoryBack) {
		
		req.setAttribute("msg", msg);
		req.setAttribute("isHistoryBack", isHistoryBack);
		
		return "project/common/js";
	}
	
	
	
}
