package com.mysite.currencyexchange.mapper;

import com.mysite.currencyexchange.dto.CurrencyRequestDto;
import com.mysite.currencyexchange.dto.CurrencyResponseDto;
import com.mysite.currencyexchange.model.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    Currency toEntity(CurrencyResponseDto dto);
    CurrencyResponseDto toCurrencyResponseDto(Currency entity);

    Currency toEntity(CurrencyRequestDto dto);
    CurrencyRequestDto toCurrencyRequestDto(Currency entity);
}
