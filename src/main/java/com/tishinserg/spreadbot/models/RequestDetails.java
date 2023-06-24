package com.tishinserg.spreadbot.models;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
@Builder
public class RequestDetails {
    @Column(name = "request_details_country")
    private String country;
    @Column(name = "request_details_payment_method")
    private String paymentMethod;
    @Column(name = "request_details_trade_type")
    private String tradeType;

    public RequestDetails(String country) {
        this.country = country;
    }

    public RequestDetails(String paymentMethod, String tradeType) {
        this.paymentMethod = paymentMethod;
        this.tradeType = tradeType;
    }

    public RequestDetails(String country, String paymentMethod, String tradeType) {
        this.country = country;
        this.paymentMethod = paymentMethod;
        this.tradeType = tradeType;
    }

    public RequestDetails() {
    }
}
