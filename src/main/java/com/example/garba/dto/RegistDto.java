package com.example.garba.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistDto {

     private Long id;

    private String firstName;

    private String lastName;

    private String mobileNo;

    private int age;

    private String gender;

    private String batchTime;

    private String address;

    private String regsNo;

    private String payment;

    private byte[] photo;
}
