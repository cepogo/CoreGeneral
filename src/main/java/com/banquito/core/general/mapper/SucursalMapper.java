package com.banquito.core.general.mapper;

import com.banquito.core.general.dto.SucursalCreacionDTO;
import com.banquito.core.general.dto.SucursalDTO;
import com.banquito.core.general.dto.SucursalUpdateDTO;
import com.banquito.core.general.modelo.Sucursal;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SucursalMapper {
    SucursalDTO toDTO(Sucursal sucursal);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "locacionGeografica", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "version", ignore = true)
    Sucursal toEntity(SucursalCreacionDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDTO(SucursalUpdateDTO dto, @MappingTarget Sucursal sucursal);
}