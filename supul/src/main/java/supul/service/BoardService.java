package supul.service;

import java.io.File;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import supul.model.board.BoardNotice;
import supul.model.board.BoardReview;
import supul.repository.board.BoardNRepository;
import supul.repository.board.BoardRRepository;

@Service
public class BoardService {
	@Autowired
	private BoardNRepository boardNRepository;

	@Autowired
	private BoardRRepository boardRRepository;

	@Transactional
	public void uploadAndSave(BoardReview br, MultipartFile file) {
		if (file != null && !file.isEmpty()) {
			String originalFileName = file.getOriginalFilename();
			String fileName = StringUtils.cleanPath(originalFileName);
			String uploadP = "C:\\Users\\Administrator\\Desktop\\supul\\supul\\src\\main\\webapp\\views\\fileup_review";

			try {
				File uploadPath = new File(uploadP);
				if (!uploadPath.exists()) {
					uploadPath.mkdirs();
				}

				// 파일 이름 중복 검사 및 중복 처리
				int number = 1;
				while (new File(uploadPath, fileName).exists()) {
					int dotIndex = originalFileName.lastIndexOf(".");
					String baseName = (dotIndex != -1) ? originalFileName.substring(0, dotIndex) : originalFileName;
					String extension = (dotIndex != -1) ? originalFileName.substring(dotIndex) : "";
					fileName = baseName + "_" + number + extension;
					number++;
				}

				File destFile = new File(uploadPath, fileName);
				file.transferTo(destFile);
				br.setFileName(fileName);
				br.setFilePath(uploadP); // 파일 경로 저장
				boardRRepository.save(br);

			} catch (Exception e) {
				e.printStackTrace();
				// 파일 업로드 실패 처리
			}
		}
	}

	@Transactional
	public void uploadAndSave(BoardNotice bn, MultipartFile file) {
		if (file != null && !file.isEmpty()) {
			String originalFileName = file.getOriginalFilename();
			String fileName = StringUtils.cleanPath(originalFileName);
			String uploadP = "C:\\Users\\Administrator\\Desktop\\supul\\supul\\src\\main\\webapp\\views\\fileup";

			try {
				File uploadPath = new File(uploadP);
				if (!uploadPath.exists()) {
					uploadPath.mkdirs();
				}

				// 파일 이름 중복 검사 및 중복 처리
				int number = 1;
				while (new File(uploadPath, fileName).exists()) {
					int dotIndex = originalFileName.lastIndexOf(".");
					String baseName = (dotIndex != -1) ? originalFileName.substring(0, dotIndex) : originalFileName;
					String extension = (dotIndex != -1) ? originalFileName.substring(dotIndex) : "";
					fileName = baseName + "_" + number + extension;
					number++;
				}

				File destFile = new File(uploadPath, fileName);

				// 이미지 업로드 전에 기존 이미지 삭제
				if (StringUtils.hasText(bn.getFileName())) {
					File oldFile = new File(uploadPath, bn.getFileName());
					if (oldFile.exists()) {
						if (oldFile.delete()) {
							// 기존 파일 삭제에 성공한 경우
							System.out.println("기존 파일 삭제 성공: " + bn.getFileName());
						} else {
							// 기존 파일 삭제에 실패한 경우
							System.out.println("기존 파일 삭제 실패: " + bn.getFileName());
						}
					}
				}

				file.transferTo(destFile);
				bn.setFileName(fileName);
				bn.setFilePath(uploadP); // 파일 경로 저장
				boardNRepository.save(bn);

			} catch (Exception e) {
				e.printStackTrace();
				// 파일 업로드 실패 처리
			}
		}
	}

	@Transactional
	public void deleteAndRemoveFile(int id) {
		Optional<BoardNotice> optionalNotice = boardNRepository.findById(id);

		if (optionalNotice.isPresent()) {
			BoardNotice notice = optionalNotice.get();

			// 파일 경로와 파일 이름을 합쳐서 파일 객체 생성
			File fileToDelete = new File(notice.getFilePath(), notice.getFileName());

			if (fileToDelete.exists()) {
				if (fileToDelete.delete()) {
					// 파일 삭제에 성공한 경우
					System.out.println("파일 삭제 성공: " + notice.getFileName());
				} else {
					// 파일 삭제에 실패한 경우
					System.out.println("파일 삭제 실패: " + notice.getFileName());
				}
			} else {
				// 파일이 존재하지 않는 경우
				System.out.println("파일이 존재하지 않습니다: " + notice.getFileName());
			}

			// 게시물 삭제
			boardNRepository.deleteById(id);
		}
	}

}
