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
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
    public Map<String,Object> transferMoney(String numberAccountOrigin, String numberAccountDestiny, Double amount,
                                            String description, String currency) throws AccountNotFoundException {
        LOGGER.info("Entering the method getAccountBalance from AccountServiceImpl with account number origin {}, destiny {} and amount {}", numberAccountOrigin, numberAccountDestiny, amount);
        Map<String,Object> response =  new HashMap<>();
        AccountEntity accountOrigin = accountDataRepository.findByAccountNumber(numberAccountOrigin);
        AccountEntity accountDestiny = accountDataRepository.findByAccountNumber(numberAccountDestiny);
        int numberOfTransactions = transactionDataRepository.countForAccountOriginAndDate(numberAccountOrigin);
        if (numberOfTransactions < 3) {
            if (accountDestiny == null || accountOrigin == null) {
                LOGGER.error("One of the account doesn't exists");
                throw new AccountNotFoundException(FundsTransferConstants.ACCOUNT_NOT_FOUNDED);
            }

            RestTemplate restTemplate =  new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofSeconds(30)).build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://apilayer.net/api/live")
                .queryParam("access_key", "890465a9ccb6ace02e98ec64a7f4eb74")
                .queryParam("currencies", currency)
                .queryParam("source", "USD")
                .queryParam("format","1");

            ResponseEntity<Map> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, Map.class);

            LinkedHashMap<String,Object> mapRate = (LinkedHashMap<String, Object>) result.getBody().get("quotes");
            Double quantityUsd = amount / Double.parseDouble(mapRate.get("USD"+currency).toString());
            Double tax = quantityUsd > 100 ? quantityUsd * 0.005 : quantityUsd * 0.002;
            Double balanceOrigin = accountOrigin.getBalance() - quantityUsd - tax;
            if (balanceOrigin < 0) {
                LOGGER.error("The account balance origin can't be negative");
                throw new BalanceOriginNegativeException(FundsTransferConstants.INSUFFICIENT_FOUNDS);
            }

            TransactionEntity transaction = new TransactionEntity(accountOrigin, accountDestiny, description, amount, tax, new Date());
            transactionDataRepository.save(transaction);

            accountOrigin.setBalance(accountOrigin.getBalance() - quantityUsd - tax);
            accountDestiny.setBalance(accountDestiny.getBalance() + quantityUsd);
            accountDataRepository.save(accountDestiny);
            response.put("tax", tax);
            LOGGER.info("Everything was ok, founds transferred");
        } else {
            LOGGER.error("Error limit of transactions reached");
            throw new MaxNumberOfTransactionsException(FundsTransferConstants.MAX_NUMBER_OF_TRANSACTIONS);
        }
        return response;
    }
}
