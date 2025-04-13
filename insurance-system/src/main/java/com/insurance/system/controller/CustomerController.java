package com.insurance.system.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public String getCustomerProfile() {
        return "Customer Profile Accessed";
    }
}
