package supul.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;



@Configuration
@ConfigurationProperties(prefix = "kakao.pay")
public class KakaoReadyResponse {

	   private String apiKey;

	    public String getApiKey() {
	        return apiKey;
	    }

	    public void setApiKey(String apiKey) {
	        this.apiKey = apiKey;
	    }
}
