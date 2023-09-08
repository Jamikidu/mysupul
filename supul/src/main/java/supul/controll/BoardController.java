package supul.controll;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import supul.model.board.BoardNotice;
import supul.model.board.BoardQComment;
import supul.model.board.BoardQuestion;
import supul.model.board.BoardReview;
import supul.repository.board.BoardNRepository;
import supul.repository.board.BoardQRepository;
import supul.repository.board.BoardRRepository;
import supul.repository.board.CommQRepository;
import supul.service.BoardService;


@Controller

public class BoardController {
	@Resource
	private BoardNRepository boardNRepository;
	@Resource
	private BoardRRepository boardRRepository;
	@Resource
	private BoardQRepository boardQRepository;
	@Resource
	private CommQRepository commQRepository;
	
	
	@Autowired // BoardNoticeService를 주입
    private BoardService service;
	


	// 후기 리스트
	@GetMapping("/reviewBoard/list")
	public String reviewList(Model mm, 
			@RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        // 페이지 및 사이즈를 기준으로 Pageable 객체 생성
    	Pageable pageable = PageRequest.of(page, size, Sort.by("no").descending());

        // Spring Data JPA를 사용하여 페이징 처리된 공지사항 목록을 가져옴
        Page<BoardReview> rboard = boardRRepository.findAll(pageable);

        // 페이지 관련 정보를 Thymeleaf로 전달
        mm.addAttribute("rList", rboard);
        mm.addAttribute("currentPage", page);
        mm.addAttribute("totalPages", rboard.getTotalPages());
        System.out.println("후기 리스트>>> ");
        return "boardr/boardRList";
	}
	
	// 후기 글쓰기 get
	@GetMapping("/reviewBoard/write")
	public String reviewWrite() {
		System.out.println("후기 글쓰기>>> ");
		return "boardr/boardRWrite";
	}
	
	// 후기 글쓰기 post
	@PostMapping("/reviewBoard/write")
	public String writeReg(BoardReview br, HttpServletRequest request) {
		br.setRegDate(LocalDateTime.now());

	    MultipartFile file = br.getFile(); // BoardReview 객체에서 파일을 가져옴
	    service.uploadAndSave(br, file); // 파일 업로드 및 JPA 저장 처리
	    boardRRepository.save(br);
	    return "redirect:/reviewBoard/list";
	}

	
	// 후기 상세
	@GetMapping("/reviewBoard/detail/{id}")
	public String reviewDetail(@PathVariable int id, Model mm) {
		Optional<BoardReview> rboard = boardRRepository.findById(id);
		
		if(rboard.isPresent()) {
			BoardReview board = rboard.get();
			boardRRepository.save(board);
			mm.addAttribute("rDetail", board);
			System.out.println("후기 상세페이지>>> ");
			return "boardr/boardRDetail";
			
		}else {
			return "redirect:/reviewBoard/list";
		}
	}
	
	// 후기 삭제
	@GetMapping("/reviewBoard/delete/{id}")
	public String reviewDelete(@PathVariable int id) {
		
		boardRRepository.deleteById(id); 
		System.out.println("후기 삭제>>> ");
		return "redirect:/reviewBoard/list";
	}
	
	// 후기 수정 get
	@GetMapping("/reviewBoard/modify/{id}")
	public String reviewModify(@PathVariable int id, Model mm) {
		Optional<BoardReview> rboard = boardRRepository.findById(id);
		
		if(rboard.isPresent()) {
			BoardReview board = rboard.get();
			mm.addAttribute("rModify", board);
			System.out.println("후기 수정>>> ");
			return "boardr/boardRModify";
			
		}else {
			return "redirect:/reviewBoard/list";
		}		
	}
	
	// 후기 수정 post	
	@PostMapping("/reviewBoard/modify/{id}")
	public String reviewModifyReg(@PathVariable int id, BoardReview br, @RequestParam("file") MultipartFile file) {
	    Optional<BoardReview> rboard = boardRRepository.findById(id);
	    if (rboard.isPresent()) {
	    	BoardReview newboard = rboard.get();
	        newboard.setContent(br.getContent());
	        newboard.setTitle(br.getTitle());
	        newboard.setRegDate(LocalDateTime.now());

	        if (!file.isEmpty()) {
	        	service.uploadAndSave(newboard, file);
	        }

	        boardRRepository.save(newboard);
	    }
	    return "redirect:/reviewBoard/detail/" + id;
	}
	
	
	
