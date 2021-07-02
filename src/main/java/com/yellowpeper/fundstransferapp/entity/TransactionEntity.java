package com.yellowpeper.fundstransferapp.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "TRANSACTIONS")
@Data
@SequenceGenerator(name = "Transaction_id_seq", initialValue = 1)
public class TransactionEntity {

    @Id
    @Column(name = "id_transaction")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Transaction_id_seq")
    private int idTransaction;

    @ManyToOne
    @JoinColumn(name = "account_origin")
    private AccountEntity accountOrigin;

    @ManyToOne
    @JoinColumn(name = "account_destiny")
    private AccountEntity accountDestiny;

    @Column(name = "description")
    private String description;

    @Column(name = "amount_transaction")
    private Double amountTransaction;

    @Column(name = "tax_transaction")
    private Double taxTransaction;

    @Column(name = "date_transaction")
    private Date dateTransaction;

    public TransactionEntity(AccountEntity accountOrigin, AccountEntity accountDestiny, String description, Double amountTransaction, Double taxTransaction, Date date) {
        this.accountOrigin = accountOrigin;
        this.accountDestiny = accountDestiny;
        this.description = description;
        this.amountTransaction = amountTransaction;
        this.taxTransaction = taxTransaction;
        this.dateTransaction = date;
    }
}
