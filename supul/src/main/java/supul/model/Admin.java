package supul.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Table(name="admin")
@Data
public class Admin {
	
	
	@Id
	@Column(name="adminid")
	String adminid;
	
	@NotEmpty(message = "비밀번호를 입력하세요.")
    @Pattern(regexp  = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,10}$", message="비밀번호는 문자+숫자 6~10자입니다.")
	@Column(name="admin_pw",nullable= false)
	String admin_pw;
	
	@NotEmpty(message = "비밀번호확인을 입력하세요.")
    @Column(name="password1" ,nullable= false)
    String admin_pw1;
	
	@Column(name="admin_name")
	String admin_name;
	
	@Column(name="admin_phone")
	String phone;
	
	@Column(name="admin_email")
	String email;
	
	@Column(name="admin_reg_date")
	LocalDateTime reg_date;
	
	@Column(name="admin_birth")
	String birth;
	

	
}