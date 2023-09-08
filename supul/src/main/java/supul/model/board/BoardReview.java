package supul.model.board;

import java.sql.Time;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name="review_board")
@Data
public class BoardReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private int no;

    @Column(name = "user_id", nullable = false)
    private String user_id;

    @Column(name = "thema_id", nullable = false)
    private int thema_id;

    @Column(name = "branch_id", nullable = false)
    private int branch_id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, length = 999)
    private String content;

    @Column(name = "regDate")
    private LocalDateTime regDate;

    @Column(name = "modiDate")
    private LocalDateTime modiDate;

    @Column(name = "cnt")
    private int cnt = 0;
 
    @Column(name = "escTime")
    private Time escTime;

    @Column(name = "clear")
    private boolean clear;

    @Column(name = "grade")
    private int grade;

    @Column(name = "fileName")
    private String fileName;

    @Transient
    private String filePath;

    @Transient
    private MultipartFile file;

//    @OneToMany(mappedBy = "user_id", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<BoardRComment> comments = new ArrayList<>();
}
