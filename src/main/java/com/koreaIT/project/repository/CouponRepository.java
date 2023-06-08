package com.koreaIT.project.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CouponRepository {

	public void doGiveCoupon(String deadLine, int studentId);

	
}
