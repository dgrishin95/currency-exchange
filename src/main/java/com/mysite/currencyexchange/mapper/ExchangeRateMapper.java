package com.mysite.currencyexchange.mapper;

import com.mysite.currencyexchange.dto.CurrencyPairDto;
import com.mysite.currencyexchange.dto.CurrencyResponseDto;
import com.mysite.currencyexchange.dto.ExchangeRateResponseDto;
import com.mysite.currencyexchange.dto.RawExchangeRateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExchangeRateMapper {
    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    @Mapping(target = "currencyPairDto", expression = "java( mapCurrencyPair(rawDto) )")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "rate", source = "rate")
    ExchangeRateResponseDto toExchangeRateResponseDto(RawExchangeRateDto rawDto);

    default CurrencyPairDto mapCurrencyPair(RawExchangeRateDto rawDto) {
        CurrencyResponseDto baseCurrencyResponseDto =
                new CurrencyResponseDto(
                        rawDto.getBaseId(),
                        rawDto.getBaseCode(),
                        rawDto.getBaseName(),
                        rawDto.getBaseSign()
                );

        CurrencyResponseDto targetCurrencyResponseDto =
                new CurrencyResponseDto(
                        rawDto.getTargetId(),
                        rawDto.getTargetCode(),
                        rawDto.getTargetName(),
                        rawDto.getTargetSign()
                );

        return new CurrencyPairDto(baseCurrencyResponseDto, targetCurrencyResponseDto);
    }
}

