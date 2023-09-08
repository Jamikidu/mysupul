package supul.repository.board;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import supul.model.board.BoardQComment;
import supul.model.board.BoardQuestion;



public interface CommQRepository extends JpaRepository<BoardQComment, Integer>{
	// 댓글 삭제
	List<BoardQComment> findByQuestionId(BoardQuestion questionId);
	


}
