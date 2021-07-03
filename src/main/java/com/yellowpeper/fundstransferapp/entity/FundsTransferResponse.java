package com.yellowpeper.fundstransferapp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundsTransferResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double account_balance;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double tax_collected;

    public FundsTransferResponse(String status, List<String> errors){
        this.status = status;
        this.errors = errors;
    }

    public FundsTransferResponse(String status,List<String> errors, Double account_balance) {
        this.status = status;
        this.errors = errors;
        this.account_balance = account_balance;
    }
}
