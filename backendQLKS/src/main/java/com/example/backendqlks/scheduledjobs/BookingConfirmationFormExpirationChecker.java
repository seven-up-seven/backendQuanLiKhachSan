package com.example.backendqlks.scheduledjobs;

import com.example.backendqlks.service.BookingConfirmationFormService;
import com.example.backendqlks.service.SMTPEmailService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingConfirmationFormExpirationChecker implements Job {

    private final BookingConfirmationFormService bookingConfirmationFormService;

    private final SMTPEmailService smtpEmailService;

    public BookingConfirmationFormExpirationChecker(BookingConfirmationFormService bookingConfirmationFormService,
                                                    SMTPEmailService smtpEmailService) {
        this.bookingConfirmationFormService = bookingConfirmationFormService;
        this.smtpEmailService = smtpEmailService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        int bookingConfirmationFormId = context.getJobDetail().getJobDataMap().getInt("bookingConfirmationFormId");
        bookingConfirmationFormService.updateBookingStateToExpiredIfStillPending(bookingConfirmationFormId);
        var bookingForm=bookingConfirmationFormService.get(bookingConfirmationFormId);
        if (bookingForm!=null && bookingForm.getGuestEmail()!=null) {
            String email=bookingForm.getGuestEmail();
            String subject = bookingForm.getGuestName();
            String info = "Mã phiếu: " + bookingForm.getId()
                    + "<br/>Phòng đặt: ID: " + bookingForm.getRoomId() + ", Tên: " + bookingForm.getRoomName()
                    + "<br/>Ngày đặt: " + bookingForm.getBookingDate()
                    + "<br/>Số ngày đặt: " + bookingForm.getRentalDays()
                    + "<br/>Ngày tạo phiếu: " + bookingForm.getCreatedAt();
            smtpEmailService.sendBookingExpiredNotification(email, subject, info);
        }
    }
}
