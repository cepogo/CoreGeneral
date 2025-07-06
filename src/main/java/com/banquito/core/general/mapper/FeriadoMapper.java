package com.banquito.core.general.mapper;

import com.banquito.core.general.dto.FeriadoCreacionDTO;
import com.banquito.core.general.dto.FeriadoDTO;
import com.banquito.core.general.modelo.Feriado;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeriadoMapper {

    FeriadoDTO toDTO(Feriado feriado);


    Feriado toEntity(FeriadoCreacionDTO dto);

} 