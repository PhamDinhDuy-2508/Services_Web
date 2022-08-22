package com.Search_Thesis.Search_Thesis.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class Email_Services {
    private  String email ;

    @Autowired
    private JavaMailSender mailSender;



    public void Send_Email(String email , String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("duypham258138@gmail.com", "CKT Support");

        helper.setTo(email);

        String subject = "Here's the link to reset your password";
        System.out.println(link);

        String content = "<p>Xin Chào</p>"
                + "<p>Bạn đã yêu cầu reset mật khẩu của bạn cho chúng tôi</p>"
                + "<p>Bạn hãy click vào đường link dưới đây để có thể thay đổi mật khẩu của bạn</p>"
                + "<p><a href=" + link + ">Change my password</a></p>"
                + "<br>"
                + "<p> Bỏ qua thư này nếu bạn đã nhớ lại được mật khẩu"
                + "Hoặt bạn không có ý định gửi thư này.</p>";
        System.out.println(content);
        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);

    }
}
