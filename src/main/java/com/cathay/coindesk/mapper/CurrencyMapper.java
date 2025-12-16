package com.cathay.coindesk.mapper;

import com.cathay.coindesk.model.bo.CurrencyBo;
import com.cathay.coindesk.model.dto.CurrencyDto;
import com.cathay.coindesk.model.entity.Currency;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    // DTO <-> BO
    CurrencyBo toBo(CurrencyDto dto);

    CurrencyDto toDto(CurrencyBo bo);

    // BO <-> Entity
    Currency toEntity(CurrencyBo bo);

    CurrencyBo toBo(Currency entity);
}
