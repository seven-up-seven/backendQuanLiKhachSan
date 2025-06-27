package com.example.backendqlks.service;

import com.example.backendqlks.dao.BookingConfirmationFormRepository;
import com.example.backendqlks.dao.RoomRepository;
import com.example.backendqlks.dto.bookingConfirmationForm.BookingConfirmationFormDto;
import com.example.backendqlks.dto.bookingConfirmationForm.ResponseBookingConfirmationFormDto;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.entity.enums.Action;
import com.example.backendqlks.entity.enums.BookingState;
import com.example.backendqlks.entity.enums.RoomState;
import com.example.backendqlks.entity.enums.Variable;
import com.example.backendqlks.mapper.BookingConfirmationFormMapper;
import com.example.backendqlks.scheduledjobs.BookingConfirmationFormExpirationChecker;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
public class BookingConfirmationFormService {
    private final BookingConfirmationFormRepository bookingConfirmationFormRepository;
    private final BookingConfirmationFormMapper bookingConfirmationFormMapper;
    private final RoomRepository roomRepository;
    private final HistoryService historyService;
    private final VariableService variableService;

    @Autowired
    private Scheduler scheduler;

    public BookingConfirmationFormService(BookingConfirmationFormRepository bookingConfirmationFormRepository,
                                          BookingConfirmationFormMapper bookingConfirmationFormMapper,
                                          RoomRepository roomRepository,
                                          HistoryService historyService,
                                          VariableService variableService) {
        this.bookingConfirmationFormMapper = bookingConfirmationFormMapper;
        this.bookingConfirmationFormRepository = bookingConfirmationFormRepository;
        this.roomRepository = roomRepository;
        this.historyService = historyService;
        this.variableService = variableService;
    }

