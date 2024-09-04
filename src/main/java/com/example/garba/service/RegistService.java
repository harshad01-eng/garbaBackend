package com.example.garba.service;

import java.util.List;

import com.example.garba.dto.RegistDto;

public interface RegistService {

    RegistDto registerDetail(RegistDto registDto);

    List<RegistDto> findByMobileNo(String mobileNo);

    RegistDto getDetailById(Long id );

    List<RegistDto> getAllData();

}
