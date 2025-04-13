package com.insurance.system.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    @GetMapping("/reports")
    @PreAuthorize("hasAnyRole('AGENT', 'ADMIN')")
    public String getAgentReports() {
        return "Agent Reports Accessed";
    }
}
