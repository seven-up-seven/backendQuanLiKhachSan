package com.example.backendqlks.dto.facility;

import com.example.backendqlks.entity.ImportInvoiceDetail;
import com.example.backendqlks.entity.RoomFacility;
import com.example.backendqlks.entity.StockRequisitionInvoiceDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.List;

public class ResponseFacilityDto {
    private int id;
    private String name;
    private short quantity;
    private double price;
    private List<ImportInvoiceDetail> importInvoiceDetails;
    private List<StockRequisitionInvoiceDetail> stockRequisitionInvoiceDetails;
    private List<RoomFacility> roomFacilities;
}
