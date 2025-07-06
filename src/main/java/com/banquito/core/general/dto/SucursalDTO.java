package com.banquito.core.general.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class SucursalDTO {

    private String codigoSucursal;
    private String codigoEntidadBancaria;
    private LocacionGeograficaDTO locacionGeografica;
    private String nombre;
    private LocalDate fechaCreacion;
    private String correoElectronico;
    private String telefono;
    private String direccionLinea1;
    private String direccionLinea2;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private String estado;
}
