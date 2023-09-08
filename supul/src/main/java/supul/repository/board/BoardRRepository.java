package supul.repository.board;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import supul.model.board.BoardReview;




public interface BoardRRepository extends JpaRepository<BoardReview, Integer>{
	
	List<BoardReview> findByTitle(String title);
	
	BoardReview save(BoardReview board);


}
