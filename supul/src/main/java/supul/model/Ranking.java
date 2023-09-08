package supul.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Ranking {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="rank_id")
	int rankId;
	int themaId, branchId, people, minute, seconds;
	String userId, date;
	String tdTitle;
	
	
	String branchName;
	
	
//	int  start, limit = 3,pageLimit=4, page, pageStart, pageEnd, pageTotal, , gid, seq;
    
     
     
	
//	public void calc(int total) {
//		
//		
//		start = (page -1) * limit;
//		
//		pageStart = (page -1)/pageLimit*pageLimit +1;
//		pageEnd = pageStart + pageLimit -1;
//		
//		
//		pageTotal = total / limit;
//		if(total % limit != 0) {
//			pageTotal++;
//		}
//		
//		if(pageEnd > pageTotal) {
//			pageEnd = pageTotal;
//		}
//		
//	}
}
