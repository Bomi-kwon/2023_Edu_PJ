package com.koreaIT.project.service;

import org.springframework.stereotype.Service;

import com.koreaIT.project.repository.BoardRepository;
import com.koreaIT.project.vo.Board;

@Service
public class BoardService {
	BoardRepository boardRepository;

	public BoardService(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

	public Board getBoardById(int boardId) {
		return boardRepository.getBoardById(boardId);
	}


}
