package com.koreaIT.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koreaIT.project.repository.CouponRepository;
import com.koreaIT.project.vo.Coupon;

@Service
public class CouponService {
	CouponRepository couponRepository;

	@Autowired
	public CouponService(CouponRepository couponRepository) {
		this.couponRepository = couponRepository;
	}

	public void doGiveCoupon(String deadLine, int studentId, String couponPassword) {
		couponRepository.doGiveCoupon(deadLine, studentId, couponPassword);
	}

	public Coupon getCouponByStudentId(int studentId) {
		return couponRepository.getCouponByStudentId(studentId);
	}



}
