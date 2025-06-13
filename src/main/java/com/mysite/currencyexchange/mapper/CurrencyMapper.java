package com.mysite.currencyexchange.mapper;

import com.mysite.currencyexchange.dto.CurrencyDto;
import com.mysite.currencyexchange.model.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    Currency toEntity(CurrencyDto dto);
    CurrencyDto toDto(Currency entity);
}
