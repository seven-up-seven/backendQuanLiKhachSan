package com.example.backendqlks.dto.staff;

import com.example.backendqlks.entity.Account;
import com.example.backendqlks.entity.Position;
import com.example.backendqlks.entity.enums.Sex;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseStaffDto {
    private Integer id;

    private String fullName;

    private Integer age;

    private String identificationNumber;

    private String address;

    private Sex sex;

    private Float salaryMultiplier;

    private Position position;

    private Account account;

    private List<Integer> invoiceIds;

    private List<Integer> rentalExtensionFormIds;

    private List<Integer> rentalFormIds;
}
