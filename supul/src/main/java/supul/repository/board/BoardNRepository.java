package supul.repository.board;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import supul.model.board.BoardNotice;




public interface BoardNRepository extends JpaRepository<BoardNotice, Integer>{
	
	List<BoardNotice> findByTitle(String title);
	
	// 파일 관련
	BoardNotice findByFileName(String fileName); 

}
