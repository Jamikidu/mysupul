package supul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import supul.model.User;


public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUserName(String username);

	User findByEmail(String email);
	
	boolean existsByEmail(String email);
	
	User findByUserId(String userid);

	User findByUserIdAndEmail(String userId, String email);
	
	User findByUserPw(String userpw);
	
	User findByUserNameAndEmailAndPhone(String userName, String email, String phone);
	
	User findUserIdByEmail(String email);
	
	User findUserPwByEmail( String email);
}