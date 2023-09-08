package supul.controll;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import supul.model.Admin;
import supul.repository.AdminRepository;

@Controller
public class AdminController {
	
    @Autowired
    AdminRepository adminRepository;
    
    @RequestMapping("/admin")
    public String adminmain() {
    	System.out.println("관리자 홈");
    	return "adminmain";
    }
    
    @GetMapping("admin/alert")
    public String alert() {
    	return "admin/alert";
    }
    @GetMapping("admin/dashboard")
    public String dashboard() {
    	return "admin/dashboard";
    }
    
    @GetMapping("/admin/adminsignup")
    public String showAdminSignupForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin/adminsignup";
    }

    @PostMapping("/admin/adminsignup")
    public String processAdminSignupForm(@Valid Admin admin, BindingResult br) {
        if (br.hasErrors()) {
            return "admin/adminsignup";
        }

        // 관리자 정보를 데이터베이스에 저장
        adminRepository.save(admin);

        return "redirect:/"; // 회원가입 성공 시 대시보드 페이지로 이동
    }
	
	
	@GetMapping("/admin/adminlogin")
    public String adminLoginForm() {
        return "admin/adminlogin";
    }
	
	
	
}
