package com.upickem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.upickem.model.Login;

@Repository
public interface LoginRepository extends JpaRepository<Login, String> {

}
