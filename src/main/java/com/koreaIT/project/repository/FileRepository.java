package com.koreaIT.project.repository;

import org.apache.ibatis.annotations.Mapper;

import com.koreaIT.project.vo.FileVO;

@Mapper
public interface FileRepository {

	void insertFileInfo(String orgName, String savedName, String savedPath, String relTypecode, int relId);

	FileVO getFileByRelId(String relTypecode, int relId);

	FileVO getFileById(int id);

	void updateFileInfo(String orgName, String savedName, String savedPath, int fileId);

	int getLastId();

}
