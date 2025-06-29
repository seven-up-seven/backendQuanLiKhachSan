package com.example.backendqlks.scheduledjobs;

import com.example.backendqlks.dto.rentalform.ResponseRentalFormDto;
import com.example.backendqlks.dto.rentalformdetail.ResponseRentalFormDetailDto;
import com.example.backendqlks.service.RentalFormDetailService;
import com.example.backendqlks.service.RentalFormService;
import com.example.backendqlks.service.SMTPEmailService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public class RentalFormExpirationChecker implements Job {
    private final RentalFormService rentalFormService;
    private final SMTPEmailService smtpEmailService;
    private final RentalFormDetailService rentalFormDetailService;
    private final Scheduler scheduler;

    public RentalFormExpirationChecker(RentalFormService rentalFormService,
                                       SMTPEmailService smtpEmailService,
                                       RentalFormDetailService rentalFormDetailService,
                                       Scheduler scheduler) {
        this.rentalFormService = rentalFormService;
        this.smtpEmailService = smtpEmailService;
        this.rentalFormDetailService = rentalFormDetailService;
        this.scheduler = scheduler;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        int rentalFormId = context.getJobDetail().getJobDataMap().getInt("rentalFormId");
        String jobType = context.getJobDetail().getJobDataMap().getString("jobType");

        ResponseRentalFormDto rentalForm = rentalFormService.getRentalFormById(rentalFormId);
        List<ResponseRentalFormDetailDto> rentalFormDetails = rentalFormDetailService.getAllRentalFormDetailsByRentalFormId(rentalFormId);

        if (rentalForm == null || rentalFormDetails == null || rentalFormDetails.isEmpty()) return;

        int totalDays = rentalFormService.countTotalRentalDaysByRentalFormId(rentalFormId);
        LocalDateTime newEndTime = rentalForm.getRentalDate().plusDays(totalDays);
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());

        if (newEndTime.atZone(ZoneId.systemDefault()).isAfter(now)) {
            try {
                JobDetail newWarningJob = JobBuilder.newJob(RentalFormExpirationChecker.class)
                        .withIdentity("warnRental_" + rentalFormId + "_rescheduled", "rental")
                        .usingJobData("rentalFormId", rentalFormId)
                        .usingJobData("jobType", "WARNING")
                        .build();

                Trigger newWarningTrigger = TriggerBuilder.newTrigger()
                        .withIdentity("trigger_warnRental_" + rentalFormId + "_rescheduled", "rental")
                        .startAt(Date.from(newEndTime.minusHours(1).atZone(ZoneId.systemDefault()).toInstant()))
                        .build();

                JobDetail newExpiredJob = JobBuilder.newJob(RentalFormExpirationChecker.class)
                        .withIdentity("expireRental_" + rentalFormId + "_rescheduled", "rental")
                        .usingJobData("rentalFormId", rentalFormId)
                        .usingJobData("jobType", "EXPIRED")
                        .build();

                Trigger newExpiredTrigger = TriggerBuilder.newTrigger()
                        .withIdentity("trigger_expireRental_" + rentalFormId + "_rescheduled", "rental")
                        .startAt(Date.from(newEndTime.atZone(ZoneId.systemDefault()).toInstant()))
                        .build();

                scheduler.scheduleJob(newWarningJob, newWarningTrigger);
                scheduler.scheduleJob(newExpiredJob, newExpiredTrigger);

                System.out.println("Đã hủy job cũ và lên lịch job mới do phiếu đã được gia hạn.");
            } catch (SchedulerException e) {
                throw new JobExecutionException("Không thể tạo lại job sau khi gia hạn phiếu thuê", e);
            }

            return;
        }

        for (var rentalFormDetail : rentalFormDetails) {
            String guestName = rentalFormDetail.getGuestName();
            String guestEmail = rentalFormDetail.getGuestEmail();
            String rentalInfo = "Phòng: Id: " + rentalForm.getRoomId() + ", Tên: " + rentalForm.getRoomName() +
                    ", Ngày thuê: " + rentalForm.getRentalDate() +
                    ", Số ngày: " + rentalForm.getNumberOfRentalDays();

            if ("WARNING".equalsIgnoreCase(jobType)) {
                smtpEmailService.sendBeforeRentalFormExpiredNotification(guestEmail, guestName, rentalInfo);
            } else if ("EXPIRED".equalsIgnoreCase(jobType)) {
                smtpEmailService.sendRentalFormExpiredNotification(guestEmail, guestName, rentalInfo);
            }
        }
    }
}
