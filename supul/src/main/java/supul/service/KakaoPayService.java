package supul.service;

import org.springframework.http.HttpEntity;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoPayService {
    private final String KAKAO_PAY_API_URL = "https://kapi.kakao.com/v1/payment/ready";
    private final String KAKAO_API_KEY = "209dbebe462b683928e5ce99141eb126";

    public String requestKakaoPay() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization:", "KakaoAK " + KAKAO_API_KEY);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 요청 바디 설정
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("cid", "TC0ONETIME");
        requestBody.add("partner_order_id", "partner_order_id");
        requestBody.add("partner_user_id", "partner_user_id");
        requestBody.add("item_name", "초코파이");
        requestBody.add("quantity", "1");
        requestBody.add("total_amount", "2200");
        requestBody.add("vat_amount", "200");
        requestBody.add("tax_free_amount", "0");
        requestBody.add("approval_url", "https://developers.kakao.com/success");
        requestBody.add("fail_url", "https://developers.kakao.com/fail");
        requestBody.add("cancel_url", "https://developers.kakao.com/cancel");
        
     // HTTP 요청 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = 
        		new HttpEntity<>(requestBody, headers);
      
    

     // REST 템플릿을 사용하여 POST 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                KAKAO_PAY_API_URL,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            // 에러 처리 로직을 추가하세요
            return "카카오페이 API 호출에 실패했습니다.";
        }
    }
}