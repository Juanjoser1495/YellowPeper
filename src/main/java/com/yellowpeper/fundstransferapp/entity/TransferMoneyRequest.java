package com.yellowpeper.fundstransferapp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransferMoneyRequest {

    private Double amount;
    private String currency;
    @JsonProperty("origin_account")
    private String originAccount;
    @JsonProperty("destination_account")
    private String destinyAccount;
    private String description;
}
