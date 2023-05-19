package com.koreaIT.project.repository;

import org.apache.ibatis.annotations.Mapper;

import com.koreaIT.project.vo.Board;

@Mapper
public interface BoardRepository {
	public Board getBoardById(int boardId);
}
