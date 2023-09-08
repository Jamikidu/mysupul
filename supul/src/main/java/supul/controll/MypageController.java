package supul.controll;

import java.util.Date;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.Resource;
import supul.model.Branch;
import supul.model.Ranking;

import supul.model.Thema;
import supul.model.User;
import supul.repository.ThemaRepository;
import supul.service.ThemaMapper;
import supul.service.UserMapper;

@Controller
@RequestMapping("/mypage")
public class MypageController {

	@Resource
	UserMapper mapper;
	@Resource
	ThemaMapper tmMapper;
	@Autowired
	ThemaRepository themaRepository;
	
	PageData pageData ;
	@RequestMapping("modify")
	public String myModify(Model model, User ud) {
		
		//List<UserData> userList = mapper.userList(ud);
//		
//		model.addAttribute("userList", userList);
//		System.out.println(userList);
		User userData = mapper.getUserById(ud.getUserId());
		    
	    // 수정 후 사용자 정보를 다시 조회하여 모델에 추가
	    List<User> userList = mapper.userList(ud);
	    model.addAttribute("userList", userList);
	    model.addAttribute("userData", userData);
		
		return "mypage/myModify";
	}
	
	
	@GetMapping("insert")
	public String insert(Model model, Ranking rd, Thema td, Branch bd,Pageable pageable,
			 @PageableDefault(size = 8,sort = "rvDate" , direction = Direction.DESC ) PageData pageData) {
		
		Page<Thema> themaList = themaRepository.findAll(pageable);
		List<Branch> brnList = tmMapper.brList(bd);

	
		
		model.addAttribute("brnList", brnList);
		model.addAttribute("themaList", themaList);
		model.addAttribute("pageData", pageData);
		
		System.out.println("brnList : " + brnList);
		return "mypage/rankForm";
	}
	
	@PostMapping("insert")
	String insertReg(Ranking rd,PageData pageData ) {
		mapper.insertRank(rd);
		pageData.setMsg("작성완료");
		pageData.setGoUrl("/mypage/rankList");

		return "mypage/alert";
	}

	@RequestMapping("rankList")
	String list(Model model, Ranking rd, Branch bd, Thema td) {
		
		
	    List<Ranking> rankData = mapper.rankList(rd);
	    List<Branch> brnData = tmMapper.brList(bd);
	    //System.out.println("rankData : " + rankData);
	    
	    for (Ranking rank : rankData) {
	    	rank.setTdTitle(tmMapper.tmTitle(rank.getThemaId()));
	    	rank.setBranchName(tmMapper.brName(rank.getBranchId()));
	    }
	    
	    //System.out.println("rankData : " + rankData);
	    //System.out.println("rd : " + rd);

	    model.addAttribute("brnList", brnData);
	    model.addAttribute("rankList", rankData);


	    return "mypage/rankList";
	}
	

	@RequestMapping("delete/{rankId}")
	String deleteReg(@PathVariable("rankId") int rankId, Ranking rd, Model model) {
		
	    Ranking delData = mapper.rankId(rankId);
	    int cnt = mapper.rankdel(delData);
	    //System.out.println(rankId);
	    if (cnt > 0) {
	    	pageData.setMsg("삭제되었습니다.");
	    	pageData.setGoUrl("/mypage/rankList");
	    }

	    return "mypage/alert";
	}

 
	@RequestMapping("writeList")
	String writeList() {
		
		return "mypage/writeList";
	} 
}
