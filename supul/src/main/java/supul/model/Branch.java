package supul.model;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name ="branch")
@Data
public class Branch {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="branch_id")
	int branchId; 
	String name;
	String phone;
	String address; 
	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "branch")
	List<Thema> tm;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "branch")
	List<Reservation> rv;

	
	
}
 