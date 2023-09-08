package supul.controll;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import supul.service.KakaoPayService;

@Controller
public class KakaoController {
    private final KakaoPayService kakaoPayService;

    public KakaoController(KakaoPayService kakaoPayService) {
        this.kakaoPayService = kakaoPayService;
    }

    @GetMapping("/pay")
    public String requestPayment(Model model) {
        String kakaoPayResponse = kakaoPayService.requestKakaoPay();
        // kakaoPayResponse를 분석하여 결제 요청 페이지로 이동 또는 결제 결과 페이지로 이동
        // ...
        model.addAttribute("kakaoPayResponse", kakaoPayResponse);
        System.out.println(kakaoPayResponse);
        return "paymentPage";
    }
}