package com.bholacodecamp.phonebook.repository;

import com.bholacodecamp.phonebook.entity.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
}