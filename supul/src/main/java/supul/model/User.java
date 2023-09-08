package supul.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
@Entity
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	int ranking;
	String wishList;
	
	
    @NotEmpty(message = "id를 입력하세요.")
    @Pattern(regexp="^[a-zA-Z0-9]{3,10}$", message = "아이디는 영문,숫자 3~10자 입니다.")
    @Column(name="user_id", unique = true)
    String userId;
    
    @NotEmpty(message = "이름을 입력하세요.")
    @Pattern(regexp="[가-힣]{2,10}", message = "한글 2~10자 입니다.")
    @Column(name="user_name", nullable=false)
    String userName;
    
    @NotEmpty(message = "비밀번호를 입력하세요.")
    @Pattern(regexp = "^.*(?=^.{6,12}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", message="비밀번호는 영문자+숫자+특수문자 6~12자입니다.")
    @Column(name="user_pw" ,nullable= false)
    String userPw;
    
    @NotEmpty(message = "비밀번호확인을 입력하세요.") 
    @Column(name="user_pw1" ,nullable= false)
    String userPw1;
    
    @NotEmpty(message = "생년월일을 입력하세요.")
    @Column(name="birth" ,nullable= false)
    String birth;
    
    @NotEmpty(message = "이메일을 입력하세요.")
    @Column(name="email" ,nullable= false)
    String email;
    
    @NotEmpty(message = "연락처를 입력하세요.")
    @Column(name="phone" ,nullable= false)
    String phone;
    
    @Column(name="reg_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime regDate;
    
    public User() {
        this.regDate = LocalDateTime.now(); // 현재 시간으로 초기화
    }
}
