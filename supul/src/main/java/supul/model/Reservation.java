package supul.model;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.apache.ibatis.type.Alias;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
@Alias("rvDTO")
@Entity
@Table(name="reservation")
@Data
public class Reservation {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name="rv_id")
	    int rvId;
	    String rvNum;
	    @Column(name="user_name")
	    String userName;
	    @Column(name="thema_name")
	    String themaName;
	     
	    LocalTime time;
	    LocalDate date;
	    @Column(name="rv_people")
	    int rvPeople;
	    @Column(name="rv_price")
	    int rvPrice;    
	    int price;    
 
	    @Column(name = "rv_date")
	    LocalDateTime rvDate;
	    
	    @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "branch_id")
	    Branch branch;
	    
	    @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "thema_id")
	    Thema thema;
	    
	    // 결제 상태 추가
	    @Column(nullable = false, columnDefinition = "boolean default false")
	    boolean paid;

	    // 노쇼 상태 추가
	    @Column(nullable = false, columnDefinition = "boolean default false")
	    boolean noShow;
	    
	    @OneToOne(mappedBy = "reservation",fetch = FetchType.EAGER)
	    Pay pay;
		
	    
	    
}
