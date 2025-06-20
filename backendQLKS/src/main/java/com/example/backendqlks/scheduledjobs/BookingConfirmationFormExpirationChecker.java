package com.example.backendqlks.scheduledjobs;

import com.example.backendqlks.service.BookingConfirmationFormService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingConfirmationFormExpirationChecker implements Job {

    private BookingConfirmationFormService bookingConfirmationFormService;

    public BookingConfirmationFormExpirationChecker(BookingConfirmationFormService bookingConfirmationFormService) {
        this.bookingConfirmationFormService = bookingConfirmationFormService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        int bookingConfirmationFormId = context.getJobDetail().getJobDataMap().getInt("bookingConfirmationFormId");
        bookingConfirmationFormService.updateBookingStateToExpiredIfStillPending(bookingConfirmationFormId);
    }
}
