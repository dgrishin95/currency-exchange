package com.mysite.currencyexchange.dto;

import java.math.BigDecimal;

public class ExchangeDto {
    private CurrencyPairDto currencyPairDto;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;

    public ExchangeDto(CurrencyPairDto currencyPairDto, BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) {
        this.currencyPairDto = currencyPairDto;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public ExchangeDto() {
    }

    public CurrencyPairDto getCurrencyPairDto() {
        return currencyPairDto;
    }

    public void setCurrencyPairDto(CurrencyPairDto currencyPairDto) {
        this.currencyPairDto = currencyPairDto;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }
}
