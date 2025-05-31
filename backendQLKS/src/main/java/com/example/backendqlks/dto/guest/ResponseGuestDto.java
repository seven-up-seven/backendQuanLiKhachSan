package com.example.backendqlks.dto.guest;

import com.example.backendqlks.entity.Invoice;
import com.example.backendqlks.entity.RentalFormDetail;
import com.example.backendqlks.entity.enums.Sex;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class ResponseGuestDto {
    private int id;
    private String name;
    private Sex sex;
    private short  age;
    private String identificationNumber;
    private String address;
    private String phoneNumber;
    private String email;
    private  String nationality;
    private List<Invoice> invoices;
    private List<RentalFormDetail> rentalFormDetails;
}
