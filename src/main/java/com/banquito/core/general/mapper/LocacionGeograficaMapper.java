package com.banquito.core.general.mapper;

import com.banquito.core.general.dto.LocacionGeograficaDTO;
import com.banquito.core.general.dto.LocacionGeograficaCreacionDTO;
import com.banquito.core.general.modelo.LocacionGeografica;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocacionGeograficaMapper {

    @ValueMapping(source = "MANTENIMIENTO", target = "INACTIVO")
    LocacionGeograficaDTO toDTO(LocacionGeografica locacionGeografica);

    LocacionGeografica toPersistence(LocacionGeograficaDTO dto);

    LocacionGeografica toEntity(LocacionGeograficaCreacionDTO dto);
} 