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
	
	// application.yml에 미리 파일을 업로드할 폴더 경로 설정해놓기!
	@Value("${file.dir}")
	private String fileDir;
	
	private FileRepository fileRepository;

	@Autowired
	public FileService(FileRepository fileRepository) {
		this.fileRepository = fileRepository;
	}

	
	/**
	 * 사용자가 올린 파일을 서버(upload 폴더)에 저장후 DB에 저장
	 * @param file 파일
	 * @param relTypecode (article,profile,audio 세 가지로 구분)
	 * @param relId (연결되는 자료의 번호)
	 * @throws IOException
	 */
	public void saveFile(MultipartFile file, String relTypecode, int relId) throws IOException {
		
		if (file.isEmpty()) {
			return;
		}
		
		// 사용자 컴퓨터에서 파일 올렸을때 이름
		String orgName = file.getOriginalFilename();
		
		// 저장할 파일 이름 중복되면 안되서 랜덤으로 난수 발생
		String uuid = UUID.randomUUID().toString();
		
		// 확장자명(jpeg 같은거)
		String extension = orgName.substring(orgName.lastIndexOf("."));

		// 저장할 파일 이름
		String savedName = uuid + extension;

		// yml에 설정해놓은 경로에 저장할 파일 이름만 추가
		String savedPath = fileDir + "/" + savedName;

		// DB에 파일 정보 INSERT
		fileRepository.insertFileInfo(orgName, savedName, savedPath, relTypecode, relId);

		// 설정한 경로와 이름으로 file을 실제로 업로드 (내 서버의 폴더에 저장)
		file.transferTo(new File(savedPath));
		
	}

	public FileVO getFileByRelId(String relTypecode, int relId) {
		return fileRepository.getFileByRelId(relTypecode, relId);
	}

	public FileVO getFileById(int id) {
		return fileRepository.getFileById(id);
	}

	
	/**
	 * 파일 다른걸로 바꾸면 업데이트 해주기
	 * @param file
	 * @param relTypecode
	 * @param relId
	 * @param fileId 그 파일의 번호
	 * @throws IOException
	 */
	public void updateFile(MultipartFile file, String relTypecode, int relId, int fileId) throws IOException {
		
		if (file.isEmpty()) {
			return;
		}
		
		deleteFile(fileId);
		
		saveFile(file, relTypecode, relId);
	
	}
	
	
	/**
	 * 파일 다른걸로 바꾸면 기존꺼 삭제 해주기
	 * @param fileId 그 파일의 번호
	 * @throws IOException
	 */
	public void deleteFile(int fileId) throws IOException {
		
		FileVO deleteFile = fileRepository.getFileById(fileId);
		
		File deletefile = new File(deleteFile.getSavedPath());
		
		if(deletefile.exists()) {
			deletefile.delete();
		}
		
		fileRepository.deleteFile(fileId);
	
	}
	

	// 프로필 이미지 등록 안 했을때 기본 이미지 저장하기
	public void saveBasicFile(String relTypecode, int relId) {
		
		String orgName = "basic.png";

		String savedName = orgName;

		String savedPath = fileDir + "/" + savedName;

		fileRepository.insertFileInfo(orgName, savedName, savedPath, relTypecode, relId);
		
	}

	/**
	 * 탑바에 현재 로그인한 멤버의 사진 보여주게 하기
	 * @param memberId 이걸로 사진 파일을 가져와야함
	 * @return 그 파일의 id를 리턴함
	 */
	public int getImageByMemberId(int memberId) {
		
		FileVO file = fileRepository.getFileByRelId("profile", memberId);
		
		if(file == null) {
			return 3;
		}
		
		return file.getId();
	}

	public int getLastId() {
		return fileRepository.getLastId();
	}

	

}
