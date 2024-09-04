package com.example.garba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.garba.entity.Registration;

public interface RegistRepository extends JpaRepository<Registration, Long> {

    List<Registration> findByMobileNo(String mobileNo);

    boolean existsByMobileNo(String mobileNo);

}
