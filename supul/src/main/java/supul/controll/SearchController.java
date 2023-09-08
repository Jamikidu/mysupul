package supul.controll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import supul.model.User;
import supul.repository.UserRepository;


@Controller
public class SearchController {

	@Autowired
    UserRepository userRepository;
    
 
    @GetMapping("user/search")
    public String search() {
        return "user/search";
    }
   

    @GetMapping("user/idresult")
    public String showFindIdForm() {
        return "user/idresult";
    }

    @PostMapping("user/idresult")
    public String findIdByEmail(@RequestParam("email") String email, Model model) {
        User foundId = userRepository.findUserIdByEmail(email);
        if (foundId != null) {
            model.addAttribute("foundId", foundId);
        } else {
            model.addAttribute("error", "일치하는 ID가 없습니다.");
        }
        return "user/idresult";
    }

    @GetMapping("user/pwresult")
    public String showResetPasswordForm() {
        return "user/pwresult";
    }

    @PostMapping("user/pwresult")
    public String findPasswordByEmail(@RequestParam("userId") String userId, @RequestParam("email") String email, Model model) {
        User foundPw = userRepository.findUserPwByEmail(email);
        if (foundPw != null) {
            model.addAttribute("foundPw", foundPw);
            return "user/pwresult";
        } else {
            model.addAttribute("error", "ID 또는 이메일이 일치하지 않습니다.");
            return "user/pwresult";
        }
    }
   


    
}