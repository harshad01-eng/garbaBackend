package com.example.garba.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.garba.dto.RegistDto;

public interface RegistService {

    RegistDto registerDetail(RegistDto registDto, MultipartFile photo);

    List<RegistDto> findByMobileNo(String mobileNo);

    RegistDto getDetailById(Long id );

    List<RegistDto> getAllData();

     byte[] downloadPdfById(Long id) throws Exception;

}
