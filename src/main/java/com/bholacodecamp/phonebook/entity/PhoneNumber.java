package com.bholacodecamp.phonebook.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class PhoneNumber {

    private static final Logger logger = LoggerFactory.getLogger(PhoneNumber.class);


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number format")
    private String number;

    @ManyToOne
    private Contact contact;

    public PhoneNumber() {
        logger.debug("PhoneNumber entity created");
    }

    public PhoneNumber(String number) {
        this.number = number;
        logger.debug("PhoneNumber entity created with number: {}", number);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getMaskedNumber() {
        if (number == null || number.length() < 4) {
            return number;
        }
        return "*".repeat(number.length() - 4) + number.substring(number.length() - 4);
    }
}
