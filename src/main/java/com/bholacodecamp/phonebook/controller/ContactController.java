package com.bholacodecamp.phonebook.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bholacodecamp.phonebook.entity.Contact;
import com.bholacodecamp.phonebook.entity.PhoneNumber;
import com.bholacodecamp.phonebook.service.ContactService;
import com.bholacodecamp.phonebook.dto.ContactDto;
import com.bholacodecamp.phonebook.dto.PhoneNumberDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);


    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public ResponseEntity<List<ContactDto>> getContacts() {
        logger.info("Fetching all contacts");
        List<Contact> contacts = contactService.getContacts();
        List<ContactDto> contactDtos = contacts.stream().map(contact -> {
            ContactDto dto = new ContactDto();
            dto.setId(contact.getId());
            dto.setName(contact.getName());
            dto.setEmail(contact.getEmail());
            dto.setPhoneNumbers(contact.getPhoneNumbers().stream()
                    .map(pn -> {
                        PhoneNumberDto pnDto = new PhoneNumberDto();
                        pnDto.setId(pn.getId());
                        pnDto.setNumber(pn.getMaskedNumber());
                        return pnDto;
                    })
                    .collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(contactDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ContactDto> createContact(@jakarta.validation.Valid @RequestBody ContactDto contactDto) {
        logger.info("Creating new contact: {}", contactDto.getName());
        Contact contact = new Contact();
        contact.setName(contactDto.getName());
        contact.setEmail(contactDto.getEmail());
        if (contactDto.getPhoneNumbers() != null) {
            contactDto.getPhoneNumbers().forEach(pnDto -> {
                PhoneNumber phoneNumber = new PhoneNumber();
                phoneNumber.setNumber(pnDto.getNumber());
                phoneNumber.setContact(contact);
                contact.getPhoneNumbers().add(phoneNumber);
            });
        }
        contactService.createContact(contact);

        ContactDto createdDto = new ContactDto();
        createdDto.setId(contact.getId());
        createdDto.setName(contact.getName());
        createdDto.setEmail(contact.getEmail());
        createdDto.setPhoneNumbers(contact.getPhoneNumbers().stream()
                .map(pn -> {
                    PhoneNumberDto pnDto = new PhoneNumberDto();
                    pnDto.setId(pn.getId());
                    pnDto.setNumber(pn.getNumber());
                    return pnDto;
                })
                .collect(Collectors.toList()));

        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDto> getContactById(@PathVariable Long id) {
        logger.info("Fetching contact with ID: {}", id);
        Contact contact = contactService.getContact(id);
        if (contact == null) {
            logger.warn("Contact with ID {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ContactDto dto = new ContactDto();
        dto.setId(contact.getId());
        dto.setName(contact.getName());
        dto.setEmail(contact.getEmail());
        dto.setPhoneNumbers(contact.getPhoneNumbers().stream()
                .map(pn -> {
                    PhoneNumberDto pnDto = new PhoneNumberDto();
                    pnDto.setId(pn.getId());
                    pnDto.setNumber(pn.getNumber());
                    return pnDto;
                })
                .collect(Collectors.toList()));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDto> updateContact(@PathVariable Long id, @jakarta.validation.Valid @RequestBody ContactDto contactDto) {
        logger.info("Updating contact with ID: {}", id);
        Contact existingContact = contactService.getContact(id);
        if (existingContact == null) {
            logger.warn("Contact with ID {} not found for update", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        existingContact.setName(contactDto.getName());
        existingContact.setEmail(contactDto.getEmail());

        // Efficiently update phone numbers
        if (contactDto.getPhoneNumbers() != null) {
            Map<Long, PhoneNumber> existingPhoneNumbers = existingContact.getPhoneNumbers().stream()
                    .collect(Collectors.toMap(PhoneNumber::getId, pn -> pn));
            List<PhoneNumber> updatedPhoneNumbers = new ArrayList<>();

            for (PhoneNumberDto pnDto : contactDto.getPhoneNumbers()) {
                if (pnDto.getId() != null && existingPhoneNumbers.containsKey(pnDto.getId())) {
                    // Existing number, update it
                    PhoneNumber existingNumber = existingPhoneNumbers.get(pnDto.getId());
                    existingNumber.setNumber(pnDto.getNumber());
                    updatedPhoneNumbers.add(existingNumber);
                    existingPhoneNumbers.remove(pnDto.getId()); // Remove from map to track deletions
                } else {
                    // New number, add it
                    PhoneNumber newPhoneNumber = new PhoneNumber();
                    newPhoneNumber.setNumber(pnDto.getNumber());
                    newPhoneNumber.setContact(existingContact);
                    updatedPhoneNumbers.add(newPhoneNumber);
                }
            }
            // Numbers left in the map were not in the DTO, so remove them
            existingContact.getPhoneNumbers().clear();
            existingContact.getPhoneNumbers().addAll(updatedPhoneNumbers);
        } else {
            // No phone numbers in DTO, clear existing ones
            existingContact.getPhoneNumbers().clear();
        }


        contactService.updateContact(existingContact);

        ContactDto updatedDto = new ContactDto();
        updatedDto.setId(existingContact.getId());
        updatedDto.setName(existingContact.getName());
        updatedDto.setEmail(existingContact.getEmail());
        updatedDto.setPhoneNumbers(existingContact.getPhoneNumbers().stream()
                .map(pn -> {
                    PhoneNumberDto pnDto = new PhoneNumberDto();
                    pnDto.setId(pn.getId());
                    pnDto.setNumber(pn.getNumber());
                    return pnDto;
                })
                .collect(Collectors.toList()));

        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        logger.info("Deleting contact with ID: {}", id);
        contactService.deleteContact(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
