package com.yellowpeper.fundstransferapp.service.internal;

import com.yellowpeper.fundstransferapp.constants.FundsTransferConstants;
import com.yellowpeper.fundstransferapp.entity.AccountEntity;
import com.yellowpeper.fundstransferapp.entity.TransactionEntity;
import com.yellowpeper.fundstransferapp.exception.AccountNotFoundException;
import com.yellowpeper.fundstransferapp.exception.BalanceOriginNegativeException;
import com.yellowpeper.fundstransferapp.exception.MaxNumberOfTransactionsException;
import com.yellowpeper.fundstransferapp.repository.AccountDataRepository;
import com.yellowpeper.fundstransferapp.repository.TransactionDataRepository;
import com.yellowpeper.fundstransferapp.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountDataRepository accountDataRepository;
    private final TransactionDataRepository transactionDataRepository;

    @Autowired
    public AccountServiceImpl(AccountDataRepository accountDataRepository, TransactionDataRepository transactionDataRepository) {
        this.accountDataRepository = accountDataRepository;
        this.transactionDataRepository = transactionDataRepository;
    }

    @Override
    public AccountEntity getAccountBalance(String accountNumber) {
        LOGGER.info("Entering the method getAccountBalance from AccountServiceImpl with account number {}", accountNumber);
        AccountEntity account = accountDataRepository.findByAccountNumber(accountNumber);
        LOGGER.info("Account founded {}", account);

        if (account == null) {
            throw new AccountNotFoundException(FundsTransferConstants.ACCOUNT_NOT_FOUNDED);
        }

        return account;
    }

    @Override
    public boolean transferMoney(String numberAccountOrigin, String numberAccountDestiny, Double amount, String description) throws AccountNotFoundException {
        LOGGER.info("Entering the method getAccountBalance from AccountServiceImpl with account number origin {}, destiny {} and amount {}", numberAccountOrigin, numberAccountDestiny, amount);
        AccountEntity accountOrigin = accountDataRepository.findByAccountNumber(numberAccountOrigin);
        AccountEntity accountDestiny = accountDataRepository.findByAccountNumber(numberAccountDestiny);
        int numberOfTransactions = transactionDataRepository.countForAccountOriginAndDate(numberAccountOrigin);
        if (numberOfTransactions < 3) {
            if (accountDestiny == null || accountOrigin == null) {
                LOGGER.error("One of the account doesn't exists");
                throw new AccountNotFoundException(FundsTransferConstants.ACCOUNT_NOT_FOUNDED);
            }


            Double tax = amount > 100 ? amount * 0.005 : amount * 0.002;
            Double balanceOrigin = accountOrigin.getBalance() - amount - tax;
            if (balanceOrigin < 0) {
                LOGGER.error("The account balance origin can't be negative");
                throw new BalanceOriginNegativeException(FundsTransferConstants.INSUFFICIENT_FOUNDS);
            }

            TransactionEntity transaction = new TransactionEntity(accountOrigin, accountDestiny, description, amount, tax, new Date());
            transactionDataRepository.save(transaction);

            accountOrigin.setBalance(accountOrigin.getBalance() - amount - tax);
            accountDestiny.setBalance(accountDestiny.getBalance() + amount);
            accountDataRepository.save(accountDestiny);
            LOGGER.info("Everything was ok, founds transferred");
        } else {
            LOGGER.error("Error limit of transactions reached");
            throw new MaxNumberOfTransactionsException(FundsTransferConstants.MAX_NUMBER_OF_TRANSACTIONS);
        }
        return false;
    }
}
