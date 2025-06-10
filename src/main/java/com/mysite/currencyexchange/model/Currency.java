package com.mysite.currencyexchange.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {
    private int id;
    private String code;
    private String fullName;
    private String sign;
}
