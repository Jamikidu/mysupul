package supul.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;


@Service
public class EmailService {

    @Resource
    private JavaMailSender javaMailSender;

    
    public void sendSimpleMail() {
        
        ArrayList<String> toUserList = new ArrayList<>();
        
        
        toUserList.add("kangjh1994@gmail.com");
        //toUserList.add("rkdwjdgus777@naver.com");
        
        
        int toUserSize = toUserList.size();
        
        
        SimpleMailMessage simpleMessage = new SimpleMailMessage();
        
        
        simpleMessage.setTo((String[]) toUserList.toArray(new String[toUserSize]));
        
        
        simpleMessage.setSubject("예약시간 2시간 전입니다!");
        
        
        simpleMessage.setText("잊지말고 방문해주세요!");
        
        
        javaMailSender.send(simpleMessage);
    }
    public void sendMail() throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // 이메일 수신자 설정
        List<String> toUserList = new ArrayList<>();
        toUserList.add("kangjh1994@google.com");
        int toUserSize = toUserList.size();
        helper.setTo((String[]) toUserList.toArray(new String[toUserSize]));

        // 이메일 제목 설정
        helper.setSubject("HTML 파일 이메일 테스트");

        // HTML 파일을 ClassPath에서 읽어옴
        //ClassPathResource htmlFile = new ClassPathResource("welcome.html");

        // HTML 파일을 본문으로 설정 (true 파라미터는 HTML을 사용한다는 의미)
        helper.setText("<html><body><h1>Hello, World!</h1><p>This is a test email with HTML content.</p></body></html>", true);


        // 이메일 보내기
        javaMailSender.send(message);
    }
	
}