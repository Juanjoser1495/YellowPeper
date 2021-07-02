package com.yellowpeper.fundstransferapp.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ACCOUNTS")
@Data
public class AccountEntity {

    @Id
    @Column(name = "id_account")
    private int idAccount;

    @Column(name ="account_number")
    private String accountNumber;

    @Column(name ="balance")
    private Double balance;

    @Column(name ="currency")
    private String currency;
}
