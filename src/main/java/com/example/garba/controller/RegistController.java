package com.example.garba.controller;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.garba.dto.RegistDto;
import com.example.garba.service.RegistService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
@RequestMapping("api")
public class RegistController {

    private RegistService registService;

    public RegistController(RegistService registService){
        this.registService = registService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistDto> registerDetail(@RequestBody RegistDto registDto) {
        return new ResponseEntity<>(registService.registerDetail(registDto), HttpStatus.CREATED);
    }

    @GetMapping("/detail/{mobileNo}")
    public  ResponseEntity<List<RegistDto>> findByMobileNo(@PathVariable String mobileNo) {
        List<RegistDto> registDtos = registService.findByMobileNo(mobileNo);
        if (registDtos.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
        return new ResponseEntity<>(registDtos, HttpStatus.OK);
        
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<RegistDto> getDetailById(@PathVariable Long id) {
        try{
       RegistDto registDto = registService.getDetailById(id);
       return new ResponseEntity<>(registDto, HttpStatus.OK);
        }catch(NoSuchElementException ex){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/gridData")
    public ResponseEntity<List<RegistDto>> getAllData() {
        List<RegistDto> registDtos = registService.getAllData();

        if (registDtos.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
        return new ResponseEntity<>(registDtos, HttpStatus.OK);

    }
    
    
    

}
