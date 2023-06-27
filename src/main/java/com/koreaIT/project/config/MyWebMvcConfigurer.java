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
	// interceptor를 실행시켜 줌.

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
		
		// addinterceptor로 애플리케이션 내에 인터셉터 드옥해주기
		
		ir = registry.addInterceptor(beforeActionInterceptor);
		ir.addPathPatterns("/**"); // 인터셉터 호출하는 주소와 경로를 추가하겠다.
		ir.addPathPatterns("/favicon.ico");
		ir.excludePathPatterns("/resource/**"); // 얘는 인터셉터 호출에서 ignore 하겠다.
		
		ir = registry.addInterceptor(needLoginInterceptor);
		ir.addPathPatterns("/project/home/select");
		ir.addPathPatterns("/project/home/setqrurl");
		ir.addPathPatterns("/project/home/doMakeQR");
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
		ir.addPathPatterns("/project/member/doRegister");
		ir.addPathPatterns("/project/member/doRegisterAfterPayment");
		ir.addPathPatterns("/project/member/getMemberByClassId");
		ir.addPathPatterns("/project/member/excelDownload");
		ir.addPathPatterns("/project/member/getStudentsByClass");
		ir.addPathPatterns("/project/member/givecoupon");
		ir.addPathPatterns("/project/member/doGiveCoupon");
		ir.addPathPatterns("/project/member/getStudentsByNameKeyWord");
		ir.addPathPatterns("/project/article/write");
		ir.addPathPatterns("/project/article/doWrite");
		ir.addPathPatterns("/project/article/doDelete");
		ir.addPathPatterns("/project/article/doDeleteArticles");
		ir.addPathPatterns("/project/article/modify");
		ir.addPathPatterns("/project/article/doModify");
		ir.addPathPatterns("/project/article/setSelectBox");
		ir.addPathPatterns("/project/article/score");
		ir.addPathPatterns("/project/article/doWriteScoreArticle");
		ir.addPathPatterns("/project/article/doScoreDelete");
		ir.addPathPatterns("/project/article/scoremodify");
		ir.addPathPatterns("/project/article/doModifyScoreArticle");
		ir.addPathPatterns("/project/article/getStudentsByClass");
		ir.addPathPatterns("/project/article/doHwCheck");
		ir.addPathPatterns("/project/article/doHwDelete");
		ir.addPathPatterns("/project/article/doHwModify");
		ir.addPathPatterns("/project/chat/enterRoom");
		
		
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
