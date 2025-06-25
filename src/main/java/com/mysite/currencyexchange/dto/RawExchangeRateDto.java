package com.mysite.currencyexchange.dto;

import java.math.BigDecimal;

public class RawExchangeRateDto {
    private int id;
    private int baseId;
    private String baseCode;
    private String baseName;
    private String baseSign;
    private int targetId;
    private String targetCode;
    private String targetName;
    private String targetSign;
    private BigDecimal rate;

    public RawExchangeRateDto(int id, int baseId, String baseCode, String baseName, String baseSign,
                              int targetId, String targetCode, String targetName, String targetSign,
                              BigDecimal rate) {
        this.id = id;
        this.baseId = baseId;
        this.baseCode = baseCode;
        this.baseName = baseName;
        this.baseSign = baseSign;
        this.targetId = targetId;
        this.targetCode = targetCode;
        this.targetName = targetName;
        this.targetSign = targetSign;
        this.rate = rate;
    }

    public RawExchangeRateDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBaseId() {
        return baseId;
    }

    public void setBaseId(int baseId) {
        this.baseId = baseId;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public void setBaseCode(String baseCode) {
        this.baseCode = baseCode;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getBaseSign() {
        return baseSign;
    }

    public void setBaseSign(String baseSign) {
        this.baseSign = baseSign;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetSign() {
        return targetSign;
    }

    public void setTargetSign(String targetSign) {
        this.targetSign = targetSign;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
