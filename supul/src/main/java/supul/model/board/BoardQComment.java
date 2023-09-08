package supul.model.board;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "admin_comment")
@Data
public class BoardQComment {
	
	@Id
	@Column(name = "comment_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int no;
	
	@ManyToOne(fetch = FetchType.LAZY)					// 내가 다수 
	@JoinColumn(name = "question_id", nullable = false)	// 부모 엔티티와 자식 엔티티 사이의 관계를 매핑할 때 // BoardQuestion JoinColumn(연관관계를 나타냄)을 사용해야됨. // @ManyToOne, @OneToMany, @OneToOne, @ManyToMany 과 함께 사용됨 //외래 키(Foreign Key)로 매핑될 때 사용
	BoardQuestion questionId;	// BoardQeustion에서 mappedBy 와 동일하게 지정
	
	@Column(name = "branch_id", nullable = false)
	private int branch_id;
	
	@Column(name = "admin_id", nullable = false)
	private String admin_id;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "regDate")
	private LocalDateTime regDate;
	
	@Column(name = "modiDate")
	private LocalDateTime modiDate;

}
