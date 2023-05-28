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
		ir.addPathPatterns("/project/member/doMemberDrop");
		ir.addPathPatterns("/project/member/memberprofile");
		ir.addPathPatterns("/project/member/membergroup");
		ir.addPathPatterns("/project/member/checkpassword");
		ir.addPathPatterns("/project/member/membermodify");
		ir.addPathPatterns("/project/member/doMemberModify");
		ir.addPathPatterns("/project/member/passwordmodify");
		ir.addPathPatterns("/project/member/doPasswordModify");
		ir.addPathPatterns("/project/member/getMembersByAuthLevel");
		ir.addPathPatterns("/project/member/groupregistration");
		ir.addPathPatterns("/project/member/getGroupsByTeacherID");
		ir.addPathPatterns("/project/member/groupregisterdetail");
		ir.addPathPatterns("/project/article/write");
		ir.addPathPatterns("/project/article/doWrite");
		ir.addPathPatterns("/project/article/doDelete");
		ir.addPathPatterns("/project/article/modify");
		ir.addPathPatterns("/project/article/doModify");
		ir.addPathPatterns("/project/article/setSelectBox");
		ir.addPathPatterns("/project/article/scorearticlewrite");
		ir.addPathPatterns("/project/article/score");
		ir.addPathPatterns("/project/article/getStudentsByClass");
		ir.addPathPatterns("/project/article/doWriteScoreArticle");
		ir.addPathPatterns("/project/article/doScoreDelete");
		ir.addPathPatterns("/project/article/scorearticlemodify");
		ir.addPathPatterns("/project/article/scoremodify");
		ir.addPathPatterns("/project/article/doModifyScoreArticle");
		
		
		ir = registry.addInterceptor(needLogoutInterceptor);
		ir.addPathPatterns("/project/member/memberjoin");
		ir.addPathPatterns("/project/member/doMemberJoin");
		ir.addPathPatterns("/project/member/memberlogin");
		ir.addPathPatterns("/project/member/doMemberLogin");
		ir.addPathPatterns("/project/member/findLoginID");
		ir.addPathPatterns("/project/member/doFindLoginID");
		ir.addPathPatterns("/project/member/findLoginPW");
		ir.addPathPatterns("/project/member/doFindLoginPW");
		
		
	}
	
	
}