	//===================================== 문의게시판==============================================//
	//리스트
		@GetMapping("/qnaBoard/list")
		public String questionList(Model mm, 
				@RequestParam(name = "page", defaultValue = "0") int page,
	            @RequestParam(name = "size", defaultValue = "10") int size) {
			
	    	Pageable pageable = PageRequest.of(page, size, Sort.by("no").descending());

	        // Spring Data JPA를 사용하여 페이징 처리된 공지사항 목록을 가져옴
	        Page<BoardQuestion> boardq = boardQRepository.findAll(pageable);

	        // 페이지 관련 정보를 Thymeleaf로 전달
	        mm.addAttribute("qList", boardq);
	        mm.addAttribute("currentPage", page);
	        mm.addAttribute("totalPages", boardq.getTotalPages());
	        System.out.println("문의 리스트>>> ");
	        return "boardq/boardQList";
		}
		
		
		// 문의 글쓰기 get
		@GetMapping("/qnaBoard/write")
		public String questionWrite() {
			System.out.println("문의 글쓰기>>> ");
			return "boardq/boardQWrite";
		}
		
		// 문의 글쓰기 post
		@PostMapping("/qnaBoard/write")
		public String writeReg(BoardQuestion bq, HttpServletRequest request) {
		    bq.setRegDate(LocalDateTime.now());
		    
		    boardQRepository.save(bq);
		    return "redirect:/qnaBoard/list";
		}

		
		// 문의 상세
		@GetMapping("/qnaBoard/detail/{id}")
		public String questionDetail(@PathVariable int id, Model mm) {
		    Optional<BoardQuestion> qboard = boardQRepository.findById(id);
		    if (qboard.isPresent()) {
		        BoardQuestion board = qboard.get();
		        // 답글 목록을 작성일자(regDate)를 기준으로 역순으로 정렬
		        Collections.sort(board.getCommentq(), Comparator.comparing(BoardQComment::getRegDate).reversed());
		        mm.addAttribute("qDetail", board);
		        boardQRepository.save(board);
		        System.out.println("문의 상세페이지>>> ");
		        return "boardq/boardQDetail";
		    } else {
		        return "redirect:/qnaBoard/list";
		    }
		}
		
		// 문의 삭제
		@GetMapping("/qnaBoard/delete/{id}")
		public String questionDelete(@PathVariable int id) {
			
			boardQRepository.deleteById(id); 
			System.out.println("문의 삭제>>> ");
			return "redirect:/qnaBoard/list";
		}
		
		// 문의 수정 get
		@GetMapping("/qnaBoard/modify/{id}")
		public String questionModify(@PathVariable int id, Model mm) {
			Optional<BoardQuestion> qboard = boardQRepository.findById(id);
			
			if(qboard.isPresent()) {
				BoardQuestion board = qboard.get();
				mm.addAttribute("qModify", board);
				System.out.println("문의 수정>>> ");
				return "boardq/boardQModify";
			}else {
				return "redirect:/qnaBoard/list";
			}		
		}
		
		// 문의 수정 post	
		@PostMapping("/qnaBoard/modify/{id}")
		public String questionModifyReg(@PathVariable int id, BoardQuestion bq) {
		    Optional<BoardQuestion> qboard = boardQRepository.findById(id);
		    if (qboard.isPresent()) {
		        BoardQuestion newboard = qboard.get();
		        newboard.setContent(bq.getContent());
		        newboard.setTitle(bq.getTitle());
		        newboard.setModiDate(LocalDateTime.now()); // 수정일 업데이트 추가

		        boardQRepository.save(newboard);
		    }
		    return "redirect:/qnaBoard/detail/" + id;
		}

		
		// 문의 댓글
		@PostMapping("/qnaBoard/addQComment/{id}")
		public String addComment(@PathVariable int id, String content, String admin_id) {
		    Optional<BoardQuestion> opboard = boardQRepository.findById(id);
		    if (opboard.isPresent()) {
		    	BoardQuestion newboard = opboard.get();
		        BoardQComment comment = new BoardQComment();
		        comment.setQuestionId(newboard);
		        comment.setContent(content);
		        comment.setAdmin_id(admin_id);
		        comment.setRegDate(LocalDateTime.now());
		        newboard.getCommentq().add(comment);
		        boardQRepository.save(newboard); // 게시글 저장
		    }

		    return "redirect:/qnaBoard/detail/{id}"; // 게시글 상세 페이지로 리다이렉트
		}
		
