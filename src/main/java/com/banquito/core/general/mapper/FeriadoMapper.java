package com.banquito.core.general.mapper;

import com.banquito.core.general.dto.FeriadoCreacionDTO;
import com.banquito.core.general.dto.FeriadoDTO;
import com.banquito.core.general.dto.FeriadoUpdateDTO;
import com.banquito.core.general.modelo.Feriado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = { LocacionGeograficaMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeriadoMapper {

    FeriadoDTO toDTO(Feriado feriado);

    Feriado toPersistence(FeriadoDTO dto);

    @Mapping(target = "locacion", ignore = true)
    Feriado toEntity(FeriadoCreacionDTO dto);

    void updateFromDTO(FeriadoUpdateDTO dto, @MappingTarget Feriado feriado);
} 