package kr.co.Lemo.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @since 2023/03/25
 * @author 서정현
 * @apiNote EmailService
 */

@Slf4j
@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    // @since 2023/03/25
    public void emailAuth(Map<String, Object> map) {

        // 인증코드 생성(6자리수 랜덤 생성)
        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);

        String subject = "Lemo 인증코드 입니다.";
        String content = "<h1>인증코드는 " + code + "입니다.</h1>";
        String fromEmail = "ooo3345sjh@gmail.com";

        int status = sendEmail((String)map.get("email"), fromEmail, subject, content);

        map.put("status", status);
        map.put("code", code);
    }

    // @since 2023/03/25
    public int qnaSendToSeller(String toEmail,
                               String fromEmail,
                               String title,
                               String content)
    {
        return sendEmail(toEmail, fromEmail, title, content);
    }

    // @since 2023/03/25
    private int sendEmail(String toEmail, String fromEmail, String subject, String content) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        int status = 0;
        try {
            /**
             * 첨부 파일(Multipartfile) 보낼거면 true
             */
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom("Lemo <" + fromEmail + ">");
            /**
             * html 템플릿으로 보낼거면 true
             * plaintext로 보낼거면 false
             */
            mimeMessageHelper.setText(content, true);

            javaMailSender.send(mimeMessage);
            log.info("send email: {}", content);
            status = 1;
        } catch (MessagingException e) {
            log.error("[EmailService.send()] error {}", e.getMessage());
            status = 0;
        }
        return status;
    }


}