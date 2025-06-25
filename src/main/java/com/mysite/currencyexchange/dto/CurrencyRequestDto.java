package com.mysite.currencyexchange.dto;

public class CurrencyRequestDto {
    private int id;
    private String code;
    private String name;
    private String sign;

    public CurrencyRequestDto(String code, String name, String sign) {
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    public CurrencyRequestDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
