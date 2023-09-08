package supul.controll;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import supul.model.User;
import supul.repository.UserRepository;
import supul.service.PasswordChange;

@Controller
public class SignUpController {

	@Resource
    UserRepository userRepository;

   
    
    @RequestMapping("/main")
    public String homeGo(Model model) {
        // 사용자 이름 가져오기
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String username = (String) request.getSession().getAttribute("username");

        model.addAttribute("username", username);
        return "main";
    }
    
    
    @GetMapping("user/alert")
    public String alert() {
    	return "user/alert";
    }
    
    @GetMapping("user/alert2")
    public String alert2() {
    	return "user/alert2";
    }
    
    @GetMapping("user/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("member", new User());
        return "user/signup";
    }

    @PostMapping("user/signup")
    public String processSignupForm(@Valid User user, BindingResult br, @RequestParam("userpw1") String userPw1) {
    	 if (br.hasErrors()) {
             return "user/signup";
         }
    	if (!user.getUserPw().equals(userPw1)) {
            br.rejectValue("userpw", null, "비밀번호가 일치하지 않습니다.");
            return "user/signup";
        }

    	 userRepository.save(user);
        
        return "redirect:/user/login";
    }
    
    // 아이디 중복 검사
    @GetMapping("/user/checkUsername")
    @ResponseBody
    public String checkUsername(@RequestParam("username") String username) {
    	User existingMember =  userRepository.findByUserId(username);
        if (existingMember != null) {
            return "duplicate";
        }
        return "unique";
    }

    // 이메일 중복 검사
    @GetMapping("user/checkEmail")
    @ResponseBody
    public String checkEmail(@RequestParam String email) {
        boolean isEmailDuplicate =  userRepository.existsByEmail(email);
        if (isEmailDuplicate) {
            return "duplicate"; // 중복된 이메일 주소를 발견한 경우
        } else {
            return "available"; // 중복된 이메일 주소가 없는 경우
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 세션에서 사용자 정보 삭제
        session.removeAttribute("username");
        return "redirect:/"; // 로그아웃 후 메인 페이지로 리디렉션
    }
    
    
    
    @GetMapping("user/login")
    public String showLoginForm() {
        return "user/login";
    }
    
    @PostMapping("user/login")
    public String processLogin(@RequestParam String userid, @RequestParam String userpw, HttpSession session, Model model) {
    	User member =  userRepository.findByUserId(userid);
        if (member != null && member.getUserPw().equals(userpw)) {
            session.setAttribute("username", member.getUserName());
            model.addAttribute("username", member.getUserName());
            return "redirect:/"; // 메인 페이지로 리디렉션
        } else {
            return "redirect:/user/alert"; // 로그인 실패 시 alert 페이지로 리디렉션
        }
    }
    
    @GetMapping("user/password-change")
    public String showPasswordChangeForm(Model model, HttpSession session) {
    	// 현재 로그인한 사용자 정보 확인
    	String username = (String) session.getAttribute("username");

        // 비밀번호 변경 폼을 표시하기 전에 사용자 정보를 가져와서 모델에 추가
    	User member =  userRepository.findByUserName(username);
        PasswordChange passwordChange = new PasswordChange(); // 새로운 PasswordChange 객체 생성
        model.addAttribute("passwordChange", passwordChange); // "passwordChange"라는 이름으로 모델에 추가
        return "user/password-change";
    }

    @PostMapping("user/password-change")
    public String processPasswordChange(@ModelAttribute("passwordChange") PasswordChange passwordChange, BindingResult br, HttpSession session) {
        if (br.hasErrors()) {
            return "user/password-change";
        }

        // 세션에서 현재 로그인한 사용자 정보 가져오기
        String username = (String) session.getAttribute("username");

        // 사용자 정보 확인 (현재 로그인한 사용자의 정보를 가져오거나, 이를 통합하는 방법을 선택하세요)
        User user =  userRepository.findByUserName(username);

        // 현재 비밀번호가 일치하는지 확인
        if (!user.getUserPw().equals(passwordChange.getCurrentPassword())) {
            br.rejectValue("currentPassword", null, "현재 비밀번호가 일치하지 않습니다.");
            return "user/password-change";
        }

        // 새 비밀번호와 새 비밀번호 확인이 일치하지 않는 경우
        if (!passwordChange.getNewPassword().equals(passwordChange.getConfirmNewPassword())) {
            br.rejectValue("confirmNewPassword", null, "새 비밀번호 확인이 일치하지 않습니다.");
            return "user/password-change";
        }
        
        // 현재 비밀번호와 새 비밀번호가 동일한 경우 변경하지 않음
        if (user.getUserPw().equals(passwordChange.getNewPassword())) {
            br.rejectValue("currentPassword", null, "현재 비밀번호와 새 비밀번호가 동일합니다.");
            return "user/password-change";
        }

        // 새 비밀번호로 업데이트 (비밀번호 암호화는 프로젝트에 따라 다를 수 있습니다)
        user.setUserPw(passwordChange.getNewPassword());
        user.setUserPw1(passwordChange.getNewPassword());
        userRepository.save(user);

        // 로그아웃: 세션에서 사용자 정보 삭제
        session.removeAttribute("username");

        // 변경 완료 후 메인 페이지로 리디렉션 또는 다른 처리 수행
        return "redirect:/user/alert2";
    }
    
    //이메일, 번호 변경
    @GetMapping("user/edit-profile")
    public String showEditProfileForm(Model model, HttpSession session) {
        // 데이터베이스에서 사용자 정보를 검색합니다.
        String username = (String) session.getAttribute("username");
        User user =  userRepository.findByUserName(username);

        // 세션에 사용자 정보를 설정합니다.
        session.setAttribute("username", user.getUserName());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("phone", user.getPhone());

        // 양식을 렌더링하기 위해 모델에 member 객체를 추가합니다.
        model.addAttribute("member", user);

        return "user/edit-profile";
    }

    @PostMapping("user/edit-profile")
    public String processEditProfile(Model model, HttpSession session, @ModelAttribute("member") @Valid User updatedMember, BindingResult br) {
        // 세션에서 현재 사용자 정보를 검색합니다.
        String currentUsername = (String) session.getAttribute("username");

        // 데이터베이스에서 사용자의 현재 정보를 검색합니다.
        User user =  userRepository.findByUserName(currentUsername);

        // 편집된 데이터로 사용자 정보를 업데이트합니다.
        user.setUserName(updatedMember.getUserName()); // 사용자 이름 업데이트
        user.setEmail(updatedMember.getEmail());
        user.setPhone(updatedMember.getPhone());

        

        // 유효성 검사에 성공한 경우 저장 및 리디렉션
        userRepository.save( user);
        session.setAttribute("username", updatedMember.getUserName());
        
        // 메인 페이지로 리디렉션
        return "redirect:/";
    }




    
}
