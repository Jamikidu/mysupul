package supul.repository;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import supul.model.Branch;
import supul.model.Reservation;
import supul.model.Thema;

public interface SaleRepository extends JpaRepository<Reservation, Integer>{

	// 검색 및 페이징처리
	Page<Reservation> findByUserNameContainingAndBranchName(String themaName,String branchName, Pageable pageable);
	Page<Reservation> findByBranchName(String branchName, Pageable pageable);
	List<Reservation> findByUserNameContaining(String themaName);
	Reservation findByRvId(int rvId);
	
	@Query("SELECT SUM(s.price) FROM Reservation s WHERE s.date BETWEEN :startDate AND :endDate")
	BigDecimal TotalSales(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	// 테마별 총 금액
	@Query("SELECT s.themaName, SUM(s.price) FROM Reservation s ")
	List<Object[]> getThema_total();

	// 테마별 총금액, 가장 큰 금액쓴 사람 리스트
	@Query("SELECT s.themaName, SUM(s.price), MAX(s.userName)FROM Reservation s GROUP BY s.themaName")
	List<Object[]> thema_price();
	
	@Query("SELECT s.themaName, SUM(s.price), MAX(s.userName)FROM Reservation s WHERE s.date BETWEEN :startDate AND :endDate GROUP BY s.themaName")
	List<Object[]> thema_priceDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	//날짜별 총금액 리스트
	@Query("SELECT s.date, SUM(s.price) FROM Reservation s GROUP BY s.date")
	List<Object[]> date_price();
	
	//지점별 총금액 리스트 
	@Query("SELECT b.name , SUM(s.price) " +
	           "FROM Branch b " +
	           "LEFT JOIN b.tm.sale s " +
	           "GROUP BY b.name")
	    List<Object[]> branchListTotal();
	    
    //지정한 지점 총금액 리스트
	 @Query("SELECT b.name, SUM(s.price)FROM Branch b LEFT JOIN b.tm.sale s " +
	           "WHERE b.name = :branchName GROUP BY b.name")
    Object[] branchTotal(@Param("branchName") String branchName);
	 //지정한 지점 테마별 총금액
	 

		@Query("SELECT b.name , t.title , SUM(r.price)" +
		           "FROM Branch b " +
		           "JOIN b.tm t " +
		           "LEFT JOIN t.sale r " +
		           "WHERE b.name = :branchName " +
		           "GROUP BY b.name, t.title " +
		           "ORDER BY b.name, t.title")
		    List<Object[]> branchThemaTotal(@Param("branchName") String branchName);

		
}
