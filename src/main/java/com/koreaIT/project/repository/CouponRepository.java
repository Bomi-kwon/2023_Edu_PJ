package com.koreaIT.project.repository;

import org.apache.ibatis.annotations.Mapper;

import com.koreaIT.project.vo.Coupon;

@Mapper
public interface CouponRepository {

	public void doGiveCoupon(String deadLine, int studentId);

	public Coupon getCouponByStudentId(int studentId);

	
}
