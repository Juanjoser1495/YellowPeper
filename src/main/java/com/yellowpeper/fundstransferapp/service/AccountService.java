package com.yellowpeper.fundstransferapp.service;

import com.yellowpeper.fundstransferapp.entity.AccountEntity;
import com.yellowpeper.fundstransferapp.exception.AccountNotFoundException;

public interface AccountService {

    AccountEntity getAccountBalance(String accountNumber);

    boolean transferMoney(String numberAccountOrigin, String numberAccountDestiny, Double amount, String description) throws AccountNotFoundException;
}
