package com.koreaIT.project.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.koreaIT.project.repository.FileRepository;
import com.koreaIT.project.vo.FileVO;

@Service
public class FileService {
	
	@Value("${file.dir}")
	private String fileDir;
	
	private FileRepository fileRepository;

	@Autowired
	public FileService(FileRepository fileRepository) {
		this.fileRepository = fileRepository;
	}

	public void saveFile(MultipartFile file, String relTypecode, int relId) throws IOException {
		
		if (file.isEmpty()) {
			return;
		}
		
		String orgName = file.getOriginalFilename();

		// 파일 이름 중복되면 안되서 랜덤으로 난수 발생
		String uuid = UUID.randomUUID().toString();
		// 확장자명(jpeg 같은거)
		String extension = orgName.substring(orgName.lastIndexOf("."));

		String savedName = uuid + extension;

		String savedPath = fileDir + "/" + savedName;

		fileRepository.insertFileInfo(orgName, savedName, savedPath, relTypecode, relId);

		file.transferTo(new File(savedPath));
		
	}

	public FileVO getFileByRelId(String relTypecode, int relId) {
		return fileRepository.getFileByRelId(relTypecode, relId);
	}

	public FileVO getFileById(int id) {
		return fileRepository.getFileById(id);
	}

	public void updateFile(MultipartFile file, String relTypecode, int relId, int fildId) throws IOException {
		
		if (file.isEmpty()) {
			return;
		}
		
		String orgName = file.getOriginalFilename();

		// 파일 이름 중복되면 안되서 랜덤으로 난수 발생
		String uuid = UUID.randomUUID().toString();
		// 확장자명(jpeg 같은거)
		String extension = orgName.substring(orgName.lastIndexOf("."));

		String savedName = uuid + extension;

		String savedPath = fileDir + "/" + savedName;

		fileRepository.updateFileInfo(orgName, savedName, savedPath, fildId);

		file.transferTo(new File(savedPath));
	
	}

	// 프로필 이미지 등록 안 했을때 기본 이미지 저장하기
	public void saveBasicFile(String relTypecode, int relId) {
		
		String orgName = "basic.png";

		String savedName = orgName;

		String savedPath = "C:/bbomi/upload/" + savedName;

		fileRepository.insertFileInfo(orgName, savedName, savedPath, relTypecode, relId);
		
	}

	public int getImageByMemberId(int memberId) {
		
		FileVO file = fileRepository.getFileByRelId("profile", memberId);
		
		if(file == null) {
			return 3;
		}
		
		return file.getId();
	}


}
