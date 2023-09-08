package supul.model.board;

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
@Table(name = "notice_board")
@Data
public class BoardNotice {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id", nullable = false)
    private int no;

    @Column(name = "user_id", nullable = false)
    private String user_id;

    @Column(name = "branch_id", nullable = false)
    private int branch_id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, length = 999)
    private String content;    
    
    @Column(name = "cate", nullable = false)
    private String cate; 
    
    @Column(name = "regDate", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "modiDate")
    private LocalDateTime modiDate;

    @Transient
    private MultipartFile file;

    @Column(name = "fileName")
    private String fileName;

    @Transient
    private String filePath;
    
}