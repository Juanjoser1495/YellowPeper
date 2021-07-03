package com.yellowpeper.fundstransferapp.service;

import com.yellowpeper.fundstransferapp.entity.AccountEntity;
import com.yellowpeper.fundstransferapp.exception.AccountNotFoundException;

import java.util.Map;

public interface AccountService {

    AccountEntity getAccountBalance(String accountNumber);

    Map<String,Object> transferMoney(String numberAccountOrigin, String numberAccountDestiny, Double amount, String description, String currency) throws AccountNotFoundException;
}
