package com.example.garba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.garba.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findByPassword(String password);
}
