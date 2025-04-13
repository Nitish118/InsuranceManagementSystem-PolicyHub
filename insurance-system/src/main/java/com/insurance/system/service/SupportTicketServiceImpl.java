package com.insurance.system.service;

import com.insurance.system.entity.SupportTicket;
import com.insurance.system.entity.User;
import com.insurance.system.repository.SupportTicketRepository;
import com.insurance.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SupportTicketServiceImpl implements SupportTicketService {

    @Autowired
    private SupportTicketRepository ticketRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public SupportTicket createTicket(Long userId, String subject, String message) {
        Optional<User> userOpt = userRepo.findById(userId);
        if (userOpt.isPresent()) {
            SupportTicket ticket = new SupportTicket();
            ticket.setUser(userOpt.get());
            ticket.setSubject(subject);
            ticket.setMessage(message);
            ticket.setStatus(SupportTicket.Status.OPEN);
            ticket.setCreatedAt(new Date());
            return ticketRepo.save(ticket);
        }
        return null;
    }

    @Override
    public List<SupportTicket> getUserTickets(Long userId) {
        Optional<User> userOpt = userRepo.findById(userId);
        return userOpt.map(ticketRepo::findByUser).orElse(List.of());
    }

    @Override
    public List<SupportTicket> getAllTickets() {
        return ticketRepo.findAll();
    }

    @Override
    public SupportTicket respondToTicket(Long ticketId, String response) {
        Optional<SupportTicket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isPresent()) {
            SupportTicket ticket = ticketOpt.get();
            ticket.setResponse(response);
            ticket.setStatus(SupportTicket.Status.IN_PROGRESS);
            ticket.setUpdatedAt(new Date()); // ✅ Fix added
            return ticketRepo.save(ticket);
        }
        return null;
    }

    @Override
    public SupportTicket updateStatus(Long ticketId, SupportTicket.Status status) {
        Optional<SupportTicket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isPresent()) {
            SupportTicket ticket = ticketOpt.get();
            ticket.setStatus(status);
            ticket.setUpdatedAt(new Date()); // ✅ Fix added
            return ticketRepo.save(ticket);
        }
        return null;
    }

}
