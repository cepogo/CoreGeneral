package com.banquito.core.general.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.banquito.core.general.modelo.Moneda;
import com.banquito.core.general.dto.MonedaDTO;

@Mapper(componentModel = "spring", uses = {PaisMapper.class})
public interface MonedaMapper {
    MonedaMapper INSTANCE = Mappers.getMapper(MonedaMapper.class);

    MonedaDTO toDTO(Moneda moneda);
    Moneda toEntity(MonedaDTO monedaDTO);
    
    void updateFromDTO(MonedaDTO monedaDTO, @MappingTarget Moneda moneda);
} 