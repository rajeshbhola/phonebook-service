package com.bholacodecamp.phonebook.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bholacodecamp.phonebook.entity.Contact;
import com.bholacodecamp.phonebook.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);


    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> getContacts() {
        logger.debug("Fetching all contacts from repository");
        return contactRepository.findAll();
    }

    public Contact getContact(Long id) {
        logger.debug("Fetching contact with ID: {}", id);
        return contactRepository.findById(id).orElse(null);
    }

    public void createContact(Contact contact) {
        logger.debug("Creating contact: {}", contact.getName());
        contactRepository.save(contact);
    }

    public void updateContact(Contact contact) {
        logger.debug("Updating contact with ID: {}", contact.getId());
        contactRepository.save(contact);
    }

    public void deleteContact(Long id) {
        logger.debug("Deleting contact with ID: {}", id);
        contactRepository.deleteById(id);
    }
}
