package supul.controll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.annotation.Resource;
import supul.model.Branch;
import supul.repository.PayRepository;
import supul.repository.SaleRepository;
import supul.repository.board.BranchRepository;
import supul.service.BranchMapper;
import supul.service.SaleService;

@Controller
@RequestMapping("/branch")
public class BranchController {
	
	//지점관리
	@Autowired
	@Qualifier("branchMapper")
	BranchMapper branchMapper;
	
	@Autowired
	private SaleService stat;

	@Resource
	SaleRepository saleRepository;
	@Resource
	BranchRepository branchRepository;
	@Resource
	PayRepository payRepository;
	
    @GetMapping("/branchList")
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
		
		model.addAttribute("list", searchList);
		model.addAttribute("pageable", pageable);
		model.addAttribute("name", name);
		model.addAttribute("sortBy", sortBy);
        return "branch/branchList";
    }

    @GetMapping("/form")
    public String showBranchForm(Model model) {
        model.addAttribute("branch", new Branch());
        return "branch/branchReg";
    }

    @PostMapping("/add")
    public String submitBranch(@ModelAttribute Branch branch) {
        branchMapper.insertBranch(branch);
        return "redirect:/branch/branchList";
    }
    
    // 지점 정보 수정 폼 보기
    @GetMapping("/update/{branchId}")
    public String showBranchUpdateForm(@PathVariable("branchId") int branchId, Model model) {
        Branch branch = branchMapper.selectById(branchId);
        System.out.println(branch.getBranchId());
        model.addAttribute("branch", branch);
        return "branch/branchUpdate";
    }

    // 지점 정보 수정 처리
    @PostMapping("/update")
    public String submitBranchUpdate(@ModelAttribute Branch branch) {
        branchMapper.updateBranch(branch);
        return "redirect:/branch/branchList";
    }

    // 지점 정보 삭제 폼 보기
    @GetMapping("/delete/{branchId}")
    public String showBranchDeleteForm(@PathVariable("branchId") int branchId, Model model) {
        Branch branch = branchMapper.selectById(branchId);
        model.addAttribute("branch", branch);
        return "branch/branchDelete";
    }

    // 지점 정보 삭제 처리
    @PostMapping("/delete")
    public String submitBranchDelete(@ModelAttribute Branch branch) {
        branchMapper.deleteBranch(branch.getBranchId());
        return "redirect:/branch/branchList";
    }

}
