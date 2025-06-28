package com.mysite.currencyexchange.dto;

public class CurrencyPairDto {
    private CurrencyResponseDto baseCurrency;
    private CurrencyResponseDto targetCurrency;

    public CurrencyPairDto(CurrencyResponseDto baseCurrency, CurrencyResponseDto targetCurrency) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
    }

    public CurrencyPairDto() {
    }

    public CurrencyResponseDto getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyResponseDto baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public CurrencyResponseDto getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(CurrencyResponseDto targetCurrency) {
        this.targetCurrency = targetCurrency;
    }
}
