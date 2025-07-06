package com.banquito.core.general.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.banquito.core.general.modelo.Pais;
import com.banquito.core.general.dto.PaisDTO;

@Mapper(componentModel = "spring")
public interface PaisMapper {
    PaisMapper INSTANCE = Mappers.getMapper(PaisMapper.class);

    @Mapping(source = "moneda.codigoMoneda", target = "codigoMoneda")
    PaisDTO toDTO(Pais pais);
    Pais toEntity(PaisDTO paisDTO);
    
    void updateFromDTO(PaisDTO paisDTO, @MappingTarget Pais pais);
} 