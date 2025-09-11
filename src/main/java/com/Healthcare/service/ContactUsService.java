package com.healthcare.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.model.ContactUs;
import com.healthcare.repository.ContactUsRepository;

@Service
public class ContactUsService {

    @Autowired
    private ContactUsRepository repository;

    public ContactUs saveMessage(ContactUs contactUs) {
        return repository.save(contactUs);
    }
    
    public List<ContactUs> getAllMessages() {
        return repository.findAll();
    }
}
