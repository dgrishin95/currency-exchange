package com.mysite.currencyexchange.dto;

import java.math.BigDecimal;

public class ExchangeRateResponseDto {
    private int id;
    private CurrencyPairDto currencyPairDto;
    private BigDecimal rate;

    public ExchangeRateResponseDto(int id, CurrencyPairDto currencyPairDto, BigDecimal rate) {
        this.id = id;
        this.currencyPairDto = currencyPairDto;
        this.rate = rate;
    }

    public ExchangeRateResponseDto(CurrencyPairDto currencyPairDto, BigDecimal rate) {
        this.currencyPairDto = currencyPairDto;
        this.rate = rate;
    }

    public ExchangeRateResponseDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
