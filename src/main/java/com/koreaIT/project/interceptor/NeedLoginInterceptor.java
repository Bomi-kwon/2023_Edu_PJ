package com.koreaIT.project.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.koreaIT.project.vo.Rq;

@Component
public class NeedLoginInterceptor implements HandlerInterceptor{
	
	private Rq rq;
	
	@Autowired
	public NeedLoginInterceptor(Rq rq) {
		this.rq = rq;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		/**
		 * 이 함수는 return 타입이 boolean이고 responsebody같은게 없으므로
		 * 여기서 바로 출력하는 것 불가능함
		 * 그래서 rq.jsprinthistoryback을 사용해서 alert 하고 return false로 ㅎ마수 끝내주기
		 */
		if(rq.getLoginedMemberId() == 0) {
			rq.jsPrintHistoryBack("로그인 후 이용해 주세요.");
			return false;
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
}