package com.bholacodecamp.phonebook.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PhoneNumberDto {
    private Long id;
    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = ".*", message = "Invalid phone number format")
    private String number;

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
}
