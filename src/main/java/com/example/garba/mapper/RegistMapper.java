package com.example.garba.mapper;

import com.example.garba.dto.RegistDto;
import com.example.garba.entity.Registration;

public class RegistMapper {

    public static Registration mapToRegistration(RegistDto registDto){
        Registration registration = new Registration(
            registDto.getId(),
            registDto.getFirstName(),
            registDto.getLastName(),
            registDto.getMobileNo(),
            registDto.getAge(),
            registDto.getGender(),
            registDto.getBatchTime(),
            registDto.getAddress(),
            registDto.getRegsNo(),
            registDto.getPayment(),
            registDto.getPhoto()
        );

        return registration;
    }

    public static RegistDto mapToRegistDto(Registration registration){
        RegistDto registDto= new RegistDto(
            registration.getId(),
            registration.getFirstName(),
            registration.getLastName(),
            registration.getMobileNo(),
            registration.getAge(),
            registration.getGender(),
            registration.getBatchTime(),
            registration.getAddress(),
            registration.getRegsNo(),
            registration.getPayment(),
            registration.getPhoto()
        );
        return registDto;
    }
}
