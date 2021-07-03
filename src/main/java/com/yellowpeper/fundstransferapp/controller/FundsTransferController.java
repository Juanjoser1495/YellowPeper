package com.yellowpeper.fundstransferapp.controller;

import com.yellowpeper.fundstransferapp.constants.FundsTransferConstants;
import com.yellowpeper.fundstransferapp.entity.AccountEntity;
import com.yellowpeper.fundstransferapp.entity.FundsTransferResponse;
import com.yellowpeper.fundstransferapp.entity.TransferMoneyRequest;
import com.yellowpeper.fundstransferapp.exception.AccountNotFoundException;
import com.yellowpeper.fundstransferapp.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/fundstransfer")
public class FundsTransferController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FundsTransferController.class);

    private final AccountService accountService;

    @Autowired
    public FundsTransferController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = "/balance/{accountNumber}")
    public ResponseEntity<FundsTransferResponse> getAccountBalance(@PathVariable("accountNumber") String accountNumber) {
        LOGGER.info("Entering the method getAccountBalance from FundsTransferController");
        AccountEntity response = accountService.getAccountBalance(accountNumber);
        if (response != null) {
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(new FundsTransferResponse(FundsTransferConstants.OK, Collections.emptyList(), response.getBalance()));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body(new FundsTransferResponse(FundsTransferConstants.ERROR, Arrays.asList(FundsTransferConstants.INTERNAL_SERVER_ERROR)));
        }
    }

    @PostMapping(value = "/transfer")
    public ResponseEntity<FundsTransferResponse> transferMoney(@RequestBody TransferMoneyRequest request) throws AccountNotFoundException {
        LOGGER.info("Entering the method transferMoney from FundsTransferController");
        Map<String,Object> response = accountService.transferMoney(request.getOriginAccount(), request.getDestinyAccount(), request.getAmount(),
                                                                   request.getDescription(),request.getCurrency());
        if (!response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(new FundsTransferResponse(FundsTransferConstants.OK,Collections.emptyList(),null,Double.parseDouble(response.get("tax").toString())));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body(new FundsTransferResponse(FundsTransferConstants.ERROR, Arrays.asList(FundsTransferConstants.ACCOUNT_NOT_FOUNDED)));
        }
    }
}
