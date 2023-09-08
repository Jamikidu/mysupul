package supul.repository;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



import supul.model.Pay;






public interface PayRepository extends JpaRepository<Pay, Integer> {
	Pay findByReservationRvId(int rvid);
	 Page<Pay> findAll(Pageable pageable);

}
 