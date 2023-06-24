package com.tishinserg.spreadbot.models.binance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Getter
@Setter
public class BinanceRequest {
    private Boolean proMerchantAds; // false
    private Integer page; // 1
    private Integer rows; // 10
    private String[] payTypes; // = new String[] {"TinkoffNew"};
    private String[] countries; // = new String[] {};
    private String publisherType; // null
    private String asset; // USDT
    private String fiat; // RUB
    private String tradeType; // SELL

    public BinanceRequest(String bank, String asset, String fiat, String tradeType) {
        this.proMerchantAds = false;
        this.page = 1;
        this.rows = 10;
        this.payTypes = new String[]{bank};
        this.countries = new String[]{};
        this.publisherType = null;
        this.asset = asset;
        this.fiat = fiat;
        this.tradeType = tradeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinanceRequest that = (BinanceRequest) o;
        return Objects.equals(proMerchantAds, that.proMerchantAds) &&
                Objects.equals(page, that.page) &&
                Objects.equals(rows, that.rows) &&
                Arrays.equals(payTypes, that.payTypes) &&
                Arrays.equals(countries, that.countries) &&
                Objects.equals(publisherType, that.publisherType) &&
                Objects.equals(asset, that.asset) &&
                Objects.equals(fiat, that.fiat) &&
                Objects.equals(tradeType, that.tradeType);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(proMerchantAds, page, rows, publisherType, asset, fiat, tradeType);
        result = 31 * result + Arrays.hashCode(payTypes);
        result = 31 * result + Arrays.hashCode(countries);
        return result;
    }
}
