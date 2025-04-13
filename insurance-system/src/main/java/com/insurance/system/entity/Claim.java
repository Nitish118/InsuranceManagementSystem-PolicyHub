package com.insurance.system.entity;

import jakarta.persistence.*;
import java.util.Date;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "claims")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long claimId;

    @ManyToOne(optional = true)
    @JoinColumn(name = "user_policy_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private UserPolicy userPolicy;


    private String claimReason;

    private Double claimAmount;

    private String claimStatus = "SUBMITTED";

    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewedAt;

    // Getters and Setters
    public Long getClaimId() {
        return claimId;
    }

    public void setClaimId(Long claimId) {
        this.claimId = claimId;
    }

    public UserPolicy getUserPolicy() {
        return userPolicy;
    }

    public void setUserPolicy(UserPolicy userPolicy) {
        this.userPolicy = userPolicy;
    }

    public String getClaimReason() {
        return claimReason;
    }

    public void setClaimReason(String claimReason) {
        this.claimReason = claimReason;
    }

    public Double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(Double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Date getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Date reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}
