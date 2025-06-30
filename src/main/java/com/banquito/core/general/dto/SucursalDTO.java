package com.banquito.core.general.dto;

import com.banquito.core.general.enums.EstadoSucursalesEnum;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class SucursalDTO {
    private String id;
    private String entidadBancariaId;
    private String locacionId;
    private String codigo;
    private String nombre;
    private LocalDate fechaCreacion;
    private String correoElectronico;
    private String telefono;
    private String direccionLinea1;
    private String direccionLinea2;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private EstadoSucursalesEnum estado;
}