		// 문의 댓글 삭제
		@GetMapping("/qnaBoard/commDelete/{id}")
		public String commDelete(@PathVariable int id, @RequestParam(name = "qid") int questionId) {
		    Optional<BoardQComment> comment = commQRepository.findById(id); // 댓글을 ID로 조회
		    if (comment.isPresent()) {
		    	commQRepository.deleteById(id); // 댓글 삭제
		    }
		    
		    return "redirect:/qnaBoard/detail/" + questionId; // 문의 상세 페이지로 리다이렉트하고 questionId를 전달하여 해당 문의 상세 페이지로 이동
		}

		
	
	//============================공지게시판=========================================================//
	
	
		// 공지사항 리스트
		@GetMapping("noticeBoard/list")
		public String noticeList(Model mm, 
				@RequestParam(name = "page", defaultValue = "0") int page,
	            @RequestParam(name = "size", defaultValue = "10") int size) {
	        // 페이지 및 사이즈를 기준으로 Pageable 객체 생성
	    	Pageable pageable = PageRequest.of(page, size, Sort.by("no").descending());

	        // Spring Data JPA를 사용하여 페이징 처리된 공지사항 목록을 가져옴
	        Page<BoardNotice> nboard = boardNRepository.findAll(pageable);

	        // 페이지 관련 정보를 Thymeleaf로 전달
	        mm.addAttribute("nList", nboard);
	        mm.addAttribute("currentPage", page);
	        mm.addAttribute("totalPages", nboard.getTotalPages());
	        System.out.println("공지사항 리스트>>> ");
	        return "board/boardNList";
		}
		
		
		
		// 공지사항 글쓰기 get
		@GetMapping("noticeBoard/write")
		public String noticeWrite() {
			System.out.println("공지사항 글쓰기>>> ");
			return "board/boardNWrite";
		}
		
		// 공지사항 글쓰기 post
		@PostMapping("noticeBoard/write")
		public String writeReg(BoardNotice bn, HttpServletRequest request) {
		    bn.setRegDate(LocalDateTime.now());

		    // 파일 업로드와 JPA 저장을 BoardNoticeService를 통해 수행
		    MultipartFile file = bn.getFile(); // BoardNotice 객체에서 파일을 가져옴
		    service.uploadAndSave(bn, file); // 파일 업로드 및 JPA 저장 처리
		    boardNRepository.save(bn);
		    
		    return "redirect:/noticeBoard/list";
		}

		
		// 공지사항 상세
		@GetMapping("noticeBoard/detail/{id}")
		public String noticeDetail(@PathVariable int id, Model mm) {
			Optional<BoardNotice> nboard = boardNRepository.findById(id);
			
			if(nboard.isPresent()) {
				BoardNotice board = nboard.get();
				boardNRepository.save(board);
				mm.addAttribute("nDetail", board);
				System.out.println("공지사항 상세페이지>>> ");
				return "board/boardNDetail";
				
			}else {
				return "redirect:/noticeBoard/list";
			}
		}
		
		// 공지사항 삭제
		@GetMapping("noticeBoard/delete/{id}")
		public String noticeDelete(@PathVariable int id) {
			
			boardNRepository.deleteById(id); 
			System.out.println("공지사항 삭제>>> ");
			return "redirect:/noticeBoard/list";
		}
		
		// 공지사항 수정 get
		@GetMapping("noticeBoard/modify/{id}")
		public String noticeModify(@PathVariable int id, Model mm) {
			Optional<BoardNotice> nboard = boardNRepository.findById(id);
			
			if(nboard.isPresent()) {
				BoardNotice board = nboard.get();
				mm.addAttribute("nModify", board);
				System.out.println("공지사항 수정>>> ");
				return "board/boardNModify";
				
			}else {
				return "redirect:/noticeBoard/list";
			}		
		}
		
		// 공지사항 수정 post	
		@PostMapping("noticeBoard/modify/{id}")
		public String noticeModifyReg(@PathVariable int id, BoardNotice bd, @RequestParam("file") MultipartFile file) {
		    Optional<BoardNotice> nboard = boardNRepository.findById(id);
		    if (nboard.isPresent()) {
		        BoardNotice newboard = nboard.get();
		        newboard.setContent(bd.getContent());
		        newboard.setTitle(bd.getTitle());
		        newboard.setModiDate(LocalDateTime.now());

		        if (!file.isEmpty()) {
		            // 파일이 업로드되었을 때만 파일 업로드 및 정보 업데이트
		            service.uploadAndSave(newboard, file);
		        }

		        boardNRepository.save(newboard);
		    }
		    return "redirect:/noticeBoard/detail/" + id;
		}
	
	
	
	
	
	
	
	
	
	
	
	
}