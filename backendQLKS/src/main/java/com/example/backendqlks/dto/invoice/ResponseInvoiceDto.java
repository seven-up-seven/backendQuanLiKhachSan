package com.example.backendqlks.dto.invoice;

import com.example.backendqlks.entity.Guest;
import com.example.backendqlks.entity.Staff;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ResponseInvoiceDto {
    private int id;
    private double totalReservationCost;
    private Guest payingGuest;
    private Staff staff;
    private LocalDateTime createdAt;
    private List<Integer> invoiceDetailIds;
}
