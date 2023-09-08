package supul.repository.board;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import supul.model.board.BoardQuestion;

public interface BoardQRepository extends JpaRepository<BoardQuestion, Integer>{
	
	List<BoardQuestion> findByTitle(String title);
	


}
