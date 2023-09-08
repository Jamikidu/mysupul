package supul.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



import supul.model.Pay;
import supul.model.Thema;






public interface ThemaRepository extends JpaRepository<Thema, Integer> {
	
	 Page<Thema> findAll(Pageable pageable);
	
}
 