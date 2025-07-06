package com.banquito.core.general.mapper;

import com.banquito.core.general.dto.EntidadBancariaCreacionDTO;
import com.banquito.core.general.dto.EntidadBancariaDTO;
import com.banquito.core.general.modelo.EntidadBancaria;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {MonedaMapper.class})
public interface EntidadBancariaMapper {
    EntidadBancariaDTO toDTO(EntidadBancaria entidad);
    EntidadBancaria toEntity(EntidadBancariaCreacionDTO dto);
}
