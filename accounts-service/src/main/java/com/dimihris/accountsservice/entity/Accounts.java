package com.dimihris.accountsservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "accounts")
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class Accounts extends BaseEntity{

    @Column(name = "customer_id", updatable = false)
    private Long customerId;

    @Id
    @Column(name = "account_number", updatable = false)
    private Long accountNumber;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "branch_address")
    private String branchAddress;
}
