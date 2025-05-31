package com.example.backendqlks.dto.facilityCompensation;

import com.example.backendqlks.entity.Facility;
import com.example.backendqlks.entity.InvoiceDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

public class ResponseFacilityCompensationDto {
    private Facility facility;
    private InvoiceDetail invoiceDetail;
    private short quantity;
}
