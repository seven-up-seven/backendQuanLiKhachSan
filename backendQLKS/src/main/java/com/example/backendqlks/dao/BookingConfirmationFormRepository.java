package com.example.backendqlks.dao;

import com.example.backendqlks.entity.BookingConfirmationForm;
import com.example.backendqlks.entity.enums.BookingState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingConfirmationFormRepository extends JpaRepository<BookingConfirmationForm, Integer> {
    Page<BookingConfirmationForm> findBookingConfirmationFormsByBookingPersonEmailContainingIgnoreCase(String bookingPersonEmail, Pageable pageable);

    Page<BookingConfirmationForm> findBookingConfirmationFormsByBookingPersonNameContainingIgnoreCase(String bookingPersonName, Pageable pageable);

    Page<BookingConfirmationForm> findBookingConfirmationFormsByBookingPersonIdentificationNumber(String bookingPersonIdentificationNumber, Pageable pageable);

    Optional<BookingConfirmationForm> findByBookingPersonNameContainingIgnoreCaseAndBookingState(String bookingPersonName, BookingState bookingState);

    Optional<BookingConfirmationForm> findByBookingPersonIdentificationNumberContainsAndBookingState(String bookingPersonIdentificationNumber, BookingState bookingState);

    Optional<BookingConfirmationForm> findByBookingPersonEmailContainingIgnoreCaseAndBookingState(String bookingPersonEmail, BookingState bookingState);
}
