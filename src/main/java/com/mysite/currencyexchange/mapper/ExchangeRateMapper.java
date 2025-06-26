package com.mysite.currencyexchange.mapper;

import com.mysite.currencyexchange.dto.CurrencyResponseDto;
import com.mysite.currencyexchange.dto.ExchangeRateResponseDto;
import com.mysite.currencyexchange.dto.RawExchangeRateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExchangeRateMapper {
    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    @Mapping(target = "baseCurrency", expression = "java( mapBaseCurrency(rawDto) )")
    @Mapping(target = "targetCurrency", expression = "java( mapTargetCurrency(rawDto) )")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "rate", source = "rate")
    ExchangeRateResponseDto toExchangeRateResponseDto(RawExchangeRateDto rawDto);

    default CurrencyResponseDto mapBaseCurrency(RawExchangeRateDto rawDto) {
        return new CurrencyResponseDto(
                rawDto.getBaseId(),
                rawDto.getBaseCode(),
                rawDto.getBaseName(),
                rawDto.getBaseSign()
        );
    }

    default CurrencyResponseDto mapTargetCurrency(RawExchangeRateDto rawDto) {
        return new CurrencyResponseDto(
                rawDto.getTargetId(),
                rawDto.getTargetCode(),
                rawDto.getTargetName(),
                rawDto.getTargetSign()
        );
    }
}

