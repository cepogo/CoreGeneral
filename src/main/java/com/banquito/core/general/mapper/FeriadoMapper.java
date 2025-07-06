package com.banquito.core.general.mapper;

import com.banquito.core.general.dto.FeriadoDTO;
import com.banquito.core.general.dto.FeriadoCreacionDTO;
import com.banquito.core.general.modelo.Feriado;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FeriadoMapper {
    
    FeriadoDTO toDTO(Feriado feriado);
    
    Feriado toEntity(FeriadoCreacionDTO dto);
} 