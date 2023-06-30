package com.tishinserg.spreadbot.models.koronapay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Objects;

@Data
public class KoronaAnswerModel {

    private SendingCurrency sendingCurrency;
    private long sendingAmount;
    private long sendingAmountDiscount;
    private long sendingAmountWithoutCommission;
    private long sendingCommission;
    private long sendingCommissionDiscount;
    private long sendingTransferCommission;
    private long paidNotificationCommission;
    private ReceivingCurrency receivingCurrency;
    private long receivingAmount;

    @JsonProperty("exchangeRate")
    private Double exchangeRate;

    private String exchangeRateType;
    private long exchangeRateDiscount;
    private long profit;
    private Properties properties;

    // Геттеры и сеттеры для всех полей

    public static class SendingCurrency {
        private String id;
        private String code;
        private String name;

        // Геттеры и сеттеры для всех полей
    }

    public static class ReceivingCurrency {
        private String id;
        private String code;
        private String name;

        // Геттеры и сеттеры для всех полей
    }

    public static class Properties {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KoronaAnswerModel that = (KoronaAnswerModel) o;
        return sendingAmount == that.sendingAmount &&
                sendingAmountDiscount == that.sendingAmountDiscount &&
                sendingAmountWithoutCommission == that.sendingAmountWithoutCommission &&
                sendingCommission == that.sendingCommission &&
                sendingCommissionDiscount == that.sendingCommissionDiscount &&
                sendingTransferCommission == that.sendingTransferCommission &&
                paidNotificationCommission == that.paidNotificationCommission &&
                receivingAmount == that.receivingAmount &&
                exchangeRateDiscount == that.exchangeRateDiscount &&
                profit == that.profit &&
                Objects.equals(sendingCurrency, that.sendingCurrency) &&
                Objects.equals(receivingCurrency, that.receivingCurrency) &&
                Objects.equals(exchangeRate, that.exchangeRate) &&
                Objects.equals(exchangeRateType, that.exchangeRateType) &&
                Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sendingCurrency, sendingAmount, sendingAmountDiscount, sendingAmountWithoutCommission, sendingCommission, sendingCommissionDiscount, sendingTransferCommission, paidNotificationCommission, receivingCurrency, receivingAmount, exchangeRate, exchangeRateType, exchangeRateDiscount, profit, properties);
    }
}
