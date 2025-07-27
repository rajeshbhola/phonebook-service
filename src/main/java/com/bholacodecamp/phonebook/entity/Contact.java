package com.bholacodecamp.phonebook.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Contact {

    private static final Logger logger = LoggerFactory.getLogger(Contact.class);


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Name can only contain alphanumeric characters and spaces")
    private String name;
    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
    @Size(min = 1, message = "Contact must have at least one phone number")
    private List<PhoneNumber> phoneNumbers = new ArrayList<>();
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Invalid email format")
    private String email;

    public Contact() {
        logger.debug("Contact entity created");
    }

    public Contact(String name, String email) {
        this.name = name;
        this.email = email;
        logger.debug("Contact entity created with name: {} and email: {}", name, email);
    }

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

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
