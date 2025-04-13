package com.insurance.system.controller;

import com.insurance.system.entity.SupportTicket;
import com.insurance.system.service.SupportTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:3000")
public class SupportTicketController {

    @Autowired
    private SupportTicketService ticketService;

    @PostMapping("/create")
    public ResponseEntity<SupportTicket> createTicket(@RequestParam Long userId,
                                                      @RequestParam String subject,
                                                      @RequestParam String message) {
        SupportTicket ticket = ticketService.createTicket(userId, subject, message);
        return ticket != null ? ResponseEntity.ok(ticket) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SupportTicket>> getUserTickets(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getUserTickets(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SupportTicket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @PutMapping("/{ticketId}/respond")
    public ResponseEntity<SupportTicket> respondToTicket(@PathVariable Long ticketId,
                                                         @RequestBody Map<String, String> payload) {
        String response = payload.get("response");
        SupportTicket updated = ticketService.respondToTicket(ticketId, response);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{ticketId}/status")
    public ResponseEntity<SupportTicket> updateTicketStatus(@PathVariable Long ticketId,
                                                            @RequestParam String status) {
        try {
            SupportTicket.Status parsedStatus = SupportTicket.Status.valueOf(status.toUpperCase());
            SupportTicket updated = ticketService.updateStatus(ticketId, parsedStatus);
            return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid status received: " + status);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.err.println("Error updating ticket status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
