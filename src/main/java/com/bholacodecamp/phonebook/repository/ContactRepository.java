package com.bholacodecamp.phonebook.repository;

import com.bholacodecamp.phonebook.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
