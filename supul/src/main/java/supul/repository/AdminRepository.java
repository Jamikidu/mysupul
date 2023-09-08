package supul.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import supul.model.Admin;



public interface AdminRepository extends JpaRepository<Admin, Long> {

	Optional<Admin> findByAdminid(String adminid);
	

	
	
	
}
