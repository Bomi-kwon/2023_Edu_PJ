package com.koreaIT.project.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.koreaIT.project.vo.Rq;

@Component
public class BeforeActionInterceptor implements HandlerInterceptor{
	// interceptor는 controller의 method에 접근하는 과정에서 뭔가를 제어할 때 사용
	// controller에 접근하기 전에 interceptor 써야하면 preHandle 쓰는 것임
	// postHandle은 controller 접근후 view로 결과를 전달하기 전에 실행되는 method임
	
	private Rq rq;

	@Autowired
	public BeforeActionInterceptor(Rq rq) {
		this.rq = rq;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		rq.initRq();
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	
	
	
}
