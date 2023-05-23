package com.koreaIT.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.koreaIT.project.interceptor.BeforeActionInterceptor;
import com.koreaIT.project.interceptor.NeedLoginInterceptor;
import com.koreaIT.project.interceptor.NeedLogoutInterceptor;

@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer{

	private BeforeActionInterceptor beforeActionInterceptor;
	private NeedLoginInterceptor needLoginInterceptor;
	private NeedLogoutInterceptor needLogoutInterceptor;
	
	@Autowired
	public MyWebMvcConfigurer(BeforeActionInterceptor beforeActionInterceptor,
			NeedLoginInterceptor needLoginInterceptor, NeedLogoutInterceptor needLogoutInterceptor) {
		this.beforeActionInterceptor = beforeActionInterceptor;
		this.needLoginInterceptor = needLoginInterceptor;
		this.needLogoutInterceptor = needLogoutInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		InterceptorRegistration ir;
		
		ir = registry.addInterceptor(beforeActionInterceptor);
		ir.addPathPatterns("/**");
		ir.addPathPatterns("/favicon.ico");
		ir.excludePathPatterns("/resource/**");
		
		ir = registry.addInterceptor(needLoginInterceptor);
		ir.addPathPatterns("/project/member/memberlist");
		ir.addPathPatterns("/project/member/doMemberLogout");
		ir.addPathPatterns("/project/article/write");
		ir.addPathPatterns("/project/article/doWrite");
		ir.addPathPatterns("/project/article/doHomeworkDelete");
		ir.addPathPatterns("/project/article/homeworkmodify");
		ir.addPathPatterns("/project/article/doHomeworkModify");
		ir.addPathPatterns("/project/article/score");
		
		
		ir = registry.addInterceptor(needLogoutInterceptor);
		ir.addPathPatterns("/project/member/doMemberJoin");
		ir.addPathPatterns("/project/member/memberlogin");
		ir.addPathPatterns("/project/member/doMemberLogin");
		
		
	}
	
	
}
