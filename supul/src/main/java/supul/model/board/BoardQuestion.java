package supul.model.board;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="qna_board")
@Data
public class BoardQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private int no;

    @Column(name = "user_id", nullable = false)
    private String user_id = ""; // 빈 문자열로 초기화!

    @Column(name = "branch_id", nullable = false)
    private int branch_id;

    @Column(name = "title", nullable = false, length = 250)
    private String title;

    @Column(name = "content", nullable = false, length = 999)
    private String content;
    
    @Column(name = "type", nullable = false, length = 250)
    private String type;

    @Column(name = "regDate")
    private LocalDateTime regDate;

    @Column(name = "modiDate")
    private LocalDateTime modiDate;
    
    // 나는 하나 // mappedBy JoinColumn의 name이 아니라 BoardQcomment에서 BoardQuestion의 변수 questionId;를 불러옴.
    @OneToMany(mappedBy = "questionId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardQComment> commentq = new ArrayList<>();

}
