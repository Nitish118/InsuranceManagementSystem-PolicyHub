// 📁 com.insurance.system.repository.SupportTicketRepository.java
package com.insurance.system.repository;

import com.insurance.system.entity.SupportTicket;
import com.insurance.system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByUser(User user);
    List<SupportTicket> findTop5ByOrderByCreatedAtDesc();
}
