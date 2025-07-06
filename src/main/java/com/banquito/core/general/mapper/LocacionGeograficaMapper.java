package com.banquito.core.general.mapper;

import com.banquito.core.general.dto.*;
import com.banquito.core.general.modelo.LocacionGeografica;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocacionGeograficaMapper {
    
    @Mapping(target = "codigoProvincia", source = "provincia")
    @Mapping(target = "codigoCanton", source = "canton")
    @Mapping(target = "codigoParroquia", source = "parroquia")
    LocacionGeograficaDTO toDTO(LocacionGeografica locacion);
    
    @Mapping(target = "codigoProvincia", source = "provincia")
    @Mapping(target = "codigoCanton", source = "canton")
    @Mapping(target = "codigoParroquia", source = "parroquia")
    LocacionGeograficaDTO toEmbeddedDTO(LocacionGeografica locacion);
    
    @Mapping(target = "provincia", source = "codigoProvincia")
    @Mapping(target = "canton", source = "codigoCanton")
    @Mapping(target = "parroquia", source = "codigoParroquia")
    LocacionGeografica toEntity(LocacionGeograficaDTO dto);

    LocacionGeografica toEntity(LocacionGeograficaCreacionDTO dto);
    
    // Métodos para DTOs específicos por nivel
    ProvinciaDTO toProvinciaDTO(LocacionGeografica locacion);
    
    CantonDTO toCantonDTO(LocacionGeografica locacion);
    
    ParroquiaDTO toParroquiaDTO(LocacionGeografica locacion);
} 