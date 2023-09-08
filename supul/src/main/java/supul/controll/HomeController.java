package supul.controll;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {

	@RequestMapping("/")
	String homeGo() {
		System.out.println("홈이다");
		return "/home";
	} 
	
	@RequestMapping("/calendar")
//	@ResponseBody
	String homeGo2() {
		System.out.println("홈이다");
		return "sales/calendar";
	}
	@RequestMapping("/pay2")
//	@ResponseBody
	String homeGo3() {
		System.out.println("홈이다");
		return "pay/paytest";
	}

	
	
}

 
