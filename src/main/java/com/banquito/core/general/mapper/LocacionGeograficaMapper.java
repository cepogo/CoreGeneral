package com.banquito.core.general.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.banquito.core.general.modelo.LocacionGeografica;
import com.banquito.core.general.dto.LocacionGeograficaDTO;
import com.banquito.core.general.dto.LocacionGeograficaCreacionDTO;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocacionGeograficaMapper {
    LocacionGeograficaMapper INSTANCE = Mappers.getMapper(LocacionGeograficaMapper.class);

    @ValueMapping(source = "MANTENIMIENTO", target = "INACTIVO")
    LocacionGeograficaDTO toDTO(LocacionGeografica locacionGeografica);

    LocacionGeografica toPersistence(LocacionGeograficaDTO dto);

    LocacionGeografica toEntity(LocacionGeograficaCreacionDTO dto);

    void updateFromEntity(LocacionGeografica source, @MappingTarget LocacionGeografica target);
    
    // Método para convertir a DTO embebido del modelo
    com.banquito.core.general.modelo.LocacionGeograficaDTO toEmbeddedDTO(LocacionGeografica locacion);
} 