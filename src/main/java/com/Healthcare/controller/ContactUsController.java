package com.healthcare.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.healthcare.model.ContactUs;
import com.healthcare.service.ContactUsService;

@RestController
@RequestMapping("/api/contact-us")
@CrossOrigin(origins = "*")
public class ContactUsController {

    @Autowired
    private ContactUsService service;

    @PostMapping
    public String submitContact(@RequestBody ContactUs contactUs) {
        service.saveMessage(contactUs);
        return "Message received. Thank you!";
    }
    @GetMapping("/all")
    public List<ContactUs> getAllContacts() {
        return service.getAllMessages();
    }
}
