package com.example.backendqlks.dao;

import com.example.backendqlks.entity.BookingConfirmationForm;
import com.example.backendqlks.entity.Guest;
import com.example.backendqlks.entity.enums.BookingState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface BookingConfirmationFormRepository extends JpaRepository<BookingConfirmationForm, Integer> {
    Page<BookingConfirmationForm> findBookingConfirmationFormsByBookingGuest(Guest bookingGuest, Pageable pageable);
}
