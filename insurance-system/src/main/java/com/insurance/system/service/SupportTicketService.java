// 📁 com.insurance.system.service.SupportTicketService.java
package com.insurance.system.service;

import com.insurance.system.entity.SupportTicket;

import java.util.List;

public interface SupportTicketService {
    SupportTicket createTicket(Long userId, String subject, String message);
    List<SupportTicket> getUserTickets(Long userId);
    List<SupportTicket> getAllTickets();
    SupportTicket respondToTicket(Long ticketId, String response);
    SupportTicket updateStatus(Long ticketId, SupportTicket.Status status);
}
