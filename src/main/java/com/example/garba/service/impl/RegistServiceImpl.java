package com.example.garba.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.garba.dto.RegistDto;
import com.example.garba.entity.Registration;
import com.example.garba.exception.MobileNumberAlreadyExistsException;
import com.example.garba.mapper.RegistMapper;
import com.example.garba.repository.RegistRepository;
import com.example.garba.service.RegistService;

@Service
public class RegistServiceImpl implements RegistService {

    private RegistRepository registRepository;

    public RegistServiceImpl(RegistRepository registRepository){
        this.registRepository = registRepository;
    }

    @Override
    public RegistDto registerDetail(RegistDto registDto) {
        if(registRepository.existsByMobileNo(registDto.getMobileNo()) && registDto.getId()==0){
            throw new MobileNumberAlreadyExistsException("Mobile number is already registered");
        }
        Registration registration = RegistMapper.mapToRegistration(registDto);
        Registration savRegistration = registRepository.save(registration);
        if(registDto.getId() == 0){
            String  regsNo = String.valueOf(savRegistration.getId() + 100);
            savRegistration.setRegsNo(regsNo);
             registRepository.save(savRegistration);
        }
       
        
       return RegistMapper.mapToRegistDto(savRegistration);
    //   return  this.registRepository ;
    }

    @Override
    public List<RegistDto> findByMobileNo(String mobileNo) {
        List<Registration> registEntities  = registRepository.findByMobileNo(mobileNo);
       
         if (registEntities.isEmpty()) {
            throw new NoSuchElementException("No records found for the mobile number: " + mobileNo);
        }
        return registEntities.stream().map(RegistMapper:: mapToRegistDto)
                                    .collect(Collectors.toList());
    }

    @Override
    public RegistDto getDetailById(Long id) {

      Optional<Registration> registration = registRepository.findById(id);

      if(registration.isPresent()){
        return RegistMapper.mapToRegistDto(registration.get());
      } else {
        // If the record is not found, you can either throw an exception
        // or return null, depending on your design choice.
        throw new NoSuchElementException("No registration found with ID: " + id);
    }
    }

    @Override
    public List<RegistDto> getAllData() {
        try{
        List<Registration> registrations = registRepository.findAll();

        return registrations.stream().map(RegistMapper:: mapToRegistDto)
                                    .collect(Collectors.toList());
        }catch(Exception e){
            throw new RuntimeException("An unexpected error occurred while processing your request", e);
        }
    }

    
    

}
