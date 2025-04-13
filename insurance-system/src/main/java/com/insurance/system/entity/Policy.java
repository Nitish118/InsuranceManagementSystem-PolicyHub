package com.insurance.system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "policies")
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POLICY_ID")
    private Long policyId;

    @Column(name = "POLICY_NAME")
    private String policyName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PREMIUM_AMOUNT")
    private double premium;

    @Column(name = "POLICY_TYPE")
    private String policyType;

    @Column(name = "COVERAGE_AMOUNT")
    private double coverageAmount;

    @Column(name = "TERM_YEARS")
    private int termYears;

    @Column(name = "CREATED_AT")
    private java.sql.Date createdAt;

    // Getters and Setters

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPremium() {
        return premium;
    }

    public void setPremium(double premium) {
        this.premium = premium;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public double getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(double coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    public int getTermYears() {
        return termYears;
    }

    public void setTermYears(int termYears) {
        this.termYears = termYears;
    }

    public java.sql.Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.sql.Date createdAt) {
        this.createdAt = createdAt;
    }
}
