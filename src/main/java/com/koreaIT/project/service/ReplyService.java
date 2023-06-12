package com.koreaIT.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koreaIT.project.repository.ReplyRepository;
import com.koreaIT.project.vo.Reply;

@Service
public class ReplyService {
	ReplyRepository replyRepository;

	@Autowired
	public ReplyService(ReplyRepository replyRepository) {
		this.replyRepository = replyRepository;
	}

	public List<Reply> getReplies(String relTypecode, int relId) {
		return replyRepository.getReplies(relTypecode, relId);
	}

	public void doWriteReply(int loginedMemberId, String relTypeCode, int relId, String replybody) {
		replyRepository.doWriteReply(loginedMemberId, relTypeCode, relId, replybody);
	}

	public Reply getReply(int id) {
		return replyRepository.getReply(id);
	}

	public void doModifyReply(int id, String replybody) {
		replyRepository.doModifyReply(id, replybody);
	}

	public void doDeleteReply(int id) {
		replyRepository.doDeleteReply(id);
	}

}
