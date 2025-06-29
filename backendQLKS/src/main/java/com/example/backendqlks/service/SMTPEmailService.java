package com.example.backendqlks.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SMTPEmailService {
    private final JavaMailSender mailSender;

    @Value("${smtp.from}")
    private String fromAddress;

    public SMTPEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordReset(String toEmail, String tempPassword) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(toEmail);
            helper.setSubject("Đặt lại mật khẩu");
            helper.setText(
                    "<p>Mật khẩu mới của bạn là: <strong>" + tempPassword + "</strong></p>" +
                            "<p>Hãy đăng nhập và đổi mật khẩu ngay sau khi vào hệ thống.</p>",
                    true
            );

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Gửi email thất bại", e);
        }
    }

    public void sendBookingExpiredNotification(String toEmail, String guestName, String bookingInfo) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(toEmail);
            helper.setSubject("Thông báo: Phiếu đặt phòng đã hết hạn");

            String content = "<p>Thân gửi quý khách " + guestName + ",</p>"
                    + "<p>Phiếu đặt phòng của bạn đã hết hạn:</p>"
                    + "<p><strong>" + bookingInfo + "</strong></p>"
                    + "<p>Nếu bạn vẫn có nhu cầu đặt phòng, vui lòng thực hiện lại quy trình đặt phòng mới.</p>"
                    + "<p>Trân trọng,</p><p>Roomify.</p>";

            helper.setText(content, true);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Gửi email thông báo hết hạn phiếu đặt phòng thất bại", e);
        }
    }

    public void sendBeforeRentalFormExpiredNotification(String toEmail, String guestName, String rentalFormInfo) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(toEmail);
            helper.setSubject("Thông báo: Phiếu thuê phòng sắp hết hạn");

            String content = "<p>Thân gửi quý khách " + guestName + ",</p>"
                    + "<p>Phiếu thuê phòng của bạn sắp hết hạn:</p>"
                    + "<p><strong>" + rentalFormInfo + "</strong></p>"
                    + "<p>Nếu bạn vẫn có nhu cầu gia hạn, vui lòng thực hiện gia hạn trên web hoặc liên hệ lễ tân.</p>"
                    + "<p>Trân trọng,</p><p>Roomify.</p>";

            helper.setText(content, true);

            mailSender.send(message);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Gửi email thông báo hết hạn phiếu thuê phòng thất bại", e);
        }
    }

    public void sendRentalFormExpiredNotification(String toEmail, String guestName, String rentalFormInfo) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(toEmail);
            helper.setSubject("Thông báo: Phiếu thuê phòng đã hết hạn");

            String content = "<p>Thân gửi quý khách " + guestName + ",</p>"
                    + "<p>Phiếu thuê phòng của bạn sắp hết hạn:</p>"
                    + "<p><strong>" + rentalFormInfo + "</strong></p>"
                    + "<p>Nếu bạn vẫn có nhu cầu sử dụng dịch vụ, vui lòng sử dụng trang web khách sạn hoặc liên hệ lễ tân.</p>"
                    + "<p>Trân trọng,</p><p>Roomify.</p>";

            helper.setText(content, true);

            mailSender.send(message);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Gửi email thông báo hết hạn phiếu thuê phòng thất bại", e);
        }
    }
}
