package com.example.garba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.garba.dto.AuthenticationRequestDto;
import com.example.garba.entity.Users;
import com.example.garba.repository.UsersRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("api")
public class UsersController {

    @Autowired
    private UsersRepository userRepository;

  
    
    @PostMapping("/login")
    public ResponseEntity<?> uesrLogin(@RequestBody AuthenticationRequestDto authenticationRequestDto) {

        Users users = userRepository.findByPassword(authenticationRequestDto.getPassword());
        Map<String, Object> response = new HashMap<>();
        
       if (users != null && users.getUsername().equals(authenticationRequestDto.getUsername())) {
        response.put("success", true);
        response.put("message", "Login successful");
        return ResponseEntity.ok(response);
        }
        response.put("success", false);
        response.put("message", "Invalid credentials");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
}
