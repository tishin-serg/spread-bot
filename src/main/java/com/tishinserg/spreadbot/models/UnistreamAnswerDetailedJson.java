package com.tishinserg.spreadbot.models;

import lombok.Data;

@Data
public class UnistreamAnswerDetailedJson {
    private String name;
    private Double acceptedAmount;
    private String acceptedCurrency;
    private Double withdrawAmount;
    private String withdrawCurrency;
    private Double rate;
    private Double acceptedTotalFee;
    private String acceptedTotalFeeCurrency;
}
