package com.insurance.system.entity;

import jakarta.persistence.*;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "user_policies")
public class UserPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_policy_seq")
    @SequenceGenerator(name = "user_policy_seq", sequenceName = "USER_POLICY_SEQ", allocationSize = 1)
    @Column(name = "user_policy_id") // 💡 This is the actual column name in your DB
    private Long userPolicyId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "purchase_date")
    private Date purchaseDate = new Date();

    // Getters and Setters
    public Long getUserPolicyId() {
        return userPolicyId;
    }

    public void setUserPolicyId(Long userPolicyId) {
        this.userPolicyId = userPolicyId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