    @Transactional(readOnly = true)
    public ResponseBookingConfirmationFormDto get(int bookingConfirmationFormId){
        var existingBookingConfirmationForm = bookingConfirmationFormRepository.findById(bookingConfirmationFormId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect booking confirmation form id"));
        return bookingConfirmationFormMapper.toResponseDto(existingBookingConfirmationForm);
    }

    @Transactional(readOnly = true)
    public Page<ResponseBookingConfirmationFormDto> getAllPage(Pageable pageable){
        var bookingConfirmationFormPage = bookingConfirmationFormRepository.findAll(pageable);
        List<ResponseBookingConfirmationFormDto> bookingConfirmationForms=bookingConfirmationFormMapper.toResponseDtoList(bookingConfirmationFormPage.getContent());
        return new PageImpl<>(bookingConfirmationForms, pageable, bookingConfirmationFormPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<ResponseBookingConfirmationFormDto> getAll()
    {
        return bookingConfirmationFormMapper.toResponseDtoList(bookingConfirmationFormRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<ResponseBookingConfirmationFormDto> getByListIds(List<Integer> ids){
        var bookingConfirmationForms=bookingConfirmationFormRepository.findAllById(ids);
        return bookingConfirmationFormMapper.toResponseDtoList(bookingConfirmationForms);
    }

    //TODO: add try catch
    public ResponseBookingConfirmationFormDto create(BookingConfirmationFormDto bookingConfirmationFormDto,
                                                     int impactorId,
                                                     String impactor){
        var newBookingConfirmationForm = bookingConfirmationFormMapper.toEntity(bookingConfirmationFormDto);
        bookingConfirmationFormRepository.save(newBookingConfirmationForm);
        var room = roomRepository.findById(bookingConfirmationFormDto.getRoomId());
        if (room.isEmpty()) {
            throw new IllegalArgumentException("Room not found with id: " + bookingConfirmationFormDto.getRoomId());
        }
        room.get().setRoomState(RoomState.BOOKED);
        roomRepository.save(room.get());
        String content = String.format(
                "bookingGuestId: %d; roomId: %d; trạng thái: %s",
                newBookingConfirmationForm.getBookingGuestId(),
                newBookingConfirmationForm.getRoomId(),
                newBookingConfirmationForm.getBookingState()
        );
        HistoryDto history=HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Phiếu đặt phòng")
                .affectedObjectId(newBookingConfirmationForm.getId())
                .action(Action.CREATE)
                .content(content)
                .build();
        historyService.create(history);

        //Create the job to automatically update the booking form state
        var maxTTL=variableService.getByName(String.valueOf(Variable.MAX_BOOKING_CONFIRMATION_TTL));
        double ttlValue = maxTTL.getValue();
        long days = (long) ttlValue;
        long hours = (long) ((ttlValue - days) * 24);

        LocalDateTime triggerTime = newBookingConfirmationForm.getBookingDate()
                .plusDays(days)
                .plusHours(hours);
        JobDetail jobDetail = JobBuilder.newJob(BookingConfirmationFormExpirationChecker.class)
                .withIdentity("expireBooking_" + newBookingConfirmationForm.getId(), "booking")
                .usingJobData("bookingConfirmationFormId", newBookingConfirmationForm.getId())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger_expireBooking_" + newBookingConfirmationForm.getId(), "booking")
                .startAt(Timestamp.valueOf(triggerTime))
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException("Không thể lên lịch job hết hạn booking", e);
        }

        return bookingConfirmationFormMapper.toResponseDto(newBookingConfirmationForm);
    }

    //TODO: add try catch
    public ResponseBookingConfirmationFormDto update(int bookingConfirmationFormId,
                                                     BookingConfirmationFormDto bookingConfirmationFormDto,
                                                     int impactorId,
                                                     String impactor){
        var existingBookingConfirmationForm = bookingConfirmationFormRepository.findById(bookingConfirmationFormId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect booking confirmation form id"));
        int oldGuestId = existingBookingConfirmationForm.getBookingGuestId();
        int oldRoomId = existingBookingConfirmationForm.getRoomId();
        BookingState oldState = existingBookingConfirmationForm.getBookingState();
        bookingConfirmationFormMapper.updateEntityFromDto(bookingConfirmationFormDto, existingBookingConfirmationForm);
        StringBuilder sb = new StringBuilder();
        if (oldGuestId != bookingConfirmationFormDto.getBookingGuestId()) {
            sb.append("bookingGuestId: ")
                    .append(oldGuestId).append(" -> ").append(bookingConfirmationFormDto.getBookingGuestId())
                    .append("; ");
        }
        if (oldRoomId != bookingConfirmationFormDto.getRoomId()) {
            sb.append("roomId: ")
                    .append(oldRoomId).append(" -> ").append(bookingConfirmationFormDto.getRoomId())
                    .append("; ");
        }
        if (oldState != bookingConfirmationFormDto.getBookingState()) {
            sb.append("bookingState: ")
                    .append(oldState).append(" -> ").append(bookingConfirmationFormDto.getBookingState())
                    .append("; ");
        }
        String content = !sb.isEmpty()
                ? sb.substring(0, sb.length() - 2)
                : "no fields changed";
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Phiếu đặt phòng")
                .affectedObjectId(existingBookingConfirmationForm.getId())
                .action(Action.UPDATE)
                .content(content)
                .build();
        historyService.create(history);
        bookingConfirmationFormRepository.save(existingBookingConfirmationForm);
        return bookingConfirmationFormMapper.toResponseDto(existingBookingConfirmationForm);
    }

    //TODO: modify later
    public void delete(int bookingConfirmationFormId,
                       int impactorId,
                       String impactor) {
        var existingBookingConfirmationForm = bookingConfirmationFormRepository.findById(bookingConfirmationFormId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect booking confirmation form id"));
        String content = String.format(
                "bookingGuestId: %d; roomId: %d; trạng thái: %s",
                existingBookingConfirmationForm.getBookingGuestId(),
                existingBookingConfirmationForm.getRoomId(),
                existingBookingConfirmationForm.getBookingState()
        );
        HistoryDto history=HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Phiếu đặt phòng")
                .affectedObjectId(existingBookingConfirmationForm.getId())
                .action(Action.DELETE)
                .content(content)
                .build();
        historyService.create(history);
        bookingConfirmationFormRepository.delete(existingBookingConfirmationForm);
    }

    public void updateBookingStateToExpiredIfStillPending(int id) {
        var item=bookingConfirmationFormRepository.findById(id);
        if (item.isPresent()) {
            var bookingConfirmationForm=item.get();
            if (bookingConfirmationForm.getBookingState() == BookingState.PENDING) {
                bookingConfirmationForm.setBookingState(BookingState.EXPIRED);
                bookingConfirmationFormRepository.save(bookingConfirmationForm);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<ResponseBookingConfirmationFormDto> getBookingConfirmationFormsByUserId(int userId) {
        return bookingConfirmationFormMapper.toResponseDtoList(bookingConfirmationFormRepository.findBookingConfirmationFormsByBookingGuestId(userId));
    }
}
