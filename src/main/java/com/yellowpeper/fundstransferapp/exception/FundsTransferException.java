package com.yellowpeper.fundstransferapp.exception;

import com.yellowpeper.fundstransferapp.constants.FundsTransferConstants;
import com.yellowpeper.fundstransferapp.entity.FundsTransferResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class FundsTransferException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<FundsTransferResponse> handleException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
            .body(new FundsTransferResponse(FundsTransferConstants.ERROR, Arrays.asList(FundsTransferConstants.INTERNAL_SERVER_ERROR), null));
    }

    @ExceptionHandler(value = AccountNotFoundException.class)
    public ResponseEntity<FundsTransferResponse> handleAccountNotFoundException(AccountNotFoundException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
            .body(new FundsTransferResponse(FundsTransferConstants.ERROR, Arrays.asList(ex.getMessage()), null));
    }

    @ExceptionHandler(value = BalanceOriginNegativeException.class)
    public ResponseEntity<FundsTransferResponse> handleBalanceNegativeException(BalanceOriginNegativeException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON)
            .body(new FundsTransferResponse(FundsTransferConstants.ERROR, Arrays.asList(ex.getMessage()), null));
    }

    @ExceptionHandler(value = MaxNumberOfTransactionsException.class)
    public ResponseEntity<FundsTransferResponse> handleMaxLimitException(MaxNumberOfTransactionsException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON)
            .body(new FundsTransferResponse(FundsTransferConstants.ERROR, Arrays.asList(ex.getMessage()), null));
    }
}
