package com.koreaIT.project.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreaIT.project.vo.Reply;

@Mapper
public interface ReplyRepository {

	List<Reply> getReplies(String relTypecode, int relId);

	void doWriteReply(int loginedMemberId, String relTypeCode, int relId, String replybody);

	Reply getReply(int id);

	void doModifyReply(int id, String replybody);

	void doDeleteReply(int id);


	
}
