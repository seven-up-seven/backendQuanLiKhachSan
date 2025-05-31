package com.example.backendqlks.dto.invoiceDetail;

import com.example.backendqlks.entity.Invoice;
import com.example.backendqlks.entity.RentalForm;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

public class ResponseDetailDto {
    private int id;
    private int numberOfRentalDays; // this column is updated after calculating total rental days of RentalForm and RentalExtensionForm
    private Invoice invoice;
    private double reservationCost;
    private RentalForm rentalForm;
}
