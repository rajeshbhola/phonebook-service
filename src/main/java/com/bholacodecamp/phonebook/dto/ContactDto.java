package com.bholacodecamp.phonebook.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ContactDto {
    private Long id;
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Name can only contain alphanumeric characters and spaces")
    private String name;
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Pattern(regexp = ".*", message = "Invalid email format")
    private String email;
    @Valid
    @Size(min = 1, message = "Contact must have at least one phone number")
    private List<PhoneNumberDto> phoneNumbers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<PhoneNumberDto> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumberDto> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
