package com.example.backendqlks.dto.invoiceDetail;

import com.example.backendqlks.entity.Invoice;
import com.example.backendqlks.entity.RentalForm;
import lombok.Data;

@Data
public class ResponseInvoiceDetailDto {
    private int id;
    private int numberOfRentalDays; // this column is updated after calculating total rental days of RentalForm and RentalExtensionForm
    private int invoiceId;
    private double reservationCost;
    private int rentalFormId;
    private int roomId;
}
