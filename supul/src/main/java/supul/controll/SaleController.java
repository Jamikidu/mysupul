package supul.controll;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Predicate;
import supul.model.Branch;
import supul.model.Pay;
import supul.model.Reservation;
import supul.model.Thema;
import supul.repository.PayRepository;
import supul.repository.SaleRepository;
import supul.repository.board.BranchRepository;
import supul.service.SaleService;

@Controller
@RequestMapping("sales")
public class SaleController {

	@Autowired
	private SaleService stat;

	@Resource
	SaleRepository saleRepository;
	@Resource
	BranchRepository branchRepository;
	@Resource
	PayRepository payRepository;

	//통계 컨트롤러
	@GetMapping("/chart/{branchName}")
	public String getTotalSalesAmount(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@PathVariable String branchName, Model model) {

		BigDecimal totalSalesPrice = stat.totalSales(startDate, endDate);
		model.addAttribute("salesData", totalSalesPrice);

		List<Object[]> thema_price = saleRepository.thema_price();
		model.addAttribute("thema_price", thema_price);

		List<Object[]> thema_priceDate = stat.thema_price(startDate, endDate);
		model.addAttribute("thema_priceDate", thema_priceDate);

		List<Object[]> date_price = saleRepository.date_price();
		model.addAttribute("date_price", date_price);

		Object[] branch_Total = stat.branch_Total(branchName);
		model.addAttribute("branch_Total", branch_Total);

		List<Object[]> branchListTotal = saleRepository.branchListTotal();
		model.addAttribute("branchListTotal", branchListTotal);

		List<Object[]> branchThemaTotal = stat.branchThemaTotal(branchName);
		model.addAttribute("branchThemaTotal", branchThemaTotal);
	
		return "sales/sales"; // 결과를 보여줄 뷰 페이지의 이름

	}


	//검색
	@GetMapping("list")
	String rvlist(String userName, @PageableDefault(size = 8,sort = "rvDate" , direction = Direction.DESC ) Pageable pageable,
			@RequestParam(name = "sortBy", defaultValue = "rvDate") String sortBy, Model model) {
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
	            Sort.by(Sort.Direction.ASC, sortBy)); 
		Page<Reservation> searchList;

		    searchList = stat.searchTm(pageable);
		   
	 List<Branch> branchList = branchRepository.findAll(); // BranchService를 이용하여 모든 지점을 가져오는 예시
	  model.addAttribute("branchList", branchList);
		  
		   
		model.addAttribute("searchList", searchList);
		model.addAttribute("pageable", pageable);
		model.addAttribute("userName", userName);
		model.addAttribute("sortBy", sortBy);
		
		return "pay/reservation";
	}
	
	
	@GetMapping("/list/{branchName}")
	public String getAll(String userName,@PathVariable String branchName, @PageableDefault(size = 8,sort = "rvDate" , direction = Direction.DESC ) Pageable pageable,
			@RequestParam(name = "sortBy", defaultValue = "rvDate") String sortBy, Model model) {
		 // 정렬을 적용합니다. (Spring Data JPA의 PageRequest 객체를 사용)
	    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
	            Sort.by(Sort.Direction.ASC, sortBy)); 
	    List<Branch> branchList = branchRepository.findAll(); // BranchService를 이용하여 모든 지점을 가져오는 예시
		  model.addAttribute("branchList", branchList);
	   
	    Page<Reservation> searchList;
		if(userName==null || userName.isEmpty()) {  
			 searchList = stat.searchbr(branchName, pageable);
		}else {
			 searchList = stat.searchTm(userName,branchName, pageable);
		}
	   
		   
		model.addAttribute("searchList", searchList);
		model.addAttribute("pageable", pageable);
		model.addAttribute("userName", userName);
		model.addAttribute("sortBy", sortBy);
		
		return "pay/reservation";
	}
	
	//노쇼처리
	 @PostMapping("/list/{branchName}/{rvId}/updateNoShow")
	    public String updateNoShowStatus(@PathVariable("rvId") int rvId) {
	     if(saleRepository.findByRvId(rvId).isNoShow()==false) {   
		 stat.updateNoShowStatus(rvId, true);} // 노쇼 상태를 true로 업데이트
	     else{
	    	 stat.updateNoShowStatus(rvId, false);
	   	
	     }
	        return "redirect:/sales/list/{branchName}"; // 노쇼 상태 업데이트 후 다시 목록 페이지로 리다이렉트
	    }
	//결제처리
	  @PostMapping("/list/{branchName}/{rvId}/updatepaid")
	    public String updatePaidStatus(@PathVariable("rvId") int rvId) {
		  Reservation a =stat.pay(rvId);
		  if(saleRepository.findByRvId(rvId).isPaid()==false) {   
		  stat.updatePaidStatus(rvId, true);
		  Pay b =new Pay();
			 b.setReservation(a);
			 b.setTotalprice(a.getPrice());
			 b.setSaleDate(LocalDateTime.now());
			 payRepository.save(b);
		  }else {
			stat.updatePaidStatus(rvId, false);
			
			Pay b =payRepository.findByReservationRvId(rvId);
			payRepository.deleteById(b.getPayId());
		  }
		 stat.pay(rvId);
		  
		
		 return "redirect:/sales/list";
	    }
	
	
	
	@GetMapping("/brlist")
	public String getBrunch(String name, @PageableDefault(size = 8,sort = "rvDate" , direction = Direction.DESC ) Pageable pageable,
			@RequestParam(name = "sortBy", defaultValue = "name") String sortBy, Model model) {
		 // 정렬을 적용합니다. (Spring Data JPA의 PageRequest 객체를 사용)
	    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
	            Sort.by(Sort.Direction.ASC, sortBy)); 
		
	    Page<Branch> searchList= stat.brList(pageable);

		   if (name == null || name.isEmpty()) {
		        // themaName이 없거나 비어있는 경우 전체 리스트 조회
		        searchList = stat.brList(pageable); 
		    } else {
		        // themaName이 있는 경우 해당 테마로 필터링하여 조회
		        searchList = stat.brSertchList(name, pageable);
		    }
		
		model.addAttribute("searchList", searchList);
		model.addAttribute("pageable", pageable);
		model.addAttribute("name", name);
		model.addAttribute("sortBy", sortBy);
		
		return "pay/brlist";
	}
   
}

